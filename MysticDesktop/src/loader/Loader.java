package loader;

public class Loader {
	
	//Singleton Pattern.
	private static Loader instance;
	public static Loader getInstance(){
		if(instance == null){
			instance = new Loader();
		}
		return instance;
	}
	
	private Loader(){
		FileLoaderThread.getInstance().start();
		DataHandlerThread.getInstance().start();
	}
	
	public void destroy(){
		FileLoaderThread.getInstance().destroy();
		DataHandlerThread.getInstance().destroy();
	}
}
