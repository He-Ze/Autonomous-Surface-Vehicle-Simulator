package gebd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import gebd.ModelsetPlus;
import loader.LoadedObjectAbstract;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import composites.entities.Entity;
import configData.Config;
import gebd.light.PointLight;
import loader.LoadedObject;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;

public class Map extends LoadedObjectAbstract {
	
	private ModelsetPlus modelset;
	private String name;
	private Config mapData;
	//private Camera camera;
	
	private Vector3f cameraPos;
	private Vector2f cameraRot;
	
	private Vector2f cameraTurnLimits;
	
	private ArrayList<Vector3f> lockonPoints;
	
	private float ambientLightIntensity = 0;
	private ArrayList<Entity> entities;
	private ArrayList<PointLight> lights;

	private ArrayList<String> names;
	private ArrayList<Short> objects;
	private ArrayList<Vector3f> coords;
	private ArrayList<Vector3f> scales;
	private ArrayList<Vector3f> rotations;
	
	private String mapPath;
	
	private LoadStage[] stagesToLoad;

	private LoadedObjectHandler.LoadStatus currentStatus;
	
	@Override
	public LoadStage[] stagesToPerform(){
		return stagesToLoad;
		
	}

	@Override
	public LoadedObjectHandler.LoadStatus getLoadStatus() {
		return currentStatus;
	}

	@Override
	public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {
		this.currentStatus = newLoadStatus;
	}

	public Map(String path) {
		stagesToLoad = new LoadStage[] {
				LoadStage.LOAD_DATA_FROM_FILE,
//				LoadStage.HANDLE_RAW_DATA,
				LoadStage.LOAD_DEPENDENCIES,
		};
		mapPath = path;
	}
	
	public Map() {
		stagesToLoad = new LoadStage[] {
				//LoadStage.LOAD_DATA_FROM_FILE,
				//LoadStage.HANDLE_RAW_DATA,
				//LoadStage.LOAD_DEPENDENCIES,
		};
		name = "untitled";
		
		entities = new ArrayList<Entity>();
		lights = new ArrayList<PointLight>();
	}

	private void loadModelset() {
		Config[] modelsetcfg;
		File data;
		String[] paths;
		
		paths = mapData.getStrings("modelset");
		
		modelsetcfg = new Config[paths.length];
		
		for(int i = 0; i < paths.length; i++ ) {
			data = new File(paths[i]);
			modelsetcfg[i] = new Config(data);
		}
		//TODO broken
		//modelset = new ModelsetPlus(modelsetcfg);
	}
	
	public int getNumObjects() {
		return entities.size();
	}
	
	public Entity getEntity(int entityId) {
		return entities.get(entityId);
	}
	
	public Entity[] getEntities() {
		return entities.toArray(new Entity[0]);
	}
	
	public String getMapName() {
		return name;
	}
	
	public void setMapName(String name) {
		this.name = name;
	}
	
	public PointLight[] getLights() {
		return lights.toArray(new PointLight[0]);
	}
	
	public void addLight(PointLight light) {
		lights.add(light);
	}
	
	public float getAmbientLightIntensity() {
		return ambientLightIntensity;
	}
	
	public void setAmbientLightIntensity(float intensity) {
		ambientLightIntensity = intensity;
	}
	
	public void removeEntity(int index) {
		entities.remove(index);
	}
	
	public Vector2f getCameraRotation() {
		return cameraRot;
	}
	
	public Vector3f getCameraPosition() {
		return cameraPos;
	}
	
	public Vector2f getCameraTurnLimits() {
		return cameraTurnLimits;
	}
	
	public Vector3f[] getLockonPoints() {
		return lockonPoints.toArray(new Vector3f[0]);
	}

	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {

		System.out.println("Loading dependencies for map at path: " + mapPath);
		File mapFile = new File(mapPath);
		Config mapCfg = new Config(mapFile);

		float[] lightCoordsData;
		float[] lightColourData;
		float[] lightStrengthData;
		float[] coordsData;
		float[] scalesData;
		float[] rotationsData;
		int numberOfObjects, numOfLights, numofLockonPoints;

		float[] cameraLocation;
		float[] cameraRotation;
		
		short[] turnLimitsData;
		
		float[] lockonPointsData;

		this.mapData = mapCfg;

		//Load map name
		name = mapData.getStrings("map")[0];

		ambientLightIntensity = mapData.getFloats("ambientLight")[0];

		//Load modelset
		loadModelset();

		//Store co-ords, scales and rotations in world space
		coordsData = mapData.getFloats("coordinates");
		scalesData = mapData.getFloats("scales");
		rotationsData = mapData.getFloats("rotations");
		objects = mapData.getShortList("entities");
		names = mapData.getStringList("names");

		numberOfObjects = objects.size();
		coords = new ArrayList<Vector3f>();
		scales = new ArrayList<Vector3f>();
		rotations = new ArrayList<Vector3f>();

		for(int i = 0; i < numberOfObjects; i++){
			coords.add(new Vector3f(coordsData[i*3], coordsData[i*3+1], coordsData[i*3+2]));
			scales.add(new Vector3f(scalesData[i*3], scalesData[i*3+1], scalesData[i*3+2]));
			rotations.add(new Vector3f(rotationsData[i*3], rotationsData[i*3+1], rotationsData[i*3+2]));
		}

		lightCoordsData = mapData.getFloats("lightCoordinates");
		lightColourData = mapData.getFloats("lightColour");
		lightStrengthData = mapData.getFloats("lightStrength");

		numOfLights = lightStrengthData.length;

		lights = new ArrayList<PointLight>();

		for(int i = 0; i < numOfLights; i++) {
			lights.add(new PointLight(new Vector3f(lightCoordsData[i*3], lightCoordsData[i*3+1], lightCoordsData[i*3+2]),
					new Vector3f(lightColourData[i*3], lightColourData[i*3+1], lightColourData[i*3+2]), 
					lightStrengthData[i]));
		}

		cameraLocation = mapData.getFloats("cameraLocation");
		cameraRotation = mapData.getFloats("cameraRotation");

		cameraPos = new Vector3f(cameraLocation[0], cameraLocation[1], cameraLocation[2]);
		cameraRot = new Vector2f((float) Math.toRadians(cameraRotation[0]), (float) Math.toRadians(cameraRotation[1]));
		
		lockonPointsData = mapData.getFloats("lockonPoints");
		
		if (lockonPointsData != null) {
			numofLockonPoints = lockonPointsData.length / 3;
		
			lockonPoints = new ArrayList<Vector3f>();
			
			for(int i = 0; i < numofLockonPoints; i++) {
				lockonPoints.add(new Vector3f(lockonPointsData[i*3], lockonPointsData[i*3+1], lockonPointsData[i*3+2]));
			}

		}
		
		turnLimitsData = mapData.getShorts("cameraTurnLimits");
		
		if (turnLimitsData != null) {
			cameraTurnLimits = new Vector2f((float) Math.toRadians(turnLimitsData[0]), (float)Math.toRadians(turnLimitsData[1]));
		
		}
	}


	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		// Not called
	}


	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		int numOfObjects = objects.size();

		entities = new ArrayList<Entity>();

		for(int i = 0; i < numOfObjects; i++) {
			int modelIndex = objects.get(i);

			handler.newDependancy(new Entity(names.get(i), modelset.getModel(modelIndex), modelset.getTexturePath(modelIndex), coords.get(i), scales.get(i), rotations.get(i)));
		}
	}
	
	/*private void addNewEntityMapping(String modelName, Entity entity){
		LinkedList<Entity> entityList = modelNameToEntityMapings.get(modelName);
		if(entityList == null){
			entityList = new LinkedList<Entity>();
			modelNameToEntityMapings.put(modelName, entityList);
		}
		entityList.add(entity);
	}*/

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		// This will be called when all the entities are loaded.
	}	
	
	public ModelsetPlus getModelset() {
		return modelset;
	}
	
}
