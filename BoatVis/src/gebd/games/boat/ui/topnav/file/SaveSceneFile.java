package gebd.games.boat.ui.topnav.file;

import gebd.games.boat.BoatVis;
import renderables.r2D.composite.userinterface.scroll.DropdownMenuItem;

import java.io.IOException;

/**
 * Created by P3TE on 5/08/2016.
 */
public class SaveSceneFile extends DropdownMenuItem {

    public static final String MENU_NAME = "Save (Ctrl + S)";

    private BoatVis boatVis;

    public SaveSceneFile(BoatVis boatVis) {
        super(MENU_NAME);
        this.boatVis = boatVis;
    }

    @Override
    public boolean enabled() {
        return (boatVis.getBoatVisScene().getFile() != null);
    }

    @Override
    public void onSelect() {
        try {
            boatVis.getBoatVisScene().saveToFile(boatVis.getBoatVisScene().getFile());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to save to given file!" + e.getMessage());
            boatVis.getBoatVisScene().setFile(null);
        }
    }
}
