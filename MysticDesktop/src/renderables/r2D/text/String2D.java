package renderables.r2D.text;

import java.util.LinkedList;

import blindmystics.input.CurrentInput;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

import gebd.Render;
import gebd.shaders.Shader2D;
import loader.LoadedObject;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import renderables.r2D.Renderable2D;
import renderables.r2D.DisplayBoundary;
import renderables.r2D.Quad;
import renderables.texture.TextureHandler;

/**
 * A object that handles rendering Strings to the screen.
 * 
 * @author P3TE
 */
public class String2D extends Renderable2D implements LoadedObject {
	// The text to render
	protected String text;
	//The new text to render.
	protected String newText;
	//Whether or not the current rendered text needs to change.
	protected boolean textRequiresUpdate = false;
	// The text split into lines.
	protected String[] lines;
	// The number of chars to render.
	protected int numRenderedChars = 0;
	//The name of the font used.
	protected String fontName;
	//The name of the font used.
	protected int fontStyle;
	// The image Image Font to use.
	protected ImageFont imageFont;

	// The alignment of the String relative to the objects' position.
	protected StringAlignment alignment;

	// The sizes and positions of each character rendered relative to the object.
	protected Vector2f[] quadPositionOffsets;
	protected Vector2f[] quadSizes;
	protected Vector2f[] quadUvPositions;
	protected Vector2f[] quadUvSizes;

	// The sizes and positions of each character rendered relative to the screen
	protected Vector2f[] renderedQuadPositions;
	protected Vector2f[] renderedQuadSizes;
	protected Vector2f[] renderedQuadUvPosition;
	protected Vector2f[] renderedQuadUvSize;

	// Whether a character is visible.
	protected boolean[] visible;

	// The bounds of rendering the string.
	protected DisplayBoundary boundary = Shader2D.DEFAULT_DISPLAY_BOUNDARY;

	// Colour modification to the displayed String.
	protected boolean useBlend = false;
	protected Vector4f blendColour = null;
	protected float blendAmount;

	//The specific text shader.
	protected Shader2D shader2D;
	
	//Whether or not the string has loaded yet.
	boolean hasLoaded = false;

	//The current load status of the String2D
	private LoadedObjectHandler.LoadStatus currentStatus;

	/**
	 * The alignment of the String relative to the objects' position.
	 * 
	 * @author Peter Smith 43180543
	 */
	public static enum StringAlignment {
		TOP_LEFT, TOP_MIDDLE, TOP_RIGHT, MID_LEFT, MID_MIDDLE, MID_RIGHT, BOT_LEFT, BOT_MIDDLE, BOT_RIGHT
	}

	/*
	 * xy is the top left. Will rotate from the center.
	 */
	public String2D(String text, StringAlignment alignment, Vector2f xy, Vector2f size, String fontName, int fontStyle) {
		setText(text);
		this.alignment = alignment;
		this.relativePosition = new Vector2f(xy);
		this.absolutePosition = new Vector2f(xy);
		this.size = new Vector2f(size);
		this.fontName = fontName;
		this.fontStyle = fontStyle;
	}

	@Override
	public Shader2D getShader(){
		return this.shader2D;
	}
	
	
	@Override
	public LoadStage[] stagesToPerform() {
		return new LoadStage[]{
//				LoadedObjectHandler.LoadStage.LOAD_DATA_FROM_FILE,
//				LoadedObjectHandler.LoadStage.HANDLE_RAW_DATA,
				LoadStage.LOAD_DEPENDENCIES,
		};
	}

	@Override
	public LoadedObjectHandler.LoadStatus getLoadStatus() {
		return currentStatus;
	}

	@Override
	public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {
		this.currentStatus = newLoadStatus;
	}

	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		//Not called.
		
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		//Not called.
		
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		this.imageFont = ImageFontHandler.loadImageFont(fontName, fontStyle, handler);
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		hasLoaded = true;
		updateText();
		this.shader2D = Render.instance.getDefault2DShader();
	}
	
	

	/**
	 * @param newBoundary - sets the display boundaries for the object.
	 */
	@Override
	public void setBoundaries(DisplayBoundary newBoundary) {
		this.boundary = newBoundary;
	}

	/**
	 * Returns the width of the text
	 */
	@Override
	public float getWidth() {
		float widthRatio = (size.x / imageFont.getFont().getSize());
		return this.imageFont.getStringWidth(this.text) * widthRatio;
	}

	/**
	 * returns the height of the font.
	 */
	@Override
	public float getHeight() {
		float heightRatio = (size.y / imageFont.getFont().getSize());
		return this.imageFont.getFontHeight() * heightRatio;
	}

	/**
	 * Changes the text to a new value and updates all characters accordingly.
	 * 
	 * @param newText
	 */
	public void setText(String newText) {
		this.newText = newText;
		this.textRequiresUpdate = true;
	}

	/**
	 * @return the current text.
	 */
	public String getText() {
		return this.newText;
	}

	/**
	 * Iterates over every character in the text, and creates a new line every 'newline' character.
	 */
	private void setupLines() {
		this.numRenderedChars = 0;
		LinkedList<String> tempLines = new LinkedList<String>();
		StringBuilder currentLine = new StringBuilder();

		// Iterate over every character in the string.
		for (int i = 0; i < this.text.length(); i++) {
			char c = this.text.charAt(i);
			if (c == '\n') {
				// If it's a new line, add the line to the list of lines.
				tempLines.add(currentLine.toString());
				// Reset the line builder.
				currentLine = new StringBuilder();
			} else {
				// Otherwise, add the character to the line builder.
				currentLine.append(c);
				this.numRenderedChars++;
			}
		}
		/**
		 * Add the last line to the list.
		 */
		if (currentLine.length() != 0) {
			tempLines.add(currentLine.toString());
		}
		// Create a fixed size array from the list.
		this.lines = new String[tempLines.size()];
		int numLines = tempLines.size();
		// Add each line to the array.
		for (int i = 0; i < numLines; i++) {
			this.lines[i] = tempLines.removeFirst();
		}
	}

	public float getCharacterWidth(char c){
		float sizeModifier = getSize().x / imageFont.getFont().getSize();
		return imageFont.getCharacterWidth(c) * sizeModifier;
	}

	/**
	 * Based on the current STRING_ALIGNMENT, update the position of each of the quads.
	 */
	private void updateTextPositionOffsets() {


		// First, determine the width of each line.
		float[] lineWidths = new float[this.lines.length];

		// Initialize variables.
		this.renderedQuadPositions = new Vector2f[this.numRenderedChars];
		this.renderedQuadSizes = new Vector2f[this.numRenderedChars];
		this.renderedQuadUvPosition = new Vector2f[this.numRenderedChars];
		this.renderedQuadUvSize = new Vector2f[this.numRenderedChars];

		this.quadPositionOffsets = new Vector2f[this.numRenderedChars];
		this.quadSizes = new Vector2f[this.numRenderedChars];
		this.quadUvPositions = new Vector2f[this.numRenderedChars];
		this.quadUvSizes = new Vector2f[this.numRenderedChars];

		this.visible = new boolean[this.numRenderedChars];

		// Iterate through each line.
		int quadNo = 0;
		for (int lineNo = 0; lineNo < this.lines.length; lineNo++) {
			String line = this.lines[lineNo];
			float xPos = 0;
			float yPos = 0;
			// Iterate through each char.
			for (int charNo = 0; charNo < line.length(); charNo++) {
				char c = line.charAt(charNo);

				// Determine the dimensions of each character quad.
				float[] uvMapping = this.imageFont.getUVMapping(c);

				float uMin = uvMapping[0];
				float vMin = uvMapping[1];
				float uMax = uvMapping[2];
				float vMax = uvMapping[3];

				float uWidth = uMax - uMin;
				float vHeight = vMax - vMin;

				float widthRatio = (size.x / imageFont.getFont().getSize());
				float quadWidth = uWidth * this.imageFont.getTextureWidth() * widthRatio;
				float heightRatio = (size.y / imageFont.getFont().getSize());
				float quadHeight = this.imageFont.getFontHeight() * heightRatio;


				xPos += (quadWidth / 2f);

				// Set the position, size, uv and uv sizes.
				this.renderedQuadPositions[quadNo] = new Vector2f(xPos, yPos);
				this.renderedQuadSizes[quadNo] = new Vector2f(quadWidth, quadHeight);
				this.renderedQuadUvPosition[quadNo] = new Vector2f(uMin, vMin);
				this.renderedQuadUvSize[quadNo] = new Vector2f(uWidth, vHeight);
				this.quadPositionOffsets[quadNo] = new Vector2f(xPos, yPos);
				this.quadSizes[quadNo] = new Vector2f(quadWidth, quadHeight);
				this.quadUvPositions[quadNo] = new Vector2f(uMin, vMin);
				this.quadUvSizes[quadNo] = new Vector2f(uWidth, vHeight);
				this.visible[quadNo] = true;

				xPos += (quadWidth / 2f);

				quadNo++;
			}
			// Set the total line width.
			lineWidths[lineNo] = xPos;
		}

		// For each line, offset the coords depending on the Alignment.
		float heightRatio = (size.y / imageFont.getFont().getSize());
		float quadHeight = this.imageFont.getFontHeight() * heightRatio;
		quadNo = 0;
		for (int lineNo = 0; lineNo < this.lines.length; lineNo++) {
			String line = this.lines[lineNo];
			float lineWidth = lineWidths[lineNo];
			float xOffset = 0;
			float yOffset = 0;
			switch (this.alignment) {
			case BOT_LEFT:
				xOffset = 0;
				yOffset = quadHeight * (lineNo + 0.5f);
				break;
			case BOT_MIDDLE:
				xOffset = -lineWidth / 2.0f;
				yOffset = quadHeight * (lineNo + 0.5f);
				break;
			case BOT_RIGHT:
				xOffset = -lineWidth;
				yOffset = quadHeight * (lineNo + 0.5f);
				break;
			case MID_LEFT:
				xOffset = 0;
				yOffset = quadHeight * (((this.lines.length - 1) / 2.0f) - lineNo);
				break;
			case MID_MIDDLE:
				xOffset = -lineWidth / 2.0f;
				yOffset = quadHeight * (((this.lines.length - 1) / 2.0f) - lineNo);
				break;
			case MID_RIGHT:
				xOffset = -lineWidth;
				yOffset = quadHeight * (((this.lines.length - 1) / 2.0f) - lineNo);
				break;
			case TOP_LEFT:
				xOffset = 0;
				yOffset = quadHeight * ((-0.5f) - lineNo);
				break;
			case TOP_MIDDLE:
				xOffset = -lineWidth / 2.0f;
				yOffset = quadHeight * ((-0.5f) - lineNo);
				break;
			case TOP_RIGHT:
				xOffset = -lineWidth;
				yOffset = quadHeight * ((-0.5f) - lineNo);
				break;
			}

			// Offset the saved coords by the calculated value.
			for (int charNo = 0; charNo < line.length(); charNo++) {
				this.renderedQuadPositions[quadNo].x += xOffset;
				this.renderedQuadPositions[quadNo].y += yOffset;
				this.quadPositionOffsets[quadNo].x += xOffset;
				this.quadPositionOffsets[quadNo].y += yOffset;
				quadNo++;
			}
		}
	}

	/**
	 * On update.
	 */
	@Override
	public void update(CurrentInput input, float delta) {
		if (textRequiresUpdate) {
			updateText();
		}
	}

	public void updateText(){
		textRequiresUpdate = false;
		if (this.newText.equals(this.text)) {
			//Don't need to update the text.
		} else {
			this.text = this.newText;
			setupLines();
			updateTextPositionOffsets();
		}
	}


	/**
	 * Render all visible quads.
	 */
	@Override
	public void render() {
		if (!hasLoaded) {
			throw new RuntimeException("Why am I rendering when I'm not loaded!?");
		}
		Render.setShader(shader2D);
		loadTexture();
		shader2D.setDisplayBoundary(this.boundary);
		shader2D.setQuadRotation(0);
		if (this.useBlend) {
			// Change the colour of the text.
			shader2D.setColour(this.blendColour);
			shader2D.setMixAmount(this.blendAmount);
		} else {
			shader2D.resetColour();
		}
		// For each character
		for (int quadNo = 0; quadNo < this.renderedQuadPositions.length; quadNo++) {
			// If visible
			if (this.visible[quadNo]) {
				// Position, Size, Rotation

				this.renderedQuadPositions[quadNo].x = this.absolutePosition.x + this.quadPositionOffsets[quadNo].x;
				this.renderedQuadPositions[quadNo].y = this.absolutePosition.y + this.quadPositionOffsets[quadNo].y;

				shader2D.setQuadLocation(this.renderedQuadPositions[quadNo]);
				shader2D.setQuadSize(this.renderedQuadSizes[quadNo]);

				// Texturing
				shader2D.setTextureOffset(this.renderedQuadUvPosition[quadNo]);
				shader2D.setTextureSize(this.renderedQuadUvSize[quadNo]);

				// Render the Character.
				Quad.render();
			}
		}
	}

	/**
	 * Prepare the texture that is linked to this string.
	 */
	public void loadTexture() {
		TextureHandler.prepareTexture(this.imageFont.getTexture());
	}

	/**
	 * Don't colour the text.
	 */
	public void resetBlend() {
		this.useBlend = false;
	}

	/**
	 * Set the colour of the text.
	 * 
	 * @param colour
	 */
	public void setBlendColour(Vector4f colour) {
		this.useBlend = true;
		this.blendColour = colour;
	}

	/**
	 * Set the blend quantity
	 * 
	 * @param amount
	 */
	public void setBlendAmount(float amount) {
		this.useBlend = true;
		this.blendAmount = amount;
	}

	/**
	 * Set both blend and colour at once.
	 * 
	 * @param colour
	 * @param blendAmount
	 */
	public void setBlend(Vector4f colour, float blendAmount) {
		this.useBlend = true;
		this.blendColour = colour;
		this.blendAmount = blendAmount;
	}

	/**
	 * @return - The image font associated with the String.
	 */
	public ImageFont getImageFont() {
		return this.imageFont;
	}

	/**
	 * Sets the string to a new alignment.
	 * 
	 * @param newAllignment
	 */
	public void setStringAlignment(StringAlignment newAllignment) {
		this.alignment = newAllignment;
		updateTextPositionOffsets();
	}




}