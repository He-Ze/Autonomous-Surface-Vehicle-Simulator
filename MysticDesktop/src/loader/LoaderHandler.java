package loader;

public abstract class LoaderHandler {
	
	public static enum loadStatus{
		WAITING,
		LOAD_DATA_FROM_FILE,
		HANDLE_RAW_DATA,
		FINISHED_SUCCESSFULLY;
	}
	
	public final void loadFileData(){
		//Debug Information!
		this.currentStage = loadStatus.LOAD_DATA_FROM_FILE; //Debug Information!
		this.loadRawDataFromFile();
	}
	
	public final void handleFileData(){
		this.currentStage = loadStatus.HANDLE_RAW_DATA;	//Debug Information!
		this.loadRawDataFromFile();
	}
	
	/*
	 * Called from the FileLoaderThread.
	 * implement it to load the file.
	 */
	public abstract void loadRawDataFromFile();
	
	/*
	 * Called from the DataHandlerThread.
	 * implement it to handle the raw data.
	 */
	public abstract void handleRawData();
	
	protected String fileName;
	protected loadStatus currentStage;
	
	public LoaderHandler(String fileName){
		this.fileName = fileName;
		this.currentStage = loadStatus.WAITING;
	}
	
	public String getFileName(){
		return this.getFileName();
	}
	
	/*
	 * Doesn't have to be synchronized!
	 */
	public loadStatus getLoadStage(){
		return this.currentStage;
	}
}
