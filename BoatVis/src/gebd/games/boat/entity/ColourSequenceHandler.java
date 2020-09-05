package gebd.games.boat.entity;

import blindmystics.input.CurrentInput;
import blindmystics.util.input.Updates;
import composites.entities.Entity;
import loader.LoadedObjectHandler;

import javax.vecmath.Vector3f;
import java.util.ArrayList;

/**
 * Created by p3te on 8/11/16.
 */
public class ColourSequenceHandler implements Updates {

    private ArrayList<ColourSequenceNode> colourSequenceNodes = new ArrayList<>();
    private float colourElapsedTime = 0f;
    private int currentSequenceIndex = 0;

    private Vector3f currentColour = new Vector3f(1f, 1f, 1f);
    private float currentBlendAmount = 1f;

    private ArrayList<Entity> colouredEntites = new ArrayList<>();

    @Override
    public void update(CurrentInput input, float delta) {
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
                currentColour.set(colourSequenceNodes.get(currentSequenceIndex).getColour());
                currentBlendAmount = colourSequenceNodes.get(currentSequenceIndex).getColourBlendAmount();

                //Update all entities.
                for (Entity entity : colouredEntites) {
                    entity.setTextureBlendColour(currentColour);
                    entity.setTextureBlendAmount(currentBlendAmount);
                }
            }

        } else {
            colourElapsedTime = 0f;
        }
    }

    public Vector3f getCurrentColour() {
        return currentColour;
    }

    public float getCurrentBlendAmount() {
        return currentBlendAmount;
    }

    public ArrayList<ColourSequenceNode> getColourSequenceNodes() {
        return colourSequenceNodes;
    }

    public void addColouredEntity(Entity entity) {
        if (colouredEntites.contains(entity)) {
            //Adready in the list.
            return;
        }
        colouredEntites.add(entity);
    }

    public boolean removeEntity(Entity entity) {
        return colouredEntites.remove(entity);
    }

    public boolean contains(Entity entity) {
        return colouredEntites.contains(entity);
    }

    public void toggleEntityUseSequence(Entity entity) {
        if (contains(entity)) {
            removeEntity(entity);
        } else {
            addColouredEntity(entity);
        }
    }

    public ArrayList<Entity> getColouredEntites() {
        return colouredEntites;
    }
}
