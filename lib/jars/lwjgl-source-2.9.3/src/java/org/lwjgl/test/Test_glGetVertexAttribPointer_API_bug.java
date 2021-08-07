package org.lwjgl.test;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.PrivilegedActionException;

/**
 * glGetVertexAttribPointer() is coded incorrectly. Any use of the buffer
 * returned by glGetVertexAttribPointer() results in a JVM crash (SIGSEGV),
 * because LWJGL is returning an offset as a DirectByteBuffer whose address is
 * typically in the first page of memory. glGetVertexAttribPointer() should
 * return a long, not a ByteBuffer.
 * <p/>
 * <p/>
 * The spec for <a href=
 * "http://www.opengl.org/sdk/docs/man3/xhtml/glVertexAttribPointer.xml"
 * >glVertexAttribPointer</a> says:
 * <p/>
 * pointer
 * <ul>
 * <li>
 * Specifies a <b>offset of the first component</b> of the first generic vertex
 * attribute in the array in the data store of the buffer currently bound to the
 * GL_ARRAY_BUFFER target. The initial value is 0.</li>
 * </ul>
 * Emphasis mine. This is an offset into the VBO, not a native pointer.
 * <p/>
 * <p/>
 * Now look at <a href=
 * "http://www.opengl.org/sdk/docs/man3/xhtml/glGetVertexAttribPointerv.xml"
 * >glGetVertexAttribPointerv</a>, which says:
 * <ul>
 * <li>The pointer returned is a <b>byte offset into the data store</b> of the
 * buffer object that was bound to the GL_ARRAY_BUFFER target (see glBindBuffer)
 * when the desired pointer was previously specified.</li>
 * </ul>
 * Emphasis mine again. The spec clearly intends this to be an offset, not a
 * pointer.
 */
public class Test_glGetVertexAttribPointer_API_bug {

	static boolean crashJvm = "true".equals(System.getProperty("crashJvm",
	                                                           "false"));

	static final int FLOAT_BYTES = 4;

	static class Attr {

		final String name;
		final int    index;
		final int    size;
		final int    offset;

		Attr(String name, int index, int size,
		     int offset) {
			this.name = name;
			this.index = index;
			this.size = size;
			this.offset = offset;
		}

		long getByteOffset() {
			return offset * FLOAT_BYTES;
		}
	}

	static class AttrGen {

		int offset = 0;

		Attr allocAttr(String name, int index, int size) {
			Attr result = new Attr(name, index, size, offset);
			offset += size;
			return result;
		}
	}

	static       AttrGen gen                = new AttrGen();
	static final Attr    position           = gen.allocAttr("position", 1, 3);
	static final Attr    normal             = gen.allocAttr("normal", 2, 3);
	static final Attr    textureCoordinates = gen.allocAttr("textureCoordinates",
	                                                        3, 2);
	static final int     stride             = gen.offset;
	static final int     strideBytes        = stride * FLOAT_BYTES;

	static final Attr[] attributes = { position, normal, textureCoordinates };

	/** Hack to call BufferUtils.getBufferAddress() */
	static long getBufferAddress(ByteBuffer buf)
		throws SecurityException, NoSuchMethodException,
		       IllegalArgumentException, IllegalAccessException,
		       InvocationTargetException {
		Method m = BufferUtils.class.getDeclaredMethod(
			"getBufferAddress", Buffer.class);
		if ( !m.isAccessible() )
			m.setAccessible(true);
		return (Long)m.invoke(null, buf);
	}

	static long glGetVertexAttribPointer(int index)
		throws NoSuchMethodException, IllegalAccessException,
		       InvocationTargetException {
		int result_size = PointerBuffer.getPointerSize();
		//ByteBuffer buf = GL20.glGetVertexAttribPointer(index, GL20.GL_VERTEX_ATTRIB_ARRAY_POINTER, result_size);

		ByteBuffer buf = BufferUtils.createByteBuffer(PointerBuffer.getPointerSize());

		GL20.glGetVertexAttribPointer(index, GL20.GL_VERTEX_ATTRIB_ARRAY_POINTER, buf);

		/*if ( buf == null )
			return 0;

		long bufferAddress = getBufferAddress(buf);
		if ( crashJvm ) {
		*//*
	     * This crashes the JVM with a SIGSEGV because it tries to
	     * dereference something that was never intended to be a pointer.
	     * It's an offset (e.g. 12).
	     *//*
			System.out
				.println("Dereferencing "
				         + bufferAddress
				         + " (this will probably crash the JVM with a SIGSEGV) ... ");
			buf.get(0);
		}
*/

		long bufferAddress = buf.getLong(0);
		System.out.println(index + " bufferAddress = " + bufferAddress);

		return bufferAddress;
	}

	public static void main(String[] args) throws LWJGLException,
	                                              SecurityException, NoSuchMethodException, IllegalArgumentException,
	                                              IllegalAccessException, InvocationTargetException,
	                                              PrivilegedActionException {
		Display.create();

	/*
	 * Note: it's not necessary to call glBufferData(); the functions
	 * involved are only concerned with data layout.
	 */

		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

		for ( Attr attr : attributes ) {
			GL20.glEnableVertexAttribArray(attr.index);
			long posPointer = glGetVertexAttribPointer(attr.index);
			if ( posPointer != 0 )
				throw new AssertionError();
			GL20.glVertexAttribPointer(attr.index, attr.size, GL11.GL_FLOAT,
			                           false, strideBytes, attr.getByteOffset());
		}

		for ( Attr attr : attributes ) {
			long posPointer = glGetVertexAttribPointer(attr.index);
	    /* this proves that value returned is the offset */
			if ( posPointer != attr.getByteOffset() )
				throw new AssertionError("this will never happen");
			System.out.println(attr.name + ": offset = " + posPointer);
		}

		if ( !crashJvm ) {
			System.out
				.println("Test passed, but try running again with -DcrashJvm=true.");
		} else {
			System.out
				.println("You won't ever get this far with LWJGL 2.8.5 or 2.9.0");
		}
	}
}
