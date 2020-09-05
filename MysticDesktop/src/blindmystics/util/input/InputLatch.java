package blindmystics.util.input;

import blindmystics.util.input.mouse.ButtonStatus;
import blindmystics.util.input.mouse.InputStatus;
import gebd.Render;

/**
 * Created by CaptainPete on 8/7/2016.
 */
public class InputLatch {

    int currentFrameUpdated = -1;

    private ButtonStatus buttonStatus;

    private InputMapper inputMapper;

    public InputLatch(InputMapper inputMapper) {
        this.inputMapper = inputMapper;
    }

    /**
     * Steals the input so that nothing else this frame can!
     * @return - The current status of the button.
     */
    public ButtonStatus consumeInput() {
        int currentRenderedFrame = Render.getCurrentFrame();
        if (currentRenderedFrame == currentFrameUpdated) {
            return buttonStatus;
        }
        currentFrameUpdated = currentRenderedFrame;
        if (inputMapper.alreadyConsumedThisFrame()) {
            if (InputStatus.isButtonDown(buttonStatus)) {
                buttonStatus = ButtonStatus.JUST_RELEASED;
            } else {
                buttonStatus = ButtonStatus.UP;
            }
        } else {
            buttonStatus = inputMapper.getButtonStatus();
        }
        return buttonStatus;
    }

    public ButtonStatus peekWithoutConsumption() {
        return inputMapper.getButtonStatus();
    }


    public boolean isHeld() {
        return InputStatus.isButtonDown(consumeInput());
    }

    public boolean isReleased() {
        return InputStatus.isButtonUp(consumeInput());
    }

    public boolean justPressed() {
        return (consumeInput() == ButtonStatus.JUST_PRESSED);
    }

    public boolean justReleased() {
        return (consumeInput() == ButtonStatus.JUST_RELEASED);
    }

}
