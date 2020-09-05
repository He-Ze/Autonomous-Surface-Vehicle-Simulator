package renderables.r2D.composite;

import java.util.Iterator;
import java.util.LinkedList;

import javax.vecmath.Vector2f;

import gebd.shaders.Shader2D;
import blindmystics.input.CurrentInput;
import loader.LoadedObject;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import renderables.r2D.Renderable2D;
import renderables.r2D.layer.LayerHandler;
import renderables.r2D.layer.Layerable;
import renderables.r2D.DisplayBoundary;

public class UserInterface extends Renderable2D {

	protected LayerHandler uiLayerHandler = new LayerHandler();
	
	protected DisplayBoundary uiDisplayBoundary = Shader2D.DEFAULT_DISPLAY_BOUNDARY;
	
	protected boolean hasCompletedLoadStage = false;
	
	protected boolean isLoaded = false;
	
	private LinkedList<LoadedObjectHandler<?>> componentsToLoad = new LinkedList<>(); //MUST ALSO BE Layerable

	private LoadedObjectHandler.LoadStatus currentStatus;
	
	public UserInterface(Vector2f relativePosition, Vector2f size, float rotation){
		this.relativePosition = new Vector2f(relativePosition);
		this.size = new Vector2f(size);
		this.rotation = rotation;
	}
	
	
	public <T extends Renderable2D> T addComponentToTop(T component){
		if(component instanceof LoadedObject){
			addNewDependancy((LoadedObject) component);
		}
		uiLayerHandler.addToTop(component);
		addChild(component);
		return component;
	}

	public <T extends Renderable2D> T addComponentToTopWithoutLoading(T component){
		component.setVisible(false);
		uiLayerHandler.addToTop(component);
		addChild(component);
		return component;
	}
	
	public <T extends Renderable2D> T addComponentToBottom(T component) {
		if(component instanceof LoadedObject){
			addNewDependancy((LoadedObject) component);
		}
		uiLayerHandler.addToBottom(component);
		addChild(component);
		return component;
	}

	public <T extends Renderable2D> T addComponentToBottomWithoutLoading(T component){
		component.setVisible(false);
		uiLayerHandler.addToBottom(component);
		addChild(component);
		return component;
	}

	public <T extends LoadedObject> LoadedObjectHandler<T> load(T component, boolean toTop){
		LoadedObjectHandler<T> newElement = LoadedObjectHandler.load(component);
		componentsToLoad.add(newElement);
		return newElement;
	}
	
	public <T extends LoadedObject> T addNewDependancy(T component){
		if (component.getLoadStatus() == null) {
			LoadedObjectHandler<LoadedObject> newElement = new LoadedObjectHandler<LoadedObject>(component);
			componentsToLoad.add(newElement);
			if(hasCompletedLoadStage){
				newElement.handleLoad();
			}
		} else {
			//This component is being loaded by something else.
		}
		return component;
	}
	
	public <T extends Layerable> void moveComponentToTop(T component){
		uiLayerHandler.moveToTop(component);
	}
	
	public <T extends Layerable> void moveComponentToBottom(T component){
		uiLayerHandler.moveToBottom(component);
	}
	
	@Override
	public void update(CurrentInput input, float delta) {
		removeLoadedComponentsFromList();
		uiLayerHandler.update(input, delta);
	}
	
	protected void removeLoadedComponentsFromList(){
		if(hasCompletedLoadStage && (componentsToLoad.size() > 0)){
			Iterator<LoadedObjectHandler<?>> loadingComponents = componentsToLoad.listIterator();
			while(loadingComponents.hasNext()){
				LoadedObjectHandler<?> element = loadingComponents.next();
				if(element.isLoaded()){
					loadingComponents.remove();
				}
			}
		}
	}

	@Override
	public void render() {
		uiLayerHandler.render();
	}

	@Override
	public void setBoundaries(DisplayBoundary newBoundary) {
		this.uiDisplayBoundary = newBoundary;
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

	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		// Probably won't be called.
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		// Probably won't be called.
	}
	
	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler){
		for(LoadedObjectHandler<?> component : componentsToLoad){
			//System.out.println("Adding " + component.getAttachedObject());
			component.addWaiting(handler, handler.getDependancyNo());
			component.handleLoad();
		}
		hasCompletedLoadStage = true;
	}
	

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		removeLoadedComponentsFromList();
	}
	
	@Override
	public Shader2D getShader(){
		return null; //Not important!
	}

}
