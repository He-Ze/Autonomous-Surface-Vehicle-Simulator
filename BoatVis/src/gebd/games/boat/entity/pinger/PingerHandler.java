package gebd.games.boat.entity.pinger;

import composites.entities.Entity;

import java.util.ArrayList;

/**
 * Created by p3te on 21/11/16.
 */
public class PingerHandler {

    //@Require pingingBuoys.size() == pingingFrequencies.size()
    private static ArrayList<Entity> pingingBuoys = new ArrayList<>();
    private static ArrayList<Double> pingingFrequencies = new ArrayList<>();

    public static void togglePingingBuoy(Entity entity, double frequency) {
        if (pingingBuoys.contains(entity)) {
            int existingIndex = pingingBuoys.indexOf(entity);
            pingingBuoys.remove(existingIndex);
            pingingFrequencies.remove(existingIndex);
        } else {
            pingingBuoys.add(entity);
            pingingFrequencies.add(frequency);
        }
    }

    public static ArrayList<Entity> getPingingBuoys() {
        return pingingBuoys;
    }

    public static ArrayList<Double> getPingingFrequencies() {
        return pingingFrequencies;
    }

    public static int getNumPingingBuoys(){
        return pingingBuoys.size();
    }

    public static Entity getEntityAt(int index) {
        return pingingBuoys.get(index);
    }

    public static double getFrequencyAt(int index) {
        return pingingFrequencies.get(index);
    }

    public static boolean contains(Entity lastLoadedEntity) {
        return pingingBuoys.contains(lastLoadedEntity);
    }

    public static double getPingingFrequency(Entity entity) {
        int existingIndex = pingingBuoys.indexOf(entity);
        if ((existingIndex >= 0) && (existingIndex < pingingBuoys.size())) {
            return pingingFrequencies.get(existingIndex);
        }
        return -1;
    }

    public static void removeExistingEntity(Entity lastLoadedEntity) {
        int existingIndex = pingingBuoys.indexOf(lastLoadedEntity);
        if ((existingIndex >= 0) && (existingIndex < pingingBuoys.size())) {
            pingingBuoys.remove(existingIndex);
            pingingFrequencies.remove(existingIndex);
        }
    }
}
