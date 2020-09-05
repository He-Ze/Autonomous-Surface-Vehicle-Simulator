package renderables.r3D.generated;

import renderables.Vertex;
import renderables.r3D.model.ModelsetModel;

/**
 * Created by CaptainPete on 10/07/2016.
 */
public abstract class GeneratedModel extends ModelsetModel {

    protected Vertex[] vertices;
    protected int[] indices;


    public Vertex[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }
}
