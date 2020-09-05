package blindmystics.input;

import blindmystics.util.input.mouse.UpdateStatus;
import org.lwjgl.input.Mouse;
import blindmystics.util.input.keyboard.HandleKeyboard;
import blindmystics.util.input.mouse.ButtonStatus;
import blindmystics.util.input.mouse.ScrollwheelStatus;
import renderables.r2D.Renderable2D;

import javax.vecmath.Vector2f;

public class CurrentInput {
	//Mouse
	public ButtonStatus leftMouse = ButtonStatus.UP;
	public ButtonStatus middleMouse = ButtonStatus.UP;
	public ButtonStatus rightMouse = ButtonStatus.UP;
	
	//Mouse position
	public int mXpos = 0;
	public int mYpos = 0;
	
	public int prev_MXpos;
	public int prev_MYpos;

	public int mouseWheelRotation;
	public ScrollwheelStatus scrollwheel = ScrollwheelStatus.INERT;

	public float mouseWheelVelocity;
	
	private Renderable2D objectThatReceivedMouse = null;
	
	//Keyboard
	public HandleKeyboard keyboardHandler = new HandleKeyboard();

	private int currentInputFrameNo = 0;

	/**
	 * This variable will be either mapped to 'esc',
	 * or it will be mapped to the back button on andriod phones.
	 */
	private boolean backButtonHitThisFrame = false;

	public boolean consumeBackButtonHit() {
		boolean returnValue = backButtonHitThisFrame;
		backButtonHitThisFrame = false;
		return returnValue;
	}
	
	public CurrentInput(){
		
	}

	public void updateInput(float delta){


		resetMouseReceivedObject(); //Clear the object that handles the mouse.

		prev_MXpos = mXpos;
		prev_MYpos = mYpos;
		mXpos = Mouse.getX();
		mYpos = Mouse.getY();
		//int MYpos = WindowHeight - Mouse.getY();

		//Update each mouse status
		leftMouse = UpdateStatus.handleMouseEvents(0, leftMouse);
		rightMouse = UpdateStatus.handleMouseEvents(1, rightMouse);
		middleMouse = UpdateStatus.handleMouseEvents(2, middleMouse);

		mouseWheelRotation = Mouse.getDWheel();
		if (mouseWheelRotation > 0) {
			scrollwheel = ScrollwheelStatus.SCROLLDOWN;
		} else if (mouseWheelRotation < 0) {
			scrollwheel = ScrollwheelStatus.SCROLLUP;
		} else {
			scrollwheel = ScrollwheelStatus.INERT;
		}
		mouseWheelVelocity = Math.abs(mouseWheelRotation) / 120f;

		//Mouse.setCursorPosition(WIDTH/2, HEIGHT/2);

		keyboardHandler.handleKeyboard();
	}

	/**
	 * if(input.getMouseWheel() < 0) equates to: Scrolled up.
	 * if(input.getMouseWheel() > 0) equates to: Scrolled down.
	 * if(input.getMouseWheel() = 0) equates to: No scroll action since previous frame.
	 * The amount at which the scroll wheel moved = abs(input.getMouseWheel())
	 * @return mouseWheelRotation
	 */
	public int getMouseWheel() {
		return mouseWheelRotation;
	}

	public ScrollwheelStatus getScrollwheel() {
		return scrollwheel;
	}

	public float getMouseWheelVelocity() {
		return mouseWheelVelocity;
	}


	public Vector2f getViewPortSpaceOfMouse(){
		return new Vector2f(mXpos, mYpos);
	}

	public int getMXpos() {
		return mXpos;
	}
	
	public int getMYpos() {
		return mYpos;
	}
	
	public void resetMouseReceivedObject(){
		currentInputFrameNo++;
		objectThatReceivedMouse = null;
	}
	
	public boolean hasComponentReceivedMouseEvent(){
		return (objectThatReceivedMouse != null);
	}

	public void setComponentReceivedMouseEventFlagIfNoneExists(Renderable2D renderableComponent) {
		if (this.objectThatReceivedMouse != null) {
			//An Object has already taken the mouse info.
			return;
		}
		this.objectThatReceivedMouse = renderableComponent;
		this.objectThatReceivedMouse.setFrameReceivedMouse(currentInputFrameNo);
	}

	public boolean receivedMouseEvent(Renderable2D object){
		return (object.getFrameReceivedMouse() == currentInputFrameNo);
	}

	public ButtonStatus getLeftMouse() {
		return leftMouse;
	}

	public static boolean isMouseHeld(ButtonStatus status){
		return ((status == ButtonStatus.JUST_PRESSED) || (status == ButtonStatus.DOWN));
	}
	
	public ButtonStatus getMiddleMouse() {
		return middleMouse;
	}
	
	public ButtonStatus getRightMouse() {
		return rightMouse;
	}

	public HandleKeyboard getKeyboardHandler() {
		return keyboardHandler;
	}


}
