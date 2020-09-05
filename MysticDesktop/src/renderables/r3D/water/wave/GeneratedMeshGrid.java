package renderables.r3D.water.wave;

import renderables.Vertex;
import renderables.r3D.generated.GeneratedModel;

/**
 * Created by CaptainPete on 10/07/2016.
 * This is going to be interesting!
 */
public class GeneratedMeshGrid extends GeneratedModel {

    private int gridWidth;
    private int gridHeight;
    private int numVertsWide;
    private int numVertsHigh;
    private int numTriangles;
    private int totalNumVerts;

    public GeneratedMeshGrid(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        numVertsWide = gridWidth + 1;
        numVertsHigh = gridHeight + 1;
        numTriangles = gridWidth * gridHeight * 2;
        totalNumVerts = numVertsWide * numVertsHigh;
        generateGridVerticies();
        generateIndicies();
    }

    /**
     * The total width and height of this grid will be 2X2,
     * centered about 0,0
     * and it can be scaled up when needed.
     */
    private void generateGridVerticies() {
        vertices = new Vertex[totalNumVerts];
        int vertIndex = 0;

        for (int yIndex = 0; yIndex < numVertsHigh; yIndex++) {
            float v = ((float) yIndex) / ((float) gridHeight);
            float zPos = (v * 2f) - 1f;
            for (int xIndex = 0; xIndex < numVertsWide; xIndex++) {
                float u = ((float) xIndex) / ((float) gridWidth);
                float xPos = (u * 2f) - 1f;
                float yPos = 0f;


                float normalX = 0f;
                float normalY = 0f;
                float normalZ = 1f;

                Vertex newVertex = new Vertex(xPos, yPos, zPos, u, v, normalX, normalY, normalZ);
                vertices[vertIndex++] = newVertex;
            }
        }
    }


    private void generateIndicies() {
        int totalNumIndicies = 6 * gridWidth * gridHeight;
        indices = new int[totalNumIndicies];
        int currentIndiceIndex = 0;

        //TODO - Check that the counter clockwise is correct. IE the winding order is correct.
        //Triangle 1:
        final int indexOffetA = 0;
        final int indexOffetB = numVertsWide;
        final int indexOffetC = 1;
        //Triangle 2:
        final int indexOffetD = 1;
        final int indexOffetE = numVertsWide;
        final int indexOffetF = numVertsWide + 1;
        for (int row = 0; row < gridHeight; row++) {
            int offsetFromRow = numVertsWide * row;
            for (int column = 0; column < gridWidth; column++) {
                int offsetFromColumn = column;
                //Triangle 1:
                indices[currentIndiceIndex++] = indexOffetA + offsetFromRow + offsetFromColumn;
                indices[currentIndiceIndex++] = indexOffetB + offsetFromRow + offsetFromColumn;
                indices[currentIndiceIndex++] = indexOffetC + offsetFromRow + offsetFromColumn;
                //Triangle 2:
                indices[currentIndiceIndex++] = indexOffetD + offsetFromRow + offsetFromColumn;
                indices[currentIndiceIndex++] = indexOffetE + offsetFromRow + offsetFromColumn;
                indices[currentIndiceIndex++] = indexOffetF + offsetFromRow + offsetFromColumn;
            }
        }
    }
}
