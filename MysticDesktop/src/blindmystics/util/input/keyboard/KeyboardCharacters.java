package blindmystics.util.input.keyboard;

import org.lwjgl.input.Keyboard;

/**
 * Used for generating the array that is used for mapping
 * the keyboard keys to the corresponding String character.
 * Created by P3TE on 11/03/2016.
 */
public class KeyboardCharacters {

    //Normal character set.
    private static final String[] NORMAL_CHARS = generateNormalCharacters();
    //Shift held cahracters.
    private static final String[] SHIFT_CHARS = generateShiftCharacters();

    /**
     * @param keyCode - the keyboard key
     * @return - String the key mapped to the keycode.
     */
    public static String getChar(int keyCode){
        if((keyCode < 0) || (keyCode > NORMAL_CHARS.length)){
            return null;
        }
        return NORMAL_CHARS[keyCode];
    }

    /**
     * @param keyCode - the keyboard key
     * @return - String the key mapped to the keyboard with shift held.
     */
    public static String getShiftChar(int keyCode){
        if((keyCode < 0) || (keyCode > SHIFT_CHARS.length)){
            return null;
        }
        return SHIFT_CHARS[keyCode];
    }

    /**
     * The characters without [SHIFT] held.
     * @return - The default mapping of the characters on the keyboard.
     */
    private static String[] generateNormalCharacters(){
        String[] genChars = new String[256];
        genChars[Keyboard.KEY_0] = "0";
        genChars[Keyboard.KEY_1] = "1";
        genChars[Keyboard.KEY_2] = "2";
        genChars[Keyboard.KEY_3] = "3";
        genChars[Keyboard.KEY_4] = "4";
        genChars[Keyboard.KEY_5] = "5";
        genChars[Keyboard.KEY_6] = "6";
        genChars[Keyboard.KEY_7] = "7";
        genChars[Keyboard.KEY_8] = "8";
        genChars[Keyboard.KEY_9] = "9";
        genChars[Keyboard.KEY_A] = "a";
        genChars[Keyboard.KEY_B] = "b";
        genChars[Keyboard.KEY_C] = "c";
        genChars[Keyboard.KEY_D] = "d";
        genChars[Keyboard.KEY_E] = "e";
        genChars[Keyboard.KEY_F] = "f";
        genChars[Keyboard.KEY_G] = "g";
        genChars[Keyboard.KEY_H] = "h";
        genChars[Keyboard.KEY_I] = "i";
        genChars[Keyboard.KEY_J] = "j";
        genChars[Keyboard.KEY_K] = "k";
        genChars[Keyboard.KEY_L] = "l";
        genChars[Keyboard.KEY_M] = "m";
        genChars[Keyboard.KEY_N] = "n";
        genChars[Keyboard.KEY_O] = "o";
        genChars[Keyboard.KEY_P] = "p";
        genChars[Keyboard.KEY_Q] = "q";
        genChars[Keyboard.KEY_R] = "r";
        genChars[Keyboard.KEY_S] = "s";
        genChars[Keyboard.KEY_T] = "t";
        genChars[Keyboard.KEY_U] = "u";
        genChars[Keyboard.KEY_V] = "v";
        genChars[Keyboard.KEY_W] = "w";
        genChars[Keyboard.KEY_X] = "x";
        genChars[Keyboard.KEY_Y] = "y";
        genChars[Keyboard.KEY_Z] = "z";
        genChars[Keyboard.KEY_AT] = "@";
        genChars[Keyboard.KEY_ADD] = "+";
        genChars[Keyboard.KEY_APOSTROPHE] = "'";
        genChars[Keyboard.KEY_BACKSLASH] = "\\";
        genChars[Keyboard.KEY_COLON] = ":";
        genChars[Keyboard.KEY_COMMA] = ",";
        genChars[Keyboard.KEY_DECIMAL] = ".";
        genChars[Keyboard.KEY_DIVIDE] = "/";
        genChars[Keyboard.KEY_EQUALS] = "=";
        genChars[Keyboard.KEY_GRAVE] = "`";
        genChars[Keyboard.KEY_LBRACKET] = "[";
        genChars[Keyboard.KEY_MINUS] = "-";
        genChars[Keyboard.KEY_MULTIPLY] = "*";
        genChars[Keyboard.KEY_NUMPAD0] = "0";
        genChars[Keyboard.KEY_NUMPAD1] = "1";
        genChars[Keyboard.KEY_NUMPAD2] = "2";
        genChars[Keyboard.KEY_NUMPAD3] = "3";
        genChars[Keyboard.KEY_NUMPAD4] = "4";
        genChars[Keyboard.KEY_NUMPAD5] = "5";
        genChars[Keyboard.KEY_NUMPAD6] = "6";
        genChars[Keyboard.KEY_NUMPAD7] = "7";
        genChars[Keyboard.KEY_NUMPAD8] = "8";
        genChars[Keyboard.KEY_NUMPAD9] = "9";
        genChars[Keyboard.KEY_NUMPADCOMMA] = ",";
        genChars[Keyboard.KEY_NUMPADEQUALS] = "=";
        genChars[Keyboard.KEY_PERIOD] = ".";
        genChars[Keyboard.KEY_RBRACKET] = "]";
        genChars[Keyboard.KEY_SEMICOLON] = ";";
        genChars[Keyboard.KEY_SLASH] = "/";
        genChars[Keyboard.KEY_SPACE] = " ";
        genChars[Keyboard.KEY_SUBTRACT] = "-";
        return genChars;
    }

    /**
     * The characters with [SHIFT] held.
     * @return - The default mapping of the characters on the keyboard with shift held.
     */
    private static String[] generateShiftCharacters(){
        String[] genChars = new String[256];
        genChars[Keyboard.KEY_0] = ")";
        genChars[Keyboard.KEY_1] = "!";
        genChars[Keyboard.KEY_2] = "@";
        genChars[Keyboard.KEY_3] = "#";
        genChars[Keyboard.KEY_4] = "$";
        genChars[Keyboard.KEY_5] = "%";
        genChars[Keyboard.KEY_6] = "^";
        genChars[Keyboard.KEY_7] = "&";
        genChars[Keyboard.KEY_8] = "*";
        genChars[Keyboard.KEY_9] = "(";
        genChars[Keyboard.KEY_A] = "A";
        genChars[Keyboard.KEY_B] = "B";
        genChars[Keyboard.KEY_C] = "C";
        genChars[Keyboard.KEY_D] = "D";
        genChars[Keyboard.KEY_E] = "E";
        genChars[Keyboard.KEY_F] = "F";
        genChars[Keyboard.KEY_G] = "G";
        genChars[Keyboard.KEY_H] = "H";
        genChars[Keyboard.KEY_I] = "I";
        genChars[Keyboard.KEY_J] = "J";
        genChars[Keyboard.KEY_K] = "K";
        genChars[Keyboard.KEY_L] = "L";
        genChars[Keyboard.KEY_M] = "M";
        genChars[Keyboard.KEY_N] = "N";
        genChars[Keyboard.KEY_O] = "O";
        genChars[Keyboard.KEY_P] = "P";
        genChars[Keyboard.KEY_Q] = "Q";
        genChars[Keyboard.KEY_R] = "R";
        genChars[Keyboard.KEY_S] = "S";
        genChars[Keyboard.KEY_T] = "T";
        genChars[Keyboard.KEY_U] = "U";
        genChars[Keyboard.KEY_V] = "V";
        genChars[Keyboard.KEY_W] = "W";
        genChars[Keyboard.KEY_X] = "X";
        genChars[Keyboard.KEY_Y] = "Y";
        genChars[Keyboard.KEY_Z] = "Z";
        genChars[Keyboard.KEY_AT] = "@";
        genChars[Keyboard.KEY_ADD] = "+";
        genChars[Keyboard.KEY_APOSTROPHE] = "\"";
        genChars[Keyboard.KEY_BACKSLASH] = "|";
        genChars[Keyboard.KEY_COLON] = ";";
        genChars[Keyboard.KEY_COMMA] = "<";
        genChars[Keyboard.KEY_DECIMAL] = ">";
        genChars[Keyboard.KEY_DIVIDE] = "/";
        genChars[Keyboard.KEY_EQUALS] = "+";
        genChars[Keyboard.KEY_GRAVE] = "~";
        genChars[Keyboard.KEY_LBRACKET] = "{";
        genChars[Keyboard.KEY_MINUS] = "_";
        genChars[Keyboard.KEY_MULTIPLY] = "*";
        genChars[Keyboard.KEY_NUMPAD0] = "0";
        genChars[Keyboard.KEY_NUMPAD1] = "1";
        genChars[Keyboard.KEY_NUMPAD2] = "2";
        genChars[Keyboard.KEY_NUMPAD3] = "3";
        genChars[Keyboard.KEY_NUMPAD4] = "4";
        genChars[Keyboard.KEY_NUMPAD5] = "5";
        genChars[Keyboard.KEY_NUMPAD6] = "6";
        genChars[Keyboard.KEY_NUMPAD7] = "7";
        genChars[Keyboard.KEY_NUMPAD8] = "8";
        genChars[Keyboard.KEY_NUMPAD9] = "9";
        genChars[Keyboard.KEY_NUMPADCOMMA] = ",";
        genChars[Keyboard.KEY_NUMPADEQUALS] = "=";
        genChars[Keyboard.KEY_PERIOD] = ">";
        genChars[Keyboard.KEY_RBRACKET] = "}";
        genChars[Keyboard.KEY_SEMICOLON] = ";";
        genChars[Keyboard.KEY_SLASH] = "?";
        genChars[Keyboard.KEY_SPACE] = " ";
        genChars[Keyboard.KEY_SUBTRACT] = "-";
        return genChars;
    }


}
