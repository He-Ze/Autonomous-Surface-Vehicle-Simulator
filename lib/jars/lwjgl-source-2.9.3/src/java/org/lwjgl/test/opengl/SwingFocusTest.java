package org.lwjgl.test.opengl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

/**
 * <p>
 * </p>
 *
 * @author Brian Matzon <brian@matzon.dk>
 */
public class SwingFocusTest {

	private JFrame frame;
	private final JPanel    panel1   = new JPanel();
	private final JPanel    panel2   = new JPanel();
	private final JTextPane textPane = new JTextPane();

	/** The Canvas where the LWJGL Display is added */
	Canvas display_parent;

	/** Thread which runs the main game loop */
	Thread gameThread;

	/** is the game loop running */
	boolean running;

	public SwingFocusTest() {
	}

	/**
	 * Once the Canvas is created its add notify method will call this method to
	 * start the LWJGL Display and game loop in another thread.
	 */
	public void startLWJGL() {
		gameThread = new Thread() {
			public void run() {
				running = true;
				try {
					Display.setParent(display_parent);
					Display.setVSyncEnabled(true);
					Display.create();
					initGL();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				gameLoop();
			}
		};
		gameThread.start();
	}

	protected void gameLoop() {
		while ( running ) {
			//System.out.println("--GAMELOOP--");

			Display.processMessages();

			if ( Display.isDirty() ) {
				if ( Keyboard.isKeyDown(Keyboard.KEY_SPACE) ) {
					glClearColor(1f, 0, 0, 0);
					System.out.println("Pigs in space!");
				} else {
					glClearColor(0f, 0f, 1f, 0f);
				}
				glClear(GL_COLOR_BUFFER_BIT);

				Display.update(false);
			}
		}
		Display.destroy();
	}

	protected void initGL() {
	}

	/**
	 * Tell game loop to stop running, after which the LWJGL Display will be destoryed.
	 * The main thread will wait for the Display.destroy() to complete
	 */
	private void stopLWJGL() {
		running = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void execute() {
		frame.setVisible(true);

		try {
			Display.setParent(display_parent);
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		initGL();

		running = true;

		while ( running ) {
			//System.out.println("--GAMELOOP--");

			Display.processMessages();

			if ( true ) {
				if ( Keyboard.isKeyDown(Keyboard.KEY_SPACE) ) {
					glClearColor(1f, 0, 0, 0);
					System.out.println("Pigs in space!");
				} else {
					glClearColor(0f, 0f, 1f, 0f);
				}
				glClear(GL_COLOR_BUFFER_BIT);

				Display.update(false);
			}
		}
		Display.destroy();
	}

	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.remove(display_parent);
				frame.dispose();
			}
		});
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();

		for ( int i = 0; i < 10; i++ ) {
			JMenu menu = new JMenu("Menu " + i);
			for ( int j = 0; j < 10; j++ ) {
				menu.add(new JMenuItem("Item " + j));
			}
			menuBar.add(menu);
		}
		frame.setJMenuBar(menuBar);

		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
		frame.getContentPane().add(panel1);
		panel1.setLayout(null);
		textPane.setBounds(10, 5, 124, 20);

		JPopupMenu contextMenu = new JPopupMenu("Edit");
		contextMenu.add(new JMenuItem("Save"));
		contextMenu.add(new JMenuItem("Save As"));
		contextMenu.add(new JMenuItem("Close"));

		textPane.setComponentPopupMenu(contextMenu);

		panel1.add(textPane);
		frame.getContentPane().add(panel2);
		panel2.setLayout(new BorderLayout(0, 0));

		display_parent = new Canvas() {
			/*public void addNotify() {
				super.addNotify();
				startLWJGL();
			}

			public void removeNotify() {
				stopLWJGL();
				super.removeNotify();
			}*/
		};
		display_parent.setFocusable(true);
		display_parent.requestFocus();
		display_parent.setIgnoreRepaint(true);
		panel2.add(display_parent);
	}

	public static void main(String[] args) {
		SwingFocusTest sit = new SwingFocusTest();
		sit.initialize();
		sit.execute();
	}
}