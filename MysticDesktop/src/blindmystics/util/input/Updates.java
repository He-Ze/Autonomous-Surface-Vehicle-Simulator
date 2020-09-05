package blindmystics.util.input;

import blindmystics.input.CurrentInput;

public interface Updates {
	/**
	 * Updates the object with the current frame.
	 * @param input - The user input since last frame.
	 * @param delta - The time (in milliseconds) since last frame. (capped at 100ms)
	 */
	void update(CurrentInput input, float delta);
}
