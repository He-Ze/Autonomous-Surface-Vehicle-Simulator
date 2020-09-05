package gebd.games.boat.ui.topnav.resolution;

import blindmystics.util.Display;
import blindmystics.util.EnvironmentException;
import gebd.Render;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import renderables.r2D.composite.userinterface.scroll.DropdownMenu;
import renderables.r2D.composite.userinterface.scroll.DropdownMenuItem;

import javax.vecmath.Vector2f;

/**
 * Created by CaptainPete on 2016-11-11.
 */
public class ResolutionDropdown extends DropdownMenu {


    public ResolutionDropdown(String menuName, Vector2f relativePosition, Vector2f size, float rotation) {
        super(menuName, relativePosition, size, rotation);

        DisplayMode[] modes = null;
        try {
            modes = Display.getAvailableDisplayModes();
        } catch (LWJGLException e) {
            e.printStackTrace();
            return; //Don't allow for any different display modes.
        }

        //Add all of the display modes available.
        for (DisplayMode displayMode : modes) {
            addDropdownOption(createResolutionOption(displayMode));
        }

    }

    private DropdownMenuItem createResolutionOption(final DisplayMode displayMode){

        String name = displayMode.toString(); //TODO - Maybe change this.
        DropdownMenuItem result = new DropdownMenuItem(name) {
            @Override
            public void onSelect() {
                Render.instance.setDisplayMode(displayMode, false);
            }

            @Override
            public boolean enabled() {
                return true;
            }
        };

        return result;
    }

}
