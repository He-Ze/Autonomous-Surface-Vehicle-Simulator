package blindmystics.util.input.keyboard;

public interface AmbiguousKeyHandler {
	public boolean controlsAllKeyEvents();
	public void handleKey(boolean keyDown, int keyCode, HandleKeyboard handler);

	/**
	 * Called when the object is selected.
	 */
	public void onSelect();

	/**
	 * Called when the object is deselected.
	 */
	public void onDeselect();
}
