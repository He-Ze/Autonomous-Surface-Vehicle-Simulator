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

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.broadphase.BroadphaseNativeType;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;
import cz.advel.stack.Stack;
import javax.vecmath.Vector3f;

/**
 * ConeShape implements a cone shape primitive, centered around the origin and
 * aligned with the Y axis. The {@link ConeShapeX} is aligned around the X axis
 * and {@link ConeShapeZ} around the Z axis.
 * 
 * @author jezek2
 */
public class ConeShape extends ConvexInternalShape {

	private float sinAngle;
	private float radius;
	private float height;
	private int[] coneIndices = new int[3];

	public ConeShape(float radius, float height) {
		this.radius = radius;
		this.height = height;
		setConeUpIndex(1);
		sinAngle = (radius / (float)Math.sqrt(this.radius * this.radius + this.height * this.height));
	}

	public float getRadius() {
		return radius;
	}

	public float getHeight() {
		return height;
	}

	private Vector3f coneLocalSupport(Vector3f v, Vector3f out) {
		float halfHeight = height * 0.5f;

		if (VectorUtil.getCoord(v, coneIndices[1]) > v.length() * sinAngle) {
			VectorUtil.setCoord(out, coneIndices[0], 0f);
			VectorUtil.setCoord(out, coneIndices[1], halfHeight);
			VectorUtil.setCoord(out, coneIndices[2], 0f);
			return out;
		}
		else {
			float v0 = VectorUtil.getCoord(v, coneIndices[0]);
			float v2 = VectorUtil.getCoord(v, coneIndices[2]);
			float s = (float)Math.sqrt(v0 * v0 + v2 * v2);
			if (s > BulletGlobals.FLT_EPSILON) {
				float d = radius / s;
				VectorUtil.setCoord(out, coneIndices[0], VectorUtil.getCoord(v, coneIndices[0]) * d);
				VectorUtil.setCoord(out, coneIndices[1], -halfHeight);
				VectorUtil.setCoord(out, coneIndices[2], VectorUtil.getCoord(v, coneIndices[2]) * d);
				return out;
			} else {
				VectorUtil.setCoord(out, coneIndices[0], 0f);
				VectorUtil.setCoord(out, coneIndices[1], -halfHeight);
				VectorUtil.setCoord(out, coneIndices[2], 0f);
				return out;
			}
		}
	}

	@Override
	public Vector3f localGetSupportingVertexWithoutMargin(Vector3f vec, Vector3f out) {
		return coneLocalSupport(vec, out);
	}

	@Override
	public void batchedUnitVectorGetSupportingVertexWithoutMargin(Vector3f[] vectors, Vector3f[] supportVerticesOut, int numVectors) {
		for (int i=0; i<numVectors; i++) {
			Vector3f vec = vectors[i];
			coneLocalSupport(vec, supportVerticesOut[i]);
		}
	}

	@Override
	public Vector3f localGetSupportingVertex(Vector3f vec, Vector3f out) {
		Vector3f supVertex = coneLocalSupport(vec, out);
		if (getMargin() != 0f) {
			Vector3f vecnorm = Stack.alloc(vec);
			if (vecnorm.lengthSquared() < (BulletGlobals.FLT_EPSILON * BulletGlobals.FLT_EPSILON)) {
				vecnorm.set(-1f, -1f, -1f);
			}
			vecnorm.normalize();
			supVertex.scaleAdd(getMargin(), vecnorm, supVertex);
		}
		return supVertex;
	}

	@Override
	public BroadphaseNativeType getShapeType() {
		return BroadphaseNativeType.CONE_SHAPE_PROXYTYPE;
	}

	@Override
	public void calculateLocalInertia(float mass, Vector3f inertia) {
		Transform identity = Stack.alloc(Transform.class);
		identity.setIdentity();
		Vector3f aabbMin = Stack.alloc(Vector3f.class), aabbMax = Stack.alloc(Vector3f.class);
		getAabb(identity, aabbMin, aabbMax);

		Vector3f halfExtents = Stack.alloc(Vector3f.class);
		halfExtents.sub(aabbMax, aabbMin);
		halfExtents.scale(0.5f);

		float margin = getMargin();

		float lx = 2f * (halfExtents.x + margin);
		float ly = 2f * (halfExtents.y + margin);
		float lz = 2f * (halfExtents.z + margin);
		float x2 = lx * lx;
		float y2 = ly * ly;
		float z2 = lz * lz;
		float scaledmass = mass * 0.08333333f;

		inertia.set(y2 + z2, x2 + z2, x2 + y2);
		inertia.scale(scaledmass);

		//inertia.x() = scaledmass * (y2+z2);
		//inertia.y() = scaledmass * (x2+z2);
		//inertia.z() = scaledmass * (x2+y2);
	}

	@Override
	public String getName() {
		return "Cone";
	}

	// choose upAxis index
	protected void setConeUpIndex(int upIndex) {
		switch (upIndex) {
			case 0:
				coneIndices[0] = 1;
				coneIndices[1] = 0;
				coneIndices[2] = 2;
				break;

			case 1:
				coneIndices[0] = 0;
				coneIndices[1] = 1;
				coneIndices[2] = 2;
				break;

			case 2:
				coneIndices[0] = 0;
				coneIndices[1] = 2;
				coneIndices[2] = 1;
				break;

			default:
				assert (false);
		}
	}

	public int getConeUpIndex() {
		return coneIndices[1];
	}
	
}
