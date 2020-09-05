package composites.map;

import loader.LoadedObjectAbstract;
import javax.vecmath.Vector2f;

import loader.LoadedObject;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;

public class MapLol extends LoadedObjectAbstract {

	private LoadedObjectHandler<Chunk>[][] chunkLoaders;
	private int width;
	private int height;
	private Vector2f chunkSize;
	//private Vector2f topLeft;
	
	private String mapFilePath;

	private LoadedObjectHandler.LoadStatus currentStatus;
	
	@Override
	public LoadStage[] stagesToPerform(){
		return new LoadStage[] {
//				LoadStage.LOAD_DATA_FROM_FILE,
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

	MapLol(String mapFilePath){
		this.mapFilePath = mapFilePath;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		//TODO: Implement properly:
		width = 5;
		height = 7;
		
		chunkSize = new Vector2f(20, 20);
		
		chunkLoaders = (LoadedObjectHandler<Chunk>[][]) new LoadedObjectHandler[width][height];
		
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				chunkLoaders[i][j] = LoadedObjectHandler.load(new Chunk("res/FakePath", true));
			}
		}
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		// TODO Auto-generated method stub
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				chunkLoaders[i][j] = LoadedObjectHandler.load(new Chunk("res/FakePath", true));
			}
		}
	}
	
	/*
	public void prepareRender(){
		// TODO Auto-generated method stub
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				LoadedObjectHandler<Chunk> chunkLoader = chunkLoaders[i][j];
				if(chunkLoader.isLoaded()){ //TODO!
					Chunk currentChunk = chunkLoader.getAttachedObject();
					currentChunk.prepareRender();
				}
			}
		}
	}
	*/
	
}
