package boatStuff.grid;

import loader.LoadedObjectHandler;
import renderables.texgen.GridChunkTexture;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;

public class TexGrid extends Grid {
	
	//NOTE - Texture size MUST be a power of 2!
	public static final int TEXTURE_SIZE = (1 << 4); //16px
	public static final int TEXTURE_WIDTH = TEXTURE_SIZE; //px.
	public static final int TEXTURE_HEIGHT = TEXTURE_SIZE; //px.
	
	public static final int TEXTURE_COUNT_WIDTH = WIDTH / TEXTURE_WIDTH;
	public static final int TEXTURE_COUNT_HEIGHT = HEIGHT / TEXTURE_HEIGHT;
	
	public static final int TEX_GRID_MASK = TEXTURE_COUNT_WIDTH -1; 
	
	public static final double meshSize = TEXTURE_WIDTH * SQUARE_SIZE;
	
	public GridChunkTexture[][] textures;
	
	public TexGrid(){
		super();
		int width = TEXTURE_COUNT_WIDTH;
		int height = TEXTURE_COUNT_HEIGHT;
		textures = new GridChunkTexture[width][height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				textures[x][y] = LoadedObjectHandler.load(new GridChunkTexture()).getAttachedObject();
			}
		}
	}
	
	@Override
	public void insertChunkAt(int x, int y, short[] chunk){
		if(chunk.length == 0){
			return;
		}
		super.insertChunkAt(x, y, chunk);
		double chunkWidth = Math.sqrt(chunk.length);
		
		int mask = TEXTURE_WIDTH-1;
		
		double xOffset = x&mask;
		double xSpan = (xOffset + chunkWidth) / ((double) TEXTURE_WIDTH);
		int numChunksInXChanged = (int) Math.ceil(xSpan);
		
		double yOffset = y&mask;
		double ySpan = (yOffset + chunkWidth) / ((double) TEXTURE_WIDTH);
		int numChunksInYChanged = (int) Math.ceil(ySpan);
		
		for(int j = 0; j < numChunksInYChanged; j++){
			for(int i = 0; i < numChunksInXChanged; i++){
				int xChange = ((x/TEXTURE_WIDTH) + i) & TEX_GRID_MASK;
				int yChange = ((y/TEXTURE_WIDTH) + j) & TEX_GRID_MASK;
				updateTextureAt(xChange, yChange);
			}
		}
	}
	
	private void updateTextureAt(int xChunk, int yChunk){
		int xPos = xChunk * TEXTURE_WIDTH;
		int yPos = yChunk * TEXTURE_HEIGHT;
		short[] chunkData = getChunkAt(xPos, yPos, TEXTURE_WIDTH);
		textures[xChunk][yChunk].createTextureFromChunk(TEXTURE_WIDTH, TEXTURE_HEIGHT, chunkData);
	}
	
	public void destroy(){
		int width = TEXTURE_COUNT_WIDTH;
		int height = TEXTURE_COUNT_HEIGHT;
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				TextureInfo texture = textures[x][y]; 
				if(texture.getTextureId() != -1){
					TextureHandler.deleteTexture(texture);
				}
			}
		}
	}
}