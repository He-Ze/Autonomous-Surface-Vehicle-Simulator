package gebd;

import blindmystics.util.FileReader;
import blindmystics.util.GLWrapper;
import loader.LoadedObjectAbstract;
import loader.LoadedObjectHandler;
import objUtils.EntRead;
import org.lwjgl.BufferUtils;
import renderables.Vertex;
import renderables.r3D.model.*;
import renderables.r3D.generated.GeneratedModel;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.ListIterator;
import java.nio.ShortBuffer;

/**
 * TODO Indices is presently broken - 1 index per vertex
 * TODO detail/physics models are presently loaded in redundantly - problem as the physics models are commonly reused
 */
public class ModelsetPlus extends LoadedObjectAbstract {
	public static final int INDEX_TYPE = GLWrapper.MAX_INDEX_TYPE;
	public static final int INDEX_BYTES = GLWrapper.MAX_INDEX_TYPE_BYTES;

	private int numberOfModels;
	private int numberOfIndices = 0;
	private int numberOfVertices = 0;
	private int indexPointer = 0;
	private int vertexPointer = 0;
	EntRead[] modelEnts;
	EntRead[] physicsEnts;
	EntRead[] graphicsEnts;

	private ModelsetModel[] models;
	private int modelNumber;

	protected int[] allIndices;
	protected Vertex[] allVertices;

	protected int vertexVBO;
	protected int indexVBO;

	protected String[] modelNames;
	protected String[] paths;
	protected String[] texturePaths;
	protected String[] physicsModelPaths = null;
	protected String[] graphicsModelPaths = null;

	private boolean usingPhysicsModels = false;
	private boolean usingGraphicsModels = false;

	private LoadedObjectHandler.LoadStatus currentStatus;

	private ArrayList<GeneratedModel> allGeneratedModels = new ArrayList<>();

	private boolean isLoaded = false;

	@Override
	public LoadedObjectHandler.LoadStage[] stagesToPerform(){
		return new LoadedObjectHandler.LoadStage[] {
				LoadedObjectHandler.LoadStage.LOAD_DATA_FROM_FILE,
				LoadedObjectHandler.LoadStage.HANDLE_RAW_DATA,
				LoadedObjectHandler.LoadStage.LOAD_DEPENDENCIES,
		};
	}

	@Override
	public LoadedObjectHandler.LoadStatus getLoadStatus() {
		return currentStatus;
	}

	@Override
	public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {
		this.currentStatus = newLoadStatus;
	}

	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		loadEnts(paths, physicsModelPaths, graphicsModelPaths);
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) {
		//TODO
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		//TODO
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		setLoadStatus(LoadedObjectHandler.LoadStatus.COMPLETE_LOAD);
		prepare();
		load();
	}

	//TODO Indices is presently broken - 1 index per vertex
	//TODO detail/physics models are presently loaded in redundantly - problem as the physics models are commonly reused
	public ModelsetPlus(String[] names, String[] paths, String[] texturePaths) {
		this(names, paths, texturePaths, null, null);
	}

	public ModelsetPlus(String[] names, String[] paths, String[] texturePaths, String[] physicsModelPaths) {
		this(names, paths, texturePaths, physicsModelPaths, null);
	}

	public ModelsetPlus(String[] names, String[] paths, String[] texturePaths, String[] physicsModelPaths, String[] graphicsModelPaths) {
		numberOfModels = names.length;
		this.modelNames = names;
		this.paths = paths;
		this.texturePaths = texturePaths;

		if (physicsModelPaths != null) {
			usingPhysicsModels = true;
			this.physicsModelPaths = physicsModelPaths;
		}

		if (graphicsModelPaths != null) {
			usingGraphicsModels = true;
			this.graphicsModelPaths = graphicsModelPaths;
		}
	}

	public String getModelPath(int index) {
		return paths[index];
	}

	public String getTexturePath(int index) {
		System.out.println("Texture: " + texturePaths[index] + " " + index);
		return texturePaths[index];
	}

	public String getTexturePath(String identifier) {
		for (int a = 0; a < modelNames.length; a++) {
			if (modelNames[a].equals(identifier)) {
				return texturePaths[a];
			}
		}
		return null;
	}

	public ModelsetModel getModel(int index) {
		return models[index];
	}

	public ModelsetModel getModel(String identifier, boolean name) {
		for (ModelsetModel model : models) {
			if (name && model.getModelName().equals(identifier)) {
				return model;
			} else if (!name && model.getModelPath().equals(identifier)) {
				return model;
			}
		}
		return null;
	}

	public int getNumberOfModels() {
		return numberOfModels;
	}

	protected void loadEnts(String[] paths, String[] physicsPaths, String[] graphicsPaths) {
		InputStream detailFile, physicsFile, graphicsFile;

		modelEnts = new EntRead[numberOfModels];
		if (usingPhysicsModels) {
			physicsEnts = new EntRead[numberOfModels];
		}

		if (usingGraphicsModels) {
			graphicsEnts = new EntRead[numberOfModels];
		}
		EntRead detail, physics, graphics;

		for (int a = 0; a < numberOfModels; a++) {
			detailFile = FileReader.asInputStream(paths[a]);
			detail = new EntRead(detailFile, paths[a]);
			modelEnts[a] = detail;
			numberOfIndices += detail.getIndices().length;
			numberOfVertices += detail.getIndices().length;

			if (usingPhysicsModels) {
				if (physicsPaths[a] != null) {
					physicsFile = FileReader.asInputStream(physicsPaths[a]);
					physics = new EntRead(physicsFile, physicsPaths[a]);
					physicsEnts[a] = physics;
					numberOfIndices += physics.getIndices().length;
					numberOfVertices += physics.getIndices().length;
				}
			}

			if (usingGraphicsModels) {
				if (graphicsPaths[a] != null) {
					graphicsFile = FileReader.asInputStream(graphicsPaths[a]);
					graphics = new EntRead(graphicsFile, graphicsPaths[a]);
					graphicsEnts[a] = graphics;
					numberOfIndices += graphics.getIndices().length;
					numberOfVertices += graphics.getIndices().length;
				}
			}
		}
	}

	public <T extends GeneratedModel> T loadGeneratedModel(T generatedModel) {
		if (getLoadStatus() == LoadedObjectHandler.LoadStatus.COMPLETE_LOAD) {
			throw new UnsupportedOperationException("This method can't be called after the complete load!");
		}
		allGeneratedModels.add(generatedModel);
		numberOfIndices += generatedModel.getIndices().length;
		numberOfVertices += generatedModel.getVertices().length;
		numberOfModels++;
		return generatedModel;
	}

	private void prepare() {
		numberOfModels = modelNames.length + allGeneratedModels.size();
		models = new ModelsetModel[numberOfModels];

		EntRead detail = null, physics = null, graphics = null;
		allVertices = new Vertex[numberOfVertices];
		Vertex[] physicsVertices;
		allIndices = new int[numberOfIndices];
		int[] indices;
		float[] xyz, uv, normal;
		ModelsetModel graphicsModel;
		PhysicsModelsetModel physicsModel;

		int detail_pointer;
		int graphics_pointer;
		int physics_pointer;

		for (int a = 0; a < modelEnts.length; a++) {
			System.out.println("Processing Model - " + paths[a]);
			System.out.println(numberOfIndices + " " + modelEnts[a].getIndices().length);
			graphicsModel = null;
			physicsModel = null;

			//Detail model
			detail = modelEnts[a];
			indices = detail.getIndices();
			xyz = detail.getXyz();
			uv = detail.getUv();
			normal = detail.getNormals();

			detail_pointer = vertexPointer;

			for (int vertexNo = 0; vertexNo < indices.length; vertexNo++) {
				allVertices[vertexPointer++] = new Vertex(
						xyz[vertexNo * Vertex.POSITION_ELEMENT_COUNT],
						xyz[vertexNo * Vertex.POSITION_ELEMENT_COUNT + 1],
						xyz[vertexNo * Vertex.POSITION_ELEMENT_COUNT + 2],
						uv[vertexNo * Vertex.TEXTURE_ELEMENT_COUNT],
						uv[vertexNo * Vertex.TEXTURE_ELEMENT_COUNT + 1],
						normal[vertexNo * Vertex.NORMAL_ELEMENT_COUNT],
						normal[vertexNo * Vertex.NORMAL_ELEMENT_COUNT + 1],
						normal[vertexNo * Vertex.NORMAL_ELEMENT_COUNT + 2]
				);
			}
			for (int indexNo = 0; indexNo < indices.length; indexNo++) {
				this.allIndices[indexPointer++] = indices[indexNo] + detail_pointer;
			}

			if (usingGraphicsModels && graphicsEnts[a] != null) {
				graphics = graphicsEnts[a];
				indices = graphics.getIndices();
				xyz = graphics.getXyz();
				uv = graphics.getUv();
				normal = graphics.getNormals();

				graphics_pointer = vertexPointer;

				for (int b = 0; b < indices.length; b++) {
					allVertices[vertexPointer++] = new Vertex(
							xyz[b * Vertex.POSITION_ELEMENT_COUNT],
							xyz[b * Vertex.POSITION_ELEMENT_COUNT + 1],
							xyz[b * Vertex.POSITION_ELEMENT_COUNT + 2],
							uv[b * Vertex.TEXTURE_ELEMENT_COUNT],
							uv[b * Vertex.TEXTURE_ELEMENT_COUNT + 1],
							normal[b * Vertex.NORMAL_ELEMENT_COUNT],
							normal[b * Vertex.NORMAL_ELEMENT_COUNT + 1],
							normal[b * Vertex.NORMAL_ELEMENT_COUNT + 2]
					);
					this.allIndices[indexPointer++] = indices[b] + graphics_pointer;
				}

				if (graphics.isAnimated()) {
					graphicsModel = new AnimatedModelsetModel(modelNames[a], graphicsModelPaths[a], graphics_pointer, graphics.getPointers(), graphics.getLengths(), graphics.getMinimums(), graphics.getMaximums());
				} else {
					graphicsModel = new ModelsetModel(modelNames[a], graphicsModelPaths[a], graphics_pointer, graphics.getIndices().length, graphics.getMinimums()[0], graphics.getMaximums()[0]);
				}
			}
			//Physics model
			if (usingPhysicsModels && physicsEnts[a] != null) {
				physics = physicsEnts[a];
				indices = physics.getIndices();
				physicsVertices = new Vertex[indices.length];
				xyz = physics.getXyz();
				uv = physics.getUv();
				normal = physics.getNormals();

				physics_pointer = vertexPointer;

				for (int b = 0; b < indices.length; b++) {
					allVertices[vertexPointer++] = new Vertex(
							xyz[b * Vertex.POSITION_ELEMENT_COUNT],
							xyz[b * Vertex.POSITION_ELEMENT_COUNT + 1],
							xyz[b * Vertex.POSITION_ELEMENT_COUNT + 2],
							uv[b * Vertex.TEXTURE_ELEMENT_COUNT],
							uv[b * Vertex.TEXTURE_ELEMENT_COUNT + 1],
							normal[b * Vertex.NORMAL_ELEMENT_COUNT],
							normal[b * Vertex.NORMAL_ELEMENT_COUNT + 1],
							normal[b * Vertex.NORMAL_ELEMENT_COUNT + 2]
					);
					physicsVertices[b] = allVertices[physics_pointer + b];
					this.allIndices[indexPointer++] = indices[b] + physics_pointer;
				}

				if (physics.isAnimated()) {
					physicsModel = new AnimatedPhysicsModelsetModel(modelNames[a], physicsModelPaths[a], physics_pointer, physics.getPointers(), physics.getLengths(), physicsVertices);
				} else {
					physicsModel = new PhysicsModelsetModel(modelNames[a], physicsModelPaths[a], physics_pointer, physics.getIndices().length, physicsVertices);
				}
			}

			if (detail.isAnimated()) {
				models[modelNumber++] = ModelsetModelInstanceHandler.addNewModel(new AnimatedModelsetModel(modelNames[a], paths[a], detail_pointer, detail.getPointers(), detail.getLengths(), detail.getMinimums(), detail.getMaximums(), physicsModel, graphicsModel));
			} else {
				models[modelNumber++] = ModelsetModelInstanceHandler.addNewModel(new ModelsetModel(modelNames[a], paths[a], detail_pointer, detail.getIndices().length, detail.getMinimums()[0], detail.getMaximums()[0], physicsModel, graphicsModel));
			}
		}

		loadGeneratedModels();
	}


	private void loadGeneratedModels() {
		//allGeneratedModels
		ListIterator<GeneratedModel> generatedModelListIterator = allGeneratedModels.listIterator();
		while (generatedModelListIterator.hasNext()) {
			GeneratedModel generatedModel = generatedModelListIterator.next();

			Vertex[] modelVertices = generatedModel.getVertices();
			int[] modelIndices = generatedModel.getIndices();
			int modelPointer = vertexPointer;

			for (int vertexNo = 0; vertexNo < modelVertices.length; vertexNo++) {
				allVertices[vertexPointer++] = modelVertices[vertexNo];
			}

			for (int indexNo = 0; indexNo < modelIndices.length; indexNo++) {
				allIndices[indexPointer++] = modelIndices[indexNo] + modelPointer;
			}

			generatedModel.setPointer(modelPointer);
			generatedModel.setLength(modelIndices.length);
		}
	}

	protected float[] createFloatArray() {
		float[] data = new float[allVertices.length * Vertex.STRIDE];
		int index = 0;

		for (Vertex vertex : allVertices) {
			for (float f : vertex.getElements()) {
				data[index] = f;
				index++;
			}
		}
		return data;
	}

	public void load() {
		if (!isLoaded) {
			float[] vertexData = createFloatArray();

			FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexData.length);
			vertexBuffer.put(vertexData);
			vertexBuffer.flip();

			Render.getInstance().exitOnGLError("ERROR - Modelset I don't know error");

			//When changing this value, remember to update the INDEX_BYTES and INDEX_TYPE values
			if (INDEX_TYPE == GLWrapper.GL_UNSIGNED_SHORT) {
				ShortBuffer indexBuffer = BufferUtils.createShortBuffer(allIndices.length);
				short[] shortIndices = new short[allIndices.length];
				for (int a = 0; a < allIndices.length; a++) {
					shortIndices[a] = (short) allIndices[a];
				}

				indexBuffer.put(shortIndices);
				indexBuffer.flip();

				indexVBO = GLWrapper.glGenBuffers();
				GLWrapper.glBindBuffer(GLWrapper.GL_ELEMENT_ARRAY_BUFFER, indexVBO);
				GLWrapper.glBufferData(GLWrapper.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GLWrapper.GL_STATIC_DRAW);
				System.out.println("DEBUG - modelset " + vertexData.length + " " + allIndices.length);
			} else {
				IntBuffer indexBuffer = BufferUtils.createIntBuffer(allIndices.length);
				indexBuffer.put(allIndices);
				indexBuffer.flip();

				indexVBO = GLWrapper.glGenBuffers();
				GLWrapper.glBindBuffer(GLWrapper.GL_ELEMENT_ARRAY_BUFFER, indexVBO);
				GLWrapper.glBufferData(GLWrapper.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GLWrapper.GL_STATIC_DRAW);
			}

			Render.getInstance().exitOnGLError("ERROR - Modelset IBO load error");


			//Load Vertex Data
			vertexVBO = GLWrapper.glGenBuffers();
			GLWrapper.glBindBuffer(GLWrapper.GL_ARRAY_BUFFER, vertexVBO);
			GLWrapper.glBufferData(GLWrapper.GL_ARRAY_BUFFER, vertexBuffer, GLWrapper.GL_STATIC_DRAW);

			Render.getInstance().exitOnGLError("ERROR - Modelset VBO load error");
			GLWrapper.glVertexAttribPointer(0, Vertex.POSITION_ELEMENT_COUNT, GLWrapper.GL_FLOAT, false, Vertex.STRIDE, Vertex.POSITION_BYTE_OFFSET);
			GLWrapper.glVertexAttribPointer(1, Vertex.TEXTURE_ELEMENT_COUNT, GLWrapper.GL_FLOAT, false, Vertex.STRIDE, Vertex.TEXTURE_BYTE_OFFSET);
			GLWrapper.glVertexAttribPointer(2, Vertex.NORMAL_ELEMENT_COUNT, GLWrapper.GL_FLOAT, false, Vertex.STRIDE, Vertex.NORMAL_BYTE_OFFSET);

			Render.getInstance().exitOnGLError("ERROR - Modelset attribute load error");
			//GLWrapper.glBindVertexArray(0);
			GLWrapper.glBindBuffer(GLWrapper.GL_ARRAY_BUFFER, 0);
			GLWrapper.glBindBuffer(GLWrapper.GL_ELEMENT_ARRAY_BUFFER, 0); //This must be unbound after the array buffer


			Render.getInstance().exitOnGLError("ERROR - Modelset unbind error");
			isLoaded = true;
		}
	}

	public void bind() {
		GLWrapper.glBindBuffer(GLWrapper.GL_ARRAY_BUFFER, vertexVBO);
		GLWrapper.glBindBuffer(GLWrapper.GL_ELEMENT_ARRAY_BUFFER, indexVBO);

		GLWrapper.glEnableVertexAttribArray(0);
		GLWrapper.glEnableVertexAttribArray(1);
		GLWrapper.glEnableVertexAttribArray(2);

		GLWrapper.glVertexAttribPointer(0, Vertex.POSITION_ELEMENT_COUNT, GLWrapper.GL_FLOAT, false, Vertex.STRIDE, Vertex.POSITION_BYTE_OFFSET);
		GLWrapper.glVertexAttribPointer(1, Vertex.TEXTURE_ELEMENT_COUNT, GLWrapper.GL_FLOAT, false, Vertex.STRIDE, Vertex.TEXTURE_BYTE_OFFSET);
		GLWrapper.glVertexAttribPointer(2, Vertex.NORMAL_ELEMENT_COUNT, GLWrapper.GL_FLOAT, false, Vertex.STRIDE, Vertex.NORMAL_BYTE_OFFSET);
	}

	public void destroy() {
		// Disable the VBO index from the VAO attributes list
		GLWrapper.glEnableVertexAttribArray(0);
		GLWrapper.glEnableVertexAttribArray(1);
		GLWrapper.glEnableVertexAttribArray(2);

		// Delete the vertex VBO
		GLWrapper.glBindBuffer(GLWrapper.GL_ARRAY_BUFFER, 0);
		GLWrapper.glDeleteBuffers(vertexVBO);

		// Delete the index VBO
		GLWrapper.glBindBuffer(GLWrapper.GL_ELEMENT_ARRAY_BUFFER, 0);
		GLWrapper.glDeleteBuffers(indexVBO);

		int errorValue = GLWrapper.glGetError();

		if (errorValue != GLWrapper.GL_NO_ERROR) {
			String errorString = GLWrapper.gluErrorString(errorValue);
			System.err.println("ERROR - " + "destroyingTheQuad" + ": " + errorString);
		}
	}
}