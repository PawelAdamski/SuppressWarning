package pl.padamski;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SuppressWarnings {

	public static void main(String[] args) throws IOException {

		new SuppressWarnings().foo(Paths.get("C:\\blad.txt"), Charset.defaultCharset());
	}

	private void foo(Path path, Charset charset) throws IOException {
		Stream<String> lines = Files.lines(path, charset);

		List<Warning> liczby = lines.map(n -> new Warning(n)).collect(Collectors.toList());
		System.out.println(liczby.toString());
		lines.close();
	}

}
