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

import com.bulletphysics.util.ObjectPool;
import com.bulletphysics.collision.broadphase.CollisionAlgorithm;
import com.bulletphysics.collision.broadphase.CollisionAlgorithmConstructionInfo;
import com.bulletphysics.collision.broadphase.DispatcherInfo;
import com.bulletphysics.collision.narrowphase.ConvexCast;
import com.bulletphysics.collision.narrowphase.ConvexPenetrationDepthSolver;
import com.bulletphysics.collision.narrowphase.DiscreteCollisionDetectorInterface.ClosestPointInput;
import com.bulletphysics.collision.narrowphase.GjkConvexCast;
import com.bulletphysics.collision.narrowphase.GjkPairDetector;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.collision.narrowphase.SimplexSolverInterface;
import com.bulletphysics.collision.narrowphase.VoronoiSimplexSolver;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import cz.advel.stack.Stack;
import javax.vecmath.Vector3f;

/**
 * ConvexConvexAlgorithm collision algorithm implements time of impact, convex
 * closest points and penetration depth calculations.
 * 
 * @author jezek2
 */
public class ConvexConvexAlgorithm extends CollisionAlgorithm {
	
	protected final ObjectPool<ClosestPointInput> pointInputsPool = ObjectPool.get(ClosestPointInput.class);

	private GjkPairDetector gjkPairDetector = new GjkPairDetector();

	public boolean ownManifold;
	public PersistentManifold manifoldPtr;
	public boolean lowLevelOfDetail;
	
	public void init(PersistentManifold mf, CollisionAlgorithmConstructionInfo ci, CollisionObject body0, CollisionObject body1, SimplexSolverInterface simplexSolver, ConvexPenetrationDepthSolver pdSolver) {
		super.init(ci);
		gjkPairDetector.init(null, null, simplexSolver, pdSolver);
		this.manifoldPtr = mf;
		this.ownManifold = false;
		this.lowLevelOfDetail = false;
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

	public void setLowLevelOfDetail(boolean useLowLevel) {
		this.lowLevelOfDetail = useLowLevel;
	}

	/**
	 * Convex-Convex collision algorithm.
	 */
	@Override
	public void processCollision(CollisionObject body0, CollisionObject body1, DispatcherInfo dispatchInfo, ManifoldResult resultOut) {
		if (manifoldPtr == null) {
			// swapped?
			manifoldPtr = dispatcher.getNewManifold(body0, body1);
			ownManifold = true;
		}
		resultOut.setPersistentManifold(manifoldPtr);

//	#ifdef USE_BT_GJKEPA
//		btConvexShape*				shape0(static_cast<btConvexShape*>(body0->getCollisionShape()));
//		btConvexShape*				shape1(static_cast<btConvexShape*>(body1->getCollisionShape()));
//		const btScalar				radialmargin(0/*shape0->getMargin()+shape1->getMargin()*/);
//		btGjkEpaSolver::sResults	results;
//		if(btGjkEpaSolver::Collide(	shape0,body0->getWorldTransform(),
//									shape1,body1->getWorldTransform(),
//									radialmargin,results))
//			{
//			dispatchInfo.m_debugDraw->drawLine(results.witnesses[1],results.witnesses[1]+results.normal,btVector3(255,0,0));
//			resultOut->addContactPoint(results.normal,results.witnesses[1],-results.depth);
//			}
//	#else

		ConvexShape min0 = (ConvexShape) body0.getCollisionShape();
		ConvexShape min1 = (ConvexShape) body1.getCollisionShape();

		ClosestPointInput input = pointInputsPool.get();
		input.init();

		// JAVA NOTE: original: TODO: if (dispatchInfo.m_useContinuous)
		gjkPairDetector.setMinkowskiA(min0);
		gjkPairDetector.setMinkowskiB(min1);
		input.maximumDistanceSquared = min0.getMargin() + min1.getMargin() + manifoldPtr.getContactBreakingThreshold();
		input.maximumDistanceSquared *= input.maximumDistanceSquared;
		//input.m_stackAlloc = dispatchInfo.m_stackAllocator;

		//	input.m_maximumDistanceSquared = btScalar(1e30);

		body0.getWorldTransform(input.transformA);
		body1.getWorldTransform(input.transformB);

		gjkPairDetector.getClosestPoints(input, resultOut, dispatchInfo.debugDraw);
		
		pointInputsPool.release(input);
		//	#endif

		if (ownManifold) {
			resultOut.refreshContactPoints();
		}
	}

	private static boolean disableCcd = false;
	
	@Override
	public float calculateTimeOfImpact(CollisionObject col0, CollisionObject col1, DispatcherInfo dispatchInfo, ManifoldResult resultOut) {
		Vector3f tmp = Stack.alloc(Vector3f.class);
		
		Transform tmpTrans1 = Stack.alloc(Transform.class);
		Transform tmpTrans2 = Stack.alloc(Transform.class);

		// Rather then checking ALL pairs, only calculate TOI when motion exceeds threshold

		// Linear motion for one of objects needs to exceed m_ccdSquareMotionThreshold
		// col0->m_worldTransform,
		float resultFraction = 1f;

		tmp.sub(col0.getInterpolationWorldTransform(tmpTrans1).origin, col0.getWorldTransform(tmpTrans2).origin);
		float squareMot0 = tmp.lengthSquared();

		tmp.sub(col1.getInterpolationWorldTransform(tmpTrans1).origin, col1.getWorldTransform(tmpTrans2).origin);
		float squareMot1 = tmp.lengthSquared();

		if (squareMot0 < col0.getCcdSquareMotionThreshold() &&
				squareMot1 < col1.getCcdSquareMotionThreshold()) {
			return resultFraction;
		}

		if (disableCcd) {
			return 1f;
		}
		
		Transform tmpTrans3 = Stack.alloc(Transform.class);
		Transform tmpTrans4 = Stack.alloc(Transform.class);

		// An adhoc way of testing the Continuous Collision Detection algorithms
		// One object is approximated as a sphere, to simplify things
		// Starting in penetration should report no time of impact
		// For proper CCD, better accuracy and handling of 'allowed' penetration should be added
		// also the mainloop of the physics should have a kind of toi queue (something like Brian Mirtich's application of Timewarp for Rigidbodies)

		// Convex0 against sphere for Convex1
		{
			ConvexShape convex0 = (ConvexShape) col0.getCollisionShape();

			SphereShape sphere1 = new SphereShape(col1.getCcdSweptSphereRadius()); // todo: allow non-zero sphere sizes, for better approximation
			ConvexCast.CastResult result = new ConvexCast.CastResult();
			VoronoiSimplexSolver voronoiSimplex = new VoronoiSimplexSolver();
			//SubsimplexConvexCast ccd0(&sphere,min0,&voronoiSimplex);
			///Simplification, one object is simplified as a sphere
			GjkConvexCast ccd1 = new GjkConvexCast(convex0, sphere1, voronoiSimplex);
			//ContinuousConvexCollision ccd(min0,min1,&voronoiSimplex,0);
			if (ccd1.calcTimeOfImpact(col0.getWorldTransform(tmpTrans1), col0.getInterpolationWorldTransform(tmpTrans2),
					col1.getWorldTransform(tmpTrans3), col1.getInterpolationWorldTransform(tmpTrans4), result)) {
				// store result.m_fraction in both bodies

				if (col0.getHitFraction() > result.fraction) {
					col0.setHitFraction(result.fraction);
				}

				if (col1.getHitFraction() > result.fraction) {
					col1.setHitFraction(result.fraction);
				}

				if (resultFraction > result.fraction) {
					resultFraction = result.fraction;
				}
			}
		}

		// Sphere (for convex0) against Convex1
		{
			ConvexShape convex1 = (ConvexShape) col1.getCollisionShape();

			SphereShape sphere0 = new SphereShape(col0.getCcdSweptSphereRadius()); // todo: allow non-zero sphere sizes, for better approximation
			ConvexCast.CastResult result = new ConvexCast.CastResult();
			VoronoiSimplexSolver voronoiSimplex = new VoronoiSimplexSolver();
			//SubsimplexConvexCast ccd0(&sphere,min0,&voronoiSimplex);
			///Simplification, one object is simplified as a sphere
			GjkConvexCast ccd1 = new GjkConvexCast(sphere0, convex1, voronoiSimplex);
			//ContinuousConvexCollision ccd(min0,min1,&voronoiSimplex,0);
			if (ccd1.calcTimeOfImpact(col0.getWorldTransform(tmpTrans1), col0.getInterpolationWorldTransform(tmpTrans2),
					col1.getWorldTransform(tmpTrans3), col1.getInterpolationWorldTransform(tmpTrans4), result)) {
				//store result.m_fraction in both bodies

				if (col0.getHitFraction() > result.fraction) {
					col0.setHitFraction(result.fraction);
				}

				if (col1.getHitFraction() > result.fraction) {
					col1.setHitFraction(result.fraction);
				}

				if (resultFraction > result.fraction) {
					resultFraction = result.fraction;
				}

			}
		}

		return resultFraction;
	}

	@Override
	public void getAllContactManifolds(ObjectArrayList<PersistentManifold> manifoldArray) {
		// should we use ownManifold to avoid adding duplicates?
		if (manifoldPtr != null && ownManifold) {
			manifoldArray.add(manifoldPtr);
		}
	}
	
	public PersistentManifold getManifold() {
		return manifoldPtr;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	public static class CreateFunc extends CollisionAlgorithmCreateFunc {
		private final ObjectPool<ConvexConvexAlgorithm> pool = ObjectPool.get(ConvexConvexAlgorithm.class);

		public ConvexPenetrationDepthSolver pdSolver;
		public SimplexSolverInterface simplexSolver;

		public CreateFunc(SimplexSolverInterface simplexSolver, ConvexPenetrationDepthSolver pdSolver) {
			this.simplexSolver = simplexSolver;
			this.pdSolver = pdSolver;
		}

		@Override
		public CollisionAlgorithm createCollisionAlgorithm(CollisionAlgorithmConstructionInfo ci, CollisionObject body0, CollisionObject body1) {
			ConvexConvexAlgorithm algo = pool.get();
			algo.init(ci.manifold, ci, body0, body1, simplexSolver, pdSolver);
			return algo;
		}

		@Override
		public void releaseCollisionAlgorithm(CollisionAlgorithm algo) {
			pool.release((ConvexConvexAlgorithm)algo);
		}
	}
	
}
