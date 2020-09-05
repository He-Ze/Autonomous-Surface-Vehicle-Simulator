package renderables.r2D.colourpicker;

import blindmystics.input.CurrentInput;
import blindmystics.util.FileReader;
import gebd.Render;
import blindmystics.util.input.mouse.InputStatus;
import loader.LoadedObjectHandler;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;
import renderables.r2D.Renderable2DUpdateHandler;
import renderables.r2D.composite.BorderedRect;
import renderables.r2D.composite.DoubleTextArea;
import renderables.r2D.composite.TextButton;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;
import renderables.texture.TextureInfo;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by CaptainPete on 9/05/2016.
 */
public class ColourPicker extends UserInterface {

    public static final Vector2f DEFAULT_SIZE = new Vector2f(700, 700);
    public static final float DEFAULT_ROTATION = 0;

    private SimpleQuad contentBlockingQuad;

    private BorderedRect backgroundRect;

    private SimpleQuad colourMapQuad;
    private TextureInfo colourMapTextureInfo;
    private ByteBuffer colourMapBuffer;
    private int colourMapTexureWidth;
    private int colourMapTexureHeight;

    private SimpleQuad currentSelectionQuad;

    private boolean mouseSelectingColourFromMap = false;

    private TextButton confirmButton;

    private float absoluteRedValue = 1;
    private float absoluteGreenValue = 1;
    private float absoluteBlueValue = 1;

    private float redValue = 1;
    private float greenValue = 1;
    private float blueValue = 1;

    DoubleTextArea brightnessDoubleTextArea;

    public ColourPicker(){
        super(new Vector2f(Render.getCentreOfScreen()), DEFAULT_SIZE, DEFAULT_ROTATION);

        //new Vector2f(Render.WIDTH / 2f, Render.HEIGHT / 2f)

        contentBlockingQuad = addComponentToBottom(new SimpleQuad(new Vector2f(0, 0), new Vector2f(Render.WIDTH, Render.HEIGHT), 0));
        contentBlockingQuad.setBlend(new Vector4f(0, 0, 0, 0.5f), 1);

        backgroundRect = addComponentToBottom(new BorderedRect(new Vector2f(), size, 0, 2));
        backgroundRect.setBackdropColour(new Vector4f(0.7f, 0.7f, 0.7f, 1f), 1f);

        Vector2f colourMapSize = new Vector2f(512, 512);
        Vector2f colourMapPosition = new Vector2f();
        //Move it the top left of the screen.
        colourMapPosition.x -= (size.x - colourMapSize.x) / 2f;
        colourMapPosition.y += (size.y - colourMapSize.y) / 2f;
        colourMapQuad = addComponentToTop(new SimpleQuad(colourMapPosition, colourMapSize, 0));

        Vector2f currentSelectionSize = new Vector2f(100, 100);
        Vector2f currentSelectionPosition = new Vector2f();
        currentSelectionPosition.x += (size.x - currentSelectionSize.x) / 2f;
        currentSelectionQuad = addComponentToTop(new SimpleQuad(currentSelectionPosition, currentSelectionSize, 0));
        currentSelectionQuad.setBlend(new Vector4f(1, 1, 1, 1), 1);



        //newTextArea = addComponentToTop(new DoubleTextArea(position, new Vector2f(150, 20), "Courier", Font.PLAIN, minVal, maxVal));
        //brightnessDoubleTextArea
        Vector2f brighnessAreaPosition = new Vector2f(0, -250);
        brightnessDoubleTextArea = addComponentToTop(new DoubleTextArea(brighnessAreaPosition, new Vector2f(200, 20), "Courier", Font.PLAIN, 0, 1));
        brightnessDoubleTextArea.setValue(1);

        addComponentToTop(new String2D("Brightness", String2D.StringAlignment.MID_MIDDLE, new Vector2f(0, -220), new Vector2f(16, 16), "Courier", Font.PLAIN));


        Vector2f confirmButtonSize = new Vector2f(50, 50);
        Vector2f confirmButtonPosition = new Vector2f(0, (confirmButtonSize.y - size.y) / 2f);
        confirmButton = addComponentToTop(new TextButton("OK", FileReader.asSharedFile("test_images/Buttonify.png"), confirmButtonPosition, confirmButtonSize, 0){
            @Override
            public void onRelease() {
                closeDisplay();
            }
        });
    }

    private void closeDisplay(){
        this.setVisible(false);
        onDisplayClosed(redValue, greenValue, blueValue);
    }

    /**
     * Currently doesn't do anything, but can be overridden.
     */
    protected void onDisplayClosed(float redVal, float greenVal, float blueVal){

    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);

        float blendWithBlack = (float) Math.pow(1 - brightnessDoubleTextArea.getCurrentValue(), 0.5);
        colourMapQuad.setBlend(new Vector4f(0, 0, 0, 1), blendWithBlack);

        if(InputStatus.isButtonDown(input.getLeftMouse()) && input.receivedMouseEvent(colourMapQuad)){
            mouseSelectingColourFromMap = true;
        }
        if(InputStatus.isButtonUp(input.getLeftMouse())){
            mouseSelectingColourFromMap = false;
        }

        if(mouseSelectingColourFromMap){
            //The quad received the mouse click.
            double mxPos = input.getMXpos();
            double myPos = input.getMYpos();
            double quadStartX = colourMapQuad.getAbsolutePosition().x - (colourMapQuad.getSize().x / 2.0);
            double quadEndX = colourMapQuad.getAbsolutePosition().x + (colourMapQuad.getSize().x / 2.0);
            double quadStartY = colourMapQuad.getAbsolutePosition().y - (colourMapQuad.getSize().y / 2.0);
            double quadEndY = colourMapQuad.getAbsolutePosition().y + (colourMapQuad.getSize().y / 2.0);

            if(mxPos < quadStartX){
                Mouse.setCursorPosition((int) quadStartX, (int) myPos);
                mxPos = quadStartX;
            } else if(mxPos >= quadEndX){
                Mouse.setCursorPosition((int) quadEndX, (int) myPos);
                mxPos = quadEndX;
            }

            if(myPos < quadStartY){
                Mouse.setCursorPosition((int) mxPos, (int) quadStartY);
                myPos = quadStartY;
            } else if(myPos >= quadEndY){
                Mouse.setCursorPosition((int) mxPos, (int) quadEndY);
                myPos = quadEndY;
            }


            double percentInX = (mxPos - quadStartX) / (quadEndX - quadStartX);
            double percentInY = 1 - ((myPos - quadStartY) / (quadEndY - quadStartY));
            int xIndex = (int) (percentInX * colourMapTexureWidth);
            int yIndex = (int) (percentInY * colourMapTexureHeight);

            int bufferIndex = ((yIndex * colourMapTexureWidth) + xIndex) * 4;
            if ((bufferIndex > (colourMapBuffer.limit() - 4)) || (bufferIndex < 0)) {
                //Forget about it. :P
            } else {
                colourMapBuffer.position(((yIndex * colourMapTexureWidth) + xIndex) * 4);
                absoluteRedValue = (0x000000FF & colourMapBuffer.get()) / 255.0f;
                absoluteGreenValue = (0x000000FF & colourMapBuffer.get()) / 255.0f;
                absoluteBlueValue = (0x000000FF & colourMapBuffer.get()) / 255.0f;
                float alphaVal = (0x000000FF & colourMapBuffer.get()) / 255.0f;
            }
        }

//        vec4 col1Squared = pow(col1, vec4(2, 2, 2, 2));
//        vec4 col2Squared = pow(col2, vec4(2, 2, 2, 2));
//        vec4 finalCol = sqrt(mix(col1Squared, col2Squared, mixAmount));



        redValue = niceMix(absoluteRedValue, 0, blendWithBlack);
        greenValue = niceMix(absoluteGreenValue, 0, blendWithBlack);
        blueValue = niceMix(absoluteBlueValue, 0, blendWithBlack);

        currentSelectionQuad.setBlend(redValue, greenValue, blueValue, 1, 1);

//        if(HandleKeyboard.enterHandler.justPressed()){
//            closeDisplay();
//        }
    }

    private float niceMix(double a, double b, double amount){
        double aSquared = a * a;
        double bSquared = b * b;
        double mixedValue = (aSquared * (1 - amount)) + (bSquared * amount);
        double result = Math.sqrt(mixedValue);
        return ((float) result);
    }

    @Override
    public void loadDependencies(LoadedObjectHandler<?> handler) {
        super.loadDependencies(handler);
        colourMapTextureInfo = TextureInfo.queueLoadOfPNGTexture(FileReader.asSharedFile("shared/colourPicker/map-brightness.png"), GL11.GL_LINEAR, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_CLAMP, handler);
    }

    @Override
    public void completeLoad(LoadedObjectHandler<?> handler) {
        super.completeLoad(handler);
        colourMapQuad.setTexture(colourMapTextureInfo);
        try {
            colourMapTextureInfo.loadTextureDataFromFile();
            colourMapBuffer = colourMapTextureInfo.getBuf();
            colourMapTexureWidth = colourMapTextureInfo.get_tWidth();
            colourMapTexureHeight = colourMapTextureInfo.get_tHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Renderable2DUpdateHandler.addRenderableComponent(this);
    }

    @Override
    public void render() {

        super.render();
    }

}
