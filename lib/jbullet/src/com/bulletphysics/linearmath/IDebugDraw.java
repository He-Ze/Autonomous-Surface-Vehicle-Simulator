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

package com.bulletphysics.linearmath;

import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import cz.advel.stack.Stack;
import javax.vecmath.Vector3f;

/**
 * IDebugDraw interface class allows hooking up a debug renderer to visually debug
 * simulations.<p>
 * 
 * Typical use case: create a debug drawer object, and assign it to a {@link CollisionWorld}
 * or {@link DynamicsWorld} using setDebugDrawer and call debugDrawWorld.<p>
 * 
 * A class that implements the IDebugDraw interface has to implement the drawLine
 * method at a minimum.
 * 
 * @author jezek2
 */
public abstract class IDebugDraw {
	
	//protected final BulletStack stack = BulletStack.get();

	public abstract void drawLine(Vector3f from, Vector3f to, Vector3f color);
	
	public void drawTriangle(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f n0, Vector3f n1, Vector3f n2, Vector3f color, float alpha) {
		drawTriangle(v0, v1, v2, color, alpha);
	}
	
	public void drawTriangle(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f color, float alpha) {
		drawLine(v0, v1, color);
		drawLine(v1, v2, color);
		drawLine(v2, v0, color);
	}

	public abstract void drawContactPoint(Vector3f PointOnB, Vector3f normalOnB, float distance, int lifeTime, Vector3f color);

	public abstract void reportErrorWarning(String warningString);

	public abstract void draw3dText(Vector3f location, String textString);

	public abstract void setDebugMode(int debugMode);

	public abstract int getDebugMode();

	public void drawAabb(Vector3f from, Vector3f to, Vector3f color) {
		Vector3f halfExtents = Stack.alloc(to);
		halfExtents.sub(from);
		halfExtents.scale(0.5f);

		Vector3f center = Stack.alloc(to);
		center.add(from);
		center.scale(0.5f);

		int i, j;

		Vector3f edgecoord = Stack.alloc(Vector3f.class);
		edgecoord.set(1f, 1f, 1f);
		Vector3f pa = Stack.alloc(Vector3f.class), pb = Stack.alloc(Vector3f.class);
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 3; j++) {
				pa.set(edgecoord.x * halfExtents.x, edgecoord.y * halfExtents.y, edgecoord.z * halfExtents.z);
				pa.add(center);

				int othercoord = j % 3;

				VectorUtil.mulCoord(edgecoord, othercoord, -1f);
				pb.set(edgecoord.x * halfExtents.x, edgecoord.y * halfExtents.y, edgecoord.z * halfExtents.z);
				pb.add(center);

				drawLine(pa, pb, color);
			}
			edgecoord.set(-1f, -1f, -1f);
			if (i < 3) {
				VectorUtil.mulCoord(edgecoord, i, -1f);
			}
		}
	}
}
