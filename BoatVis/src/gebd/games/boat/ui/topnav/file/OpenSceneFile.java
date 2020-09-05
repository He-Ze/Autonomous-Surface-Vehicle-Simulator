package gebd.games.boat.ui.topnav.file;

import gebd.games.boat.BoatVis;
import renderables.r2D.composite.userinterface.scroll.DropdownMenuItem;
import util.file.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * Created by P3TE on 5/08/2016.
 */
public class OpenSceneFile extends DropdownMenuItem {

    public static final String MENU_NAME = "Open (Ctrl + O)";

    private BoatVis boatVis;

    public OpenSceneFile(BoatVis boatVis) {
        super(MENU_NAME);
        this.boatVis = boatVis;
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void onSelect() {
        new FileChooser(){
            @Override
            public void onFileChosen(File file) {
                boatVis.loadBoatVisSceneFromFile(file);
            }

            @Override
            public void onNoFileSelected() {
                //Do nothing.
            }
        };
    }
}
