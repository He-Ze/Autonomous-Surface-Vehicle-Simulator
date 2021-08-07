/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GL_APICALL GLvoid * (GL_APIENTRY *glMapBufferRangeEXTPROC) (GLenum target, GLintptr offset, GLsizeiptr length, GLbitfield access);
typedef GL_APICALL void (GL_APIENTRY *glFlushMappedBufferRangeEXTPROC) (GLenum target, GLintptr offset, GLsizeiptr length);

static glMapBufferRangeEXTPROC glMapBufferRangeEXT;
static glFlushMappedBufferRangeEXTPROC glFlushMappedBufferRangeEXT;

static jobject JNICALL Java_org_lwjgl_opengles_EXTMapBufferRange_nglMapBufferRangeEXT(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong length, jint access, jobject old_buffer) {
	GLvoid * __result = glMapBufferRangeEXT(target, offset, length, access);
	return safeNewBufferCached(env, __result, length, old_buffer);
}

static void JNICALL Java_org_lwjgl_opengles_EXTMapBufferRange_nglFlushMappedBufferRangeEXT(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong length) {
	glFlushMappedBufferRangeEXT(target, offset, length);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengles_EXTMapBufferRange_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nglMapBufferRangeEXT", "(IJJIJLjava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;", (void *)&Java_org_lwjgl_opengles_EXTMapBufferRange_nglMapBufferRangeEXT, "glMapBufferRangeEXT", (void *)&glMapBufferRangeEXT, false},
		{"nglFlushMappedBufferRangeEXT", "(IJJ)V", (void *)&Java_org_lwjgl_opengles_EXTMapBufferRange_nglFlushMappedBufferRangeEXT, "glFlushMappedBufferRangeEXT", (void *)&glFlushMappedBufferRangeEXT, false}
	};
	int num_functions = NUMFUNCTIONS(functions);
	extgl_InitializeClass(env, clazz, num_functions, functions);
}
