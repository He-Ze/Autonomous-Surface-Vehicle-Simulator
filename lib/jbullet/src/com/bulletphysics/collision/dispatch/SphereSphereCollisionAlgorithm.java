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

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.broadphase.CollisionAlgorithm;
import com.bulletphysics.collision.broadphase.CollisionAlgorithmConstructionInfo;
import com.bulletphysics.collision.broadphase.DispatcherInfo;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import com.bulletphysics.util.ObjectPool;
import cz.advel.stack.Stack;
import javax.vecmath.Vector3f;

/**
 * Provides collision detection between two spheres.
 * 
 * @author jezek2
 */
public class SphereSphereCollisionAlgorithm extends CollisionAlgorithm {
	
	private boolean ownManifold;
	private PersistentManifold manifoldPtr;
	
	public void init(PersistentManifold mf, CollisionAlgorithmConstructionInfo ci, CollisionObject col0, CollisionObject col1) {
		super.init(ci);
		manifoldPtr = mf;

		if (manifoldPtr == null) {
			manifoldPtr = dispatcher.getNewManifold(col0, col1);
			ownManifold = true;
		}
	}

	@Override
	public void init(CollisionAlgorithmConstructionInfo ci) {
		super.init(ci);
	}
	
	@Override
	public void destroy() {
		if (ownManifold) {
			if (manifoldPtr != null) {
				dispatcher.releaseManifold(manifoldPtr);
			}
			manifoldPtr = null;
		}
	}
	
	@Override
	public void processCollision(CollisionObject col0, CollisionObject col1, DispatcherInfo dispatchInfo, ManifoldResult resultOut) {
		if (manifoldPtr == null) {
			return;
		}
		
		Transform tmpTrans1 = Stack.alloc(Transform.class);
		Transform tmpTrans2 = Stack.alloc(Transform.class);

		resultOut.setPersistentManifold(manifoldPtr);

		SphereShape sphere0 = (SphereShape) col0.getCollisionShape();
		SphereShape sphere1 = (SphereShape) col1.getCollisionShape();

		Vector3f diff = Stack.alloc(Vector3f.class);
		diff.sub(col0.getWorldTransform(tmpTrans1).origin, col1.getWorldTransform(tmpTrans2).origin);

		float len = diff.length();
		float radius0 = sphere0.getRadius();
		float radius1 = sphere1.getRadius();

		//#ifdef CLEAR_MANIFOLD
		//manifoldPtr.clearManifold(); // don't do this, it disables warmstarting
		//#endif

		// if distance positive, don't generate a new contact
		if (len > (radius0 + radius1)) {
			//#ifndef CLEAR_MANIFOLD
			resultOut.refreshContactPoints();
			//#endif //CLEAR_MANIFOLD
			return;
		}
		// distance (negative means penetration)
		float dist = len - (radius0 + radius1);

		Vector3f normalOnSurfaceB = Stack.alloc(Vector3f.class);
		normalOnSurfaceB.set(1f, 0f, 0f);
		if (len > BulletGlobals.FLT_EPSILON) {
			normalOnSurfaceB.scale(1f / len, diff);
		}

		Vector3f tmp = Stack.alloc(Vector3f.class);

		// point on A (worldspace)
		Vector3f pos0 = Stack.alloc(Vector3f.class);
		tmp.scale(radius0, normalOnSurfaceB);
		pos0.sub(col0.getWorldTransform(tmpTrans1).origin, tmp);

		// point on B (worldspace)
		Vector3f pos1 = Stack.alloc(Vector3f.class);
		tmp.scale(radius1, normalOnSurfaceB);
		pos1.add(col1.getWorldTransform(tmpTrans2).origin, tmp);

		// report a contact. internally this will be kept persistent, and contact reduction is done
		resultOut.addContactPoint(normalOnSurfaceB, pos1, dist);

		//#ifndef CLEAR_MANIFOLD
		resultOut.refreshContactPoints();
		//#endif //CLEAR_MANIFOLD
	}

	@Override
	public float calculateTimeOfImpact(CollisionObject body0, CollisionObject body1, DispatcherInfo dispatchInfo, ManifoldResult resultOut) {
		return 1f;
	}

	@Override
	public void getAllContactManifolds(ObjectArrayList<PersistentManifold> manifoldArray) {
		if (manifoldPtr != null && ownManifold) {
			manifoldArray.add(manifoldPtr);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////

	public static class CreateFunc extends CollisionAlgorithmCreateFunc {
		private final ObjectPool<SphereSphereCollisionAlgorithm> pool = ObjectPool.get(SphereSphereCollisionAlgorithm.class);

		@Override
		public CollisionAlgorithm createCollisionAlgorithm(CollisionAlgorithmConstructionInfo ci, CollisionObject body0, CollisionObject body1) {
			SphereSphereCollisionAlgorithm algo = pool.get();
			algo.init(null, ci, body0, body1);
			return algo;
		}

		@Override
		public void releaseCollisionAlgorithm(CollisionAlgorithm algo) {
			pool.release((SphereSphereCollisionAlgorithm)algo);
		}
	};
	
}
