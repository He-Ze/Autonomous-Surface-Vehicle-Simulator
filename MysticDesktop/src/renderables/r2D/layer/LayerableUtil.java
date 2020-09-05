package renderables.r2D.layer;

/**
 * Created by P3TE on 5/07/2016.
 */
public class LayerableUtil {

    public static void removeFromLayer(Layerable layerable){
        //Make sure that the start and end of the list aren't broken from this.
        LayerHandler parent = layerable.getParent();
        if (parent != null) {
            if (parent.firstElement == layerable) {
                parent.firstElement = layerable.getNext();
            }
            if (parent.lastElement == layerable) {
                parent.lastElement = layerable.getPrev();
            }
        }
        //Remove from the list.
        if(layerable.getPrev() != null){
            layerable.getPrev().setNext(layerable.getNext());
        }
        if (layerable.getNext() != null) {
            layerable.getNext().setPrev(layerable.getPrev());
        }
        layerable.setPrev(null);
        layerable.setNext(null);
    }

}
