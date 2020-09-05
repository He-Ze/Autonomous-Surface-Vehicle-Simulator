package blindmystics.util;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * Created by alec on 28/06/16.
 */
public class Display {

    public static void setVSyncEnabled(boolean value) {
        org.lwjgl.opengl.Display.setVSyncEnabled(value);
    }

    public static void setDisplayMode(DisplayMode mode) throws EnvironmentException {
        try {
            org.lwjgl.opengl.Display.setDisplayMode(mode);
        } catch (LWJGLException e) {
            throw new EnvironmentException(e);
        }
    }

    public static void setTitle(String title) {
        org.lwjgl.opengl.Display.setTitle(title);
    }

    public static void setFullscreen(boolean value) throws EnvironmentException {
        try {
            org.lwjgl.opengl.Display.setFullscreen(value);
        } catch (LWJGLException e) {
            throw new EnvironmentException(e);
        }
    }

    public static void create(PixelFormat format) throws EnvironmentException {
        try {
            org.lwjgl.opengl.Display.create(format);
        } catch (LWJGLException e) {
            throw new EnvironmentException(e);
        }
    }

    public static DisplayMode getDesktopDisplayMode() {
        return org.lwjgl.opengl.Display.getDesktopDisplayMode();
    }

    public static boolean isCloseRequested() {
        return org.lwjgl.opengl.Display.isCloseRequested();
    }

    public static void update() {
        org.lwjgl.opengl.Display.update();
    }

    public static void sync(int frameratecap) {
        org.lwjgl.opengl.Display.sync(frameratecap);
    }

    public static void destroy() {
        org.lwjgl.opengl.Display.destroy();
    }

    public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
        return org.lwjgl.opengl.Display.getAvailableDisplayModes();
    }
}
