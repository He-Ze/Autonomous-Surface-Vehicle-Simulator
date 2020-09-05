package composites;

import java.io.File;

import loader.LoadedObjectAbstract;
import javax.vecmath.Vector4f;

import configData.Config;
import gebd.Render;
import loader.LoadedObject;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;

public class ImageFontLegacy extends LoadedObjectAbstract {
	private String fontName, fontTexturePath, configPath;
	private short[] size;
	private Config fontData;
	private TextureInfo texture;
	//private int textureId;

	private LoadedObjectHandler.LoadStatus currentStatus;

	@Override
	public LoadedObjectHandler.LoadStatus getLoadStatus() {
		return currentStatus;
	}

	@Override
	public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {
		this.currentStatus = newLoadStatus;
	}
	@Override
	public LoadStage[] stagesToPerform(){
		return new LoadStage[] {
				LoadStage.LOAD_DATA_FROM_FILE,
//				LoadStage.HANDLE_RAW_DATA,
				LoadStage.LOAD_DEPENDENCIES,
		};
	}
	
	public ImageFontLegacy(String configPath) {
		this.configPath = configPath;
	}

	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		try {
			File configFile = new File(configPath);
			this.fontData = new Config(configFile);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1); //TODO - This is a BAD way of handling a bad load!
		}
		String[] fontInfo = fontData.getStrings("font");
		this.fontName = fontInfo[0];
		this.fontTexturePath = fontInfo[1];
		size = fontData.getShorts("font");
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		//Not called.
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		this.texture = TextureInfo.queueLoadOfPNGTexture(fontTexturePath, handler);
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		
	}
	
	public String getname() {
		return fontName;
	}
	
	public short[] getSize() {
		return size;
	}
	
	public String getTexPath() {
		return fontTexturePath;
	}
	
	public TextureInfo getTexture(){
		return this.texture;
	}
	
	private short[] getPositions(String character){
		short[] positions = fontData.getShorts(character);
		if(positions == null){
			positions = fontData.getShorts("?"); //TODO - Change to a square.
		}
		return positions;
	}
	
	public Vector4f getTexPositions(String character) {
		short[] positions = getPositions(character);
		return new Vector4f(positions[0], positions[1], positions[2], positions[3]);
	}
	
	public int[] getCharSize(String character) {
		short[] positions = getPositions(character);
		int sizeX = positions[2] - positions[0] + 1,
				sizeY = positions[3] - positions[1] + 1;
		return new int[] {sizeX, sizeY};
	}
	
	
	public float getFontHeight(){
		/*
		 * TODO
		 */
		return 6f;
	}
	
	public float getSpaceDistance(){
		/*
		 * TODO
		 */
		return 2f;
	}
	
	public float getNewLineDistance(){
		/*
		 * TODO
		 */
		return (getFontHeight() + 1f);
	}

	
}
