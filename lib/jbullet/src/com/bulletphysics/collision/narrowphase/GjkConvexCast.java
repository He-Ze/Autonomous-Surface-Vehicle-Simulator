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

package com.bulletphysics.collision.narrowphase;

import com.bulletphysics.util.ObjectPool;
import com.bulletphysics.collision.narrowphase.DiscreteCollisionDetectorInterface.ClosestPointInput;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;
import cz.advel.stack.Stack;
import javax.vecmath.Vector3f;

/**
 * GjkConvexCast performs a raycast on a convex object using support mapping.
 * 
 * @author jezek2
 */
public class GjkConvexCast extends ConvexCast {

	//protected final BulletStack stack = BulletStack.get();
	protected final ObjectPool<ClosestPointInput> pointInputsPool = ObjectPool.get(ClosestPointInput.class);

//#ifdef BT_USE_DOUBLE_PRECISION
//	private static final int MAX_ITERATIONS = 64;
//#else
	private static final int MAX_ITERATIONS = 32;
//#endif
	
	private SimplexSolverInterface simplexSolver;
	private ConvexShape convexA;
	private ConvexShape convexB;
	
	private GjkPairDetector gjk = new GjkPairDetector();

	public GjkConvexCast(ConvexShape convexA, ConvexShape convexB, SimplexSolverInterface simplexSolver) {
		this.simplexSolver = simplexSolver;
		this.convexA = convexA;
		this.convexB = convexB;
	}
	
	public boolean calcTimeOfImpact(Transform fromA, Transform toA, Transform fromB, Transform toB, CastResult result) {
		simplexSolver.reset();

		// compute linear velocity for this interval, to interpolate
		// assume no rotation/angular velocity, assert here?
		Vector3f linVelA = Stack.alloc(Vector3f.class);
		Vector3f linVelB = Stack.alloc(Vector3f.class);

		linVelA.sub(toA.origin, fromA.origin);
		linVelB.sub(toB.origin, fromB.origin);

		float radius = 0.001f;
		float lambda = 0f;
		Vector3f v = Stack.alloc(Vector3f.class);
		v.set(1f, 0f, 0f);

		int maxIter = MAX_ITERATIONS;

		Vector3f n = Stack.alloc(Vector3f.class);
		n.set(0f, 0f, 0f);
		boolean hasResult = false;
		Vector3f c = Stack.alloc(Vector3f.class);
		Vector3f r = Stack.alloc(Vector3f.class);
		r.sub(linVelA, linVelB);

		float lastLambda = lambda;
		//btScalar epsilon = btScalar(0.001);

		int numIter = 0;
		// first solution, using GJK

		Transform identityTrans = Stack.alloc(Transform.class);
		identityTrans.setIdentity();

		//result.drawCoordSystem(sphereTr);

		PointCollector pointCollector = new PointCollector();

		gjk.init(convexA, convexB, simplexSolver, null); // penetrationDepthSolver);		
		ClosestPointInput input = pointInputsPool.get();
		input.init();
		try {
			// we don't use margins during CCD
			//	gjk.setIgnoreMargin(true);

			input.transformA.set(fromA);
			input.transformB.set(fromB);
			gjk.getClosestPoints(input, pointCollector, null);

			hasResult = pointCollector.hasResult;
			c.set(pointCollector.pointInWorld);

			if (hasResult) {
				float dist;
				dist = pointCollector.distance;
				n.set(pointCollector.normalOnBInWorld);

				// not close enough
				while (dist > radius) {
					numIter++;
					if (numIter > maxIter) {
						return false; // todo: report a failure
					}
					float dLambda = 0f;

					float projectedLinearVelocity = r.dot(n);

					dLambda = dist / (projectedLinearVelocity);

					lambda = lambda - dLambda;

					if (lambda > 1f) {
						return false;
					}
					if (lambda < 0f) {
						return false;					// todo: next check with relative epsilon
					}
					
					if (lambda <= lastLambda) {
						return false;
					//n.setValue(0,0,0);
					//break;
					}
					lastLambda = lambda;

					// interpolate to next lambda
					result.debugDraw(lambda);
					VectorUtil.setInterpolate3(input.transformA.origin, fromA.origin, toA.origin, lambda);
					VectorUtil.setInterpolate3(input.transformB.origin, fromB.origin, toB.origin, lambda);

					gjk.getClosestPoints(input, pointCollector, null);
					if (pointCollector.hasResult) {
						if (pointCollector.distance < 0f) {
							result.fraction = lastLambda;
							n.set(pointCollector.normalOnBInWorld);
							result.normal.set(n);
							result.hitPoint.set(pointCollector.pointInWorld);
							return true;
						}
						c.set(pointCollector.pointInWorld);
						n.set(pointCollector.normalOnBInWorld);
						dist = pointCollector.distance;
					}
					else {
						// ??
						return false;
					}

				}

				// is n normalized?
				// don't report time of impact for motion away from the contact normal (or causes minor penetration)
				if (n.dot(r) >= -result.allowedPenetration) {
					return false;
				}
				result.fraction = lambda;
				result.normal.set(n);
				result.hitPoint.set(c);
				return true;
			}

			return false;
		}
		finally {
			pointInputsPool.release(input);
		}
	}
	
}
