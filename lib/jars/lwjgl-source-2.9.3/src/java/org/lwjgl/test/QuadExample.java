package org.lwjgl.test;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class QuadExample {

    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800,600));
	        Display.setResizable(true);
	        Display.setVSyncEnabled(true);
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // init OpenGL
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 800, 0, 600, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        Mouse.setGrabbed(true);

        while (!Display.isCloseRequested()) {

            Display.sync(30);

            this.updateMouseVars();

	        while ( Keyboard.next() ) {
		        if ( Keyboard.getEventKeyState() == false && Keyboard.getEventKey() == Keyboard.KEY_G )
			        Mouse.setGrabbed(!Mouse.isGrabbed());
	        }

            // Clear the screen and depth buffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // set the color of the quad (R,G,B,A)
            glColor3f(0.5f, 0.5f, 1.0f);

            // draw quad
            glBegin(GL_QUADS);
            glVertex2f(mouseDx, mouseDy);
            glVertex2f(200 + mouseDx, mouseDy);
            glVertex2f(200 + mouseDx, 200 + mouseDy);
            glVertex2f(mouseDx, 200 + mouseDy);
            glEnd();

            Display.update();
        }

        Display.destroy();
    }

    int mouseDx = 0;
    int mouseDy = 0;

    public void updateMouseVars(){

        int localMouseDx = 0;
        int localMouseDy = 0;

	    int c=  0;
        while(Mouse.next()){

            localMouseDx += Mouse.getEventDX();
            localMouseDy += Mouse.getEventDY();
	        c++;

        }

        mouseDx += localMouseDx;
        mouseDy += localMouseDy;

    }

    public static void main(String[] argv) {
        QuadExample quadExample = new QuadExample();
        quadExample.start();
    }
}