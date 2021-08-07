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

package com.bulletphysics.collision.shapes;

import com.bulletphysics.collision.broadphase.BroadphaseNativeType;
import com.bulletphysics.linearmath.MatrixUtil;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;
import cz.advel.stack.Stack;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

// JAVA NOTE: ScaledBvhTriangleMeshShape from 2.73 SP1

/**
 * The ScaledBvhTriangleMeshShape allows to instance a scaled version of an existing
 * {@link BvhTriangleMeshShape}. Note that each {@link BvhTriangleMeshShape} still can
 * have its own local scaling, independent from this ScaledBvhTriangleMeshShape 'localScaling'.
 *
 * @author jezek2
 */
public class ScaledBvhTriangleMeshShape extends ConcaveShape {

	protected final Vector3f localScaling = new Vector3f();
	protected BvhTriangleMeshShape bvhTriMeshShape;

	public ScaledBvhTriangleMeshShape(BvhTriangleMeshShape childShape, Vector3f localScaling) {
		this.localScaling.set(localScaling);
		this.bvhTriMeshShape = childShape;
	}

	public BvhTriangleMeshShape getChildShape() {
		return bvhTriMeshShape;
	}

	@Override
	public void processAllTriangles(TriangleCallback callback, Vector3f aabbMin, Vector3f aabbMax) {
		ScaledTriangleCallback scaledCallback = new ScaledTriangleCallback(callback, localScaling);

		Vector3f invLocalScaling = Stack.alloc(Vector3f.class);
		invLocalScaling.set(1.f / localScaling.x, 1.f / localScaling.y, 1.f / localScaling.z);

		Vector3f scaledAabbMin = Stack.alloc(Vector3f.class);
		Vector3f scaledAabbMax = Stack.alloc(Vector3f.class);

		// support negative scaling
		scaledAabbMin.x = localScaling.x >= 0f ? aabbMin.x * invLocalScaling.x : aabbMax.x * invLocalScaling.x;
		scaledAabbMin.y = localScaling.y >= 0f ? aabbMin.y * invLocalScaling.y : aabbMax.y * invLocalScaling.y;
		scaledAabbMin.z = localScaling.z >= 0f ? aabbMin.z * invLocalScaling.z : aabbMax.z * invLocalScaling.z;

		scaledAabbMax.x = localScaling.x <= 0f ? aabbMin.x * invLocalScaling.x : aabbMax.x * invLocalScaling.x;
		scaledAabbMax.y = localScaling.y <= 0f ? aabbMin.y * invLocalScaling.y : aabbMax.y * invLocalScaling.y;
		scaledAabbMax.z = localScaling.z <= 0f ? aabbMin.z * invLocalScaling.z : aabbMax.z * invLocalScaling.z;

		bvhTriMeshShape.processAllTriangles(scaledCallback, scaledAabbMin, scaledAabbMax);
	}

	@Override
	public void getAabb(Transform trans, Vector3f aabbMin, Vector3f aabbMax) {
		Vector3f localAabbMin = bvhTriMeshShape.getLocalAabbMin(Stack.alloc(Vector3f.class));
		Vector3f localAabbMax = bvhTriMeshShape.getLocalAabbMax(Stack.alloc(Vector3f.class));

		Vector3f tmpLocalAabbMin = Stack.alloc(Vector3f.class);
		Vector3f tmpLocalAabbMax = Stack.alloc(Vector3f.class);
		VectorUtil.mul(tmpLocalAabbMin, localAabbMin, localScaling);
		VectorUtil.mul(tmpLocalAabbMax, localAabbMax, localScaling);

		localAabbMin.x = (localScaling.x >= 0f) ? tmpLocalAabbMin.x : tmpLocalAabbMax.x;
		localAabbMin.y = (localScaling.y >= 0f) ? tmpLocalAabbMin.y : tmpLocalAabbMax.y;
		localAabbMin.z = (localScaling.z >= 0f) ? tmpLocalAabbMin.z : tmpLocalAabbMax.z;
		localAabbMax.x = (localScaling.x <= 0f) ? tmpLocalAabbMin.x : tmpLocalAabbMax.x;
		localAabbMax.y = (localScaling.y <= 0f) ? tmpLocalAabbMin.y : tmpLocalAabbMax.y;
		localAabbMax.z = (localScaling.z <= 0f) ? tmpLocalAabbMin.z : tmpLocalAabbMax.z;

		Vector3f localHalfExtents = Stack.alloc(Vector3f.class);
		localHalfExtents.sub(localAabbMax, localAabbMin);
		localHalfExtents.scale(0.5f);

		float margin = bvhTriMeshShape.getMargin();
		localHalfExtents.x += margin;
		localHalfExtents.y += margin;
		localHalfExtents.z += margin;

		Vector3f localCenter = Stack.alloc(Vector3f.class);
		localCenter.add(localAabbMax, localAabbMin);
		localCenter.scale(0.5f);

		Matrix3f abs_b = Stack.alloc(trans.basis);
		MatrixUtil.absolute(abs_b);

		Vector3f center = Stack.alloc(localCenter);
		trans.transform(center);

		Vector3f extent = Stack.alloc(Vector3f.class);
		Vector3f tmp = Stack.alloc(Vector3f.class);
		abs_b.getRow(0, tmp);
		extent.x = tmp.dot(localHalfExtents);
		abs_b.getRow(1, tmp);
		extent.y = tmp.dot(localHalfExtents);
		abs_b.getRow(2, tmp);
		extent.z = tmp.dot(localHalfExtents);

		aabbMin.sub(center, extent);
		aabbMax.add(center, extent);
	}

	@Override
	public BroadphaseNativeType getShapeType() {
		return BroadphaseNativeType.SCALED_TRIANGLE_MESH_SHAPE_PROXYTYPE;
	}

	@Override
	public void setLocalScaling(Vector3f scaling) {
		localScaling.set(scaling);
	}

	@Override
	public Vector3f getLocalScaling(Vector3f out) {
		out.set(localScaling);
		return out;
	}

	@Override
	public void calculateLocalInertia(float mass, Vector3f inertia) {
	}

	@Override
	public String getName() {
		return "SCALEDBVHTRIANGLEMESH";
	}

	////////////////////////////////////////////////////////////////////////////

	private static class ScaledTriangleCallback extends TriangleCallback {
		private TriangleCallback originalCallback;
		private Vector3f localScaling;
		private Vector3f[] newTriangle = new Vector3f[3];

		public ScaledTriangleCallback(TriangleCallback originalCallback, Vector3f localScaling) {
			this.originalCallback = originalCallback;
			this.localScaling = localScaling;
			
			for (int i=0; i<newTriangle.length; i++) {
				newTriangle[i] = new Vector3f();
			}
		}

		public void processTriangle(Vector3f[] triangle, int partId, int triangleIndex) {
			VectorUtil.mul(newTriangle[0], triangle[0], localScaling);
			VectorUtil.mul(newTriangle[1], triangle[1], localScaling);
			VectorUtil.mul(newTriangle[2], triangle[2], localScaling);
			originalCallback.processTriangle(newTriangle, partId, triangleIndex);
		}
	}

}
