package loader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import gebd.ActiveThreads;

public class FileLoaderThread extends Thread{
	
	//Singleton Pattern ...
	private static FileLoaderThread instance;
	public static FileLoaderThread getInstance(){
		if(instance == null){
			instance = new FileLoaderThread();
		}
		return instance;
	}
	
	private List<LoadedObjectHandler<?>> fileQueueNeed;
	private List<LoadedObjectHandler<?>> fileQueueGreed;
	
	private boolean running = false;
	private int needQueueSize = 0;
	private int greedQueueSize = 0;
	
	
	private FileLoaderThread(){
		fileQueueNeed = new LinkedList<LoadedObjectHandler<?>>();
		fileQueueGreed = new LinkedList<LoadedObjectHandler<?>>();
	}
	
	public synchronized void queueJob(LoadedObjectHandler<?> job, boolean need){
		if(need){
			getFileQueueNeed().add(job);
		} else {
			getFileQueueGreed().add(job);
		}
	}
	
	private synchronized List<LoadedObjectHandler<?>> getFileQueueNeed(){
		return this.fileQueueNeed;
	}
	
	private synchronized List<LoadedObjectHandler<?>> getFileQueueGreed(){
		return this.fileQueueGreed;
	}
	
	@Override
	public void run(){
		running = true;
		
		while(running){
			needQueueSize = getFileQueueNeed().size();
			greedQueueSize = getFileQueueGreed().size();
			if(needQueueSize > 0){
				LoadedObjectHandler<?> job = getFileQueueNeed().remove(0);
				job.loadFileData();
			} else if(greedQueueSize > 0){
				LoadedObjectHandler<?> job = getFileQueueGreed().remove(0);
				job.loadFileData();
			} else {
				//No file to load, don't eat up the CPU...
				//Just wait for a little while.
				try {
					Thread.sleep(10); //10 millisecond wait time max for load.
				} catch (InterruptedException e) {
					// Don't sleep.
				}
			}
		}
	}
	
	public void destroy(){
		running = false;
	}	
	
}

