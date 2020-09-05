package gebd.games.boat.scene;

import renderables.r3D.water.WaterSettings;

import java.io.File;
import java.io.IOException;

/**
 * Created by CaptainPete on 8/1/2016.
 */
public class BoatVisSceneStoreTest {

    public static void main(String[] args) {
        BoatVisSceneStoreTest boatVisSceneStoreTest = new BoatVisSceneStoreTest();
        boatVisSceneStoreTest.lolTest();
    }

    void lolTest() {
        BoatVisScene scene = new BoatVisScene();

        scene.getWaterSettings().setValue(WaterSettings.MURKINESS, 0.65f);

        File file = new File("test/OMG.txt");

        try {
            scene.saveToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        scene.getWaterSettings().setValue(WaterSettings.MURKINESS, 0.23f);
        System.out.println("OMG WHAT?");

        try {
            scene.loadFromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("DONE.");

    }

}
