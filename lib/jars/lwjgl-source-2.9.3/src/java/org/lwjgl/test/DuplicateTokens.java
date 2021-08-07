package org.lwjgl.test;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DuplicateTokens {

	private static Pattern CORE;

	private DuplicateTokens() {
	}

	public static void main(String[] args) {
		File path = new File("bin/" + args[0].replace('.', '/'));

		Map<String, Class> tokens = new HashMap<String, Class>(256);

		CORE = Pattern.compile(args[1] + "[0-9]*");

		checkFiles(tokens, filterPath(path, Pattern.compile(CORE.pattern() + "\\.class")), args[0]);
		checkFiles(tokens, filterPath(path, Pattern.compile("((?!" + CORE.pattern() + ")[^.])+\\.class")), args[0]);
	}

	private static File[] filterPath(File path, final Pattern pattern) {
		File[] files = path.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pattern.matcher(pathname.getName()).matches();
			}
		});

		Arrays.sort(files, new Comparator<File>() {
			public int compare(final File o1, final File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		return files;
	}

	private static void checkFiles(Map<String, Class> tokens, File[] files, String root) {
		try {
			for ( File file : files ) {
				Class c = Class.forName(root + '.' + file.getName().substring(0, file.getName().length() - ".class".length()));
				for ( Field f : c.getFields() ) {
					int mod = f.getModifiers();
					if ( !Modifier.isStatic(mod) )
						continue;

					if ( tokens.containsKey(f.getName()) ) {
						Class src = tokens.get(f.getName());
						if ( CORE.matcher(src.getSimpleName()).matches() || CORE.matcher(c.getSimpleName()).matches() )
							System.out.println("DUPLICATE: " + f.getName() + " at " + c.getSimpleName() + " [" + src.getSimpleName() + ']');
					} else
						tokens.put(f.getName(), c);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}