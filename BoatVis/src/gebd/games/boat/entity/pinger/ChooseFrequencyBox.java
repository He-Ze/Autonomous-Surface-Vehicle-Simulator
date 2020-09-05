package gebd.games.boat.entity.pinger;

import blindmystics.util.FileReader;
import gebd.Render;
import loader.LoadedObjectHandler;
import renderables.r2D.Renderable2DUpdateHandler;
import renderables.r2D.composite.BorderedRect;
import renderables.r2D.composite.DoubleTextArea;
import renderables.r2D.composite.TextButton;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.awt.*;

/**
 * Created by p3te on 21/11/16.
 */
public abstract class ChooseFrequencyBox extends UserInterface {

    public static final Vector2f DEFAULT_SIZE = new Vector2f(300, 300);
    public static final float DEFAULT_ROTATION = 0;

    private SimpleQuad contentBlockingQuad;

    private BorderedRect backgroundRect;

    protected TextButton confirmButton;

    DoubleTextArea frequencyTextArea;

    public ChooseFrequencyBox() {
        super(new Vector2f(Render.getCentreOfScreen()), DEFAULT_SIZE, DEFAULT_ROTATION);

        contentBlockingQuad = addComponentToBottom(new SimpleQuad(new Vector2f(0, 0), new Vector2f(Render.WIDTH, Render.HEIGHT), 0));
        contentBlockingQuad.setBlend(new Vector4f(0, 0, 0, 0.5f), 1);

        backgroundRect = addComponentToBottom(new BorderedRect(new Vector2f(), size, 0, 2));
        backgroundRect.setBackdropColour(new Vector4f(0.7f, 0.7f, 0.7f, 1f), 1f);

        Vector2f frequencyTextAreaSize = new Vector2f(200, 20);
        Vector2f frequencyTextAreaPosition = new Vector2f(0, 100);
        frequencyTextArea = addComponentToTop(new DoubleTextArea(frequencyTextAreaPosition, frequencyTextAreaSize, "Courier", Font.PLAIN, 0.0, Double.MAX_VALUE));
        frequencyTextArea.setValue(1000f);


        Vector2f frequencyHelperTextPosition = new Vector2f(frequencyTextAreaPosition);
        frequencyHelperTextPosition.y += 25;
        addComponentToTop(new String2D("Frequency (Hz)", String2D.StringAlignment.MID_MIDDLE, frequencyHelperTextPosition, new Vector2f(16, 16), "Courier", Font.PLAIN));

        Vector2f confirmButtonSize = new Vector2f(200, 20);
        Vector2f confirmButtonPosition = new Vector2f(0, -100);
        confirmButton = addComponentToTop(new TextButton("Confirm", FileReader.asSharedFile("test_images/Buttonify.png"), confirmButtonPosition, confirmButtonSize, 0){
            @Override
            protected void onRelease() {
                closeDisplay();
            }
        });

    }

    private void closeDisplay(){
        setVisible(false);
        onDisplayClosed(frequencyTextArea.getCurrentValue());
    }

    @Override
    public void completeLoad(LoadedObjectHandler<?> handler) {
        super.completeLoad(handler);
        Renderable2DUpdateHandler.addRenderableComponent(this);
    }

    protected abstract void onDisplayClosed(double frequency);

}
