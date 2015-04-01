package pl.padamski;

import java.nio.file.Path;

public interface FindAnnotationLocation {
    public int findLocation(int warningLine, Path p);
}
