package renderables.r2D;

import javax.vecmath.Vector2f;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Renderable2DUpdateHandler {
	
	public static Renderable2D topLevelElement = null;
	
	public static void addRenderableComponent(Renderable2D newComponent){
		newComponent.setSibling(topLevelElement);
		topLevelElement = newComponent;
	}
	
	/*
	private static enum Step {
		UPDATE_WITH_INPUT,
		UPDATE_POSITION,
		RENDER
	}
	*/
	/**
	 * Order of operations:
	 * 1: Send through the update for input.
	 * 2: Transform the objects based on their parent.
	 * 3: Render the objects.
	 */
	/*
	public static void handleAll2DObjects(CurrentInput input, float delta){
		
		Renderable2D firstElement = topLevelElement;
		
		while(firstElement != null && firstElement.shouldBeRemoved()){
			Renderable2D sibling = firstElement.getSibling();
			firstElement.setSibling(null);
			firstElement = sibling;
		}
		
		//Ensure that there are elements to render at all.
		if(firstElement != null){
			//TODO graphical picking.
			
			//Update all elements first.
			handleElement(firstElement, input, delta, false);
		}
	}
	*/
	
	public static void update2DObjectPosition() {
		Renderable2D firstElement = topLevelElement;

		while(firstElement != null && firstElement.shouldBeRemoved()){
			Renderable2D sibling = firstElement.getSibling();
			firstElement.setSibling(null);
			firstElement = sibling;
		}
		//Ensure that there are elements to render at all.
		if(firstElement != null) {
			//TODO graphical picking.

			//Update all elements first.
			update2DObjectPosition2(firstElement);
			//update2DObjectPosition(firstElement);
		}
	}
	
	private static float offsetX = 0;
	private static float offsetY = 0;

	private static boolean isBadFloatValue(float val){
		return (Float.isNaN(val) || Float.isInfinite(val));
	}

	public static void update2DObjectPositionAtOffset(Renderable2D element, Vector2f startingOffset){
		update2DObjectPositionAtOffset(element, startingOffset.x, startingOffset.y);
	}

	public static void update2DObjectPositionAtOffset(Renderable2D element, float startingOffsetX, float startingOffsetY){
		offsetX = startingOffsetX;
		offsetY = startingOffsetY;
		update2DObjectPosition2(element);
		offsetX = 0;
		offsetY = 0;
	}


	public static void update2DObjectPosition2(Renderable2D firstElement) {

		Deque<Renderable2D> updateQueue = new LinkedList<>();
		Deque<Boolean> handledChildQueue = new LinkedList<>();
		Deque<Float> updateQueueRelX = new LinkedList<>();
		Deque<Float> updateQueueRelY = new LinkedList<>();

		updateQueue.push(firstElement);
		handledChildQueue.push(false);

		while (!updateQueue.isEmpty()) {

			Renderable2D element = updateQueue.peek();
			boolean handledChild = handledChildQueue.pop();

			if (!handledChild) {

				if(isBadFloatValue(offsetX) || isBadFloatValue(offsetY)){
					offsetX = 0;
					offsetY = 0;
				}

				float relX = element.getRelX() * element.getSizeScale().x;
				if(Float.isNaN(relX) || Float.isInfinite(relX)){
					updateQueue.pop();
					continue;
				}
				float relY = element.getRelY() * element.getSizeScale().y;
				if(Float.isNaN(relY) || Float.isInfinite(relY)){
					updateQueue.pop();
					continue;
				}

				//Remove all children that need to be removed.
				Renderable2D child = element.getChild();
				while(child != null && child.shouldBeRemoved()){
					element.setChild(child.getSibling());
					child.setSibling(null);
					child = element.getChild();
				}

				if(element.isVisible){
					//Only update visible elements.

					float newOffsetX = offsetX + relX;
					float newOffsetY = offsetY + relY;
					element.setAbsolutePosition(newOffsetX, newOffsetY);

					//Check if there is a child to deal with.
					if(child != null){

						updateQueueRelX.push(offsetX);
						updateQueueRelY.push(offsetY);

						//Apply Transformation(s).
						offsetX = newOffsetX;
						offsetY = newOffsetY;

						updateQueue.push(child);
						handledChildQueue.push(true);
						handledChildQueue.push(false);

						continue;
					}
				}
			} else {
				//Restore Previous Transformation(s).
				offsetX = updateQueueRelX.pop();
				offsetY = updateQueueRelY.pop();
			}

			//Remove the current element, it's now been handled.
			updateQueue.pop();

			//Move onto the sibling.
			//Remove all siblings that should be removed.
			Renderable2D sibling = element.getSibling();
			while(sibling != null && sibling.shouldBeRemoved()){
				element.setSibling(sibling.getSibling());
				sibling.setSibling(null);
				sibling = element.getSibling();
			}
			if(sibling != null){
				//Put the sibling on the queue to be handled next.
				updateQueue.push(sibling);
				handledChildQueue.push(false);
			}

		}
	}

	@Deprecated
	public static void update2DObjectPosition(Renderable2D element){

		if(isBadFloatValue(offsetX) || isBadFloatValue(offsetY)){
			offsetX = 0;
			offsetY = 0;
		}

		float relX = element.getRelX();
		if(Float.isNaN(relX) || Float.isInfinite(relX)){
			return;
		}
		float relY = element.getRelY();
		if(Float.isNaN(relY) || Float.isInfinite(relY)){
			return;
		}

		float prevOffsetX = offsetX;
		float prevOffsetY = offsetY;

		//Apply Transformation(s).
		offsetX += relX;
		offsetY += relY;
		Renderable2D child = element.getChild();
		while(child != null && child.shouldBeRemoved()){
			element.setChild(child.getSibling());
			child.setSibling(null);
			child = element.getChild();
		}
		
		
		if(element.isVisible){
			if(child != null){
				update2DObjectPosition(child);
			}
			element.setAbsolutePosition(offsetX, offsetY);
		}
		
		//Remove Transformation(s).
		offsetX = prevOffsetX;
		offsetY = prevOffsetY;
		
		//Move onto the sibling.
		Renderable2D sibling = element.getSibling();
		while(sibling != null && sibling.shouldBeRemoved()){
			element.setSibling(sibling.getSibling());
			sibling.setSibling(null);
			sibling = element.getSibling();
		}
		if(sibling != null){
			
			
			
			update2DObjectPosition(sibling);
		}
	}

	public static void scaleChildrenToScale(Renderable2D element, Vector2f scale){
		scaleChildrenToScale(element, scale.x, scale.y);
	}

	public static void scaleChildrenToScale(Renderable2D element, float scaleX, float scaleY){
		Renderable2D child = element.getChild();
		while(child != null && child.shouldBeRemoved()){
			element.setChild(child.getSibling());
			child.setSibling(null);
			child = element.getChild();
		}

		if(element.isVisible){
			if(child != null){
				scaleChildrenToScale(child, scaleX, scaleY);
			}
			element.setSizeScale(scaleX, scaleY);
		}

		//Move onto the sibling.
		Renderable2D sibling = element.getSibling();
		while(sibling != null && sibling.shouldBeRemoved()){
			element.setSibling(sibling.getSibling());
			sibling.setSibling(null);
			sibling = element.getSibling();
		}
		if(sibling != null){
			scaleChildrenToScale(sibling, scaleX, scaleY);
		}
	}


}
