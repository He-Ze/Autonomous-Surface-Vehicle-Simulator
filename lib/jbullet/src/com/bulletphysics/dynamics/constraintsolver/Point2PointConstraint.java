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

package com.bulletphysics.dynamics.constraintsolver;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;
import cz.advel.stack.Stack;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

/**
 * Point to point constraint between two rigid bodies each with a pivot point that
 * descibes the "ballsocket" location in local space.
 * 
 * @author jezek2
 */
public class Point2PointConstraint extends TypedConstraint {

	private final JacobianEntry[] jac = new JacobianEntry[]/*[3]*/ { new JacobianEntry(), new JacobianEntry(), new JacobianEntry() }; // 3 orthogonal linear constraints
	
	private final Vector3f pivotInA = new Vector3f();
	private final Vector3f pivotInB = new Vector3f();

	public ConstraintSetting setting = new ConstraintSetting();
	
	public Point2PointConstraint() {
		super(TypedConstraintType.POINT2POINT_CONSTRAINT_TYPE);
	}

	public Point2PointConstraint(RigidBody rbA, RigidBody rbB, Vector3f pivotInA, Vector3f pivotInB) {
		super(TypedConstraintType.POINT2POINT_CONSTRAINT_TYPE, rbA, rbB);
		this.pivotInA.set(pivotInA);
		this.pivotInB.set(pivotInB);
	}

	public Point2PointConstraint(RigidBody rbA, Vector3f pivotInA) {
		super(TypedConstraintType.POINT2POINT_CONSTRAINT_TYPE, rbA);
		this.pivotInA.set(pivotInA);
		this.pivotInB.set(pivotInA);
		rbA.getCenterOfMassTransform(Stack.alloc(Transform.class)).transform(this.pivotInB);
	}

	@Override
	public void buildJacobian() {
		appliedImpulse = 0f;

		Vector3f normal = Stack.alloc(Vector3f.class);
		normal.set(0f, 0f, 0f);

		Matrix3f tmpMat1 = Stack.alloc(Matrix3f.class);
		Matrix3f tmpMat2 = Stack.alloc(Matrix3f.class);
		Vector3f tmp1 = Stack.alloc(Vector3f.class);
		Vector3f tmp2 = Stack.alloc(Vector3f.class);
		Vector3f tmpVec = Stack.alloc(Vector3f.class);
		
		Transform centerOfMassA = rbA.getCenterOfMassTransform(Stack.alloc(Transform.class));
		Transform centerOfMassB = rbB.getCenterOfMassTransform(Stack.alloc(Transform.class));

		for (int i = 0; i < 3; i++) {
			VectorUtil.setCoord(normal, i, 1f);

			tmpMat1.transpose(centerOfMassA.basis);
			tmpMat2.transpose(centerOfMassB.basis);

			tmp1.set(pivotInA);
			centerOfMassA.transform(tmp1);
			tmp1.sub(rbA.getCenterOfMassPosition(tmpVec));

			tmp2.set(pivotInB);
			centerOfMassB.transform(tmp2);
			tmp2.sub(rbB.getCenterOfMassPosition(tmpVec));

			jac[i].init(
					tmpMat1,
					tmpMat2,
					tmp1,
					tmp2,
					normal,
					rbA.getInvInertiaDiagLocal(Stack.alloc(Vector3f.class)),
					rbA.getInvMass(),
					rbB.getInvInertiaDiagLocal(Stack.alloc(Vector3f.class)),
					rbB.getInvMass());
			VectorUtil.setCoord(normal, i, 0f);
		}
	}

	@Override
	public void solveConstraint(float timeStep) {
		Vector3f tmp = Stack.alloc(Vector3f.class);
		Vector3f tmp2 = Stack.alloc(Vector3f.class);
		Vector3f tmpVec = Stack.alloc(Vector3f.class);

		Transform centerOfMassA = rbA.getCenterOfMassTransform(Stack.alloc(Transform.class));
		Transform centerOfMassB = rbB.getCenterOfMassTransform(Stack.alloc(Transform.class));
		
		Vector3f pivotAInW = Stack.alloc(pivotInA);
		centerOfMassA.transform(pivotAInW);

		Vector3f pivotBInW = Stack.alloc(pivotInB);
		centerOfMassB.transform(pivotBInW);

		Vector3f normal = Stack.alloc(Vector3f.class);
		normal.set(0f, 0f, 0f);

		//btVector3 angvelA = m_rbA.getCenterOfMassTransform().getBasis().transpose() * m_rbA.getAngularVelocity();
		//btVector3 angvelB = m_rbB.getCenterOfMassTransform().getBasis().transpose() * m_rbB.getAngularVelocity();

		for (int i = 0; i < 3; i++) {
			VectorUtil.setCoord(normal, i, 1f);
			float jacDiagABInv = 1f / jac[i].getDiagonal();

			Vector3f rel_pos1 = Stack.alloc(Vector3f.class);
			rel_pos1.sub(pivotAInW, rbA.getCenterOfMassPosition(tmpVec));
			Vector3f rel_pos2 = Stack.alloc(Vector3f.class);
			rel_pos2.sub(pivotBInW, rbB.getCenterOfMassPosition(tmpVec));
			// this jacobian entry could be re-used for all iterations

			Vector3f vel1 = rbA.getVelocityInLocalPoint(rel_pos1, Stack.alloc(Vector3f.class));
			Vector3f vel2 = rbB.getVelocityInLocalPoint(rel_pos2, Stack.alloc(Vector3f.class));
			Vector3f vel = Stack.alloc(Vector3f.class);
			vel.sub(vel1, vel2);

			float rel_vel;
			rel_vel = normal.dot(vel);

			/*
			//velocity error (first order error)
			btScalar rel_vel = m_jac[i].getRelativeVelocity(m_rbA.getLinearVelocity(),angvelA,
			m_rbB.getLinearVelocity(),angvelB);
			 */

			// positional error (zeroth order error)
			tmp.sub(pivotAInW, pivotBInW);
			float depth = -tmp.dot(normal); //this is the error projected on the normal

			float impulse = depth * setting.tau / timeStep * jacDiagABInv - setting.damping * rel_vel * jacDiagABInv;

			float impulseClamp = setting.impulseClamp;
			if (impulseClamp > 0f) {
				if (impulse < -impulseClamp) {
					impulse = -impulseClamp;
				}
				if (impulse > impulseClamp) {
					impulse = impulseClamp;
				}
			}

			appliedImpulse += impulse;
			Vector3f impulse_vector = Stack.alloc(Vector3f.class);
			impulse_vector.scale(impulse, normal);
			tmp.sub(pivotAInW, rbA.getCenterOfMassPosition(tmpVec));
			rbA.applyImpulse(impulse_vector, tmp);
			tmp.negate(impulse_vector);
			tmp2.sub(pivotBInW, rbB.getCenterOfMassPosition(tmpVec));
			rbB.applyImpulse(tmp, tmp2);

			VectorUtil.setCoord(normal, i, 0f);
		}
	}
	
	public void updateRHS(float timeStep) {
	}

	public void setPivotA(Vector3f pivotA) {
		pivotInA.set(pivotA);
	}

	public void setPivotB(Vector3f pivotB) {
		pivotInB.set(pivotB);
	}

	public Vector3f getPivotInA(Vector3f out) {
		out.set(pivotInA);
		return out;
	}

	public Vector3f getPivotInB(Vector3f out) {
		out.set(pivotInB);
		return out;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	public static class ConstraintSetting {
		public float tau = 0.3f;
		public float damping = 1f;
		public float impulseClamp = 0f;
	}
	
}
