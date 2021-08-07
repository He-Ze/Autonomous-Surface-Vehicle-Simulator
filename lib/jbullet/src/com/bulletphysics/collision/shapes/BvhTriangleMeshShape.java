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
import com.bulletphysics.util.ObjectPool;
import com.bulletphysics.collision.broadphase.BroadphaseNativeType;
import com.bulletphysics.linearmath.VectorUtil;
import cz.advel.stack.Stack;
import javax.vecmath.Vector3f;

/**
 * BvhTriangleMeshShape is a static-triangle mesh shape with several optimizations,
 * such as bounding volume hierarchy. It is recommended to enable useQuantizedAabbCompression
 * for better memory usage.<p>
 *
 * It takes a triangle mesh as input, for example a {@link TriangleMesh} or
 * {@link TriangleIndexVertexArray}. The BvhTriangleMeshShape class allows for
 * triangle mesh deformations by a refit or partialRefit method.<p>
 *
 * Instead of building the bounding volume hierarchy acceleration structure, it is
 * also possible to serialize (save) and deserialize (load) the structure from disk.
 * See ConcaveDemo for an example.
 * 
 * @author jezek2
 */
public class BvhTriangleMeshShape extends TriangleMeshShape {

	private OptimizedBvh bvh;
	private boolean useQuantizedAabbCompression;
	private boolean ownsBvh;
	
	private ObjectPool<MyNodeOverlapCallback> myNodeCallbacks = ObjectPool.get(MyNodeOverlapCallback.class);
	
	public BvhTriangleMeshShape() {
		super(null);
		this.bvh = null;
		this.ownsBvh = false;
	}

	public BvhTriangleMeshShape(StridingMeshInterface meshInterface, boolean useQuantizedAabbCompression) {
		this(meshInterface, useQuantizedAabbCompression, true);
	}
	
	public BvhTriangleMeshShape(StridingMeshInterface meshInterface, boolean useQuantizedAabbCompression, boolean buildBvh) {
		super(meshInterface);
		this.bvh = null;
		this.useQuantizedAabbCompression = useQuantizedAabbCompression;
		this.ownsBvh = false;

		// construct bvh from meshInterface
		//#ifndef DISABLE_BVH

		Vector3f bvhAabbMin = new Vector3f(), bvhAabbMax = new Vector3f();
		meshInterface.calculateAabbBruteForce(bvhAabbMin, bvhAabbMax);

		if (buildBvh) {
			bvh = new OptimizedBvh();
			bvh.build(meshInterface, useQuantizedAabbCompression, bvhAabbMin, bvhAabbMax);
			ownsBvh = true;

			// JAVA NOTE: moved from TriangleMeshShape
			recalcLocalAabb();
		}

		//#endif //DISABLE_BVH
	}

	/**
	 * Optionally pass in a larger bvh aabb, used for quantization. This allows for deformations within this aabb.
	 */
	public BvhTriangleMeshShape(StridingMeshInterface meshInterface, boolean useQuantizedAabbCompression, Vector3f bvhAabbMin, Vector3f bvhAabbMax) {
		this(meshInterface, useQuantizedAabbCompression, bvhAabbMin, bvhAabbMax, true);
	}
	
	/**
	 * Optionally pass in a larger bvh aabb, used for quantization. This allows for deformations within this aabb.
	 */
	public BvhTriangleMeshShape(StridingMeshInterface meshInterface, boolean useQuantizedAabbCompression, Vector3f bvhAabbMin, Vector3f bvhAabbMax, boolean buildBvh) {
		super(meshInterface);

		this.bvh = null;
		this.useQuantizedAabbCompression = useQuantizedAabbCompression;
		this.ownsBvh = false;

		// construct bvh from meshInterface
		//#ifndef DISABLE_BVH

		if (buildBvh) {
			bvh = new OptimizedBvh();

			bvh.build(meshInterface, useQuantizedAabbCompression, bvhAabbMin, bvhAabbMax);
			ownsBvh = true;
		}

		// JAVA NOTE: moved from TriangleMeshShape
		recalcLocalAabb();
		//#endif //DISABLE_BVH
	}

	public boolean getOwnsBvh() {
		return ownsBvh;
	}
	
	@Override
	public BroadphaseNativeType getShapeType() {
		return BroadphaseNativeType.TRIANGLE_MESH_SHAPE_PROXYTYPE;
	}

	public void performRaycast(TriangleCallback callback, Vector3f raySource, Vector3f rayTarget) {
		MyNodeOverlapCallback myNodeCallback = myNodeCallbacks.get();
		myNodeCallback.init(callback, meshInterface);

		bvh.reportRayOverlappingNodex(myNodeCallback, raySource, rayTarget);
		
		myNodeCallbacks.release(myNodeCallback);
	}
	
	public void performConvexcast(TriangleCallback callback, Vector3f raySource, Vector3f rayTarget, Vector3f aabbMin, Vector3f aabbMax) {
		MyNodeOverlapCallback myNodeCallback = myNodeCallbacks.get();
		myNodeCallback.init(callback, meshInterface);

		bvh.reportBoxCastOverlappingNodex(myNodeCallback, raySource, rayTarget, aabbMin, aabbMax);

		myNodeCallbacks.release(myNodeCallback);
	}

	/**
	 * Perform bvh tree traversal and report overlapping triangles to 'callback'.
	 */
	@Override
	public void processAllTriangles(TriangleCallback callback, Vector3f aabbMin, Vector3f aabbMax) {
		//#ifdef DISABLE_BVH
		// // brute force traverse all triangles
		//btTriangleMeshShape::processAllTriangles(callback,aabbMin,aabbMax);
		//#else

		// first get all the nodes
		MyNodeOverlapCallback myNodeCallback = myNodeCallbacks.get();
		myNodeCallback.init(callback, meshInterface);

		bvh.reportAabbOverlappingNodex(myNodeCallback, aabbMin, aabbMax);

		myNodeCallbacks.release(myNodeCallback);
		//#endif//DISABLE_BVH
	}
	
	public void refitTree(Vector3f aabbMin, Vector3f aabbMax) {
		// JAVA NOTE: update it for 2.70b1
		//bvh.refit(meshInterface, aabbMin, aabbMax);
		bvh.refit(meshInterface);

		recalcLocalAabb();
	}

	/**
	 * For a fast incremental refit of parts of the tree. Note: the entire AABB of the tree will become more conservative, it never shrinks.
	 */
	public void partialRefitTree(Vector3f aabbMin, Vector3f aabbMax) {
		bvh.refitPartial(meshInterface,aabbMin,aabbMax );

		VectorUtil.setMin(localAabbMin, aabbMin);
		VectorUtil.setMax(localAabbMax, aabbMax);
	}

	@Override
	public String getName() {
		return "BVHTRIANGLEMESH";
	}
	
	@Override
	public void setLocalScaling(Vector3f scaling) {
		Vector3f tmp = Stack.alloc(Vector3f.class);
		tmp.sub(getLocalScaling(Stack.alloc(Vector3f.class)), scaling);

		if (tmp.lengthSquared() > BulletGlobals.SIMD_EPSILON) {
			super.setLocalScaling(scaling);
			/*
			if (ownsBvh)
			{
			m_bvh->~btOptimizedBvh();
			btAlignedFree(m_bvh);
			}
			*/
			///m_localAabbMin/m_localAabbMax is already re-calculated in btTriangleMeshShape. We could just scale aabb, but this needs some more work
			bvh = new OptimizedBvh();
			// rebuild the bvh...
			bvh.build(meshInterface, useQuantizedAabbCompression, localAabbMin, localAabbMax);
			ownsBvh = true;
		}
	}
	
	public OptimizedBvh getOptimizedBvh() {
		return bvh;
	}

	public void setOptimizedBvh(OptimizedBvh bvh) {
		Vector3f scaling = Stack.alloc(Vector3f.class);
		scaling.set(1f, 1f, 1f);
		setOptimizedBvh(bvh, scaling);
	}

	public void setOptimizedBvh(OptimizedBvh bvh, Vector3f scaling) {
		assert (this.bvh == null);
		assert (!ownsBvh);

		this.bvh = bvh;
		ownsBvh = false;

		// update the scaling without rebuilding the bvh
		Vector3f tmp = Stack.alloc(Vector3f.class);
		tmp.sub(getLocalScaling(Stack.alloc(Vector3f.class)), scaling);

		if (tmp.lengthSquared() > BulletGlobals.SIMD_EPSILON) {
			super.setLocalScaling(scaling);
		}
	}

	public boolean usesQuantizedAabbCompression() {
		return useQuantizedAabbCompression;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	protected static class MyNodeOverlapCallback extends NodeOverlapCallback {
		public StridingMeshInterface meshInterface;
		public TriangleCallback callback;

		private Vector3f[] triangle/*[3]*/ = new Vector3f[] { new Vector3f(), new Vector3f(), new Vector3f() };

		public MyNodeOverlapCallback() {
		}
		
		public void init(TriangleCallback callback, StridingMeshInterface meshInterface) {
			this.meshInterface = meshInterface;
			this.callback = callback;
		}

		public void processNode(int nodeSubPart, int nodeTriangleIndex) {
			VertexData data = meshInterface.getLockedReadOnlyVertexIndexBase(nodeSubPart);

			Vector3f meshScaling = meshInterface.getScaling(Stack.alloc(Vector3f.class));

			data.getTriangle(nodeTriangleIndex*3, meshScaling, triangle);

			/* Perform ray vs. triangle collision here */
			callback.processTriangle(triangle, nodeSubPart, nodeTriangleIndex);
			
			meshInterface.unLockReadOnlyVertexBase(nodeSubPart);
		}
	}
	
}
