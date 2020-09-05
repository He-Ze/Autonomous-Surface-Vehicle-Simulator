package renderables.r2D.composite;

import blindmystics.input.CurrentInput;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

import gebd.shaders.Shader2D;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import renderables.r2D.Renderable2D;
import renderables.r2D.DisplayBoundary;
import renderables.r2D.simple.SimpleQuad;

/**
 * Two quads, one larger than the other, and behind the other. The both have different colours, so
 * it appears that the smaller quad has a border.
 * 
 * @author Peter Smith 43180543
 */
public class BorderedRect extends Renderable2D {

	// The width of the border.
	protected float lineWidth = 0f;
	// The size of the larger quad
	protected Vector2f borderSize = new Vector2f();
	protected Vector2f fillSize = new Vector2f();
	// The rotation
	protected float rotation;

	// The smaller quad.
	protected SimpleQuad fill;
	protected SimpleQuad borderQuad;

	//The texture
	protected String texturePath;

	// Black is the default line colour.
	public static final Vector4f DEFAULT_LINE_COLOUR = new Vector4f(0, 0, 0, 1);
	public static final Vector4f DEFAULT_FILL_COLOUR = new Vector4f(1, 1, 1, 1);

	protected Vector4f fillColour = DEFAULT_FILL_COLOUR;
	protected float fillBlend = 1f;

	protected Vector4f lineColour = DEFAULT_LINE_COLOUR;
	protected float lineBlend = 1f;

	protected LineAlignment lineAlignment;

	private LoadedObjectHandler.LoadStatus currentStatus;

	public enum LineAlignment {
		OUTSIDE,
		CENTER,
		INSIDE
	}

	/**
	 * Constructor that initializes both quads.
	 * 
	 * @param texturePath
	 * @param screenPosition
	 * @param size
	 * @param rotation
	 * @param lineWidth
	 */
	public BorderedRect(String texturePath, Vector2f screenPosition, Vector2f size, float rotation,
						float lineWidth) {
		this(texturePath, screenPosition, size, rotation, lineWidth, LineAlignment.OUTSIDE);
	}
	public BorderedRect(String texturePath, Vector2f screenPosition, Vector2f size, float rotation,
			float lineWidth, LineAlignment lineAlignment) {
		this.relativePosition = screenPosition;
		this.size = size;
		this.rotation = rotation;

		this.lineAlignment = lineAlignment;
		
		this.texturePath = texturePath;
		
		this.lineWidth = lineWidth;
		this.relativePosition = screenPosition;
		this.size = size;
		this.rotation = rotation;
		
		setLineColour(DEFAULT_LINE_COLOUR, 1.0f);

		this.fill = new SimpleQuad(texturePath, new Vector2f(0, 0), size, rotation);
		this.borderQuad = new SimpleQuad(texturePath, new Vector2f(0, 0), size, rotation);
	}

	@Override
	public LoadStage[] stagesToPerform() {
		return new LoadStage[] {
				//LoadStage.LOAD_DATA_FROM_FILE,
				//LoadStage.HANDLE_RAW_DATA,
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

	/**
	 * Constructor that initializes both quads, but the smaller without a texture.
	 * 
	 * @param screenPosition
	 * @param size
	 * @param rotation
	 * @param lineWidth
	 */
	public BorderedRect(Vector2f screenPosition, Vector2f size, float rotation, float lineWidth) {
		this(null, screenPosition, size, rotation, lineWidth);
	}

	public LineAlignment getLineAlignment() {
		return lineAlignment;
	}

	public void setLineAlignment(LineAlignment lineAlignment) {
		this.lineAlignment = lineAlignment;
		calculateQuadSizes();
	}

	protected void calculateQuadSizes(){
		float innerSizeX = size.x;
		float innerSizeY = size.y;
		switch (lineAlignment) {
			case OUTSIDE:
				//Size doesn't require modification.
				break;
			case CENTER:
				innerSizeX -= lineWidth;
				innerSizeY -= lineWidth;
				break;
			case INSIDE:
				innerSizeX -= (lineWidth * 2);
				innerSizeY -= (lineWidth * 2);
				break;
		}
		float fillSizeX = innerSizeX + (2 * lineWidth);
		float fillSizeY = innerSizeY + (2 * lineWidth);
		this.fill.setSize(innerSizeX, innerSizeY);
		this.borderQuad.setSize(fillSizeX, fillSizeY);
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
		handler.newDependancy(this.borderQuad);
		setLineColour(lineColour, lineBlend);
		this.borderQuad.setParentRenderableComponent(this);

		handler.newDependancy(this.fill);
		this.fill.setParentRenderableComponent(this);
		setBackdropColour(fillColour, fillBlend);
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		setSize(size);
	}
	



	/**
	 * Sets the size of both quads.
	 */
	@Override
	public void setSize(float sizeX, float sizeY) {
		super.setSize(sizeX, sizeY);
		calculateQuadSizes();
	}

	/**
	 * Sets the boudaries for both quads.
	 */
	@Override
	public void setBoundaries(DisplayBoundary newBoundary) {
		this.fill.setBoundaries(newBoundary);
		this.borderQuad.setBoundaries(newBoundary);
	}

	/**
	 * Only have to update the bigger quad as it covers all space that the smaller one does.
	 */
	@Override
	public void update(CurrentInput input, float delta) {
		// Only really have to update this one, save processing power!
		borderQuad.update(input, delta);
	}
	
	/**
	 * Renders both quads in order.
	 */
	@Override public void render() {
		this.borderQuad.render();
		this.fill.render();
	}

	@Override
	public Shader2D getShader() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Sets the colour and blend of the smaller quad.
	 * 
	 * @param colour
	 * @param blendAmount
	 */
	public void setBackdropColour(Vector4f colour, float blendAmount) {
		if (fill == null) {
			this.fillColour = colour;
			this.fillBlend = blendAmount;
		} else {
			fill.setBlend(colour, blendAmount);
		}
	}

	/**
	 * sets the colour and blend of the line.
	 * 
	 * @param colour
	 * @param blendAmount
	 */
	public void setLineColour(Vector4f colour, float blendAmount) {
		if (borderQuad == null) {
			this.lineColour = colour;
			this.lineBlend = blendAmount;
		} else {
			borderQuad.setBlend(colour, blendAmount);
		}
	}

	/**
	 * rotates both quads to the specified amount.
	 */
	@Override
	public void setRotation(float newRotation) {
		super.setRotation(newRotation);
		this.fill.setRotation(newRotation);
	}

	

}
