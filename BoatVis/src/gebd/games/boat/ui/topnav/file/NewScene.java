package gebd.games.boat.ui.topnav.file;

import gebd.games.boat.BoatVis;
import renderables.r2D.composite.userinterface.scroll.DropdownMenuItem;
import util.file.FileChooser;

import java.io.File;

/**
 * Created by P3TE on 5/08/2016.
 */
public class NewScene extends DropdownMenuItem {

    public static final String MENU_NAME = "New Scene";

    private BoatVis boatVis;

    public NewScene(BoatVis boatVis) {
        super(MENU_NAME);
        this.boatVis = boatVis;
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void onSelect() {
        boatVis.createNewScene();
    }
}
