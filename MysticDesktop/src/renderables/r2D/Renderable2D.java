package renderables.r2D;

import gebd.Render;
import loader.LoadedObject;
import loader.LoadedObjectAbstract;
import javax.vecmath.Vector2f;

import gebd.shaders.Shader2D;
import renderables.r2D.layer.LayerHandler;
import renderables.r2D.layer.Layerable;

/**
 * Every renderable object extends this and it provides some useful functions for them all.
 * 
 * @author Peter Smith 43180543
 */
public abstract class Renderable2D extends LoadedObjectAbstract implements Layerable {

	// The sibling object to render with this one.
	Renderable2D nextSiblingRenderableComponent = null;
	// The sibling object before that is rendered with this one.
	Renderable2D prevSiblingRenderableComponent = null;
	// The child object to render after this one.
	Renderable2D childRenderableComponent = null;
	//The parent.
	Renderable2D parentRenderableComponent = null;
	//Whether this object should be destroyed.
	private boolean shouldBeRemoved = false;

	//Wheter this object is currently visible.
	protected boolean isVisible = true;

	// The objects absolute screen position.
	protected Vector2f absolutePosition = new Vector2f();
	// The object position relative to its parent.
	protected Vector2f relativePosition = new Vector2f();

	// The scale to apply to the size.
	protected Vector2f sizeScale = new Vector2f(1, 1);
	// The size of the object.
	protected Vector2f size = new Vector2f();

	//The rotation of the object.
	protected float rotation = 0;

	//What the renderableObject currently thinks the screen resolution is.
	protected float renderWidth = Render.WIDTH;
	protected float renderHeight = Render.HEIGHT;
	
	//Layer manager.
	private LayerHandler parentLayerableElement = null;
	private Layerable prevLayerable = null;
	private Layerable nextLayerable = null;

	private boolean hasCompletelyLoaded = false;

	private int frameReceivedMouse = -1;

	/**
	 * @return absolutePosition
	 */
	public Vector2f getAbsolutePosition() {
		return this.absolutePosition;
	}

	/**
	 * @return relativePosition
	 */
	public Vector2f getRelativePosition() {
		return this.relativePosition;
	}

	/**
	 * @return relativePosition.x
	 */
	public float getRelX() {
		return this.relativePosition.x;
	}
	
	public float getRelY() {
		return this.relativePosition.y;
	}

	/**
	 * @param newRelativeX new x component of relativePosition
     */
	public void setRelX(float newRelativeX) {
		this.relativePosition.x = newRelativeX;
	}

	/**
	 * @param newRelativeY new y component of relativePosition
	 */
	public void setRelY(float newRelativeY) {
		this.relativePosition.y = newRelativeY;
	}

	/**
	 * @param newAbsolutePosition - set the absolute position.
	 */
	public void setAbsolutePosition(Vector2f newAbsolutePosition) {
		setAbsolutePosition(newAbsolutePosition.x, newAbsolutePosition.y);
	}
	
	public void setAbsolutePosition(float absoluteX, float absoluteY) {
		this.absolutePosition.x = absoluteX;
		this.absolutePosition.y = absoluteY;
	}

	/**
	 * @param newRelativePosition - sets the relative position
	 */
	public void setRelativePosition(Vector2f newRelativePosition) {
		this.relativePosition.x = newRelativePosition.x;
		this.relativePosition.y = newRelativePosition.y;
	}

	/**
	 * sets the relative position
	 * 
	 * @param x
	 * @param y
	 */
	public void setRelativePosition(float x, float y) {
		this.relativePosition.x = x;
		this.relativePosition.y = y;
	}

	/**
	 * @param x sets relativePosition.x
	 */
	public void setRelativePositionX(float x) {
		this.relativePosition.x = x;
	}

	/**
	 * @param newSize - sets the size
	 */
	public void setSize(Vector2f newSize) {
		setSize(newSize.x, newSize.y);
	}

	/**
	 * sets the size
	 * 
	 * @param width
	 * @param height
	 */
	public void setSize(float width, float height) {
		this.size.x = width;
		this.size.y = height;
	}

	/**
	 * Sets the width
	 * @param sizeX
     */
	public void setWidth(float sizeX){
		setSize(sizeX, this.size.y);
	}

	/**
	 * Sets the height
	 * @param sizeY
     */
	public void setHeight(float sizeY) {
		setSize(this.size.x, sizeY);
	}

	/**
	 * @return size
	 */
	public Vector2f getSize() {
		return this.size;
	}

	/**
	 * @return width
	 */
	public float getWidth() {
		return this.size.x;
	}

	/**
	 * @return height
	 */
	public float getHeight() {
		return this.size.y;
	}

	/**
	 * @return scaling factor for the size.
     */
	public Vector2f getSizeScale() {
		return sizeScale;
	}

	/**
	 * @param newSizeScale
     */
	public void setSizeScale(Vector2f newSizeScale) {
		setSizeScale(newSizeScale.x, newSizeScale.y);
	}

	/**
	 * @param scaleX - New scaleX
	 * @param scaleY - new scaleY
     */
	public void setSizeScale(float scaleX, float scaleY) {
		this.sizeScale.x = scaleX;
		this.sizeScale.y = scaleY;
	}
	
	public Renderable2D getParentRenderableComponent() {
		return parentRenderableComponent;
	}

	public void setParentRenderableComponent(Renderable2D parentRenderableComponent) {
		this.parentRenderableComponent = parentRenderableComponent;
		if(parentRenderableComponent != null){
			parentRenderableComponent.addChild(this);
		}
	}

	public final void addSibling(Renderable2D newSibling) {
		newSibling.parentRenderableComponent = parentRenderableComponent;
		newSibling.prevSiblingRenderableComponent = this;
		if(nextSiblingRenderableComponent != null){
			nextSiblingRenderableComponent.prevSiblingRenderableComponent = newSibling;
		}
		newSibling.nextSiblingRenderableComponent = nextSiblingRenderableComponent;
		nextSiblingRenderableComponent = newSibling;
	}

	public final <T extends Renderable2D> T addChild(T newChild) {
		newChild.parentRenderableComponent = this;
		if(childRenderableComponent != null){
			newChild.nextSiblingRenderableComponent = childRenderableComponent;
			newChild.prevSiblingRenderableComponent = null;
			childRenderableComponent.prevSiblingRenderableComponent = newChild;
		}
		childRenderableComponent = newChild;
		return newChild;
	}

	public final <T extends Renderable2D> void disconnectChild(T childToRemove){
		Renderable2D child = childRenderableComponent;
		while(child != null){
			if(child == childToRemove){
				if (child.prevSiblingRenderableComponent != null) {
					child.prevSiblingRenderableComponent.nextSiblingRenderableComponent = child.nextSiblingRenderableComponent;
				}
				if (child.nextSiblingRenderableComponent != null) {
					child.nextSiblingRenderableComponent.prevSiblingRenderableComponent = child.prevSiblingRenderableComponent;
				}
				if(child == childRenderableComponent){
					childRenderableComponent = child.getSibling();
				}
				child.prevSiblingRenderableComponent = null;
				child.nextSiblingRenderableComponent = null;
				child.setParentRenderableComponent(null);
				return;
			}
			child = child.getSibling();
		}
		//If it hits here, it can't be found!
	}

	public final void removeAndRemoveAllChildren(){
		shouldBeRemoved = true;
		if(childRenderableComponent != null){
			childRenderableComponent.removeAndRemoveAllChildrenAndSiblings();
		}
		childRenderableComponent = null;
		setParentRenderableComponent(null);
	}

	private final void removeAndRemoveAllChildrenAndSiblings(){
		shouldBeRemoved = true;
		if(childRenderableComponent != null){
			childRenderableComponent.removeAndRemoveAllChildrenAndSiblings();
		}
		childRenderableComponent = null;
		if(nextSiblingRenderableComponent != null){
			nextSiblingRenderableComponent.removeAndRemoveAllChildrenAndSiblings();
		}
		nextSiblingRenderableComponent = null;
		setParentRenderableComponent(null);
	}

	public boolean shouldBeRemoved(){
		return shouldBeRemoved;
	}

	/**
	 * @return - child
	 */
	public final Renderable2D getChild() {
		return childRenderableComponent;
	}
	
	public void setChild(Renderable2D newChild) {
		this.childRenderableComponent = newChild;
	}

	public final Renderable2D getSibling() {
		return nextSiblingRenderableComponent;
	}
	
	public void setSibling(Renderable2D newSibling) {
		this.nextSiblingRenderableComponent = newSibling;
	}

	/**
	 * @param newBoundary - sets the display boundaries for the object.
	 */
	public abstract void setBoundaries(DisplayBoundary newBoundary);

	
	
	public abstract Shader2D getShader();

	/**
	 * Given an x,y point and quad dimensions, returns whether the point is within the quad.
	 * 
	 * @param posX
	 * @param posY
	 * @param centerOfQuad
	 * @param quadSize
	 * @return whether the point is within the quad.
	 */
	public static boolean isWithinQuadBounds(float posX, float posY, Vector2f centerOfQuad,
			Vector2f quadSize) {
		float minX = centerOfQuad.x - (quadSize.x / 2);
		float maxX = centerOfQuad.x + (quadSize.x / 2);

		float minY = centerOfQuad.y - (quadSize.y / 2);
		float maxY = centerOfQuad.y + (quadSize.y / 2);

		return ((posX >= minX) && (posX <= maxX) && (posY >= minY) && (posY <= maxY));
	}

	/**
	 * A method that will do nothing by default as roration is not usually implemented. Other object
	 * can implement it if the so choose.
	 * 
	 * @param newRotation
	 */
	public void setRotation(float newRotation) {
		this.rotation = newRotation;
	}

	@Override
	public boolean isVisible() {
		return (isVisible && hasCompletelyLoaded());
	}



	private boolean hasCompletelyLoaded(){
		if (!hasCompletelyLoaded) {
			if (this instanceof LoadedObject) {
				hasCompletelyLoaded = ((LoadedObject) this).isLoaded();
			} else {
				hasCompletelyLoaded = true;
			}
		}
		return hasCompletelyLoaded;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void toggleVisiblity() {
		this.isVisible = !this.isVisible;
	}

	

	/**
	 * Layer manager:
	 */
	@Override public Layerable getNext() {
		return nextLayerable;
	}

	@Override public Layerable getPrev() {
		return prevLayerable;
	}

	@Override
	public LayerHandler getParent() {
		return parentLayerableElement;
	}

	@Override
	public void setParent(LayerHandler newParent) {
		this.parentLayerableElement = newParent;
	}

	@Override public void setNext(Layerable newNext){
		this.nextLayerable = newNext;
	}
	
	@Override public void setPrev(Layerable newPrev){
		this.prevLayerable = newPrev;
	}
	

	public boolean screenResolutionChanged(){
		if((renderWidth == Render.WIDTH) && (renderHeight == Render.HEIGHT)){
			return false;
		}
		renderWidth = Render.WIDTH;
		renderHeight = Render.HEIGHT;
		return true;
	}

	public void setFrameReceivedMouse(int frameReceivedMouse) {
		this.frameReceivedMouse = frameReceivedMouse;
		if (this.parentRenderableComponent != null) {
			this.parentRenderableComponent.setFrameReceivedMouse(frameReceivedMouse);
		}
	}

	public int getFrameReceivedMouse() {
		return frameReceivedMouse;
	}
}
