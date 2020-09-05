package blindmystics.util.input.keyboard;

import blindmystics.util.input.InputMapper;
import blindmystics.util.input.mouse.ButtonStatus;

import java.util.Arrays;

/**
 * Created by P3TE on 8/10/2016.
 * A class that will be used with the new input regime.
 */
public class KeyboardKeyMapper extends InputMapper {

    private int hashCode;

    private final Boolean[] modifierKeysMap = new Boolean[KeyboardModifierKey.values().length];

    private ButtonStatus absoluteButtonStatus = ButtonStatus.UP;

    private final int keyboardKey;

    public KeyboardKeyMapper(int key, KeyboardModifierKey... modifierKeys) {
        hashCode = generateHash(key, modifierKeys);
        this.keyboardKey = key;
        for (KeyboardModifierKey modifierKey : modifierKeys) {
            this.modifierKeysMap[modifierKey.ordinal()] = true;
        }
    }

    public int getKeyboardKey() {
        return keyboardKey;
    }

    /**
     * It may be interesting to add different functionality to this.
     * However it still holds for the moment.
     * @param newButtonStatus
     */
    public void updateButtonStatus(ButtonStatus newButtonStatus) {
        this.absoluteButtonStatus = newButtonStatus;
        if (prerequisiteKeysSatisfied()) {
            buttonStatus = absoluteButtonStatus;
        }
    }


    public void updateButtonStatus(boolean isKeyDown) {
        if (isKeyDown) {
            buttonStatus = ButtonStatus.JUST_PRESSED;
        } else {
            buttonStatus = ButtonStatus.JUST_RELEASED;
        }
    }

    public void update(){
        if (buttonStatus == ButtonStatus.JUST_PRESSED) {
            buttonStatus = ButtonStatus.DOWN;
        } else if (buttonStatus == ButtonStatus.JUST_RELEASED) {
            buttonStatus = ButtonStatus.UP;
        }
    }

    /**
     * If shiftHeldRequired = true
     * and HandleKeyboard.isShiftHeld()
     * @return true
     *
     * If shiftHeldRequired = false
     * and !HandleKeyboard.isShiftHeld()
     * @return true
     *
     * else
     * @return false
     *
     * This applies for:
     * shiftHeldRequired
     * ctrlHeldRequired
     * altHeldRequired
     */
    public boolean prerequisiteKeysSatisfied() {
        boolean prerequisiteKeysSatisfied = true;
        prerequisiteKeysSatisfied &= modifierKeysMap[KeyboardModifierKey.SHIFT.ordinal()] == HandleKeyboard.isShiftHeld();
        prerequisiteKeysSatisfied &= modifierKeysMap[KeyboardModifierKey.CTRL.ordinal()] == HandleKeyboard.isControlHeld();
        prerequisiteKeysSatisfied &= modifierKeysMap[KeyboardModifierKey.ALT.ordinal()] == HandleKeyboard.isAltHeld();
        return prerequisiteKeysSatisfied;
    }


    public int generateHash(int key, KeyboardModifierKey... modifierKeys) {
        int hash = key;
        Arrays.sort(modifierKeys);
        int[] modifierList = new int[KeyboardModifierKey.values().length];
        for (int i = 0; i < modifierKeys.length; i++) {
            KeyboardModifierKey modifierKey = modifierKeys[i];
            modifierList[modifierKey.ordinal()] = 1;
        }
        for (int i = 0; i < modifierList.length; i++) {
            hash *= 2;
            hash += modifierList[i];
        }
        return hash;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof KeyboardKeyMapper)) {
            return false;
        }
        KeyboardKeyMapper other = (KeyboardKeyMapper) obj;
        boolean equal = this.keyboardKey == other.keyboardKey;
        if (this.modifierKeysMap.length != other.modifierKeysMap.length) {
            return false;
        }
        for (int i = 0; i < modifierKeysMap.length; i++) {
            equal &= (this.modifierKeysMap[i] == other.modifierKeysMap[i]);
        }
        return equal;
    }
}
