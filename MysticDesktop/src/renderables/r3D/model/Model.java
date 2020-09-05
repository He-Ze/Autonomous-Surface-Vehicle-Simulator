package renderables.r3D.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;
import javax.vecmath.Vector3f;

import composites.entities.Entity;
import loader.LoadedObject;
import loader.LoadedObjectHandler;
import loader.LoadedObjectHandler.LoadStage;
import objUtils.EntRead;
import objUtils.EntRead;
import renderables.Vertex;
import renderables.Vertex;

public class Model {
	
	protected EntRead data;
	
	public final int XYZ_STRIDE = 3;
	public final int UV_STRIDE = 2;
	public final int NORMAL_STRIDE = 3;

	private int vao; //Storage for the vbos
	private int indexVBO;
	private int vertexVBO;

	protected String modelName;
	protected String modelENTPath;

	protected int vaoId;
	protected int vboId;
	protected int vboiId;
	protected ShortBuffer verticesByteBuffer;
	
	protected int indexCount = -1;
	
	protected Entity firstRenderedEntity = null;
	protected Entity lastRenderedEntity = null;
	
	protected int[] indices;
	protected Vertex[] vertices;
	/*
	public void addFrameEntity(Entity e){
		if(firstRenderedEntity == null){
			firstRenderedEntity = e;
			firstRenderedEntity.setNextRenderedEntity(null);
		} else {
			lastRenderedEntity.setNextRenderedEntity(e);
		}
		lastRenderedEntity = e;
	}
	
	@Override
	public LoadStage[] stagesToPerform(){
		return stagesToLoad;
	}
	
	protected LoadStage[] stagesToLoad;
	
	
	protected Model(){
		stagesToLoad = new LoadStage[] {};
	}
	
	public Model(String modelENTPath, Vector3f position, Vector3f size, Vector3f rotation) {
		stagesToLoad = new LoadStage[] {
				LoadStage.LOAD_DATA_FROM_FILE,
				LoadStage.HANDLE_RAW_DATA,
				LoadStage.LOAD_DEPENDENCIES,
		};
	}
	*/
	

	public Model(String name, String modelENTPath, EntRead ent) {
		this.modelName = name;
		this.modelENTPath = modelENTPath;
		createVertices(ent);
	}
	/*
	@Override
	public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
		System.out.println("Loading model: " + modelENTPath);
		File modelFile = new File(modelENTPath);
		if(!modelFile.exists()){
			try {
				throw new FileNotFoundException("The ENT file doesn't exist - Panic!");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		try {
			data = new EntRead(modelFile);
		} catch (Exception e){
			//Error loading config!
			e.printStackTrace();
		}
	}

	@Override
	public void handleRawData(LoadedObjectHandler<?> handler) { 
		float[] coords;
		float[] textureST;
		float[] normals;

		allIndices = data.getIndices();
		coords = data.getXyz();
		textureST = data.getUv();
		normals = data.getNormals();
		
		//System.out.println(modelENTPath + " - handleRawData");
		//System.out.println("data.getXyz() = " + String.valueOf(data.getXyz()));
		//System.out.println("coords = " + String.valueOf(coords));
		//System.out.println("coords.length = " + String.valueOf(coords.length));
		
		//Load the allVertices and the textureSt into open GL
		//Create the allVertices and store them in an array
		numOfVerts = coords.length/3;
		//System.out.println("numOfVerts = " + numOfVerts);
		allVertices = createVertices(coords, textureST, normals);
		
		data = null; //Config no longer needed - go away.
	}

	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		//NONE
	}

	@Override
	public void completeLoad(LoadedObjectHandler<?> handler) {
		setupModel();
	}
	*/

	private void createVertices(EntRead ent) {
		int[] indices = ent.getIndices();
		float[] xyz = ent.getXyz();
		float[] uv = ent.getUv();
		float[] normal = ent.getNormals();

		this.indices = new int[indices.length];

		vertices = new Vertex[indices.length];

		for (int a = 0; a < vertices.length; a++) {
			vertices[a] = new Vertex(
					xyz[a*XYZ_STRIDE], xyz[a*XYZ_STRIDE+1], xyz[a*XYZ_STRIDE+2],
					uv[a*UV_STRIDE], uv[a*UV_STRIDE+1],
					normal[a*NORMAL_STRIDE], normal[a*NORMAL_STRIDE+1], normal[a*NORMAL_STRIDE+2]
			);
			this.indices[a] = indices[a];
		}
	}

	public String getModelName() {
		return modelName;
	}
	public String getModelPath() {
		return modelENTPath;
	}


	public void load() {
		float[] vertexData = createFloatArray();

		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexData.length);
		vertexBuffer.put(vertexData);
		vertexBuffer.flip();

		IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
		indexBuffer.put(indices);
		indexBuffer.flip();


		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		indexVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexVBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);


		//Load Vertex Data
		vertexVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.POSITION_BYTE_OFFSET);
		GL20.glVertexAttribPointer(1, Vertex.TEXTURE_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.TEXTURE_BYTE_OFFSET);
		GL20.glVertexAttribPointer(2, Vertex.NORMAL_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.NORMAL_BYTE_OFFSET);

		GL30.glBindVertexArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0); //This must be unbound after the array buffer

		int errorValue = GL11.glGetError();

		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + "Model load error" + ": " + errorString);
			System.exit(-1);
		}
	}

	public void destroy() {
		// Select the VAO
		GL30.glBindVertexArray(vao);

		// Disable the VBO index from the VAO attributes list
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);

		// Delete the vertex VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vertexVBO);

		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(indexVBO);

		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vao);

		int errorValue = GL11.glGetError();

		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + "destroyingTheQuad" + ": " + errorString);
		}
	}

	private float[] createFloatArray() {
		float[] data = new float[vertices.length * Vertex.STRIDE];
		int index = 0;

		for (Vertex vertex : vertices) {
			for (float f : vertex.getElements()) {
				data[index] = f;
				index++;
			}
		}
		return data;
	}

	public void drawElements() {
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
	}
}
