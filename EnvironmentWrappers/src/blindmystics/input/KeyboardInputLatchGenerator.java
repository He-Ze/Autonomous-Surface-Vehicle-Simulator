package blindmystics.input;

import blindmystics.util.input.InputLatch;
import blindmystics.util.input.InputMapper;
import blindmystics.util.input.keyboard.HandleKeyboard;
import blindmystics.util.input.keyboard.KeyboardKeyMapper;
import blindmystics.util.input.keyboard.KeyboardModifierKey;

import java.util.HashMap;

/**
 * Created by CaptainPete on 8/7/2016.
 */
public class KeyboardInputLatchGenerator {

    private static HashMap<KeyboardKeyMapper, KeyboardKeyMapper> keyboardKeyMappers = new HashMap<>();

    public static InputLatch generateInputLatchForKey(int keyboardKey, KeyboardModifierKey... modifierKeys) {
        return new InputLatch(generateInputMapperForKey(keyboardKey, modifierKeys));
    }

    public static InputMapper generateInputMapperForKey(int keyboardKey, KeyboardModifierKey... modifierKeys) {
        KeyboardKeyMapper keyboardKeyMapper = new KeyboardKeyMapper(keyboardKey, modifierKeys);
        if (keyboardKeyMappers.containsKey(keyboardKeyMapper)) {
            keyboardKeyMapper = keyboardKeyMappers.get(keyboardKeyMapper);
        } else {
            HandleKeyboard.addKeyboardMapper(keyboardKeyMapper);
            keyboardKeyMappers.put(keyboardKeyMapper, keyboardKeyMapper);
        }
        return keyboardKeyMapper;
    }

}
