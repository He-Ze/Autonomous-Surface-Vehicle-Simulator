package blindmystics.util.input;

import blindmystics.util.input.mouse.ButtonStatus;
import gebd.Render;

/**
 * Created by CaptainPete on 8/7/2016.
 */
public class InputMapper {

    private int frameLastUpdated = -1;

    protected ButtonStatus buttonStatus = ButtonStatus.UP;

    protected InputMapper() {}

    boolean alreadyConsumedThisFrame() {
        int currentFrame = Render.getCurrentFrame();
        boolean alreadyConsumed = (currentFrame == frameLastUpdated);
        frameLastUpdated = currentFrame;
        return alreadyConsumed;
    }

    ButtonStatus getButtonStatus() {
        return buttonStatus;
    }

}
