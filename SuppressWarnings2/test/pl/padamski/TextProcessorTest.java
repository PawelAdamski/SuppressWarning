package pl.padamski;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TextProcessorTest {

    private TextProcessor processor = new TextProcessor();
    private TextChanges textChanges;

    @Before
    public void setUp() {
        textChanges = new TextChanges();
    }

    @Test
    public void addLines() {
        textChanges.linesToAdd = Lists.newArrayList(new Change("add1", 1), new Change("add2", 2));
        List<String> input = Lists.newArrayList("one", "two", "three");
        List<String> output = processor.process(input, textChanges);

        assertThat(output, contains("add1", "one", "add2", "two", "three"));

    }

    @Test
    public void replaceLines() {
        textChanges.linesToReplace = Lists.newArrayList(new Change("add1", 1), new Change("add2", 2));
        List<String> input = Lists.newArrayList("one", "two", "three");
        List<String> output = processor.process(input, textChanges);

        assertThat(output, contains("add1", "add2", "three"));
    }

    @Test
    public void addAndReplaceLines() {
        textChanges.linesToAdd = Lists.newArrayList(new Change("add1", 1));
        textChanges.linesToReplace = Lists.newArrayList(new Change("add2", 2));
        List<String> input = Lists.newArrayList("one", "two", "three");
        List<String> output = processor.process(input, textChanges);

        assertThat(output, contains("add1", "one", "add2", "three"));

    }
}
