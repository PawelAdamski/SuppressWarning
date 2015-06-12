package pl.padamski;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Warning {

    enum Type {
        // @formatter:off
        UNUSED_METHOD("The method.*from the type .* is never used locally", new AddLineAbove(), "unused"),
        UNUSED_FIELD("The value of the field .* is not used",new AddLineAbove(), "unused"),
        UNUSED_LOCAL("The value of the local variable .* is not used", new AddLineAbove(), "unused"), 
        RAW_TYPE(".* is a raw type.*", new AddToMethod(), "rawtypes"), 
        STATIC_METHOD(".* should be accessed in a static way", new AddToMethod(), "static-access"),
        DEPRECTATION("The method .* from the type .* is deprecated", new AddToMethod(), "deprecation"),
        TYPE_SAFETY("Type safety:.*", new AddToMethod(), "unchecked"),
        DEAD_CODE("Dead code",new AddToMethod(), "unused"),
        NULL_ACESS("Null pointer access.*",new AddToMethod(),"null"),
        RESOURCE_LEAK("Resource leak.*",new AddToMethod(),"resource"),
        ENUM_CONSTAN("The enum constant.*",new AddToMethod(),"incomplete-switch"),
        USE_SERIALIZABE("The serializable class  does not declare a static final.*",new AddToClass(),"serial"),
        SERIALIZABE("The serializable class ....* does not declare a static final.*",new AddToClass(),"serial"),
        OTHER("other", null, "");
     // @formatter:on

        String regex;
        FindAnnotationLocation annotationLocation;
        String annotationMessage;

        Type(String regex, FindAnnotationLocation annotationLocation, String annotationMessage) {
            this.regex = regex;
            this.annotationLocation = annotationLocation;
            this.annotationMessage = annotationMessage;
        }
    }

    Type type;
    Path path;
    int lineNo;

    public Warning(String line) {
        String[] columns = line.split("\t");
        type = Type.OTHER;
        for (Type t : Type.values()) {
            if (columns[0].matches(t.regex)) {
                type = t;
                break;
            }
        }

        path = Paths.get(columns[2].substring(1), columns[1]);
        lineNo = Integer.parseInt(columns[3].substring(5));

    }

    Annotation generateAnnotation() {
        return new Annotation(path, type.annotationMessage, type.annotationLocation.findLocation(lineNo, path));

    }
}
