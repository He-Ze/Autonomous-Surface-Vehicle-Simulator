/*
 * Java port of Bullet (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * Bullet Continuous Collision Detection and Physics Library
 * Copyright (c) 2003-2008 Erwin Coumans  http://www.bulletphysics.com/
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose, 
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package com.bulletphysics.collision.dispatch;

import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.collision.broadphase.CollisionAlgorithm;
import com.bulletphysics.collision.broadphase.CollisionAlgorithmConstructionInfo;
import com.bulletphysics.collision.broadphase.DispatcherInfo;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import com.bulletphysics.util.ObjectPool;
import cz.advel.stack.Stack;

/**
 * CompoundCollisionAlgorithm supports collision between {@link CompoundShape}s and
 * other collision shapes.
 * 
 * @author jezek2
 */
public class CompoundCollisionAlgorithm extends CollisionAlgorithm {

	private final ObjectArrayList<CollisionAlgorithm> childCollisionAlgorithms = new ObjectArrayList<CollisionAlgorithm>();
	private boolean isSwapped;
	
	public void init(CollisionAlgorithmConstructionInfo ci, CollisionObject body0, CollisionObject body1, boolean isSwapped) {
		super.init(ci);
		
		this.isSwapped = isSwapped;

		CollisionObject colObj = isSwapped ? body1 : body0;
		CollisionObject otherObj = isSwapped ? body0 : body1;
		assert (colObj.getCollisionShape().isCompound());

		CompoundShape compoundShape = (CompoundShape) colObj.getCollisionShape();
		int numChildren = compoundShape.getNumChildShapes();
		int i;

		//childCollisionAlgorithms.resize(numChildren);
		for (i = 0; i < numChildren; i++) {
			CollisionShape tmpShape = colObj.getCollisionShape();
			CollisionShape childShape = compoundShape.getChildShape(i);
			colObj.internalSetTemporaryCollisionShape(childShape);
			childCollisionAlgorithms.add(ci.dispatcher1.findAlgorithm(colObj, otherObj));
			colObj.internalSetTemporaryCollisionShape(tmpShape);
		}
	}

	@Override
	public void destroy() {
		int numChildren = childCollisionAlgorithms.size();
		for (int i=0; i<numChildren; i++) {
			//childCollisionAlgorithms.get(i).destroy();
			dispatcher.freeCollisionAlgorithm(childCollisionAlgorithms.getQuick(i));
		}
		childCollisionAlgorithms.clear();
	}
	
	@Override
	public void processCollision(CollisionObject body0, CollisionObject body1, DispatcherInfo dispatchInfo, ManifoldResult resultOut) {
		CollisionObject colObj = isSwapped ? body1 : body0;
		CollisionObject otherObj = isSwapped ? body0 : body1;

		assert (colObj.getCollisionShape().isCompound());
		CompoundShape compoundShape = (CompoundShape) colObj.getCollisionShape();

		// We will use the OptimizedBVH, AABB tree to cull potential child-overlaps
		// If both proxies are Compound, we will deal with that directly, by performing sequential/parallel tree traversals
		// given Proxy0 and Proxy1, if both have a tree, Tree0 and Tree1, this means:
		// determine overlapping nodes of Proxy1 using Proxy0 AABB against Tree1
		// then use each overlapping node AABB against Tree0
		// and vise versa.

		Transform tmpTrans = Stack.alloc(Transform.class);
		Transform orgTrans = Stack.alloc(Transform.class);
		Transform childTrans = Stack.alloc(Transform.class);
		Transform orgInterpolationTrans = Stack.alloc(Transform.class);
		Transform newChildWorldTrans = Stack.alloc(Transform.class);

		int numChildren = childCollisionAlgorithms.size();
		int i;
		for (i = 0; i < numChildren; i++) {
			// temporarily exchange parent btCollisionShape with childShape, and recurse
			CollisionShape childShape = compoundShape.getChildShape(i);

			// backup
			colObj.getWorldTransform(orgTrans);
			colObj.getInterpolationWorldTransform(orgInterpolationTrans);

			compoundShape.getChildTransform(i, childTrans);
			newChildWorldTrans.mul(orgTrans, childTrans);
			colObj.setWorldTransform(newChildWorldTrans);
			colObj.setInterpolationWorldTransform(newChildWorldTrans);
			
			// the contactpoint is still projected back using the original inverted worldtrans
			CollisionShape tmpShape = colObj.getCollisionShape();
			colObj.internalSetTemporaryCollisionShape(childShape);
			childCollisionAlgorithms.getQuick(i).processCollision(colObj, otherObj, dispatchInfo, resultOut);
			// revert back
			colObj.internalSetTemporaryCollisionShape(tmpShape);
			colObj.setWorldTransform(orgTrans);
			colObj.setInterpolationWorldTransform(orgInterpolationTrans);
		}
	}

	@Override
	public float calculateTimeOfImpact(CollisionObject body0, CollisionObject body1, DispatcherInfo dispatchInfo, ManifoldResult resultOut) {
		CollisionObject colObj = isSwapped ? body1 : body0;
		CollisionObject otherObj = isSwapped ? body0 : body1;

		assert (colObj.getCollisionShape().isCompound());

		CompoundShape compoundShape = (CompoundShape) colObj.getCollisionShape();

		// We will use the OptimizedBVH, AABB tree to cull potential child-overlaps
		// If both proxies are Compound, we will deal with that directly, by performing sequential/parallel tree traversals
		// given Proxy0 and Proxy1, if both have a tree, Tree0 and Tree1, this means:
		// determine overlapping nodes of Proxy1 using Proxy0 AABB against Tree1
		// then use each overlapping node AABB against Tree0
		// and vise versa.

		Transform tmpTrans = Stack.alloc(Transform.class);
		Transform orgTrans = Stack.alloc(Transform.class);
		Transform childTrans = Stack.alloc(Transform.class);
		float hitFraction = 1f;

		int numChildren = childCollisionAlgorithms.size();
		int i;
		for (i = 0; i < numChildren; i++) {
			// temporarily exchange parent btCollisionShape with childShape, and recurse
			CollisionShape childShape = compoundShape.getChildShape(i);

			// backup
			colObj.getWorldTransform(orgTrans);

			compoundShape.getChildTransform(i, childTrans);
			//btTransform	newChildWorldTrans = orgTrans*childTrans ;
			tmpTrans.set(orgTrans);
			tmpTrans.mul(childTrans);
			colObj.setWorldTransform(tmpTrans);

			CollisionShape tmpShape = colObj.getCollisionShape();
			colObj.internalSetTemporaryCollisionShape(childShape);
			float frac = childCollisionAlgorithms.getQuick(i).calculateTimeOfImpact(colObj, otherObj, dispatchInfo, resultOut);
			if (frac < hitFraction) {
				hitFraction = frac;
			}
			// revert back
			colObj.internalSetTemporaryCollisionShape(tmpShape);
			colObj.setWorldTransform(orgTrans);
		}
		return hitFraction;
	}

	@Override
	public void getAllContactManifolds(ObjectArrayList<PersistentManifold> manifoldArray) {
		for (int i=0; i<childCollisionAlgorithms.size(); i++) {
			childCollisionAlgorithms.getQuick(i).getAllContactManifolds(manifoldArray);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	public static class CreateFunc extends CollisionAlgorithmCreateFunc {
		private final ObjectPool<CompoundCollisionAlgorithm> pool = ObjectPool.get(CompoundCollisionAlgorithm.class);

		@Override
		public CollisionAlgorithm createCollisionAlgorithm(CollisionAlgorithmConstructionInfo ci, CollisionObject body0, CollisionObject body1) {
			CompoundCollisionAlgorithm algo = pool.get();
			algo.init(ci, body0, body1, false);
			return algo;
		}

		@Override
		public void releaseCollisionAlgorithm(CollisionAlgorithm algo) {
			pool.release((CompoundCollisionAlgorithm)algo);
		}
	};
	
	public static class SwappedCreateFunc extends CollisionAlgorithmCreateFunc {
		private final ObjectPool<CompoundCollisionAlgorithm> pool = ObjectPool.get(CompoundCollisionAlgorithm.class);

		@Override
		public CollisionAlgorithm createCollisionAlgorithm(CollisionAlgorithmConstructionInfo ci, CollisionObject body0, CollisionObject body1) {
			CompoundCollisionAlgorithm algo = pool.get();
			algo.init(ci, body0, body1, true);
			return algo;
		}

		@Override
		public void releaseCollisionAlgorithm(CollisionAlgorithm algo) {
			pool.release((CompoundCollisionAlgorithm)algo);
		}
	};

}
