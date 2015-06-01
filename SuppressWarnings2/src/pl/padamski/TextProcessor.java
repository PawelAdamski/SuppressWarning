package pl.padamski;

import java.util.List;

public class TextProcessor {

    public List<String> process(List<String> lines, TextChanges textChanges) {

        replaceLines(lines, textChanges.linesToReplace);
        addLines(lines, textChanges.linesToAdd);
        return lines;
    }

    private void addLines(List<String> lines, List<Change> linesToAdd) {
        for (int i = 0; i < linesToAdd.size(); i++) {
            Change a = linesToAdd.get(i);
            lines.add(a.lineNumber - 1, a.text);
            for (int j = i + 1; j < linesToAdd.size(); j++) {
                if (a.lineNumber <= linesToAdd.get(j).lineNumber) {
                    linesToAdd.get(j).lineNumber++;
                }
            }
        }
    }

    private void replaceLines(List<String> lines, List<Change> linesToChange) {
        for (Change c : linesToChange) {
            lines.remove(c.lineNumber - 1);
            lines.add(c.lineNumber - 1, c.text);
        }
    }
}
