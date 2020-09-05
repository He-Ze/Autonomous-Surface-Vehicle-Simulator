package gebd.camera.transparency;

import composites.entities.Entity;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 8/2/2016.
 */
public class TransparentEntityOrderHandler {

    private TransparentLinkedNode firstNode;
    private int size = 0;

    public void addTransparentEntity(Entity entity) {
        TransparentLinkedNode newTransparentLinkedNode = new TransparentLinkedNode(this, null, firstNode, entity);
        firstNode = newTransparentLinkedNode;
        size++;
    }

    void removeTransparentEntity(TransparentLinkedNode node) {
        if (firstNode == node) {
            firstNode = firstNode.getNext();
        }
        size--;
    }

    public int getSize() {
        return size;
    }

    private void determineDistancesToCamera(Vector3f cameraPosition) {
        TransparentLinkedNode currentNode = firstNode;
        while (currentNode != null) {
            Vector3f currentNodeEntityPosition = currentNode.getTransparentEntity().getPosition();
            currentNode.setDistanceToCameraSquared(getDistanceSquared(cameraPosition, currentNodeEntityPosition));
            currentNode = currentNode.getNext();
        }
    }

    private static float getDistanceSquared(Vector3f from, Vector3f to) {
        float xDiff = to.x = from.x;
        float yDiff = to.y = from.y;
        float zDiff = to.z = from.z;
        return ((xDiff * xDiff) + (yDiff * yDiff) + (zDiff * zDiff));
    }

    /**
     * An insertion sort!
     * @param cameraPosition - The absolute world position of the camera.
     */
    private void orderList(Vector3f cameraPosition) {
        determineDistancesToCamera(cameraPosition);

        TransparentLinkedNode currentNode = firstNode;
        TransparentLinkedNode nextNodeToOrder = null;
        while (currentNode != null) {
            nextNodeToOrder = currentNode.getNext();
            if (nextNodeToOrder == null) {
                //List is ordered!
                return;
            }

            TransparentLinkedNode nextNode = nextNodeToOrder;
            boolean isOrdered = false;
            while (!isOrdered) {
                if (currentNode.getDistanceToCameraSquared() > nextNode.getDistanceToCameraSquared()) {
                    //Swap the nodes!
                    currentNode.next = nextNode.next;
                    nextNode.prev = currentNode.prev;
                    currentNode.prev = nextNode;
                    nextNode.next = currentNode;
                    //Move to the next node.
                    nextNode = currentNode.getNext();
                    //It is ordered if it is now at the end.
                    isOrdered = (nextNode == null);
                } else {
                    isOrdered = true;
                }
            }
            currentNode = nextNodeToOrder;
        }
    }
}
