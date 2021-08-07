package org.lwjgl.test.input;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

/** @author Spasi */
public class ControlFail {

	public ControlFail() {

	}

	public static void main(String[] args) {
		Canvas canvas = new Canvas();
		JFrame frame = new JFrame("Test");

		MenuBar menu = new MenuBar();
		Menu file = new Menu("File");
		file.add(new MenuItem("New"));
		file.add(new MenuItem("Open"));
		file.add(new MenuItem("Save"));
		menu.add(file);

		frame.setMenuBar(menu);

		JComboBox combo = new JComboBox<String>(new String[] { "foo", "bar", "yo", "sakis" });
		combo.setLightWeightPopupEnabled(false);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(combo, BorderLayout.NORTH);
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		frame.getContentPane().add(new JTextField(32), BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(640, 480));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		canvas.setBackground(Color.RED);
		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(final MouseEvent e) {
				System.out.println("e = " + e);
			}
		});

		try {
			Display.setParent(canvas);
			Display.setVSyncEnabled(true);
			Display.create();
			//Mouse.setGrabbed(true);
			OUTER:
			while ( !Display.isCloseRequested() ) {
				float color = new Random().nextFloat();
				GL11.glClearColor(color, color, color, 0.0f);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				Display.update();

				/*if ( Display.isActive() && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) )
					System.out.println("control is up " + System.currentTimeMillis());*/

				while ( Keyboard.next() ) {
					int character_code = ((int)Keyboard.getEventCharacter()) & 0xffff;
					System.out.println("Checking key:" + Keyboard.getKeyName(Keyboard.getEventKey()));
					System.out.println("Pressed:" + Keyboard.getEventKeyState());
					System.out.println("Key character code: 0x" + Integer.toHexString(character_code));
					System.out.println("Key character: " + Keyboard.getEventCharacter());
					System.out.println("Repeat event: " + Keyboard.isRepeatEvent());

					if ( Keyboard.getEventKey() == Keyboard.KEY_G && Keyboard.getEventKeyState() ) {
						Mouse.setGrabbed(!Mouse.isGrabbed());
					}
					if ( Keyboard.getEventKey() == Keyboard.KEY_ESCAPE ) {
						break OUTER;
					}
				}

				//Display.sync(10);
			}

			Display.destroy();
			frame.dispose();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

}