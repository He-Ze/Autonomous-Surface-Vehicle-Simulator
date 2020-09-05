package gebd.games.boat.ui;

import blindmystics.input.CurrentInput;
import blindmystics.input.KeyboardInputLatchGenerator;
import blindmystics.util.FileReader;
import blindmystics.util.input.InputLatch;
import blindmystics.util.input.keyboard.HandleKeyboard;
import blindmystics.util.input.mouse.ButtonStatus;
import composites.entities.Entity;
import gebd.ModelsetPlus;
import gebd.Render;
import gebd.games.boat.BoatModels;
import gebd.games.boat.BoatVis;
import gebd.games.boat.entity.pinger.ChooseFrequencyBox;
import gebd.games.boat.entity.pinger.PingerHandler;
import gebd.games.boat.ui.model.AddModelPopup;
import loader.LoadedObjectHandler;
import org.lwjgl.input.Keyboard;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import renderables.r2D.colourpicker.ColourPicker;
import renderables.r2D.composite.DoubleTextArea;
import renderables.r2D.composite.TextButton;
import renderables.r2D.composite.TopLevelInterface;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.Button;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;
import renderables.r3D.model.ModelsetModel;
import renderables.texture.TextureInfo;
import util.file.FileChooser;

import java.awt.*;
import java.io.File;

/**
 * Created by CaptainPete on 2/05/2016.
 */
public class AddBuoyDislpay extends UserInterface {

    protected SimpleQuad backgroundQuad;
    protected SimpleQuad foregroundQuad;

    protected TextButton addModelButton;
    protected TextButton selectTextureButton;
    private AddModelPopup addModelPopup;


    protected TextButton setPositionButton;
    protected DoubleTextArea positionX;
    protected DoubleTextArea positionY;
    protected DoubleTextArea positionZ;

    protected DoubleTextArea rotationX;
    protected DoubleTextArea rotationY;
    protected DoubleTextArea rotationZ;

    protected DoubleTextArea scaleX;
    protected DoubleTextArea scaleY;
    protected DoubleTextArea scaleZ;

    protected DoubleTextArea textureColourBlendArea;

    protected Button reduceButton;

    protected TextButton selectColourButton;

    protected TextButton useColourSequenceButton;
    protected TextButton isPingerBuoyToggleButton;
    protected ChooseFrequencyBox chooseFrequencyBox;

    protected Vector2f startMousePosition = new Vector2f();

    protected ColourPicker colourPickerTest;
    protected Vector3f pickedColour = new Vector3f(1, 1, 1);
    protected SimpleQuad currentColourDisplay;


    protected LoadedObjectHandler<Entity> currentlyLoadingEntity;
    protected ModelsetPlus modelset = BoatModels.getInstance();
    protected Entity lastLoadedEntity = null;

    private TextureInfo loadingTexture;
    private LoadedObjectHandler<TextureInfo> loadingTextureHandler;

    protected BoatVis boatVis;
    protected ParameterOptions parameterOptions = ParameterOptions.NONE;
    protected boolean changingParameterBasedOnX = false;
    protected boolean changingParameterBasedOnY = false;
    protected boolean changingParameterBasedOnZ = false;
    protected Vector3f prevEntityPosition = new Vector3f();
    protected Vector3f prevEntityRotation = new Vector3f();
    protected Vector3f prevEntityScale = new Vector3f();


    private InputLatch setPositionHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_G);
    private InputLatch setRotationHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_R);
    private InputLatch setScaleHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_T);
    private InputLatch beginFollowMouseHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_P);
    private InputLatch xPositionHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_X);
    private InputLatch yPositionHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_Y);
    private InputLatch zPositionHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_Z);
    private InputLatch escHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_ESCAPE);
    private InputLatch deleteHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_DELETE);

    public void setSelectedEntity(Entity selectedEntity) {
        lastLoadedEntity = selectedEntity;
        if (selectedEntity == null) {
            resetPositionScaleRotationBoxes();
            setNewColour(new Vector3f(1, 1, 1));
            deselect(false);
            textureColourBlendArea.setValue(0.5);
        } else {
            setSelectedEntityPosition(selectedEntity.getPosition());
            setSelectedEntityRotation(selectedEntity.getRotation());
            setSelectedEntityScale(selectedEntity.getSize());
            textureColourBlendArea.setValue(selectedEntity.getTextureBlendAmount());
            setNewColour(selectedEntity.getTextureBlendColour());
        }
    }

    private void updateColSeqButton(){

        if (lastLoadedEntity == null) {

            useColourSequenceButton.setText("-----");
            useColourSequenceButton.setEnabled(false);

        } else {
            useColourSequenceButton.setEnabled(true);
            if (boatVis.getColourSequenceHandler().contains(lastLoadedEntity)) {
                useColourSequenceButton.setText("Col Seq Enabled");
            } else {
                useColourSequenceButton.setText("Col Seq Disabled");
                lastLoadedEntity.setTextureBlendAmount((float) textureColourBlendArea.getCurrentValue());
            }
        }

    }

    private void updateIsPingerButton(){

        if (lastLoadedEntity == null) {

            isPingerBuoyToggleButton.setText("-----");
            isPingerBuoyToggleButton.setEnabled(false);

        } else {
            isPingerBuoyToggleButton.setEnabled(true);
            if (PingerHandler.contains(lastLoadedEntity)) {
                double pingingFrequency = PingerHandler.getPingingFrequency(lastLoadedEntity);
                isPingerBuoyToggleButton.setText("Pinging @ " + pingingFrequency + "Hz");
            } else {
                isPingerBuoyToggleButton.setText("Not a Pinger");
            }
        }

    }


    public enum ParameterOptions {
        NONE,
        FOLLOW_MOUSE,
        POSITION,
        ROTATION,
        SCALE
    }

    public AddBuoyDislpay(Vector2f relativePosition, Vector2f size, final BoatVis boatVis) {
        super(relativePosition, size, 0);
        this.boatVis = boatVis;

        backgroundQuad = addComponentToBottom(new SimpleQuad(new Vector2f(), size, 0));
        backgroundQuad.setBlend(new Vector4f(0.3f, 0.3f, 0.6f, 0.8f), 1f);

        addModelPopup = addNewDependancy(new AddModelPopup(this));
        TopLevelInterface.addComponentToTopLayer(addModelPopup);
        addModelPopup.setVisible(false);

        selectTextureButton = addComponentToTop(new TextButton("Select Texture", FileReader.asSharedFile("test_images/Buttonify.png"), new Vector2f(0, 200), new Vector2f(200, 50), 0){
            @Override
            protected void onRelease() {
                new FileChooser(){
                    @Override
                    public void onFileChosen(File file) {
                        loadSelectedTextre(file);
                    }

                    @Override
                    public void onNoFileSelected() {
                        //Do nothing.
                    }
                };
            }
        });

        useColourSequenceButton = addComponentToTop(new TextButton("-----", FileReader.asSharedFile("test_images/Buttonify.png"), new Vector2f(0, 250), new Vector2f(200, 50), 0) {
            @Override
            protected void onRelease() {
                boatVis.getColourSequenceHandler().toggleEntityUseSequence(lastLoadedEntity);
            }
        });

        isPingerBuoyToggleButton = addComponentToTop(new TextButton("-----", FileReader.asSharedFile("test_images/Buttonify.png"), new Vector2f(0, 300), new Vector2f(200, 50), 0) {
            @Override
            protected void onRelease() {
                if (PingerHandler.contains(lastLoadedEntity)) {
                    PingerHandler.removeExistingEntity(lastLoadedEntity);
                } else {
                    chooseFrequencyBox.setVisible(true);
                }
            }
        });

        textureColourBlendArea = createDoubleTextArea("Colour Blend", new Vector2f(60, 150), 0, 1, 0.5);

        addModelButton = addComponentToTop(new TextButton("Add New Model", FileReader.asSharedFile("test_images/Buttonify.png"), new Vector2f(0, 100), new Vector2f(200, 50), 0){
            @Override
            protected void onRelease() {
                addModelPopup.setVisible(true);
            }
        });

        Vector2f setPositionButtonSize = new Vector2f(200, 20);
        Vector2f setPositionButtonPosition = new Vector2f(0, 25);
        setPositionButton = addComponentToTop(new TextButton("Set Position", FileReader.asSharedFile("test_images/Buttonify.png"), setPositionButtonPosition, setPositionButtonSize, 0){
            @Override
            protected void onRelease() {
                if(lastLoadedEntity != null){
                    beginFollowMouse();
                }
            }
        });
        positionX = createDoubleTextArea("positionX:", new Vector2f(30, -0), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        positionY = createDoubleTextArea("positionY:", new Vector2f(30, -25), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        positionZ = createDoubleTextArea("positionZ:", new Vector2f(30, -50), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);

        rotationX = createDoubleTextArea("rotationX:", new Vector2f(30, -100), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        rotationY = createDoubleTextArea("rotationY:", new Vector2f(30, -125), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        rotationZ = createDoubleTextArea("rotationZ:", new Vector2f(30, -150), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);

        scaleX = createDoubleTextArea("scaleX:", new Vector2f(30, -200), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        scaleY = createDoubleTextArea("scaleY:", new Vector2f(30, -225), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        scaleZ = createDoubleTextArea("scaleZ:", new Vector2f(30, -250), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);

        Vector2f buttonSize = new Vector2f(25, 100);
        Vector2f buttonPosition = new Vector2f(0, buttonSize.y - 20);
        buttonPosition.x = -((size.x - buttonSize.x) / 2f);
        reduceButton = addComponentToTop(new Button(FileReader.asResource("res/boat/textures/ExpandThing.png"), buttonPosition, buttonSize, 0));

//        new TextButton("OK", "res/test_images/Buttonify.png", confirmButtonPosition, confirmButtonSize, 0


        Vector2f selectedColourQuadSize = new Vector2f(40, 40);
        Vector2f selectedColourQuadPosition = new Vector2f(0, -330);
        currentColourDisplay = addComponentToTop(new SimpleQuad(selectedColourQuadPosition, selectedColourQuadSize, 0));
        currentColourDisplay.setBlend(new Vector4f(pickedColour.x, pickedColour.y, pickedColour.z, 1), 1);

        Vector2f selectColourButtonSize = new Vector2f(200, 20);
        Vector2f selectColourButtonPosition = new Vector2f(0, -300);
        selectColourButton = addComponentToTop(new TextButton("Choose a colour", FileReader.asSharedFile("test_images/Buttonify.png"), selectColourButtonPosition, selectColourButtonSize, 0){
            @Override
            protected void onRelease() {
                selectNewColour();
            }
        });



        foregroundQuad = addComponentToTop(new SimpleQuad(new Vector2f(), size, 0));
        foregroundQuad.setBlend(new Vector4f(0, 0, 0, 0.5f), 1f);
        foregroundQuad.setVisible(false);
    }

    private void loadSelectedTextre(File file) {
        loadingTexture = TextureInfo.loadTexture(file.getPath());
    }

    public void selectNewColour(){
        colourPickerTest.setVisible(true);
        selectColourButton.setEnabled(false);
    }

    public void onPositionSet(boolean escWasPressed){
        foregroundQuad.setVisible(false);
        if (escWasPressed) {
            lastLoadedEntity.setPosition(prevEntityPosition);
        }
    }


    public Button getReduceButton() {
        return reduceButton;
    }

    public void selectNewEntity(BoatModels.ModelName modelName){
        addModelPopup.setVisible(false);
        ModelsetModel newModel = modelset.getModel(modelName.name(), true);
        String newTexturePath = modelset.getTexturePath(modelName.name());
        Entity newEntity = new Entity(modelName.name(), newModel, newTexturePath, new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f), new Vector3f(0, 0, 0));
        currentlyLoadingEntity = LoadedObjectHandler.load(newEntity);
    }

    private void resetPositionScaleRotationBoxes(){
        positionX.setValue(0);
        positionY.setValue(0);
        positionZ.setValue(0);

        rotationX.setValue(0);
        rotationY.setValue(0);
        rotationZ.setValue(0);

        scaleX.setValue(1);
        scaleY.setValue(1);
        scaleZ.setValue(1);
    }

    private DoubleTextArea createDoubleTextArea(String name, Vector2f position, double minVal, double maxVal, double currentValue) {
        Vector2f textAreaSize = new Vector2f(150, 20);
        Vector2f textPosition = new Vector2f(position);
        textPosition.x -= ((textAreaSize.x / 2f) + 10);
        addComponentToTop(new String2D(name, String2D.StringAlignment.MID_RIGHT, textPosition, new Vector2f(16, 16), "Courier", Font.PLAIN));
        DoubleTextArea newTextArea = addComponentToTop(new DoubleTextArea(position, new Vector2f(150, 20), "Courier", Font.PLAIN, minVal, maxVal));
        newTextArea.setValue(currentValue);
        return newTextArea;
    }

    @Override
    public void update(CurrentInput input, float delta) {
        handleWorldPositionSelection(input);

        super.update(input, delta);

        //Disable if there is nothing to move.
        setPositionButton.setEnabled(lastLoadedEntity != null);

        if(currentlyLoadingEntity != null){
            if (currentlyLoadingEntity.isLoaded()) {
                textureColourBlendArea.setValue(0.5);
                lastLoadedEntity = currentlyLoadingEntity.getAttachedObject();
                lastLoadedEntity.setTextureBlendColour(pickedColour);
                resetPositionScaleRotationBoxes();
                boatVis.getBoatVisScene().addNewSceneObject(lastLoadedEntity);
                currentlyLoadingEntity = null;
            }

        }

        if (loadingTexture != null) {
            if (loadingTexture.isLoaded()) {
                lastLoadedEntity.setTexture(loadingTexture);

                if (loadingTexture.failedToLoad()) {
                    //The texture failed to load.
                    System.err.println("ERROR - Failed to load texture: " + loadingTexture.getPath());
                    System.err.println("... Resorting to backup texture.");
                }

                loadingTexture = null;
            }
        }

        if (deleteHandler.justPressed()) {
            if (lastLoadedEntity != null) {
                //Remove the entity from the render list.
                lastLoadedEntity.removeFromRenderList();
                setSelectedEntity(null);
            }
        }

        updateColSeqButton();
        updateIsPingerButton();


    }

    private void handleWorldPositionSelection(CurrentInput input) {
        if (lastLoadedEntity != null) {
            //Only select position if something exists to select the position with.
            if (setPositionHandler.justPressed()) {
                startPositionSelection(input);
            } else if (setRotationHandler.justPressed()) {

            } else if (setScaleHandler.justPressed()) {

            }
            if (beginFollowMouseHandler.justPressed()) {
                beginFollowMouse();
            }
        }

        boolean xyzWerePressed = false;
        //X - axis.
        if (xPositionHandler.justReleased()) {
            xyzWerePressed = true;
            changingParameterBasedOnX = true;
            changingParameterBasedOnY = false;
            changingParameterBasedOnZ = false;
        }

        //Y - axis.
        if (yPositionHandler.justReleased()) {
            xyzWerePressed = true;
            changingParameterBasedOnX = false;
            changingParameterBasedOnY = true;
            changingParameterBasedOnZ = false;
        }

        //Z - axis.
        if (zPositionHandler.justReleased()) {
            xyzWerePressed = true;
            changingParameterBasedOnX = false;
            changingParameterBasedOnY = false;
            changingParameterBasedOnZ = true;
        }

        if (xyzWerePressed) {
            //TODO - This is probably going to have to change.
            if (HandleKeyboard.isShiftHeld()) {
                //Flip them.
                changingParameterBasedOnX = !changingParameterBasedOnX;
                changingParameterBasedOnY = !changingParameterBasedOnY;
                changingParameterBasedOnZ = !changingParameterBasedOnZ;
            }
            //Reset parameters that now can't move.
            resetParameters(!changingParameterBasedOnX, !changingParameterBasedOnY, !changingParameterBasedOnZ);
        }

        changeParameterBasedOnMouse();

        //Deselect (If applicable)
        if (parameterOptions != ParameterOptions.NONE) {
            if (escHandler.justPressed()
                    || (input.getRightMouse() == ButtonStatus.JUST_RELEASED)) {
                deselect(true);
            } else if (input.getLeftMouse() == ButtonStatus.JUST_RELEASED) {
                deselect(false);
            }
        }
    }

    private void changeParameterBasedOnMouse() {
        if (lastLoadedEntity == null) {
            //No entity to move.
            return;
        }

        switch (parameterOptions) {
            case NONE:
                //Do nothing.
                break;
            case FOLLOW_MOUSE:
                Vector3f mouseWorldLocation = boatVis.getMouseWaterLocation();
                if (changingParameterBasedOnX) {
                    setPositionX(mouseWorldLocation.x);
                }
                if (changingParameterBasedOnZ) {
                    setPositionZ(mouseWorldLocation.z);
                }
                break;
            case POSITION:
                //TODO - I'm PRETTY SURE that this isn't how it's done in blender!
                break;
            case ROTATION:
                break;
            case SCALE:
                break;
        }


        lastLoadedEntity.setPosition((float) positionX.getCurrentValue(), (float) positionY.getCurrentValue(), (float) positionZ.getCurrentValue());
        lastLoadedEntity.setRotation((float) rotationX.getCurrentValue(), (float) rotationY.getCurrentValue(), (float) rotationZ.getCurrentValue());
        lastLoadedEntity.setSize((float) scaleX.getCurrentValue(), (float) scaleY.getCurrentValue(), (float) scaleZ.getCurrentValue());
    }

    private void setSelectedEntityPosition(Vector3f position) {
        setSelectedEntityPosition(position.x, position.y, position.z);
    }

    private void setSelectedEntityPosition(float x, float y, float z) {
        setPositionX(x);
        setPositionY(y);
        setPositionZ(z);
    }

    private void setPositionX(float posX) {
        positionX.setValue(posX);
    }

    private void setPositionY(float posY) {
        positionY.setValue(posY);
    }

    private void setPositionZ(float posZ) {
        positionZ.setValue(posZ);
    }

    //Rotation
    private void setSelectedEntityRotation(Vector3f rotation) {
        setSelectedEntityRotation(rotation.x, rotation.y, rotation.z);
    }

    private void setSelectedEntityRotation(float x, float y, float z) {
        setRotationX(x);
        setRotationY(y);
        setRotationZ(z);
    }

    private void setRotationX(float posX) {
        rotationX.setValue(posX);
    }

    private void setRotationY(float posY) {
        rotationY.setValue(posY);
    }

    private void setRotationZ(float posZ) {
        rotationZ.setValue(posZ);
    }


    //Scale
    private void setSelectedEntityScale(Vector3f scale) {
        setSelectedEntityScale(scale.x, scale.y, scale.z);
    }

    private void setSelectedEntityScale(float x, float y, float z) {
        setScaleX(x);
        setScaleY(y);
        setScaleZ(z);
    }

    private void setScaleX(float posX) {
        scaleX.setValue(posX);
    }

    private void setScaleY(float posY) {
        scaleY.setValue(posY);
    }

    private void setScaleZ(float posZ) {
        scaleZ.setValue(posZ);
    }

    private void deselect(boolean resetParameters) {
        if (resetParameters) {
            resetParameters(true, true, true);
        }
        parameterOptions = ParameterOptions.NONE;
        foregroundQuad.setVisible(false);
    }

    private void resetParameters(boolean resetX, boolean resetY, boolean resetZ) {
        switch (parameterOptions) {
            case NONE:
                //Do nothing.
                break;
            case FOLLOW_MOUSE:
            case POSITION:
                resetPosition(resetX, resetY, resetZ);
                break;
            case ROTATION:
                //TODO - Reset rotation.
                break;
            case SCALE:
                //TODO - Reset rotation.
                break;
        }
    }

    private void resetPosition(boolean resetX, boolean resetY, boolean resetZ) {
        if (resetX) {
            setPositionX(prevEntityPosition.x);
        }
        if (resetY) {
            setPositionY(prevEntityPosition.y);
        }
        if (resetZ) {
            setPositionZ(prevEntityPosition.z);
        }
    }

    private void beginFollowMouse() {
        boatVis.setMouseFollowingEntity(null);
        startPositionSelection(Render.getInstance().getCurrentInput());
        parameterOptions = ParameterOptions.FOLLOW_MOUSE;
    }

    private void startPositionSelection(CurrentInput input) {
        prevEntityPosition.x = (float) positionX.getCurrentValue();
        prevEntityPosition.y = (float) positionY.getCurrentValue();
        prevEntityPosition.z = (float) positionZ.getCurrentValue();
        parameterOptions = ParameterOptions.POSITION;
        changingParameterBasedOnX = true;
        changingParameterBasedOnY = false;
        changingParameterBasedOnZ = true;
        foregroundQuad.setVisible(true);
        startMousePosition.x = input.getMXpos();
        startMousePosition.y = input.getMYpos();
    }

    public void setNewColour(Vector3f newColour) {
        setNewColour(newColour.x, newColour.y, newColour.z);
    }

    public void setNewColour(float red, float green, float blue){
        pickedColour.x = red;
        pickedColour.y = green;
        pickedColour.z = blue;
        currentColourDisplay.setBlend(new Vector4f(pickedColour.x, pickedColour.y, pickedColour.z, 1), 1f);
        selectColourButton.setEnabled(true);
        if(lastLoadedEntity != null){
            lastLoadedEntity.setTextureBlendColour(pickedColour);
            lastLoadedEntity.setTextureBlendAmount((float) textureColourBlendArea.getCurrentValue());
        }
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


        chooseFrequencyBox = handler.newDependancy(new ChooseFrequencyBox() {
            @Override
            protected void onDisplayClosed(double frequency) {
                PingerHandler.togglePingingBuoy(lastLoadedEntity, frequency);
            }
        });
        chooseFrequencyBox.setVisible(false);
        TopLevelInterface.addComponentToTopLayer(chooseFrequencyBox);

        super.loadDependencies(handler);
    }

    public boolean isManipulatingObject() {
        return ((lastLoadedEntity != null) && (parameterOptions != ParameterOptions.NONE));
    }
}
