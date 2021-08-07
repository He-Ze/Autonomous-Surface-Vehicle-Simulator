package org.lwjgl.test.opengl;

import org.lwjgl.opengl.*;

public class WindowLocationTest {
	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setLocation(0, 0);
			Display.create();
			Display.setLocation(0, 0);

			for ( int i = 0; i < 5; i++ ) {
				int x = Display.getX();
				int y = Display.getY();

				System.out.println("x=" + x + ", y=" + y);

				Display.setLocation(x, y);
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Display.destroy();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}