package gebd.shaders;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

import renderables.r2D.DisplayBoundary;

public abstract class Shader2D extends Shader{

	// The default display boundary is a huge box!
	public static final DisplayBoundary DEFAULT_DISPLAY_BOUNDARY =
			new DisplayBoundary(0, 0, 10000, 10000);

	// Adjust what can be seen and what cannot.
	// Only pixels within these boundaries are visible.
	protected int boundaryBottomLeftLocation;
	protected int boundaryTopRightLocation;

	// The current display boundary.
	protected DisplayBoundary currentDisplayBoundary = null;

	// The default blend colour is white
	public static final Vector4f DEFAULT_MIX_COLOUR = new Vector4f(1, 1, 1, 1); // White!
	// The default amount of blend of 0%
	public static final float DEFAULT_MIX_AMOUNT = 0;
	// Whether or not the colour mixing is at default
	protected boolean colourIsDefault = true;

	// The position of the quad. (pixels)
	protected int quadPosLocation;
	// The size of the quad. (pixels)
	protected int quadScaleLocation;
	// The rotation of the quad. (Radians)
	protected int quadRotationLocation;
	// The screen resolution.
	protected int screenResolutionLocation;
	// The Texture Coordinate Offset
	protected int textureCoordLocation_2D;
	// The size of the subsection of the texture
	protected int textureSizeLocation_2D;
	// The mixed colour of the texture.
	protected int mixColourLocation;
	// The amount of colour change.
	protected int mixAmountLocation;

	public Shader2D(int vertexShaderId, int fragmentShaderId) {
		super(vertexShaderId, fragmentShaderId);
	}

	public void setScreenResolution(Vector2f screenResolution){
		loadVec2(screenResolutionLocation, screenResolution.x, screenResolution.y);
	}
	public void setScreenResolution(int width, int height){
		loadVec2(screenResolutionLocation, width, height);
	}

	public void setTextureOffset(Vector2f textureOffset){
		loadVec2(textureCoordLocation_2D, textureOffset.x, textureOffset.y);
	}
	public void setTextureOffset(float x, float y){
		loadVec2(textureCoordLocation_2D, x, y);
	}

	public void setTextureSize(Vector2f textureSize){
		loadVec2(textureSizeLocation_2D, textureSize.x, textureSize.y);
	}
	public void setTextureSize(float width, float height){
		loadVec2(textureSizeLocation_2D, width, height);
	}


	public void setQuadLocation(Vector2f position){
		loadVec2(quadPosLocation, position);
	}
	public void setQuadLocation(float x, float y){
		loadVec2(quadPosLocation, x, y);
	}

	public void setQuadSize(Vector2f size){
		loadVec2(quadScaleLocation, size);
	}
	public void setQuadSize(float width, float height){
		loadVec2(quadScaleLocation, width, height);
	}

	public void setQuadRotation(float theta){
		loadFloat(quadRotationLocation, theta);
	}

	/**
	 * Reset the colour to the default values.
	 */
	public void resetColour() {
		if (!this.colourIsDefault) {
			setColour(DEFAULT_MIX_COLOUR);
			setMixAmount(DEFAULT_MIX_AMOUNT);
			this.colourIsDefault = true;
		}
	}

	/**
	 * Sets the current blend colour.
	 * 
	 * @param colour
	 */
	public void setColour(Vector4f colour) {
		loadVec4(this.mixColourLocation, colour);
		this.colourIsDefault = false;
	}

	/**
	 * Sets the current blend colour
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public void setColour(float red, float green, float blue, float alpha) {
		loadVec4(this.mixColourLocation, red, green, blue, alpha);
		this.colourIsDefault = false;
	}

	/**
	 * Sets the amount the colour is blended with the texture
	 * 
	 * @param amount
	 */
	public void setMixAmount(float amount) {
		loadFloat(this.mixAmountLocation, amount);
		this.colourIsDefault = false;
	}

	/**
	 * If the display boundary is not already set, set it to the new display boundary.
	 * 
	 * @param newDisplayBoundary
	 */
	public void setDisplayBoundary(DisplayBoundary newDisplayBoundary) {
		if (this.currentDisplayBoundary != newDisplayBoundary) {
			this.currentDisplayBoundary = newDisplayBoundary;
			loadVec2(this.boundaryBottomLeftLocation, newDisplayBoundary.getBotLeftX(),
					newDisplayBoundary.getBotLeftY());
			loadVec2(this.boundaryTopRightLocation, newDisplayBoundary.getTopRightX(),
					newDisplayBoundary.getTopRightY());
		}
	}
}
