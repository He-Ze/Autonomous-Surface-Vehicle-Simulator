package gebd.games.boat.input;

import blindmystics.input.KeyboardInputLatchGenerator;
import blindmystics.util.input.GameKeyMappings;
import blindmystics.util.input.ModifiableInputLatch;
import blindmystics.util.input.ModifiableInputMapper;
import org.lwjgl.input.Keyboard;

/**
 * Created by p3te on 15/08/16.
 */
public class BoatVisInput extends GameKeyMappings {


    public static final ModifiableInputLatch MOVE_CAMERA_FORWARD_KEY_HANDLER = addMapper(
            new ModifiableInputMapper("Camera Forward", "Move the camera forward.",
                    KeyboardInputLatchGenerator.generateInputMapperForKey(Keyboard.KEY_W)));


    public static final ModifiableInputLatch CAMERA_CYCLE_KEY_HANDLER = addMapper(
            new ModifiableInputMapper("Camera Cycle", "Cycle the camera.",
                    KeyboardInputLatchGenerator.generateInputMapperForKey(Keyboard.KEY_SPACE)));


    public static final ModifiableInputLatch MOVE_BOAT_KEY_HANDLER = addMapper(
            new ModifiableInputMapper("Move Boat", "Move the boat.",
                    KeyboardInputLatchGenerator.generateInputMapperForKey(Keyboard.KEY_M)));

    public static final ModifiableInputLatch TOGGLE_NORTH_KEY_HANDLER = addMapper(
            new ModifiableInputMapper("Toggle North Arrow", "Toggles the North Arrow.",
                    KeyboardInputLatchGenerator.generateInputMapperForKey(Keyboard.KEY_N)));

    public static final ModifiableInputLatch TOGGLE_UI_HANDLER = addMapper(
            new ModifiableInputMapper("Toggles UI Visibility", "Toggles visibility of the UI.",
                    KeyboardInputLatchGenerator.generateInputMapperForKey(Keyboard.KEY_F1)));

    public static final ModifiableInputLatch FULL_SCREEN_TOGGLE = addMapper(
            new ModifiableInputMapper("Toggles Fullscreen (Dodgy)", "Toggles fullscreen in a dodgy way.",
                    KeyboardInputLatchGenerator.generateInputMapperForKey(Keyboard.KEY_F2)));

}
