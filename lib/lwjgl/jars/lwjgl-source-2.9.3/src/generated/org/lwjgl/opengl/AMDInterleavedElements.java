/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class AMDInterleavedElements {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of VertexAttribParameteriAMD and
	 *  GetVertexAttrib{iv|dv|fv|Iiv|Iuiv|Ldv}:
	 */
	public static final int GL_VERTEX_ELEMENT_SWIZZLE_AMD = 0x91A4;

	/**
	 * Selected by the &lt;pname&gt; parameter of ProgramParameteri and GetProgramiv: 
	 */
	public static final int GL_VERTEX_ID_SWIZZLE_AMD = 0x91A5;

	private AMDInterleavedElements() {}

	public static void glVertexAttribParameteriAMD(int index, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribParameteriAMD;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribParameteriAMD(index, pname, param, function_pointer);
	}
	static native void nglVertexAttribParameteriAMD(int index, int pname, int param, long function_pointer);
}
