package renderables.r2D.composite;

import java.util.HashSet;

import blindmystics.input.CurrentInput;
import blindmystics.util.input.keyboard.KeyboardCharacters;
import blindmystics.util.input.util.ClipboardHandler;
import org.lwjgl.input.Keyboard;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

import gebd.shaders.Shader2D;
import blindmystics.util.input.keyboard.AmbiguousKeyHandler;
import blindmystics.util.input.keyboard.HandleKeyboard;
import blindmystics.util.input.mouse.ButtonStatus;
import loader.LoadedObject;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import renderables.r2D.DisplayBoundary;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.ImageFont;
import renderables.r2D.text.String2D;
import renderables.r2D.text.String2D.StringAlignment;

/**
 * An object that takes keyboard input and displays it.
 * 
 * @author Peter Smith 43180543
 */
public class TextArea extends BorderedRect implements AmbiguousKeyHandler, LoadedObject {

	// Align the text from left to right.
	protected StringAlignment alignment = StringAlignment.MID_LEFT;
	// The current visible text.
	protected String2D text;
	// The offset of the text from the centre.
	protected Vector2f textOffset = new Vector2f();

	// The black line that blinks when this is selected.
	protected SimpleQuad textPointer;
	protected SimpleQuad textHighlight;
	// Used to ensure the blinking pointer updates correctly.
	protected float timeBetweenBlinks = 530;
	protected float timeUntilNextBlink = this.timeBetweenBlinks;
	protected boolean textPointerBlinkedOut = false;
	protected boolean textPointerVisible = false;

	// When the user is selecting text.
	boolean selecting = false;
	int currentSelectedStartIndex = 0;
	int currentSelectedEndIndex = 0;
	int startMouseSelectionIndex = 0;
	
	
	protected String fontName;
	protected int fontStyle;
	protected Vector2f textSize;
	
	protected Vector2f textPointerSize;

	// All the characters that are valid to type.
	protected HashSet<Character> validCharacters = generateDefaultValidCharacters();
	
	//Whether or not this has loaded.
	protected boolean hasLoaded = false;
	protected String delayedTextToSet = null;

	protected int maxNumCharacters = Integer.MAX_VALUE;

	protected String textFirstSection = "";
	protected String textSelectedSection = "";
	protected String textFinalSection = "";

	protected String combinedText;
	public void setValidCharacters(HashSet<Character> newValidCharacters){
		this.validCharacters = newValidCharacters;
	}

	public void setValidCharacters(String validCharactersString){
		HashSet<Character> newValidCharacters = new HashSet<>();
		for(int charNo = 0; charNo < validCharactersString.length(); charNo++){
			char c = validCharactersString.charAt(charNo);
			newValidCharacters.add(c);
		}
		this.validCharacters = newValidCharacters;
	}

	public void setCharacterLimit(int newMaxNumCharacters){
		this.maxNumCharacters = newMaxNumCharacters;
	}


	@Override
	public LoadStage[] stagesToPerform() {
		return new LoadStage[] {
				//LoadStage.LOAD_DATA_FROM_FILE,
				//LoadStage.HANDLE_RAW_DATA,
				LoadStage.LOAD_DEPENDENCIES
		};
	}
	
	/**
	 * Initialize all components.
	 * 
	 * @param position
	 * @param size
	 * @param fontName
     * @param fontStyle
	 */
	public TextArea(Vector2f position, Vector2f size, String fontName, int fontStyle) {
		super(position, size, 0, 2);

		// Offset the text to the correct location.
		this.textOffset.x = -size.x / 2.0f;
		this.textOffset.y = 0;

		// Default textbox will say "Enter Text"
		textSize = new Vector2f(size.y, size.y);
		this.fontName = fontName;

        this.fontStyle = fontStyle;

        //String text, StringAlignment alignment, Vector2f xy, Vector2f size, String fontName, int fontStyle
		
		//this.text = new String2D("Enter Text", this.alignment, this.textOffset, textSize, fontName, fontStyle);
		//this.text.setParentRenderableComponent(this);

		// Initialize the blinking pointer
		textPointerSize = new Vector2f(2, 16);

		setText("Enter Text");
	}
	
	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		super.loadRawDataFromFile(handler);
		// Not called.
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		super.handleRawData(handler);
		// Not called.
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		super.loadDependencies(handler);
		this.text = handler.newDependancy(new String2D("Enter Text", this.alignment, this.textOffset, textSize, fontName, fontStyle));
		this.text.setParentRenderableComponent(this);
		this.text.setBlend(new Vector4f(0, 0, 0, 1), 1);
		
		this.textPointer = handler.newDependancy(new SimpleQuad(new Vector2f(0, 0), textPointerSize, 0));
		this.textPointer.setBlend(new Vector4f(0, 0, 0, 1), 1);
		this.textPointer.setParentRenderableComponent(this);
		this.textPointer.setVisible(false);

		this.textHighlight = handler.newDependancy(new SimpleQuad(new Vector2f(0, 0), textPointerSize, 0));
		this.textHighlight.setBlend(new Vector4f(0.5f, 0.5f, 1, 0.5f), 1);
		this.textHighlight.setParentRenderableComponent(this);
		this.textHighlight.setVisible(false);
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		super.completeLoad(handler);
		this.hasLoaded = true;
		if(delayedTextToSet != null){
			setText(delayedTextToSet);
			delayedTextToSet = null;
		}
	}

	/**
	 * Simple function for returing the shader that is being used.
	 * @return
     */
	@Override
	public Shader2D getShader() {
		return text.getShader();
	}
	
	
	
	

	/**
	 * @return a set containing all characters between 0 and 255.
	 */
	protected static final HashSet<Character> generateDefaultValidCharacters() {
		HashSet<Character> validCharacters = new HashSet<Character>();
		for (int i = 0; i < 255; i++) {
			Character newChar = (char) i;
			validCharacters.add(newChar);
		}
		return validCharacters;
	}

	/**
	 * Update boundaries for all objects
	 */
	@Override
	public void setBoundaries(DisplayBoundary newBoundary) {
		setBoundaries(newBoundary);
		this.text.setBoundaries(newBoundary);
		this.textPointer.setBoundaries(newBoundary);
	}


	/**
	 * Determine whether the text box has been selected.
	 */
	@Override
	public void update(CurrentInput input, float delta) {
		super.update(input, delta);
		text.update(input, delta);

		if ((input.getLeftMouse() == ButtonStatus.JUST_RELEASED)
				|| (input.getLeftMouse() == ButtonStatus.UP)) {
			// The user is not dragging their mouse.
			this.selecting = false;
		}

		if (input.receivedMouseEvent(this)) {
			input.setComponentReceivedMouseEventFlagIfNoneExists(this);
			// The mouse is over the textbox
			if ((input.getLeftMouse() == ButtonStatus.JUST_PRESSED)) {
				// The mouse has selected the text box
				this.selecting = true;
				// Ask to receive all keyboard events.
				input.getKeyboardHandler().setNewAmbiguousKeyHandler(this);
				//Update the selection index
				this.startMouseSelectionIndex = determineSelectionIndexAtPoint(input.getMXpos());
			}

		} else {
			if (input.getLeftMouse() == ButtonStatus.JUST_PRESSED) {
				// The mouse has clicked away from the text box.
				this.textPointerVisible = false;
				this.textPointerBlinkedOut = false;
				this.timeUntilNextBlink = this.timeBetweenBlinks;
				if (input.getKeyboardHandler().getAmbiguousKeyHandler() == this) {
					// Don't want to hear keyboard events anymore.
					input.getKeyboardHandler().setNewAmbiguousKeyHandler(null);
				}
			}
		}

		if (this.selecting) {
			//Ensure that the start index is always first.
			int currentMouseSelectionIndex = determineSelectionIndexAtPoint(input.getMXpos());
			if(currentMouseSelectionIndex > startMouseSelectionIndex){
				currentSelectedEndIndex = currentMouseSelectionIndex;
				currentSelectedStartIndex = startMouseSelectionIndex;
			} else {
				currentSelectedEndIndex = startMouseSelectionIndex;
				currentSelectedStartIndex = currentMouseSelectionIndex;
			}

			// Don't blink whilst the user is dragging.
			this.textPointerVisible = true;
			this.timeUntilNextBlink = this.timeBetweenBlinks;
		}

		updatePointerVisibility(delta);
		determinePointerSize();
	}

	/**
	 * Determines the selection index for a particular mouse X
	 * point.
	 * @param mousePositionX
	 * @return
     */
	private int determineSelectionIndexAtPoint(float mousePositionX){
		float startX = this.absolutePosition.x + this.textOffset.x;
		float currentX = startX;

		String currentText = this.text.getText();
		int returnedIndex = 0;
		// Iterate through each character in the text area.
		for (int charNo = 0; charNo < currentText.length(); charNo++) {
			boolean isAfter = false;
			char c = currentText.charAt(charNo);
			float charWidth = text.getCharacterWidth(c);
			currentX += charWidth / 2.0f;
			if (mousePositionX > currentX) {
				// The mouse X is past this chraracter.
				isAfter = true;
				returnedIndex++;
			}
			currentX += charWidth / 2.0f;
		}
		return returnedIndex;
	}


	/**
	 * Determines which pointer should show
	 * (Either the highlight or the blinking pointer)
	 * Will blink the pointer if it is not in highlight mode.
	 * @param delta
     */
	private void updatePointerVisibility(float delta){
		if (!this.textPointerVisible) {
			// Set the colour of the pointer to black.
			this.textPointer.setVisible(false);
			textHighlight.setVisible(false);
		} else {
			if(currentSelectedStartIndex == currentSelectedEndIndex){
				//Use a blinking pointer.
				textHighlight.setVisible(false);
				// Determine if the text pointer should blink
				// to invisible or not.
				this.timeUntilNextBlink -= delta;
				if (this.timeUntilNextBlink <= 0) {
					this.timeUntilNextBlink = this.timeBetweenBlinks;
					this.textPointerBlinkedOut = !this.textPointerBlinkedOut;
				}
				if (this.textPointerBlinkedOut) {
					// Hide the blinker
					this.textPointer.setVisible(false);
				} else {
					// Show the blinker.
					this.textPointer.setVisible(true);
				}
			} else {
				textPointer.setVisible(false);
				textHighlight.setVisible(true);
			}
		}
	}

	/**
	 * Render all related objects.
	 */
	@Override
	public void render() {
		super.render();
		this.text.render();
		this.textPointer.render();
		this.textHighlight.render();
	}

	/**
	 * Whether or not this wants to receive all mouse events exclusively.
	 */
	@Override
	public boolean controlsAllKeyEvents() {
		return true;
	}

	/**
	 * Called when the object is selected.
	 */
	@Override
	public void onSelect() {
		// Do nothing.
	}

	/**
	 * Called when the object is deselected.
	 */
	@Override
	public void onDeselect() {
		// Do nothing.
	}

	/**
	 * Sets the current pointer to visible
	 * and resets the pointer.
	 */
	private void showPointer(){
		this.textPointerBlinkedOut = false;
		this.textPointer.setVisible(true);
		this.timeUntilNextBlink = this.timeBetweenBlinks;
	}

	/**
	 * Handlers a keyboard event
	 */
	@Override
	public void handleKey(boolean keyDown, int keyCode, HandleKeyboard keyboardHandler) {
		if (!keyDown) {
			// Don't care about key up events... Yet...
			return;
		}

		// Determines the first and last half of the current text.
		// And it will put the new character in the middle.
		combinedText = this.text.getText();
		// Pointer is in the middle of the text.
		textFirstSection = combinedText.substring(0, currentSelectedStartIndex);
		textSelectedSection = combinedText.substring(currentSelectedStartIndex, currentSelectedEndIndex);
		textFinalSection = combinedText.substring(currentSelectedEndIndex, combinedText.length());


		if(HandleKeyboard.isControlHeld()){
			if(keyCode == Keyboard.KEY_C){
				if(currentSelectedStartIndex != currentSelectedEndIndex){
					ClipboardHandler.instance.setClipboardContents(textSelectedSection);
				}
			} else if(keyCode == Keyboard.KEY_X){
				if(currentSelectedStartIndex != currentSelectedEndIndex){
					ClipboardHandler.instance.setClipboardContents(textSelectedSection);
					textSelectedSection = "";
					currentSelectedEndIndex = currentSelectedStartIndex;
				}
			} else if(keyCode == Keyboard.KEY_V){
				textSelectedSection = "";
				String pastedContents = ClipboardHandler.instance.getClipboardContents();
				addNewCharacters(pastedContents);
			}
		} else if (keyCode == Keyboard.KEY_BACK) {
			// Backspace was pressed.
			if(currentSelectedStartIndex != currentSelectedEndIndex){
				//Remove the highlighted chunk.
				textSelectedSection = "";
				currentSelectedEndIndex = currentSelectedStartIndex;
			} else {
				//Remove the character before the pointer.
				if (textFirstSection.length() > 0) {
					textFirstSection = textFirstSection.substring(0, textFirstSection.length() - 1);
					//Move the text pointer back by 1.
					currentSelectedStartIndex--;
				}
			}
			currentSelectedEndIndex = currentSelectedStartIndex;
		} else if ((keyCode == Keyboard.KEY_RETURN) || (keyCode == Keyboard.KEY_ESCAPE)) {
			// enter or esc was pressed. de-select the text area.
			keyboardHandler.setNewAmbiguousKeyHandler(null);
			this.textPointerVisible = false;
			this.textPointerBlinkedOut = false;
			this.timeUntilNextBlink = this.timeBetweenBlinks;
			return;
		} else if (keyCode == Keyboard.KEY_DELETE) {
			if(currentSelectedStartIndex != currentSelectedEndIndex){
				//Remove the highlighted chunk.
				textSelectedSection = "";
				currentSelectedEndIndex = currentSelectedStartIndex;
			} else {
				if (textFinalSection.length() > 0) {
					textFinalSection = textFinalSection.substring(1, textFinalSection.length());
				}
			}
		} else if (keyCode == Keyboard.KEY_LEFT) {
			if(HandleKeyboard.isShiftHeld()) {
				//Shift is held.
				currentSelectedStartIndex--;
			} else {
				//Shift is not held.
				if(currentSelectedStartIndex == currentSelectedEndIndex){
					//Selection Area exists.
					currentSelectedStartIndex--;
				}
				currentSelectedEndIndex = currentSelectedStartIndex;
			}
		} else if (keyCode == Keyboard.KEY_RIGHT) {
			if(HandleKeyboard.isShiftHeld()) {
				//Shift is held.
				currentSelectedEndIndex++;
			} else {
				//Shift is not held.
				if(currentSelectedStartIndex == currentSelectedEndIndex){
					//Selection Area Doesn't exist.
					currentSelectedEndIndex++;
				}
				currentSelectedStartIndex = currentSelectedEndIndex;
			}
		} else if (keyCode == Keyboard.KEY_END) {
			currentSelectedEndIndex = combinedText.length();
			if(!HandleKeyboard.isShiftHeld()){
				//Shift is not held.
				currentSelectedStartIndex = currentSelectedEndIndex;
			}
		} else if (keyCode == Keyboard.KEY_HOME) {
			currentSelectedStartIndex = 0;
			if(!HandleKeyboard.isShiftHeld()){
				//Shift is not held.
				currentSelectedEndIndex = currentSelectedStartIndex;
			}
		} else {
			if(HandleKeyboard.isShiftHeld()){
				addNewCharacters(KeyboardCharacters.getShiftChar(keyCode));
			} else {
				addNewCharacters(KeyboardCharacters.getChar(keyCode));
			}
		}

		//Determine the current text.
		combinedText = textFirstSection + textSelectedSection + textFinalSection;
		//Determine whether the index is correct.
		currentSelectedStartIndex = limitSelectIndex(currentSelectedStartIndex, combinedText);
		currentSelectedEndIndex = limitSelectIndex(currentSelectedEndIndex, combinedText);
		//Update the sections areas.
		textFirstSection = combinedText.substring(0, currentSelectedStartIndex);
		textSelectedSection = combinedText.substring(currentSelectedStartIndex, currentSelectedEndIndex);
		textFinalSection = combinedText.substring(currentSelectedEndIndex, combinedText.length());


		// After adding the new character to the first, recombine and update the text.
		combinedText = textFirstSection + textSelectedSection + textFinalSection;
		updateDisplayedText();
		determinePointerSize();
		showPointer();
	}

	/**
	 * Updates the display to show the TextArea text.
	 */
	private void updateDisplayedText(){
		String currentDisplayedText = text.getText();
		if(!combinedText.equals(currentDisplayedText)){
			this.text.setText(combinedText);
		}
	}


	/**
	 * Limits a selection index to the
	 * @param currentIndex
	 * @param currentText
     * @return
     */
	private int limitSelectIndex(int currentIndex, String currentText){
		int newIndex = currentIndex;
		if (newIndex > currentText.length()){
			newIndex = currentText.length();
		}
		if(newIndex < 0){
			newIndex = 0;
		}
		return newIndex;
	}

	/**
	 * Determines and sets the size and position of the
	 * pointer or highlight depending on the current state.
	 */
	private void determinePointerSize(){
		if(currentSelectedStartIndex != currentSelectedEndIndex){
			//There is highlighted text, show the highlight quad.
			float startHightlightX = getXOffsetAtSelectionIndex(currentSelectedStartIndex);
			float endHightlightX = getXOffsetAtSelectionIndex(currentSelectedEndIndex);
			float width = endHightlightX - startHightlightX;
			float centerX = startHightlightX + (width / 2f);
			textHighlight.setRelX(centerX);
			textHighlight.setWidth(width);
		} else {
			//Offset the pointer by 1 pixel so it doesn't overlap with the text.
			this.textPointer.setRelX(getXOffsetAtSelectionIndex(currentSelectedStartIndex) + 1);
		}
	}

	/**
	 * Given a index, returns the xOffset from the center of the
	 * renderable object with which to position the cursor.
	 * @param selectionIndex
	 * @return
     */
	private float getXOffsetAtSelectionIndex(int selectionIndex){
		//There is no highlighted text, show just the text pointer.
		ImageFont imageFont = this.text.getImageFont();
		float currentX = this.textOffset.x;
		float widthRatio = (textSize.x / imageFont.getFont().getSize());
		// Iterate through each character and determine the offset to give the pointer.
		//TODO - Fixx.
		try {
			for (int charNo = 0; charNo < selectionIndex; charNo++) {
				char c = combinedText.charAt(charNo);
				float charWidth = imageFont.getCharacterWidth(c) * widthRatio;
				currentX += charWidth;
			}
		} catch (IndexOutOfBoundsException e){
			currentX = 0;
		}

		// Return the calculated amount.
		return currentX;
	}

	/**
	 * Adds a String of Characters current pointer.
	 * @param newCharacters
     */
    private void addNewCharacters(String newCharacters){
        if(newCharacters == null){
            return;
        }
		textSelectedSection = "";
        for(int charNo = 0; charNo < newCharacters.length(); charNo ++){
			char charAdded = newCharacters.charAt(charNo);
			if((textFirstSection.length() + textSelectedSection.length() + textFinalSection.length()) >= maxNumCharacters){
				//If we've hit the max characters, stop added them.
				return;
			}
			if (this.validCharacters.contains(charAdded)) {
				addNewCharacter(charAdded);
			}
        }
    }

	/**
	 * @param charAdded - the char to be added.
	 * @require validCharacters.contains(charAdded)
     */
    private void addNewCharacter(char charAdded){
		//Clear the selected text.
		textSelectedSection = "";
		// The character pressed was in the set of valid characters.
		float charWidth = text.getCharacterWidth(charAdded);
		this.textPointer.setRelX(this.textPointer.getRelX() + charWidth);
		textFirstSection = textFirstSection + charAdded;
		this.currentSelectedStartIndex++;
		currentSelectedEndIndex = currentSelectedStartIndex;
    }

	/**
	 * Sets the text of the text area.
	 * 
	 * @param newText
	 */
	public void setText(String newText) {
		if(hasLoaded){
			combinedText = newText;
			updateDisplayedText();
			this.currentSelectedStartIndex = 0;
			this.currentSelectedEndIndex = 0;
			determinePointerSize();
		} else {
			delayedTextToSet = newText;
		}
	}

	/**
	 * @return the current text of the text area.
	 */
	public String getString() {
		return this.text.getText();
	}

	@Override
	public String toString(){
		if(text == null){
			if(delayedTextToSet == null){
				return super.toString();
			}
			return delayedTextToSet;
		}
		return getString();
	}

	public boolean isSelecting() {
		return selecting;
	}
}
