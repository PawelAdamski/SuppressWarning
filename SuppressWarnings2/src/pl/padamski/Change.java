package pl.padamski;

public class Change {
    String text;
    int lineNumber;

    public Change(String text, int lineNumber) {
        this.text = text;
        this.lineNumber = lineNumber;
    }

    public String getText() {
        return text;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}