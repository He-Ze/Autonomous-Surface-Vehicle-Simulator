package loader;

import java.util.Iterator;
import java.util.LinkedList;

import gebd.ActiveThreads;
import gebd.Render;

public class LoadedObjectHandler <K extends LoadedObject>{
	
	public static enum LoadStatus {
		INITIATE,
		LOAD_DATA_FROM_FILE,
		HANDLE_RAW_DATA,
		LOAD_DEPENDENCIES,
		WAIT_FOR_DEPENDANCIES,
		COMPLETE_LOAD;
	}
	
	public static enum LoadStage{
		LOAD_DATA_FROM_FILE,
		HANDLE_RAW_DATA,
		LOAD_DEPENDENCIES;
	}
	
	protected ActiveThreads currentThreadWithin = ActiveThreads.GL_RENDER;
	private void setCurrentThread(ActiveThreads newThread){
		this.currentThreadWithin = newThread;
	}
	
	protected K attachedObject;
	
	protected boolean performLoadRawDataFromFile = false;
	protected boolean performHandleRawData = false;
	protected boolean performLoadDependancies = false;
	
	protected LinkedList<LoadStage> stagesToComplete = new LinkedList<LoadStage>();
	
	protected boolean loggingEnabled = false;
	
	protected LoadStatus currentStage;
	protected Object[] dependencies = null;
	protected int numDependenciesToLoad = -1;
	protected int numDependenciesLoaded = 0;
	
	protected int dNo = 0;
	
	protected boolean loaded = false;
	
	public Object[] getDependencies(){
		return dependencies;
	}
	/* Invariant - The size of these always are the same.
	 * Also - loadBlockedObjects.get(i) corresponds to loadBlockedObjectDependancyPositions.get(i)
	 */
	public LinkedList<LoadedObjectHandler<?>> loadBlockedObjects = new LinkedList<LoadedObjectHandler<?>>();
	public LinkedList<Integer> loadBlockedObjectDependancyPositions = new LinkedList<Integer>();

	public LinkedList<Integer> loadedDependancyPositions = new LinkedList<Integer>();
	public LinkedList<LoadedObjectHandler<?>> loadedDependancies = new LinkedList<LoadedObjectHandler<?>>();
	
	public LoadedObjectHandler(K objectToAttach){
		this.attachedObject = objectToAttach;
		setStages(objectToAttach.stagesToPerform());
	}

	private void setLoadStatus(LoadStatus newLoadStatus){
		this.currentStage = newLoadStatus;
		this.attachedObject.setLoadStatus(newLoadStatus);
	}
	
	protected void setStages(LoadStage[] AllStagesToComplete){
		//System.out.print("@Contructor - ");
		//System.out.println(String.valueOf(getAttachedObject()));
		setLoadStatus(LoadStatus.INITIATE);
		for(int i = 0; i < AllStagesToComplete.length; i++){
			//System.out.println("AllStagesToComplete[i] = " + AllStagesToComplete[i].name());
			switch(AllStagesToComplete[i]){
			case LOAD_DATA_FROM_FILE:
				performLoadRawDataFromFile = true;
				break;
			case HANDLE_RAW_DATA:
				performHandleRawData = true;
				break;
			case LOAD_DEPENDENCIES:
				performLoadDependancies = true;
				break;
			}
		}
		if(performLoadRawDataFromFile){
			stagesToComplete.addLast(LoadStage.LOAD_DATA_FROM_FILE);
		}
		if(performHandleRawData){
			stagesToComplete.addLast(LoadStage.HANDLE_RAW_DATA);
		}
		if(performLoadDependancies){
			stagesToComplete.addLast(LoadStage.LOAD_DEPENDENCIES);
		}
	}
	
	
	public void handleLoad(){
		//System.out.print("@PostConstruct ! - ");
		//System.out.println(String.valueOf(getAttachedObject()));
		if(stagesToComplete.size() == 0){
			this.handleCompleteLoad();
			return;
		}
		LoadStage nextStage = stagesToComplete.removeFirst();
		stagesToComplete.clear();
		switch(nextStage){
		case LOAD_DATA_FROM_FILE:
			FileLoaderThread.getInstance().queueJob(this, true); //TODO - THIS IS ALWAYS TRUE!
			break;
		case HANDLE_RAW_DATA:
			DataHandlerThread.getInstance().queueJob(this);
			break;
		case LOAD_DEPENDENCIES:
			this.handleLoadDependencyStage();
			break;
		}
	}
	
	public final void loadFileData(){
		setCurrentThread(ActiveThreads.FILE_LOADER);
		setLoadStatus(LoadStatus.LOAD_DATA_FROM_FILE); //Debug Information!
		attachedObject.loadRawDataFromFile(this);
		if(performHandleRawData){
			DataHandlerThread.getInstance().queueJob(this);
		} else {
			Render.instance.addWaitingLoadJob(this);
		}
	}
	
	public final void handleFileData(){
		setCurrentThread(ActiveThreads.RAW_DATA_HANDLER);
		setLoadStatus(LoadStatus.HANDLE_RAW_DATA); //Debug Information!
		attachedObject.handleRawData(this);
		Render.instance.addWaitingLoadJob(this);
	}
	
	public int getDependancyNo(){
		int currentDependancyNo = dNo;
		dNo++;
		return currentDependancyNo;
	}
	
	public final void handleLoadDependencyStage(){
		setCurrentThread(ActiveThreads.GL_RENDER);
		if(performLoadDependancies){
			setLoadStatus(LoadStatus.LOAD_DEPENDENCIES);
			dNo = 0;
			attachedObject.loadDependencies(this);
			this.dependencies = new Object[dNo];
			setLoadStatus(LoadStatus.WAIT_FOR_DEPENDANCIES);
			int loadDependanciesListSize = loadedDependancies.size();
			for(int i = 0; i < loadDependanciesListSize; i++){
				notifyLoadedDependancy(loadedDependancyPositions.removeFirst(), loadedDependancies.removeFirst());
			}
			if(dependencies == null){
				numDependenciesToLoad = 0;
			} else {
				numDependenciesToLoad = dependencies.length;
			}
			if(numDependenciesToLoad == numDependenciesLoaded){
				handleCompleteLoad();
			} else if(numDependenciesToLoad < numDependenciesLoaded){
				System.err.println("Error Detected (LoadedObject.java)! - numDependenciesToLoad is smaller than numDependenciesLoaded!");
				System.err.println(this);
				System.err.println(numDependenciesToLoad + " : " + numDependenciesLoaded);
			}
		} else {
			handleCompleteLoad();
		}
	}
	
	private final void handleCompleteLoad(){
		//System.out.println("completeLoad " + getAttachedObject().toString());
		attachedObject.completeLoad(this);
		setLoadStatus(LoadStatus.COMPLETE_LOAD);
		loaded = true;
		this.dependencies = null;
		notifyAllWaiting();
	}
	
	/*
	 * Doesn't have to be synchronized!
	 */
	public LoadStatus getLoadStage(){
		return this.currentStage;
	}
	
	public int getNumdependanciesToLoad(){
		return numDependenciesToLoad;
	}
	
	public int getNumDependenciesLoaded(){
		return numDependenciesLoaded;
	}
	
	public void displayPositionsLoaded(){
		//private LinkedList<Integer> loadedDependancyPositions = new LinkedList<Integer>();
		//private LinkedList<LoadedObject> loadedDependancies = new LinkedList<LoadedObject>();
		for(int i = 0; i < loadedDependancies.size(); i++){
			System.out.println(loadedDependancyPositions.get(i) + " - " + String.valueOf(loadedDependancies.get(i)));
		}
	}
	
	public void notifyLoadedDependancy(int localDependencyLocation, LoadedObjectHandler<?> dependency){
		if(this.currentStage == LoadStatus.WAIT_FOR_DEPENDANCIES){
			dependencies[localDependencyLocation] = dependency;
			numDependenciesLoaded++;
			if(numDependenciesToLoad == numDependenciesLoaded){
				handleCompleteLoad();
			}
		} else {
			loadedDependancyPositions.add(localDependencyLocation);
			loadedDependancies.add(dependency);
		}
	}
	
	public boolean isLoaded(){
		return this.loaded;
	}
	
	
	public void addWaiting(LoadedObjectHandler<?> waitingJob, int loadPosition){
		if(this.currentStage == LoadStatus.COMPLETE_LOAD){
			waitingJob.notifyLoadedDependancy(loadPosition, this);
		} else {
			loadBlockedObjects.add(waitingJob);
			loadBlockedObjectDependancyPositions.add(loadPosition);
		}
	}
	
	private void notifyAllWaiting(){ //TODO - PRIVATE
		Iterator<LoadedObjectHandler<?>> jobIterator = loadBlockedObjects.listIterator();
		Iterator<Integer> loadPositionIterator = loadBlockedObjectDependancyPositions.listIterator();
		
		while(jobIterator.hasNext()){
			LoadedObjectHandler<?> dependancyObject = jobIterator.next();
			dependancyObject.notifyLoadedDependancy(loadPositionIterator.next(), this);
			//jobIterator.next().notifyLoadedDependancy(loadPositionIterator.next(), this);
		}
		
		//Destroy the un-needed data.
		loadBlockedObjects.clear();
		loadBlockedObjectDependancyPositions.clear();
	}
	
	private void log(Object obj){
		if(loggingEnabled){
			System.out.println(String.valueOf(obj));
		}
	}
	
	public void setLoaded(boolean isLoaded) {
		this.loaded = isLoaded;
	}
	
	public K getAttachedObject(){
		return this.attachedObject;
	}
	
	/*
	 * TODO - Implement! :)
	 */
	public static <T extends LoadedObject> LoadedObjectHandler<T> load(T objectToLoad){
		LoadedObjectHandler<T> objectLoadHandler = new LoadedObjectHandler<T>(objectToLoad);
		objectLoadHandler.handleLoad();
		return objectLoadHandler;
	}
	
	public <T extends LoadedObject> LoadedObjectHandler<T> createDependancy(T newDependancy){
		LoadedObjectHandler<T> objectLoadHandler = new LoadedObjectHandler<T>(newDependancy);
		objectLoadHandler.addWaiting(this, dNo++);
		objectLoadHandler.handleLoad();
		return objectLoadHandler;
	}
	
	public <T extends LoadedObject> T newDependancy(T newDependancy){
		LoadedObjectHandler<T> objectLoadHandler = new LoadedObjectHandler<T>(newDependancy);
		objectLoadHandler.addWaiting(this, dNo++);
		objectLoadHandler.handleLoad();
		return newDependancy;
	}

	
}
