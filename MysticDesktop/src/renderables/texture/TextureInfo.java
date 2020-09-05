package renderables.texture;

import blindmystics.util.FileReader;
import blindmystics.util.GLWrapper;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import loader.AsyncLoader;
import loader.LoadedObjectAbstract;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import renderables.texture.generated.SolidFillTextureHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class TextureInfo extends LoadedObjectAbstract {

	public static final String DEFAULT_TEXTURE = FileReader.asSharedFile("shared/textures/default_texture.png");

	protected String filePath;
	protected int textureID;
	protected int numOfReferences = 1;
	protected boolean hasAlpha;
	protected int textureUnit = TextureHandler.DEFAULT_TEXTURE_UNIT;
	protected boolean resortedToMissingTexture = false;


	/**
	 * Available MAX filters:
		GL_NEAREST
		GL_LINEAR
	 *
	 */
	public static final int DEFAULT_MAX_TEXTURE_FILTER = GLWrapper.GL_LINEAR;
	protected int maxTextureFilter = DEFAULT_MAX_TEXTURE_FILTER;




	/**
	 * Available MIN filters:
		GL_NEAREST
		GL_LINEAR
		GL_NEAREST_MIPMAP_NEAREST
		GL_NEAREST_MIPMAP_LINEAR
		GL_LINEAR_MIPMAP_NEAREST
		GL_LINEAR_MIPMAP_LINEAR
	 *
	 */
	public static final int DEFAULT_MIN_TEXTURE_FILTER = GLWrapper.GL_LINEAR_MIPMAP_LINEAR;
	protected int minTextureFilter = DEFAULT_MIN_TEXTURE_FILTER;

	public static final int DEFAULT_TILING_STYLE = GLWrapper.GL_REPEAT;
	protected int tilingStyle = DEFAULT_TILING_STYLE;

	
	/*
	 * TODO:
	 * Fix the references thing - I believe that it is not properly implemented!
	 */

	protected ByteBuffer buf;
	protected int tWidth = 0;
	protected int tHeight = 0;

	private LoadedObjectHandler.LoadStatus currentStatus;



	protected static HashMap<String, LoadedObjectHandler<TextureInfo>> loadedTextures = new HashMap<String, LoadedObjectHandler<TextureInfo>>();

	public static TextureInfo loadTexture(String texturePath){
		return queueLoadOfPNGTexture(texturePath, AsyncLoader.ASYNC_LOADER);
	}

	public static TextureInfo queueLoadOfPNGTexture(String pngFilename, int maxFilter, int minFilter, int tilingStyle, LoadedObjectHandler<?> waitingJob){
		int loadPosition = waitingJob.getDependancyNo();

		String textureKey = pngFilename + "" + maxFilter + "" + minFilter + "" + tilingStyle;

		LoadedObjectHandler<TextureInfo> texture = loadedTextures.get(textureKey);

		if(texture != null){
			texture.getAttachedObject().incrememntReferences();
			if(texture.isLoaded()){
				//Texture already loaded!
				//System.out.println("Texture already loaded: " + filename);
				waitingJob.notifyLoadedDependancy(loadPosition, texture);
			} else {
				//Texture in the process of being loaded!
				//System.out.println("Texture in the process of being loaded: " + filename);
				texture.addWaiting(waitingJob, loadPosition);
			}
		} else {
			//Texture doesn't exist at the moment, start loading it!
			//System.out.println("Texture doesn't exist at the moment, start loading it: " + filename);
			texture = new LoadedObjectHandler<TextureInfo>(new TextureInfo(pngFilename, maxFilter, minFilter, tilingStyle));
			texture.addWaiting(waitingJob, loadPosition);
			//System.out.println("textureKey = " + textureKey);
			texture.handleLoad();
			loadedTextures.put(textureKey, texture);
		}
		return texture.getAttachedObject();
	}

	public static TextureInfo queueLoadOfPNGTexture(String filename, LoadedObjectHandler<?> waitingJob){
		return queueLoadOfPNGTexture(filename, DEFAULT_MAX_TEXTURE_FILTER, DEFAULT_MIN_TEXTURE_FILTER, DEFAULT_TILING_STYLE, waitingJob);
	}


	@Override
	public LoadStage[] stagesToPerform(){
		return new LoadStage[] {
				LoadStage.LOAD_DATA_FROM_FILE,
//				LoadStage.HANDLE_RAW_DATA,
//				LoadStage.LOAD_DEPENDENCIES,
		};
	}

	@Override
	public LoadedObjectHandler.LoadStatus getLoadStatus() {
		return currentStatus;
	}

	@Override
	public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {
		this.currentStatus = newLoadStatus;
	}

	protected TextureInfo(){

	}

	private TextureInfo(String path, int maxTextureFilter, int minTextureFilter, int tilingStyle){
		this(path);
		this.tilingStyle = tilingStyle;
		this.maxTextureFilter = maxTextureFilter;
		this.minTextureFilter = minTextureFilter;
	}

	protected TextureInfo(int texID){
		this.textureID = texID;
		setLoadStatus(LoadedObjectHandler.LoadStatus.COMPLETE_LOAD);
	}

	protected TextureInfo(String path){
		this.filePath = path;
		this.textureID = SolidFillTextureHandler.getSolidFillTexture().getTextureId();
	}

	public static TextureInfo generateNewTexture(int tWidth, int tHeight, ByteBuffer buf){
		return generateNewTexture(TextureHandler.DEFAULT_TEXTURE_UNIT, tWidth, tHeight, buf, DEFAULT_MIN_TEXTURE_FILTER, DEFAULT_MAX_TEXTURE_FILTER);
	}

	public static TextureInfo generateNewTexture(int textureUnit, int tWidth, int tHeight, ByteBuffer buf, int minFilter, int maxFilter){
		String uniqueId = "" + Math.random();
		return generateNewTexture(uniqueId, textureUnit, tWidth, tHeight, buf, minFilter, maxFilter);
	}

	public static TextureInfo generateNewTexture(String uniqueId, int textureUnit, int tWidth, int tHeight, ByteBuffer buf, int minFilter, int maxFilter){
		LoadedObjectHandler<TextureInfo> existingTexture = TextureInfo.loadedTextures.get(uniqueId);
		if(existingTexture != null){
			existingTexture.getAttachedObject().incrememntReferences();
			return existingTexture.getAttachedObject();
		}
		int texId = TextureGenerator.GL_GenerateTexture(textureUnit, tWidth, tHeight, buf, minFilter, maxFilter);
		TextureInfo newTexture = new TextureInfo(texId);
		newTexture.tWidth = tWidth;
		newTexture.tHeight = tHeight;
		newTexture.buf = buf;
		newTexture.filePath = uniqueId;
		LoadedObjectHandler<TextureInfo> newTextureLoader = new LoadedObjectHandler<TextureInfo>(newTexture);
		newTextureLoader.setLoaded(true);
		loadedTextures.put(uniqueId, newTextureLoader);
		return newTexture;
	}

	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		try {
			loadTextureDataFromFile();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not load file: " + filePath);
			resortedToMissingTexture = true;
		}
	}

	public void loadTextureDataFromFile() throws IOException {
		System.out.println("Loading Texture Data!! - " + filePath);

		PNGDecoder decoder = null;
		InputStream in;
		try {
			// Open the PNG file as an InputStream
			in = FileReader.asInputStream(this.getPath());

			// Link the PNG decoder to this stream
			decoder = new PNGDecoder(in);
		} catch (Exception e) {
			throw new RuntimeException("Count not load texture: " + filePath, e);
		}
		//hello!
		// Get the width and height of the texture
		tWidth = decoder.getWidth();
		tHeight = decoder.getHeight();
		hasAlpha = decoder.hasAlpha();


		//System.out.println("loadTextureDataFromFile " + toString() + " " + filePath);

		// Decode the PNG file in a ByteBuffer
		buf = (ByteBuffer.allocateDirect(
				4 * decoder.getWidth() * decoder.getHeight()));
		decoder.decode(getBuf(), decoder.getWidth() * 4, Format.RGBA);
		getBuf().flip();

		if (in != null) {
			in.close();
		}
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		//Won't be called!
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		//No dependencies to load, this method will be skipped.
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		if (resortedToMissingTexture) {
			this.setTextureId(SolidFillTextureHandler.getSolidFillTexture().getTextureId());
		} else {
			int textureId = TextureGenerator.GL_GenerateTextureFromPNG(textureUnit, this, tilingStyle);
			this.setTextureId(textureId);
			//Remove buf - I think - memory management is weird in java, I just don't want memory leaks.
			clearBuffer();
		}
	}

	/**
	 * Remove buf - I think - memory management is weird in java, I just don't want memory leaks.
	 */
	protected void clearBuffer(){
		buf.clear();
		buf = null;
	}

	
	/*
	public TextureInfo(String path, int textID){
		this.filePath = path;
		this.textureID = textID;
		this.numOfReferences = 1;
	}
	*/

	public void setTextureId(int texId){
		textureID = texId;
	}

	public void incrememntReferences(){
		numOfReferences++;
	}

	/*
	 * Decrememnts the number of references by 1,
	 * if number of references == 0, return true
	 * else, return false
	 */
	public boolean decrementReferences(){
		//TODO - This is a nice idea in theory, but is not currently properly implemented.
//		numOfReferences--;
//		if (numOfReferences == 0) {
//			loadedTextures.remove(filePath);
//			GL11.glDeleteTextures(getTextureId());
//			return true;
//		} else {
//			return false;
//		}
		return true;
	}

	public int getTextureId(){
		return this.textureID;
	}

	public String getPath(){
		return filePath;
	}

	public boolean hasAlpha(){
		return hasAlpha;
	}

	public int get_tWidth() {
		return tWidth;
	}

	public int get_tHeight() {
		return tHeight;
	}

	public ByteBuffer getBuf() {
		return buf;
	}

	/**
	 * Manually sets the data for the TextureInfo instead of loading it.
	 * @param buffer
	 * @param textureWidth
	 * @param textureHeight
	 * @param hasAlpha
	 */
	public void setData(ByteBuffer buffer, int textureWidth, int textureHeight, boolean hasAlpha) {
		this.buf = buffer;
		this.tWidth = textureWidth;
		this.tHeight = textureHeight;
		this.hasAlpha = hasAlpha;
	}

	public int getMaxTextureFilter(){
		return maxTextureFilter;
	}

	public int getMinTextureFilter(){
		return minTextureFilter;
	}

	public boolean failedToLoad() {
		return resortedToMissingTexture;
	}
}

