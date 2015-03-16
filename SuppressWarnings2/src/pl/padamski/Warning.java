package pl.padamski;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Warning {

	enum Type {
		UNUSED_METHOD("The method.*from the type .* is never used locally"), OTHER(".*");

		String regex;

		Type(String regex) {
			this.regex = regex;
		}
	}

	Type type;
	Path path;
	int lineNo;

	public Warning(String line) {
		String[] columns = line.split("\t");

		for (Type t : Type.values()) {
			if (columns[0].matches(t.regex)) {
				type = t;
				break;
			}
		}

		path = Paths.get(columns[2].substring(1), columns[1]);
		lineNo = Integer.parseInt(columns[3].substring(5));

	}
}
