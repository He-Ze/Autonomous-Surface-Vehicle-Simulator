package renderables;

public interface AnimatableObject{
	public abstract void setFrame(int frame);
	
	abstract void updateFrame();
	
	public abstract void setDelays(short[] delays);
}
