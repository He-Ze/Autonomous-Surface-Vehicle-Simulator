package org.lwjgl.test.opengl.pbuffers;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;

public class MSAATest {

	public MSAATest() {

	}

	public static void main(String[] args) {
		try {
			new Pbuffer(64, 64, new PixelFormat(32, 8, 24, 8, 4), null);
		} catch (LWJGLException e) {
			throw new RuntimeException(e);
		}
		System.out.println("done");
	}

}