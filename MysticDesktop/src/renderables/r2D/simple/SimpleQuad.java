package renderables.r2D.simple;

import blindmystics.input.CurrentInput;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

import gebd.Render;
import gebd.shaders.Shader2D;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import renderables.r2D.Renderable2D;
import renderables.r2D.DisplayBoundary;
import renderables.r2D.Quad;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;
import renderables.texture.generated.SolidFillTextureHandler;

/**
 * An object that is just a quad with a texture on it.
 * 
 * @author Peter Smith 43180543
 *
 */
public class SimpleQuad extends Renderable2D {

	// The texture default offset for the QUAD. - Default to none.
	public static final Vector2f DEFAULT_TEXTURE_OFFSET = new Vector2f(0, 0);
	public static final Vector2f DEFAULT_TEXTURE_SIZE = new Vector2f(1, 1);

	// The current texture offset of the quad before transformations
	protected Vector2f relativeTextureOffsetCoord = new Vector2f(DEFAULT_TEXTURE_OFFSET);
	protected Vector2f relativeTextureSize = new Vector2f(DEFAULT_TEXTURE_SIZE);

	// The current texture offset of the quad after transformations
	protected Vector2f absoluteTextureOffsetCoord = new Vector2f(DEFAULT_TEXTURE_OFFSET);
	protected Vector2f absoluteTextureSize = new Vector2f(DEFAULT_TEXTURE_SIZE);

	// The file path to the PNG image
	protected String texturePath;

	//Whether or not the quad is within the display boundaries.
	protected boolean withinBoundaries = true;

	// The quad's rotation
	protected float rotation;

	// The texture attached to the quad.
	protected TextureInfo texture;
	// Whether the texture should repeat if the quad is larger than the texture.
	protected boolean textureIsRepeating = false;

	//Whether the texture if about the X and Y axis.
	protected boolean textureIsFlippedHorizontally = false;
	protected boolean textureIsFlippedVertically = false;


	// The current display boundaries
	protected DisplayBoundary boundary = Shader2D.DEFAULT_DISPLAY_BOUNDARY;

	// The colour and colour blend of the quad.
	protected boolean useBlend = false;
	protected Vector4f blendColour = new Vector4f();
	protected float blendAmount;

	//The specific shader.
	protected Shader2D shader2D;

	protected LoadStage[] stagesToLoad;

	private LoadedObjectHandler.LoadStatus currentStatus;

	/**
	 * Constructor without any texture (Use the default)
	 *
	 * @param screenPosition
	 * @param size
	 * @param rotation
	 */
	public SimpleQuad(Vector2f screenPosition, Vector2f size, float rotation) {
		this("", screenPosition, size, rotation);
	}



	/**
	 * A constructor that with a pre-loaded texture.
	 *
	 * @param givenTexture
	 * @param screenPosition
	 * @param size
	 * @param rotation
	 */
	public SimpleQuad(TextureInfo givenTexture, Vector2f screenPosition, Vector2f size,
			float rotation) {
		this.texture = givenTexture;
		this.texturePath = this.texture.getPath();
		this.rotation = rotation;
		this.relativePosition = new Vector2f(screenPosition);
		this.absolutePosition = new Vector2f(screenPosition);
		this.size = new Vector2f(size);
		stagesToLoad = new LoadStage[] {};
	}

	/**
	 * A constructor that will also load a texture.
	 *
	 * @param texturePath
	 * @param screenPosition
	 * @param size
	 * @param rotation
	 */
	public SimpleQuad(String texturePath, Vector2f screenPosition, Vector2f size, float rotation) {
		if ((texturePath == null) || (texturePath.equals(""))) {
			// No texture to use, load the default
			this.texture = SolidFillTextureHandler.getSolidFillTexture();
			this.texturePath = this.texture.getPath();
			stagesToLoad = new LoadStage[] {};
		} else {
			this.texturePath = texturePath;
			stagesToLoad = new LoadStage[] {
					//LoadStage.LOAD_DATA_FROM_FILE,
					//LoadStage.HANDLE_RAW_DATA,
					LoadStage.LOAD_DEPENDENCIES,
			};
		}
		this.rotation = rotation;
		this.relativePosition = new Vector2f(screenPosition);
		this.absolutePosition = new Vector2f(screenPosition);
		this.size = new Vector2f(size);
	}

	@Override
	public LoadStage[] stagesToPerform(){
		return stagesToLoad;
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
		// Not called.
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		// Not called.

	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		this.texture = TextureInfo.queueLoadOfPNGTexture(texturePath, handler);
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		// Texture is now loaded.
		this.shader2D = Render.instance.getDefault2DShader();
		updateAbsoluteTextureSizeAndCoords();
	}

	/**
	 * Loads the texture in OpenGl ready to use.
	 */
	protected void prepareTexture() {
		TextureHandler.prepareTexture(this.texture);
	}


	@Override
	public Shader2D getShader(){
		return this.shader2D;
	}

	/**
	 * On update, if the mouse clicked on the quad, ensure that nothing covered by this quad will
	 * receive the mouse click.
	 */
	@Override
	public void update(CurrentInput input, float delta) {

		if (input.hasComponentReceivedMouseEvent()) {
			// Something else has already received a click event.
			return;
		}
		if (!this.boundary.isWithinBounds(input.getMXpos(), input.getMYpos())) {
			// Quad is not visible at the mouse.
			return;
		}
		if (isWithinQuadBounds(input.getMXpos(), input.getMYpos(), this.absolutePosition, this.size)) {
			// Mouse is within the quad.
			//TODO - Consider Texture transparency.
			input.setComponentReceivedMouseEventFlagIfNoneExists(this);
		}
	}

	/**
	 * Render the quad.
	 */
	@Override
	public void render() {
		if (this.isVisible && this.withinBoundaries) {

			Render.setShader(shader2D);

			// If the quad is visible, prepare the texture
			prepareTexture();

			shader2D.setDisplayBoundary(this.boundary);

			if (this.useBlend) {
				// Alter the colour of the quad.
				shader2D.setColour(this.blendColour);
				shader2D.setMixAmount(this.blendAmount);
			} else {
				shader2D.resetColour();
			}

			// position, size, rotation
			shader2D.setQuadLocation(this.absolutePosition);
			shader2D.setQuadSize(this.size.x * this.sizeScale.x, this.size.y * this.sizeScale.y);
			shader2D.setQuadRotation(this.rotation);

			shader2D.setTextureOffset(absoluteTextureOffsetCoord);
			shader2D.setTextureSize(absoluteTextureSize);

			// Render.
			Quad.render();
		}
	}

	/**
	 * Update the display boundaries.
	 */
	@Override
	public void setBoundaries(DisplayBoundary newBoundary) {
		this.boundary = newBoundary;
	}

	/**
	 * Determine whether the quad is visible or not.
	 */
	protected void updateBoundaries() {
		if (this.boundary != Shader2D.DEFAULT_DISPLAY_BOUNDARY) {
			float minX = this.absolutePosition.x - (this.size.x / 2);
			float minY = this.absolutePosition.y - (this.size.y / 2);

			float maxX = this.absolutePosition.x + (this.size.x / 2);
			float maxY = this.absolutePosition.y + (this.size.y / 2);

			float boundaryMinX = this.boundary.getBotLeftX();
			float boundaryMinY = this.boundary.getBotLeftY();

			float boundaryMaxX = this.boundary.getTopRightX();
			float boundaryMaxY = this.boundary.getTopRightY();


			if ((maxX < boundaryMinX) || (maxY < boundaryMinY) || (minX > boundaryMaxX)
					|| (minY > boundaryMaxY)) {
				// The quad is not visible.
				this.withinBoundaries = false;
			} else {
				this.withinBoundaries = true;
			}

		} else {
			this.withinBoundaries = true;
		}
	}

	/**
	 * Sets the UV texture sizes for the texture.
	 * @param textureSizeGlCoords - The new size.
     */
	public void setTextureSize(Vector2f textureSizeGlCoords) {
		setTextureSize(textureSizeGlCoords.x, textureSizeGlCoords.y);
	}

	/**
	 * Sets the UV texture sizes for the texture.
	 *
	 * @param textureSizeGlCoordsX - new sizeX
	 * @param textureSizeGlCoordsY - new sizeY
	 */
	public void setTextureSize(float textureSizeGlCoordsX, float textureSizeGlCoordsY) {
		this.relativeTextureSize.x = textureSizeGlCoordsX;
		this.relativeTextureSize.y = textureSizeGlCoordsY;
		updateAbsoluteTextureSizeAndCoords();
	}

	/**
	 * Sets the UV texture offsets for the texture.
	 *
	 * @param textureOffsetGlCoords
	 */
	public void setTextureOffset(Vector2f textureOffsetGlCoords) {
		this.relativeTextureOffsetCoord = textureOffsetGlCoords;
		updateAbsoluteTextureSizeAndCoords();
	}

	/**
	 * Transforms the absolute coordinates based on the current
	 * horizontal and vertical flipping.
	 */
	private void updateAbsoluteTextureSizeAndCoords(){
		// UV texture offset
		float textureOffsetX = relativeTextureOffsetCoord.x;
		float textureOffsetY = relativeTextureOffsetCoord.y;
		float textureSizeX = relativeTextureSize.x;
		float textureSizeY = relativeTextureSize.y;
		if(textureIsFlippedHorizontally){
			textureOffsetX = relativeTextureOffsetCoord.x + relativeTextureSize.x;
			textureSizeX = -relativeTextureSize.x;
		}
		if(textureIsFlippedVertically){
			textureOffsetY = relativeTextureOffsetCoord.y + relativeTextureSize.y;
			textureSizeY = -relativeTextureSize.y;
		}
		absoluteTextureOffsetCoord.x = textureOffsetX;
		absoluteTextureOffsetCoord.y = textureOffsetY;
		absoluteTextureSize.x = textureSizeX;
		absoluteTextureSize.y = textureSizeY;
	}

	/**
	 * @param textureShouldRepeat If true, it should repeat the texture. If false, it should scale the
	 *        image to the size of the quad.
	 */
	public void setTextureRepeat(boolean textureShouldRepeat) {
		this.textureIsRepeating = textureShouldRepeat;
		if (textureShouldRepeat) {
			// repeat the texture to fit.
			int textureWidth = this.texture.get_tWidth();
			int textureHeight = this.texture.get_tHeight();
			float uvWidth = this.size.x / textureWidth;
			float uvHeight = this.size.y / textureHeight;
			setTextureSize(new Vector2f(uvWidth, uvHeight));
		} else {
			// scale the texture to the size of the quad.
			this.relativeTextureOffsetCoord = DEFAULT_TEXTURE_OFFSET;
			this.relativeTextureSize = DEFAULT_TEXTURE_SIZE;
		}
	}

	/**
	 * Use the increase the rotation
	 *
	 * @param rotationIncrease
	 */
	public void rotateBy(float rotationIncrease) {
		this.rotation += rotationIncrease;
	}

	/**
	 * Sets the rotation of the Quad.
	 */
	@Override
	public void setRotation(float newRotation) {
		this.rotation = newRotation;
	}

	/**
	 * Sets the XY position of the quad.
	 *
	 * @param xPos
	 * @param yPos
	 */
	public void setXY(float xPos, float yPos) {
		this.absolutePosition.x = xPos;
		this.absolutePosition.y = yPos;
		setTextureRepeat(this.textureIsRepeating);
		updateBoundaries();
	}

	/**
	 * Sets the XY position of the quad.
	 *
	 * @param xy
	 */
	public void setXY(Vector2f xy) {
		this.absolutePosition = xy;
		setTextureRepeat(this.textureIsRepeating);
		updateBoundaries();
	}

	/**
	 * Sets the x position of the quad.
	 *
	 * @param newX
	 */
	public void setX(float newX) {
		this.absolutePosition.x = newX;
		updateBoundaries();
	}

	/**
	 * Sets the y position of the quad.
	 *
	 * @param newY
	 */
	public void setY(float newY) {
		this.absolutePosition.y = newY;
		updateBoundaries();
	}

	/**
	 * @return (x,y) position of the quad.
	 */
	public Vector2f getXY() {
		return this.absolutePosition;
	}

	/**
	 * Sets the size of the quad.
	 */
	public void setSize(float sizeX, float sizeY) {
		this.size.x = sizeX;
		this.size.y = sizeY;
		updateBoundaries();
	}

	public void setShader2D(Shader2D shader2D) {
		this.shader2D = shader2D;
	}

	/**
	 * Disables the colour blend for the quad.
	 */
	public void resetBlend() {
		this.useBlend = false;
	}

	/**
	 * Sets the colour of the quad.
	 *
	 * @param colour RGBA (floats)
	 */
	public void setBlendColour(Vector4f colour) {
		this.useBlend = true;
		this.blendColour = colour;
	}

	/**
	 * sets the blend percentage of the quad.
	 *
	 * @param amount %
	 */
	public void setBlendAmount(float amount) {
		this.useBlend = true;
		this.blendAmount = amount;
	}

	/**
	 * Sets both the colour and the blend amount
	 * 
	 * @param colour RGBA floats
	 * @param blendAmount %
	 */
	public void setBlend(Vector4f colour, float blendAmount) {
		this.useBlend = true;
		this.blendColour = colour;
		this.blendAmount = blendAmount;
	}

	/**
	 * Sets both the colour and the blend amount
	 * @param r
	 * @param g
	 * @param b
	 * @param a
     * @param blendAmount
     */
	public void setBlend(float r, float g, float b, float a, float blendAmount) {
		this.useBlend = true;
		this.blendColour.x = r;
		this.blendColour.y = g;
		this.blendColour.z = b;
		this.blendColour.w = a;
		this.blendAmount = blendAmount;
	}

	/**
	 * @return the current texture used by the simple quad.
     */
	public TextureInfo getTexture() {
		return texture;
	}

	/**
	 * Sets a new texture for the quad to use.
	 * @param newTexture - The new texture to use.
     */
	public void setTexture(TextureInfo newTexture) {
		newTexture.incrememntReferences();
		texture.decrementReferences();
		this.texture = newTexture;
	}

	/**
	 * @return Whether or not the texture is flipped horizontally.
     */
	public boolean isTextureIsFlippedHorizontally() {
		return textureIsFlippedHorizontally;
	}

	/**
	 * Set or unset the texture having a horizontal flip
	 * @param textureIsFlippedHorizontally
     */
	public void setTextureIsFlippedHorizontally(boolean textureIsFlippedHorizontally) {
		this.textureIsFlippedHorizontally = textureIsFlippedHorizontally;
		updateAbsoluteTextureSizeAndCoords();
	}

	/**
	 * @return Whether or not the texture is flipped vertically.
	 */
	public boolean isTextureIsFlippedVertically() {
		return textureIsFlippedVertically;
	}

	/**
	 * Set or unset the texture having a vertical flip
	 * @param textureIsFlippedVertically
	 */
	public void setTextureIsFlippedVertically(boolean textureIsFlippedVertically) {
		this.textureIsFlippedVertically = textureIsFlippedVertically;
		updateAbsoluteTextureSizeAndCoords();
	}

    public Vector4f getBlendColour() {
        return blendColour;
    }
}
