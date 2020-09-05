package gebd.games.boat.ui.topnav.file;

import blindmystics.input.CurrentInput;
import blindmystics.input.KeyboardInputLatchGenerator;
import blindmystics.util.input.InputLatch;
import gebd.Render;
import gebd.games.boat.BoatVis;
import org.lwjgl.input.Keyboard;
import renderables.r2D.composite.userinterface.scroll.DropdownMenuItem;
import util.file.FileChooser;

import java.io.File;

/**
 * Created by P3TE on 5/08/2016.
 */
public class ExitBoatVis extends DropdownMenuItem {
    public static final String MENU_NAME = "Exit (X)";

    private BoatVis boatVis;

    private InputLatch fKeyHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_X);

    public ExitBoatVis(BoatVis boatVis) {
        super(MENU_NAME);
        this.boatVis = boatVis;
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void onSelect() {
        /*
         * NOTE - It might be best to ask the user whether they want to save.
         * Or just prompt the user whether they are sure (Will lose unsaved data).
         * Or have a DEFAULT file, and save to that. However for this to work,
         * you have to be able to save the current file, but NOT all of the dependant
         * files in case a mistake was made.
         */
        Render.getInstance().closeGame(0);
    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);
        if (fKeyHandler.justPressed()) {
            onSelect();
        }
    }
}
