package loader;

import java.util.LinkedList;
import java.util.List;

import configData.Config;
import gebd.ActiveThreads;
import loader.dataHandler.*;

public class DataHandlerThread extends Thread{
	
	/*
	 * @TODO:
	 * ASDF?
	 */
	
	
	
	//Singleton Pattern ...
	private static DataHandlerThread instance;
	public static DataHandlerThread getInstance(){
		if(instance == null){
			instance = new DataHandlerThread();
		}
		return instance;
	}
	
	private List<LoadedObjectHandler> dataQueue;
	private boolean running = false;
	
	private int queueSize = 0;
	
	
	private DataHandlerThread(){
		dataQueue = new LinkedList<LoadedObjectHandler>();
	}
	
	public synchronized void queueJob(LoadedObjectHandler job){
		getDataQueue().add(job);
	}
	
	private synchronized List<LoadedObjectHandler> getDataQueue(){
		return this.dataQueue;
	}
	
	@Override
	public void run(){
		running = true;
		
		while(running){
			queueSize = getDataQueue().size();
			
			
			if(queueSize > 0){
				LoadedObjectHandler job = getDataQueue().remove(0);
				job.handleFileData();
			} else {
				try {
					Thread.sleep(10); //10 millisecond wait time max for load.
				} catch (InterruptedException e) {
					// Don't sleep.
					e.printStackTrace();
				}
			}
		}
	}
	
	public void destroy(){
		running = false;
	}
	
}
