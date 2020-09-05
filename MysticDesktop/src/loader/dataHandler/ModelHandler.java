package loader.dataHandler;

import java.io.File;

import configData.Config;
import gebd.Render;
import loader.DataHandlerThread;
import loader.LoaderHandler;


public class ModelHandler extends LoaderHandler{
	
	protected Config data = null;
	
	public ModelHandler(String fileName){
		super(fileName);
	}

	@Override
	public void loadRawDataFromFile() {
		/*
		File newFile = new File(fileName);
		
		try {
			data = new Config(newFile);
		} catch (Exception e){
			//Error loading config!
			e.printStackTrace();
			Render.getInstance().notifyLoadFailure(this);
		}
		
		DataHandlerThread.getInstance().queueJob(this);
		*/
	}

	@Override
	public void handleRawData() {
		//Handle Data, and then send it back to the place it needs to go!
		/*
		Model model = null;
		
		
		try {
			model = new Model(data);
		} catch (Exception e){
			e.printStackTrace();
			Render.getInstance().notifyLoadFailure(this);
		}
		
		this.currentStage = loadStatus.FINISHED_SUCCESSFULLY;
		Render.getInstance().setModel(model);
		*/
	}
	
}