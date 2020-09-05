package gebd.camera.implementation;

import blindmystics.input.CurrentInput;
import gebd.camera.Camera;
import renderables.r3D.rotation.Quat4fHelper;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class WaterFlippedCamera extends Camera {

	private float waterYPosition = 0;

	public WaterFlippedCamera(){

	}

	public void setWaterHeight(float waterHeight) {
		this.waterYPosition = waterHeight;
	}

	@Override
	public void updateCamera(float delta, CurrentInput input) {
		//Nothing to do here.
	}

	@Override
	public void resetCamera() {
		//Nothing to do here.
	}

	public void updateWithParentCamera(Camera parentCamera) {
		if (parentCamera == null) {
			throw new RuntimeException("Please set the parentCamera first!");
		}

		this.projectionMatrixHandler = parentCamera.getProjectionMatrixHandler();
		this.resetMatricies();

		//Put the camera below the surface at equal depth to height.
		Vector3f parentCameraPosition = parentCamera.getPosition();
		float xPos = parentCameraPosition.x;
		float yPos = flipAboutAxis(waterYPosition, parentCameraPosition.y);
		float zPos = parentCameraPosition.z;
		this.setPosition(xPos, yPos, zPos);

		//Flip about the x (Phi) and z (Roll) axis.
		this.getQuatRotation().set(parentCamera.getQuatRotation());
		Quat4fHelper.flipAboutXAxis(this.getQuatRotation());
		Quat4fHelper.flipAboutZAxis(this.getQuatRotation());

	}

	public static float flipAboutAxis(float axis, float position) {
		return ((2 * axis) - position);
	}
}

