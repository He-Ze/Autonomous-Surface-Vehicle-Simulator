package gebd.games.boat.entity;

import blindmystics.input.CurrentInput;
import composites.entities.Entity;
import renderables.r3D.model.ModelsetModel;
import renderables.texture.TextureInfo;

import javax.vecmath.Vector3f;
import java.util.ArrayList;

/**
 * Created by p3te on 8/11/16.
 */
public class ColourSequenceEntity extends Entity {


    private ArrayList<ColourSequenceNode> colourSequenceNodes = new ArrayList<>();
    private float colourElapsedTime = 0f;
    private int currentSequenceIndex = 0;

    public ColourSequenceEntity(String modelPath, String texturePath, Vector3f position, Vector3f size, Vector3f rotation) {
        super(modelPath, texturePath, position, size, rotation);
    }

    public ColourSequenceEntity(String name, String modelPath, String texturePath, Vector3f position, Vector3f size, Vector3f rotation) {
        super(name, modelPath, texturePath, position, size, rotation);
    }

    public ColourSequenceEntity(Vector3f position, Vector3f size, Vector3f rotation) {
        super(position, size, rotation);
    }

    public ColourSequenceEntity(String name, ModelsetModel model, String texturePath, Vector3f position, Vector3f size, Vector3f rotation) {
        super(name, model, texturePath, position, size, rotation);
    }

    public ColourSequenceEntity(String name, ModelsetModel model, TextureInfo texture, Vector3f position, Vector3f size, Vector3f rotation) {
        super(name, model, texture, position, size, rotation);
    }


    public ArrayList<ColourSequenceNode> getColourSequenceNodes() {
        return colourSequenceNodes;
    }

    public void addColourToSequence(Vector3f colour, float blendAmount, float time) {

        //ColourSequenceNode newNode = new ColourSequenceNode(colour, blendAmount, time, this, colourSequenceNodes.size());
        //colourSequenceNodes.add(newNode);

    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);

        if (!colourSequenceNodes.isEmpty()) {

            colourElapsedTime += delta;
            float maxTimeToWait = colourSequenceNodes.get(currentSequenceIndex).getDuration();
            maxTimeToWait = Math.max(maxTimeToWait, 1f); //To stop any possible infinite loops.

            boolean colourChange = false;

            while (colourElapsedTime >= maxTimeToWait) {
                colourElapsedTime -= maxTimeToWait;
                currentSequenceIndex++;
                currentSequenceIndex = currentSequenceIndex % colourSequenceNodes.size();
                maxTimeToWait = colourSequenceNodes.get(currentSequenceIndex).getDuration();
                maxTimeToWait = Math.max(maxTimeToWait, 1f); //To stop any possible infinite loops.
                colourChange = true;
            }

            if (colourChange) {
                setTextureBlendColour(colourSequenceNodes.get(currentSequenceIndex).getColour());
                setTextureBlendAmount(colourSequenceNodes.get(currentSequenceIndex).getColourBlendAmount());
            }

        } else {
            colourElapsedTime = 0f;
        }

    }
}
