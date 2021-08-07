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

/*
2007-09-09
btGeneric6DofConstraint Refactored by Francisco Le�n
email: projectileman@yahoo.com
http://gimpact.sf.net
*/

package com.bulletphysics.dynamics.constraintsolver;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MatrixUtil;
import com.bulletphysics.linearmath.Transform;


/// 
import com.bulletphysics.linearmath.VectorUtil;
import cz.advel.stack.Stack;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
/*!

*/
/**
 * Generic6DofConstraint between two rigidbodies each with a pivot point that descibes
 * the axis location in local space.<p>
 * 
 * Generic6DofConstraint can leave any of the 6 degree of freedom "free" or "locked".
 * Currently this limit supports rotational motors.<br>
 * 
 * <ul>
 * <li>For linear limits, use {@link #setLinearUpperLimit}, {@link #setLinearLowerLimit}.
 * You can set the parameters with the {@link TranslationalLimitMotor} structure accsesible
 * through the {@link #getTranslationalLimitMotor} method.
 * At this moment translational motors are not supported. May be in the future.</li>
 * 
 * <li>For angular limits, use the {@link RotationalLimitMotor} structure for configuring
 * the limit. This is accessible through {@link #getRotationalLimitMotor} method,
 * this brings support for limit parameters and motors.</li>
 * 
 * <li>Angulars limits have these possible ranges:
 * <table border="1">
 * <tr>
 * 	<td><b>AXIS</b></td>
 * 	<td><b>MIN ANGLE</b></td>
 * 	<td><b>MAX ANGLE</b></td>
 * </tr><tr>
 * 	<td>X</td>
 * 		<td>-PI</td>
 * 		<td>PI</td>
 * </tr><tr>
 * 	<td>Y</td>
 * 		<td>-PI/2</td>
 * 		<td>PI/2</td>
 * </tr><tr>
 * 	<td>Z</td>
 * 		<td>-PI/2</td>
 * 		<td>PI/2</td>
 * </tr>
 * </table>
 * </li>
 * </ul>
 * 
 * @author jezek2
 */
public class Generic6DofConstraint extends TypedConstraint {

	protected final Transform frameInA = new Transform(); //!< the constraint space w.r.t body A
    protected final Transform frameInB = new Transform(); //!< the constraint space w.r.t body B

	protected final JacobianEntry[] jacLinear/*[3]*/ = new JacobianEntry[] { new JacobianEntry(), new JacobianEntry(), new JacobianEntry() }; //!< 3 orthogonal linear constraints
    protected final JacobianEntry[] jacAng/*[3]*/ = new JacobianEntry[] { new JacobianEntry(), new JacobianEntry(), new JacobianEntry() }; //!< 3 orthogonal angular constraints

	protected final TranslationalLimitMotor linearLimits = new TranslationalLimitMotor();

	protected final RotationalLimitMotor[] angularLimits/*[3]*/ = new RotationalLimitMotor[] { new RotationalLimitMotor(), new RotationalLimitMotor(), new RotationalLimitMotor() };

	protected float timeStep;
    protected final Transform calculatedTransformA = new Transform();
    protected final Transform calculatedTransformB = new Transform();
    protected final Vector3f calculatedAxisAngleDiff = new Vector3f();
    protected final Vector3f[] calculatedAxis/*[3]*/ = new Vector3f[] { new Vector3f(), new Vector3f(), new Vector3f() };
	
	protected final Vector3f anchorPos = new Vector3f(); // point betwen pivots of bodies A and B to solve linear axes
    
    protected boolean useLinearReferenceFrameA;

	public Generic6DofConstraint() {
		super(TypedConstraintType.D6_CONSTRAINT_TYPE);
		useLinearReferenceFrameA = true;
	}

	public Generic6DofConstraint(RigidBody rbA, RigidBody rbB, Transform frameInA, Transform frameInB, boolean useLinearReferenceFrameA) {
		super(TypedConstraintType.D6_CONSTRAINT_TYPE, rbA, rbB);
		this.frameInA.set(frameInA);
		this.frameInB.set(frameInB);
		this.useLinearReferenceFrameA = useLinearReferenceFrameA;
	}

	private static float getMatrixElem(Matrix3f mat, int index) {
		int i = index % 3;
		int j = index / 3;
		return mat.getElement(i, j);
	}
	
	/**
	 * MatrixToEulerXYZ from http://www.geometrictools.com/LibFoundation/Mathematics/Wm4Matrix3.inl.html
	 */
	private static boolean matrixToEulerXYZ(Matrix3f mat, Vector3f xyz) {
		//	// rot =  cy*cz          -cy*sz           sy
		//	//        cz*sx*sy+cx*sz  cx*cz-sx*sy*sz -cy*sx
		//	//       -cx*cz*sy+sx*sz  cz*sx+cx*sy*sz  cx*cy
		//

		if (getMatrixElem(mat, 2) < 1.0f) {
			if (getMatrixElem(mat, 2) > -1.0f) {
				xyz.x = (float) Math.atan2(-getMatrixElem(mat, 5), getMatrixElem(mat, 8));
				xyz.y = (float) Math.asin(getMatrixElem(mat, 2));
				xyz.z = (float) Math.atan2(-getMatrixElem(mat, 1), getMatrixElem(mat, 0));
				return true;
			}
			else {
				// WARNING.  Not unique.  XA - ZA = -atan2(r10,r11)
				xyz.x = -(float) Math.atan2(getMatrixElem(mat, 3), getMatrixElem(mat, 4));
				xyz.y = -BulletGlobals.SIMD_HALF_PI;
				xyz.z = 0.0f;
				return false;
			}
		}
		else {
			// WARNING.  Not unique.  XAngle + ZAngle = atan2(r10,r11)
			xyz.x = (float) Math.atan2(getMatrixElem(mat, 3), getMatrixElem(mat, 4));
			xyz.y = BulletGlobals.SIMD_HALF_PI;
			xyz.z = 0.0f;
		}

		return false;
	}

	/**
	 * Calcs the euler angles between the two bodies.
	 */
	protected void calculateAngleInfo() {
		Matrix3f mat = Stack.alloc(Matrix3f.class);

		Matrix3f relative_frame = Stack.alloc(Matrix3f.class);
		mat.set(calculatedTransformA.basis);
		MatrixUtil.invert(mat);
		relative_frame.mul(mat, calculatedTransformB.basis);

		matrixToEulerXYZ(relative_frame, calculatedAxisAngleDiff);

		// in euler angle mode we do not actually constrain the angular velocity
		// along the axes axis[0] and axis[2] (although we do use axis[1]) :
		//
		//    to get			constrain w2-w1 along		...not
		//    ------			---------------------		------
		//    d(angle[0])/dt = 0	ax[1] x ax[2]			ax[0]
		//    d(angle[1])/dt = 0	ax[1]
		//    d(angle[2])/dt = 0	ax[0] x ax[1]			ax[2]
		//
		// constraining w2-w1 along an axis 'a' means that a'*(w2-w1)=0.
		// to prove the result for angle[0], write the expression for angle[0] from
		// GetInfo1 then take the derivative. to prove this for angle[2] it is
		// easier to take the euler rate expression for d(angle[2])/dt with respect
		// to the components of w and set that to 0.

		Vector3f axis0 = Stack.alloc(Vector3f.class);
		calculatedTransformB.basis.getColumn(0, axis0);

		Vector3f axis2 = Stack.alloc(Vector3f.class);
		calculatedTransformA.basis.getColumn(2, axis2);

		calculatedAxis[1].cross(axis2, axis0);
		calculatedAxis[0].cross(calculatedAxis[1], axis2);
		calculatedAxis[2].cross(axis0, calculatedAxis[1]);

		//    if(m_debugDrawer)
		//    {
		//
		//    	char buff[300];
		//		sprintf(buff,"\n X: %.2f ; Y: %.2f ; Z: %.2f ",
		//		m_calculatedAxisAngleDiff[0],
		//		m_calculatedAxisAngleDiff[1],
		//		m_calculatedAxisAngleDiff[2]);
		//    	m_debugDrawer->reportErrorWarning(buff);
		//    }
	}

	/**
	 * Calcs global transform of the offsets.<p>
	 * Calcs the global transform for the joint offset for body A an B, and also calcs the agle differences between the bodies.
	 * 
	 * See also: Generic6DofConstraint.getCalculatedTransformA, Generic6DofConstraint.getCalculatedTransformB, Generic6DofConstraint.calculateAngleInfo
	 */
	public void calculateTransforms() {
		rbA.getCenterOfMassTransform(calculatedTransformA);
		calculatedTransformA.mul(frameInA);

		rbB.getCenterOfMassTransform(calculatedTransformB);
		calculatedTransformB.mul(frameInB);

		calculateAngleInfo();
	}
	
	protected void buildLinearJacobian(/*JacobianEntry jacLinear*/int jacLinear_index, Vector3f normalWorld, Vector3f pivotAInW, Vector3f pivotBInW) {
		Matrix3f mat1 = rbA.getCenterOfMassTransform(Stack.alloc(Transform.class)).basis;
		mat1.transpose();

		Matrix3f mat2 = rbB.getCenterOfMassTransform(Stack.alloc(Transform.class)).basis;
		mat2.transpose();

		Vector3f tmpVec = Stack.alloc(Vector3f.class);
		
		Vector3f tmp1 = Stack.alloc(Vector3f.class);
		tmp1.sub(pivotAInW, rbA.getCenterOfMassPosition(tmpVec));

		Vector3f tmp2 = Stack.alloc(Vector3f.class);
		tmp2.sub(pivotBInW, rbB.getCenterOfMassPosition(tmpVec));

		jacLinear[jacLinear_index].init(
				mat1,
				mat2,
				tmp1,
				tmp2,
				normalWorld,
				rbA.getInvInertiaDiagLocal(Stack.alloc(Vector3f.class)),
				rbA.getInvMass(),
				rbB.getInvInertiaDiagLocal(Stack.alloc(Vector3f.class)),
				rbB.getInvMass());
	}

	protected void buildAngularJacobian(/*JacobianEntry jacAngular*/int jacAngular_index, Vector3f jointAxisW) {
		Matrix3f mat1 = rbA.getCenterOfMassTransform(Stack.alloc(Transform.class)).basis;
		mat1.transpose();

		Matrix3f mat2 = rbB.getCenterOfMassTransform(Stack.alloc(Transform.class)).basis;
		mat2.transpose();

		jacAng[jacAngular_index].init(jointAxisW,
				mat1,
				mat2,
				rbA.getInvInertiaDiagLocal(Stack.alloc(Vector3f.class)),
				rbB.getInvInertiaDiagLocal(Stack.alloc(Vector3f.class)));
	}

	/**
	 * Test angular limit.<p>
	 * Calculates angular correction and returns true if limit needs to be corrected.
	 * Generic6DofConstraint.buildJacobian must be called previously.
	 */
	public boolean testAngularLimitMotor(int axis_index) {
		float angle = VectorUtil.getCoord(calculatedAxisAngleDiff, axis_index);

		// test limits
		angularLimits[axis_index].testLimitValue(angle);
		return angularLimits[axis_index].needApplyTorques();
	}
	
	@Override
	public void buildJacobian() {
		// Clear accumulated impulses for the next simulation step
		linearLimits.accumulatedImpulse.set(0f, 0f, 0f);
		for (int i=0; i<3; i++) {
			angularLimits[i].accumulatedImpulse = 0f;
		}
		
		// calculates transform
		calculateTransforms();
		
		Vector3f tmpVec = Stack.alloc(Vector3f.class);

		//  const btVector3& pivotAInW = m_calculatedTransformA.getOrigin();
		//  const btVector3& pivotBInW = m_calculatedTransformB.getOrigin();
		calcAnchorPos();
		Vector3f pivotAInW = Stack.alloc(anchorPos);
		Vector3f pivotBInW = Stack.alloc(anchorPos);
		
		// not used here
		//    btVector3 rel_pos1 = pivotAInW - m_rbA.getCenterOfMassPosition();
		//    btVector3 rel_pos2 = pivotBInW - m_rbB.getCenterOfMassPosition();

		Vector3f normalWorld = Stack.alloc(Vector3f.class);
		// linear part
		for (int i=0; i<3; i++) {
			if (linearLimits.isLimited(i)) {
				if (useLinearReferenceFrameA) {
					calculatedTransformA.basis.getColumn(i, normalWorld);
				}
				else {
					calculatedTransformB.basis.getColumn(i, normalWorld);
				}

				buildLinearJacobian(
						/*jacLinear[i]*/i, normalWorld,
						pivotAInW, pivotBInW);

			}
		}

		// angular part
		for (int i=0; i<3; i++) {
			// calculates error angle
			if (testAngularLimitMotor(i)) {
				this.getAxis(i, normalWorld);
				// Create angular atom
				buildAngularJacobian(/*jacAng[i]*/i, normalWorld);
			}
		}
	}

	@Override
	public void solveConstraint(float timeStep) {
		this.timeStep = timeStep;

		//calculateTransforms();

		int i;

		// linear

		Vector3f pointInA = Stack.alloc(calculatedTransformA.origin);
		Vector3f pointInB = Stack.alloc(calculatedTransformB.origin);

		float jacDiagABInv;
		Vector3f linear_axis = Stack.alloc(Vector3f.class);
		for (i = 0; i < 3; i++) {
			if (linearLimits.isLimited(i)) {
				jacDiagABInv = 1f / jacLinear[i].getDiagonal();

				if (useLinearReferenceFrameA) {
					calculatedTransformA.basis.getColumn(i, linear_axis);
				}
				else {
					calculatedTransformB.basis.getColumn(i, linear_axis);
				}

				linearLimits.solveLinearAxis(
						this.timeStep,
						jacDiagABInv,
						rbA, pointInA,
						rbB, pointInB,
						i, linear_axis, anchorPos);

			}
		}

		// angular
		Vector3f angular_axis = Stack.alloc(Vector3f.class);
		float angularJacDiagABInv;
		for (i = 0; i < 3; i++) {
			if (angularLimits[i].needApplyTorques()) {
				// get axis
				getAxis(i, angular_axis);

				angularJacDiagABInv = 1f / jacAng[i].getDiagonal();

				angularLimits[i].solveAngularLimits(this.timeStep, angular_axis, angularJacDiagABInv, rbA, rbB);
			}
		}
	}
	

    public void updateRHS(float timeStep) {
	}

	/**
	 * Get the rotation axis in global coordinates.
	 * Generic6DofConstraint.buildJacobian must be called previously.
	 */
	public Vector3f getAxis(int axis_index, Vector3f out) {
		out.set(calculatedAxis[axis_index]);
		return out;
	}

	/**
	 * Get the relative Euler angle.
	 * Generic6DofConstraint.buildJacobian must be called previously.
	 */
	public float getAngle(int axis_index) {
		return VectorUtil.getCoord(calculatedAxisAngleDiff, axis_index);
	}

	/**
	 * Gets the global transform of the offset for body A.<p>
	 * See also: Generic6DofConstraint.getFrameOffsetA, Generic6DofConstraint.getFrameOffsetB, Generic6DofConstraint.calculateAngleInfo.
	 */
	public Transform getCalculatedTransformA(Transform out) {
		out.set(calculatedTransformA);
		return out;
	}

	/**
	 * Gets the global transform of the offset for body B.<p>
	 * See also: Generic6DofConstraint.getFrameOffsetA, Generic6DofConstraint.getFrameOffsetB, Generic6DofConstraint.calculateAngleInfo.
	 */
	public Transform getCalculatedTransformB(Transform out) {
		out.set(calculatedTransformB);
		return out;
	}

	public Transform getFrameOffsetA(Transform out) {
		out.set(frameInA);
		return out;
	}

	public Transform getFrameOffsetB(Transform out) {
		out.set(frameInB);
		return out;
	}
	
	public void setLinearLowerLimit(Vector3f linearLower) {
		linearLimits.lowerLimit.set(linearLower);
	}

	public void setLinearUpperLimit(Vector3f linearUpper) {
		linearLimits.upperLimit.set(linearUpper);
	}

	public void setAngularLowerLimit(Vector3f angularLower) {
		angularLimits[0].loLimit = angularLower.x;
		angularLimits[1].loLimit = angularLower.y;
		angularLimits[2].loLimit = angularLower.z;
	}

	public void setAngularUpperLimit(Vector3f angularUpper) {
		angularLimits[0].hiLimit = angularUpper.x;
		angularLimits[1].hiLimit = angularUpper.y;
		angularLimits[2].hiLimit = angularUpper.z;
	}

	/**
	 * Retrieves the angular limit informacion.
	 */
	public RotationalLimitMotor getRotationalLimitMotor(int index) {
		return angularLimits[index];
	}

	/**
	 * Retrieves the limit informacion.
	 */
	public TranslationalLimitMotor getTranslationalLimitMotor() {
		return linearLimits;
	}

	/**
	 * first 3 are linear, next 3 are angular
	 */
	public void setLimit(int axis, float lo, float hi) {
		if (axis < 3) {
			VectorUtil.setCoord(linearLimits.lowerLimit, axis, lo);
			VectorUtil.setCoord(linearLimits.upperLimit, axis, hi);
		}
		else {
			angularLimits[axis - 3].loLimit = lo;
			angularLimits[axis - 3].hiLimit = hi;
		}
	}
	
	/**
	 * Test limit.<p>
	 * - free means upper &lt; lower,<br>
	 * - locked means upper == lower<br>
	 * - limited means upper &gt; lower<br>
	 * - limitIndex: first 3 are linear, next 3 are angular
	 */
	public boolean isLimited(int limitIndex) {
		if (limitIndex < 3) {
			return linearLimits.isLimited(limitIndex);

		}
		return angularLimits[limitIndex - 3].isLimited();
	}
	
	// overridable
	public void calcAnchorPos() {
		float imA = rbA.getInvMass();
		float imB = rbB.getInvMass();
		float weight;
		if (imB == 0f) {
			weight = 1f;
		}
		else {
			weight = imA / (imA + imB);
		}
		Vector3f pA = calculatedTransformA.origin;
		Vector3f pB = calculatedTransformB.origin;

		Vector3f tmp1 = Stack.alloc(Vector3f.class);
		Vector3f tmp2 = Stack.alloc(Vector3f.class);

		tmp1.scale(weight, pA);
		tmp2.scale(1f - weight, pB);
		anchorPos.add(tmp1, tmp2);
	}
	
}
