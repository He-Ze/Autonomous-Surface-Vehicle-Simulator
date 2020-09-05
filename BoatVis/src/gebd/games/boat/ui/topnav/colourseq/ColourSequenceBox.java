package gebd.games.boat.ui.topnav.colourseq;

import blindmystics.util.FileReader;
import gebd.Render;
import gebd.games.boat.entity.ColourSequenceNode;
import loader.LoadedObjectHandler;
import renderables.r2D.Renderable2DUpdateHandler;
import renderables.r2D.colourpicker.ColourPicker;
import renderables.r2D.composite.*;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.awt.*;

/**
 * Created by CaptainPete on 2016-11-12.
 */
public abstract class ColourSequenceBox extends UserInterface {

    public static final Vector2f DEFAULT_SIZE = new Vector2f(500, 500);
    public static final float DEFAULT_ROTATION = 0;

    private SimpleQuad contentBlockingQuad;

    private BorderedRect backgroundRect;

    protected TextButton selectColourButton;
    private ColourPicker colourPicker;
    private Vector3f currentColour = new Vector3f(1f, 1f, 1f);
    protected SimpleQuad currentColourDisplay;

    DoubleTextArea durationInMsDoubleTextArea;

    DoubleTextArea colourBlendTextArea;

    protected TextButton confirmButton;

    public ColourSequenceBox() {
        super(new Vector2f(Render.getCentreOfScreen()), DEFAULT_SIZE, DEFAULT_ROTATION);

        contentBlockingQuad = addComponentToBottom(new SimpleQuad(new Vector2f(0, 0), new Vector2f(Render.WIDTH, Render.HEIGHT), 0));
        contentBlockingQuad.setBlend(new Vector4f(0, 0, 0, 0.5f), 1);

        backgroundRect = addComponentToBottom(new BorderedRect(new Vector2f(), size, 0, 2));
        backgroundRect.setBackdropColour(new Vector4f(0.7f, 0.7f, 0.7f, 1f), 1f);

        Vector2f selectColourButtonSize = new Vector2f(200, 20);
        Vector2f selectColourButtonPosition = new Vector2f(0, 0);
        selectColourButton = addComponentToTop(new TextButton("Choose a colour", FileReader.asSharedFile("test_images/Buttonify.png"), selectColourButtonPosition, selectColourButtonSize, 0){
            @Override
            protected void onRelease() {
                colourPicker.setVisible(true);
                TopLevelInterface.getInstance().moveComponentToTop(colourPicker);
            }
        });

        Vector2f durationTextAreaSize = new Vector2f(200, 20);
        Vector2f durationTextAreaPosition = new Vector2f(0, 200);
        durationInMsDoubleTextArea = addComponentToTop(new DoubleTextArea(durationTextAreaPosition, durationTextAreaSize, "Courier", Font.PLAIN, 0.0, 60 * 1000));
        durationInMsDoubleTextArea.setValue(1000f);


        Vector2f durationHelperTextPosition = new Vector2f(durationTextAreaPosition);
        durationHelperTextPosition.y += 25;
        addComponentToTop(new String2D("Duration (ms)", String2D.StringAlignment.MID_MIDDLE, durationHelperTextPosition, new Vector2f(16, 16), "Courier", Font.PLAIN));


        Vector2f colourBlendAreaPosition = new Vector2f(0, 100);
        colourBlendTextArea = addComponentToTop(new DoubleTextArea(colourBlendAreaPosition, durationTextAreaSize, "Courier", Font.PLAIN, 0.0, 1f));
        colourBlendTextArea.setValue(1f);

        Vector2f blendHelperTextPosition = new Vector2f(colourBlendAreaPosition);
        blendHelperTextPosition.y += 25;
        addComponentToTop(new String2D("Colour Blend", String2D.StringAlignment.MID_MIDDLE, blendHelperTextPosition, new Vector2f(16, 16), "Courier", Font.PLAIN));


        Vector2f selectedColourQuadSize = new Vector2f(40, 40);
        Vector2f selectedColourQuadPosition = new Vector2f(0, -50);
        currentColourDisplay = addComponentToTop(new SimpleQuad(selectedColourQuadPosition, selectedColourQuadSize, 0));
        currentColourDisplay.setBlend(new Vector4f(currentColour.x, currentColour.y, currentColour.z, 1), 1);

        Vector2f confirmButtonSize = new Vector2f(200, 20);
        Vector2f confirmButtonPosition = new Vector2f(0, -200);
        confirmButton = addComponentToTop(new TextButton("Confirm", FileReader.asSharedFile("test_images/Buttonify.png"), confirmButtonPosition, confirmButtonSize, 0){
            @Override
            protected void onRelease() {
                closeDisplay();
                onDisplayClosed(new Vector3f(currentColour), (float) durationInMsDoubleTextArea.getCurrentValue(), (float) colourBlendTextArea.getCurrentValue());
            }
        });

    }

    private void closeDisplay(){
        setVisible(false);
    }

    @Override
    public void loadDependencies(LoadedObjectHandler<?> handler) {
        colourPicker = handler.newDependancy(new ColourPicker(){
            @Override
            protected void onDisplayClosed(float redVal, float greenVal, float blueVal) {
                currentColour.set(redVal, greenVal, blueVal);
                currentColourDisplay.setBlend(new Vector4f(currentColour.x, currentColour.y, currentColour.z, 1), 1);
            }
        });
        colourPicker.setVisible(false);
        TopLevelInterface.addComponentToTopLayer(colourPicker);
        super.loadDependencies(handler);
    }

    @Override
    public void completeLoad(LoadedObjectHandler<?> handler) {
        super.completeLoad(handler);
        Renderable2DUpdateHandler.addRenderableComponent(this);
    }

    public void show(ColourSequenceNode colourSequenceNode){
        super.setVisible(true);
        durationInMsDoubleTextArea.setValue(colourSequenceNode.getDuration());
        colourBlendTextArea.setValue(colourSequenceNode.getColourBlendAmount());
        currentColour.set(colourSequenceNode.getColour());
        currentColourDisplay.setBlend(currentColour.x, currentColour.y, currentColour.z, 1f, 1f);
    }

    public void hide(){
        super.setVisible(false);
    }

    protected abstract void onDisplayClosed(Vector3f colour, float duration, float colourBlendAmount);
}
