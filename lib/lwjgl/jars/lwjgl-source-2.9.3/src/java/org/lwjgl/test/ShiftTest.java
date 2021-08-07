package org.lwjgl.test;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class ShiftTest {

	public static final int SCREEN_WIDTH  = 640;
	public static final int SCREEN_HEIGHT = 480;

	public static void main(String[] args) throws Exception {
		setDisplayMode();
		Display.setTitle("Shift Test");
		Display.setFullscreen(false);
		Display.setVSyncEnabled(true);
		Display.create(new PixelFormat(32, 0, 24, 8, 0));
		Mouse.setGrabbed(false);

		boolean lcurr = false;
		boolean rcurr = false;
		while ( true ) {

			boolean lShiftDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
			boolean rShiftDown = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

			if ( lcurr != lShiftDown || rcurr != rShiftDown ) {
				System.out.println(String.format("LShift: %b\tRShift: %b", lShiftDown, rShiftDown));
				lcurr = lShiftDown;
				rcurr = rShiftDown;
			}

			if ( Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) ) {
				break;
			}
			Display.processMessages();
			Display.sync(60);
		}
	}

	private static void setDisplayMode() throws Exception {
		DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(SCREEN_WIDTH, SCREEN_HEIGHT, -1, -1, -1, -1, 60, 60);
		org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
			"width=" + SCREEN_WIDTH,
			"height=" + SCREEN_HEIGHT,
			"freq=" + 60,
			"bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
		});
	}
}