package pl.padamski;

import java.nio.file.Path;

public class Annotation {
    Path path;
    String text;
    int line;

    public Annotation(Path p, String text, int line) {
        super();
        this.path = p;
        this.text = text;
        this.line = line;
    }

    public Path getPath() {
        return path;
    }

    public String getText() {
        return text;
    }

    public int getLine() {
        return line;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + line;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Annotation other = (Annotation) obj;
        if (line != other.line)
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

}
