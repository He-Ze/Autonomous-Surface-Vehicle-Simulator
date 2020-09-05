package gebd.games.boat.ui;

import blindmystics.input.CurrentInput;
import blindmystics.util.FileReader;
import gebd.games.boat.BoatVis;
import gebd.games.boat.boat.WamV;
import gebd.games.boat.json.util.Vector3fJson;
import gebd.games.boat.json.util.info.JsonUtil;
import gebd.games.boat.json.util.info.TryLoadIgnoreMissingValue;
import gebd.games.boat.json.util.info.TryLoadJson;
import gebd.games.boat.scene.StoresInSceneFile;
import loader.LoadedObjectHandler;
import org.json.simple.JSONObject;
import renderables.r2D.colourpicker.ColourPicker;
import renderables.r2D.composite.DoubleTextArea;
import renderables.r2D.composite.TextButton;
import renderables.r2D.composite.TopLevelInterface;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.Button;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;
import renderables.r3D.skybox.Skybox;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.awt.*;

/**
 * Created by CaptainPete on 2/05/2016.
 */
public class SkyboxSettingsDislpay extends UserInterface implements StoresInSceneFile {

    protected BoatVis boatVis;

    protected SimpleQuad backgroundQuad;
    protected SimpleQuad foregroundQuad;

    protected DoubleTextArea boatLinearDampingField;
    protected DoubleTextArea boatAngularDampingField;

    protected DoubleTextArea cameraFieldOfView;

    protected DoubleTextArea blendAmount;

    protected Button reduceButton;

    protected TextButton selectColourButton;
    protected ColourPicker colourPickerTest;
    protected Vector3f pickedColour = new Vector3f(1, 1, 1);
    protected SimpleQuad currentColourDisplay;


    public SkyboxSettingsDislpay(Vector2f relativePosition, Vector2f size, BoatVis boatVis) {
        super(relativePosition, size, 0);

        this.boatVis = boatVis;

        backgroundQuad = addComponentToBottom(new SimpleQuad(new Vector2f(), size, 0));
        backgroundQuad.setBlend(new Vector4f(0.3f, 0.3f, 0.6f, 0.8f), 1f);

        cameraFieldOfView = createDoubleTextArea("Camera FOV", new Vector2f(0, -200), 10.0, 170.0, 40.0);

        Vector2f buttonSize = new Vector2f(25, 100);
        Vector2f buttonPosition = new Vector2f(0, buttonSize.y);
        buttonPosition.x = -((size.x - buttonSize.x) / 2f);
        reduceButton = addComponentToTop(new Button(FileReader.asResource("res/boat/textures/ExpandThing.png"), buttonPosition, buttonSize, 0));

        boatLinearDampingField = createDoubleTextArea("Boat Linear Damping", new Vector2f(0, -100), 0.0, 1.0, WamV.DEFAULT_LINEAR_DAMPING);
        boatAngularDampingField = createDoubleTextArea("Boat Angular Damping", new Vector2f(0, -150), 0.0, 1.0, WamV.DEFAULT_ANGULAR_DAMPING);

        blendAmount = createDoubleTextArea("Skybox Blend Amount", new Vector2f(0, -250), 0.0, 1.0, 0.0);

        Vector2f selectedColourQuadSize = new Vector2f(40, 40);
        Vector2f selectedColourQuadPosition = new Vector2f(0, -330);
        currentColourDisplay = addComponentToTop(new SimpleQuad(selectedColourQuadPosition, selectedColourQuadSize, 0));
        currentColourDisplay.setBlend(new Vector4f(pickedColour.x, pickedColour.y, pickedColour.z, 1), 1);

        Vector2f selectColourButtonSize = new Vector2f(200, 20);
        Vector2f selectColourButtonPosition = new Vector2f(0, -300);
        selectColourButton = addComponentToTop(new TextButton("Choose a skybox colour", FileReader.asSharedFile("test_images/Buttonify.png"), selectColourButtonPosition, selectColourButtonSize, 0){
            @Override
            protected void onRelease() {
                selectNewColour();
            }
        });



        foregroundQuad = addComponentToTop(new SimpleQuad(new Vector2f(), size, 0));
        foregroundQuad.setBlend(new Vector4f(0, 0, 0, 0.5f), 1f);
        foregroundQuad.setVisible(false);
    }

    public void selectNewColour(){
        colourPickerTest.setVisible(true);
        selectColourButton.setEnabled(false);
    }

    public Button getReduceButton() {
        return reduceButton;
    }

    public float getCameraFieldOfView() {
        return (float) cameraFieldOfView.getCurrentValue();
    }

    private DoubleTextArea createDoubleTextArea(String name, Vector2f position, double minVal, double maxVal, double currentValue) {
        Vector2f textAreaSize = new Vector2f(150, 20);
        Vector2f textPosition = new Vector2f(position);

        //Position offset for the text.
        //textPosition.x -= ((textAreaSize.x / 2f) + 10);
        textPosition.y += ((textAreaSize.y / 2f) + 10);
        String2D.StringAlignment textAlignment = String2D.StringAlignment.BOT_MIDDLE;

        addComponentToTop(new String2D(name, textAlignment, textPosition, new Vector2f(16, 16), "Courier", Font.PLAIN));
        DoubleTextArea newTextArea = addComponentToTop(new DoubleTextArea(position, new Vector2f(150, 20), "Courier", Font.PLAIN, minVal, maxVal));
        newTextArea.setValue(currentValue);
        return newTextArea;
    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);
        applyCurrentValues();
    }

    private void applyCurrentValues(){

        Skybox skybox = boatVis.getSkybox();
        skybox.setSkyboxBlendAmount((float) blendAmount.getCurrentValue());

        boatVis.getPhysicsBoat().setDamping((float) boatLinearDampingField.getCurrentValue(), (float) boatAngularDampingField.getCurrentValue());

    }


    public void setNewColour(Vector3f newColour) {
        setNewColour(newColour.x, newColour.y, newColour.z);
    }

    public void setNewColour(float red, float green, float blue){
        pickedColour.x = red;
        pickedColour.y = green;
        pickedColour.z = blue;
        currentColourDisplay.setBlend(new Vector4f(pickedColour.x, pickedColour.y, pickedColour.z, 1), 1);
        selectColourButton.setEnabled(true);
        boatVis.getSkybox().setSkyboxBlendColour(pickedColour);
    }

    @Override
    public void loadDependencies(LoadedObjectHandler<?> handler) {
        colourPickerTest = handler.newDependancy(new ColourPicker(){
            @Override
            protected void onDisplayClosed(float redVal, float greenVal, float blueVal) {
                setNewColour(redVal, greenVal, blueVal);
            }
        });
        colourPickerTest.setVisible(false);
        TopLevelInterface.addComponentToTopLayer(colourPickerTest);
        super.loadDependencies(handler);
    }

    //Loading and storing from a JSON file.

    public static final String JSON_VALUES_KEY = "misc_settings_sky";

    public static final String SKYBOX_BLEND_AMOUNT_KEY = "skybox_blend_amount";
    public static final String SKYBOX_COLOUR_KEY = "skybox_blend_colour";
    public static final String CAMERA_FIELD_OF_VIEW_KEY = "camera_filed_of_view";
    public static final String PHYSICS_LINEAR_DAMPING_KEY = "physics_boat_linear_damping";
    public static final String PHYSICS_ANGULAR_DAMPING_KEY = "physics_boat_angular_damping";

    @Override
    public void storeInformation(JSONObject object) {

        JSONObject allInformation = new JSONObject();

        //Store all of the information.
        JsonUtil.tryStore(allInformation, SKYBOX_BLEND_AMOUNT_KEY, blendAmount.getCurrentValue(), 0.0);
        Vector3fJson.storeVector3f(pickedColour, object, SKYBOX_COLOUR_KEY);
        JsonUtil.tryStore(allInformation, CAMERA_FIELD_OF_VIEW_KEY, cameraFieldOfView.getCurrentValue(), 40.0);
        JsonUtil.tryStore(allInformation, PHYSICS_LINEAR_DAMPING_KEY, boatLinearDampingField.getCurrentValue(), WamV.DEFAULT_LINEAR_DAMPING);
        JsonUtil.tryStore(allInformation, PHYSICS_ANGULAR_DAMPING_KEY, boatAngularDampingField.getCurrentValue(), WamV.DEFAULT_ANGULAR_DAMPING);

        object.put(JSON_VALUES_KEY, allInformation);

    }

    @Override
    public void loadInformation(JSONObject object) {

        JsonUtil.tryLoad(object, JSON_VALUES_KEY, new TryLoadJson() {
            @Override
            public void onSuccess(Object loadedObject) {
                loadAllInformation((JSONObject) loadedObject);
            }

            @Override public void onFailure() {
                //Do nothing.
            }
        });

    }

    private void loadAllInformation(JSONObject object) {

        //Skybox blend amount.
        JsonUtil.tryLoadIgnoreMissing(object, SKYBOX_BLEND_AMOUNT_KEY, new TryLoadIgnoreMissingValue() {
            @Override
            public void onSuccess(Object loadedObject) {
                blendAmount.setValue((Double) loadedObject);
            }
        });

        //Skybox blend colour.
        try {
            pickedColour = Vector3fJson.parseVector3f(object, SKYBOX_COLOUR_KEY);
        } catch (Exception e) {
            //Can't load the colour, oh well.
        }

        //Camera field of view.
        JsonUtil.tryLoadIgnoreMissing(object, CAMERA_FIELD_OF_VIEW_KEY, new TryLoadIgnoreMissingValue() {
            @Override
            public void onSuccess(Object loadedObject) {
                cameraFieldOfView.setValue((Double) loadedObject);
            }
        });

        //Linear damping.
        JsonUtil.tryLoadIgnoreMissing(object, PHYSICS_LINEAR_DAMPING_KEY, new TryLoadIgnoreMissingValue() {
            @Override
            public void onSuccess(Object loadedObject) {
                boatLinearDampingField.setValue((Double) loadedObject);
            }
        });

        //Angular damping.
        JsonUtil.tryLoadIgnoreMissing(object, PHYSICS_ANGULAR_DAMPING_KEY, new TryLoadIgnoreMissingValue() {
            @Override
            public void onSuccess(Object loadedObject) {
                boatAngularDampingField.setValue((Double) loadedObject);
            }
        });

        applyCurrentValues();

    }
}
