package gebd.games.boat.ui;

import blindmystics.input.CurrentInput;
import blindmystics.input.KeyboardInputLatchGenerator;
import blindmystics.util.FileReader;
import blindmystics.util.input.InputLatch;
import com.bulletphysics.linearmath.Transform;
import gebd.games.boat.BoatVis;
import gebd.games.boat.lidar.LidarHelper;
import gebd.games.boat.physics.PhysicsSettings;
import org.lwjgl.input.Keyboard;
import physics.util.PhysTransform;
import renderables.r2D.composite.DoubleTextArea;
import renderables.r2D.composite.TextButton;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.Button;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;
import renderables.r3D.water.WaterSettings;
import renderables.r3D.water.wave.WaveSettings;
import util.settings.SettingsHolderWithParents;

import javax.vecmath.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by CaptainPete on 2/05/2016.
 */
public class PhysicsSettingsDisplay extends UserInterface implements WaveSettings {

    SettingsHolderWithParents<PhysicsSettings> physicsSettings;

    SimpleQuad backgroundQuad;

    DoubleTextArea physicsSpeedModifier;

    DoubleTextArea leftMotorForceTextArea;
    DoubleTextArea rightMotorForceTextArea;

    TextButton useSimpleWater;
    DoubleTextArea waveHeight;
    DoubleTextArea waveDirection;
    DoubleTextArea waveFrequency;
    DoubleTextArea distanceBetweenWaves;

    String2D usePhysicsBoatText;
    TextButton usePhysicsBoatButton;

    TextButton setPhysicsBoatPositionButton;
    boolean setPhysicsBoatPosition = false;

    TextButton setPhysicsBoatRotationButton;
    boolean setPhysicsBoatRotation = false;

    Button reduceButton;

   TextButton resetBoatPositionButton;

    static Vector3f defaultBoatPosition = new Vector3f();
    static Quat4f defaultBoatRotation = new Quat4f();

    TextButton lidar5HzButton;
    TextButton lidar10HzButton;
    TextButton lidar20HzButton;
    HashMap<LidarHelper.LidarRotationSpeed, TextButton> lidarSpeedMap = new HashMap<>();

    HashMap<PhysicsSettings, DoubleTextArea> settingsMap = new HashMap<>();

    InputLatch positionSetter = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_P);
    InputLatch rotationSetter = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_R);

    static public BoatVis boatVis;

    public PhysicsSettingsDisplay(Vector2f relativePosition, Vector2f size, float rotation, final BoatVis boatVis,
                                  SettingsHolderWithParents<PhysicsSettings> physicsSettingsAttributes) {
        super(relativePosition, size, rotation);

        this.boatVis = boatVis;
        this.physicsSettings = physicsSettingsAttributes;

        defaultBoatPosition.x = (float) physicsSettings.getValue(PhysicsSettings.BOAT_DEFAULT_POSITION_X);
        defaultBoatPosition.y = (float) physicsSettings.getValue(PhysicsSettings.BOAT_DEFAULT_POSITION_Y);
        defaultBoatPosition.z = (float) physicsSettings.getValue(PhysicsSettings.BOAT_DEFAULT_POSITION_Z);

        defaultBoatRotation.x = (float) physicsSettings.getValue(PhysicsSettings.BOAT_DEFAULT_ROTATION_X);
        defaultBoatRotation.y = (float) physicsSettings.getValue(PhysicsSettings.BOAT_DEFAULT_ROTATION_Y);
        defaultBoatRotation.z = (float) physicsSettings.getValue(PhysicsSettings.BOAT_DEFAULT_ROTATION_Z);
        defaultBoatRotation.w = (float) physicsSettings.getValue(PhysicsSettings.BOAT_DEFAULT_ROTATION_W);

        //TODO - This is a HUGE issue, for some reason, this is showing up as the TEXT TEXTURE! :O
        backgroundQuad = addComponentToBottom(new SimpleQuad(new Vector2f(), size, 0));
        backgroundQuad.setBlend(new Vector4f(0.3f, 0.3f, 0.6f, 0.8f), 1f);

        physicsSpeedModifier = createDoubleTextArea("Physics Speed Modifier", new Vector2f(0, 150), 0.0, 10.0, PhysicsSettings.PHYSICS_SPEED_MODIFIER);

        addComponentToTop(new String2D("Left Motor", String2D.StringAlignment.MID_MIDDLE, new Vector2f(-75, 123), new Vector2f(16, 16), "Courier", Font.PLAIN));
        leftMotorForceTextArea = addComponentToTop(new DoubleTextArea(new Vector2f(-75, 100), new Vector2f(100, 20), "Courier", Font.PLAIN, -200.0, 200.0){
            @Override
            public void onDeselect() {
                super.onDeselect();
                float hz=(float) getCurrentValue();
                boatVis.getPhysicsBoat().applyConstantMotorForceOnLeftPontoon(hz);
            }
        });
        leftMotorForceTextArea.setValue(0.0f);

        addComponentToTop(new String2D("Right Motor", String2D.StringAlignment.MID_MIDDLE, new Vector2f(75, 123), new Vector2f(16, 16), "Courier", Font.PLAIN));
        rightMotorForceTextArea = addComponentToTop(new DoubleTextArea(new Vector2f(75, 100), new Vector2f(100, 20), "Courier", Font.PLAIN, -200.0, 200.0){
            @Override
            public void onDeselect() {
                super.onDeselect();
                float hz=(float) getCurrentValue();
                boatVis.getPhysicsBoat().applyConstantMotorForceOnRightPontoon(hz);
            }
        });
        rightMotorForceTextArea.setValue(0.0f);




        waveHeight = createDoubleTextArea("Wave Height", new Vector2f(0, -0), 0.0, 2.0, PhysicsSettings.WAVE_HEIGHT);
        waveDirection = createDoubleTextArea("Water Direction", new Vector2f(0, -50), 0, 360, PhysicsSettings.WATER_DIRECTION);
        waveFrequency = createDoubleTextArea("Water Frequency", new Vector2f(0, -100), 1.0f, Double.POSITIVE_INFINITY, PhysicsSettings.WATER_FREQUENCY);
        distanceBetweenWaves = createDoubleTextArea("Distance Between Waves", new Vector2f(0, -150), 0.5f, Double.POSITIVE_INFINITY, PhysicsSettings.DISTANCE_BEWTEEN_WAVES);

        usePhysicsBoatText = addComponentToTop(new String2D(
                "Use Physics Simulated Boat", String2D.StringAlignment.BOT_MIDDLE,
                new Vector2f(0, -200), new Vector2f(16, 16),
                "Courier", Font.PLAIN
        ));


        useSimpleWater = addComponentToTop(new TextButton(
                "Simple Water", FileReader.asSharedFile("test_images/Buttonify.png"),
                new Vector2f(0, 50), new Vector2f(200, 40), 0f
        ){
            @Override
            protected void onRelease() {
                super.onRelease();
                boolean existingValue = (boolean) physicsSettings.getValue(PhysicsSettings.USE_SIMPLE_WATER);
                physicsSettings.setValue(PhysicsSettings.USE_SIMPLE_WATER, !existingValue);
                if (!existingValue) {
                    setText("Simple Water");
                } else {
                    setText("Complex Water");
                }
            }
        });


        usePhysicsBoatButton = addComponentToTop(new TextButton(
                "" + physicsSettings.getValue(PhysicsSettings.USE_PHYSICS_BOAT),
                FileReader.asSharedFile("test_images/Buttonify.png"),
                new Vector2f(0, -220), new Vector2f(200, 40), 0f
        ){
            @Override
            protected void onRelease() {
                super.onRelease();
                boatVis.syncBoatPosition();
                boolean existingValue = (boolean) physicsSettings.getValue(PhysicsSettings.USE_PHYSICS_BOAT);
                physicsSettings.setValue(PhysicsSettings.USE_PHYSICS_BOAT, !existingValue);
                setText("" + (!existingValue));
            }
        });

        usePhysicsBoatText = addComponentToTop(new String2D(
                "Use Physics Simulated Boat", String2D.StringAlignment.BOT_MIDDLE,
                new Vector2f(0, -200), new Vector2f(16, 16),
                "Courier", Font.PLAIN
        ));

        setPhysicsBoatPositionButton = addComponentToTop(new TextButton(
                "Set Boat Position", FileReader.asSharedFile("test_images/Buttonify.png"),
                new Vector2f(0, 210), new Vector2f(200, 30), 0f
        ){
            @Override
            protected void onRelease() {
                super.onRelease();
                setPhysicsBoatPosition = !setPhysicsBoatPosition;
            }
        });

        setPhysicsBoatRotationButton = addComponentToTop(new TextButton(
                "Set Boat Rotation", FileReader.asSharedFile("test_images/Buttonify.png"),
                new Vector2f(0, 250), new Vector2f(200, 30), 0f
        ){
            @Override
            protected void onRelease() {
                super.onRelease();
                setPhysicsBoatRotation = !setPhysicsBoatRotation;
            }
        });



        resetBoatPositionButton = addComponentToTop(new TextButton(
                "Reset Boat", FileReader.asSharedFile("test_images/Buttonify.png"),
                new Vector2f(0, 290), new Vector2f(200, 30), 0f
        ){
            @Override
            protected void onRelease() {
                super.onRelease();
                resetBoatPosition();
            }
        });



        lidar5HzButton = addComponentToTop(new TextButton(
                "5Hz", FileReader.asSharedFile("test_images/Buttonify.png"),
                new Vector2f(0, -260), new Vector2f(200, 30), 0f
        ){
            @Override
            protected void onRelease() {
                super.onRelease();
                boatVis.setLidarRotationSpeed(LidarHelper.LidarRotationSpeed.Hz5);
            }
        });
        lidarSpeedMap.put(LidarHelper.LidarRotationSpeed.Hz5, lidar5HzButton);
        lidar10HzButton = addComponentToTop(new TextButton(
                "10Hz", FileReader.asSharedFile("test_images/Buttonify.png"),
                new Vector2f(0, -290), new Vector2f(200, 30), 0f
        ){
            @Override
            protected void onRelease() {
                super.onRelease();
                boatVis.setLidarRotationSpeed(LidarHelper.LidarRotationSpeed.Hz10);
            }
        });
        lidarSpeedMap.put(LidarHelper.LidarRotationSpeed.Hz10, lidar10HzButton);
        lidar20HzButton = addComponentToTop(new TextButton(
                "20Hz", FileReader.asSharedFile("test_images/Buttonify.png"),
                new Vector2f(0, -320), new Vector2f(200, 30), 0f
        ){
            @Override
            protected void onRelease() {
                super.onRelease();
                boatVis.setLidarRotationSpeed(LidarHelper.LidarRotationSpeed.Hz20);
            }
        });
        lidarSpeedMap.put(LidarHelper.LidarRotationSpeed.Hz20, lidar20HzButton);

        lidar5HzButton.setEnabled(false);
        lidar10HzButton.setEnabled(true);
        lidar20HzButton.setEnabled(false);

        Vector2f buttonSize = new Vector2f(25, 100);
        Vector2f buttonPosition = new Vector2f(0, 0);
        buttonPosition.x = -((size.x - buttonSize.x) / 2f);
        reduceButton = addComponentToTop(new Button(FileReader.asResource("res/boat/textures/ExpandThing.png"), buttonPosition, buttonSize, 0));
    }

    public static void resetBoatPosition() {
        Transform boatTransform = new Transform();

        Vector3f physicsBoatPosition = PhysTransform.toPhysPosition(defaultBoatPosition);
        physicsBoatPosition.y = boatVis.getRenderedWater().getWaterHeight(defaultBoatPosition.x, defaultBoatPosition.z) + 0.80f;

        boatTransform.origin.set(physicsBoatPosition);
        boatTransform.basis.set(PhysTransform.toPhysRotation(defaultBoatRotation));
        boatVis.getPhysicsBoat().getBody().setWorldTransform(boatTransform);
        boatVis.getPhysicsBoat().getBody().clearForces();
    }

    public Button getReduceButton() {
        return reduceButton;
    }

    private DoubleTextArea createDoubleTextArea(String name, Vector2f position, double minVal, double maxVal, final PhysicsSettings physicsSetting) {
        Vector2f textAreaSize = new Vector2f(150, 20);
        Vector2f textPosition = new Vector2f(position);
        textPosition.y += textAreaSize.y;
        addComponentToTop(new String2D(name, String2D.StringAlignment.MID_MIDDLE, textPosition, new Vector2f(16, 16), "Courier", Font.PLAIN));
        DoubleTextArea newTextArea = addComponentToTop(new DoubleTextArea(position, new Vector2f(150, 20), "Courier", Font.PLAIN, minVal, maxVal){
            @Override
            public void onDeselect() {
                super.onDeselect();
                physicsSettings.setValue(physicsSetting, (float) getCurrentValue());
            }
        });
        newTextArea.setValue((float) physicsSettings.getValue(physicsSetting));
        settingsMap.put(physicsSetting, newTextArea);
        return newTextArea;
    }

    private void setButtonAsNonClickableForLidarSpeed(TextButton buttonJustClicked) {
        lidar5HzButton.setEnabled(true);
        lidar10HzButton.setEnabled(true);
        lidar20HzButton.setEnabled(true);
        buttonJustClicked.setEnabled(false);
    }

    @Override
    public float getWaterWaveHeight() {
        return (float) waveHeight.getCurrentValue();
    }

    @Override
    public float getWaterDirection() {
        return (float) waveDirection.getCurrentValue();
    }

    @Override
    public float getWaterWaveFrequency() {
        return (float) waveFrequency.getCurrentValue();
    }

    @Override
    public float getDistanceBetweenWaves() {
        return (float) distanceBetweenWaves.getCurrentValue();
    }

    public boolean usePhysicsBoat() {
        return (boolean) physicsSettings.getValue(PhysicsSettings.USE_PHYSICS_BOAT);
    }

    public boolean setPhysicsBoatPositionBoat() {
        return setPhysicsBoatPosition;
    }

    public boolean setPhysicsBoatRotationBoat() {
        return setPhysicsBoatRotation;
    }

    public float getPhysicsSpeedModifier() {
        return (float) physicsSpeedModifier.getCurrentValue();
    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);

        if (positionSetter.justPressed()) {
            setPhysicsBoatPosition = !setPhysicsBoatPosition;
        }
        if (rotationSetter.justPressed()) {
            setPhysicsBoatRotation = !setPhysicsBoatRotation;
        }

        boolean usePhysicsBoat = (boolean) physicsSettings.getValue(PhysicsSettings.USE_PHYSICS_BOAT);
        setPhysicsBoatPositionButton.setEnabled(usePhysicsBoat);
        setPhysicsBoatRotationButton.setEnabled(usePhysicsBoat);
        resetBoatPositionButton.setEnabled(usePhysicsBoat);

        if (!leftMotorForceTextArea.isSelecting()) {
            leftMotorForceTextArea.setValue(boatVis.getPhysicsBoat().getCurrentLeftMotorForce());
        }
        if (!rightMotorForceTextArea.isSelecting()) {
            rightMotorForceTextArea.setValue(boatVis.getPhysicsBoat().getCurrentRightMotorForce());
        }

    }

    public Vector3f getDefaultBoatPos() {
        return defaultBoatPosition;
    }

    public Quat4f getDefaultBoatRotation(){
        return defaultBoatRotation;
    }

    public void setDefaultPosition(Vector3f newDefaultPosition) {
        this.defaultBoatPosition.set(newDefaultPosition);
        physicsSettings.setValue(PhysicsSettings.BOAT_DEFAULT_POSITION_X, defaultBoatPosition.x);
        physicsSettings.setValue(PhysicsSettings.BOAT_DEFAULT_POSITION_Y, defaultBoatPosition.y);
        physicsSettings.setValue(PhysicsSettings.BOAT_DEFAULT_POSITION_Z, defaultBoatPosition.z);
        setPhysicsBoatPosition = false;
    }

    public void setDefaultBoatRotation(Quat4f quat4f) {
        this.defaultBoatRotation.set(quat4f);
        physicsSettings.setValue(PhysicsSettings.BOAT_DEFAULT_ROTATION_X, defaultBoatRotation.x);
        physicsSettings.setValue(PhysicsSettings.BOAT_DEFAULT_ROTATION_Y, defaultBoatRotation.y);
        physicsSettings.setValue(PhysicsSettings.BOAT_DEFAULT_ROTATION_Z, defaultBoatRotation.z);
        physicsSettings.setValue(PhysicsSettings.BOAT_DEFAULT_ROTATION_W, defaultBoatRotation.w);
        setPhysicsBoatRotation = false;
    }

    public void updateAllValues(SettingsHolderWithParents newPhysicsSettings) {
        newPhysicsSettings.setNewParent(this.physicsSettings);
        this.physicsSettings = newPhysicsSettings;
        updateAllValues();
    }

    public void updateValue(PhysicsSettings physicsSetting) {
        DoubleTextArea doubleTextArea = settingsMap.get(physicsSetting);
        if (doubleTextArea != null) {
            doubleTextArea.setValue((Float) physicsSettings.getValue(physicsSetting));
        } else {
            //TODO - The positions & rotations.
            switch (physicsSetting) {
                case BOAT_DEFAULT_ROTATION_X:
                    break;
            }
        }
    }

    private void updateAllValues(){
        for (PhysicsSettings physicsSetting : PhysicsSettings.values()) {
            updateValue(physicsSetting);
        }
    }

    public void resetAllValues() {
        physicsSettings.setAllValuesToDefault();
        updateAllValues();
    }

    public boolean useSimpleWater() {
        return (boolean) physicsSettings.getValue(PhysicsSettings.USE_SIMPLE_WATER);
    }

}
