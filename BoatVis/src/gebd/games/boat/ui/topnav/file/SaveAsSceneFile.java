package gebd.games.boat.ui.topnav.file;

import gebd.games.boat.BoatVis;
import renderables.r2D.composite.userinterface.scroll.DropdownMenuItem;
import util.file.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * Created by P3TE on 5/08/2016.
 */
public class SaveAsSceneFile extends DropdownMenuItem {

    public static final String MENU_NAME = "Save As (Ctrl + Shift + S)";

    private BoatVis boatVis;

    public SaveAsSceneFile(BoatVis boatVis) {
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
                try {
                    boatVis.getBoatVisScene().saveToFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    //TODO - This should be handled better!
                }
            }

            @Override
            public void onNoFileSelected() {
                //Do nothing.
            }
        };
    }
}
