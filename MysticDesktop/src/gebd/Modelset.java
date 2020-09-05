package gebd;

import configData.Config;
import blindmystics.util.FileReader;
import loader.LoadedObjectAbstract;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import objUtils.EntRead;

import java.io.InputStream;

import renderables.Vertex;
import renderables.r3D.model.Model;

public class Modelset extends LoadedObjectAbstract {
	private Config modelData = new Config();
	private int numberOfModels;
	private int numberOfElements;
	private EntRead[] ents;
	private Model[] models;

	private short[] indices;
	private Vertex[] vertices;

	private LoadedObjectHandler.LoadStatus currentStatus;

	public Modelset(Config... modelData) {
		
		for (int a = 0; a < modelData.length; a++) {
			for (String key : modelData[a].getKeys()) {
				this.modelData.addValues(key, modelData[a].getStrings(key));
			}
		}
		numberOfModels = this.modelData.getKeys().length;
		models = new Model[numberOfModels];

		loadEnts();
		prepareModels();
	}

	public Modelset(String path) {
		this(new Config(path));
	}

	public int getModelIndex(String name) {
		return modelData.setExists(name);
	}

	public String[] getModelNames() {
		return modelData.getKeys();
	}

	public String getModelName(int index) {
		return modelData.getKeys()[index];
	}

	public String getModelPath(String name) {
		return modelData.getStrings(name)[0];
	}

	public String getModelPath(int index) {
		String key = this.modelData.getKeys()[index];
		return modelData.getStrings(key)[0];
	}

	public String getTexturePath(String name) {
		return modelData.getStrings(name)[1];
	}

	public String getTexturePath(int index) {
		String key = this.modelData.getKeys()[index];
		return modelData.getStrings(key)[1];
	}

	public int getNumberOfModels() {
		return numberOfModels;
	}

	public Model getModel(String identifier, boolean name) {
		for (Model model : models) {
			if (name && model.getModelName() == identifier) {
				return model;
			} else if (!name && model.getModelPath() == identifier) {
				return model;
			}
		}
		return null;
	}

	public Model[] getLoadedModels() {
		return models;
	}

	private void loadEnts() {
		InputStream modelFile;

		ents = new EntRead[numberOfModels];
		EntRead ent;

		numberOfElements = 0;

		String[] keys = modelData.getKeys();
		String key;
		String path;

		for (int a = 0; a < numberOfModels; a++) {
			key = keys[a];
			path = modelData.getStrings(key)[0];
			modelFile = FileReader.asInputStream(path);

			ent = new EntRead(modelFile, path);
			ents[a] = ent;

			numberOfElements += ent.getIndices().length;
		}
	}

	private void prepareModels() {
		String[] keys = modelData.getKeys();
		for (int a = 0; a < numberOfModels; a++) {
			models[a] = new Model(keys[a], modelData.getStrings(keys[a])[0], ents[a]);
		}
	}

	public void load() {
		for (Model model : models) {
			model.load();
		}
	}

	public void destroy() {
		for (Model model : models) {
			model.destroy();
		}
	}

	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {

	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {

	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {

	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {

	}

	@Override
	public LoadStage[] stagesToPerform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoadedObjectHandler.LoadStatus getLoadStatus() {
		return currentStatus;
	}

	@Override
	public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {
		this.currentStatus = newLoadStatus;
	}
}
