package renderables.r2D.simple;

import blindmystics.input.CurrentInput;
import blindmystics.util.FileReader;
import gebd.Render;
import javax.vecmath.Vector2f;

import blindmystics.util.input.mouse.ButtonStatus;
import renderables.texture.TextureCoord;

/**
 * A button is an animated quad, that doesn't animate by its self But changes frame based on the
 * mouse input.
 * 
 * @author Peter Smith 43180543
 */
public class Button extends AnimatedQuad {

	/**
	 * The status of the button.
	 * 
	 * @author Peter Smith 43180543
	 */
	public enum ButtonState {

		BUTTON_UP(0), BUTTON_JUST_RELEASED(0), BUTTON_HOVER(1), BUTTON_JUST_DOWN(2), BUTTON_DOWN(
				2), BUTTON_DISABLED(3);

		// The frame that a button state should display.
		private int frameNo;

		/**
		 * @param frameNo - The frame that a button state should display.
		 */
		ButtonState(int frameNo) {
			this.frameNo = frameNo;
		}

		/**
		 * @return the corrseponding frame number.
		 */
		public int getFrameNo() {
			return this.frameNo;
		}
	}

	// Whether or not this button is enabled.
	protected boolean enabled = true;
	// The current status of the button.
	protected ButtonState status = ButtonState.BUTTON_UP;

	// The texture coordinates that are used to split up a button image into four texture.
	// Used as states on the sprite sheet.
	public static final TextureCoord coord1 = new TextureCoord(0.0f, 0.0f, 0.5f, 0.5f);
	public static final TextureCoord coord2 = new TextureCoord(0.5f, 0.0f, 1.0f, 0.5f);
	public static final TextureCoord coord3 = new TextureCoord(0.0f, 0.5f, 0.5f, 1.0f);
	public static final TextureCoord coord4 = new TextureCoord(0.5f, 0.5f, 1.0f, 1.0f);

	//A useful default texture.
	public static final String BUTONIFY_TEXTURE = FileReader.asSharedFile("test_images/Buttonify.png");

	// The default split for the button.
	public static final TextureCoord[] DEFUALT_UV_LAYOUT = {coord1, coord2, coord3, coord4};

	protected int frameButtonPressed = -1;
	protected int frameButtonReleased = -1;

	/**
	 * The default constructor for the button.
	 * 
	 * @param buttonTexturePath
	 * @param screenPosition
	 * @param size
	 * @param rotation
	 */
	public Button(String buttonTexturePath, Vector2f screenPosition, Vector2f size, float rotation) {
		super(buttonTexturePath, DEFUALT_UV_LAYOUT, screenPosition, size, rotation);
		//System.out.println("Button.");
		this.setAnimating(false);
	}

	/**
	 * On update, if the mouse is within the button, determine whether the state of the button should
	 * change.
	 */
	@Override
	public void update(CurrentInput input, float delta) {
		super.update(input, delta);

		//Ensure that the state of the button is always at the latest.
		setEnabled(isEnabled());

		if(this.status == ButtonState.BUTTON_JUST_RELEASED){
			this.status = ButtonState.BUTTON_UP;
		}

		if (this.status == ButtonState.BUTTON_DISABLED) {
			// Do nothing, ignore button!
			return;
		}
		ButtonStatus leftMouse = input.leftMouse;

		if (isWithinQuadBounds(input.getMXpos(), input.getMYpos(), this.absolutePosition, this.size)) {
			// Mouse is over the button.
			if (leftMouse == ButtonStatus.UP) {
				if (input.receivedMouseEvent(this)) {
					// Hover.
					this.status = ButtonState.BUTTON_HOVER;
				} else {
					// Something is above the button.
					this.status = ButtonState.BUTTON_UP;
				}
			} else if (leftMouse == ButtonStatus.JUST_PRESSED) {
				if (input.receivedMouseEvent(this)) {
					// If the mouse has just clicked on the button.
					this.status = ButtonState.BUTTON_JUST_DOWN;
					frameButtonPressed = Render.getCurrentFrame();
				}
			} else if (leftMouse == ButtonStatus.DOWN) {
				if (this.status == ButtonState.BUTTON_JUST_DOWN) {
					if (input.receivedMouseEvent(this)) {
						// If the mouse has held over the button.
						this.status = ButtonState.BUTTON_DOWN;
					}
				}
			} else if (leftMouse == ButtonStatus.JUST_RELEASED) {
				if ((this.status == ButtonState.BUTTON_JUST_DOWN)
						|| (this.status == ButtonState.BUTTON_DOWN)) {
					// The button was just released.
					this.status = ButtonState.BUTTON_JUST_RELEASED;
					frameButtonReleased = Render.getCurrentFrame();
					onRelease();
				}
			}
		} else {
			if ((leftMouse == ButtonStatus.UP) || (leftMouse == ButtonStatus.JUST_RELEASED)
					|| (leftMouse == ButtonStatus.JUST_PRESSED)) {
				// The button is not being pressed at the moment.
				this.status = ButtonState.BUTTON_UP;
			} else if ((leftMouse == ButtonStatus.DOWN)) {
				// The mouse is not affecting the button.
			}
		}
		
		// Change the button texture based on button state.
		updateAnimationBasedOnStatus();
	}

	/**
	 * Currently doesn't do anything, but can be overridden.
	 */
	protected void onRelease(){

	}

	/**
	 * @return whether the buitton is enabled.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Whether or not to disable the button.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		if (enabled == this.enabled) {
			return;
		}
		this.enabled = enabled;
		if (enabled) {
			if (this.status == ButtonState.BUTTON_DISABLED) {
				// Return to Default Up state.
				this.status = ButtonState.BUTTON_UP;
			}
		} else {
			// Button is now disabled.
			this.status = ButtonState.BUTTON_DISABLED;
		}
		// Change the button texture based on button state.
		updateAnimationBasedOnStatus();
	}

	/**
	 * Change the button texture based on button state.
	 */
	private void updateAnimationBasedOnStatus() {
		setFrame(this.status.getFrameNo());
	}

	/**
	 * @return whether the button was just clicked.
	 */
	public boolean justClicked() {
		return ((this.status == ButtonState.BUTTON_JUST_RELEASED) && (frameButtonReleased == Render.getCurrentFrame()));
	}

	/**
	 * @return whether the button was just pressed.
	 */
	public boolean justPressed() {
		return ((this.status == ButtonState.BUTTON_JUST_DOWN) && (frameButtonPressed == Render.getCurrentFrame()));
	}

	/**
	 * @return whether the button was just released.
	 */
	public boolean justReleased() {
		return justClicked();
	}

	/**
	 * @return The current button state.
	 */
	public ButtonState getButtonState() {
		return this.status;
	}

	/**
	 * @return whether the button is currently held.
	 */
	public boolean isHeld() {
		return ((this.status == ButtonState.BUTTON_DOWN)
				|| (this.status == ButtonState.BUTTON_JUST_DOWN));
	}
}
