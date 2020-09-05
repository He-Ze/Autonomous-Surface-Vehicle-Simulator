package gebd.camera.implementation;

import java.util.ArrayList;

import blindmystics.input.CurrentInput;
import blindmystics.util.input.mouse.ButtonStatus;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import gebd.camera.Camera;

public class FixedCamera extends Camera {
	
	private boolean limitSet = false, inertiaEnabled = false, lockon = false, zoomed = false, zooming = false;
	private double hLimit, vLimit;
	private float inertiaTheta, inertiaPhi, lockonRadius, lockonStrength, zoomStepSpeed = 1, zoomDistance = 0, zoomSmoothness = 1;
	private int inertia;
	private ArrayList<Vector3f> lockonPoints = new ArrayList<Vector3f>();
	private Vector3f unzoomedLocation = new Vector3f(), zoomedLocation = new Vector3f();
	
	public FixedCamera(){
		resetCamera();
	}
	
	public FixedCamera(Vector3f position, Vector2f rotation) {
		this(position, rotation.x, rotation.y);
	}
	
	public FixedCamera(Vector3f position, float theta, float phi){
		setPosition(position);
		setDefaultOrientation(theta, phi);
		setRotation(theta, phi);
		unzoomedLocation = position;
	}
	
	/**
	 * Set the angle of movement in degrees.
	 * @param turnLimits
	 */
	public void setTurnLimits(Vector2f turnLimits) {
		limitSet = true;
		hLimit = turnLimits.x / 2;
		vLimit = turnLimits.y / 2;
	}
	
	public void unsetTurnLimit() {
		limitSet = false;
	}
	
	public void setInertia(int inertia) {
		inertiaEnabled = true;
		this.inertia = inertia;
		
		inertiaTheta = getTheta();
		inertiaPhi = getPhi();
	}
	
	public void unsetInertia() {
		inertiaEnabled = false;
	}
	
	public void setZoomStats(float distance, float smoothness) {
		zoomDistance = distance;
		zoomSmoothness = smoothness;
	}
	
	@Override
	public void updateCamera(float delta, CurrentInput input) {
		
		rayCalculatedThisFrame = false;
		viewMatrixCalculatedThisFrame = false;
		
		if((input.rightMouse == ButtonStatus.DOWN) || (input.rightMouse == ButtonStatus.JUST_PRESSED)){
			float mouseDiffX = input.prev_MXpos - input.mXpos;
			float mouseDiffY = input.prev_MYpos - input.mYpos;
			
			if (inertiaEnabled) {
				float difference;
				//Camera movement values should be stored as normal, but applied over time.
				
				inertiaTheta += Math.toRadians(0.1f) * mouseDiffX;
				inertiaPhi += Math.toRadians(0.1f) * mouseDiffY;
				
				Vector2f limits = elipticalLimit(inertiaTheta, inertiaPhi);
				
				inertiaTheta = limits.x;
				inertiaPhi = limits.y;
				
				if (lockon) {
					Vector2f update = lockon();
					inertiaTheta += update.x;
					inertiaPhi += update.y;
				}

				if (inertiaTheta != getTheta()) {
					difference = inertiaTheta - getTheta();
					incrementTheta(difference / inertia * delta);
				}
				
				if (inertiaPhi != getPhi()) {
					difference = inertiaPhi - getPhi();
					incrementPhi(difference / inertia * delta);
				}
				
				
			} else {
				incrementTheta((float) (Math.toRadians(0.1f) * mouseDiffX));
				incrementPhi((float) (Math.toRadians(0.1f) * mouseDiffY));
				
				if (lockon) {
					Vector2f update = lockon();
					incrementTheta(update.x);
					incrementTheta(update.y);
				}
			}
			
			if (limitSet) {
				Vector2f limits = elipticalLimit(getTheta(), getPhi());
				setTheta(limits.x);
				setPhi(limits.y);
			}
			
			if(getPhi() > Math.PI){
				setPhi((float) Math.PI);
			} else if(getPhi() < 0){
				setPhi(0f);
			}
		}
		
		if(input.leftMouse == ButtonStatus.JUST_PRESSED) {
			zoomDolly(zoomDistance, zoomSmoothness);
		} else if (input.leftMouse == ButtonStatus.JUST_RELEASED) {
			unzoomDolly();
		}
		
		if (zoomed) {
			position.add(zoomStep());
		} else {
			position.sub(unzoomStep());
		}
	}
	
	private Vector2f elipticalLimit(float theta, float phi) {
		double squishMultiplier = vLimit / hLimit,
				radius = vLimit,
				maxDist = radius,
				phiOffset = phi - DEFAULT_PHI,
				thetaOffset = (theta - DEFAULT_THETA) * squishMultiplier,
				currDist = Math.sqrt((phiOffset * phiOffset) + (thetaOffset * thetaOffset));
		
		if(currDist > maxDist){
			double rotationSquished = Math.atan2(phiOffset, thetaOffset);
			phi = (float) (radius * Math.sin(rotationSquished)) + DEFAULT_PHI;
			theta = (float) ((radius * Math.cos(rotationSquished)) / squishMultiplier) + DEFAULT_THETA;
		}
		return new Vector2f(theta, phi);
	}
	
	public void setLockonStats(float radius, float strength) {
		lockon = true;
		lockonRadius = radius;
		lockonStrength = strength;
	}
	
	public void unsetLockonStats() {
		lockon = false;
	}

	public void addLockonPoint(Vector3f location) {
		lockonPoints.add(location);
	}
	
	public void addLockonPoints(Vector3f[] locations) {
		for (int a = 0; a < locations.length; a++) {
			lockonPoints.add(locations[a]);
		}
	}
	
	
	//TODO additional lockon mode for when user leave camera idle in lockon range
	
	/**
	 * Checks if the camera is in the lock on radius of a point and returns
	 * how the distance needed to follow the lockon rules
	 * 
	 * @return A Vector2f containing the difference in theta and phi
	 */
	private Vector2f lockon() {
		Vector3f location, diff = new Vector3f();
		Vector2f update = new Vector2f();
		float radius = 0;
		
		for (int a = 0; a < lockonPoints.size(); a++) {
			location = lockonPoints.get(a);
			diff = sphericalDifference(location);
			radius = lockonRadius / diff.x;
			if (Math.sqrt(diff.y * diff.y + diff.z * diff.z) <= radius) {
				update.x = diff.y * lockonStrength;
				update.y = diff.z * lockonStrength;
				
				a = lockonPoints.size();
			}
		}
		return update;
	}
	
	public void zoomDolly(float distance, float smoothness) {
		if (!zooming) {
			Vector3f cartesianCoord = cartesianCoordinate(new Vector3f(distance, getTheta(), getPhi()));
			zoomStepSpeed = 1/smoothness;
			zoomed = true;
			unzoomedLocation = new Vector3f(position);

			zoomedLocation = new Vector3f(position);
			zoomedLocation.sub(cartesianCoord);
			zooming = true;
		}
	}
	
	public void unzoomDolly() {
		zoomed = false;
		zooming = true;
	}
	
	/**
	 * Calculates a forward zoom step
	 * 
	 * @return A zoom step towards a zoomed location
	 */
	private Vector3f zoomStep() {
		Vector3f step = new Vector3f();
		
		if (zooming) {
			step.set(zoomedLocation);
			step.sub(position);

			if (Math.abs(step.length()) < 0.01d) {
				zooming = false;
			} else {
				step.x = step.x * zoomStepSpeed;
				step.y = step.y * zoomStepSpeed;
				step.z = step.z * zoomStepSpeed;
			}
		}
		
		return step;
	}
	
	private Vector3f unzoomStep() {
		Vector3f step = new Vector3f();
		
		if (zooming) {
			step.set(position);
			step.sub(unzoomedLocation);

			if (Math.abs(step.length()) < 0.01d) {
				zooming = false;
			} else {
				step.x = step.x * zoomStepSpeed;
				step.y = step.y * zoomStepSpeed;
				step.z = step.z * zoomStepSpeed;
			}
		}
		
		return step;
	}

	@Override
	public void resetCamera() {
		setPosition(new Vector3f(0, 0, 0)); //TODO make this properly the default
		setRotation(DEFAULT_THETA, DEFAULT_PHI);
	}
}