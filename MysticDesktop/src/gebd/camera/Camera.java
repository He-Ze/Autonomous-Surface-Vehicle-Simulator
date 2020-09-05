package gebd.camera;

import blindmystics.input.CurrentInput;
import blindmystics.util.vector.Matrix4fHelper;
import gebd.camera.projection.ProjectionMatrixHandler;
import gebd.util.Util3D;


import gebd.Render;
import renderables.r3D.object.Has3DPositionAndRotation;

import javax.vecmath.*;

public abstract class Camera extends Has3DPositionAndRotation {
	/*
	 * Currently, there is a problem that when a new camera is changed
	 * that all of the input handlers that are attached to key bindings aren't destroyed.
	 * OH SHIT
	 * Memory management in java?!
	 * Woah
	 * I wonder if it will clean it up for me ... I doubt it though.
	 * 
	 *  TESTED:
	 *  IT WON'T
	 *  
	 *  A method will be needed to be created,
	 *  presumably within RenderBase.
	 *  That will remove all references, before creating a new type of camera.
	 */
	//private static final float DEFAULT_PHI = (float) (Math.PI / 2);
	//private static final float DEFAULT_THETA = (float) ((Math.PI) / 2);

	//Default orientation is looking directly forward.
	protected float DEFAULT_PHI = 0f;
	protected float DEFAULT_THETA = 0f;
	protected float DEFAULT_ROLL = 0f;
	
	protected Matrix4f rotationMatrix = new Matrix4f();
	protected Matrix4f invertedRotationMatrix = new Matrix4f();
	protected ProjectionMatrixHandler projectionMatrixHandler = new ProjectionMatrixHandler(0.5f, 1000f, 60f, Render.ASPECT_RATIO, true);
	protected Matrix4f projectionRotationMatrix = new Matrix4f();
	protected Matrix4f viewMatrix = new Matrix4f();
	protected Matrix4f invertedViewMatrix = new Matrix4f();
	protected Vector3f currentRay = new Vector3f();
	protected boolean viewMatrixCalculatedThisFrame = false;
	protected boolean invertedViewMatrixCalculatedThisFrame = false;
	protected boolean invertedRotationMatrixCalculatedThisFrame = false;
	protected boolean rayCalculatedThisFrame = false;
	protected boolean projectionRotationMatrixCalculatedThisFrame = false;
	protected boolean rotationMatrixUpdatedThisFrame = false;

	public Camera() {
		setRotation(DEFAULT_THETA, DEFAULT_PHI, DEFAULT_ROLL);
	}

	/**
	 * How the camera behaves on a per-camera basis.
	 * @param delta - The current time step.
	 * @param input - The current input.
     */
	protected abstract void updateCamera(float delta, CurrentInput input);

	/**
	 * Called when the camera is reset.
     */
	public abstract void resetCamera();

	public ProjectionMatrixHandler getProjectionMatrixHandler() {
		return projectionMatrixHandler;
	}

	public void calculateRotationMatrix() {

		//Only calculate the matrix once per frame for efficiency.
		if (rotationMatrixUpdatedThisFrame) {
			//Rotation matrix has already been calculated.
			//return;
		}
		rotationMatrixUpdatedThisFrame = true;

		/*
		//The rotation matrix is updating, so the projection rotation matrix must also update.
		float cameraRotationX = (float) (getPhi() - (Math.PI / 2));
		float cameraRotationY = (float) -(getTheta() - (Math.PI / 2));
		float cameraRotationZ = getRoll();

		Matrix4f rotationXMatrix = new Matrix4f();
		//m[column][row]
		rotationXMatrix.m00 = 1;
		rotationXMatrix.m11 = (float) Math.cos(cameraRotationX);
		rotationXMatrix.m21 = (float) -Math.sin(cameraRotationX);
		rotationXMatrix.m12 = (float) Math.sin(cameraRotationX);
		rotationXMatrix.m22 = (float) Math.cos(cameraRotationX);
		rotationXMatrix.m33 = 1;

		Matrix4f rotationYMatrix = new Matrix4f();
		//m[column][row]
		rotationYMatrix.m00 = (float) Math.cos(cameraRotationY);
		rotationYMatrix.m20 = (float) Math.sin(cameraRotationY);
		rotationYMatrix.m11 = 1;
		rotationYMatrix.m02 = (float) -Math.sin(cameraRotationY);
		rotationYMatrix.m22 = (float) Math.cos(cameraRotationY);
		rotationYMatrix.m33 = 1;

		Matrix4f rotationZMatrix = new Matrix4f();
		//m[column][row]
		rotationZMatrix.m00 = (float) Math.cos(cameraRotationZ);
		rotationZMatrix.m10 = (float) -Math.sin(cameraRotationZ);
		rotationZMatrix.m22 = 1;
		rotationZMatrix.m01 = (float) Math.sin(cameraRotationZ);
		rotationZMatrix.m11 = (float) Math.cos(cameraRotationZ);
		rotationZMatrix.m33 = 1;



//		Matrix4f.mul(rotationZMatrix, rotationYMatrix, rotationMatrix);
//		Matrix4f.mul(rotationXMatrix, rotationMatrix, rotationMatrix);

		//NOTE: - For Efficiency, this will trash rotationYMatrix so don't use them again.
		rotationYMatrix.mul(rotationXMatrix);
		rotationMatrix.set(rotationZMatrix);
		rotationMatrix.mul(rotationYMatrix);
		*/

		//The new implementation of the rotations.
		rotationMatrix = new Matrix4f();
		Quat4f rotation = new Quat4f(getQuatRotation());
		//rotation.inverse();
		rotationMatrix.set(rotation);
		//rotationMatrix.invert();

		/*
		AxisAngle4f angles = new AxisAngle4f(0, 0, 0, 0);
		Quat4f rotation = new Quat4f();
		rotation.set(angles);
		rotationMatrix.set(rotation); //TODO - Change.
		*/

		//I HOPE this is the correct order...
//		rotationMatrix.set(rotationZMatrix);
//		rotationMatrix.mul(rotationYMatrix);
//		rotationMatrix.mul(rotationXMatrix);

//		Matrix4f.mul(rotationXMatrix, rotationYMatrix, rotationMatrix);

		projectionRotationMatrix.set(rotationMatrix);
		projectionRotationMatrix.mul(projectionMatrixHandler.getProjectionMatrix());
		projectionRotationMatrixCalculatedThisFrame = true;
	}

	public void update(float delta, CurrentInput input){
		resetMatricies();
		updateCamera(delta, input);
	}

	public void resetMatricies() {
		this.viewMatrixCalculatedThisFrame = false;
		this.invertedViewMatrixCalculatedThisFrame = false;
		this.rayCalculatedThisFrame = false;
		this.projectionRotationMatrixCalculatedThisFrame = false;
		this.invertedRotationMatrixCalculatedThisFrame = false;
		this.rotationMatrixUpdatedThisFrame = false;
	}

	public void updateRotation(float thetaIncrease, float phiIncrease) {
		incrementPhi(phiIncrease);
		incrementTheta(thetaIncrease);
	}
	
	public Matrix4f getRotationMatrix() {
		calculateRotationMatrix();
		return rotationMatrix;
	}
	
	public Matrix4f getProjectionRotationMatrix(){
		calculateRotationMatrix();
		return projectionRotationMatrix;
	}
	
	protected void calculateViewMatrix(){
		//Real Space to GL space translation where z & y are swapped.
		Vector3f negativeCameraPos = new Vector3f(-position.x, -position.z, -position.y);
        viewMatrix.set(getRotationMatrix());
		//Matrix4fHelper.translate(viewMatrix, negativeCameraPos);
		viewMatrixCalculatedThisFrame = true;
	}
	
	public Matrix4f getViewMatrix(){
		if(!viewMatrixCalculatedThisFrame){
			calculateViewMatrix();
		}
		return viewMatrix;
	}
	
	public Matrix4f getInvertedViewMatrix(){
		if(!invertedViewMatrixCalculatedThisFrame){
            invertedViewMatrix.set(getViewMatrix());
            invertedViewMatrix.invert();
            invertedViewMatrixCalculatedThisFrame = true;
		}
		return invertedViewMatrix;
	}

	public Matrix4f getInvertedRotationMatrix() {
		if (!invertedRotationMatrixCalculatedThisFrame) {
			invertedViewMatrix.invert(getRotationMatrix());
		}
		return invertedViewMatrix;
	}
	
	
	private void updateCurrentRay(CurrentInput input){
		currentRay = getRayFromScreenCoords(input.getViewPortSpaceOfMouse());
		rayCalculatedThisFrame = true;
	}

	public Vector3f getRayFromScreenCoords(Vector2f screenCoords){
		return getRayFromScreenCoords(screenCoords, Render.getScreenSize());
	}

	public Vector3f getRayFromScreenCoords(Vector2f screenCoords, Vector2f renderedSize){
		Vector2f normalizedCoords = Util3D.getNormalisedDeviceSpace(screenCoords, renderedSize);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1, 1);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f newRay = toWorldSpace(eyeCoords);
		return newRay;
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords){
        Vector4f cCoords = new Vector4f(clipCoords);
        projectionMatrixHandler.getInvertedProjectionMatrix().transform(cCoords);
		return new Vector4f(cCoords.x, cCoords.y, -1f, 0f);
	}
	
	private Vector3f toWorldSpace(Vector4f eyeCoords){
		Vector3f worldCoords = new Vector3f(eyeCoords.x, eyeCoords.y, eyeCoords.z);
		//getInvertedRotationMatrix().transform(worldCoords);
		getRotationMatrix().transform(worldCoords);
		worldCoords.normalize();
		return worldCoords;
		/*
        Vector4f worldCoords = new Vector4f(eyeCoords);
        getInvertedViewMatrix().transform(worldCoords);
		Vector3f calculatedRay = new Vector3f(worldCoords.x, worldCoords.y, worldCoords.z);
		calculatedRay.normalize();
		return calculatedRay;
		*/
	}

	public Vector2f getScreenCoordsFromRay(Vector3f ray, Vector2f renderedSize) {
		Vector4f worldCoords = new Vector4f(ray.x, ray.y, ray.z, 0);
		worldCoords.scale(-1.0f / ray.z); //Anti - Normalize step... Weird, yes, but required...
        Vector4f eyeCoords = new Vector4f(worldCoords);
        getViewMatrix().transform(eyeCoords);
		Vector4f cCoords = new Vector4f(eyeCoords.x, eyeCoords.y, -1, 0);
        Vector4f clipCoords = new Vector4f(cCoords);
        projectionMatrixHandler.getProjectionMatrix().transform(clipCoords);
		//Vector2f normalizedDeviceSpaceCoords = new Vector2f(clipCoords.x, clipCoords.y);
		return Util3D.getScreenSpace(clipCoords.x, clipCoords.y, renderedSize.x, renderedSize.y);
	}
	
	public Vector3f getProjectedRay(CurrentInput input) {
		if(!rayCalculatedThisFrame){
			updateCurrentRay(input);
		}
		return currentRay;
	}
	
	public void setDefaultOrientation(float theta, float phi) {
		this.DEFAULT_THETA = theta;
		this.DEFAULT_PHI = phi;
	}
	
	/**
	 * Points camera at the specified location.
	 * 
	 * @param location Location to point at
	 */
	public void lookAt(Vector3f location) {
		Vector3f difference = sphericalDifference(location);
		
		updateRotation(difference.y, difference.z);
	}
	
	/**
	 * Calculates the difference in spherical coordinates between the camera's own spherical coordinates
	 * and another location.
	 * 
	 * Result is in form - (r, theta, phi)
	 * When theta is zero camera looks along the positive x axis
	 * When phi is zero camera looks along the positive z axis
	 * @param location Another location in space
	 * @return A Vector3f containing amount the camera needs to rotate and move to match the location of the object
	 */
	protected Vector3f sphericalDifference(Vector3f location) {
        Vector3f relativeLocation = new Vector3f(location);
        relativeLocation.sub(position);
        Vector3f result;

		result = sphericalCoordinate(relativeLocation);
		result.y -= getTheta();
		result.z -= getPhi();
		return result;
	}
	
	protected Vector3f sphericalCoordinate(Vector3f cartesianCoordinates) {
		Vector3f result = new Vector3f();
		result.x = (float) cartesianCoordinates.length();
		result.y = (float) Math.atan2(cartesianCoordinates.y, cartesianCoordinates.x);
		result.z = (float) Math.acos(cartesianCoordinates.z/result.x);
		
		return result;
	}
	
	/**
	 * 
	 * @param sphericalCoordinates In form (r, theta, phi)
	 * @return
	 */
	protected Vector3f cartesianCoordinate(Vector3f sphericalCoordinates) {
		Vector3f result = new Vector3f();
		result.x = (float) (sphericalCoordinates.x * Math.cos(sphericalCoordinates.y) * Math.sin(sphericalCoordinates.z));
		result.y = (float) (sphericalCoordinates.x * Math.sin(sphericalCoordinates.y) * Math.sin(sphericalCoordinates.z));
		result.z = (float) (sphericalCoordinates.x * Math.cos(sphericalCoordinates.z));

		return result;
	}


	public void updateProjectionWithNewResolution(float width, float height) {
		//private ProjectionMatrixHandler projectionMatrixHandler = new ProjectionMatrixHandler(0.1f, 100f, 60f, Render.ASPECT_RATIO, true);
		projectionMatrixHandler.setAspectRatio(height / width);

	}
}
