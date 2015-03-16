package pl.padamski;

import java.nio.file.Path;

public class Warning {

	enum Type {
		UNUSED_METHOD("The method.*from the type .* is never used locally"), OTHER, HEADER("Description	Resource.*");

		String prefix;

		Type(String prefix) {
			this.prefix = prefix;
		}
	}

	Type type;
	Path f;
	int line;

	public Warning(String line) {
		// TODO Auto-generated constructor stub
	}

}
