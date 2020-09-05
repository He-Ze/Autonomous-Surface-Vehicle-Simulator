package renderables.r2D;

import java.nio.FloatBuffer;

import blindmystics.util.GLWrapper;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;

import renderables.Vertex2DSimple;

public class Quad {
	private static int vertexVBO;
	private Vertex2DSimple[] vertices;

	public static final Vertex2DSimple[] DEFAULT_QUAD_VERTICES(){
		Vertex2DSimple[] vertices = new Vertex2DSimple[4];
		vertices[0] = new Vertex2DSimple(-1, 1);
		vertices[1] = new Vertex2DSimple(-1, -1);
		vertices[2] = new Vertex2DSimple(1, -1);
		vertices[3] = new Vertex2DSimple(1, 1);
		return vertices;
	}

	public Quad(){
		this.vertices = Quad.DEFAULT_QUAD_VERTICES();
	}

	public Quad(Vertex2DSimple[] vertices){
		this.vertices = vertices;
	}

	private FloatBuffer prepareVertexArray() {
		// Put each 'Vertex' in one FloatBuffer
		FloatBuffer verticesFloatBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex2DSimple.STRIDE);
		for (int i = 0; i < vertices.length; i++) {
			// Add position, color and texture floats to the buffer
			verticesFloatBuffer.put(vertices[i].getElements());
		}
		verticesFloatBuffer.flip();
		return verticesFloatBuffer;
	}


	public void setupQuad() {
		FloatBuffer vertexBuffer = prepareVertexArray();
		vertexVBO = GLWrapper.glGenBuffers();

		GLWrapper.glBindBuffer(GLWrapper.GL_ARRAY_BUFFER, vertexVBO);
		GLWrapper.glBufferData(GLWrapper.GL_ARRAY_BUFFER, vertexBuffer, GLWrapper.GL_STATIC_DRAW);
		GLWrapper.glVertexAttribPointer(0, Vertex2DSimple.POSITION_ELEMENT_COUNT, GLWrapper.GL_FLOAT, false, Vertex2DSimple.STRIDE, Vertex2DSimple.POSITION_BYTE_OFFSET);

		GLWrapper.glBindBuffer(GLWrapper.GL_ARRAY_BUFFER, 0);
	}

	public static void bind(){
		GLWrapper.glBindBuffer(GLWrapper.GL_ARRAY_BUFFER, vertexVBO);

		GLWrapper.glEnableVertexAttribArray(0);
		GLWrapper.glDisableVertexAttribArray(1);
		GLWrapper.glDisableVertexAttribArray(2);
		GLWrapper.glVertexAttribPointer(0, Vertex2DSimple.POSITION_ELEMENT_COUNT, GLWrapper.GL_FLOAT, false, Vertex2DSimple.STRIDE, Vertex2DSimple.POSITION_BYTE_OFFSET);
	}

	public static void render() {
		GLWrapper.glDrawArrays(GLWrapper.GL_QUADS, 0, 4);
	}

	public void destroy() {
		// Disable the VBO index from the VAO attributes list
		GLWrapper.glDisableVertexAttribArray(0);

		// Delete the vertex VBO
		GLWrapper.glBindBuffer(GLWrapper.GL_ARRAY_BUFFER, 0);
		GLWrapper.glDeleteBuffers(vertexVBO);

		int errorValue = GLWrapper.glGetError();

		if (errorValue != GLWrapper.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + "destroyingTheQuad" + ": " + errorString);
		}
	}
}