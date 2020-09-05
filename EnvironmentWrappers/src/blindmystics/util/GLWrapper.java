package blindmystics.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class GLWrapper {
    //Handy regex GL[0-9]{2}

    //GL11
    public static int GL_COLOR_BUFFER_BIT = GL11.GL_COLOR_BUFFER_BIT;
    public static int GL_DEPTH_BUFFER_BIT = GL11.GL_DEPTH_BUFFER_BIT;
    public static int GL_NO_ERROR = GL11.GL_NO_ERROR;
    public static int GL_MODELVIEW = GL11.GL_MODELVIEW;
    public static int GL_PROJECTION = GL11.GL_PROJECTION;
    public static int GL_VERSION = GL11.GL_VERSION;
    public static int GL_BLEND = GL11.GL_BLEND;
    public static int GL_SRC_ALPHA = GL11.GL_SRC_ALPHA;
    public static int GL_ONE_MINUS_SRC_ALPHA = GL11.GL_ONE_MINUS_SRC_ALPHA;
    public static int GL_CULL_FACE = GL11.GL_CULL_FACE;
    public static int GL_BACK = GL11.GL_BACK;
    public static int GL_FLOAT = GL11.GL_FLOAT;
    public static int GL_TRIANGLES = GL11.GL_TRIANGLES;
    public static int GL_UNSIGNED_INT = GL11.GL_UNSIGNED_INT;
    public static int GL_UNSIGNED_SHORT = GL11.GL_UNSIGNED_SHORT;
    public static int GL_FALSE = GL11.GL_FALSE;
    public static int GL_TEXTURE_2D = GL11.GL_TEXTURE_2D;
    public static int GL_LINEAR = GL11.GL_LINEAR;
    public static int GL_LINEAR_MIPMAP_LINEAR = GL11.GL_LINEAR_MIPMAP_LINEAR;
    public static int GL_REPEAT = GL11.GL_REPEAT;
    public static int GL_TRIANGLE_STRIP = GL11.GL_TRIANGLE_STRIP;
    public static int GL_QUADS = GL11.GL_QUADS;
    public static int GL_UNSIGNED_BYTE = GL11.GL_UNSIGNED_BYTE;
    public static int GL_TEXTURE_WRAP_S = GL11.GL_TEXTURE_WRAP_S;
    public static int GL_TEXTURE_WRAP_T = GL11.GL_TEXTURE_WRAP_T;
    public static int GL_RGBA = GL11.GL_RGBA;
    public static int GL_CLAMP = GL11.GL_CLAMP;
    public static int GL_TEXTURE_MAG_FILTER = GL11.GL_TEXTURE_MAG_FILTER;
    public static int GL_TEXTURE_MIN_FILTER = GL11.GL_TEXTURE_MIN_FILTER;
    public static int GL_UNPACK_ALIGNMENT = GL11.GL_UNPACK_ALIGNMENT;
    public static int GL_DEPTH_TEST = GL11.GL_DEPTH_TEST;
    public static int GL_VERTEX_SHADER = GL20.GL_VERTEX_SHADER;
    public static int GL_FRAGMENT_SHADER = GL20.GL_FRAGMENT_SHADER;
    public static int GL_CCW = GL11.GL_CCW;
    public static int GL_DEPTH_COMPONENT = GL11.GL_DEPTH_COMPONENT;
    public static int GL_FRAMEBUFFER = GL30.GL_FRAMEBUFFER;
    public static int GL_DEPTH_ATTACHMENT = GL30.GL_DEPTH_ATTACHMENT;
    public static int GL_COLOR_ATTACHMENT0 = GL30.GL_COLOR_ATTACHMENT0;
    public static int GL_RENDERBUFFER = GL30.GL_RENDERBUFFER;

//    public static int MAX_INDEX_TYPE = GL_UNSIGNED_INT;
//    public static int MAX_INDEX_TYPE_BYTES = 4;
    public static int MAX_INDEX_TYPE = GL_UNSIGNED_INT;
    public static int MAX_INDEX_TYPE_BYTES = 4;

    public static void glMatrixMode(int mode) {
        GL11.glMatrixMode(mode);
    }

    public static void glLoadIdentity() {
        GL11.glLoadIdentity();
    }

    public static void glViewport(int x, int y, int width, int height) {
        GL11.glViewport(x, y, width, height);
    }

    public static void glEnable(int cap) {
        GL11.glEnable(cap);
    }

    public static void glDisable(int cap) {
        GL11.glDisable(cap);
    }

    public static void glFrontFace(int direction) {
        GL11.glFrontFace(direction);
    }

    public static void glBlendFunc(int sfactor, int dfactor) {
        GL11.glBlendFunc(sfactor, dfactor);
    }

    public static void glCullFace(int mode) {
        GL11.glCullFace(mode);
    }

    public static int glGetError() {
        return GL11.glGetError();
    }

    public static String gluErrorString(int errorValue) {
        return GLU.gluErrorString(errorValue);
    }

    public static void glClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    public static void glClear(int clearId) {
        GL11.glClear(clearId);
    }

    public static String glGetString(int name) {
        return GL11.glGetString(name);
    }


    public static void glDrawArrays(int mode, int first, int count) {
        GL11.glDrawArrays(mode, first, count);
    }

    public static void glDrawElements(int mode, int indices_count, int type, long indices_buffer_offset) {
        GL11.glDrawElements(mode, indices_count, type, indices_buffer_offset);
    }

    public static void glDeleteTextures(int texture) {
        GL11.glDeleteTextures(texture);
    }

    public static void glBindTexture(int target, int texture) {
        GL11.glBindTexture(target, texture);
    }

    //GL13
    public static int GL_TEXTURE0 = GL13.GL_TEXTURE0;

    //GL15
    public static int GL_ELEMENT_ARRAY_BUFFER = GL15.GL_ELEMENT_ARRAY_BUFFER;
    public static int GL_STATIC_DRAW = GL15.GL_STATIC_DRAW;
    public static int GL_ARRAY_BUFFER = GL15.GL_ARRAY_BUFFER;
    public static int GL_STREAM_DRAW = GL15.GL_STREAM_DRAW;

    public static int glGenBuffers() {
        return GL15.glGenBuffers();
    }

    public static void glBindBuffer(int target, int buffer) {
        GL15.glBindBuffer(target, buffer);
    }

    public static void glBufferData(int target, IntBuffer data, int usage) {
        GL15.glBufferData(target, data, usage);
    }

    public static void glBufferData(int target, FloatBuffer data, int usage) {
        GL15.glBufferData(target, data, usage);
    }

    public static void glBufferData(int target, ShortBuffer data, int usage) {
        GL15.glBufferData(target, data, usage);
    }

    public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset) {
        GL20.glVertexAttribPointer(index, size, type, normalized, stride, buffer_buffer_offset);
    }

    public static void glDeleteBuffers(int buffer) {
        GL15.glDeleteBuffers(buffer);
    }

    public static void glBufferData(int target, ByteBuffer data, int usage) {
        GL15.glBufferData(target, data, usage);
    }

    //GL20
    public static int GL_COMPILE_STATUS = GL20.GL_COMPILE_STATUS;
    public static int GL_LINK_STATUS = GL20.GL_LINK_STATUS;

    public static void glUseProgram(int program) {
        GL20.glUseProgram(program);
    }

    public static void glDeleteProgram(int program) {
        GL20.glDeleteProgram(program);
    }

    public static void glEnableVertexAttribArray(int index) {
        GL20.glEnableVertexAttribArray(index);
    }

    public static void glDisableVertexAttribArray(int index) {
        GL20.glDisableVertexAttribArray(index);
    }

    public static void glAttachShader(int program, int shader) {
        GL20.glAttachShader(program, shader);
    }

    public static void glLinkProgram(int program) {
        GL20.glLinkProgram(program);
    }

    public static void glValidateProgram(int program) {
        GL20.glValidateProgram(program);
    }

    public static int glCreateShader(int type) {
        return GL20.glCreateShader(type);
    }

    public static void glShaderSource(int shader, CharSequence string) {
        GL20.glShaderSource(shader, string);
    }

    public static void glCompileShader(int shader) {
        GL20.glCompileShader(shader);
    }

    public static int glGetShaderi(int shader, int pname) {
        return GL20.glGetShaderi(shader, pname);
    }

    public static int glGetUniformLocation(int program, CharSequence name) {
        return GL20.glGetUniformLocation(program, name);
    }

    public static void glUniform1i(int location, int v0) {
        GL20.glUniform1i(location, v0);
    }

    public static void glUniform1f(int location, float v0) {
        GL20.glUniform1f(location, v0);
    }

    public static void glUniform1(int location, float[] v0) {
        FloatBuffer floatBuff = BufferUtils.createFloatBuffer(v0.length);
        floatBuff.put(v0);
        floatBuff.flip();
        GL20.glUniform1(location, floatBuff);
    }

    public static void glUniform2f(int location, float v0, float v1) {
        GL20.glUniform2f(location, v0, v1);
    }

    public static void glUniform3f(int location, float v0, float v1, float v2) {
        GL20.glUniform3f(location, v0, v1, v2);
    }

    public static void glUniform4f(int location, float v0, float v1, float v2, float v3) {
        GL20.glUniform4f(location, v0, v1, v2, v3);
    }

    public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
        GL20.glUniformMatrix4(location, transpose, matrices);
    }

    public static void glUniform1(int location, FloatBuffer values) {
        GL20.glUniform1(location, values);
    }

    public static void glBindAttribLocation(int program, int index, CharSequence name) {
        GL20.glBindAttribLocation(program, index, name);
    }

    //GL30
    public static int glGenVertexArrays() {
        return GL30.glGenVertexArrays();
    }

    public static void glBindVertexArray(int array) {
        GL30.glBindVertexArray(array);
    }

    public static void glDeleteVertexArrays(int array) {
        GL30.glDeleteVertexArrays(array);
    }

    public static int glCreateProgram() {
        return GL20.glCreateProgram();
    }

    public static void glActiveTexture(int texture) {
        GL13.glActiveTexture(texture);
    }

    public static void glPixelStorei(int pname, int param) {
        GL11.glPixelStorei(pname, param);
    }

    public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
        GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
    }

    public static void glGenerateMipmap(int target) {
        GL30.glGenerateMipmap(target);
    }

    public static void glTexParameteri(int target, int pname, int param) {
        GL11.glTexParameteri(target, pname, param);
    }

    public static int glGenTextures() {
        return GL11.glGenTextures();
    }

    public static int glGetAttribLocation(int program, String name) {
        return GL20.glGetAttribLocation(program, name);
    }

    public static boolean glIsProgram(int program) {
        return GL20.glIsProgram(program);
    }

    public static int glGetProgrami(int programId, int pname) {
        return GL20.glGetProgrami(programId, pname);
    }

    public static void glBindFramebuffer(int target, int framebuffer) {
        GL30.glBindFramebuffer(target, framebuffer);
    }

    public static int glGenFramebuffers() {
        return GL30.glGenFramebuffers();
    }

    public static void glDrawBuffer(int mode) {
        GL11.glDrawBuffer(mode);
    }

    public static int glGenRenderbuffers() {
        return GL30.glGenRenderbuffers();
    }

    public static void glBindRenderbuffer(int target, int renderbuffer) {
        GL30.glBindRenderbuffer(target, renderbuffer);
    }

    public static void glRenderbufferStorage(int target, int internalformat, int width, int height) {
        GL30.glRenderbufferStorage(target, internalformat, width, height);
    }

    public static void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
        GL30.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
    }

    public static void glDeleteFramebuffers(int framebuffer) {
        GL30.glDeleteFramebuffers(framebuffer);
    }

    public static void glDeleteRenderbuffers(int renderbuffer) {
        GL30.glDeleteRenderbuffers(renderbuffer);
    }

    public static void glFramebufferTexture(int target, int attachment, int texture, int level) {
        GL32.glFramebufferTexture(target, attachment, texture, level);
    }
}