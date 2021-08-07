/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class AMDSparseTexture {

	/**
	 * Accepted by the &lt;flags&gt; parameter to TexStorageSparseAMD and TextureStorageSparseAMD: 
	 */
	public static final int GL_TEXTURE_STORAGE_SPARSE_BIT_AMD = 0x1;

	/**
	 * Accepted by the &lt;pname&gt; parameter to GetInternalformativ: 
	 */
	public static final int GL_VIRTUAL_PAGE_SIZE_X_AMD = 0x9195,
		GL_VIRTUAL_PAGE_SIZE_Y_AMD = 0x9196,
		GL_VIRTUAL_PAGE_SIZE_Z_AMD = 0x9197;

	/**
	 *  Accepted by the &lt;pname&gt; parameter to GetIntegerv, GetFloatv, GetDoublev,
	 *  GetInteger64v, and GetBooleanv:
	 */
	public static final int GL_MAX_SPARSE_TEXTURE_SIZE_AMD = 0x9198,
		GL_MAX_SPARSE_3D_TEXTURE_SIZE_AMD = 0x9199,
		GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS = 0x919A;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexParameter{if}v: 
	 */
	public static final int GL_MIN_SPARSE_LEVEL_AMD = 0x919B;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of TexParameter{if}{v} and
	 *  GetTexParameter{if}v:
	 */
	public static final int GL_MIN_LOD_WARNING_AMD = 0x919C;

	private AMDSparseTexture() {}

	public static void glTexStorageSparseAMD(int target, int internalFormat, int width, int height, int depth, int layers, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexStorageSparseAMD;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexStorageSparseAMD(target, internalFormat, width, height, depth, layers, flags, function_pointer);
	}
	static native void nglTexStorageSparseAMD(int target, int internalFormat, int width, int height, int depth, int layers, int flags, long function_pointer);

	public static void glTextureStorageSparseAMD(int texture, int target, int internalFormat, int width, int height, int depth, int layers, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureStorageSparseAMD;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureStorageSparseAMD(texture, target, internalFormat, width, height, depth, layers, flags, function_pointer);
	}
	static native void nglTextureStorageSparseAMD(int texture, int target, int internalFormat, int width, int height, int depth, int layers, int flags, long function_pointer);
}
