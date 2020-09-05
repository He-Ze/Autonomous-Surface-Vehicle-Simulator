package gebd.camera.transparency;

import composites.entities.Entity;

/**
 * Created by CaptainPete on 8/2/2016.
 */
public class TransparentLinkedNode {

    private float distanceToCameraSquared = 0;
    private TransparentEntityOrderHandler parent;
    TransparentLinkedNode prev;
    TransparentLinkedNode next;
    private Entity transparentEntity;

    public TransparentLinkedNode(TransparentEntityOrderHandler parent, TransparentLinkedNode prev, TransparentLinkedNode next, Entity transparentEntity) {
        this.parent = parent;
        this.prev = prev;
        this.next = next;
        this.transparentEntity = transparentEntity;
    }

    TransparentLinkedNode getNext() {
        return next;
    }

    TransparentLinkedNode getPrev() {
        return prev;
    }

    public Entity getTransparentEntity() {
        return transparentEntity;
    }

    public void unlinkNode() {
        if (prev != null) {
            prev.next = next;
        }
        if (next != null) {
            next.prev = prev;
        }
        transparentEntity = null;
        parent.removeTransparentEntity(this);
    }

    float getDistanceToCameraSquared() {
        return distanceToCameraSquared;
    }

    void setDistanceToCameraSquared(float distanceToCameraSquared) {
        this.distanceToCameraSquared = distanceToCameraSquared;
    }
}
