package pl.padamski;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AddAnnotation {
    public List<String> addAnnotations(List<String> lines, List<Annotation> annotations) {
        annotations = annotations.parallelStream().distinct().collect(Collectors.toList());
        Collections.sort(annotations, (Annotation a1, Annotation a2) -> a1.line - a2.line);
        List<Annotation> distAnnon = new ArrayList<Annotation>();
        for (int i = 0; i < annotations.size(); i++) {
            Annotation a1 = annotations.get(i);
            String val = a1.text;
            boolean moreThatOne = false;
            for (int j = i + 1; j < annotations.size() && annotations.get(j).line == a1.line; j++) {
                val += "\", \"" + annotations.get(j).text;
                moreThatOne = true;
            }
            if (moreThatOne) {
                val = "{ " + val + " }";
            }
            distAnnon.add(new Annotation(null, val, a1.line));

        }

        for (int i = 0; i < annotations.size(); i++) {
            Annotation a = annotations.get(i);
            String indent = getIndent(lines.get(a.line));
            lines.add(a.line, indent + "@SuppressWarnings(" + a.text + ")");
            for (int j = i + 1; j < annotations.size(); j++) {
                if (a.line <= annotations.get(j).line) {
                    annotations.get(j).line++;
                }
            }
        }

        return lines;
    }

    private String getIndent(String line) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length() && line.charAt(i) == ' '; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }
}
