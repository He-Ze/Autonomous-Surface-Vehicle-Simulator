package org.lwjgl.test.opengl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 *
 */
public class CrashTest {

	/**
	 * The main method.
	 *
	 * @param args command-line arguments (ignored)
	 */
	public static void main(String[] args) {
		try {

			// native library loading
			/*System.setProperty("java.library.path", "lib/native/macosx");
			Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);*/

			// initialize LWJGL
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.create(new PixelFormat(), new ContextAttribs(4, 2).withProfileCompatibility(true).withDebug(true));
			Mouse.create();
			Mouse.setGrabbed(true);
			Mouse.poll();
			Keyboard.create();

			AMDDebugOutput.glDebugMessageCallbackAMD(new AMDDebugOutputCallback());

			// initialize OpenGL
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();

			// clear screen
			glDepthMask(true);
			glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			// prepare data (24 quads, 4 vertices each, 2 floats each, 4 bytes each)
			IntBuffer buffer = ByteBuffer.allocateDirect(24 * 4 * 2 * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
			for ( int i = 0; i < 6; i++ ) {
				for ( int j = 0; j < 4; j++ ) {
					buffer.put(2 * i);
					buffer.put(2 * j);
					buffer.put(2 * i + 1);
					buffer.put(2 * j);
					buffer.put(2 * i + 1);
					buffer.put(2 * j + 1);
					buffer.put(2 * i);
					buffer.put(2 * j + 1);
				}
			}

			// prepare OpenGL buffer
			int arrayBufferName = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, arrayBufferName);
			//buffer.rewind(); // <-- forget this and it crashes!
			glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

			// set up OpenGL state
			glScalef(0.25f, 1.0f / 3.0f, 1.0f);
			glVertexPointer(2, GL_INT, 0, 0);
			glEnableClientState(GL_VERTEX_ARRAY);

			// draw the frame
			glDrawArrays(GL_TRIANGLES, 0, 24 * 4);
			glFlush();
			Display.swapBuffers();

			// wait for keypress
			while ( !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) ) {
				Keyboard.poll();
			}

			// clean up
			Keyboard.destroy();
			Mouse.destroy();
			Display.destroy();

		} catch (Throwable e) {
			System.err.println("got an exception");
			throw new RuntimeException(e);
		}
	}

}
