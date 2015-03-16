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

public class SuppressWarnings {

	private static final String SUPPRESS_UNUSED_METHOD = "\t@SuppressWarnings(\"unused\")";

	public static void main(String[] args) throws IOException {
		new SuppressWarnings().foo(Paths.get("C:\\blad.txt"), Charset.defaultCharset());
	}

	private void foo(Path path, Charset charset) throws IOException {
		Stream<String> lines = Files.lines(path, charset);
		Map<Path, List<Warning>> warnings = lines.skip(1).map(n -> new Warning(n)).filter(n -> n.type.equals(Warning.Type.UNUSED_METHOD))
				.collect(Collectors.groupingBy(n -> n.path));
		lines.close();

		warnings.forEach((x, y) -> addAnnotations(x, y, charset));
	}

	private Object addAnnotations(Path path, List<Warning> warnings, Charset charset) {
		try {
			List<String> lines = Files.readAllLines(path, charset);

			for (int i = 0; i < warnings.size(); i++) {
				lines.add(warnings.get(i).lineNo - 1, SUPPRESS_UNUSED_METHOD);
				for (int j = i + 1; j < warnings.size(); j++) {
					warnings.get(j).lineNo++;
				}
			}

			Files.write(path, lines, charset);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}

}
