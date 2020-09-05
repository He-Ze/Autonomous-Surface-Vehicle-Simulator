package renderables.r2D.layer;

import blindmystics.input.CurrentInput;

public class LayerHandler implements Layerable {
	
	protected Layerable prev = null;
	protected Layerable next = null;
	
	protected Layerable firstElement = null;
	protected Layerable lastElement = null;

	protected LayerHandler parentLayerableElement = null;

	/**
	 * Empty constructor.
	 */
	public LayerHandler(){}

	@Override
	public LayerHandler getParent() {
		return parentLayerableElement;
	}

	@Override
	public void setParent(LayerHandler newParent) {
		this.parentLayerableElement = newParent;
	}

	@Override
	public void update(CurrentInput input, float delta) {
		Layerable currentElement = lastElement;
		while(currentElement != null){
			if(currentElement.isVisible()){
				currentElement.update(input, delta);
			}
			currentElement = currentElement.getPrev();
		}
	}


	@Override
	public void render() {
		Layerable currentElement = firstElement;
		while(currentElement != null){
			if(currentElement.isVisible()){
				currentElement.render();
			}
			currentElement = currentElement.getNext();
		}
	}
	
	
	/**
	 * Adds a new element to the top of the layer.
	 * @param newElement
	 */
	public void addToTop(Layerable newElement){
		if (newElement == firstElement) {
			moveToTop(newElement); //Was already in the layer, move it to the top.
			return;
		}
		if(newElement == lastElement){
			return; //Is already at the top.
		}

		//Just in case, remove it from the layer it was previously in (If any).
		LayerableUtil.removeFromLayer(newElement);

		if(firstElement == null){
			firstElement = newElement;
			newElement.setPrev(null);
		} else {
			lastElement.setNext(newElement);
			newElement.setPrev(lastElement);
		}
		newElement.setNext(null);
		lastElement = newElement;
		newElement.setParent(this);
	}
	
	/**
	 * Adds a new element to the bottom of the layer.
	 * @param newElement
	 */
	public void addToBottom(Layerable newElement){
		if(newElement == lastElement){
			moveToBottom(newElement); //Was already in the layer, move it to the bottom.
			return;
		}
		if (newElement == firstElement) {
			return; //Is already at the bottom.
		}

		//Just in case, remove it from the layer it was previously in (If any).
		LayerableUtil.removeFromLayer(newElement);

		if(lastElement == null){
			lastElement = newElement;
			newElement.setNext(null);
		} else {
			firstElement.setPrev(newElement);
			newElement.setNext(firstElement);
		}
		newElement.setPrev(null);
		firstElement = newElement;
		newElement.setParent(this);
	}
	
	/**
	 * Moves the existing element to the top of the layer.
	 * @param existingElement
	 */
	public void moveToTop(Layerable existingElement){
		if(lastElement == existingElement){
			return; //Is already at the top.
		}

		if(existingElement == firstElement){
			firstElement = existingElement.getNext();
		}

		LayerableUtil.removeFromLayer(existingElement);

		lastElement.setNext(existingElement);
		existingElement.setPrev(lastElement);
		lastElement = existingElement;
		existingElement.setParent(this);
	}
	
	/**
	 * Moves the existing element to the bottom of the layer.
	 * @param existingElement
	 */
	public void moveToBottom(Layerable existingElement){
		if(firstElement == existingElement){
			return; //Is already at the bottom.
		}

		if(existingElement == lastElement){
			lastElement = existingElement.getPrev();
		}

		LayerableUtil.removeFromLayer(existingElement);

		firstElement.setPrev(existingElement);
		existingElement.setNext(firstElement);
		firstElement = existingElement;
		existingElement.setParent(this);
	}

	@Override
	public Layerable getNext() {
		return next;
	}

	@Override
	public Layerable getPrev() {
		return prev;
	}
	
	@Override
	public void setNext(Layerable newNext){
		this.next = newNext;
	}
	
	@Override
	public void setPrev(Layerable newPrev){
		this.prev = newPrev;
	}

	@Override
	public boolean isVisible(){
		return true;
	}

}
