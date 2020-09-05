package renderables.r2D.layer;

import renderables.RendersAndUpdates;

public interface Layerable extends RendersAndUpdates {

	//Doubly linked list.
	LayerHandler getParent();
	void setParent(LayerHandler newParent);
	Layerable getNext();
	void setNext(Layerable newNext);
	Layerable getPrev();
	void setPrev(Layerable newPrev);
	boolean isVisible();

}
