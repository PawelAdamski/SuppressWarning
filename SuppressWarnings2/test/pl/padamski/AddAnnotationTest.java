package pl.padamski;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class AddAnnotationTest {
    AddAnnotation addAnnotation = new AddAnnotation();
    List<String> lines = new ArrayList<String>();
    List<Annotation> annotations = new ArrayList<Annotation>();

    @Before
    public void setUp() {
        lines.add("a");
        lines.add("b");
        lines.add("c");
    }

    @Test
    public void addSingleAnnotation() {
        annotations.add(new Annotation(null, "unused", 1));
        TextChanges changes = addAnnotation.addAnnotations(lines, annotations);
        //assertThat(lines, Matchers.contains("@SuppressWarnings(\"unused\")", "a", "b", "c"));
    }

    @Test
    public void addAnnotationInEveryLine() {
        annotations.add(new Annotation(null, "anon1", 1));
        annotations.add(new Annotation(null, "anon2", 2));
        annotations.add(new Annotation(null, "anon3", 3));
        TextChanges changes = addAnnotation.addAnnotations(lines, annotations);
        //assertThat(lines, Matchers.contains("@SuppressWarnings(\"anon1\")", "a", "@SuppressWarnings(\"anon2\")", "b", "@SuppressWarnings(\"anon3\")", "c"));
    }

    @Test
    public void twoAnnotationInOneLine() {
        annotations.add(new Annotation(null, "anon1", 1));
        annotations.add(new Annotation(null, "anon2", 1));
        TextChanges changes = addAnnotation.addAnnotations(lines, annotations);
        //assertThat(lines, Matchers.contains("@SuppressWarnings({\"anon1\", \"anon2\"})", "a", "b", "c"));
    }

    @Test
    public void addAnnotationToAnnotation() {
        lines.clear();
        lines.add("@SuppressWarnings(\"anon1\")");
        lines.add("@SomeAnnotation");
        lines.add("a");
        annotations.add(new Annotation(null, "anon2", 3));
        TextChanges changes = addAnnotation.addAnnotations(lines, annotations);
        assertThat(changes.linesToReplace, hasSize(1));
        assertThat(changes.linesToAdd, hasSize(0));
    }

    @Test
    public void addToExistingAnnotation() {
        lines.clear();
        lines.add("@SuppressWarnings(\"anon1\")");
        lines.add("@SomeAnnotation");
        lines.add("a");
        annotations.add(new Annotation(null, "anon2", 3));
        TextChanges changes = addAnnotation.addAnnotations(lines, annotations);
        assertThat(changes.linesToReplace, hasSize(1));
        assertThat(changes.linesToAdd, hasSize(0));
    }

    @Test
    public void addToExistingAnnotations() {
        lines.clear();
        lines.add("@SuppressWarnings({ \"unchecked\", \"unused\" })");
        lines.add("@SomeAnnotation");
        lines.add("a");
        annotations.add(new Annotation(null, "anon2", 3));
        TextChanges changes = addAnnotation.addAnnotations(lines, annotations);
        assertThat(changes.linesToReplace, hasSize(1));
        assertThat(changes.linesToAdd, hasSize(0));
        Set<String> annons = AddAnnotation.parseAnnotation(changes.linesToReplace.get(0).text);
        assertThat(annons, containsInAnyOrder("anon2", "unchecked", "unused"));
        assertThat(annons, hasSize(3));
    }

    @Test
    public void addToExistingAnnotationsForward() {
        lines.clear();
        lines.add("@SomeAnnotaion");
        lines.add("@SuppressWarnings({ \"unchecked\", \"unused\" })");
        lines.add("@SomeAnnotation");
        lines.add("a");
        annotations.add(new Annotation(null, "anon2", 1));
        TextChanges changes = addAnnotation.addAnnotations(lines, annotations);
        assertThat(changes.linesToReplace, hasSize(1));
        assertThat(changes.linesToAdd, hasSize(0));
        Set<String> annons = AddAnnotation.parseAnnotation(changes.linesToReplace.get(0).text);
        assertThat(annons, containsInAnyOrder("anon2", "unchecked", "unused"));
        assertThat(annons, hasSize(3));
    }

    @Test
    public void theSameAnnotationAlreadyExists() {
        lines.clear();
        lines.add("@SuppressWarnings(\"anon1\")");
        lines.add("@SomeAnnotation");
        lines.add("a");
        annotations.add(new Annotation(null, "anon1", 3));
        TextChanges changes = addAnnotation.addAnnotations(lines, annotations);
        Set<String> annon = AddAnnotation.parseAnnotation(changes.linesToReplace.get(0).text);
        assertThat(annon, contains("anon1"));
        assertThat(annon, hasSize(1));
    }
}