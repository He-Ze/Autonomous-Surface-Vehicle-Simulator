package gebd.games.boat.ui;

import blindmystics.util.FileReader;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

import gebd.games.boat.BoatVis;
import gebd.games.boat.physics.PhysicsSettings;
import renderables.r2D.composite.DoubleTextArea;
import renderables.r2D.composite.TextButton;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.Button;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;
import util.settings.SettingsHolder;
import util.settings.SettingsHolderWithParents;
import renderables.r3D.water.WaterSettings;

import java.awt.Font;
import java.util.HashMap;

/**
 * Created by CaptainPete on 2/05/2016.
 */
public class WaterSettingsUI extends UserInterface {

    private SettingsHolderWithParents<WaterSettings> waterSettings;

    protected SimpleQuad backgroundQuad;

    protected DoubleTextArea waterRedColour;
    protected DoubleTextArea waterGreenColour;
    protected DoubleTextArea waterBlueColour;

    protected DoubleTextArea murkiness;
    protected DoubleTextArea waterReflectivity;

    protected DoubleTextArea tilingX;
    protected DoubleTextArea tilingY;

    protected DoubleTextArea waveStrength;
    protected DoubleTextArea waterRotation;

    protected Button reduceButton;

    protected String2D useGoldCreekText;
    protected TextButton useGoldCreekSceneButton;

    private HashMap<WaterSettings, DoubleTextArea> settingsMap = new HashMap<>();

    private BoatVis boatVis;

    public WaterSettingsUI(final BoatVis boatVis, Vector2f relativePosition, Vector2f size, float rotation, SettingsHolderWithParents<WaterSettings> waterSettings) {
        super(relativePosition, size, rotation);

        this.boatVis = boatVis;
        this.waterSettings = waterSettings;

        backgroundQuad = addComponentToBottom(new SimpleQuad(new Vector2f(), size, 0));
        backgroundQuad.setBlend(new Vector4f(0.3f, 0.3f, 0.6f, 0.8f), 1f);

        System.out.println("F1A");

        waterRedColour = createDoubleTextArea("red", new Vector2f(0, 300), 0, 1, WaterSettings.RED_COLOUR);
        waterGreenColour = createDoubleTextArea("Water Green Colour", new Vector2f(0, 250), 0, 1, WaterSettings.GREEN_COLOUR);
        waterBlueColour = createDoubleTextArea("Water Blue Colour", new Vector2f(0, 200), 0, 1, WaterSettings.BLUE_COLOUR);

        System.out.println("F1B");

        waterReflectivity = createDoubleTextArea("Water Reflectivity", new Vector2f(0, 100), 0, 50, WaterSettings.WATER_REFLECTIVITY);

        murkiness = createDoubleTextArea("Murkiness", new Vector2f(), 0, 1, WaterSettings.MURKINESS);

        tilingX = createDoubleTextArea("TilingX", new Vector2f(0, -100), 0.1, 1000, WaterSettings.TILING_X);
        tilingY = createDoubleTextArea("TilingY", new Vector2f(0, -150), 0.1, 1000, WaterSettings.TILING_Y);

        waveStrength = createDoubleTextArea("Wave Strength", new Vector2f(0, -200), 0.0, 1, WaterSettings.WAVE_STRENGTH);
        waterRotation = createDoubleTextArea("Water Rotation", new Vector2f(0, -250), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, WaterSettings.WATER_ROTATION);



        //
        useGoldCreekText = addComponentToTop(new String2D(
                "Use Gold Creek Dam Scene", String2D.StringAlignment.BOT_MIDDLE,
                new Vector2f(0, -290), new Vector2f(16, 16),
                "Courier", Font.PLAIN
        ));
        String initialText = "" + boatVis.useGoldCreekDamScene();
        useGoldCreekSceneButton = addComponentToTop(new TextButton(
                initialText, FileReader.asSharedFile("test_images/Buttonify.png"),
                new Vector2f(0, -310), new Vector2f(200, 40), 0f
        ){
            @Override
            protected void onRelease() {
                super.onRelease();
                //Toggle gold creek dam.
                boatVis.setUseGoldCreekDamScene(!boatVis.useGoldCreekDamScene());
                setText("" + boatVis.useGoldCreekDamScene());
            }
        });

        Vector2f buttonSize = new Vector2f(25, 100);
        Vector2f buttonPosition = new Vector2f(0, 0);
        buttonPosition.x = -((size.x - buttonSize.x) / 2f);
        reduceButton = addComponentToTop(new Button(FileReader.asResource("res/boat/textures/ExpandThing.png"), buttonPosition, buttonSize, 0));
    }

    public Button getReduceButton() {
        return reduceButton;
    }

    private DoubleTextArea createDoubleTextArea(String name, Vector2f position, double minVal, double maxVal, final WaterSettings waterSetting) {
        Vector2f textAreaSize = new Vector2f(150, 20);
        Vector2f textPosition = new Vector2f(position);
        textPosition.y += textAreaSize.y;
        addComponentToTop(new String2D(name, String2D.StringAlignment.MID_MIDDLE, textPosition, new Vector2f(16, 16), "Courier", Font.PLAIN));
        DoubleTextArea newTextArea = addComponentToTop(new DoubleTextArea(position, new Vector2f(150, 20), "Courier", Font.PLAIN, minVal, maxVal){
            @Override
            public void onDeselect() {
                super.onDeselect();
                waterSettings.setValue(waterSetting, (float) getCurrentValue());
            }
        });
        newTextArea.setValue((float) waterSettings.getValue(waterSetting));
        settingsMap.put(waterSetting, newTextArea);
        return newTextArea;
    }

    public void updateAllValues(SettingsHolderWithParents newWaterSettings) {
        newWaterSettings.setNewParent(this.waterSettings);
        this.waterSettings = newWaterSettings;
        updateAllValues();
    }

    public void updateAllValues(){
        for (WaterSettings waterSetting : WaterSettings.values()) {
            updateValue(waterSetting);
        }
    }

    public void updateValue(WaterSettings waterSetting) {
        DoubleTextArea doubleTextArea = settingsMap.get(waterSetting);
        if (doubleTextArea == null) {
            //Ingore.
            return;
        }
        doubleTextArea.setValue((Float) waterSettings.getValue(waterSetting));
    }

    public void resetAllValues(){
        waterSettings.setAllValuesToDefault();
        updateAllValues();
    }

    public SettingsHolder<WaterSettings> getWaterSettings() {
        return waterSettings;
    }
}
