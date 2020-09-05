package renderables.framebuffer;

import blindmystics.util.Compatibility;
import blindmystics.util.GLWrapper;
import gebd.Render;
import org.lwjgl.opengl.*;
import javax.vecmath.Vector2f;
import renderables.texture.TextureHandler;
import renderables.texture.generated.GeneratedTextureInfo;

import java.nio.ByteBuffer;

public class FrameBufferObject {

	private static int currentlyBoundFrameBuffer = 0;

	public static int getCurrentlyBoundFrameBuffer() {
		return currentlyBoundFrameBuffer;
	}

	private int previousBoundFrameBuffer = currentlyBoundFrameBuffer;

	protected int width;
	protected int height;
	
	private int frameBuffer;
	private int textureId;
	private int depthBuffer;
	private int depthTextureId;

	private boolean useDepthTexture = false;
	
	protected GeneratedTextureInfo textureInfo;
	protected GeneratedTextureInfo depthTextureInfo;


	private final int GL_VERSION_MINIMUM_FRAMEBUFFER = Compatibility.MIN_FRAMEBUFFER_COMPATIBLE.getVersionNumber();
	//private final int GL_VERSION_NUMBER = Compatibility.GL_VERSION.getVersionNumber();
	private final int GL_VERSION_NUMBER = Compatibility.GL_VERSION.getVersionNumber();
	private final boolean FRAMEBUFFER_CAPABLE = GL_VERSION_NUMBER >= GL_VERSION_MINIMUM_FRAMEBUFFER;

	public FrameBufferObject(int width, int height){
		this(width, height, false);
	}
	
	public FrameBufferObject(int width, int height, boolean useDepthTexture){
		this.useDepthTexture = useDepthTexture;
		this.width = width;		
		this.height = height;
		initializeTheFrameBuffers();
		this.textureInfo = new GeneratedTextureInfo(textureId, width, height);
		if(useDepthTexture){
			this.depthTextureInfo = new GeneratedTextureInfo(depthTextureId, width, height);
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Vector2f getSize() {
		return new Vector2f(this.width, this.height);
	}
	
	public int getFrameBuffer(){
		return frameBuffer;
	}
	
	public int getTextureId(){
		return textureId;
	}

	public int getDepthTextureId(){
		return depthTextureId;
	}
	
	public GeneratedTextureInfo getTexture(){
		return textureInfo;
	}

	public GeneratedTextureInfo getDepthTextureInfo() {
		return depthTextureInfo;
	}
	
	public int getDepthBuffer(){
		return depthBuffer;
	}
	
	
	public void initializeTheFrameBuffers(){
		initialiseFrameBuffer();
	}
	
	public void bindFrameBuffer(){
		bindFrameBuffer(frameBuffer, width, height);
	}
	
	public void unbindCurrentFrameBuffer(){
		if (FRAMEBUFFER_CAPABLE) {
			GLWrapper.glBindFramebuffer(GLWrapper.GL_FRAMEBUFFER, previousBoundFrameBuffer);
		} else {
			EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, previousBoundFrameBuffer);
		}
		GLWrapper.glViewport(0, 0, Render.getInstance().getWidth(), Render.getInstance().getHeight());
		/*
		 * NOTE - Textures get unloaded at this time also.
		 * The texture handler will still think it is loaded, so
		 * you must tell it that nothing is loaded.
		 */
		TextureHandler.prepareTexture(0);
		currentlyBoundFrameBuffer = previousBoundFrameBuffer;
	}
	
	private void initialiseFrameBuffer() {
		frameBuffer = createFrameBuffer();
		textureId = createTextureAttachment(width, height); //TODO WIDTH & HEIGHT!
		depthBuffer = createDepthBufferAttachment(width, height);
		if (useDepthTexture) {
			depthTextureId = createDepthTextureAttachment(width, height);
		} else {
		}
		unbindCurrentFrameBuffer();
	}
	
	private void bindFrameBuffer(int frameBuffer, int width, int height){
		/*
		 * NOTE - Textures get unloaded at this time also.
		 * The texture handler will still think it is loaded, so
		 * you must tell it that nothing is loaded.
		 */
		TextureHandler.prepareTexture(0);
		if (FRAMEBUFFER_CAPABLE) {
			GLWrapper.glBindFramebuffer(GLWrapper.GL_FRAMEBUFFER, frameBuffer);
		} else {
			EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, frameBuffer);
		}
		GLWrapper.glViewport(0, 0, width, height);
		previousBoundFrameBuffer = currentlyBoundFrameBuffer;
		currentlyBoundFrameBuffer = frameBuffer;
	}

	private int createFrameBuffer(){
		int frameBuffer;
		Render.getInstance().exitOnGLError("createFrameBuffer");
		if (FRAMEBUFFER_CAPABLE) {
			frameBuffer = GLWrapper.glGenFramebuffers();
			GLWrapper.glBindFramebuffer(GLWrapper.GL_FRAMEBUFFER, frameBuffer);
			GLWrapper.glDrawBuffer(GLWrapper.GL_COLOR_ATTACHMENT0);
		} else {
			frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();
			EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, frameBuffer);
			GLWrapper.glDrawBuffer(EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT);
		}
		return frameBuffer;
	}
	
	private int createTextureAttachment(int width, int height){
		int texture = GLWrapper.glGenTextures();
		GLWrapper.glBindTexture(GLWrapper.GL_TEXTURE_2D, texture);
		GLWrapper.glTexImage2D(GLWrapper.GL_TEXTURE_2D, 0, GLWrapper.GL_RGBA, width, height, 0, GLWrapper.GL_RGBA, GLWrapper.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_MAG_FILTER, GLWrapper.GL_LINEAR);
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_MIN_FILTER, GLWrapper.GL_LINEAR);
		if (FRAMEBUFFER_CAPABLE) {
			GLWrapper.glFramebufferTexture(GLWrapper.GL_FRAMEBUFFER, GLWrapper.GL_COLOR_ATTACHMENT0, texture, 0);
		} else {
			EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GLWrapper.GL_TEXTURE_2D, texture, 0);
		}
		return texture;
	}
	
	private int createDepthTextureAttachment(int width, int height){
		int texture = GLWrapper.glGenTextures();
		GLWrapper.glBindTexture(GLWrapper.GL_TEXTURE_2D, texture);
		GLWrapper.glTexImage2D(GLWrapper.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 
				0, GLWrapper.GL_DEPTH_COMPONENT, GLWrapper.GL_FLOAT, (ByteBuffer) null);
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_MAG_FILTER, GLWrapper.GL_LINEAR);
		GLWrapper.glTexParameteri(GLWrapper.GL_TEXTURE_2D, GLWrapper.GL_TEXTURE_MIN_FILTER, GLWrapper.GL_LINEAR);

		if (FRAMEBUFFER_CAPABLE) {
			GLWrapper.glFramebufferTexture(GLWrapper.GL_FRAMEBUFFER, GLWrapper.GL_DEPTH_ATTACHMENT, texture, 0);
		} else {
			EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, GLWrapper.GL_TEXTURE_2D, texture, 0);
		}
		return texture;
	}
	
	private int createDepthBufferAttachment(int width, int height){
		int depthBuffer;
		if (FRAMEBUFFER_CAPABLE) {
			depthBuffer = GLWrapper.glGenRenderbuffers();
			GLWrapper.glBindRenderbuffer(GLWrapper.GL_RENDERBUFFER, depthBuffer);
			GLWrapper.glRenderbufferStorage(GLWrapper.GL_RENDERBUFFER, GLWrapper.GL_DEPTH_COMPONENT, width, height);
			GLWrapper.glFramebufferRenderbuffer(GLWrapper.GL_FRAMEBUFFER, GLWrapper.GL_DEPTH_ATTACHMENT, GLWrapper.GL_RENDERBUFFER, depthBuffer);
		} else {
			depthBuffer = EXTFramebufferObject.glGenRenderbuffersEXT();
			EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthBuffer);
			EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GLWrapper.GL_DEPTH_COMPONENT, width, height);
			EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthBuffer);
		}
		return depthBuffer;
	}
	
	public void cleanUp(){
		if (FRAMEBUFFER_CAPABLE) {
			GLWrapper.glDeleteFramebuffers(frameBuffer);
			GLWrapper.glDeleteTextures(textureId);
			if (useDepthTexture) {
				GLWrapper.glDeleteTextures(depthTextureId);
			} else {
				GLWrapper.glDeleteRenderbuffers(depthBuffer);
			}
		} else {
			EXTFramebufferObject.glDeleteFramebuffersEXT(frameBuffer);
			GLWrapper.glDeleteTextures(textureId);
			if (useDepthTexture) {
				GLWrapper.glDeleteTextures(depthTextureId);
			} else {
				EXTFramebufferObject.glDeleteRenderbuffersEXT(depthBuffer);
			}
		}
	}

	
	
}
