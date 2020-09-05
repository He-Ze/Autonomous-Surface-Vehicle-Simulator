package blindmystics.util.input.mouse;

public class InputStatus {

	public static boolean isButtonDown(ButtonStatus status) {
		return ((status == ButtonStatus.JUST_PRESSED) || (status == ButtonStatus.DOWN));
	}

	public static boolean isButtonUp(ButtonStatus status) {
		return ((status == ButtonStatus.JUST_RELEASED) || (status == ButtonStatus.UP));
	}
}
