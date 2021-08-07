package org.lwjgl.test.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.metal.MetalLookAndFeel;

import static org.lwjgl.opengl.GL11.*;

public class CanvasFocusTest extends JFrame {

	private static final long serialVersionUID = 2853176700479564421L;
	private JMenuBar menuBar;
	private Canvas   canvas;
	private Thread   gameThread;

	private boolean running;

	private JInternalFrame createDebugFrame() {

		JInternalFrame internalFrame = new JInternalFrame("Debug Frame");
		internalFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		internalFrame.setSize(400, 100);
		internalFrame.setVisible(true);

		JButton focus = new JButton("Focus");
		focus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.setFocusable(true);
			}
		});
		internalFrame.add(focus);

		JButton unfocus = new JButton("Unfocus");
		unfocus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.setFocusable(false);
			}
		});

		internalFrame.add(unfocus);

		JButton metal = new JButton("Metal");
		metal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switchMetal();
			}
		});

		internalFrame.add(metal);

		JButton substance = new JButton("Substance");
		substance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switchSubstance();
			}
		});

		internalFrame.add(substance);
		return internalFrame;
	}

	private JInternalFrame createGameFrame() {
		final JInternalFrame canvasFrame = new JInternalFrame("Game Frame");
		canvasFrame.setSize(800, 600);
		canvasFrame.setLocation(80, 80);
		canvasFrame.setVisible(true);

		canvas = new Canvas() {
			@Override
			public void addNotify() {
				super.addNotify();
				startLWJGL();
			}

			@Override
			public void removeNotify() {
				stopLWJGL();
				super.removeNotify();
			}
		};

		canvasFrame.add(canvas);
		canvas.setSize(canvasFrame.getContentPane().getSize());
		canvas.setFocusable(true);
		canvas.requestFocus();
		canvas.setIgnoreRepaint(true);

		canvasFrame.addInternalFrameListener(new InternalFrameListener() {
			public void internalFrameOpened(InternalFrameEvent e) {
				SwingUtilities.getWindowAncestor(canvasFrame).validate();
			}

			public void internalFrameClosing(InternalFrameEvent e) {
				SwingUtilities.getWindowAncestor(canvasFrame).validate();
			}

			public void internalFrameClosed(InternalFrameEvent e) {
				SwingUtilities.getWindowAncestor(canvasFrame).validate();
			}

			public void internalFrameIconified(InternalFrameEvent e) {
				SwingUtilities.getWindowAncestor(canvasFrame).validate();
			}

			public void internalFrameDeiconified(InternalFrameEvent e) {
				SwingUtilities.getWindowAncestor(canvasFrame).validate();
			}

			public void internalFrameActivated(InternalFrameEvent e) {
				SwingUtilities.getWindowAncestor(canvasFrame).validate();
			}

			public void internalFrameDeactivated(InternalFrameEvent e) {
				SwingUtilities.getWindowAncestor(canvasFrame).validate();
			}
		});
		canvasFrame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				SwingUtilities.getWindowAncestor(canvasFrame).validate();
			}

			public void componentMoved(ComponentEvent e) {
				SwingUtilities.getWindowAncestor(canvasFrame).validate();

			}
		});

		Thread thread = new Thread() {
			@Override
			public void run() {
				while ( true ) {
					while ( !Mouse.isCreated() ) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					while ( Mouse.next() ) {
						int wheelD = Mouse.getDWheel();
						if ( wheelD != 0 ) {
							System.out.println("" + wheelD);
						}
					}
				}
			}
		};
		thread.start();

		return canvasFrame;
	}

	protected void gameLoop() {
		glClearColor(1f, 0f, 1f, 0f);

		Boolean active = null;
		while ( running ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			Display.update();
			Display.sync(60);

			boolean a = Display.isActive();
			if ( active == null || active != a ) {
				System.out.println("Diplay.isActive() = " + a + " - " +canvas.isFocusOwner());
				active = a;
			}
		}

		Display.destroy();
	}

	public void init() {

	}

	protected void initGL() {
	}

	private void loadSwing() {

		switchSubstance();

		menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		file.add(new JMenuItem("Item 1"));
		file.add(new JMenuItem("Item 2"));
		file.add(new JMenuItem("Item 3"));
		menuBar.add(file);

		setJMenuBar(menuBar);

		JDesktopPane desktop = new JDesktopPane();
		this.add(desktop);

		JInternalFrame canvasFrame = createGameFrame();
		desktop.add(canvasFrame);

		JInternalFrame debugFrame = createDebugFrame();
		desktop.add(debugFrame);

		this.setSize(1024, 768);

		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void start() {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				loadSwing();
			}
		});

	}

	public void startLWJGL() {
		gameThread = new Thread() {

			@Override
			public void run() {
				running = true;
				try {
					Display.setParent(canvas);
					Display.create();
					initGL();
				} catch (LWJGLException e) {
					e.printStackTrace();
					return;
				}
				gameLoop();
			}
		};
		gameThread.start();
	}

	private void stopLWJGL() {
		Display.destroy();
	}

	protected void switchMetal() {
		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
			UIManager.getLookAndFeelDefaults().put("ClassLoader", getClass().getClassLoader());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	protected void switchSubstance() {
			UIManager.getLookAndFeelDefaults().put("ClassLoader", getClass().getClassLoader());
			SwingUtilities.updateComponentTreeUI(this);
	}

	public static void main(String[] args) {
		new CanvasFocusTest().start();
	}

}