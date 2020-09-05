package gebd.camera.implementation;

import blindmystics.input.CurrentInput;
import gebd.camera.Camera;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class SuperFixedCamera extends Camera {

	public SuperFixedCamera(){
		resetCamera();
	}

	public SuperFixedCamera(Vector3f position, float theta, float phi){
		setPosition(position);
		setDefaultOrientation(theta, phi);
		setRotation(theta, phi);
	}

	public SuperFixedCamera(Vector3f position, Vector2f rotation) {
		this(position, rotation.x, rotation.y);
	}

	@Override
	public void updateCamera(float delta, CurrentInput input) {
		rayCalculatedThisFrame = false;
		viewMatrixCalculatedThisFrame = false;
	}

	@Override
	public void resetCamera() {
		setPosition(new Vector3f(0, 0, 0)); //TODO make this properly the default
		setRotation(DEFAULT_THETA, DEFAULT_PHI);
	}
}