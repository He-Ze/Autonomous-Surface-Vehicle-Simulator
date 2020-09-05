package renderables.r2D;

import blindmystics.input.CurrentInput;
import gebd.shaders.Shader2D;
import loader.LoadedObjectHandler;
import javax.vecmath.Vector2f;

/**
 * A Renderable2D object that is never seen!
 *
 * @author Peter Smith 43180543
 */
public class UnrenderableRenderable2D extends Renderable2D {

	public UnrenderableRenderable2D(Vector2f position, Vector2f size, float rotation) {
		setRelativePosition(position);
		setSize(size);
		setRotation(rotation);
	}

	@Override
	public void setBoundaries(DisplayBoundary newBoundary) {
		//Not implemented.
	}

	@Override
	public Shader2D getShader() {
		return null; //No shader.
	}

	@Override
	public void update(CurrentInput input, float delta) {
		//No Operation.
	}

	@Override
	public void render() {
		//Nothings rendered.
	}

	@Override
	public LoadedObjectHandler.LoadStage[] stagesToPerform() {
		return new LoadedObjectHandler.LoadStage[0];
	}

	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {

	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {

	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {

	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {

	}

	@Override
	public LoadedObjectHandler.LoadStatus getLoadStatus() {
		return null;
	}

	@Override
	public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {

	}

}
