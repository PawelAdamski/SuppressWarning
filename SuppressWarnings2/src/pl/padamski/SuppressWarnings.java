package pl.padamski;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.padamski.Warning.Type;

public class SuppressWarnings {

    public static void main(String[] args) throws IOException {
        new SuppressWarnings().foo(Paths.get("D:\\bledy2.txt"), Charset.forName("Cp1250"));
        System.out.println("DONE");
    }

    private void foo(Path path, Charset charset) throws IOException {

        Stream<String> lines = Files.lines(path, charset);
        Map<Path, List<Annotation>> annotations = lines.skip(1)
                                                       .map(n -> new Warning(n))
                                                       .filter(w -> w.type != Type.OTHER)
                                                       .map(w -> w.generateAnnotation())
                                                       .collect(Collectors.groupingBy(Annotation::getPath));

        lines.close();

        annotations.forEach((x, y) -> addAnnotations(x, y, charset));
    }

    private Object addAnnotations(Path path, List<Annotation> annotations, Charset charset) {
        try {
            List<String> lines = Files.readAllLines(path, charset);
            TextChanges textChanges = new AddAnnotation().addAnnotations(lines, annotations);
            lines = new SimpleTextProcessor().process(lines, textChanges);
            Files.write(path, lines, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
