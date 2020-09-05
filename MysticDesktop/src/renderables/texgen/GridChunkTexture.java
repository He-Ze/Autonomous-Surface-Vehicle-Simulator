package renderables.texgen;

import org.lwjgl.BufferUtils;

import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import renderables.texture.TextureGenerator;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;

public class GridChunkTexture extends TextureInfo{

	@Override
	public LoadStage[] stagesToPerform(){
		return new LoadStage[] {
//				LoadStage.LOAD_DATA_FROM_FILE,
//				LoadStage.HANDLE_RAW_DATA,
//				LoadStage.LOAD_DEPENDENCIES,
		};
	}
	
	public GridChunkTexture(){
		this.textureID = -1;
		this.numOfReferences = 0;
	}
	
	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		//Won't be called.
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
		//Nah.
	}
	
	public void createTextureFromChunk(int width, int height, short[] data){
		
		if(textureID != -1){
			TextureHandler.deleteTexture(this);
		}
		
		final int BYTES_PER_PIXEL = 4;
		this.tWidth = width;
		this.tHeight = height;
		buf = BufferUtils.createByteBuffer(tWidth * tHeight * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB
		
		hasAlpha = false;
		
		for(int y = 0; y < tHeight; y++){
			for(int x = 0; x < tWidth; x++){
				byte red = 0;
				byte green = (byte) (255 - data[(y*tWidth) + x]);
				byte blue = (byte) (255 - data[(y*tWidth) + x]);
				byte alpha = (byte) 0xFF;
				
				if(alpha != 0xFF){
					hasAlpha = true;
				}
				
				buf.put(red);
				buf.put(green);
				buf.put(blue);
				buf.put(alpha);
			}
		}
		
		buf.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS
		
//		this.textureID = TextureGenerator.GL_GenerateTextureFromPNG(TextureHandler.DEFAULT_TEXTURE_UNIT, this);
		//this.textureID = (int) (Math.random() * 90) + 10; //This was for testing purposes!
		
		buf.clear();
		buf = null;
		
	}
	

}
