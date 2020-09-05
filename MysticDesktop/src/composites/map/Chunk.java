package composites.map;

import java.util.ArrayList;
import java.util.HashMap;

import composites.entities.Entity;
import composites.entities.OversizedEntity;
import loader.FileLoaderThread;
import loader.LoadedObject;
import loader.LoadedObjectAbstract;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;

public class Chunk extends LoadedObjectAbstract {
	private ArrayList<Entity> containedEntities;
	private ArrayList<OversizedEntity> oversizedEntities;
	
	private String chunkDataPath;
	private boolean requiredQuickly;
	
	private HashMap<Integer, OversizedEntity> OversizedEntitiesLoading = new HashMap<Integer, OversizedEntity>();

	private LoadedObjectHandler.LoadStatus currentStatus;
	
	@Override
	public LoadStage[] stagesToPerform(){
		return new LoadStage[] {
				LoadStage.LOAD_DATA_FROM_FILE,
				LoadStage.HANDLE_RAW_DATA,
				LoadStage.LOAD_DEPENDENCIES,
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

	public Chunk(String chunkDataPath, boolean requiredQuickly){
		this.chunkDataPath = chunkDataPath;
		this.requiredQuickly = requiredQuickly;
	}
	
	
	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		//TODO
	}
	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		/*
		 * TODO
		 * This part we will load the objects only.
		 */

	}
	
	int numberOfDependancies = 0;
	int numberOfEntities = 0;
	
	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		// TODO BROKEN!
		
		
		int oversizedEntityUniqueId = -1;
		ArrayList<Chunk> chucksThatShareOversizedEntity = new ArrayList<Chunk>();

		boolean entityAlreadyBeingLoaded = false;
		
		for(int i = 0; ((i < chucksThatShareOversizedEntity.size()) && (!entityAlreadyBeingLoaded)); i++){
			OversizedEntity entity = handler.newDependancy(chucksThatShareOversizedEntity.get(i).getOversizedEntity(oversizedEntityUniqueId));
		}

	}
	
	
	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		// TODO Auto-generated method stub
		/*for(OversizedEntity e : oversizedEntities){
			
		}*/
	}
	
	public OversizedEntity getOversizedEntity(int uniqueEntityId){
		return OversizedEntitiesLoading.get(uniqueEntityId);
	}
	
	public boolean hasLoadedThingAreadylolz(){
		return false;
	}
	/*
	public void prepareRender() {
		for(Entity e : containedEntities){
			e.getModel().addFrameEntity(e);
		}
		for(OversizedEntity e : oversizedEntities){
			if(!e.hasBeenPreparedThisFrame()){
				e.getModel().addFrameEntity(e);
			}
		}
	}
	*/
}
