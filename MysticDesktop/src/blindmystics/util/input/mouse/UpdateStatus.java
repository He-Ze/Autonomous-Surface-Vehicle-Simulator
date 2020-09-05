package blindmystics.util.input.mouse;

import org.lwjgl.input.Mouse;

public class UpdateStatus {
	public static ButtonStatus handleMouseEvents(int mouseButton, ButtonStatus currentStatus){
		//UP, DOWN, JUST_PRESSED, JUST_RELEASED;
		ButtonStatus nextStatus = currentStatus;
		switch(currentStatus){
		case JUST_RELEASED:
			nextStatus = ButtonStatus.UP;
		case UP:
			if(Mouse.isButtonDown(mouseButton)){
				nextStatus = ButtonStatus.JUST_PRESSED;
			}
			break;
		case JUST_PRESSED:
			nextStatus = ButtonStatus.DOWN;
		case DOWN:
			if(!Mouse.isButtonDown(mouseButton)){
				nextStatus = ButtonStatus.JUST_RELEASED;
			}
			break;
		}
		return nextStatus;
	}
}
