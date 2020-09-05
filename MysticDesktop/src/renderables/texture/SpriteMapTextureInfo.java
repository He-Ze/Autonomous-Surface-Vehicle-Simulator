package renderables.texture;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import loader.AsyncLoader;
import loader.LoadedObject;
import loader.LoadedObjectAbstract;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import renderables.texture.generated.GeneratedTextureInfo;

public class SpriteMapTextureInfo extends LoadedObjectAbstract {
	protected String filePath;
	protected int textureUnit = TextureHandler.DEFAULT_TEXTURE_UNIT;
	
	protected TextureCoord[] texturesCoords;
	protected TextureInfo[] textures;
	protected ArrayList<LoadedObjectHandler<TextureInfo>> loadingTextures;
	protected boolean[] textureRequiresLoading;
	
	protected LoadStage[] stagesToPerform;

	private LoadedObjectHandler.LoadStatus currentStatus;
	
	@Override
	public LoadStage[] stagesToPerform(){
		return stagesToPerform;
	}

	@Override
	public LoadedObjectHandler.LoadStatus getLoadStatus() {
		return currentStatus;
	}

	@Override
	public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {
		this.currentStatus = newLoadStatus;
	}

	/**
	 * TODO - Make it so it determine the textureCoords if none are specified!
	 * @param path
	 * @param givenTextureCoords
     */
	public SpriteMapTextureInfo(String path, TextureCoord[] givenTextureCoords){
		this.filePath = path;
		texturesCoords = new TextureCoord[givenTextureCoords.length];
		textures = new TextureInfo[givenTextureCoords.length];
		textureRequiresLoading = new boolean[givenTextureCoords.length];
		loadingTextures = new ArrayList<LoadedObjectHandler<TextureInfo>>(givenTextureCoords.length);
		
		boolean allSpritesAlreadyLoaded = true;
		
		for(int i = 0; i < givenTextureCoords.length; i++){
			texturesCoords[i] = givenTextureCoords[i];
			String uniqueTextureId = getUniqueSpriteTextureId(path, givenTextureCoords[i]);

			LoadedObjectHandler<TextureInfo> existingTexture = TextureInfo.loadedTextures.get(uniqueTextureId);
			if(existingTexture == null){
				allSpritesAlreadyLoaded = false;
				textureRequiresLoading[i] = true;
				existingTexture = new LoadedObjectHandler<TextureInfo>(new TextureInfo(uniqueTextureId));
				TextureInfo.loadedTextures.put(uniqueTextureId, existingTexture);
				loadingTextures.add(existingTexture);
			} else {
				loadingTextures.add(existingTexture);
				textureRequiresLoading[i] = false;
			}
			textures[i] = existingTexture.getAttachedObject();
		}
		
		if(allSpritesAlreadyLoaded){
			stagesToPerform = new LoadStage[]{
				LoadStage.LOAD_DEPENDENCIES
			};
		} else {
			stagesToPerform = new LoadStage[]{
					LoadStage.LOAD_DATA_FROM_FILE,
					//LoadStage.HANDLE_RAW_DATA,
					LoadStage.LOAD_DEPENDENCIES
			};
		}
	}
	
	

	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		// Load the raw data from the PNG file.
		TextureInfo wholeTexture = new TextureInfo(filePath);
		try {
			wholeTexture.loadTextureDataFromFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Iterate through the coordinates and generate a sub texture.
		for (int i = 0; i < texturesCoords.length; i++) {
			if(textureRequiresLoading[i]){
				TextureCoord textureCoord = texturesCoords[i];
				String storedTextureId = getUniqueSpriteTextureId(filePath, textureCoord);
				TextureInfo newTexture = textures[i];
				createSubTexture(wholeTexture, textureCoord, storedTextureId, newTexture);
			}
		}
	}
	
	
	/**
	 * Generates a subtexture from a full PNG image an a set of texture coordinates determine the size
	 * of the subtexture.
	 * 
	 * @param texture - The whole PNG image.
	 * @param currentTextureCood - texture coordinates determine the size of the subtexture.
	 * @param uniqueTexturePath - The unique ID given to the texture.
	 * @return - The newly generated Sub-Texture.
	 */
	private static void createSubTexture(TextureInfo texture, TextureCoord currentTextureCood,
			String uniqueTexturePath, TextureInfo newTexture) {

		// The dimensions of the whole PNG image.
		int imageWidth = texture.get_tWidth();
		int imageHeight = texture.get_tHeight();
		ByteBuffer imgBuffer = texture.getBuf();
		

		// The coordinates of the sub texture.
		float botX = currentTextureCood.getBottomX();
		float botY = currentTextureCood.getBottomY();
		float topX = currentTextureCood.getTopX();
		float topY = currentTextureCood.getTopY();

		// Round the coordinates the the nearest pixel.
		int minX = (int) (Math.round((imageWidth * botX)));
		int maxX = (int) (Math.round((imageWidth * topX)));
		int minY = (int) (Math.round((imageHeight * botY)));
		int maxY = (int) (Math.round((imageHeight * topY)));

		// Determine the width and height of the sub texture.
		int subTexWidth = maxX - minX;
		int subTexHeight = maxY - minY;
		boolean subImageHasAlpha = false;

		// Iterate through each pixel in the PNG image that is in the sub texture.
		ByteBuffer subBuffer = ByteBuffer.allocateDirect(4 * subTexWidth * subTexHeight);
		for (int y = minY; y < maxY; y++) {
			imgBuffer.position((y * imageWidth + minX) * 4);
			for (int x = minX; x < maxX; x++) {
				// Add the 4 colour bytes RGBA
				for (int j = 0; j < 4; j++) {
					byte pixelData = imgBuffer.get();
					subBuffer.put(pixelData);
					if((j == 3) && (pixelData != 0xFF)){
						subImageHasAlpha = true;
					}
				}
			}
		}
		subBuffer.flip();

		newTexture.setData(subBuffer, subTexWidth, subTexHeight, subImageHasAlpha);
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		//Won't be called!
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		/*
		for(int i = 0; i < texturesCoords.length; i++){
			if(!textureRequiresLoading[i]){
				//We depend on other textures being loaded, wait for them.
				int loadPosition = handler.getDependancyNo();
				loadingTextures.get(i).addWaiting(handler, loadPosition);
			}
		}
		*/
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		for(int i = 0; i < texturesCoords.length; i++){
			if(textureRequiresLoading[i]){
				TextureInfo texture = textures[i];
				//Complete the load and notify all dependencies.
				loadingTextures.get(i).getAttachedObject().completeLoad(AsyncLoader.ASYNC_LOADER);
			}
		}
		//Memory management.
		loadingTextures.clear();
		loadingTextures = null;
	}
	
	public static String getUniqueSpriteTextureId(String path, TextureCoord textureCoord){
		return path + "-" + textureCoord.toString();
	}
	
	
	public String getPath(){
		return filePath;
	}
	
	public TextureCoord[] getTextureCoords(){
		return texturesCoords;
	}
	
	public TextureInfo[] getTextures(){
		return textures;
	}
	
	public TextureInfo getTexture(int texureNo){
		return textures[texureNo % getNumTextures()]; //Ensure that it doesn't overflow.
	}
	
	public int getNumTextures(){
		return textures.length;
	}
}

