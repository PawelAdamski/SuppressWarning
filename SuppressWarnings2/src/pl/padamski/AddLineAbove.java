package pl.padamski;

import java.nio.file.Path;

public class AddLineAbove implements FindAnnotationLocation {

    public int findLocation(int warningLine, Path p) {
        return warningLine - 1;
    }

}
