package org.lwjgl.test;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Test {
	public static void main(String[] args) throws Exception {
		Canvas canvas = new Canvas();
		JFrame frame = new JFrame("Test");
		frame.add(canvas);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(640, 480));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		Display.setParent(canvas);
		Display.setVSyncEnabled(true);
		Display.create();
		while ( !Display.isCloseRequested() ) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			Display.update();
			// Press and release ALT on Windows, the printing stops until you press a key or click.
			//System.out.println(Mouse.getX() + ", " + Mouse.getY() + " - " +System.currentTimeMillis());
			while ( Keyboard.next() ) {
				System.out.println(Keyboard.getKeyName(Keyboard.getEventKey()) + " - " +Keyboard.getEventKeyState());
			}
		}
	}
}