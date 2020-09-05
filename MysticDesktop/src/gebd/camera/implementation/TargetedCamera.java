package gebd.camera.implementation;

import blindmystics.input.CurrentInput;
import blindmystics.input.KeyboardInputLatchGenerator;
import blindmystics.util.input.InputLatch;
import blindmystics.util.input.keyboard.HandleKeyboard;
import blindmystics.util.input.mouse.ButtonStatus;

import org.lwjgl.input.Keyboard;
import javax.vecmath.Vector3f;

import gebd.camera.Camera;

public class TargetedCamera extends Camera {
	
	private float distance = 1f, targetTheta = 0f, targetPhi = 0f;
	private Vector3f targetOrigin = new Vector3f(0, 0, 0);

	private InputLatch forwardHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_W);
	private InputLatch backwardHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_S);
	private InputLatch leftHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_A);
	private InputLatch rightHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_D);
	private InputLatch upHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_R);
	private InputLatch downHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_F);
	private InputLatch speedUpHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_LSHIFT);
	private InputLatch slowDownHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_LCONTROL);
	
	public TargetedCamera(){
		resetCamera();
	}

	public TargetedCamera(Vector3f targetOrigin, float theta, float phi, float distance){
		setPosition(targetOrigin);
		setRotation(theta, phi);
		this.targetOrigin = targetOrigin;
		this.distance = distance;
	}
	
	@Override
	public void updateCamera(float delta, CurrentInput input) {
		rayCalculatedThisFrame = false;
		viewMatrixCalculatedThisFrame = false;
		
		
		//leftMouse
		//middleMouse
		//rightMouse
		
		if((input.rightMouse == ButtonStatus.DOWN) || (input.rightMouse == ButtonStatus.JUST_PRESSED)){
			float mouseDiffX = input.prev_MXpos - input.mXpos;
			float mouseDiffY = input.prev_MYpos - input.mYpos;
			//System.out.println(Phi);
			
			targetTheta += Math.toRadians(0.1f) * mouseDiffX;
			targetPhi -= Math.toRadians(0.1f) * mouseDiffY;
			
			float sinPhi = (float) Math.sin(targetPhi);
			
			float boomX = (float) (distance * Math.cos(targetTheta));
			float boomZ = (float) (distance * Math.sin(targetTheta));
			
			boomX = boomX * sinPhi;
			boomZ = boomZ * sinPhi;
			
			position.x = targetOrigin.x + boomX;
			position.z = targetOrigin.z + boomZ;
			position.y = targetOrigin.y + (float) (distance * Math.cos(targetPhi));
			
			lookAt(targetOrigin);
			
			if (targetPhi > Math.PI){
				targetPhi = (float) Math.PI;
			} else if (targetPhi < 0){
				targetPhi = 0;
			}
		}
		
		float extraSpeed = 1;
		if(speedUpHandler.isHeld()){
			extraSpeed = 4;
		} else if (slowDownHandler.isHeld()) {
			extraSpeed = 0.25f;
		}
		
		float moveDistance = 0.001f * delta * extraSpeed;
		
		if(leftHandler.isHeld()){
			float tempTheta = (float) (getTheta() + (Math.PI/2));
			targetOrigin.x += Math.cos(tempTheta) * moveDistance;
			targetOrigin.z += Math.sin(tempTheta) * moveDistance;
			
		}
		
		if(rightHandler.isHeld()){
			float tempTheta = (float) (getTheta() + (Math.PI/2));
			targetOrigin.x -= Math.cos(tempTheta) * moveDistance;
			targetOrigin.z -= Math.sin(tempTheta) * moveDistance;
		}
		
		if(upHandler.isHeld()){
			targetOrigin.y = targetOrigin.y - (0.001f * delta);
		}
		
		if(downHandler.isHeld()){
			targetOrigin.y = targetOrigin.y + (0.001f * delta);
		}
		
		if(forwardHandler.isHeld()){
			distance += 0.001f * delta;
			/*
			//KEY_W Usually
			
			System.out.println("Phi = " + phi);
			System.out.println("Theta = " + theta);
			System.out.println("sin(Phi) = " + Math.sin(phi));
			System.out.println("cos(Phi) = " + Math.cos(phi));
			
			System.out.println("sin(Theta) = " + Math.sin(theta));
			System.out.println("cos(Theta) = " + Math.cos(theta));
			
			
			
			cameraPos.y += moveDistance * Math.sin(theta) * Math.sin(phi);
			
			cameraPos.x += moveDistance * Math.cos(theta) * Math.sin(phi);
			
			cameraPos.z += moveDistance * Math.cos(phi);
			
			System.out.println(cameraPos);
			System.out.println();*/
		}
		
		if(backwardHandler.isHeld()){
			distance -= 0.001f * delta;
			
			if (distance < 0) {
				distance = 0;
			}
			
			/*
			//KEY_S Usually
			
			cameraPos.y -= moveDistance * Math.sin(theta) * Math.sin(phi);
			
			cameraPos.x -= moveDistance * Math.cos(theta) * Math.sin(phi);
			
			cameraPos.z -= moveDistance * Math.cos(phi);
			*/
		}
	}
	
	public float getScale() {
		return distance;
	}

	/**
	 * Resets the camera to its default position and rotation.
	 */
	@Override
	public void resetCamera() {
		setPosition(new Vector3f(0, 0, 0));
		setRotation(DEFAULT_THETA, DEFAULT_PHI);
	}

	public void setForwardHandler(InputLatch forwardHandler) {
		this.forwardHandler = forwardHandler;
	}

	public void setBackwardHandler(InputLatch backwardHandler) {
		this.backwardHandler = backwardHandler;
	}

	public void setLeftHandler(InputLatch leftHandler) {
		this.leftHandler = leftHandler;
	}

	public void setRightHandler(InputLatch rightHandler) {
		this.rightHandler = rightHandler;
	}

	public void setUpHandler(InputLatch upHandler) {
		this.upHandler = upHandler;
	}

	public void setDownHandler(InputLatch downHandler) {
		this.downHandler = downHandler;
	}

	public void setSpeedUpHandler(InputLatch speedUpHandler) {
		this.speedUpHandler = speedUpHandler;
	}

	public void setSlowDownHandler(InputLatch slowDownHandler) {
		this.slowDownHandler = slowDownHandler;
	}
}
