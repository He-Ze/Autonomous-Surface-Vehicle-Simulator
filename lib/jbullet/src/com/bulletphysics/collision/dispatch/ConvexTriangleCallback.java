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

import com.bulletphysics.collision.broadphase.CollisionAlgorithm;
import com.bulletphysics.collision.broadphase.CollisionAlgorithmConstructionInfo;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.broadphase.DispatcherInfo;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleCallback;
import com.bulletphysics.collision.shapes.TriangleShape;
import com.bulletphysics.linearmath.Transform;
import cz.advel.stack.Stack;
import javax.vecmath.Vector3f;

/**
 * For each triangle in the concave mesh that overlaps with the AABB of a convex
 * (see {@link #convexBody} field), processTriangle is called.
 * 
 * @author jezek2
 */
class ConvexTriangleCallback extends TriangleCallback {

	//protected final BulletStack stack = BulletStack.get();
	
	private CollisionObject convexBody;
	private CollisionObject triBody;

	private final Vector3f aabbMin = new Vector3f();
	private final Vector3f aabbMax = new Vector3f();

	private ManifoldResult resultOut;

	private Dispatcher dispatcher;
	private DispatcherInfo dispatchInfoPtr;
	private float collisionMarginTriangle;
	
	public int triangleCount;
	public PersistentManifold manifoldPtr;
	
	public ConvexTriangleCallback(Dispatcher dispatcher, CollisionObject body0, CollisionObject body1, boolean isSwapped) {
		this.dispatcher = dispatcher;
		this.dispatchInfoPtr = null;

		convexBody = isSwapped ? body1 : body0;
		triBody = isSwapped ? body0 : body1;

		//
		// create the manifold from the dispatcher 'manifold pool'
		//
		manifoldPtr = dispatcher.getNewManifold(convexBody, triBody);

		clearCache();
	}
	
	public void destroy() {
		clearCache();
		dispatcher.releaseManifold(manifoldPtr);
	}

	public void setTimeStepAndCounters(float collisionMarginTriangle, DispatcherInfo dispatchInfo, ManifoldResult resultOut) {
		this.dispatchInfoPtr = dispatchInfo;
		this.collisionMarginTriangle = collisionMarginTriangle;
		this.resultOut = resultOut;

		// recalc aabbs
		Transform convexInTriangleSpace = Stack.alloc(Transform.class);

		triBody.getWorldTransform(convexInTriangleSpace);
		convexInTriangleSpace.inverse();
		convexInTriangleSpace.mul(convexBody.getWorldTransform(Stack.alloc(Transform.class)));

		CollisionShape convexShape = (CollisionShape)convexBody.getCollisionShape();
		//CollisionShape* triangleShape = static_cast<btCollisionShape*>(triBody->m_collisionShape);
		convexShape.getAabb(convexInTriangleSpace, aabbMin, aabbMax);
		float extraMargin = collisionMarginTriangle;
		Vector3f extra = Stack.alloc(Vector3f.class);
		extra.set(extraMargin, extraMargin, extraMargin);

		aabbMax.add(extra);
		aabbMin.sub(extra);
	}

	private CollisionAlgorithmConstructionInfo ci = new CollisionAlgorithmConstructionInfo();
	private TriangleShape tm = new TriangleShape();
	
	public void processTriangle(Vector3f[] triangle, int partId, int triangleIndex) {
		// just for debugging purposes
		//printf("triangle %d",m_triangleCount++);

		// aabb filter is already applied!	

		ci.dispatcher1 = dispatcher;

		CollisionObject ob = (CollisionObject) triBody;

		// debug drawing of the overlapping triangles
		if (dispatchInfoPtr != null && dispatchInfoPtr.debugDraw != null && dispatchInfoPtr.debugDraw.getDebugMode() > 0) {
			Vector3f color = Stack.alloc(Vector3f.class);
			color.set(255, 255, 0);
			Transform tr = ob.getWorldTransform(Stack.alloc(Transform.class));

			Vector3f tmp1 = Stack.alloc(Vector3f.class);
			Vector3f tmp2 = Stack.alloc(Vector3f.class);

			tmp1.set(triangle[0]); tr.transform(tmp1);
			tmp2.set(triangle[1]); tr.transform(tmp2);
			dispatchInfoPtr.debugDraw.drawLine(tmp1, tmp2, color);

			tmp1.set(triangle[1]); tr.transform(tmp1);
			tmp2.set(triangle[2]); tr.transform(tmp2);
			dispatchInfoPtr.debugDraw.drawLine(tmp1, tmp2, color);

			tmp1.set(triangle[2]); tr.transform(tmp1);
			tmp2.set(triangle[0]); tr.transform(tmp2);
			dispatchInfoPtr.debugDraw.drawLine(tmp1, tmp2, color);

			//btVector3 center = triangle[0] + triangle[1]+triangle[2];
			//center *= btScalar(0.333333);
			//m_dispatchInfoPtr->m_debugDraw->drawLine(tr(triangle[0]),tr(center),color);
			//m_dispatchInfoPtr->m_debugDraw->drawLine(tr(triangle[1]),tr(center),color);
			//m_dispatchInfoPtr->m_debugDraw->drawLine(tr(triangle[2]),tr(center),color);
		}

		//btCollisionObject* colObj = static_cast<btCollisionObject*>(m_convexProxy->m_clientObject);

		if (convexBody.getCollisionShape().isConvex()) {
			tm.init(triangle[0], triangle[1], triangle[2]);
			tm.setMargin(collisionMarginTriangle);

			CollisionShape tmpShape = ob.getCollisionShape();
			ob.internalSetTemporaryCollisionShape(tm);

			CollisionAlgorithm colAlgo = ci.dispatcher1.findAlgorithm(convexBody, triBody, manifoldPtr);
			// this should use the btDispatcher, so the actual registered algorithm is used
			//		btConvexConvexAlgorithm cvxcvxalgo(m_manifoldPtr,ci,m_convexBody,m_triBody);

			resultOut.setShapeIdentifiers(-1, -1, partId, triangleIndex);
			//cvxcvxalgo.setShapeIdentifiers(-1,-1,partId,triangleIndex);
			//cvxcvxalgo.processCollision(m_convexBody,m_triBody,*m_dispatchInfoPtr,m_resultOut);
			colAlgo.processCollision(convexBody, triBody, dispatchInfoPtr, resultOut);
			//colAlgo.destroy();
			ci.dispatcher1.freeCollisionAlgorithm(colAlgo);
			ob.internalSetTemporaryCollisionShape(tmpShape);
		}
	}

	public void clearCache() {
		dispatcher.clearManifold(manifoldPtr);
	}

	public Vector3f getAabbMin(Vector3f out) {
		out.set(aabbMin);
		return out;
	}

	public Vector3f getAabbMax(Vector3f out) {
		out.set(aabbMax);
		return out;
	}
	
}
