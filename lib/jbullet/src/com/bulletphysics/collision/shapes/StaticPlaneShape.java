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
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.TransformUtil;
import com.bulletphysics.linearmath.VectorUtil;
import cz.advel.stack.Stack;
import javax.vecmath.Vector3f;

/**
 * StaticPlaneShape simulates an infinite non-moving (static) collision plane.
 * 
 * @author jezek2
 */
public class StaticPlaneShape extends ConcaveShape {

	protected final Vector3f localAabbMin = new Vector3f();
	protected final Vector3f localAabbMax = new Vector3f();
	
	protected final Vector3f planeNormal = new Vector3f();
	protected float planeConstant;
	protected final Vector3f localScaling = new Vector3f(0f, 0f, 0f);

	public StaticPlaneShape(Vector3f planeNormal, float planeConstant) {
		this.planeNormal.normalize(planeNormal);
		this.planeConstant = planeConstant;
	}

	public Vector3f getPlaneNormal(Vector3f out) {
		out.set(planeNormal);
		return out;
	}

	public float getPlaneConstant() {
		return planeConstant;
	}
	
	@Override
	public void processAllTriangles(TriangleCallback callback, Vector3f aabbMin, Vector3f aabbMax) {
		Vector3f tmp = Stack.alloc(Vector3f.class);
		Vector3f tmp1 = Stack.alloc(Vector3f.class);
		Vector3f tmp2 = Stack.alloc(Vector3f.class);

		Vector3f halfExtents = Stack.alloc(Vector3f.class);
		halfExtents.sub(aabbMax, aabbMin);
		halfExtents.scale(0.5f);

		float radius = halfExtents.length();
		Vector3f center = Stack.alloc(Vector3f.class);
		center.add(aabbMax, aabbMin);
		center.scale(0.5f);

		// this is where the triangles are generated, given AABB and plane equation (normal/constant)

		Vector3f tangentDir0 = Stack.alloc(Vector3f.class), tangentDir1 = Stack.alloc(Vector3f.class);

		// tangentDir0/tangentDir1 can be precalculated
		TransformUtil.planeSpace1(planeNormal, tangentDir0, tangentDir1);

		Vector3f supVertex0 = Stack.alloc(Vector3f.class), supVertex1 = Stack.alloc(Vector3f.class);

		Vector3f projectedCenter = Stack.alloc(Vector3f.class);
		tmp.scale(planeNormal.dot(center) - planeConstant, planeNormal);
		projectedCenter.sub(center, tmp);

		Vector3f[] triangle = new Vector3f[] { Stack.alloc(Vector3f.class), Stack.alloc(Vector3f.class), Stack.alloc(Vector3f.class) };

		tmp1.scale(radius, tangentDir0);
		tmp2.scale(radius, tangentDir1);
		VectorUtil.add(triangle[0], projectedCenter, tmp1, tmp2);

		tmp1.scale(radius, tangentDir0);
		tmp2.scale(radius, tangentDir1);
		tmp.sub(tmp1, tmp2);
		VectorUtil.add(triangle[1], projectedCenter, tmp);

		tmp1.scale(radius, tangentDir0);
		tmp2.scale(radius, tangentDir1);
		tmp.sub(tmp1, tmp2);
		triangle[2].sub(projectedCenter, tmp);

		callback.processTriangle(triangle, 0, 0);

		tmp1.scale(radius, tangentDir0);
		tmp2.scale(radius, tangentDir1);
		tmp.sub(tmp1, tmp2);
		triangle[0].sub(projectedCenter, tmp);

		tmp1.scale(radius, tangentDir0);
		tmp2.scale(radius, tangentDir1);
		tmp.add(tmp1, tmp2);
		triangle[1].sub(projectedCenter, tmp);

		tmp1.scale(radius, tangentDir0);
		tmp2.scale(radius, tangentDir1);
		VectorUtil.add(triangle[2], projectedCenter, tmp1, tmp2);

		callback.processTriangle(triangle, 0, 1);
	}

	@Override
	public void getAabb(Transform t, Vector3f aabbMin, Vector3f aabbMax) {
		aabbMin.set(-1e30f, -1e30f, -1e30f);
		aabbMax.set(1e30f, 1e30f, 1e30f);
	}

	@Override
	public BroadphaseNativeType getShapeType() {
		return BroadphaseNativeType.STATIC_PLANE_PROXYTYPE;
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
		//moving concave objects not supported
		inertia.set(0f, 0f, 0f);
	}

	@Override
	public String getName() {
		return "STATICPLANE";
	}

}
