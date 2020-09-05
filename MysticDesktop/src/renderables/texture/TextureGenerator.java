package renderables.texture;

import blindmystics.util.GLWrapper;
import gebd.Render;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import renderables.TextureInfoGroup;

import java.nio.ByteBuffer;

public class TextureGenerator {


	/*
	 * Warning - MUST be called from within the Render Thread.
	 */
	public static int GL_GenerateTexture(int textureUnit, int tWidth, int tHeight, ByteBuffer buf, int minFilter, int maxFilter){
		// Create a new texture object in memory and bind it

		int texId = GLWrapper.glGenTextures();
		GLWrapper.glActiveTexture(textureUnit);
		GLWrapper.glBindTexture(GLWrapper.GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		GLWrapper.glPixelStorei(GLWrapper.GL_UNPACK_ALIGNMENT, 1);

		// Upload the texture data and generate mip maps (for scaling)
		GLWrapper.glTexImage2D(GLWrapper.GL_TEXTURE_2D, 0, GLWrapper.GL_RGBA, tWidth, tHeight, 0,
				GLWrapper.GL_RGBA, GLWrapper.GL_UNSIGNED_BYTE, buf);
		GLWrapper.glGenerateMipmap(GLWrapper.GL_TEXTURE_2D);

		// Setup the ST coordinate system
		// Switching from Wrap to clamp
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_WRAP_S, GLWrapper.GL_CLAMP);
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_WRAP_T, GLWrapper.GL_CLAMP);

		// Setup what to do when the texture has to be scaled
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_MAG_FILTER, maxFilter);
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_MIN_FILTER, minFilter);


		Render.instance.exitOnGLError("generate PNGTexture");

		return texId;
	}



	public static int GL_GenerateTextureFromPNG(int textureUnit, TextureInfo rawTextureData, int tiling){

		// Create a new texture object in memory and bind it
		int texId = GLWrapper.glGenTextures();
		GLWrapper.glActiveTexture(textureUnit);
		GLWrapper.glBindTexture(GLWrapper.GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		GLWrapper.glPixelStorei(GLWrapper.GL_UNPACK_ALIGNMENT, 1);

		// Upload the texture data and generate mip maps (for scaling)
		GLWrapper.glTexImage2D(GLWrapper.GL_TEXTURE_2D, 0, GLWrapper.GL_RGBA, rawTextureData.get_tWidth(), rawTextureData.get_tHeight(), 0,
				GLWrapper.GL_RGBA, GLWrapper.GL_UNSIGNED_BYTE, rawTextureData.getBuf());
		GLWrapper.glGenerateMipmap(GLWrapper.GL_TEXTURE_2D);

		// Setup the ST coordinate system
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_WRAP_S, tiling);
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_WRAP_T, tiling);

		// Setup what to do when the texture has to be scaled
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_MAG_FILTER, rawTextureData.getMaxTextureFilter());
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_MIN_FILTER, rawTextureData.getMinTextureFilter()); //TODO consider.

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f);

		Render.instance.exitOnGLError("generate PNGTexture");

		return texId;
	}

	//TODO duplicate function of above
	public static int GL_GenerateTextureFromPNG(int textureUnit, TextureInfoGroup rawTextureData){

		// Create a new texture object in memory and bind it
		int texId = GLWrapper.glGenTextures();
		GLWrapper.glActiveTexture(textureUnit);
		GLWrapper.glBindTexture(GLWrapper.GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		GLWrapper.glPixelStorei(GLWrapper.GL_UNPACK_ALIGNMENT, 1);

		// Upload the texture data and generate mip maps (for scaling)
		GLWrapper.glTexImage2D(GLWrapper.GL_TEXTURE_2D, 0, GLWrapper.GL_RGBA, rawTextureData.totalWidth, rawTextureData.totalHeight, 0,
				GLWrapper.GL_RGBA, GLWrapper.GL_UNSIGNED_BYTE, rawTextureData.buf);
		GLWrapper.glGenerateMipmap(GL11.GL_TEXTURE_2D);

		// Setup the ST coordinate system
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_WRAP_S, GLWrapper.GL_CLAMP);
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_WRAP_T, GLWrapper.GL_CLAMP);

		// Setup what to do when the texture has to be scaled
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_MAG_FILTER, GLWrapper.GL_LINEAR);
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_MIN_FILTER, GLWrapper.GL_LINEAR_MIPMAP_LINEAR);

		Render.instance.exitOnGLError("generate PNGTexture");

		return texId;
	}

}
