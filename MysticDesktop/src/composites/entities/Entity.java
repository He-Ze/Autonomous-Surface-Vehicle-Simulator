package composites.entities;

import blindmystics.input.CurrentInput;
import blindmystics.util.input.Updates;

import gebd.GraphicalPicking;
import gebd.Render;
import gebd.shaders.Shader3D;
import loader.LoadedObject;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;

import javax.vecmath.*;

import renderables.r3D.model.ModelsetModel;
import renderables.r3D.model.ModelsetModelInstanceHandler;
import renderables.r3D.object.Has3DPositionAndRotation;
import renderables.r3D.rotation.Quat4fHelper;
import renderables.texture.TextureInfo;

public class Entity extends Has3DPositionAndRotation implements LoadedObject, Updates {

	public static final Vector3f DEFAULT_POSITION = new Vector3f(0, 0, 0);
	protected Vector3f position = new Vector3f(DEFAULT_POSITION);
	public static final Vector3f DEFAULT_SIZE = new Vector3f(1, 1, 1);
	protected Vector3f size = new Vector3f();
	public static final Vector3f DEFAULT_ROTATION = new Vector3f(0, 0, 0);
	protected Quat4f quaternionRotation = new Quat4f();
	protected ModelsetModel model;
	protected String modelPath;
	protected String texturePath;
	protected TextureInfo texture;
	protected String name;
	protected boolean visible = true;
	protected boolean usePicking = true;
	public static final Vector3f DEFAULT_BLEND_COLOUR= new Vector3f(1, 1, 1);
	protected Vector3f textureBlendColour = new Vector3f(DEFAULT_BLEND_COLOUR);
	public static final float DEFAULT_BLEND_AMOUNT = 0f;
	protected float textureBlendAmount = DEFAULT_BLEND_AMOUNT;
	
	protected LoadStage[] loadStages;

	private LoadedObjectHandler.LoadStatus currentStatus;
	
	@Override
	public LoadStage[] stagesToPerform(){
		return loadStages;
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
	public boolean isLoaded() {
		return (getLoadStatus() == LoadedObjectHandler.LoadStatus.COMPLETE_LOAD);
	}

	public Entity (String modelPath, String texturePath, Vector3f position, Vector3f size, Vector3f rotation){
		this("UNKNOWN NAME!", modelPath, texturePath, position, size, rotation);
	}
	
	public Entity (String name, String modelPath, String texturePath, Vector3f position, Vector3f size, Vector3f rotation){
		loadStages = new LoadStage[] {
				LoadStage.LOAD_DEPENDENCIES
		};
		this.name = name;
		this.modelPath = modelPath;
		this.texturePath = texturePath;
		setPosition(position);
		setSize(size);
		setRotation(rotation);
	}
	
	protected Entity (Vector3f position, Vector3f size, Vector3f rotation){
		loadStages = new LoadStage[] {};
		
		setPosition(position);
		setSize(size);
		setRotation(rotation);
	}


	public Entity(String name, ModelsetModel model, String texturePath, Vector3f position, Vector3f size, Vector3f rotation) {
		loadStages = new LoadStage[] {
				//LoadStage.LOAD_DATA_FROM_FILE,
				//LoadStage.HANDLE_RAW_DATA,
				LoadStage.LOAD_DEPENDENCIES,
		};
		this.name = name;
		this.model = model;
		this.texturePath = texturePath;
		setPosition(position);
		setSize(size);
		setRotation(rotation);
	}
	

	public Entity(String name, ModelsetModel model, TextureInfo texture, Vector3f position, Vector3f size, Vector3f rotation){
		loadStages = new LoadStage[] {
				//LoadStage.LOAD_DATA_FROM_FILE,
				//LoadStage.HANDLE_RAW_DATA,
				//LoadStage.LOAD_DEPENDENCIES,
		};
		this.name = name;
		this.model = model;
		this.texture = texture;
		texture.incrememntReferences();
		setPosition(position);
		setSize(size);
		setRotation(rotation);
	}
	
	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		//NOT CALLED!
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		//NOT CALLED!
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		if (texture == null) {
			texture = TextureInfo.queueLoadOfPNGTexture(texturePath, handler);
		}
		if (model == null) {
			//model = Render.getInstance().queueLoadModel(modelPath, this);
			model = ModelsetModelInstanceHandler.getModel(modelPath);
		}
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		//model.addEntity(this);
		//System.out.println("Entity completed load");
		Render.instance.addLoadedEntity(this);
	}

	public void removeFromRenderList() {
		Render.instance.removeLoadedEntity(this);
	}

	public String getName() {
		return name;
	}

	@Deprecated
	public void increaseRotX(double XIncrease){
		Quat4f offset = new Quat4f();
		offset.set(new AxisAngle4d(1f, 0f, 0f, XIncrease));
		offset.mul(quaternionRotation);
		quaternionRotation.set(offset);
	}

	@Deprecated
	public void increaseRotY(double YIncrease){
		Quat4f offset = new Quat4f();
		offset.set(new AxisAngle4d(0f, 1f, 0f, YIncrease));
		offset.mul(quaternionRotation);
		quaternionRotation.set(offset);
	}

	@Deprecated
	public void increaseRotZ(double ZIncrease){
		Quat4f offset = new Quat4f();
		offset.set(new AxisAngle4d(0f, 0f, 1f, ZIncrease));
		offset.mul(quaternionRotation);
		quaternionRotation.set(offset);
	}
	/*
	public void addEntityToRenderList(){
		model.addFrameEntity(this);
	}
	*/
	
	
	/*public int getNumOfVertices() {
		return model.getNumOfVertices();
	}*/
	
	public boolean hasTransparancy(){
		//System.out.println(this.texture.getTextureId());
		return texture.hasAlpha();
	}
	
	/*public void addToRenderList(){
		model.addFrameEntity(this);
	}
	
	public Model getModel(){
		return this.model;
	}*/


	public void setSize(Vector3f newSize) {
		setSize(newSize.x, newSize.y, newSize.z);
	}
	
	public void setSize(float x, float y, float z) {
		size.x = x;
		size.y = y;
		size.z = z;
	}
	
	public Vector3f getSize() {
		return size; //Consider instead using: new Vector3f(size);
	}

//	@Override
//	public void setRotation(float rotationX, float rotationY, float rotationZ) {
//		Quat4fHelper.toQuat4f(rotationX, rotationY, rotationZ, quaternionRotation);
//	}

	@Deprecated
	public Quat4f getQuaternionRotation() {
		return quaternionRotation;
	}

	public void setTexture(TextureInfo newTexture){
		if (this.texture != null) {
			this.texture.decrementReferences();
		}
		newTexture.incrememntReferences();
		this.texture = newTexture;
	}

	public void setPosition(javax.vecmath.Vector3f position) {
		setPosition(position.x, position.y, position.z);
	}

	public TextureInfo getTexture() {
		return this.texture;
	}

	public ModelsetModel getModel() {
		return model;
	}

	public Vector3f getTextureBlendColour() {
		return textureBlendColour;
	}

	public void setTextureBlendColour(Vector3f newBlendColour) {
		this.textureBlendColour.x = newBlendColour.x;
		this.textureBlendColour.y = newBlendColour.y;
		this.textureBlendColour.z = newBlendColour.z;
		this.textureBlendAmount = 1f;
	}

	public float getTextureBlendAmount() {
		return textureBlendAmount;
	}

	public void setTextureBlendAmount(float textureBlendAmount) {
		this.textureBlendAmount = textureBlendAmount;
	}

	@Override
	public void update(CurrentInput input, float delta) {
		//Nothing to update.
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * TODO - Think about memory management!
	 * @param
	 */
	/*
	public void setNextRenderedEntity(Entity e) {
		nextRenderedEntity = e;
	}
	
	public Entity getNextRenderedEntity() {
		return nextRenderedEntity;
	}
	*/



	public void render(Shader3D shader) {
		if (visible) {
			shader.prepareEntity(this);
			model.drawElements();
		}
	}

	public void render(Shader3D shader, Vector4f colour, boolean pickingRender) {
		if (visible) {
			shader.prepareEntity(this);
			shader.setColour(colour);

			if (pickingRender) {
				model.drawGraphicalPicking();
			} else {
				model.drawElements();
			}
		}
	}

	public void renderAsPicking(Shader3D shader, int entityNumber) {
		if (visible && usePicking) {
			shader.prepareEntity(this);
			GraphicalPicking.prepareEntityColour(shader, entityNumber);
			model.drawGraphicalPicking();
		}
	}

	public boolean isUsePicking() {
		return usePicking;
	}

	public void setUsePicking(boolean usePicking) {
		this.usePicking = usePicking;
	}

	/*public void renderAsTransparent(Shader3D shader3D) {
		model.prepareModel();
		shader3D.prepareEntity(this);
		model.drawElements();
	}*/
}
