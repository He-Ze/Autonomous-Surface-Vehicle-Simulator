package renderables.r2D.simple;

import blindmystics.input.CurrentInput;
import javax.vecmath.Vector2f;

import gebd.Render;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import renderables.texture.SpriteMapTextureInfo;
import renderables.texture.TextureCoord;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;

/**
 * The class is used to handle Quads with animating textures.
 * 
 * @author Peter Smith 43180543
 */
public class AnimatedQuad extends SimpleQuad {

	// The time (in milliseconds) between each frame
	protected short[] animationDelays;
	// The frames of the animation.
	protected short[] animationFrames;

	// The current position in the animation.
	protected short animationPosition = 0;
	// The current time elapsed counter.
	protected float delayCount = 0;

	// The List of animation textures.
	private TextureInfo[] animationTextures;

	//The spritemap holding the texture information
	private SpriteMapTextureInfo animationTextureMap;
	
	//The UV coords for generating the sprite map.
	private TextureCoord[] uvCoords;
	
	// Whether or not it is currently animating.
	protected boolean isAnimating = true;

	/**
	 * The default constructor. It will split the texture into smaller textures And will load the
	 * correct smaller texture for each render.
	 * 
	 * @param texturePath
	 * @param uvCoords
	 * @param screenPosition
	 * @param size
	 * @param rotation
	 */
	public AnimatedQuad(String texturePath, TextureCoord[] uvCoords, Vector2f screenPosition,
			Vector2f size, float rotation) {
		super(screenPosition, size, rotation);
		this.uvCoords = uvCoords;
		this.texturePath = texturePath;
		this.stagesToLoad = new LoadStage[] {
				//LoadStage.LOAD_DATA_FROM_FILE,
				//LoadStage.HANDLE_RAW_DATA,
				LoadStage.LOAD_DEPENDENCIES,
		};
		this.animationFrames = new short[uvCoords.length];
		this.animationDelays = new short[uvCoords.length];
		for (int i = 0; i < uvCoords.length; i++) {
			this.animationFrames[i] = (short) i;
			// The default animation timing.
			this.animationDelays[i] = (short) 100;
		}
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		// Split textures into many smaller ones (Sprite map)
		//this.animationTextures = TextureHandler.generateSpriteMapTextures(texturePath, uvCoords);
		//SpriteMapTextureInfo
		animationTextureMap = handler.newDependancy(new SpriteMapTextureInfo(texturePath, uvCoords));
	}
	
	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		// Texture is now loaded.
		this.shader2D = Render.instance.getDefault2DShader();
		//Setup the textures.
		animationTextures = animationTextureMap.getTextures();
	}

	/**
	 * Prepares the correct texture to render depending on the current frame.
	 */
	@Override
	protected void prepareTexture() {
		TextureHandler.prepareTexture(this.animationTextures[this.animationPosition]);
	}

	/**
	 * Updates the delayCount and determines whether the frame should change or not.
	 */
	@Override
	public void update(CurrentInput input, float delta) {
		super.update(input, delta);
		if (this.isAnimating) {
			animateBy(delta);
		}
	}

	/**
	 * Updates the animation based on a given delta.
	 * @param delta
     */
	public void animateBy(float delta) {
		this.delayCount += delta;
		if (this.delayCount > this.animationDelays[this.animationPosition]) {
			// Time since last frame > time to spend on a frame,
			// Move to the next frame.
			this.delayCount -= this.animationDelays[this.animationPosition];
			moveToNextFrame();
		}
	}

	/**
	 * @param isAnimating - Whether or not to play the animation.
	 */
	public void setAnimating(boolean isAnimating) {
		this.isAnimating = isAnimating;
	}

	/**
	 * Move the animation to the next frame.
	 */
	public void moveToNextFrame() {
		this.animationPosition++;
		if (this.animationPosition >= this.animationFrames.length) {
			// Reached the end of the animation, Loop the animation.
			this.animationPosition = 0;
		}
	}

	/**
	 * @return - The number of frames in the animation.
	 */
	public int getNumberOfFrames() {
		return this.animationFrames.length;
	}

	/**
	 * Jump the animation to a particular frame.
	 * 
	 * @param frameNo
	 */
	public void setFrame(int frameNo) {
		this.animationPosition = (short) (frameNo % this.animationFrames.length);
		this.delayCount = 0;
	}


	public SpriteMapTextureInfo getAnimationTextureMap() {
		return animationTextureMap;
	}

}
