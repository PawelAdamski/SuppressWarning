package pl.padamski;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AddAnnotation {
    public TextChanges addAnnotations(List<String> lines, List<Annotation> annotations) {
        annotations = annotations.parallelStream().distinct().collect(Collectors.toList());
        Collections.sort(annotations, (Annotation a1, Annotation a2) -> a1.line - a2.line);
        List<Annotation> distAnnon = new ArrayList<Annotation>();
        for (int i = 0; i < annotations.size(); i++) {
            Annotation a1 = annotations.get(i);
            String val = a1.text;
            boolean moreThatOne = false;
            int j;
            for (j = i + 1; j < annotations.size() && annotations.get(j).line == a1.line; j++) {
                val += "\", \"" + annotations.get(j).text;
                moreThatOne = true;
            }
            i = j - 1;
            val = "\"" + val + "\"";
            if (moreThatOne) {
                val = "{ " + val + " }";
            }
            distAnnon.add(new Annotation(null, val, a1.line));

        }

        return null;
    }

}
