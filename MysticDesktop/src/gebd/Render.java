package gebd;

import blindmystics.input.CurrentInput;
import blindmystics.util.Display;
import blindmystics.util.EnvironmentException;
import blindmystics.util.FileReader;
import blindmystics.util.GLWrapper;
import composites.ImageFontLegacy;
import composites.entities.Entity;
import gebd.Exception.NotImplementedException;
import gebd.camera.Camera;
import gebd.concurrent.ThreadDestroyer;
import gebd.light.PointLight;
import gebd.shaders.Shader;
import gebd.shaders.Shader2D;
import gebd.shaders.Shader3D;
import gebd.shaders.shaders.Picking3DShader;
import gebd.util.ExitTypes;
import loader.LoadedObjectAbstract;
import loader.LoadedObjectHandler;
import loader.Loader;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import renderables.View;
import renderables.r2D.Quad;
import renderables.r2D.Renderable2DUpdateHandler;
import renderables.r2D.composite.TopLevelInterface;
import renderables.r2D.composite.UserInterface;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;
import util.file.FileChooser;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.concurrent.Semaphore;

public abstract class Render extends LoadedObjectAbstract {
	//Singleton Pattern - Only one instance available.
	public static Render instance;

	protected static int currentFrame = 0;


	private LoadedObjectHandler.LoadStatus currentStatus;
	private CurrentInput currentInput;

	@Override
	public LoadedObjectHandler.LoadStage[] stagesToPerform(){
		return new LoadedObjectHandler.LoadStage[] {
				LoadedObjectHandler.LoadStage.LOAD_DATA_FROM_FILE,
				LoadedObjectHandler.LoadStage.HANDLE_RAW_DATA,
				LoadedObjectHandler.LoadStage.LOAD_DEPENDENCIES,
		};
	}

	@Override
	public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {
		this.currentStatus = newLoadStatus;
	}

	@Override
	public LoadedObjectHandler.LoadStatus getLoadStatus() {
		return currentStatus;
	}

	/*
	 * TODO - Should I be concerned here?
	 *
	@Override
	public void handleLoad(LoadedObjectHandler<?> handler){
		//Do Nothing!
	}
	*/

	protected abstract void initialize();

	protected LoadedObjectHandler rootObjectLoadHandler = new LoadedObjectHandler(this);

	private static final int DEAD_RENDER = 1;

	protected String WINDOW_TITLE = "3D Render";
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	//public static int HEIGHT = 720;
//	public static int WIDTH = 1920;
//	public static int HEIGHT = 1080;
	public static float ASPECT_RATIO = (float) WIDTH / (float) HEIGHT;

	protected static int TOP = HEIGHT;
	protected static int BOTTOM = 0;
	protected static int LEFT = 0;
	protected static int RIGHT = WIDTH;

	protected static int MIDDLE_HORIZONTAL = WIDTH/2;
	protected static int MIDDLE_VERTICAL = HEIGHT/2;

	protected static DisplayMode displayMode;
	protected static boolean fullscreen = false;
	private static boolean vSyncEnabled = false;

	private static Vector4f clearColour;



	public static int getWidth(){
		return WIDTH;
	}

	public static int getHeight(){
		return HEIGHT;
	}

	protected LinkedList<Shader> loadedShaders = new LinkedList<Shader>();


	/** time at last frame */
	protected double lastFrame;
	/** frames per second */
	protected float fps;
	/** last fps time */
	protected double lastFPS;
	/** time between now and previous frame (in ms) */
	protected static float delta = 0;
	/** Frame Number **/
	public static int frameNumber = 0;

	protected static Shader currentShader = null;

	// Camera position vector
	public final static Vector3f CAMERA_POS_DEFAULT = new Vector3f(0, 0, 0);
	public final static Vector3f CAMERA_ROT_DEFAULT = new Vector3f(0, 0, 0);

	public static float ambientLightIntensity;



	//2D quad that renders stuff:
	public static final Quad QUAD_2D = new Quad();

	protected float[] backgroundColour = {0.4f, 0.6f, 0.9f, 0.0f}; //TODO default black or put in map
	//protected float[] backgroundColour = {0f, 0f, 0f};
	//TODO - GLSL Make some fog!

	//protected HashMap<String, LoadedObjectHandler<Model>> LoadedModels = new HashMap<String, LoadedObjectHandler<Model>>();
	protected HashMap<String, LoadedObjectHandler<ImageFontLegacy>> LoadedFontLoaders = new HashMap<>();

	public static Semaphore canUseWaitingJobs = new Semaphore(1);
	protected LinkedList<LoadedObjectHandler> waitingLoadJobs = new LinkedList<>();
	protected synchronized LinkedList<LoadedObjectHandler> getWaitingLoadJobs() {
		//TODO: Not a good way to do multithreading.
		return this.waitingLoadJobs;
	}

	//Camera
	protected Camera camera;
	protected Camera tempStoredCamera;

	//Input
	protected CurrentInput input = new CurrentInput();


	protected static FloatBuffer matrix44Buffer;

	protected abstract void destroyOpenGL();

	protected List<Entity>entities = new ArrayList<>();

	//TODO - This should probably be turned into sunlight. (IE a directional light).
	protected PointLight light;

	protected UserInterface rootUI = new UserInterface(new Vector2f(0, 0), new Vector2f(WIDTH, HEIGHT), 0f){
		@Override
		public void render() {
			if (isVisible()) {
				super.render();
			}
		}

		@Override
		public void update(CurrentInput input, float delta) {
			if (isVisible()) {
				super.update(input, delta);
			}
		}
	};

	private int forcedFramerateCap = -1;
	protected boolean enforceFramerateCap = false;

	private View currentView = null;


	protected Exception criticalGameException = null;

	public static final Vector4f DEFAULT_CLIP_PLANE = new Vector4f(0, 1, 0, -10000);
	private Vector4f currentClipPlane = new Vector4f(DEFAULT_CLIP_PLANE);

	public static Vector2f getScreenSize() {
		return new Vector2f(WIDTH, HEIGHT);
	}

	public static Vector2f getCentreOfScreen() {
		return new Vector2f(WIDTH / 2f, HEIGHT / 2f);
	}

	public void setCurrentClipPlane(Vector4f newClipPlane) {
		setCurrentClipPlane(newClipPlane.x, newClipPlane.y, newClipPlane.z, newClipPlane.w);
	}

	public void setCurrentClipPlane(float a, float b, float c, float d) {
		currentClipPlane.x = a;
		currentClipPlane.y = b;
		currentClipPlane.z = c;
		currentClipPlane.w = d;
	}

	public Vector4f getCurrentClipPlane() {
		return currentClipPlane;
	}


	public void setAmbientLightIntensity(float intensity) {
		ambientLightIntensity = intensity;
	}

	public void setWindowTitle(String title){
		WINDOW_TITLE = title;
	}

	public void setDisplayMode(int width, int height, boolean isFullscreen) {
		WIDTH = width;
		HEIGHT = height;

		TOP = HEIGHT;
		BOTTOM = 0;
		LEFT = 0;
		RIGHT = WIDTH;

		MIDDLE_HORIZONTAL = WIDTH/2;
		MIDDLE_VERTICAL = HEIGHT/2;

		ASPECT_RATIO = (float) WIDTH / (float) HEIGHT;
		displayMode = new DisplayMode(width, height);

		Camera currentCamera = getCamera();
		if (currentCamera != null) {
			currentCamera.getProjectionMatrixHandler().setAspectRatio(ASPECT_RATIO);
		}

		try {
			Display.setDisplayMode(displayMode);
			setFullscreen(isFullscreen);
		} catch (EnvironmentException e) {
			e.printStackTrace();
			System.exit(ExitTypes.UNEXPECTED_OPENGL_ERROR);
		}
	}

	public void setDisplayMode(DisplayMode mode, boolean isFullscreen) {
		setDisplayMode(mode.getWidth(), mode.getHeight(), isFullscreen);
	}

	protected void setvSyncEnabled(boolean enabled) {
		vSyncEnabled = enabled;
		//vSync only works in fullscreen mode.
		Display.setVSyncEnabled(fullscreen && vSyncEnabled);
	}

    protected void setupOpenGL() {
		// Setup an OpenGL context with API version 3.2
		try {
			PixelFormat pixelFormat = new PixelFormat();
			//int MSAA = 8; //MSAA - I think this is MSAA, only available in OpenGl 3.2 and above.
			//PixelFormat pixelFormat = new PixelFormat(0, 8, 0, MSAA);

			//Context attributes removed because it didn't work with numerous laptops?

			if(displayMode == null){
				System.setProperty("org.lwjgl.opengl.Window.undecorated", "true"); //Borderless windows.
				System.out.println(Display.getDesktopDisplayMode());
			}

			Display.setTitle(WINDOW_TITLE);
			//Display.setVSyncEnabled(true);

			Display.create(pixelFormat);

			GLWrapper.glMatrixMode(GLWrapper.GL_PROJECTION);
			GLWrapper.glLoadIdentity();
			GLWrapper.glMatrixMode(GLWrapper.GL_MODELVIEW);
			GLWrapper.glLoadIdentity();

			// Map the internal OpenGL coordinate system to the entire screen
			GLWrapper.glViewport(0, 0, WIDTH, HEIGHT);

			System.out.println("OpenGL version: " + GLWrapper.glGetString(GLWrapper.GL_VERSION));
		} catch (EnvironmentException e) {
			e.printStackTrace();
			System.exit(ExitTypes.UNEXPECTED_OPENGL_ERROR);
		}


		setBackgroundColour(backgroundColour[0], backgroundColour[1], backgroundColour[2], 0f);

		GLWrapper.glEnable(GLWrapper.GL_BLEND);
		GLWrapper.glBlendFunc(GLWrapper.GL_SRC_ALPHA, GLWrapper.GL_ONE_MINUS_SRC_ALPHA);

		GLWrapper.glEnable(GLWrapper.GL_CULL_FACE);
		GLWrapper.glCullFace(GLWrapper.GL_BACK);

		matrix44Buffer = BufferUtils.createFloatBuffer(16);

		exitOnGLError("setupOpenGL");
	}

	public void toggleFullscreen() {
		setFullscreen(!fullscreen);
	}

	public void setFullscreen(boolean isFullscreen) {
		if (isFullscreen) {
			enableFullscreenIfPossible();
		} else {
			try {
				Display.setFullscreen(false);
				fullscreen = false;
			} catch (EnvironmentException e) {
				e.printStackTrace();
			}
		}
	}

	public void enableFullscreenIfPossible() {
		//WIDTH
		//HEIGHT
		DisplayMode[] availableDisplayModes = null;
		try {
			availableDisplayModes = Display.getAvailableDisplayModes();
		} catch (LWJGLException e) {
			e.printStackTrace();
			return;
		}

		boolean fullScreenSuccessful = false;
		for (int i = 0; i < availableDisplayModes.length; i++) {
			DisplayMode availableDisplayMode = availableDisplayModes[i];
			if ((availableDisplayMode.getWidth() == WIDTH) && (availableDisplayMode.getHeight() == HEIGHT)) {
				try {
					Display.setDisplayMode(availableDisplayMode);
					Display.setFullscreen(true);
					fullscreen = true;
					Display.setVSyncEnabled(fullscreen && vSyncEnabled);
				} catch (EnvironmentException e) {
					e.printStackTrace();
					return;
				}
				fullScreenSuccessful = true;
				break;
			}
		}


		if (!fullScreenSuccessful) {
			System.err.println("Fullscreen is unavailable at the requested resolution of: (" + WIDTH + "," + HEIGHT + ")");
		}
	}

	/**
	 * NOTE - I'm not sure if the alpha channel does much here.
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	protected void setBackgroundColour(float red, float green, float blue, float alpha){
		backgroundColour[0] = red;
		backgroundColour[1] = green;
		backgroundColour[2] = blue;
		backgroundColour[3] = alpha;
		GLWrapper.glClearColor(red, green, blue, alpha);
		clearColour = new Vector4f(red, green, blue, alpha);
	}

	public Vector4f getClearColour() {
		return clearColour;
	}

	protected void setBackgroundColour(float[] colour){
		setBackgroundColour(colour[0], colour[1], colour[2], colour[3]);
	}

	protected void setBackgroundColour(Vector4f colour){
		setBackgroundColour(colour.x, colour.y, colour.z, colour.w);
	}

	/*
	 * Use the shader.
	 */
	public static void setShader(Shader shader){
		if (!shader.equals(currentShader)) {
			GLWrapper.glUseProgram(shader.getProgramId());
			currentShader = shader;
			prepareShader();
			TextureHandler.prepareTexture(0);
		}
	}

	protected static void prepareShader(){
		currentShader.prepare();
	}

	protected int loadShader(String filename, int type) {
		int shaderId = Shader.loadShader(filename, type);
		exitOnGLError("loadShader");
		return shaderId;
	}

	protected <T extends Shader> T initializeShader(T shader){
		loadedShaders.add(shader);
		shader.initialize();
		return shader;
	}

	public void addWaitingLoadJob(LoadedObjectHandler job){
		try {
			canUseWaitingJobs.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace(); //TODO - Figure out how this actually works :(
		}
		getWaitingLoadJobs().add(job);
		canUseWaitingJobs.release();
	}

	protected void checkWaitingJobs(){
		try {
			canUseWaitingJobs.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace(); //TODO - Figure out how this actually works :(
		}
		//System.out.println("Jobs waiting = " + getWaitingLoadJobs().size());
		Iterator<LoadedObjectHandler> jobIterator = getWaitingLoadJobs().listIterator();
		while(jobIterator.hasNext()){
			LoadedObjectHandler nextJob = jobIterator.next();
			jobIterator.remove();
			//log("handleLoadDependencyStage");
			nextJob.handleLoadDependencyStage();
		}
		canUseWaitingJobs.release();
	}

	/**
	 * Clears shaders, models and textures from openGl memory.
	 */
	protected void destroyShadersModelsTextures(){
		/*
		 * TODO:
		 * Have both a linked list of all models.
		 * And a HashMap of Key to Model.
		 * first:
		 * check if the hashMap already contains the model,
		 * if so, nothing much
		 * if not, add it to the linkedList as well.
		 *
		 * Also:
		 *
		 * As you iterate through the loaded models:
		 * have a boolean - shouldBeRemoved
		 * if that is true, remove the model from the linked list (IE no entites of that type to render anymore).
		 *
		 */
		TextureHandler.destroyAllTextures();
		//destroyAllModels();
		destroyAllShaders();
	}

	/**
	 * Clears all shaders from openGl memory.
	 */
	protected void destroyAllShaders(){
		for(Shader shader : loadedShaders){
			GLWrapper.glDeleteProgram(shader.getProgramId());
		}
		loadedShaders.clear();
	}

	/**
	 * Clears all models from openGl memory
	 */
	/*
	protected void destroyAllModels(){
		for(Entry<String, LoadedObjectHandler<Model>> entry : LoadedModels.entrySet()){
			entry.getValue().getAttachedObject().destroy();
		}
	}*/

	/** Get the time in milliseconds
	 *
	 * @return The system time in milliseconds
	 */
	public double getTime() {
		return System.nanoTime() / 1000000d;
	}

	/** Calculate how many milliseconds have passed
	 * since last frame.
	 *
	 * @return milliseconds passed since last frame
	 */
	protected void calculateFrameDelta() {
		double time = getTime();
		delta = (float) (time - lastFrame);
		if (delta > 100) {
			//FPS is LOWER than 10!
			//Reduce the delta so things don't go crazy.
			delta = 100f;
		}
		lastFrame = time;
	}

	public static float getDelta(){
		return delta;
	}

	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			if (fps <= DEAD_RENDER) {
				/*
				 * TODO - This is a dodgy fix, if I am to hold
				 * the [x] button for 1 second, the game will close,
				 * regardless of if I am to release it or not.
				 */
				//Render.getInstance().closeGame(1);
			}
			//System.out.println("FPS: " + fps);
			setFps(0);
			lastFPS += 1000;
		}
		setFps(fps + 1);
	}

	public void setFps(float newFps){
		fps = newFps;
	}

	/*
	protected Entity firstTransparentEntity;
	protected Entity lastTransparentEntity;
	*/

	public void enforceFramerateCap(int frameRateCap){
		if(frameRateCap < 0){
			enforceFramerateCap = false;
		} else {
			enforceFramerateCap = true;
			forcedFramerateCap = frameRateCap;
		}
	}
	
	public Shader2D getDefault2DShader() {
		// Implement in the super class. This should be abstract.
		throw new NotImplementedException("getDefault2DShader used and not implemented!");
	}

	public Shader3D getDefault3DShader() {
		// Implement in the super class. This should be abstract.
		throw new NotImplementedException("getDefault3DShader used and not implemented!");
	}
	
	public static void setRenderer(Render render){
		//Don't setup twice!
		if(instance == null){
			Loader.getInstance();
			instance = render;
			instance.initialize();
			instance.setupOpenGl();
			instance.setupSpashScreen();
			instance.loadRawDataFromFile(instance.rootObjectLoadHandler);
			instance.handleRawData(instance.rootObjectLoadHandler);
			instance.addWaitingLoadJob(instance.rootObjectLoadHandler);
			instance.start();
		}
	}

	protected void setupRootUI() {
		//Setup the root ui.
		rootObjectLoadHandler.newDependancy(rootUI);
		Renderable2DUpdateHandler.addRenderableComponent(rootUI);
	}
	
	public static Render getInstance(){
		return instance;
	}
	/*
	private Render(Game game){
		this.game = game;
		//resize(800, 600);
	}
	*/
	public void addLoadedEntity(Entity entity){
		//System.out.println("Entity added: " + entity.getName());
		entities.add(entity);
	}

	public void removeLoadedEntity(Entity entity) {
		entities.remove(entity);
	}

	public boolean entityInRenderList(Entity entity) {
		return entities.contains(entity);
	}

	public Entity[] getEntities() {
		return entities.toArray(new Entity[entities.size()]);
	}
	
	private HashMap<String, Boolean> requiredFiles = new HashMap<String, Boolean>();

	private void setupOpenGl(){
		setupOpenGL();
		setupShaders();
		exitOnGLError("setupShaders");
		QUAD_2D.setupQuad();
	}

	private TextureInfo mysticTexture;
	private float mysticTextureRotation = -0.0f;
	private float mysticTextureSize = 512f;
	private final float mysticTextureSizeSpeed = 40.0f / 1000.0f;
	private final float mysticTextureRotationSpeed = 0.05f / 1000f;

	public void setCurrentCamera(Camera camera) {
		this.tempStoredCamera = this.camera;
		this.camera = camera;
	}

	public void resetCamera() {
		this.camera = this.tempStoredCamera;
		this.tempStoredCamera = this.camera;
	}

	public CurrentInput getCurrentInput() {
		return currentInput;
	}


    private class SpashScreenTexture extends TextureInfo {
		public SpashScreenTexture(String texturePath){
			this.filePath = texturePath;
			loadRawDataFromFile(null);
			handleRawData(null);
			loadDependencies(null);
			completeLoad(null);
		}
	}


	private void setupSpashScreen(){
		System.out.println("Splash Screen to be added!");
		System.out.println("Powered by HeZe.");
		mysticTexture = new SpashScreenTexture(FileReader.asSharedFile("mystic/mystic.png"));
	}


	public void start() {

		rootUI.addComponentToTop(TopLevelInterface.getInstance());


		// Initialize OpenGL (Display)

		calculateFrameDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
		int errorCode = 0;

		try {
			while (!Display.isCloseRequested() && !ThreadDestroyer.shouldCloseGame()) {

				// Do a single loop (logic/render)
				calculateFrameDelta();
				loopCycle(delta);
				updateFPS();
				currentFrame++;

				// Let the CPU synchronize with the GPU if GPU is tagging behind
				Display.update();

				afterDisplayUpdate();

				//BY THE NINE DIVINES! update() before you sync for MUCH smoother frames!!!
				// Force a maximum FPS of about 120
				if(enforceFramerateCap){
					Display.sync(forcedFramerateCap);
				}
			}
			System.out.println("Close Game Normally");
		} catch (Throwable e) {
			if (e instanceof Exception) {
				criticalGameException = (Exception) e;
				errorCode = ExitTypes.UNEXPECTED_RENDER_EXCEPTION;
			} else {
                /*
                 * This is an ERROR not an EXCEPTION,
                 * Something seriously bad has happened,
                 * Exit Immediately.
                 */
				e.printStackTrace();
				System.exit(ExitTypes.UNEXPECTED_JVM_ERROR);
			}
		} finally {
			/*
			 	if there is an exception thrown it will close the
				Display and shutdown threads correctly instead of just hanging.
			 */
			// Destroy OpenGL (Display) & Threads & Stuff
			closeGame(errorCode);
		}
	}


	/**
	 * Called after:
	 * Display.update();
	 * and before:
	 * Display.sync(forcedFramerateCap);
     */
	protected void afterDisplayUpdate() {}
	
	protected abstract void setupShaders();

	protected abstract void logicCycle(float delta);
	
	protected abstract void renderCycle();

	protected void loopCycle(float delta) {
		exitOnGLError("Start loop cycle");
		frameNumber++;

		//Reset the clip plane.
		setCurrentClipPlane(0, 1, 0, 10000);

		//Loading Stuffs.
		checkWaitingJobs();
		if(this.rootObjectLoadHandler.isLoaded()){
			exitOnGLError("rootui before");

			input.updateInput(delta); //Update the current input.

			//Update 2D Elements.
			rootUI.moveComponentToTop(TopLevelInterface.getInstance());


			rootUI.update(input, delta);

			exitOnGLError("rootui after");

			//Update all the entities.
			ListIterator<Entity> entityListIterator = entities.listIterator();
			for (int entityNo = 0; entityListIterator.hasNext(); entityNo++) {
				Entity nextEntity = entityListIterator.next();
				nextEntity.update(input, delta);
			}

			// Update logic
			logicCycle(delta);

			Renderable2DUpdateHandler.update2DObjectPosition();

			exitOnGLError("logicCycle");

			if (currentView != null) {
				currentView.logicCycle(input, delta);
				exitOnGLError("view logicCycle");
				currentView.renderCycle();
				exitOnGLError("view renderCycle");
			}

			//TODO Transparency should probably be handled on the Camera using an Insertion Sort. (For the best average case complexity)

			// Update rendered frame
			renderCycle();
			exitOnGLError("renderCycle");

			FileChooser.handleFileChooserIfApplicable();
			
		} else {
            //Show the splash screen.

			GLWrapper.glClearColor(0, 0, 0, 1);
			GLWrapper.glClear(GLWrapper.GL_COLOR_BUFFER_BIT | GLWrapper.GL_DEPTH_BUFFER_BIT);

			//mysticTextureRotation += delta * mysticTextureRotationSpeed;
			mysticTextureSize += delta * mysticTextureSizeSpeed;


			GLWrapper.glBindTexture(GLWrapper.GL_TEXTURE_2D, mysticTexture.getTextureId());
			Shader2D shader2D = getDefault2DShader();
			setShader(shader2D);
			shader2D.setQuadLocation(WIDTH / 2f, HEIGHT / 2f);
			shader2D.setQuadSize(mysticTextureSize, mysticTextureSize);
			shader2D.setQuadRotation(mysticTextureRotation);
			shader2D.setTextureOffset(0, 0);
			shader2D.setTextureSize(1, 1);

			Quad.bind();
			Quad.render();
        }
		
		exitOnGLError("loopCycle");
	}	
	

	protected void renderEntities(Shader3D shader3D) {
		renderEntities(shader3D, camera);
	}

	public void renderEntities(Shader3D shader3D, Camera worldCamera) {
		prepareToRender(shader3D);
		for (Entity entity : entities) {
			entity.render(shader3D);
		}
	}

	protected void prepare3DCamera(Shader3D shader3D, Camera worldCamera){
		worldCamera.calculateRotationMatrix();
		shader3D.setCameraPosition(worldCamera.getPosition());
		shader3D.setProjectionRotationMatrix(worldCamera.getProjectionRotationMatrix());
	}

	public void renderWorld(Shader3D shader3D, Camera worldCamera) {
		throw new NotImplementedException("Not implemented lol");
	}

	protected void renderPickingEntities(Picking3DShader shader3D) {
		prepareToRender(currentShader);
		for (int a = 0; a < entities.size(); a++) {
			entities.get(a).renderAsPicking(shader3D, a);
		}
	}

	protected abstract void prepareToRender(Shader shader);

	public void toggleMainMenu(){
		System.out.println("Main menu was toggled, but not implemetned.");
	}
	

	
	public Camera getCamera() {
		return this.camera;
	}
	
	public Vector2f toGlCoords(Vector2f screenPosition) {
		return new Vector2f((screenPosition.x - WIDTH/2) / (WIDTH/2), (screenPosition.y - HEIGHT/2) / (HEIGHT/2));
	}

	public static int getCurrentFrame() {
		return currentFrame;
	}

    public void exitOnGLError(String errorMessage) {
        int errorValue = GLWrapper.glGetError();


        if (errorValue != GLWrapper.GL_NO_ERROR) {
            String errorString = GLWrapper.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);
            destroyAll();
            System.exit(ExitTypes.UNEXPECTED_OPENGL_ERROR);
        }
    }

    public void closeGame(int status){
        //If the server/client logger is running, suspend it.

        try {
            // Fix for AMD, Alec's laptop - closing in fullscreen will crash the graphics drivers
            DisplayMode safeMode = new DisplayMode(800, 600);
            setDisplayMode(safeMode, false);
        } catch (Exception e){

        }

        try {
            destroyAll();
        } catch (Exception e) {
            System.err.println("Error occurred when calling: destroyAll(), this should be fixed!");
            e.printStackTrace();
        }
        if (criticalGameException != null) {
            System.err.println("Game exited due to a critical error:");
            criticalGameException.printStackTrace();
        }
        if (status != 0) {
            System.err.println("To determine what each exit status means, please refer to: ExitTypes.java");
        }
        System.exit(status);
    }
	
	protected void destroyAll(){
		/*
		 * Destroy all running threads.
		 */

        ThreadDestroyer.setShouldCloseGame(true);

		//Destroy the loader, and it's threads.
		Loader.getInstance().destroy();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		destroyShadersModelsTextures();
		
		QUAD_2D.destroy();
		
		/*
		 * Destroy all remaining OpenGL.
		 */
		destroyOpenGL();
		
		
		int errorValue = GLWrapper.glGetError();
		
		if (errorValue != GLWrapper.GL_NO_ERROR) {
			String errorString = GLWrapper.gluErrorString(errorValue);
			System.err.println("ERROR - " + "destroyOpenGL" + ": " + errorString);
		}
		Display.destroy();
	}

}
