import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import pl.padamski.AddAnnotation;
import pl.padamski.Annotation;

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
		lines = addAnnotation.addAnnotations(lines, annotations);
		assertThat(lines, Matchers.contains("@SuppressWarnings(\"unused\")", "a", "b", "c"));
	}

	@Test
	public void addAnnotationInEveryLine() {
		annotations.add(new Annotation(null, "anon1", 1));
		annotations.add(new Annotation(null, "anon2", 2));
		annotations.add(new Annotation(null, "anon3", 3));
		lines = addAnnotation.addAnnotations(lines, annotations);
		assertThat(lines,
				Matchers.contains("@SuppressWarnings(\"anon1\")", "a", "@SuppressWarnings(\"anon2\")", "b", "@SuppressWarnings(\"anon3\")", "c"));
	}

	@Test
	public void twoAnnotationInOneLine() {
		annotations.add(new Annotation(null, "anon1", 1));
		annotations.add(new Annotation(null, "anon2", 1));
		lines = addAnnotation.addAnnotations(lines, annotations);
		assertThat(lines, Matchers.contains("@SuppressWarnings({\"anon1\", \"anon2\"})", "a", "b", "c"));
	}

	@Test
	public void addToExistingAnnotation() {
		lines.clear();
		lines.add("@SuppressWarnings(\"anon1\")");
		lines.add("@SomeAnnotation");
		lines.add("a");
		annotations.add(new Annotation(null, "anon2", 3));
		assertThat(lines, Matchers.contains("@SuppressWarnings({\"anon1\", \"anon2\"})", "@SomeAnnotation", "a"));
	}

	@Test
	public void theSameAnnotationAlreadyExists() {
		lines.clear();
		lines.add("@SuppressWarnings(\"anon1\")");
		lines.add("@SomeAnnotation");
		lines.add("a");
		annotations.add(new Annotation(null, "anon1", 3));
		assertThat(lines, Matchers.contains("@SuppressWarnings({\"anon1\"})", "@SomeAnnotation", "a"));
	}
}
