/*
 * Java port of Bullet (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * This source file is part of GIMPACT Library.
 *
 * For the latest info, see http://gimpact.sourceforge.net/
 *
 * Copyright (c) 2007 Francisco Leon Najera. C.C. 80087371.
 * email: projectileman@yahoo.com
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

package com.bulletphysics.extras.gimpact;

import com.bulletphysics.collision.broadphase.BroadphaseNativeType;
import com.bulletphysics.collision.broadphase.CollisionAlgorithm;
import com.bulletphysics.collision.broadphase.CollisionAlgorithmConstructionInfo;
import com.bulletphysics.collision.broadphase.DispatcherInfo;
import com.bulletphysics.collision.dispatch.CollisionAlgorithmCreateFunc;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.ManifoldResult;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.collision.shapes.ConcaveShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.extras.gimpact.BoxCollision.AABB;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;
import com.bulletphysics.util.IntArrayList;
import com.bulletphysics.util.ObjectArrayList;
import com.bulletphysics.util.ObjectPool;
import cz.advel.stack.Stack;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * Collision Algorithm for GImpact Shapes.<p>
 * 
 * For register this algorithm in Bullet, proceed as following:
 * <pre>
 * CollisionDispatcher dispatcher = (CollisionDispatcher)dynamicsWorld.getDispatcher();
 * GImpactCollisionAlgorithm.registerAlgorithm(dispatcher);
 * </pre>
 * 
 * @author jezek2
 */
public class GImpactCollisionAlgorithm extends CollisionAlgorithm {

	protected CollisionAlgorithm convex_algorithm;
    protected PersistentManifold manifoldPtr;
	protected ManifoldResult resultOut;
	protected DispatcherInfo dispatchInfo;
	protected int triface0;
	protected int part0;
	protected int triface1;
	protected int part1;

	private PairSet tmpPairset = new PairSet();
	
	public void init(CollisionAlgorithmConstructionInfo ci, CollisionObject body0, CollisionObject body1) {
		super.init(ci);
		manifoldPtr = null;
		convex_algorithm = null;
	}
	
	@Override
	public void destroy() {
		clearCache();
	}

	@Override
	public void processCollision(CollisionObject body0, CollisionObject body1, DispatcherInfo dispatchInfo, ManifoldResult resultOut) {
		clearCache();

		this.resultOut = resultOut;
		this.dispatchInfo = dispatchInfo;
		GImpactShapeInterface gimpactshape0;
		GImpactShapeInterface gimpactshape1;

		if (body0.getCollisionShape().getShapeType()==BroadphaseNativeType.GIMPACT_SHAPE_PROXYTYPE)
		{
			gimpactshape0 = (GImpactShapeInterface)body0.getCollisionShape();

			if( body1.getCollisionShape().getShapeType()==BroadphaseNativeType.GIMPACT_SHAPE_PROXYTYPE )
			{
				gimpactshape1 = (GImpactShapeInterface)body1.getCollisionShape();

				gimpact_vs_gimpact(body0,body1,gimpactshape0,gimpactshape1);
			}
			else
			{
				gimpact_vs_shape(body0,body1,gimpactshape0,body1.getCollisionShape(),false);
			}

		}
		else if (body1.getCollisionShape().getShapeType()==BroadphaseNativeType.GIMPACT_SHAPE_PROXYTYPE )
		{
			gimpactshape1 = (GImpactShapeInterface)body1.getCollisionShape();

			gimpact_vs_shape(body1,body0,gimpactshape1,body0.getCollisionShape(),true);
		}
	}
	
	public void gimpact_vs_gimpact(CollisionObject body0, CollisionObject body1, GImpactShapeInterface shape0, GImpactShapeInterface shape1) {
		if (shape0.getGImpactShapeType() == ShapeType.TRIMESH_SHAPE) {
			GImpactMeshShape meshshape0 = (GImpactMeshShape) shape0;
			part0 = meshshape0.getMeshPartCount();

			while ((part0--) != 0) {
				gimpact_vs_gimpact(body0, body1, meshshape0.getMeshPart(part0), shape1);
			}

			return;
		}

		if (shape1.getGImpactShapeType() == ShapeType.TRIMESH_SHAPE) {
			GImpactMeshShape meshshape1 = (GImpactMeshShape) shape1;
			part1 = meshshape1.getMeshPartCount();

			while ((part1--) != 0) {
				gimpact_vs_gimpact(body0, body1, shape0, meshshape1.getMeshPart(part1));
			}

			return;
		}

		Transform orgtrans0 = body0.getWorldTransform(Stack.alloc(Transform.class));
		Transform orgtrans1 = body1.getWorldTransform(Stack.alloc(Transform.class));

		PairSet pairset = tmpPairset;
		pairset.clear();

		gimpact_vs_gimpact_find_pairs(orgtrans0, orgtrans1, shape0, shape1, pairset);

		if (pairset.size() == 0) {
			return;
		}
		if (shape0.getGImpactShapeType() == ShapeType.TRIMESH_SHAPE_PART &&
		    shape1.getGImpactShapeType() == ShapeType.TRIMESH_SHAPE_PART) {
			
			GImpactMeshShapePart shapepart0 = (GImpactMeshShapePart) shape0;
			GImpactMeshShapePart shapepart1 = (GImpactMeshShapePart) shape1;
			
			//specialized function
			//#ifdef BULLET_TRIANGLE_COLLISION
			//collide_gjk_triangles(body0,body1,shapepart0,shapepart1,&pairset[0].m_index1,pairset.size());
			//#else
			collide_sat_triangles(body0, body1, shapepart0, shapepart1, pairset, pairset.size());
			//#endif

			return;
		}

		// general function

		shape0.lockChildShapes();
		shape1.lockChildShapes();

		GIM_ShapeRetriever retriever0 = new GIM_ShapeRetriever(shape0);
		GIM_ShapeRetriever retriever1 = new GIM_ShapeRetriever(shape1);

		boolean child_has_transform0 = shape0.childrenHasTransform();
		boolean child_has_transform1 = shape1.childrenHasTransform();

		Transform tmpTrans = Stack.alloc(Transform.class);

		int i = pairset.size();
		while ((i--) != 0) {
			Pair pair = pairset.get(i);
			triface0 = pair.index1;
			triface1 = pair.index2;
			CollisionShape colshape0 = retriever0.getChildShape(triface0);
			CollisionShape colshape1 = retriever1.getChildShape(triface1);

			if (child_has_transform0) {
				tmpTrans.mul(orgtrans0, shape0.getChildTransform(triface0));
				body0.setWorldTransform(tmpTrans);
			}

			if (child_has_transform1) {
				tmpTrans.mul(orgtrans1, shape1.getChildTransform(triface1));
				body1.setWorldTransform(tmpTrans);
			}

			// collide two convex shapes
			convex_vs_convex_collision(body0, body1, colshape0, colshape1);

			if (child_has_transform0) {
				body0.setWorldTransform(orgtrans0);
			}

			if (child_has_transform1) {
				body1.setWorldTransform(orgtrans1);
			}

		}

		shape0.unlockChildShapes();
		shape1.unlockChildShapes();
	}
	
	public void gimpact_vs_shape(CollisionObject body0, CollisionObject body1, GImpactShapeInterface shape0, CollisionShape shape1, boolean swapped) {
		if (shape0.getGImpactShapeType() == ShapeType.TRIMESH_SHAPE) {
			GImpactMeshShape meshshape0 = (GImpactMeshShape) shape0;
			part0 = meshshape0.getMeshPartCount();

			while ((part0--) != 0) {
				gimpact_vs_shape(body0,
						body1,
						meshshape0.getMeshPart(part0),
						shape1, swapped);
			}

			return;
		}

		//#ifdef GIMPACT_VS_PLANE_COLLISION
		if (shape0.getGImpactShapeType() == ShapeType.TRIMESH_SHAPE_PART &&
				shape1.getShapeType() == BroadphaseNativeType.STATIC_PLANE_PROXYTYPE) {
			GImpactMeshShapePart shapepart = (GImpactMeshShapePart) shape0;
			StaticPlaneShape planeshape = (StaticPlaneShape) shape1;
			gimpacttrimeshpart_vs_plane_collision(body0, body1, shapepart, planeshape, swapped);
			return;
		}
		//#endif

		if (shape1.isCompound()) {
			CompoundShape compoundshape = (CompoundShape) shape1;
			gimpact_vs_compoundshape(body0, body1, shape0, compoundshape, swapped);
			return;
		}
		else if (shape1.isConcave()) {
			ConcaveShape concaveshape = (ConcaveShape) shape1;
			gimpact_vs_concave(body0, body1, shape0, concaveshape, swapped);
			return;
		}

		Transform orgtrans0 = body0.getWorldTransform(Stack.alloc(Transform.class));
		Transform orgtrans1 = body1.getWorldTransform(Stack.alloc(Transform.class));

		IntArrayList collided_results = new IntArrayList();

		gimpact_vs_shape_find_pairs(orgtrans0, orgtrans1, shape0, shape1, collided_results);

		if (collided_results.size() == 0) {
			return;
		}
		shape0.lockChildShapes();

		GIM_ShapeRetriever retriever0 = new GIM_ShapeRetriever(shape0);

		boolean child_has_transform0 = shape0.childrenHasTransform();

		Transform tmpTrans = Stack.alloc(Transform.class);

		int i = collided_results.size();

		while ((i--) != 0) {
			int child_index = collided_results.get(i);
			if (swapped) {
				triface1 = child_index;
			}
			else {
				triface0 = child_index;
			}
			CollisionShape colshape0 = retriever0.getChildShape(child_index);

			if (child_has_transform0) {
				tmpTrans.mul(orgtrans0, shape0.getChildTransform(child_index));
				body0.setWorldTransform(tmpTrans);
			}

			// collide two shapes
			if (swapped) {
				shape_vs_shape_collision(body1, body0, shape1, colshape0);
			}
			else {
				shape_vs_shape_collision(body0, body1, colshape0, shape1);
			}

			// restore transforms
			if (child_has_transform0) {
				body0.setWorldTransform(orgtrans0);
			}

		}

		shape0.unlockChildShapes();
	}
	
	public void gimpact_vs_compoundshape(CollisionObject body0, CollisionObject body1, GImpactShapeInterface shape0, CompoundShape shape1, boolean swapped) {
		Transform orgtrans1 = body1.getWorldTransform(Stack.alloc(Transform.class));
		Transform childtrans1 = Stack.alloc(Transform.class);
		Transform tmpTrans = Stack.alloc(Transform.class);

		int i = shape1.getNumChildShapes();
		while ((i--) != 0) {
			CollisionShape colshape1 = shape1.getChildShape(i);
			childtrans1.mul(orgtrans1, shape1.getChildTransform(i, tmpTrans));

			body1.setWorldTransform(childtrans1);

			// collide child shape
			gimpact_vs_shape(body0, body1,
					shape0, colshape1, swapped);

			// restore transforms
			body1.setWorldTransform(orgtrans1);
		}
	}
	
	public void gimpact_vs_concave(CollisionObject body0, CollisionObject body1, GImpactShapeInterface shape0, ConcaveShape shape1, boolean swapped) {
		// create the callback
		GImpactTriangleCallback tricallback = new GImpactTriangleCallback();
		tricallback.algorithm = this;
		tricallback.body0 = body0;
		tricallback.body1 = body1;
		tricallback.gimpactshape0 = shape0;
		tricallback.swapped = swapped;
		tricallback.margin = shape1.getMargin();

		// getting the trimesh AABB
		Transform gimpactInConcaveSpace = Stack.alloc(Transform.class);

		body1.getWorldTransform(gimpactInConcaveSpace);
		gimpactInConcaveSpace.inverse();
		gimpactInConcaveSpace.mul(body0.getWorldTransform(Stack.alloc(Transform.class)));

		Vector3f minAABB = Stack.alloc(Vector3f.class), maxAABB = Stack.alloc(Vector3f.class);
		shape0.getAabb(gimpactInConcaveSpace, minAABB, maxAABB);

		shape1.processAllTriangles(tricallback, minAABB, maxAABB);
	}
	
	/**
	 * Creates a new contact point.
	 */
	protected PersistentManifold newContactManifold(CollisionObject body0, CollisionObject body1) {
		manifoldPtr = dispatcher.getNewManifold(body0, body1);
		return manifoldPtr;
	}

	protected void destroyConvexAlgorithm() {
		if (convex_algorithm != null) {
			//convex_algorithm.destroy();
			dispatcher.freeCollisionAlgorithm(convex_algorithm);
			convex_algorithm = null;
		}
	}

	protected void destroyContactManifolds() {
		if (manifoldPtr == null) return;
		dispatcher.releaseManifold(manifoldPtr);
		manifoldPtr = null;
	}

	protected void clearCache() {
		destroyContactManifolds();
		destroyConvexAlgorithm();

		triface0 = -1;
		part0 = -1;
		triface1 = -1;
		part1 = -1;
	}

	protected PersistentManifold getLastManifold() {
		return manifoldPtr;
	}

	/**
	 * Call before process collision.
	 */
	protected void checkManifold(CollisionObject body0, CollisionObject body1) {
		if (getLastManifold() == null) {
			newContactManifold(body0, body1);
		}

		resultOut.setPersistentManifold(getLastManifold());
	}
	
	/**
	 * Call before process collision.
	 */
	protected CollisionAlgorithm newAlgorithm(CollisionObject body0, CollisionObject body1) {
		checkManifold(body0, body1);

		CollisionAlgorithm convex_algorithm = dispatcher.findAlgorithm(body0, body1, getLastManifold());
		return convex_algorithm;
	}
	
	/**
	 * Call before process collision.
	 */
	protected void checkConvexAlgorithm(CollisionObject body0, CollisionObject body1) {
		if (convex_algorithm != null) return;
		convex_algorithm = newAlgorithm(body0, body1);
	}

	protected void addContactPoint(CollisionObject body0, CollisionObject body1, Vector3f point, Vector3f normal, float distance) {
		resultOut.setShapeIdentifiers(part0, triface0, part1, triface1);
		checkManifold(body0, body1);
		resultOut.addContactPoint(normal, point, distance);
	}

	/*
	protected void collide_gjk_triangles(CollisionObject body0, CollisionObject body1, GImpactMeshShapePart shape0, GImpactMeshShapePart shape1, IntArrayList pairs, int pair_count) {
	}
	*/
	
	void collide_sat_triangles(CollisionObject body0, CollisionObject body1, GImpactMeshShapePart shape0, GImpactMeshShapePart shape1, PairSet pairs, int pair_count) {
		Vector3f tmp = Stack.alloc(Vector3f.class);

		Transform orgtrans0 = body0.getWorldTransform(Stack.alloc(Transform.class));
		Transform orgtrans1 = body1.getWorldTransform(Stack.alloc(Transform.class));

		PrimitiveTriangle ptri0 = Stack.alloc(PrimitiveTriangle.class);
		PrimitiveTriangle ptri1 = Stack.alloc(PrimitiveTriangle.class);
		TriangleContact contact_data = Stack.alloc(TriangleContact.class);

		shape0.lockChildShapes();
		shape1.lockChildShapes();

		int pair_pointer = 0;

		while ((pair_count--) != 0) {
			//triface0 = pairs.get(pair_pointer);
			//triface1 = pairs.get(pair_pointer + 1);
			//pair_pointer += 2;
			Pair pair = pairs.get(pair_pointer++);
			triface0 = pair.index1;
			triface1 = pair.index2;

			shape0.getPrimitiveTriangle(triface0, ptri0);
			shape1.getPrimitiveTriangle(triface1, ptri1);

			//#ifdef TRI_COLLISION_PROFILING
			//bt_begin_gim02_tri_time();
			//#endif

			ptri0.applyTransform(orgtrans0);
			ptri1.applyTransform(orgtrans1);

			// build planes
			ptri0.buildTriPlane();
			ptri1.buildTriPlane();

			// test conservative
			if (ptri0.overlap_test_conservative(ptri1)) {
				if (ptri0.find_triangle_collision_clip_method(ptri1, contact_data)) {

					int j = contact_data.point_count;
					while ((j--) != 0) {
						tmp.x = contact_data.separating_normal.x;
						tmp.y = contact_data.separating_normal.y;
						tmp.z = contact_data.separating_normal.z;

						addContactPoint(body0, body1,
								contact_data.points[j],
								tmp,
								-contact_data.penetration_depth);
					}
				}
			}

			//#ifdef TRI_COLLISION_PROFILING
			//bt_end_gim02_tri_time();
			//#endif
		}

		shape0.unlockChildShapes();
		shape1.unlockChildShapes();
	}

	protected void shape_vs_shape_collision(CollisionObject body0, CollisionObject body1, CollisionShape shape0, CollisionShape shape1) {
		CollisionShape tmpShape0 = body0.getCollisionShape();
		CollisionShape tmpShape1 = body1.getCollisionShape();

		body0.internalSetTemporaryCollisionShape(shape0);
		body1.internalSetTemporaryCollisionShape(shape1);

		{
			CollisionAlgorithm algor = newAlgorithm(body0, body1);
			// post :	checkManifold is called

			resultOut.setShapeIdentifiers(part0, triface0, part1, triface1);

			algor.processCollision(body0, body1, dispatchInfo, resultOut);

			//algor.destroy();
			dispatcher.freeCollisionAlgorithm(algor);
		}

		body0.internalSetTemporaryCollisionShape(tmpShape0);
		body1.internalSetTemporaryCollisionShape(tmpShape1);
	}
	
	protected void convex_vs_convex_collision(CollisionObject body0, CollisionObject body1, CollisionShape shape0, CollisionShape shape1) {
		CollisionShape tmpShape0 = body0.getCollisionShape();
		CollisionShape tmpShape1 = body1.getCollisionShape();

		body0.internalSetTemporaryCollisionShape(shape0);
		body1.internalSetTemporaryCollisionShape(shape1);

		resultOut.setShapeIdentifiers(part0, triface0, part1, triface1);

		checkConvexAlgorithm(body0, body1);
		convex_algorithm.processCollision(body0, body1, dispatchInfo, resultOut);

		body0.internalSetTemporaryCollisionShape(tmpShape0);
		body1.internalSetTemporaryCollisionShape(tmpShape1);
	}

	void gimpact_vs_gimpact_find_pairs(Transform trans0, Transform trans1, GImpactShapeInterface shape0, GImpactShapeInterface shape1, PairSet pairset) {
		if (shape0.hasBoxSet() && shape1.hasBoxSet()) {
			GImpactBvh.find_collision(shape0.getBoxSet(), trans0, shape1.getBoxSet(), trans1, pairset);
		}
		else {
			AABB boxshape0 = Stack.alloc(AABB.class);
			AABB boxshape1 = Stack.alloc(AABB.class);
			int i = shape0.getNumChildShapes();

			while ((i--) != 0) {
				shape0.getChildAabb(i, trans0, boxshape0.min, boxshape0.max);

				int j = shape1.getNumChildShapes();
				while ((j--) != 0) {
					shape1.getChildAabb(i, trans1, boxshape1.min, boxshape1.max);

					if (boxshape1.has_collision(boxshape0)) {
						pairset.push_pair(i, j);
					}
				}
			}
		}
	}

	protected void gimpact_vs_shape_find_pairs(Transform trans0, Transform trans1, GImpactShapeInterface shape0, CollisionShape shape1, IntArrayList collided_primitives) {
		AABB boxshape = Stack.alloc(AABB.class);

		if (shape0.hasBoxSet()) {
			Transform trans1to0 = Stack.alloc(Transform.class);
			trans1to0.inverse(trans0);
			trans1to0.mul(trans1);

			shape1.getAabb(trans1to0, boxshape.min, boxshape.max);

			shape0.getBoxSet().boxQuery(boxshape, collided_primitives);
		}
		else {
			shape1.getAabb(trans1, boxshape.min, boxshape.max);

			AABB boxshape0 = Stack.alloc(AABB.class);
			int i = shape0.getNumChildShapes();

			while ((i--) != 0) {
				shape0.getChildAabb(i, trans0, boxshape0.min, boxshape0.max);

				if (boxshape.has_collision(boxshape0)) {
					collided_primitives.add(i);
				}
			}
		}
	}
	
	protected void gimpacttrimeshpart_vs_plane_collision(CollisionObject body0, CollisionObject body1, GImpactMeshShapePart shape0, StaticPlaneShape shape1, boolean swapped) {
		Transform orgtrans0 = body0.getWorldTransform(Stack.alloc(Transform.class));
		Transform orgtrans1 = body1.getWorldTransform(Stack.alloc(Transform.class));

		StaticPlaneShape planeshape = shape1;
		Vector4f plane = Stack.alloc(Vector4f.class);
		PlaneShape.get_plane_equation_transformed(planeshape, orgtrans1, plane);

		// test box against plane

		AABB tribox = Stack.alloc(AABB.class);
		shape0.getAabb(orgtrans0, tribox.min, tribox.max);
		tribox.increment_margin(planeshape.getMargin());

		if (tribox.plane_classify(plane) != PlaneIntersectionType.COLLIDE_PLANE) {
			return;
		}
		shape0.lockChildShapes();

		float margin = shape0.getMargin() + planeshape.getMargin();

		Vector3f vertex = Stack.alloc(Vector3f.class);

		Vector3f tmp = Stack.alloc(Vector3f.class);

		int vi = shape0.getVertexCount();
		while ((vi--) != 0) {
			shape0.getVertex(vi, vertex);
			orgtrans0.transform(vertex);

			float distance = VectorUtil.dot3(vertex, plane) - plane.w - margin;

			if (distance < 0f)//add contact
			{
				if (swapped) {
					tmp.set(-plane.x, -plane.y, -plane.z);
					addContactPoint(body1, body0, vertex, tmp, distance);
				}
				else {
					tmp.set(plane.x, plane.y, plane.z);
					addContactPoint(body0, body1, vertex, tmp, distance);
				}
			}
		}

		shape0.unlockChildShapes();
	}
	
	
	public void setFace0(int value) {
		triface0 = value;
	}

	public int getFace0() {
		return triface0;
	}

	public void setFace1(int value) {
		triface1 = value;
	}

	public int getFace1() {
		return triface1;
	}

	public void setPart0(int value) {
		part0 = value;
	}

	public int getPart0() {
		return part0;
	}

	public void setPart1(int value) {
		part1 = value;
	}

	public int getPart1() {
		return part1;
	}

	@Override
	public float calculateTimeOfImpact(CollisionObject body0, CollisionObject body1, DispatcherInfo dispatchInfo, ManifoldResult resultOut) {
		return 1f;
	}

	@Override
	public void getAllContactManifolds(ObjectArrayList<PersistentManifold> manifoldArray) {
		if (manifoldPtr != null) {
			manifoldArray.add(manifoldPtr);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Use this function for register the algorithm externally.
	 */
	public static void registerAlgorithm(CollisionDispatcher dispatcher) {
		CreateFunc createFunc = new CreateFunc();

		for (int i=0; i<BroadphaseNativeType.MAX_BROADPHASE_COLLISION_TYPES.ordinal(); i++) {
			dispatcher.registerCollisionCreateFunc(BroadphaseNativeType.GIMPACT_SHAPE_PROXYTYPE.ordinal(), i, createFunc);
		}

		for (int i=0; i<BroadphaseNativeType.MAX_BROADPHASE_COLLISION_TYPES.ordinal(); i++) {
			dispatcher.registerCollisionCreateFunc(i, BroadphaseNativeType.GIMPACT_SHAPE_PROXYTYPE.ordinal(), createFunc);
		}
	}
	
	public static class CreateFunc extends CollisionAlgorithmCreateFunc {
		private final ObjectPool<GImpactCollisionAlgorithm> pool = ObjectPool.get(GImpactCollisionAlgorithm.class);
		
		@Override
		public CollisionAlgorithm createCollisionAlgorithm(CollisionAlgorithmConstructionInfo ci, CollisionObject body0, CollisionObject body1) {
			GImpactCollisionAlgorithm algo = pool.get();
			algo.init(ci, body0, body1);
			return algo;
		}

		@Override
		public void releaseCollisionAlgorithm(CollisionAlgorithm algo) {
			pool.release((GImpactCollisionAlgorithm)algo);
		}
	}
	
}
