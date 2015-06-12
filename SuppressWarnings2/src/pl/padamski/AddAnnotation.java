package pl.padamski;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class AddAnnotation {
    public TextChanges addAnnotations(List<String> lines, List<Annotation> annotations) {
        List<AnnotationsInLine> annotationsInLines = mergeAnnotationsFromOneLine(annotations);
        TextChanges result = addOrReplace(annotationsInLines, lines);
        return result;
    }

    private TextChanges addOrReplace(List<AnnotationsInLine> annotationsInLines, List<String> lines) {
        TextChanges result = new TextChanges();
        for (AnnotationsInLine a : annotationsInLines) {
            Optional<Integer> previousAnnotationLineNumber = findPreviousAnnotation(a.lineNumber, lines);
            if (previousAnnotationLineNumber.isPresent()) {
                Set<String> t = parseAnnotation(lines.get(previousAnnotationLineNumber.get() - 1));
                a.annoations.addAll(t);
                Change change = new Change(a.toString(), previousAnnotationLineNumber.get());
                result.linesToReplace.add(change);
            } else {
                Change change = new Change(a.toString(), a.lineNumber);
                result.linesToAdd.add(change);
            }

        }
        return result;
    }

    public static Set<String> parseAnnotation(String text) {
        text = text.replaceAll(" ", "");
        text = text.replaceAll(".*\\(", "");
        text = text.replaceAll("\\).*", "");
        text = text.replaceAll(".*\\{", "");
        text = text.replaceAll("}.*", "");
        text = text.replaceAll("\"", "");
        String[] tab = text.split(",");
        return Sets.newHashSet(tab);
    }

    private Optional<Integer> findPreviousAnnotation(int startLineNumber, List<String> lines) {
        int lineNumber = startLineNumber;
        if (lineNumber > 0) {
            if (lines.get(lineNumber - 1).trim().startsWith("@SuppressWarnings")) {
                return Optional.of(lineNumber);
            }
        }
        lineNumber--;
        while (lineNumber > 0 && lines.get(lineNumber - 1).trim().startsWith("@")) {
            if (lines.get(lineNumber - 1).trim().startsWith("@SuppressWarnings")) {
                return Optional.of(lineNumber);
            }
            lineNumber--;
        }

        lineNumber = startLineNumber + 1;
        while (lineNumber < lines.size() && lines.get(lineNumber - 1).trim().startsWith("@")) {
            if (lines.get(lineNumber - 1).trim().startsWith("@SuppressWarnings")) {
                return Optional.of(lineNumber);
            }
            lineNumber++;
        }
        return Optional.empty();
    }

    private List<AnnotationsInLine> mergeAnnotationsFromOneLine(List<Annotation> annotations) {
        Collections.sort(annotations, (Annotation a1, Annotation a2) -> a1.line - a2.line);
        List<AnnotationsInLine> result = Lists.newArrayList();
        for (int i = 0; i < annotations.size(); i++) {
            Annotation a = annotations.get(i);
            AnnotationsInLine inLine = new AnnotationsInLine(a.line);
            inLine.annoations.add(a.text);
            for (int j = i + 1; j < annotations.size() && annotations.get(j).line == a.line; j++, i++) {
                inLine.annoations.add(annotations.get(j).text);
            }
            result.add(inLine);
        }
        return result;
    }
}


class AnnotationsInLine {
    Set<String> annoations = new HashSet<String>();
    int lineNumber;

    public AnnotationsInLine(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String toString() {
        String result = "@SuppressWarnings({";
        for (String a : annoations) {
            result += "\"" + a + "\",";
        }
        result = result.substring(0, result.length() - 1);
        result += "})";
        return result;
    }
}