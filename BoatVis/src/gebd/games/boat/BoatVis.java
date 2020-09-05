package gebd.games.boat;
import gebd.games.boat.lidar.LidarCalculationHandler;
import gebd.games.boat.lidar.LidarReading;
import gebd.games.boat.camera.CameraCalculationThread;
import blindmystics.input.CurrentInput;
import blindmystics.input.KeyboardInputLatchGenerator;
import blindmystics.util.FileReader;
import blindmystics.util.input.InputLatch;
import com.bulletphysics.linearmath.Transform;
import gebd.games.boat.BoatVis;
import gebd.games.boat.lidar.LidarHelper;
import gebd.games.boat.physics.PhysicsSettings;
import org.lwjgl.input.Keyboard;
import physics.util.PhysTransform;
import renderables.r2D.composite.DoubleTextArea;
import renderables.r2D.composite.TextButton;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.Button;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;
import renderables.r3D.water.WaterSettings;
import renderables.r3D.water.wave.WaveSettings;
import util.settings.SettingsHolderWithParents;
import gebd.games.boat.ui.PhysicsSettingsDisplay;
import javax.vecmath.*;
import java.awt.*;
import java.util.HashMap;
import blindmystics.input.CurrentInput;
import blindmystics.input.KeyboardInputLatchGenerator;
import blindmystics.util.FileReader;
import blindmystics.util.GLWrapper;
import blindmystics.util.input.InputLatch;
import blindmystics.util.input.keyboard.KeyboardModifierKey;
import blindmystics.util.input.mouse.ButtonStatus;
import blindmystics.util.input.mouse.InputStatus;
import com.bulletphysics.linearmath.Transform;
import composites.entities.Entity;
import composites.entities.EntityPositionHelper;
import gebd.GraphicalPicking;
import gebd.ModelsetPlus;
import gebd.Render;
import gebd.camera.Camera;
import gebd.camera.implementation.NoClipCamera;
import gebd.concurrent.ThreadDestroyer;
import gebd.games.boat.boat.BoatEntity;
import gebd.games.boat.boat.WamV;
import gebd.games.boat.camera.CameraCalculationThread;
import gebd.games.boat.entity.ColourSequenceHandler;
import gebd.games.boat.entity.pinger.PingerHandler;
import gebd.games.boat.input.BoatVisInput;
import gebd.games.boat.lidar.LidarCalculationHandler;
import gebd.games.boat.lidar.LidarHelper;
import gebd.games.boat.lidar.vis.LidarVisualRepresentation;
import gebd.games.boat.lidar.vis.VisPixelPositions;
import gebd.games.boat.physics.BoatVisPhysics;
import gebd.games.boat.scene.BoatVisScene;
import gebd.games.boat.ui.AddBuoyDislpay;
import gebd.games.boat.ui.PhysicsSettingsDisplay;
import gebd.games.boat.ui.SkyboxSettingsDislpay;
import gebd.games.boat.ui.WaterSettingsUI;
import gebd.games.boat.ui.topnav.TopNavigationBar;
import gebd.games.boat.ui.topnav.colourseq.ColourSequenceDropdown;
import gebd.light.PointLight;
import gebd.shaders.Shader;
import gebd.shaders.Shader2D;
import gebd.shaders.Shader3D;
import gebd.shaders.quat.Textured3DQuatShader;
import gebd.shaders.shaders.Picking3DShader;
import gebd.shaders.shaders.Textured2DShader;
import gebd.shaders.shaders.Textured3DShader;
import gebd.shaders.shaders.lidar.LiDAR3DShader;
import gebd.shaders.shaders.lidar.LiDARQuatShader;
import gebd.shaders.shaders.skybox.SkyboxShader;
import gebd.shaders.shaders.water.WaterShader;
import loader.LoadedObjectHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import javax.vecmath.*;

import physics.util.PhysTransform;
import renderables.framebuffer.FrameBufferObject;
import renderables.r2D.Quad;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.Button;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;
import renderables.r3D.model.ModelsetModel;
import renderables.r3D.skybox.RawModel;
import renderables.r3D.skybox.Skybox;
import renderables.r3D.water.RenderedWater;
import renderables.r3D.water.wave.GeneratedMeshGrid;
import renderables.texture.TextureInfo;
import renderables.texture.generated.SolidFillTextureHandler;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

import static gebd.games.boat.boat.WamV.DEFAULT_ANGULAR_DAMPING;
import static gebd.games.boat.boat.WamV.DEFAULT_LINEAR_DAMPING;
import static gebd.games.boat.input.BoatVisInput.FULL_SCREEN_TOGGLE;
import static gebd.games.boat.input.BoatVisInput.TOGGLE_UI_HANDLER;


public class BoatVis extends Render {

	private static String[] programArgs = new String[0];

	public static void main(String[] args) {
		programArgs = args;
		Render.setRenderer(new BoatVis());
	}

	public BoatVis() {
		FileReader.RESOURCES_FILES_DIRECTORY_PREFIX = "BoatVis/";
		FileReader.COMMON_FILES_DIRECTORY = "MysticDesktop/res/";
	}

	public BoatVis(String resourcesFileDirectoryLocation) {
		FileReader.RESOURCES_FILES_DIRECTORY_PREFIX = resourcesFileDirectoryLocation;
		FileReader.COMMON_FILES_DIRECTORY = "MysticDesktop/res/";
	}

	//	protected Terrain test;

	protected Textured3DShader shader3D;
	protected Textured2DShader shader2D;
	protected SkyboxShader skyboxShader;
	protected WaterShader waterShader;

	protected LiDARQuatShader lidarShader3D;

//	protected VisualGrid visGridTest;≤
//	protected ChunkGrid grid;
//	protected ChunkVisEntity visTest;

	private int timeSinceLastPing = 0;

	private int timeBetweenPings = 200; //ms

	protected Entity cubeTestMousePointer;

	protected Entity entityFollowingMouse = null;
	protected Vector3f mouseWaterLocation = new Vector3f();

	protected Entity cameraPlaceholder;

	protected CameraCalculationThread cameraCalculationThread;
	//TODO - Modifiable?
	public static final int boatCameraResolutionWidth = 640;
	public static final int boatCameraResolutionHeight = 480;

	protected BoatEntity boatEntity;
	protected BoatEntity physicsBoatEntity;
	protected Entity cubeEntity;
	protected Entity cylinderEntity;
	protected Entity bridgeTest;
	protected Entity velodyneEntity;

	protected Entity goldCreekLakebed;
	protected Entity goldCreekTreeline;
	private boolean useGoldCreekDamScene = false;

	private Entity northSky1;
	private Entity northSky2;
	private Entity northSky3;

	protected Skybox skyboxTest;

	protected ModelsetModel quadUpModel;
	protected ModelsetModel waterQuadModel;
	protected TextureInfo tempWaterQuadTexure;

	protected RenderedWater ocean;
	protected RenderedWater reducedSizeOcean;

	protected GeneratedMeshGrid generatedMeshGrid;
	protected Entity generatedMeshGridTest;

	protected Camera freeFlyCamera;
	protected Camera boatMountedCamera;
	protected FrameBufferObject secondaryCameraViewFBO;
	protected SimpleQuad secondaryCameraDisplay;

	protected ModelsetPlus modelset;

	protected SimpleQuad reflectionTextureTest;
	protected SimpleQuad refractionTextureTest;
	protected SimpleQuad refractionDepthTextureTest;
	protected String2D fpsDisplay;

	protected SimpleQuad graphicalPickingVis;

	protected WaterSettingsUI waterSettingsUIDisplay;
	protected AddBuoyDislpay addBouyDisplay;
	protected SkyboxSettingsDislpay skyboxSettingsDislpay;
	protected PhysicsSettingsDisplay physicsSettingsDisplay;
	protected TopNavigationBar topNavigationBar;

	protected renderables.r2D.simple.Button expandWaterSettingsButton;
	protected renderables.r2D.simple.Button expandAddBouyDisplayButton;
	protected renderables.r2D.simple.Button expandSkyboxDisplayButton;
	protected renderables.r2D.simple.Button expandPhysicsSettingsDisplayButton;

	private ArrayList<Button> expandButtons = new ArrayList<>();


	protected Vector3f cameraPosition = new Vector3f(0, 3, 0);
	protected float cameraTheta;
	protected float cameraPhi;
	private VisState visState = VisState.SCENE_VIEW;

	protected Vector2f componentSize = new Vector2f(Render.WIDTH / 2f, Render.WIDTH / 2f);

	private UserInterface positionParentLidarVis;
	private Camera[] lidarCameras = new Camera[LidarHelper.DepthBufferDirection.values().length];
	private LidarVisualRepresentation[] lidarVisualRepresentations = new LidarVisualRepresentation[LidarHelper.DepthBufferDirection.values().length];
	private VisPixelPositions[] lidarVisualPixelIndicies = new VisPixelPositions[LidarHelper.DepthBufferDirection.values().length];
	private int lidarRenderIndex = 0;
	private int lidarStoreIndex = 0;

	protected SimpleQuad fifteenMinTimerQuad;
	protected long presentationStartTime = System.currentTimeMillis();
	protected boolean lowerFramerateForSpeech = false;
	protected InputLatch startPresentationLatch = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_J, KeyboardModifierKey.ALT, KeyboardModifierKey.CTRL, KeyboardModifierKey.SHIFT);
	protected InputLatch alterFramerateLatch = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_L, KeyboardModifierKey.ALT, KeyboardModifierKey.CTRL, KeyboardModifierKey.SHIFT);
	private Vector2f mouseHeldStartPositionLidar = new Vector2f();

	protected BoatVisScene boatVisScene = new BoatVisScene();
	protected BoatVisScene boatVisSceneLoading = null;

	protected LidarHelper lidarHelper;
	protected BoatVisPhysics physicsHandler;

	protected boolean shouldSetBoatPosition = false;

	protected ColourSequenceHandler colourSequenceHandler = new ColourSequenceHandler();

	public LidarHelper getLidarHelper() {
		return lidarHelper;
	}

	public Skybox getSkybox() {
		return skyboxTest;
	}

	public ColourSequenceDropdown getColourSequenceDropdown() {
		return topNavigationBar.getColourSequenceDropdown();
	}

	public enum VisState {
		SCENE_VIEW,
		SCENE_VIEW_WITH_CAM,
		BOAT_CAM,
		LIDAR_VIEW,
	}

	/**
	 * NOTE - OpenGL is NOT initialized here, so don't do anything OpenGL related here.
	 */
	@Override
	public void initialize() {
		Render.instance.setWindowTitle("Boat Vis of HeZe's version");
		System.out.println("HeZe's version of simulation is starting...");
		setDisplayMode(new DisplayMode(WIDTH, HEIGHT), false);
//		setDisplayMode(new DisplayMode(1920, 1080), false);
	}

	@Override
	protected void setupShaders(){
		// Load the shaders
		//3D

		int vsId_Textured3D = loadShader(FileReader.asSharedFile("shared/shaders/3d/textured/vertex_quat.glsl"), GL20.GL_VERTEX_SHADER);
		int fsId_Textured3D = loadShader(FileReader.asSharedFile("shared/shaders/3d/textured/fragment.glsl"), GL20.GL_FRAGMENT_SHADER);
//		int fsId_Textured3D = loadShader("res/shared/shaders/lidar/fragment.glsl", GL20.GL_FRAGMENT_SHADER);
		shader3D = initializeShader(new Textured3DQuatShader(vsId_Textured3D, fsId_Textured3D));


		//TODO - Optimise the LiDAR shader.
		int vsId_Lidar3D = loadShader(FileReader.asSharedFile("shared/shaders/lidar/vertex_quat.glsl"), GL20.GL_VERTEX_SHADER);
		//int vsId_Lidar3D = loadShader(FileReader.asSharedFile("shared/shaders/lidar/test/vertex_quat_simple.glsl"), GL20.GL_VERTEX_SHADER); //This was for testing purposes.
		int fsId_Lidar3D = loadShader(FileReader.asSharedFile("shared/shaders/lidar/fragment.glsl"), GL20.GL_FRAGMENT_SHADER);
		lidarShader3D = initializeShader(new LiDARQuatShader(vsId_Lidar3D, fsId_Lidar3D));

		//2D
		int vsId_Textured2D = loadShader(FileReader.asSharedFile("shared/shaders/textured_and_coloured/vertex.glsl"), GL20.GL_VERTEX_SHADER);
		int fsId_Textured2D = loadShader(FileReader.asSharedFile("shared/shaders/textured_and_coloured/fragment.glsl"), GL20.GL_FRAGMENT_SHADER);
		shader2D = initializeShader(new Textured2DShader(vsId_Textured2D, fsId_Textured2D));

		//Skybox
		int vsId_Skybox = loadShader(FileReader.asSharedFile("shared/shaders/skybox/vertex.glsl"), GL20.GL_VERTEX_SHADER);
		int fsId_Skybox = loadShader(FileReader.asSharedFile("shared/shaders/skybox/fragment.glsl"), GL20.GL_FRAGMENT_SHADER);
		skyboxShader = initializeShader(new SkyboxShader(vsId_Skybox, fsId_Skybox));

		//Water Shader
		int vsId_Water = loadShader(FileReader.asSharedFile("shared/shaders/water/vertex.glsl"), GL20.GL_VERTEX_SHADER);
		int fsId_Water = loadShader(FileReader.asSharedFile("shared/shaders/water/fragment.glsl"), GL20.GL_FRAGMENT_SHADER);
		waterShader = initializeShader(new WaterShader(vsId_Water, fsId_Water));

		//Graphical Picking.
		//TODO - Check that this is correct.
		int vsId_GraphicalPicking = loadShader(FileReader.asSharedFile("mystic/shaders/3d/graphicalPickingPerspective/vertex.glsl"), GL20.GL_VERTEX_SHADER);
		int fsId_GraphicalPicking = loadShader(FileReader.asSharedFile("mystic/shaders/3d/graphicalPickingPerspective/fragment.glsl"), GL20.GL_FRAGMENT_SHADER);

		Picking3DShader pickingShader3D = initializeShader(new Picking3DShader(vsId_GraphicalPicking, fsId_GraphicalPicking));

		GraphicalPicking.setShader(pickingShader3D);
	}

	@Override
	public Shader3D getDefault3DShader() {
		return shader3D;
	}

	@Override
	public Shader2D getDefault2DShader() {
		return shader2D;
	}

	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		System.out.println("load data from file");
		modelset = BoatModels.getInstance();
		modelset.loadRawDataFromFile(null);
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		System.out.println("handle raw data");
		modelset.handleRawData(null);
	}

	private void addNewRandomSceneObject(LoadedObjectHandler<?> handler, String objectName, ModelsetModel objectModel) {
		Vector3f position = new Vector3f();
		position.x = (float) Math.random() * 100;
		position.z = (float) Math.random() * 100;

		Vector3f size = new Vector3f();
		size.x = (float) (Math.random() * 2.5 + 0.5);
		size.y = (float) (Math.random() * 2.5 + 0.5);
		size.z = (float) (Math.random() * 2.5 + 0.5);

		Vector3f rotation = new Vector3f();
		rotation.x = (float) (Math.random() * 2 * Math.PI);
		rotation.y = (float) (Math.random() * 2 * Math.PI);
		rotation.z = (float) (Math.random() * 2 * Math.PI);

		Entity entity = handler.newDependancy(new Entity(objectName, objectModel, SolidFillTextureHandler.getSolidFillTexture(), position, size, rotation));

		Vector3f textureBlend = new Vector3f();
		textureBlend.x = (float) Math.random();
		textureBlend.y = (float) Math.random();
		textureBlend.z = (float) Math.random();

		entity.setTextureBlendColour(textureBlend);
		entity.setTextureBlendAmount(1f);
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		System.out.println("load dependancies");
		int gridWidth = 200;
		int gridHeight = 200;
		generatedMeshGrid = modelset.loadGeneratedModel(new GeneratedMeshGrid(gridWidth, gridHeight));
		modelset.completeLoad(null);

		String default_texture = FileReader.asSharedFile("shared/textures/default_texture.png");
//		modelset.load();
//		test = new Terrain();
//		test.setupModel();

//		visTest = handler.newDependancy(new ChunkVisEntity(new Vector3f(0, -1, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0)));




		String boatModelName = BoatModels.ModelName.WAMV_PHYS.name();
		ModelsetModel boatModel = modelset.getModel(boatModelName, true);
		String texturePath = modelset.getTexturePath(boatModelName);
		boatEntity = handler.newDependancy(new BoatEntity(boatModelName, boatModel, texturePath, new Vector3f(0, 0.72f, 0), new Vector3f(1f, 1f, 1f), new Vector3f(0, 0, 0), this));
		boatEntity.setUsePicking(false);
		physicsBoatEntity = handler.newDependancy(new BoatEntity(boatModelName, boatModel, texturePath, new Vector3f(0, 0.72f, 0), new Vector3f(1f, 1f, 1f), new Vector3f(0, 0, 0), this));
		physicsBoatEntity.setUsePicking(false);

		String cubeModelName = BoatModels.ModelName.CUBE_TEST.name();
		ModelsetModel cubeModel = modelset.getModel(cubeModelName, true);
		String cubeTexturePath = modelset.getTexturePath(cubeModelName);

		cubeEntity = handler.newDependancy(new Entity(cubeModelName, cubeModel, default_texture, new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f), new Vector3f(0, 0, 0)));
		cubeEntity.setUsePicking(false);//

		String cylinderModelName = BoatModels.ModelName.UNIT_CYLINDER.name();
		ModelsetModel cylinderModel = modelset.getModel(cylinderModelName, true);
		//String cylinderTexturePath = modelset.getTexturePath(cylinderModelName);
		cylinderEntity = handler.newDependancy(new Entity(cylinderModelName, cylinderModel, default_texture, new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f), new Vector3f(0, 0, 0)));

		//protected Entity cubeEntity;
		//protected Entity cylinderEntity;

		String sphereModelName = BoatModels.ModelName.SHPERE.name();
		ModelsetModel sphereModel = modelset.getModel(sphereModelName, true);
		String sphereTexturePath = modelset.getTexturePath(sphereModelName);

		String velodyneModelName = BoatModels.ModelName.VELODYNE_HDE32E.name();
		ModelsetModel velodyneModel = modelset.getModel(velodyneModelName, true);
		String velodyneTexturePath = modelset.getTexturePath(velodyneModelName);
		velodyneEntity = handler.newDependancy(new Entity(velodyneModelName, velodyneModel, velodyneTexturePath,
				new Vector3f(), new Vector3f(1, 1, 1), new Vector3f()));
		velodyneEntity.setUsePicking(false);//

		//TODO - Remove me later! [START]
		{
			String buoyStandardModelName = BoatModels.ModelName.BUOY_STANDARD.name();
			ModelsetModel buoyStandardModel = modelset.getModel(buoyStandardModelName, true);

			String buoyTaperedModelName = BoatModels.ModelName.BUOY_TAPERED.name();
			ModelsetModel buoyTaperedModel = modelset.getModel(buoyTaperedModelName, true);

			final int numSceneObjects = 0;

			for (int i = 0; i < (numSceneObjects / 4); i++) {

				//Add the 4 types.
				addNewRandomSceneObject(handler, buoyStandardModelName, buoyStandardModel);
				addNewRandomSceneObject(handler, buoyTaperedModelName, buoyTaperedModel);
				addNewRandomSceneObject(handler, cylinderModelName, cylinderModel);
				addNewRandomSceneObject(handler, sphereModelName, sphereModel);

			}
		}
		//TODO - Remove me later! [END]



		/*generatedMeshGridTest = handler.newDependancy(new Entity("Generated Meshgrid test", generatedMeshGrid, cubeTexturePath,
				new Vector3f(0, 3, -3), new Vector3f(120f, 1f, 120f), new Vector3f(0, 0, 0)));
		*/

		cubeTestMousePointer = handler.newDependancy(new Entity(cubeModelName, cubeModel, cubeTexturePath, new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.5f, 0.1f), new Vector3f(0, 0, 0)));
		entityFollowingMouse = cubeTestMousePointer;
		cubeTestMousePointer.setVisible(false);

//		cubeTest1;
//		protected Entity cubeTest2;
//		protected Entity cubeTest3

		String bridgeModelName = BoatModels.ModelName.BRIDGE_TEST.name();
		ModelsetModel bridgeModel = modelset.getModel(bridgeModelName, true);
		String bridgeTexturePath = modelset.getTexturePath(bridgeModelName);

		//baseDir + "entities/bridge.png"
		//res/boat/
		bridgeTexturePath = FileReader.asResource("res/boat/models/bridge.png");
		//bridgeTest = handler.newDependancy(new Entity(bridgeModelName, bridgeModel, bridgeTexturePath, new Vector3f(-10, 0, -10), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f()));

		Vector3f sizeModifierLakebed = new Vector3f(100, 100, 100);
		Vector3f sizeModifierTreeline = new Vector3f(100, 200, 100);
//		Vector3f relativePostion = new Vector3f(-50, -4, -400);
//		Vector3f lackbedRotation = new Vector3f(0f, 0f, 0f);

//		Vector3f relativePostion = new Vector3f(-180f, -4f, 52f);
//		Vector3f lackbedRotation = new Vector3f((float) (-Math.PI / 2.0), 0f, 0f);

		Vector3f relativePostion = new Vector3f(-180f, -4f, 52f);
		Vector3f lackbedRotation = new Vector3f((float) (Math.PI * (1.0 / 2.0)), 0f, 0f);

		String goldCreekLakeBedModelName = BoatModels.ModelName.GOLD_CREEK_LAKE_BED.name();
		ModelsetModel goldCreekLakeBedModel = modelset.getModel(goldCreekLakeBedModelName, true);
		String goldCreekLakeBedTexturePath = modelset.getTexturePath(goldCreekLakeBedModelName);
		goldCreekLakebed = handler.newDependancy(new Entity(
				goldCreekLakeBedModelName, goldCreekLakeBedModel, goldCreekLakeBedTexturePath,
				relativePostion, sizeModifierLakebed, lackbedRotation)
		);
		goldCreekLakebed.setVisible(useGoldCreekDamScene);
		goldCreekLakebed.setUsePicking(false);

		String goldCreekTreeLineModelName = BoatModels.ModelName.GOLD_CREEK_TREELINE.name();
		ModelsetModel goldCreekTreeLineModel = modelset.getModel(goldCreekTreeLineModelName, true);
		String goldCreekTreeLineTexturePath = modelset.getTexturePath(goldCreekTreeLineModelName);
		goldCreekTreeline = handler.newDependancy(new Entity(
				goldCreekTreeLineModelName, goldCreekTreeLineModel, goldCreekTreeLineTexturePath,
				relativePostion, sizeModifierTreeline, lackbedRotation)
		);
		goldCreekTreeline.setVisible(useGoldCreekDamScene);
		goldCreekTreeline.setUsePicking(false);


		float lengthOfArrow = 80f;
		float heightOfArrow = 30f;
		northSky1 = handler.newDependancy(new Entity(
				cylinderModelName,
				cylinderModel,
				SolidFillTextureHandler.getSolidFillTexture(),
				new Vector3f(0, heightOfArrow, -lengthOfArrow / 2f),
				new Vector3f(0.5f, lengthOfArrow / 2f, 0.5f),
				new Vector3f((float) (Math.PI / 2.0), (float) (Math.PI / 2.0), 0)));
		northSky1.setUsePicking(false);

		float arrowTipLength = 5f;
		northSky2 = handler.newDependancy(new Entity(
				cylinderModelName,
				cylinderModel,
				SolidFillTextureHandler.getSolidFillTexture(),
				new Vector3f((float) (arrowTipLength * Math.sin(Math.PI / 5.0)), heightOfArrow, (float) (-(lengthOfArrow) + arrowTipLength * Math.cos(Math.PI / 5.0))),
				new Vector3f(0.5f, arrowTipLength, 0.5f),
				new Vector3f((float) (Math.PI / 2.0 + Math.PI / 5.0), (float) (Math.PI / 2.0), 0)));
		northSky2.setUsePicking(false);

		northSky3 = handler.newDependancy(new Entity(
				cylinderModelName,
				cylinderModel,
				SolidFillTextureHandler.getSolidFillTexture(),
				new Vector3f((float) (-arrowTipLength * Math.sin(Math.PI / 5.0)), heightOfArrow, (float) (-(lengthOfArrow) + arrowTipLength * Math.cos(Math.PI / 5.0))),
				new Vector3f(0.5f, arrowTipLength, 0.5f),
				new Vector3f((float) (Math.PI / 2.0 - Math.PI / 5.0), (float) (Math.PI / 2.0), 0)));
		northSky3.setUsePicking(false);

		Vector3f colourOfArrow = new Vector3f(1f, 0f, 0f);
		northSky1.setTextureBlendColour(colourOfArrow);
		northSky1.setTextureBlendAmount(1f);
		northSky2.setTextureBlendColour(colourOfArrow);
		northSky2.setTextureBlendAmount(1f);
		northSky3.setTextureBlendColour(colourOfArrow);
		northSky3.setTextureBlendAmount(1f);


		//cylinderDirectionTest = handler.newDependancy(new Entity(sphereModelName, sphereModel, sphereTexturePath, new Vector3f(0, 0, 0), new Vector3f(4, 4, 4), new Vector3f()));


		String cameraModelName = BoatModels.ModelName.CAMERA_PLACEHOLDER.name();
		ModelsetModel cameraModel = modelset.getModel(cameraModelName, true);
		String cameraTexturePath = modelset.getTexturePath(cameraModelName);
		cameraPlaceholder = handler.newDependancy(new Entity(cameraModelName, cameraModel, cameraTexturePath, new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f()));
		cameraPlaceholder.setUsePicking(false);


		String simpleQuadUpModelName = BoatModels.ModelName.QUAD_UP.name();
		quadUpModel = modelset.getModel(simpleQuadUpModelName, true);


		String quadUpModelName = BoatModels.ModelName.WATER_WITH_CENTER_HOLE.name();
		waterQuadModel = modelset.getModel(quadUpModelName, true);
		tempWaterQuadTexure = TextureInfo.queueLoadOfPNGTexture(modelset.getTexturePath(quadUpModelName), handler);


		int fullSizeWaterWidth = Render.WIDTH / 2;
		int fullSizeWaterHeight = Render.HEIGHT / 2;
		//ocean = handler.newDependancy(new RenderedWater(waterShader, waterQuadModel, this, fullSizeWaterWidth, fullSizeWaterHeight));
		ocean = handler.newDependancy(new RenderedWater(waterShader, generatedMeshGrid, waterQuadModel, quadUpModel, this, fullSizeWaterWidth, fullSizeWaterHeight));

		int reducedSizeWaterWidth = Render.WIDTH / 4;
		int reducedSizeWaterHeight = Render.HEIGHT / 4;
		//reducedSizeOcean = handler.newDependancy(new RenderedWater(waterShader, waterQuadModel, this, reducedSizeWaterWidth, reducedSizeWaterHeight));
		reducedSizeOcean = handler.newDependancy(new RenderedWater(waterShader, generatedMeshGrid, waterQuadModel, quadUpModel, this, reducedSizeWaterWidth, reducedSizeWaterHeight));

		secondaryCameraViewFBO = new FrameBufferObject(boatCameraResolutionWidth, boatCameraResolutionHeight);
		Vector2f secondaryCameraDisplaySize = new Vector2f(boatCameraResolutionWidth, boatCameraResolutionHeight);
		cameraCalculationThread = new CameraCalculationThread(boatCameraResolutionWidth, boatCameraResolutionHeight);
		ThreadDestroyer.registerAndStartDestroyableThread(cameraCalculationThread);


		Vector2f secondaryCameraDisplayPosition = new Vector2f();
		secondaryCameraDisplayPosition.x = Render.WIDTH - (secondaryCameraDisplaySize.x / 2f);
		secondaryCameraDisplayPosition.y = Render.HEIGHT - (secondaryCameraDisplaySize.y / 2f);
		secondaryCameraDisplay = rootUI.addComponentToTop(new SimpleQuad(secondaryCameraViewFBO.getTexture(), secondaryCameraDisplayPosition, secondaryCameraDisplaySize, 0));
		secondaryCameraDisplay.setTextureIsFlippedVertically(true);
		secondaryCameraDisplay.setVisible(true);

//		quadUpTest = handler.newDependancy(new Entity(quadUpModelName, quadUpModel, quadUpTexturePath, new Vector3f(2, 0, 0), new Vector3f(1f, 1f, 1f), new Vector3f(0, 0, 0)));


//		boatTest = handler.newDependancy(new Entity("res/boat/testBoat.ent", "res/boat/boatTexture.png", new Vector3f(0, 0.95f, 0), new Vector3f(1f, 1f, 1f), new Vector3f(0, 0, 0)));



		reflectionTextureTest = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(160, 90), new Vector2f(320, 180), 0));
		reflectionTextureTest.setTexture(ocean.getReflectionFBO().getTexture());
		reflectionTextureTest.setTextureIsFlippedVertically(true);
		reflectionTextureTest.setVisible(false);

		refractionTextureTest = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(320 + 160, 90), new Vector2f(320, 180), 0));
		refractionTextureTest.setTexture(ocean.getRefractionFBO().getTexture());
		refractionTextureTest.setTextureIsFlippedVertically(true);
		refractionTextureTest.setVisible(false);

		refractionDepthTextureTest = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(640 + 160, 90), new Vector2f(320, 180), 0));
		refractionDepthTextureTest.setTexture(ocean.getRefractionFBO().getDepthTextureInfo());
		refractionDepthTextureTest.setTextureIsFlippedVertically(true);
		refractionDepthTextureTest.setVisible(false);

		graphicalPickingVis = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(400, 400), new Vector2f(320, 180), 0));
		graphicalPickingVis.setTextureIsFlippedVertically(true);
		graphicalPickingVis.setVisible(true);

		skyboxTest = handler.newDependancy(new Skybox(skyboxShader));


		lidarHelper = new LidarHelper();
		setLidarRotationSpeed(LidarHelper.LidarRotationSpeed.Hz10);


/*
		float xPosition = 0;
		float yPosition = (componentSize.y / 2f);
		lidarTextureTestBackLeftHalf = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(xPosition, yPosition), componentSize, 0));
		lidarTextureTestBackLeftHalf.setTexture(lidarTextureBack.getTexture());
		lidarTextureTestBackLeftHalf.setTextureIsFlippedVertically(true);

		xPosition += componentSize.x;
		lidarTextureTestLeft = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(xPosition, yPosition), componentSize, 0));
		lidarTextureTestLeft.setTexture(lidarTextureLeft.getTexture());
		lidarTextureTestLeft.setTextureIsFlippedVertically(true);

		xPosition += componentSize.x;
		lidarTextureTestForward = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(xPosition, yPosition), componentSize, 0));
		lidarTextureTestForward.setTexture(lidarTextureForward.getTexture());
		lidarTextureTestForward.setTextureIsFlippedVertically(true);

		xPosition += componentSize.x;
		lidarTextureTestRight = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(xPosition, yPosition), componentSize, 0));
		lidarTextureTestRight.setTexture(lidarTextureRight.getTexture());
		lidarTextureTestRight.setTextureIsFlippedVertically(true);

		xPosition += componentSize.x;
		lidarTextureTestBackRightHalf = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(xPosition, yPosition), componentSize, 0));
		lidarTextureTestBackRightHalf.setTexture(lidarTextureBack.getTexture());
		lidarTextureTestBackRightHalf.setTextureIsFlippedVertically(true);
*/

//		protected String2D lidarDisplayTextForward;
//		protected String2D lidarDisplayTextRight;
//		protected String2D lidarDisplayTextBack;
//		protected String2D lidarDisplayTextLeft;



		for (int i = 0; i < 4; i++) {
			//Rotation and position set at runtime based on boat position.
			lidarCameras[i] = new NoClipCamera(new Vector3f(), 0, 0);
			lidarCameras[i].getProjectionMatrixHandler().setNearPlane(0.1f);
			lidarCameras[i].getProjectionMatrixHandler().setFarPlane(100f);



			lidarCameras[i].getProjectionMatrixHandler().setAspectRatio(LidarHelper.LidarRotationSpeed.Hz10.getAspectRatio());
			lidarCameras[i].getProjectionMatrixHandler().setFieldOfView(LidarHelper.LidarRotationSpeed.Hz10.getFieldOfView());
			lidarCameras[i].calculateRotationMatrix();
		}


		positionParentLidarVis = new UserInterface(Render.getCentreOfScreen(), new Vector2f(1f, 1f), 0);

		//float xPosition = Render.WIDTH - (componentSize.x / 2f);
		//float yPosition = Render.HEIGHT - (componentSize.y / 2f);
		float xPosition = 0;
		float yPosition = 0;
		for (int i = 0; i < lidarVisualRepresentations.length; i++) {
			Vector2f position = new Vector2f(xPosition, yPosition);
			lidarVisualRepresentations[i] = positionParentLidarVis.addComponentToTop(new LidarVisualRepresentation(position, componentSize));

			//This is used for visualizing the points on the screen indexed by the LiDAR.
			LidarHelper.DepthBufferDirection depthBufferDirection = LidarHelper.DepthBufferDirection.values()[i];
			lidarVisualPixelIndicies[i] = positionParentLidarVis.addComponentToTop(new VisPixelPositions(position, componentSize, 0f, lidarHelper, depthBufferDirection));

			xPosition -= componentSize.x;
			//yPosition -= componentSize.y;
		}


		rootUI.addComponentToTop(positionParentLidarVis);

		setupUI(handler);
		setupRootUI();

	}



	private void setupUI(LoadedObjectHandler<?> handler){
		//TODO - Remove or revise.

		Vector2f waterSettingsSize = new Vector2f(300, Render.HEIGHT);
		Vector2f waterSettingsPosition = new Vector2f(Render.WIDTH - (waterSettingsSize.x / 2f), Render.HEIGHT / 2f);
		waterSettingsUIDisplay = rootUI.addComponentToTop(new WaterSettingsUI(this, waterSettingsPosition, waterSettingsSize, 0, boatVisScene.getWaterSettings()));
		waterSettingsUIDisplay.setVisible(false);

		Vector2f buttonSize = new Vector2f(25, 100);
		Vector2f expandWaterSettingsButtonPosition = new Vector2f(Render.WIDTH - (buttonSize.x / 2f), Render.HEIGHT / 2f - (buttonSize.y * 2f));
		expandWaterSettingsButton = rootUI.addComponentToTop(new Button(FileReader.asResource("res/boat/textures/ExpandThing.png"), expandWaterSettingsButtonPosition, buttonSize, 0));
		expandWaterSettingsButton.setTextureIsFlippedHorizontally(true);
		expandWaterSettingsButton.setVisible(true);

		Vector2f addBouyDisplaySize = new Vector2f(300, Render.HEIGHT);
		Vector2f addBouyDisplayPosition = new Vector2f(Render.WIDTH - (waterSettingsSize.x / 2f), Render.HEIGHT / 2f);
		addBouyDisplay = rootUI.addComponentToTop(new AddBuoyDislpay(addBouyDisplayPosition, addBouyDisplaySize, this));
		addBouyDisplay.setVisible(false);

		Vector2f addBouyDisplayButtonPosition = new Vector2f(Render.WIDTH - (buttonSize.x / 2f), Render.HEIGHT / 2f);
		expandAddBouyDisplayButton = rootUI.addComponentToTop(new Button(FileReader.asResource("res/boat/textures/ExpandThing.png"), addBouyDisplayButtonPosition, buttonSize, 0));
		expandAddBouyDisplayButton.setTextureIsFlippedHorizontally(true);
		expandAddBouyDisplayButton.setVisible(true);


		Vector2f skyboxDisplaySize = new Vector2f(300, Render.HEIGHT);
		Vector2f skyboxDisplayPosition = new Vector2f(Render.WIDTH - (waterSettingsSize.x / 2f), Render.HEIGHT / 2f);
		skyboxSettingsDislpay = rootUI.addComponentToTop(new SkyboxSettingsDislpay(skyboxDisplayPosition, skyboxDisplaySize, this));
		skyboxSettingsDislpay.setVisible(false);

		Vector2f skyboxDisplayButtonPosition = new Vector2f(Render.WIDTH - (buttonSize.x / 2f), Render.HEIGHT / 2f + 300);
		expandSkyboxDisplayButton = rootUI.addComponentToTop(new Button(FileReader.asResource("res/boat/textures/ExpandThing.png"), skyboxDisplayButtonPosition, buttonSize, 0));
		expandSkyboxDisplayButton.setTextureIsFlippedHorizontally(true);
		expandSkyboxDisplayButton.setVisible(true);


		Vector2f physicsSettingsDisplaySize = new Vector2f(300, Render.HEIGHT);
		Vector2f physicsSettingsDisplayPosition = new Vector2f(Render.WIDTH - (waterSettingsSize.x / 2f), Render.HEIGHT / 2f);
		physicsSettingsDisplay = rootUI.addComponentToTop(new PhysicsSettingsDisplay(
				physicsSettingsDisplayPosition,
				physicsSettingsDisplaySize,
				0f, this, boatVisScene.getPhysicsSettings()
		));
		physicsSettingsDisplay.setVisible(false);

		Vector2f physicsSettingsDisplayButtonPosition = new Vector2f(Render.WIDTH - (buttonSize.x * (1f / 2f)), Render.HEIGHT / 2f + (buttonSize.y * 2f));
		expandPhysicsSettingsDisplayButton = rootUI.addComponentToTop(new Button(FileReader.asResource("res/boat/textures/ExpandThing.png"), physicsSettingsDisplayButtonPosition, buttonSize, 0));
		expandPhysicsSettingsDisplayButton.setTextureIsFlippedHorizontally(true);
		expandPhysicsSettingsDisplayButton.setVisible(true);



		Vector2f reduceLidarButtonSize = new Vector2f(50, 50);
		Vector2f reduceLidarButtonPosition = new Vector2f(25, 25);
		Button reduceLidarButton = rootUI.addComponentToTop(new Button(FileReader.asSharedFile("test_images/Buttonify.png"), reduceLidarButtonPosition, reduceLidarButtonSize, 0){
			@Override
			protected void onRelease() {
				for (int i = 0; i < LidarHelper.DepthBufferDirection.values().length; i++) {
					lidarVisualRepresentations[i].toggleVisiblity();
				}

			}
		});

		fifteenMinTimerQuad = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(Render.WIDTH / 2f, Render.HEIGHT - 5), new Vector2f(10, 10), 0));
		fifteenMinTimerQuad.setVisible(false);


//		protected SimpleQuad lidarTextureTestBackLeftHalf;
//		protected SimpleQuad lidarTextureTestLeft;
//		protected SimpleQuad lidarTextureTestForward;
//		protected SimpleQuad lidarTextureTestRight;
//		protected SimpleQuad lidarTextureTestBackRightHalf;




/*		float startYPos = (componentSize.y / 2f) + (componentSize.y / 8);
		float endYPos = (componentSize.y / 2f) - 3 * (componentSize.y / 8);
		TimerTask task=new TimerTask(){
			public void run(){
				System.out.printf("xPosition:%f    ",componentSize.x-640);
				System.out.printf("yPosition:%f\n",componentSize.y-640);
			}
		};
		Timer timer=new Timer();
		long delay=5000;
		long intevalPeriod=5*1000;
		timer.scheduleAtFixedRate(task,delay,intevalPeriod);
*/
        /*

		for (int channelNo = 0; channelNo < 32; channelNo++) {
			float percentageY = ((float) channelNo) / (32f);
			float currY = startYPos + ((endYPos - startYPos) * percentageY);
			SimpleQuad velodyneChannel = rootUI.addComponentToTop(new SimpleQuad(new Vector2f(Render.WIDTH / 2f, currY), new Vector2f(Render.WIDTH, 1), 0));
			velodyneChannel.setBlend(new Vector4f(1, 0, 0, 1), 1);
		}

        */

		topNavigationBar = rootUI.addComponentToTop(new TopNavigationBar(this));

		Vector2f sizeTemp = new Vector2f(16, 16);
		Vector2f fpsDisplayPosition = new Vector2f(WIDTH - 150, HEIGHT - (sizeTemp.y * 1.5f));
		fpsDisplay = rootUI.addComponentToTop(new String2D("  FPS: ???", String2D.StringAlignment.MID_LEFT, fpsDisplayPosition, sizeTemp, "Courier", Font.PLAIN));
	}


	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		System.out.println("HeZe's version complete load!");

		ambientLightIntensity = 0.6f;

		expandButtons.add(expandWaterSettingsButton);
		expandButtons.add(expandAddBouyDisplayButton);
		expandButtons.add(expandPhysicsSettingsDisplayButton);
		expandButtons.add(expandSkyboxDisplayButton);


		float padding = 5; //px
		Vector2f buttonSize = new Vector2f(25, 100);

		//Setup all the button positions.
		for (int i = 0; i < expandButtons.size(); i++) {
			Button button = expandButtons.get(i);

			float availableHeight = HEIGHT - topNavigationBar.getHeight() - buttonSize.y  - (2 * padding);
			float startPositionY = (buttonSize.y / 2f) + padding;
			float curentPositionY = startPositionY + ((availableHeight / (expandButtons.size() - 1f)) * i);

			button.setRelY(curentPositionY);

		}





		//goldCreekLakebed.setRotation((float) );



		float theta = (float) ((Math.PI / 2));
		float phi = 0f;
		this.freeFlyCamera = new NoClipCamera(new Vector3f(0, 2.5f, 4), theta, phi);
		cameraPosition = freeFlyCamera.getPosition();
		cameraTheta = freeFlyCamera.getTheta();
		cameraPhi = freeFlyCamera.getPhi();
		this.boatMountedCamera = new NoClipCamera(new Vector3f(0, 2.5f, 4), theta, phi){
			@Override
			public void updateCamera(float delta, CurrentInput input) {
				//Do nothing.
			}
		};

		float newBoatMountedAspectRatio = ((float) boatCameraResolutionWidth) / ((float) boatCameraResolutionHeight);
		boatMountedCamera.getProjectionMatrixHandler().setAspectRatio(newBoatMountedAspectRatio);

		this.camera = freeFlyCamera;

//		grid = new ChunkGrid();
//		visGridTest = new VisualGrid(visTest);
//		grid.setVisualiser(visGridTest);

		//texIds = new int[1];
        //texIds[0] = loadPNGTexture("res/test_images/Sprite_0001.png", GL13.GL_TEXTURE0);

		enforceFramerateCap(-1);

		light = new PointLight(new Vector3f(100, 40, -100), new Vector3f(1, 1, 1), 8f);

		GraphicalPicking.setup(500, ASPECT_RATIO);
		graphicalPickingVis.setTexture(GraphicalPicking.getFbo().getTexture());
		graphicalPickingVis.setSize(GraphicalPicking.getFbo().getSize());
		graphicalPickingVis.setVisible(false);

		removeLoadedEntity(cubeEntity);
		removeLoadedEntity(cylinderEntity);
		removeLoadedEntity(physicsBoatEntity);
		physicsHandler = new BoatVisPhysics(this);
		physicsHandler.initPhysics();
		Thread physicsThread = new Thread(physicsHandler);
		physicsThread.start();
		ThreadDestroyer.registerDestroyableThread(physicsHandler);

		ocean.setWaveSettings(physicsSettingsDisplay);
		reducedSizeOcean.setWaveSettings(physicsSettingsDisplay);

		switchDisplayType(VisState.SCENE_VIEW);

		loadInitialSceneIfApplicable();

	}

	private void loadInitialSceneIfApplicable(){

		String sceneFileToLoad = System.getenv("SCENE_FILE_TO_LOAD");
		if ((sceneFileToLoad == null) || sceneFileToLoad.equals("")) {
			System.out.println("No scene to load.");
			return; //Nothing to do.
		}
		System.out.println("Attempting to load: " + sceneFileToLoad);
		/*
		if (programArgs.length < 1) {
			System.out.println("No scene to load.");
			return; //Nothing to do.
		}
		*/

		//String sceneToLoad = programArgs[0];
		File boatvisSceneFile = new File(sceneFileToLoad);
		loadBoatVisSceneFromFile(boatvisSceneFile);

	}

	public void syncBoatPosition() {

		Vector3f position = new Vector3f();
		Quat4f rotation = new Quat4f();

		if (physicsSettingsDisplay.usePhysicsBoat()) {

			Transform currentWorldTransform = new Transform();
			physicsHandler.getWamV().getBody().getWorldTransform(currentWorldTransform);
			position.set(PhysTransform.toGlPosition(currentWorldTransform.origin));
			rotation.set(PhysTransform.toGlRotation(currentWorldTransform.basis));

		} else {

			position.set(boatEntity.getPosition());
			rotation.set(boatEntity.getQuatRotation());

		}

		//The physics boat.
		Transform physicBoatTransform = new Transform();
		physicBoatTransform.origin.set(PhysTransform.toPhysPosition(position));
		physicBoatTransform.basis.set(PhysTransform.toPhysRotation(rotation));
		physicsHandler.getWamV().getBody().setWorldTransform(physicBoatTransform);

		boatEntity.setPosition(position);
		boatEntity.getQuatRotation().set(rotation);

	}

	public void setMousePointer(CurrentInput input){
		float x0 = camera.getPosition().x;
		float y0 = camera.getPosition().y;
		float z0 = camera.getPosition().z;

		float x1 = x0 + camera.getProjectedRay(input).x;
		float y1 = y0 + camera.getProjectedRay(input).y;
		float z1 = z0 + camera.getProjectedRay(input).z;

		//Intersect with plane y = 1.
		//Therefore: 1 = y0 + (y1-y0)*t
		//Therefore: t = (1-y0)/(y1-y0)
		float waterHeight = 0;
		float t = (waterHeight - y0) / (y1 - y0);

		Vector3f mPos = new Vector3f();
		mPos.x = x0 + ((x1 - x0)*t);
		mPos.y = y0 + ((y1 - y0)*t);
		mPos.z = z0 + ((z1 - z0)*t);

		if(Float.isNaN(mPos.x) || Float.isInfinite(mPos.x)){
			mPos.x = 0;
		}
		if(Float.isNaN(mPos.y) || Float.isInfinite(mPos.y)){
			mPos.y = 0;
		}
		if(Float.isNaN(mPos.z) || Float.isInfinite(mPos.z)){
			mPos.z = 0;
		}

		if(physicsSettingsDisplay.setPhysicsBoatPositionBoat()){
			//Reset the physics transform of the boat.
			Transform physicBoatTransform = new Transform();
			physicsHandler.getWamV().getBody().getWorldTransform(physicBoatTransform);
			float yComponent = ocean.getWaterHeight(mPos.x, mPos.z) + 0.80f;
			physicBoatTransform.origin.set(mPos.z, yComponent, mPos.x);
			AxisAngle4f angles = new AxisAngle4f(0f, 1f, 0f, 0f);
			physicBoatTransform.basis.set(angles);
			physicsHandler.getWamV().getBody().setWorldTransform(physicBoatTransform);
			physicsHandler.getWamV().getBody().clearForces();
			physicsHandler.getWamV().getBody().setDamping(1f, 1f);

			if (input.getLeftMouse() == ButtonStatus.JUST_PRESSED) {
				physicsSettingsDisplay.setDefaultPosition(PhysTransform.toGlPosition(physicBoatTransform.origin));
				physicsHandler.getWamV().getBody().setDamping(DEFAULT_LINEAR_DAMPING, DEFAULT_ANGULAR_DAMPING);
			}


		} else if (physicsSettingsDisplay.setPhysicsBoatRotationBoat()) {
			if (entityFollowingMouse != null) {
				entityFollowingMouse.setPosition(mPos);
			}

			Transform physicBoatTransform = new Transform();
			physicsHandler.getWamV().getBody().getWorldTransform(physicBoatTransform);

			float xDiff = mPos.x - physicBoatTransform.origin.z;
			float zDiff = mPos.z - physicBoatTransform.origin.x;
			float boatRotationAngle = (float) (Math.atan2(zDiff, xDiff) + (Math.PI / 2.0));
			AxisAngle4f boatRotationAxis = new AxisAngle4f(0, 1, 0, boatRotationAngle);

			physicBoatTransform.basis.set(boatRotationAxis);
			physicsHandler.getWamV().getBody().setWorldTransform(physicBoatTransform);
			physicsHandler.getWamV().getBody().setDamping(1f, 1f);

			if (input.getLeftMouse() == ButtonStatus.JUST_PRESSED) {
				Quat4f newDefaultRotation = new Quat4f();
				newDefaultRotation.set(boatRotationAxis);
				physicsSettingsDisplay.setDefaultBoatRotation(PhysTransform.toGlRotation(newDefaultRotation));
				physicsHandler.getWamV().getBody().setDamping(DEFAULT_LINEAR_DAMPING, DEFAULT_ANGULAR_DAMPING);
			}

		} else {
			if (entityFollowingMouse != null) {
				entityFollowingMouse.setPosition(mPos);
			}
		}

		this.mouseWaterLocation = mPos;

	}

	private void toggleExtendedDisplay(UserInterface extendedDisplay, boolean visible){
		extendedDisplay.setVisible(visible);
		setVisibilityOfAllExpandButtons(!visible);
	}

	private void setVisibilityOfAllExpandButtons(boolean visibilityOfAllExpandButtons){
		expandAddBouyDisplayButton.setVisible(visibilityOfAllExpandButtons);
		expandWaterSettingsButton.setVisible(visibilityOfAllExpandButtons);
		expandPhysicsSettingsDisplayButton.setVisible(visibilityOfAllExpandButtons);
		expandSkyboxDisplayButton.setVisible(visibilityOfAllExpandButtons);
	}

	private void closeAllOpenDisplays() {
		waterSettingsUIDisplay.setVisible(false);
		addBouyDisplay.setVisible(false);
		setVisibilityOfAllExpandButtons(true);
	}

	public void setMouseFollowingEntity(Entity newFollowingEntity) {
		entityFollowingMouse = newFollowingEntity;
	}


	float distTest = 5f; //TODO - Remove.

	@Override
	protected void logicCycle(float delta) {

		colourSequenceHandler.update(input, delta);

		if (TOGGLE_UI_HANDLER.justPressed()) {
			rootUI.setVisible(!rootUI.isVisible());
		}

		if (FULL_SCREEN_TOGGLE.justPressed()) {
			//setDisplayMode();
			toggleFullscreen();
		}


		boatMountedCamera.getProjectionMatrixHandler().setFieldOfView(skyboxSettingsDislpay.getCameraFieldOfView());

		goldCreekTreeline.setPosition(-180, -6, 52);
		//goldCreekTreeline.setPosition();

//		goldCreekLakebed.setRotation((float) (Math.PI / 2.0), 0f, 0f);
//		goldCreekLakebed.setPosition(-180f, -4f, 52f);
//
//		goldCreekTreeline.setRotation(goldCreekLakebed.getRotation());
//		goldCreekTreeline.setPosition(goldCreekLakebed.getPosition());

		boolean mouseIsOverOpenWater = true;

		//Update the graphical picking before moving any objects.
		cubeTestMousePointer.setVisible(false); //Hide the mouse pointer.
		goldCreekLakebed.setVisible(false);
		goldCreekTreeline.setVisible(false);
		if (!input.hasComponentReceivedMouseEvent() && ((visState == VisState.SCENE_VIEW) || (visState == VisState.SCENE_VIEW_WITH_CAM))) {
			//TODO - Update Graphical picking to handle Quaternions.
			int entityIndex = GraphicalPicking.getSelectedEntity(input.getMXpos(), input.getMYpos(), entities.size());
			if (entityIndex >= 0) {
				Entity entity = entities.get(entityIndex);
				if ((entity == boatEntity) || (entity == bridgeTest) || (entity == cubeTestMousePointer) || (entity == cameraPlaceholder)) {
					//Ignore.
				} else {
					mouseIsOverOpenWater = false;
					if (input.getLeftMouse() == ButtonStatus.JUST_RELEASED) {
						if(!addBouyDisplay.isManipulatingObject()){
							closeAllOpenDisplays();
							toggleExtendedDisplay(addBouyDisplay, true);
							addBouyDisplay.setSelectedEntity(entity);
						}
					}
				}
			} else {
				//Nothing selected.
			}
		}

		//Handled in it's own thread now!
		//physicsHandler.updatePhysics(1f * physicsSettingsDisplay.getPhysicsSpeedModifier());

		if (BoatVisInput.MOVE_BOAT_KEY_HANDLER.justPressed()) {
			shouldSetBoatPosition = true;
		}

		if (BoatVisInput.TOGGLE_NORTH_KEY_HANDLER.justPressed()) {
			northSky1.setVisible(!northSky1.isVisible());
			northSky2.setVisible(!northSky2.isVisible());
			northSky3.setVisible(!northSky3.isVisible());
		}

		//cubeTestMousePointer.setVisible(true); //Set the mouse pointer visible again.
		goldCreekLakebed.setVisible(useGoldCreekDamScene);
		goldCreekTreeline.setVisible(useGoldCreekDamScene);



		boolean grabMouse = false;
		if ((visState == VisState.SCENE_VIEW) || (visState == VisState.SCENE_VIEW_WITH_CAM)) {
			this.freeFlyCamera.update(delta, input);
		} else if (visState == VisState.LIDAR_VIEW) {

			if (input.getRightMouse() == ButtonStatus.JUST_PRESSED) {
				mouseHeldStartPositionLidar = new Vector2f(input.getMXpos(), input.getMYpos());
			}

			if (InputStatus.isButtonDown(input.getRightMouse())) {

				float xDiff = input.getMXpos() - mouseHeldStartPositionLidar.x;
				float yDiff = input.getMYpos() - mouseHeldStartPositionLidar.y;
				positionParentLidarVis.setRelX(positionParentLidarVis.getRelX() + xDiff);

				int setMousePosX = (int) (getWidth() / 2.0);
				int setMousePosY = (int) (getHeight() / 2.0);
				Mouse.setCursorPosition(setMousePosX, setMousePosY);
				mouseHeldStartPositionLidar = new Vector2f(setMousePosX, setMousePosY);
				grabMouse = true;
			}

			if (Mouse.isGrabbed() != grabMouse) {
				Mouse.setGrabbed(grabMouse);
			}

			//Scrolls across infinitely.
			for (int i = 0; i < lidarVisualRepresentations.length; i++) {
				LidarVisualRepresentation lidarVisualRepresentation = lidarVisualRepresentations[i];
				VisPixelPositions visPixelPositions = lidarVisualPixelIndicies[i];

				if (lidarVisualRepresentation.getAbsolutePosition().x > (Render.WIDTH + lidarVisualRepresentation.getSize().x * 1.0)) {
					lidarVisualRepresentation.setRelX(lidarVisualRepresentation.getRelX() - (lidarVisualRepresentation.getSize().x * 4.0f));
					visPixelPositions.setRelX(visPixelPositions.getRelX() - (visPixelPositions.getSize().x * 4.0f));
				}

				if (lidarVisualRepresentation.getAbsolutePosition().x < (0 - lidarVisualRepresentation.getSize().x * 1.0)) {
					lidarVisualRepresentation.setRelX(lidarVisualRepresentation.getRelX() + (lidarVisualRepresentation.getSize().x * 4.0f));
					visPixelPositions.setRelX(visPixelPositions.getRelX() + (visPixelPositions.getSize().x * 4.0f));
				}
			}

		}


		if(BoatVisInput.CAMERA_CYCLE_KEY_HANDLER.justReleased()){

			//Cycle through the vis states.
			switchDisplayType(VisState.values()[(visState.ordinal() + 1) % (VisState.values().length)]);

		}

		float cameraPhiOffset = (float) (Math.PI / 2);
		Vector3f cameraRotationOffset = new Vector3f(cameraPhiOffset, (float) BoatEntity.BOAT_ROTATION_OFFSET, 0);
		Quat4f cameraRotationOffsetQuat = new Quat4f();
		Quat4f theta180Offset = new Quat4f();
		theta180Offset.set(new AxisAngle4f(0, 1, 0, (float) Math.PI));

		cameraRotationOffsetQuat.set(theta180Offset);

		//cameraRotationOffsetQuat.set(new AxisAngle4f(0, 1, 0, (float) Math.PI));

		Quat4f cameraRotationPhiOffsetQuat = new Quat4f();
		cameraRotationPhiOffsetQuat.set(new AxisAngle4f(1, 0, 0, (float) 0.10));
		cameraRotationOffsetQuat.mul(cameraRotationPhiOffsetQuat);

		//cameraRotationOffsetQuat.mul(cameraRotationPhiOffsetQuat);


		Vector3f cameraPositionOffset = new Vector3f(0, 0.86f, -0.0f);
		if (physicsSettingsDisplay.usePhysicsBoat()) {
			WamV wamV = physicsHandler.getWamV();

			EntityPositionHelper.setRelativeTranslation(wamV.getWamVModel(), cameraPlaceholder, cameraPositionOffset, cameraRotationOffsetQuat);
			EntityPositionHelper.setRelativeTranslation(cameraPlaceholder, boatMountedCamera, new Vector3f(), theta180Offset);
			boatEntity.setVisible(false);
		} else {
			boatEntity.setVisible(true);
			EntityPositionHelper.setRelativeTranslation(boatEntity, cameraPlaceholder, cameraPositionOffset, cameraRotationOffsetQuat);
			EntityPositionHelper.setRelativeTranslation(cameraPlaceholder, boatMountedCamera, new Vector3f(), theta180Offset);
		}

		//Always update the boat mounted camera.
		this.boatMountedCamera.update(delta, input);

		//setRelativeTranslation

		//EntityPositionHelper.setRelativePosition(boatMountedCamera, boatEntity, cameraPositionOffset, cameraRotationOffset);

		//Update the rippling effect on the ocean.
		ocean.updateSettings(waterSettingsUIDisplay.getWaterSettings(), physicsSettingsDisplay.useSimpleWater());
		ocean.update(input, delta * physicsSettingsDisplay.getPhysicsSpeedModifier());

		reducedSizeOcean.updateSettings(waterSettingsUIDisplay.getWaterSettings(), physicsSettingsDisplay.useSimpleWater());
		reducedSizeOcean.update(input, delta * physicsSettingsDisplay.getPhysicsSpeedModifier());

		setMousePointer(input);

		if (waterSettingsUIDisplay.getReduceButton().justClicked()) {
			toggleExtendedDisplay(waterSettingsUIDisplay, false);
		} else if (addBouyDisplay.getReduceButton().justClicked()) {
			toggleExtendedDisplay(addBouyDisplay, false);
		} else if (physicsSettingsDisplay.getReduceButton().justClicked()) {
			toggleExtendedDisplay(physicsSettingsDisplay, false);
		} else if (skyboxSettingsDislpay.getReduceButton().justClicked()) {
			toggleExtendedDisplay(skyboxSettingsDislpay, false);
		}

		if (expandWaterSettingsButton.justClicked()) {
			toggleExtendedDisplay(waterSettingsUIDisplay, true);
		} else if(expandAddBouyDisplayButton.justClicked()){
			toggleExtendedDisplay(addBouyDisplay, true);
		} else if (expandPhysicsSettingsDisplayButton.justClicked()) {
			toggleExtendedDisplay(physicsSettingsDisplay, true);
		} else if (expandSkyboxDisplayButton.justClicked()) {
			toggleExtendedDisplay(skyboxSettingsDislpay, true);
		}

		if (!input.hasComponentReceivedMouseEvent()) {
			if(!addBouyDisplay.isManipulatingObject()){
				entityFollowingMouse = cubeTestMousePointer;
				if (input.getLeftMouse() == ButtonStatus.DOWN) {
					if (mouseIsOverOpenWater && !physicsSettingsDisplay.usePhysicsBoat()) {
						if (shouldSetBoatPosition) {
							boatEntity.setGoalBoatPosition(cubeTestMousePointer.getPosition());
							shouldSetBoatPosition = false;
						}

					}
				}
			}
		}

		if (input.getRightMouse() == ButtonStatus.DOWN) {
			if (shouldSetBoatPosition) {
				shouldSetBoatPosition = false;
			}
		}

		//rotateCameraLeftHandler); //J
		//addKeyHandler(Keyboard.KEY_L, rotateCameraRightHandler);

		if (startPresentationLatch.justReleased()) {
			//J Key
			presentationStartTime = System.currentTimeMillis();
		}
		long currentTime = System.currentTimeMillis();
		double currentWidth = ((((double) currentTime) - ((double) presentationStartTime)) / (15 * 60 * 1000)) * Render.WIDTH;
		fifteenMinTimerQuad.setWidth((float) currentWidth);
		if (alterFramerateLatch.justReleased()) {
			//L key
			lowerFramerateForSpeech = !lowerFramerateForSpeech;
			if (lowerFramerateForSpeech) {
				enforceFramerateCap(5);
			} else {
				enforceFramerateCap(-1);
			}
		}



		/*
		float oppositeDiff = cubeTestMousePointer.getPosition().y - boatTest.getPosition().y;
		float adjacentDiff = cubeTestMousePointer.getPosition().x - boatTest.getPosition().x;

		float maxSpeed = 0.1f;
		
		float moveSpeedX = adjacentDiff/100;
		float moveSpeedY = oppositeDiff/100;
		double totalMoveSpeed = Math.sqrt(Math.pow(moveSpeedX, 2) + Math.pow(moveSpeedY, 2));
		if(totalMoveSpeed > maxSpeed){
			moveSpeedX *= (maxSpeed / totalMoveSpeed);
			moveSpeedY *= (maxSpeed / totalMoveSpeed);
		}
		float newBoatXPos = boatTest.getPosition().x + moveSpeedX;
		float newBoatYPos = boatTest.getPosition().y + moveSpeedY;
		boatTest.setPosition(new Vector3f(newBoatXPos, newBoatYPos, 1));
		
		float angle = (float) (Math.atan2(oppositeDiff, adjacentDiff) + Math.PI);
		boatTest.setRotation(new Vector3f(0, 0, angle));
		*/
//		Vector3f lightPosition = new Vector3f();
//		lightPosition.x = boatTest.getPosition().x;
//		lightPosition.y = boatTest.getPosition().y;
//		lightPosition.z = boatTest.getPosition().z + 2;
//		light.setPosition(lightPosition);


		velodyneTheta += ((Math.PI * 2.0f) * delta) / 250f;
		Quat4f lidarRotationOffsetQuat = new Quat4f();
		lidarRotationOffsetQuat.set(new AxisAngle4f(0, 1, 0, (float) velodyneTheta));
		Quat4f tempQuat = new Quat4f();
		tempQuat.set(new AxisAngle4f(1, 0, 0, (float) 0f));
		lidarRotationOffsetQuat.mul(tempQuat);
		EntityPositionHelper.setRelativeTranslation(getBoatEntity(), velodyneEntity, LidarHelper.LIDAR_POSITION_OFFSET, lidarRotationOffsetQuat);
		//setLidarTranslation(, , , velodyneTheta, LidarHelper.PHI_OFFSET);


		//Update (If applicable) the lidar visual representations.
		for (int i = 0; i < LidarHelper.NUM_DIRECTIONS; i++) {
			lidarVisualRepresentations[i].setLidarCalculationHandler(lidarHelper.getLidarCalculationHandler(i));
		}

	}

	float velodyneTheta = 0f;

	public void setLidarRotationSpeed(LidarHelper.LidarRotationSpeed lidarRotationSpeed) {
		lidarHelper.setLidarRotationSpeed(lidarRotationSpeed);
		lidarRenderIndex = 0;
	}

	public Vector3f getMouseWaterLocation() {
		return mouseWaterLocation;
	}

	@Override
	public void setFps(float newFps){
		if(newFps == 0){
			fpsDisplay.setText("  FPS: " + fps);
		}
		super.setFps(newFps);
	}


	@Override
	protected void prepareToRender(Shader currentShader) {
		modelset.bind();
		if (currentShader instanceof Textured3DShader) {
			((Textured3DShader) currentShader).loadPointLight(light);
		}
	}

	private void renderWorldAndOcean(RenderedWater oceanToRender, Camera cameraToRender, FrameBufferObject frameBufferObjectToRenderTo){
		Camera previousCamera = camera;
		this.camera = cameraToRender;

		if (frameBufferObjectToRenderTo != null) {
			frameBufferObjectToRenderTo.bindFrameBuffer();
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_CULL_FACE);

		if (frameBufferObjectToRenderTo != null) {
			frameBufferObjectToRenderTo.unbindCurrentFrameBuffer();
		}


		oceanToRender.render(cameraToRender, light, frameBufferObjectToRenderTo);

		if (frameBufferObjectToRenderTo != null) {
			frameBufferObjectToRenderTo.bindFrameBuffer();
		}

		//GL11.glEnable(GL30.GL_CLIP_DISTANCE0); //NOTE! This may be ignored! Also set the clip plane to something extreme!
		//shader3D.setClipPlane(0, 1, 0, -10000);
		//Render.instance.setCurrentClipPlane(0, 1, 0, 0);

		renderWorld(shader3D, cameraToRender);

		if (frameBufferObjectToRenderTo != null) {
			frameBufferObjectToRenderTo.unbindCurrentFrameBuffer();
		}

		//GL11.glDisable(GL30.GL_CLIP_DISTANCE0); //NOTE! This may be ignored! Also set the clip plane to something extreme!
		//TODO - Also set the clip plane to something extreme! GL11.glDisable(GL30.GL_CLIP_DISTANCE0); doesn't work on all PCs


		this.camera = previousCamera;
	}

	private void switchDisplayType(VisState newVisualState) {
		this.visState = newVisualState;

		switch (visState) {
			case SCENE_VIEW:

				secondaryCameraDisplay.setVisible(false);
				positionParentLidarVis.setVisible(false);

				break;
			case SCENE_VIEW_WITH_CAM:

				secondaryCameraDisplay.setVisible(true);
				secondaryCameraDisplay.setSize(secondaryCameraViewFBO.getWidth() / 2f, secondaryCameraViewFBO.getHeight() / 2f);
				//secondaryCameraDisplay.setRelX(Render.WIDTH - (secondaryCameraDisplay.getWidth() / 2f) - 50);
				//secondaryCameraDisplay.setRelY(Render.HEIGHT - (secondaryCameraDisplay.getHeight() / 2f) - 50);

				secondaryCameraDisplay.setRelX((secondaryCameraDisplay.getWidth() / 2f) + 20);
				secondaryCameraDisplay.setRelY(Render.HEIGHT - (secondaryCameraDisplay.getHeight() / 2f) - 50);
				positionParentLidarVis.setVisible(false);

				break;
			case BOAT_CAM:

				secondaryCameraDisplay.setVisible(true);
				secondaryCameraDisplay.setRelativePosition(Render.getCentreOfScreen());
				secondaryCameraDisplay.setSize(secondaryCameraViewFBO.getSize());
				positionParentLidarVis.setVisible(false);

				break;
			case LIDAR_VIEW:

				secondaryCameraDisplay.setVisible(false);
				positionParentLidarVis.setVisible(true);

				break;
		}
	}

	@Override
	protected void renderCycle() {


		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		renderWorldAndOcean(reducedSizeOcean, boatMountedCamera, secondaryCameraViewFBO);

		switch (visState) {
			case SCENE_VIEW:
			case SCENE_VIEW_WITH_CAM:

				renderWorldAndOcean(ocean, freeFlyCamera, null);

				break;
			case BOAT_CAM:

				setBackgroundColour(0, 0, 0, 1f);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glEnable(GL11.GL_CULL_FACE);

				break;
			case LIDAR_VIEW:

				break;
		}


		renderLidars();
		setCurrentClipPlane(DEFAULT_CLIP_PLANE);



		//2D setup
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		Quad.bind();
		rootUI.render();
	}

	@Override
	protected void afterDisplayUpdate() {
		if (!isLoaded()) {
			//Ignore until loaded.
			return;
		}

		secondaryCameraViewFBO.bindFrameBuffer();
		cameraCalculationThread.writeNewColourData();
		secondaryCameraViewFBO.unbindCurrentFrameBuffer();

	}

	private void renderLidars() {
		//Clip all below the water level.
		setCurrentClipPlane(0, 1, 0, 0);

		//Render the LiDAR depth buffers.
		for (int i = 0; i < lidarHelper.getLidarRotationSpeed().getBuffersUpdatedPerFrame(); i++) {
			lidarRenderIndex++;
			lidarRenderIndex = lidarRenderIndex % (LidarHelper.DepthBufferDirection.values().length);
			LidarCalculationHandler lidarCalculationHandler = lidarHelper.getLidarCalculationHandler(lidarRenderIndex);
			renderLidar(lidarCalculationHandler);
		}
	}

	private void renderLidar(LidarCalculationHandler lidarCalculationHandler) {
		FrameBufferObject frameBufferObject = lidarCalculationHandler.getFrameBuffer();
		LidarHelper.DepthBufferDirection direction = lidarCalculationHandler.getDepthBufferDirection();
		Camera lidarCamera = lidarCameras[direction.ordinal()];

		Entity currentBoatModel;
		if (physicsSettingsDisplay.usePhysicsBoat()) {
			currentBoatModel = physicsHandler.getWamV().getWamVModel();
		} else {
			currentBoatModel = boatEntity;
		}

		setLidarTranslation(currentBoatModel, lidarCamera, LidarHelper.LIDAR_POSITION_OFFSET, direction.getThetaAngleOffset(), LidarHelper.PHI_OFFSET);
		lidarCamera.resetMatricies(); //Reset the matricies.
		lidarCamera.calculateRotationMatrix();
		frameBufferObject.bindFrameBuffer();
		setShader(lidarShader3D);
		GLWrapper.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		renderWorld(lidarShader3D, lidarCamera);
		lidarHelper.addLidarReadings(lidarCamera, lidarCalculationHandler);
		frameBufferObject.unbindCurrentFrameBuffer();

	}


	/**
	 * Translates the orientation of the lidar camera to be correct.
	 * @param boat - The boat that is is mounted to.
	 * @param lidarCamera - The camera to translate.
	 * @param offsetPosition - The offset position on the boat to translate from.
	 * @param theta - y-axis angle to rotate.
     * @param phi - x-axis angle to rotate.
     */
	private void setLidarTranslation(Entity boat, Camera lidarCamera, Vector3f offsetPosition, double theta, double phi) {
		Quat4f lidarRotationOffsetQuat = new Quat4f();
		lidarRotationOffsetQuat.set(new AxisAngle4f(0, 1, 0, (float) theta));
		Quat4f tempQuat = new Quat4f();
		tempQuat.set(new AxisAngle4f(1, 0, 0, (float) phi));
		lidarRotationOffsetQuat.mul(tempQuat);
		EntityPositionHelper.setRelativeTranslation(boat, lidarCamera, offsetPosition, lidarRotationOffsetQuat);
	}

	private Vector2f calThetaPhi(Vector3f xyz) {
		double rDot = Math.sqrt((xyz.x * xyz.x) + (xyz.z * xyz.z));
		double theta = Math.atan2(-xyz.z, xyz.x);
		double phi = Math.atan2(xyz.y, rDot);
		return new Vector2f((float) theta, (float) phi);
	}

	private Vector3f calcRayFromThetaPhi(double theta, double phi) {
		double yDot = Math.sin(phi);
		double rDot = Math.cos(phi);
		double zDot = -rDot * Math.sin(theta);
		double xDot = rDot * Math.cos(theta);
		return new Vector3f((float) xDot, (float) yDot, (float) zDot);
	}

	public void renderWorld(Shader3D shader3D, Camera worldCamera) {
		//setBackgroundColour(1, 1, 1, 1);
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_CULL_FACE); //Try with back face culling to ensure that the skybox is correct.
		GL11.glCullFace(GL11.GL_BACK);
		if(shader3D instanceof Textured3DShader){
			skyboxTest.renderSkybox(worldCamera);
			setShader(shader3D);
			prepare3DCamera(shader3D, worldCamera);
			renderEntities(shader3D, worldCamera);


			//TODO - Remove (Testing of LiDAR positions).
/*
			//float maxVal = 5000f;
			ArrayList<LidarReading> currentLidarReadings = lidarHelper.getCurrentLidarReadings();


			LidarReading[][] rawReadings = lidarHelper.getCurrentLidarRawReadings();

			for (int beamNo = 0; beamNo < rawReadings.length; beamNo++) {
				LidarReading[] horizontalReadings = rawReadings[beamNo];
				for (int xIndex = 0; xIndex < horizontalReadings.length; xIndex++) {


					//int readingIndex = (int) (currentLidarReadings.size() * (i / maxVal));
					//LidarReading lidarReading = currentLidarReadings.get(readingIndex);
					LidarReading lidarReading = horizontalReadings[xIndex];

					if ((lidarReading.getDepth() > 50.0)) {
						continue;
					}
					if(lidarReading.getDepth() < 0.001){
						continue;
					}

					double yOffset = boatEntity.getPosition().y + LidarHelper.LIDAR_POSITION_OFFSET.y;
					double yPosition = lidarReading.getDepth() * Math.sin(lidarReading.getPhi()) + yOffset;
					double rVal = lidarReading.getDepth() * Math.cos(lidarReading.getPhi());
					double xPosition = rVal * Math.cos(lidarReading.getTheta());
					double zPosition = -rVal * Math.sin(lidarReading.getTheta());

					System.out.printf("%f",xPosition);
					System.out.printf("%f",yPosition);
					System.out.printf("%f",zPosition);
					//cubeEntity.setSize(0.05f, 0.05f, 0.05f);
					//cubeEntity.setPosition((float) xPosition, (float) yPosition, (float) zPosition);
					//cubeEntity.render(shader3D);

				}
			}


 */

			/*
			//OLD IMPLEMENTATION!
			int numReadings = currentLidarReadings.size();

			for (float i = 0; i < numReadings; i += 1) {


				//int readingIndex = (int) (currentLidarReadings.size() * (i / maxVal));
				//LidarReading lidarReading = currentLidarReadings.get(readingIndex);
				LidarReading lidarReading = currentLidarReadings.get((int) i);

				if ((lidarReading.getDepth() > 50.0) || (lidarReading.getDepth() < 0.1)) {
					continue;
				}

				double yPosition = lidarReading.getDepth() * Math.sin(lidarReading.getPhi()) + 2.0;
				double rVal = lidarReading.getDepth() * Math.cos(lidarReading.getPhi());
				double xPosition = rVal * Math.cos(lidarReading.getTheta());
				double zPosition = -rVal * Math.sin(lidarReading.getTheta());

				cubeEntity.setSize(0.05f, 0.05f, 0.05f);
				cubeEntity.setPosition((float) xPosition, (float) yPosition, (float) zPosition);
				cubeEntity.render(shader3D);
			}
			*/


		} else if(shader3D instanceof LiDAR3DShader){
			setShader(shader3D);
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			((LiDAR3DShader) shader3D).setClipPlane(0, 1, 0, 0);
//			Render.instance.setCurrentClipPlane(0, 1, 0, 100);
			prepare3DCamera(shader3D, worldCamera);
			renderEntities(shader3D, worldCamera);
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		}
	}

	@Override
	public void renderEntities(Shader3D shader3D, Camera worldCamera) {
		super.renderEntities(shader3D, worldCamera);
		physicsHandler.renderPhysics(shader3D);
	}

	@Override
	protected void renderEntities(Shader3D shader3D) {
		throw new UnsupportedOperationException("IDK what's happening here, but I don't think this should be called.");
	}

	@Override
	protected void destroyOpenGL() {
		/*
		 * Destroy all running threads.
		 */
		modelset.destroy(); //Probably create interface for objects to be destroyed and maintain a list in render/renderbase
		RawModel.destroyAllRawModels();
		ocean.destroy();
		reducedSizeOcean.destroy();

//		test.destroy();
//		visGridTest.destroy();

	}

	public BoatEntity getBoatEntity() {
		if (physicsSettingsDisplay.usePhysicsBoat()) {
			return getPhysicsBoatEntity(1,0,0);
		}
		return getNormalBoatEntity();
	}

	public void getPhysicsBoatEntity2(int i,float q,float p) {
		//调用相机的LidarCalculationHandler文件的getDepthAtPixelPercentages函数，获取某个像素点RGB值
		if (i==11) {
			System.out.printf("%f\n", LidarCalculationHandler.getDepthAtPixelPercentages(q, p));
		}
		//调用雷达的CameraCalculationThread文件，获取某个方向障碍物距离
		if(i==12){
			int qq=(int)(640*q);
			int pp=(int)(480*p);
			CameraCalculationThread.aaa(qq,pp);
		}
	}
	//此函数是终端控制的调用接口，根据传入i的值的不同返回不同的信息
	public BoatEntity getPhysicsBoatEntity(int i,int q,int p) {
		if(i==1) {
			return physicsBoatEntity;
		}
		//传入2，调用getPosition()函数获取当前GPS信息
		else if(i==2) {
			System.out.print("Position: ");
			System.out.print(physicsBoatEntity.getPosition().toString());
			System.out.print("\n");
		}
		//传入3，调用getCurrentBoatVelocity()函数获取当前速度
		else if(i==3) {
			System.out.print("Velocity: ");
			System.out.print(getCurrentBoatVelocity().toString());
			System.out.print("\n");
		}
		//传入4，调用getPosition().length()函数获取当前运动距离
		else if(i==4) {
			System.out.print("Distance: ");
			System.out.printf("%.2f", physicsBoatEntity.getPosition().length());
			System.out.printf("\n");
			//System.out.printf("%.2f",Math.sqrt(physicsBoatEntity.getPosition().x*physicsBoatEntity.getPosition().x+physicsBoatEntity.getPosition().y*physicsBoatEntity.getPosition().y+physicsBoatEntity.getPosition().z*physicsBoatEntity.getPosition().z));
		}
		else if(i==5) {
			System.out.printf("QuatRotation: ");
			System.out.printf(physicsBoatEntity.getQuatRotation().toString());
			System.out.printf("\n");
		}
		else if(i==6){
			PhysicsSettingsDisplay.boatVis.getPhysicsBoat().applyConstantMotorForceOnLeftPontoon(q);
			System.out.printf("The left motor's force has changed to %dN\n",q);
		}
		else if(i==7){
			PhysicsSettingsDisplay.boatVis.getPhysicsBoat().applyConstantMotorForceOnRightPontoon(q);
			System.out.printf("The right motor's force has changed to %dN\n",q);
		}
		else if(i==8){
			PhysicsSettingsDisplay.boatVis.getPhysicsBoat().applyConstantMotorForceOnLeftPontoon(q);
			PhysicsSettingsDisplay.boatVis.getPhysicsBoat().applyConstantMotorForceOnRightPontoon(q);
			System.out.printf("Both motor's value has changed to %d\n",q);
		}
		else if(i==9){
			Scanner s=new Scanner(System.in);
			PhysicsSettingsDisplay.resetBoatPosition();
			PhysicsSettingsDisplay.boatVis.getPhysicsBoat().applyConstantMotorForceOnLeftPontoon(0);
			PhysicsSettingsDisplay.boatVis.getPhysicsBoat().applyConstantMotorForceOnRightPontoon(0);
			System.out.printf("Reset finished!\n");

		}
		else if(i==10){
			PhysicsSettingsDisplay.boatVis.getPhysicsBoat().applyConstantMotorForceOnLeftPontoon(q);
			PhysicsSettingsDisplay.boatVis.getPhysicsBoat().applyConstantMotorForceOnRightPontoon(p);
			System.out.printf("The left and right motors's values have changed to %d and %d\n",q,p);
		}
		else if(i==12){
			System.out.printf("%f,%f,%f,%f,%f,%f\n",physicsBoatEntity.getPosition().x,physicsBoatEntity.getPosition().y,physicsBoatEntity.getPosition().z,getCurrentBoatVelocity().x,getCurrentBoatVelocity().y,getCurrentBoatVelocity().z);
		}

		return physicsBoatEntity;

	}


	public BoatEntity getNormalBoatEntity() {
		return boatEntity;
	}

	public CameraCalculationThread getCameraCalculationThread() {
		return cameraCalculationThread;
	}

	public RenderedWater getRenderedWater() {
		return ocean;
	}

	public BoatVisScene getBoatVisScene() {
		return boatVisScene;
	}

	/**
	 * TODO - This isn't properly multi-threaded, and it should be.
	 * @param file - The scene file to load.
     */
	public void loadBoatVisSceneFromFile(File file) {
		boatVisSceneLoading = new BoatVisScene();
		try {
			boatVisSceneLoading.loadFromFile(file);
			boatVisSceneLoading.loadAllEntites();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		//Swap over to the new scene.
		this.boatVisScene = boatVisSceneLoading;
	}

	public boolean useGoldCreekDamScene() {
		return useGoldCreekDamScene;
	}

	public void setUseGoldCreekDamScene(boolean useGoldCreekDamScene) {
		this.useGoldCreekDamScene = useGoldCreekDamScene;
		goldCreekLakebed.setVisible(useGoldCreekDamScene);
		goldCreekTreeline.setVisible(useGoldCreekDamScene);
	}

	public Entity getCubeEntity() {
		return cubeEntity;
	}

	public Entity getCylinderEntity() {
		return cylinderEntity;
	}

	public PhysicsSettingsDisplay getPhysicsSettingsDisplay() {
		return physicsSettingsDisplay;
	}

	public WamV getPhysicsBoat() {
		return physicsHandler.getWamV();
	}

	public Vector3f getCurrentBoatVelocity(){
		if (physicsSettingsDisplay.usePhysicsBoat()) {
			Vector3f physicsSpaceVelocity = getPhysicsBoat().getVelocity();
			Vector3f openGlSpaceVelocity = PhysTransform.toGlPosition(physicsSpaceVelocity);
			return openGlSpaceVelocity;
		} else {
			return getNormalBoatEntity().getVelocity();
		}
	}

	public WaterSettingsUI getWaterSettingsDisplay() {
		return waterSettingsUIDisplay;
	}

	public ColourSequenceHandler getColourSequenceHandler() {
		return colourSequenceHandler;
	}


	public void createNewScene() {
		waterSettingsUIDisplay.resetAllValues();
		physicsSettingsDisplay.resetAllValues();
		List<Entity> entitiesListToRemove = new LinkedList<Entity>();
		for (Entity entity : entities) {
			if (entity.isUsePicking()) {
				entitiesListToRemove.add(entity);
			}
		}
		for (Entity entity : entitiesListToRemove) {
			entity.removeFromRenderList();
		}

/*		Scanner s=new Scanner(System.in);
		int a=s.nextInt();
		System.out.printf("%d",a);*/


		colourSequenceHandler.getColourSequenceNodes().clear();
		colourSequenceHandler.getColouredEntites().clear();

		PingerHandler.getPingingBuoys().clear();
		PingerHandler.getPingingFrequencies().clear();

	}
}
