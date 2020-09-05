package loader;

import loader.LoadedObjectHandler.LoadStage;

public interface LoadedObject {

	/**
	 * Returns a list of all the stages that the
	 * objects requires to load.
	 * @return AllStagesToComplete
	 */
	LoadStage[] stagesToPerform();
	
	
	/**
	 * -- FILE LOADER THREAD --
	 * Called from the FileLoaderThread.
	 * implement it to load the file.
	 */
	void loadRawDataFromFile(LoadedObjectHandler<?> handler);
	
	/**
	 * -- DATA HANDLER THREAD --
	 * Called from the DataHandlerThread.
	 * implement it to handle the raw data.
	 */
	void handleRawData(LoadedObjectHandler<?> handler);
	
	
	/**
	 * -- RENDER THREAD --
	 * Called when you know what is required 
	 * to load this object.
	 */
	void loadDependencies(LoadedObjectHandler<?> handler);
	
	
	/**
	 * -- RENDER THREAD --
	 * Complete load
	 */
	void completeLoad(LoadedObjectHandler<?> handler);

	/**
	 * This is annoying in the fact that I can't give interfaces variables. :'(
	 * @return the current stage of loading that the object is at.
     */
	LoadedObjectHandler.LoadStatus getLoadStatus();


	/**
	 * Sets the current load status of the Object.
	 * @param newLoadStatus - The current load status of the object.
     */
	void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus);

	boolean isLoaded();

	//TODO: Cannot do this on Java 7 (Android's Java version as of 2016) :'(
	/*
	default boolean isLoaded(){
		return (getLoadStatus() == LoadedObjectHandler.LoadStatus.COMPLETE_LOAD);
	}
	*/
}
