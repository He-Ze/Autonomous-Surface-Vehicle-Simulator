package org.lwjgl.test.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

public class JNIPerfTestLWJGL {

	private JNIPerfTestLWJGL() {
	}

	public static void main(String[] args) {
		try {
			Display.setFullscreen(false);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		int DURATION = 1024 * 1024;
		int WARMUP = 5 * DURATION;
		int BENCH = 10 * DURATION;

		benchmark(WARMUP, BENCH);

		//BufferUtils.noop4(0.0f, 0.0f, 0.0f, 0.0f);
	}

	private static void benchmark(int WARMUP, int BENCH) {
		IntBuffer buffer = BufferUtils.createIntBuffer(16);

		int sum = 0;
		sum += benchLoop(buffer, WARMUP);

		System.out.println("WARMUP DONE");

		long time = System.nanoTime();
		sum += benchLoop(buffer, BENCH);
		time = (System.nanoTime() - time) / BENCH;

		System.out.println(time);
		if ( sum == Integer.MIN_VALUE )
			throw new RuntimeException();
	}

	private static int benchLoop(IntBuffer buffer, int duration) {
		int sum = 0;
		for ( int i = 0; i < duration; i++ ) {
			//glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS, buffer); // 18
			//sum += buffer.get(0);
			//glDepthMask(true); // 12
			//glColor4f(1.0f, 1.0f, 1.0f, 0.0f); // 13

			//glColor3f(1.0f, 1.0f, 1.0f); // 16
			//BufferUtils.noop0(); // 7
			//BufferUtils.noop0f(0L); // 8
			//BufferUtils.noop3(1, 2, 3); // 8
			//BufferUtils.noop4f(1.0f, 1.0f, 1.0f, 0.0f, 0L); // 8
		}
		return sum;
	}

}