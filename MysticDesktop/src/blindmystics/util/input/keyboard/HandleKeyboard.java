package blindmystics.util.input.keyboard;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class HandleKeyboard {

	private static boolean leftShiftHeld = false;
	private static boolean rightShiftHeld = false;
	private static boolean leftAltHeld = false;
	private static boolean rightAltHeld = false;
	private static boolean leftCtrlHeld = false;
	private static boolean rightCtrlHeld = false;

	private static ArrayList<KeyboardKeyMapper>[] keyboardMappers;

	protected AmbiguousKeyHandler ambiguousKeyHandler = null;

	public static boolean keysLocked = false;

	public HandleKeyboard(){
		keyboardMappers = new ArrayList[Keyboard.KEYBOARD_SIZE];
		for (int keyboardLatchNo = 0; keyboardLatchNo < keyboardMappers.length; keyboardLatchNo++) {
			keyboardMappers[keyboardLatchNo] = new ArrayList<>();
		}
	}

	public static void addKeyboardMapper(KeyboardKeyMapper keyboardKeyMapper) {
		keyboardMappers[keyboardKeyMapper.getKeyboardKey()].add(keyboardKeyMapper);
	}

	/**
	 * @return - Whether or not either shift key is currently held.
     */
	public static boolean isShiftHeld(){
		return (leftShiftHeld || rightShiftHeld);
	}

	/**
	 * @return - Whether or not either ctrl key is currently held.
	 */
	public static boolean isControlHeld(){
		return (leftCtrlHeld || rightCtrlHeld);
	}

	/**
	 * @return - Whether or not either alt key is currently held.
	 */
	public static boolean isAltHeld() {
		return (leftAltHeld || rightAltHeld);
	}
	
	
	/*
	 * Takes a integer representing a keyboardEventKey
	 * and a (non null) handler.
	 * swaps the handler to be attached to the new Key
	 * 
	 * If there was a handler already there, sets it to be
	 * the Key of the handler passed to this function.
	 */
	public void swapKeyHandler(int newKey, KeyboardKeyMapper handler){
		throw new UnsupportedOperationException("This has changed, and needs revision some time.");
	}
	
	
	public boolean withinKeyboardKeyRange(int number){
		return ((number >= 0) && (number < keyboardMappers.length));
	}
	
	
	public void handleKeyboard(){
		//Eventsize = 18?
		//Keyboard.getKeyCound = 131.
		
		//System.out.println("Event Key Count = " + Keyboard.getKeyCount());
		//System.out.println("Keyboard.getEventCharacter() = " + Keyboard.getEventCharacter());
		//System.out.println("Key Pressed (" + Keyboard.getEventCharacter() + ") = " + Keyboard.getEventKey());
		//Keyboard.next()
		
		//System.out.println("Keyboard.next() = " + Keyboard.next());

		for (int keyCode = 0; keyCode < keyboardMappers.length; keyCode++) {
			ArrayList<KeyboardKeyMapper> keyboardKeyMapperList = keyboardMappers[keyCode];
			for (KeyboardKeyMapper keyboardKeyMapper : keyboardKeyMapperList) {
				keyboardKeyMapper.update();
			}
		}
		
		while(Keyboard.next()) {
			//System.out.println("Key Pressed (" + Keyboard.getEventCharacter() + ") = " + Keyboard.getEventKey());
			//System.out.println("Keyboard.KEY_W = " + Keyboard.KEY_W);
			//Iterate through all the keys that have had events during this time.
			
			int keyCode = Keyboard.getEventKey();
			boolean keyboardEventKeyState = Keyboard.getEventKeyState();
			handleShiftCtrlAltIfApplicable(keyCode, keyboardEventKeyState);
			
			if(ambiguousKeyHandler != null){
				ambiguousKeyHandler.handleKey(keyboardEventKeyState, keyCode, this);
			}
			ArrayList<KeyboardKeyMapper> keyboardKeyMapperList = keyboardMappers[keyCode];
			for (KeyboardKeyMapper keyboardKeyMapper : keyboardKeyMapperList) {
				keyboardKeyMapper.updateButtonStatus(keyboardEventKeyState);
			}
			//Otherwise, No handler exists, do nothing.
		}
	}

	private void handleShiftCtrlAltIfApplicable(int keyboardKey, boolean keyDown) {
		switch (keyboardKey) {
			case Keyboard.KEY_LSHIFT:
				leftShiftHeld = keyDown;
				break;
			case Keyboard.KEY_RSHIFT:
				rightShiftHeld = keyDown;
				break;
			case Keyboard.KEY_LMENU:
				leftAltHeld = keyDown;
				break;
			case Keyboard.KEY_RMENU:
				rightAltHeld = keyDown;
				break;
			case Keyboard.KEY_LCONTROL:
				leftCtrlHeld = keyDown;
				break;
			case Keyboard.KEY_RCONTROL:
				rightCtrlHeld = keyDown;
				break;
			default:
				//Do nothing.
				break;
		}
	}
	
	public void setNewAmbiguousKeyHandler(AmbiguousKeyHandler newAmbiguousKeyHandler){
		if(ambiguousKeyHandler != null){
			ambiguousKeyHandler.onDeselect();
		}
		this.ambiguousKeyHandler = newAmbiguousKeyHandler;
		if(this.ambiguousKeyHandler == null){
			keysLocked = false;
			return;
		} else {
			ambiguousKeyHandler.onSelect();
		}
		keysLocked = newAmbiguousKeyHandler.controlsAllKeyEvents();
	}
	
	public AmbiguousKeyHandler getAmbiguousKeyHandler(){
		return this.ambiguousKeyHandler;
	}
	
	public void putAmbiguousKeyHandler(AmbiguousKeyHandler ambiguousKeyHandler){
		setNewAmbiguousKeyHandler(ambiguousKeyHandler);
	}
	
	public void clearAmbiguousKeyHandler(AmbiguousKeyHandler ambiguousKeyHandler){
		if(getAmbiguousKeyHandler() == null){
			setNewAmbiguousKeyHandler(null);
		} else if(getAmbiguousKeyHandler().equals(ambiguousKeyHandler)){
			setNewAmbiguousKeyHandler(null);
		}
	}

	public static boolean areKeysLocked() {
		return keysLocked;
	}
}
