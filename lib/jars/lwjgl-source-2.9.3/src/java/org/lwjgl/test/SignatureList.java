package org.lwjgl.test;

import org.lwjgl.opengl.GL11;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SignatureList {

	public SignatureList() {
	}

	public static void main(String[] args) {
		Class<?> clazz = GL11.class;

		final Method[] methods = clazz.getMethods();

		List<Method> sortedMethods = new ArrayList<Method>(methods.length);
		for ( final Method m : methods ) {
			sortedMethods.add(m);
		}

		Collections.sort(sortedMethods, new Comparator<Method>() {
			public int compare(final Method o1, final Method o2) {
				int cmp = o1.getName().compareTo(o2.getName());
				if ( cmp != 0 )
					return cmp;

				final Class<?>[] params1 = o1.getParameterTypes();
				final Class<?>[] params2 = o2.getParameterTypes();

				cmp = Integer.compare(params1.length, params2.length);
				if ( cmp != 0 ) return cmp;

				for ( int i = 0; i < params1.length; i++ ) {
					if ( params1[i] != params2[i] )
						return params1[i].getSimpleName().compareTo(params2[i].getSimpleName());
				}

				return 0;
			}
		});

		for ( final Method m : sortedMethods ) {
			if ( !m.getName().startsWith("gl") )
				continue;

			Class<?>[] params = m.getParameterTypes();

			System.out.print(m.getName() + "(");

			boolean first = true;
			for ( final Class<?> p : params ) {
				if ( first )
					first = false;
				else
					System.out.print(", ");
				System.out.print(p.getSimpleName());
			}
			System.out.println(")");
		}
	}

}