package gebd.camera.implementation;

import blindmystics.input.CurrentInput;
import blindmystics.input.KeyboardInputLatchGenerator;
import blindmystics.util.input.InputLatch;
import blindmystics.util.input.keyboard.HandleKeyboard;
import blindmystics.util.input.mouse.ButtonStatus;

import gebd.Render;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import gebd.camera.Camera;
import org.lwjgl.input.Mouse;

public class NoClipCamera extends Camera {
	
	public NoClipCamera(){
		resetCamera();
	}
	
	public NoClipCamera(Vector3f cameraPos, float theta, float phi){
		setPosition(cameraPos);
		setRotation(theta, phi);
	}

	private InputLatch forwardHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_W);
	private InputLatch backwardHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_S);
	private InputLatch leftHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_A);
	private InputLatch rightHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_D);
	private InputLatch upHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_Q);
	private InputLatch downHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_E);
	private InputLatch speedUpHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_LSHIFT);
	private InputLatch slowDownHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_LCONTROL);

	//private InputLatch tempRollLeftHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_Q);
	//private InputLatch tempRollRightHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_E);

	private Vector2f mouseHeldStartPositionLidar = new Vector2f(0, 0);

	@Override
	public void updateCamera(float delta, CurrentInput input) {

		//setTheta((float) (Math.PI / 2.0));
		//incrementTheta(delta * 0.0001f);
		//incrementPhi(delta * 0.0001f);
		//setPhi(0.0f);
		//setRoll(0.0f);
		//setPosition(0f, 1f, 0f);
		//incrementRoll(delta * 0.001f);

		while (getTheta() > (Math.PI)) {
			incrementTheta((float) (-2 * Math.PI));
		}

		while (getTheta() < (- Math.PI)) {
			incrementTheta((float) (2 * Math.PI));
		}
		
		//leftMouse
		//middleMouse
		//rightMouse
		
		if((input.rightMouse == ButtonStatus.DOWN) || (input.rightMouse == ButtonStatus.JUST_PRESSED)){
			float mouseDiffX = input.prev_MXpos - input.mXpos;
			float mouseDiffY = input.prev_MYpos - input.mYpos;

			incrementTheta((float) (Math.toRadians(0.1f) * mouseDiffX));
			incrementPhi((float) (Math.toRadians(-0.1f) * mouseDiffY));

			if(getPhi() > (Math.PI / 2.0)){
				setPhi((float) (Math.PI / 2.0));
			} else if(getPhi() < (-Math.PI / 2.0)){
				setPhi((float) -(Math.PI / 2.0));
			}

			if (input.rightMouse == ButtonStatus.JUST_PRESSED) {
				mouseHeldStartPositionLidar = new Vector2f(input.getMXpos(), input.getMYpos());
				if (!Mouse.isGrabbed()) {
					Mouse.setGrabbed(true);
				}
			} else {
				int setMousePosX = (int) (Render.getWidth() / 2.0);
				int setMousePosY = (int) (Render.getHeight() / 2.0);
				Mouse.setCursorPosition(setMousePosX, setMousePosY);
				input.mXpos = setMousePosX;
				input.mYpos = setMousePosY;
			}
		} else {
			if (Mouse.isGrabbed()) {
				Mouse.setGrabbed(false);
				//Don't Reset mouse position... I didn't like it :P
//				input.mXpos = (int) mouseHeldStartPositionLidar.x;
//				input.mYpos = (int) mouseHeldStartPositionLidar.y;
//				Mouse.setCursorPosition((int) mouseHeldStartPositionLidar.x, (int) mouseHeldStartPositionLidar.y);
			}
		}



		/*
		//Roll test.
		if (HandleKeyboard.qHandler.isKeyDown()) {
			roll -= delta / 1000f;
		}
		if (HandleKeyboard.eHandler.isKeyDown()) {
			roll += delta / 1000f;
		}
		*/

		if(input.keyboardHandler.getAmbiguousKeyHandler() != null){
			return;
		}

		float extraSpeed = 5;
		if(speedUpHandler.isHeld()){
			//extraSpeed = 4;
			extraSpeed = 15;
		} else if (slowDownHandler.isHeld()) {
			extraSpeed = 1f;
		}
		
		float moveDistance = 0.001f * delta * extraSpeed;

//		if (tempRollLeftHandler.isHeld()) {
//			incrementRoll(moveDistance);
//		}
//		if (tempRollRightHandler.isHeld()) {
//			incrementRoll(-moveDistance);
//		}
		
		if(leftHandler.isHeld()){
			float tempTheta = (float) (getTheta() + (Math.PI/2));
			position.x += Math.cos(tempTheta) * moveDistance;
			position.z -= Math.sin(tempTheta) * moveDistance;
			
		}
		
		if(rightHandler.isHeld()){
			float tempTheta = (float) (getTheta() - (Math.PI/2));
			position.x += Math.cos(tempTheta) * moveDistance;
			position.z -= Math.sin(tempTheta) * moveDistance;
			
		}
		
		if(upHandler.isHeld()){
			position.y += (0.001f * delta);
		}
		
		if(downHandler.isHeld()){
			position.y -= + (0.001f * delta);
		}

		if (forwardHandler.isHeld()) {

			//KEY_W Usually

			position.z -= moveDistance * Math.sin(getTheta()) * Math.cos(getPhi());

			position.x += moveDistance * Math.cos(getTheta()) * Math.cos(getPhi());

			position.y += moveDistance * Math.sin(getPhi());
		}
		
		if(backwardHandler.isHeld()){
			//KEY_S Usually

			position.z += moveDistance * Math.sin(getTheta()) * Math.cos(getPhi());

			position.x -= moveDistance * Math.cos(getTheta()) * Math.cos(getPhi());

			position.y -= moveDistance * Math.sin(getPhi());
			
		}
	}

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
