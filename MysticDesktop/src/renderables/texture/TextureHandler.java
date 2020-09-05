package renderables.texture;

import blindmystics.util.GLWrapper;
import loader.LoadedObjectHandler;

public class TextureHandler {

	public static final int DEFAULT_TEXTURE_UNIT = GLWrapper.GL_TEXTURE0;
	
	protected static int loadedTexId = -1;



	public static void deletePNGTexture(TextureInfo texture){
		texture.decrementReferences();
	}
	
	/**
	 * Clears all textures from openGl memory
	 */
	public static void destroyAllTextures(){
		for(LoadedObjectHandler<TextureInfo> texture : TextureInfo.loadedTextures.values()){
			GLWrapper.glDeleteTextures(texture.getAttachedObject().getTextureId());
		}
	}

	public static final void prepareTexture(int textureType, int textureId){
		if(loadedTexId != textureId){
			loadedTexId = textureId;
			GLWrapper.glBindTexture(textureType, textureId);
		}
	}
	
	public static final void prepareTexture(int textureId){
		if(loadedTexId != textureId){
			loadedTexId = textureId;
			GLWrapper.glBindTexture(GLWrapper.GL_TEXTURE_2D, textureId);
		}
	}
	
	public static final void prepareTexture(TextureInfo texture){
		if(loadedTexId != texture.getTextureId()){
			loadedTexId = texture.getTextureId();
			GLWrapper.glBindTexture(GLWrapper.GL_TEXTURE_2D, texture.getTextureId());
		}
	}
	
	public static final void deleteTexture(TextureInfo texture){
		if(loadedTexId != texture.getTextureId()){
			GLWrapper.glDeleteTextures(texture.getTextureId());
		}
	}

}
