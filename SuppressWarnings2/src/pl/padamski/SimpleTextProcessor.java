package pl.padamski;

import java.util.List;

public class SimpleTextProcessor {

    public List<String> process(List<String> lines, TextChanges textChanges) {

        for (int i = 0; i < textChanges.linesToAdd.size(); i++) {
            Change a = textChanges.linesToAdd.get(i);
            String indent = getIndent(lines.get(a.lineNumber - 1));
            lines.add(a.line - 1, indent + "@SuppressWarnings(" + a.text + ")");
            for (int j = i + 1; j < distAnnon.size(); j++) {
                if (a.line <= distAnnon.get(j).line) {
                    distAnnon.get(j).line++;
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
