package renderables.r2D.composite.userinterface.util;

import blindmystics.util.FileReader;
import gebd.Render;
import javax.vecmath.Vector2f;
import renderables.r2D.composite.TextButton;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.*;
import renderables.r2D.simple.Button;
import renderables.r2D.text.String2D;

import java.awt.*;

/**
 * Created by CaptainPete on 8/2/2016.
 */
public abstract class OppressiveInformationUi extends UserInterface {

    private SimpleQuad backgroundQuad;
    private String2D messageString2D;
    private TextButton okButton;

    private static final Vector2f OK_BUTTON_POSITION = new Vector2f(0, -50);
    private static final Vector2f OK_BUTTON_SIZE = new Vector2f(200, 50);

    private static final Vector2f MESSAGE_POSITION = new Vector2f(0, 50);
    private static final Vector2f MESSAGE_SIZE = new Vector2f(24, 24);
    private static final String MESSAGE_FONT = "Courier";
    private static final int MESSAGE_FONT_TYPE = Font.PLAIN;

    public OppressiveInformationUi(String message) {
        super(Render.instance.getCentreOfScreen(), Render.instance.getScreenSize(), 0);

        backgroundQuad = addComponentToBottom(new SimpleQuad(new Vector2f(), this.size, 0));
        backgroundQuad.setBlend(0, 0, 0, 0.5f, 1f);

        messageString2D = addComponentToTop(new String2D(message, String2D.StringAlignment.MID_MIDDLE, MESSAGE_POSITION, MESSAGE_SIZE, MESSAGE_FONT, MESSAGE_FONT_TYPE));

        okButton = addComponentToTop(new TextButton("Acknowledged.", Button.BUTONIFY_TEXTURE, OK_BUTTON_POSITION, OK_BUTTON_SIZE, 0) {
            @Override
            protected void onRelease() {
                super.onRelease();
                onClose();
            }
        });
    }

    protected abstract void onClose();
}
