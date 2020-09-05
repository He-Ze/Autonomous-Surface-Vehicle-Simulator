package renderables.r3D.skybox;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;
import renderables.Vertex;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by CaptainPete on 16/04/2016.
 */
public class RawModel {

    private static LinkedList<Integer> vaos = new LinkedList<>();
    private static LinkedList<Integer> vbos = new LinkedList<>();


    private int vaoId;
    private int vertexCount;

    private RawModel(int vaoId, int vertexCount) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public static void destroyAllRawModels() {
        ListIterator<Integer> vaoIterator = vaos.listIterator();
        while (vaoIterator.hasNext()) {
            Integer vao = vaoIterator.next();
            GL30.glDeleteVertexArrays(vao);
        }

        ListIterator<Integer> vboIterator = vbos.listIterator();
        while (vboIterator.hasNext()) {
            Integer vbo = vboIterator.next();
            GL15.glDeleteBuffers(vbo);
        }
    }


    public static RawModel loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        storeDataInAttribureList(0, dimensions, positions);
        unbindVao();
        RawModel rawModel = new RawModel(vaoID, positions.length / dimensions);
        int errorValue = GL11.glGetError();
        return rawModel;
    }

    private static int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }


    private static void storeDataInAttribureList(int attributeNumber, int coordinateSize, float[] data) {
        //TODO - I'm not sure what dimensions do!!!
        int vboID = GL15.glGenBuffers();
        FloatBuffer buffer = storeDataInFloatBuffer(data);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
//        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
//        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, coordinateSize * Float.BYTES, 0);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, coordinateSize * 4, 0);


//        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexVBO);
//        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
//        GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.POSITION_BYTE_OFFSET);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private static void unbindVao() {
        GL30.glBindVertexArray(0);
    }

    private static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
