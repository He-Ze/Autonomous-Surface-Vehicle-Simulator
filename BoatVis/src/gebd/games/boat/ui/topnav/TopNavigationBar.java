package gebd.games.boat.ui.topnav;

import blindmystics.input.CurrentInput;
import blindmystics.input.KeyboardInputLatchGenerator;
import blindmystics.util.input.InputLatch;
import gebd.Render;
import gebd.games.boat.BoatVis;
import gebd.games.boat.ui.topnav.colourseq.ColourSequenceDropdown;
import gebd.games.boat.ui.topnav.file.*;
import gebd.games.boat.ui.topnav.resolution.ResolutionDropdown;
import org.lwjgl.input.Keyboard;
import javax.vecmath.Vector2f;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.composite.userinterface.scroll.DropdownMenu;
import renderables.r2D.simple.SimpleQuad;

/**
 * Created by P3TE on 4/08/2016.
 */
public class TopNavigationBar extends UserInterface {

    private static float BAR_HEIGHT = 35; //px.

    private SimpleQuad backgroundQuad;
    private DropdownMenu fileMenu;
    private ResolutionDropdown resolutionDropdown;
    private ColourSequenceDropdown colourSequenceDropdown;

    private static final Vector2f DROPDOWN_MENU_SIZE = new Vector2f(100, BAR_HEIGHT);

    private int menuItems = 0;

    private BoatVis boatVis;

    private InputLatch fKeyHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_ESCAPE);

    public TopNavigationBar(BoatVis boatVis) {
        super(new Vector2f(), new Vector2f(), 0);
        this.boatVis = boatVis;
        this.relativePosition = new Vector2f(Render.getCentreOfScreen().x, Render.HEIGHT - (BAR_HEIGHT) /2f);
        this.size = new Vector2f(Render.WIDTH, BAR_HEIGHT);

        backgroundQuad = addComponentToTop(new SimpleQuad(new Vector2f(), size, 0));
        backgroundQuad.setBlend(0.6f, 0.8f, 0.8f, 1f, 1f);

        fileMenu = addComponentToTop(new DropdownMenu("File (Esc)", getNextMenuItemPosition(), DROPDOWN_MENU_SIZE, 0));
        fileMenu.addDropdownOption(new NewScene(boatVis));
        fileMenu.addDropdownOption(new SaveSceneFile(boatVis));
        fileMenu.addDropdownOption(new SaveAsSceneFile(boatVis));
        fileMenu.addDropdownOption(new OpenSceneFile(boatVis));
        fileMenu.addDropdownOption(new ExitBoatVis(boatVis));

        resolutionDropdown = addComponentToTop(new ResolutionDropdown("Resolution", getNextMenuItemPosition(), DROPDOWN_MENU_SIZE, 0));

        colourSequenceDropdown = addComponentToTop(new ColourSequenceDropdown("Colour Seq", getNextMenuItemPosition(), DROPDOWN_MENU_SIZE, 0, boatVis));

    }

    private Vector2f getNextMenuItemPosition() {
        float xPosition = -((size.x - DROPDOWN_MENU_SIZE.x) / 2f);
        xPosition += (menuItems * DROPDOWN_MENU_SIZE.x);
        menuItems++;
        return new Vector2f(xPosition, 0);
    }

    public DropdownMenu getFileMenu() {
        return fileMenu;
    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);
        if (fKeyHandler.justPressed()) {
            fileMenu.toggleMenu();
        }
    }

    public ColourSequenceDropdown getColourSequenceDropdown() {
        return colourSequenceDropdown;
    }

}
