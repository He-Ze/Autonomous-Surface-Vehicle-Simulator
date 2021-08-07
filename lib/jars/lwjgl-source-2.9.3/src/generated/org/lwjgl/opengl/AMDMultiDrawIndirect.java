/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class AMDMultiDrawIndirect {

	private AMDMultiDrawIndirect() {}

	public static void glMultiDrawArraysIndirectAMD(int mode, ByteBuffer indirect, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirectAMD;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 4 * 4 : stride) * primcount);
		nglMultiDrawArraysIndirectAMD(mode, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
	}
	static native void nglMultiDrawArraysIndirectAMD(int mode, long indirect, int primcount, int stride, long function_pointer);
	public static void glMultiDrawArraysIndirectAMD(int mode, long indirect_buffer_offset, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirectAMD;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOenabled(caps);
		nglMultiDrawArraysIndirectAMDBO(mode, indirect_buffer_offset, primcount, stride, function_pointer);
	}
	static native void nglMultiDrawArraysIndirectAMDBO(int mode, long indirect_buffer_offset, int primcount, int stride, long function_pointer);

	/** Overloads glMultiDrawArraysIndirectAMD. */
	public static void glMultiDrawArraysIndirectAMD(int mode, IntBuffer indirect, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirectAMD;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 4 : stride >> 2) * primcount);
		nglMultiDrawArraysIndirectAMD(mode, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
	}

	public static void glMultiDrawElementsIndirectAMD(int mode, int type, ByteBuffer indirect, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirectAMD;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 5 * 4 : stride) * primcount);
		nglMultiDrawElementsIndirectAMD(mode, type, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
	}
	static native void nglMultiDrawElementsIndirectAMD(int mode, int type, long indirect, int primcount, int stride, long function_pointer);
	public static void glMultiDrawElementsIndirectAMD(int mode, int type, long indirect_buffer_offset, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirectAMD;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOenabled(caps);
		nglMultiDrawElementsIndirectAMDBO(mode, type, indirect_buffer_offset, primcount, stride, function_pointer);
	}
	static native void nglMultiDrawElementsIndirectAMDBO(int mode, int type, long indirect_buffer_offset, int primcount, int stride, long function_pointer);

	/** Overloads glMultiDrawElementsIndirectAMD. */
	public static void glMultiDrawElementsIndirectAMD(int mode, int type, IntBuffer indirect, int primcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirectAMD;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 5 : stride >> 2) * primcount);
		nglMultiDrawElementsIndirectAMD(mode, type, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
	}
}
