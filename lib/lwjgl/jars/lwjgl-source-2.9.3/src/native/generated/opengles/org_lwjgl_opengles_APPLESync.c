/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GL_APICALL GLsync (GL_APIENTRY *glFenceSyncAPPLEPROC) (GLenum condition, GLbitfield flags);
typedef GL_APICALL GLboolean (GL_APIENTRY *glIsSyncAPPLEPROC) (GLsync sync);
typedef GL_APICALL void (GL_APIENTRY *glDeleteSyncAPPLEPROC) (GLsync sync);
typedef GL_APICALL GLenum (GL_APIENTRY *glClientWaitSyncAPPLEPROC) (GLsync sync, GLbitfield flags, GLuint64 timeout);
typedef GL_APICALL void (GL_APIENTRY *glWaitSyncAPPLEPROC) (GLsync sync, GLbitfield flags, GLuint64 timeout);
typedef GL_APICALL void (GL_APIENTRY *glGetInteger64vAPPLEPROC) (GLenum pname, GLint64 * params);
typedef GL_APICALL void (GL_APIENTRY *glGetSyncivAPPLEPROC) (GLsync sync, GLenum pname, GLsizei bufSize, GLsizei * length, GLint * values);

static glFenceSyncAPPLEPROC glFenceSyncAPPLE;
static glIsSyncAPPLEPROC glIsSyncAPPLE;
static glDeleteSyncAPPLEPROC glDeleteSyncAPPLE;
static glClientWaitSyncAPPLEPROC glClientWaitSyncAPPLE;
static glWaitSyncAPPLEPROC glWaitSyncAPPLE;
static glGetInteger64vAPPLEPROC glGetInteger64vAPPLE;
static glGetSyncivAPPLEPROC glGetSyncivAPPLE;

static jlong JNICALL Java_org_lwjgl_opengles_APPLESync_nglFenceSyncAPPLE(JNIEnv *env, jclass clazz, jint condition, jint flags) {
	GLsync __result = glFenceSyncAPPLE(condition, flags);
	return (intptr_t)__result;
}

static jboolean JNICALL Java_org_lwjgl_opengles_APPLESync_nglIsSyncAPPLE(JNIEnv *env, jclass clazz, jlong sync) {
	GLboolean __result = glIsSyncAPPLE((GLsync)(intptr_t)sync);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_APPLESync_nglDeleteSyncAPPLE(JNIEnv *env, jclass clazz, jlong sync) {
	glDeleteSyncAPPLE((GLsync)(intptr_t)sync);
}

static jint JNICALL Java_org_lwjgl_opengles_APPLESync_nglClientWaitSyncAPPLE(JNIEnv *env, jclass clazz, jlong sync, jint flags, jlong timeout) {
	GLenum __result = glClientWaitSyncAPPLE((GLsync)(intptr_t)sync, flags, timeout);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_APPLESync_nglWaitSyncAPPLE(JNIEnv *env, jclass clazz, jlong sync, jint flags, jlong timeout) {
	glWaitSyncAPPLE((GLsync)(intptr_t)sync, flags, timeout);
}

static void JNICALL Java_org_lwjgl_opengles_APPLESync_nglGetInteger64vAPPLE(JNIEnv *env, jclass clazz, jint pname, jlong params) {
	GLint64 *params_address = (GLint64 *)(intptr_t)params;
	glGetInteger64vAPPLE(pname, params_address);
}

static void JNICALL Java_org_lwjgl_opengles_APPLESync_nglGetSyncivAPPLE(JNIEnv *env, jclass clazz, jlong sync, jint pname, jint bufSize, jlong length, jlong values) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLint *values_address = (GLint *)(intptr_t)values;
	glGetSyncivAPPLE((GLsync)(intptr_t)sync, pname, bufSize, length_address, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengles_APPLESync_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nglFenceSyncAPPLE", "(II)J", (void *)&Java_org_lwjgl_opengles_APPLESync_nglFenceSyncAPPLE, "glFenceSyncAPPLE", (void *)&glFenceSyncAPPLE, false},
		{"nglIsSyncAPPLE", "(J)Z", (void *)&Java_org_lwjgl_opengles_APPLESync_nglIsSyncAPPLE, "glIsSyncAPPLE", (void *)&glIsSyncAPPLE, false},
		{"nglDeleteSyncAPPLE", "(J)V", (void *)&Java_org_lwjgl_opengles_APPLESync_nglDeleteSyncAPPLE, "glDeleteSyncAPPLE", (void *)&glDeleteSyncAPPLE, false},
		{"nglClientWaitSyncAPPLE", "(JIJ)I", (void *)&Java_org_lwjgl_opengles_APPLESync_nglClientWaitSyncAPPLE, "glClientWaitSyncAPPLE", (void *)&glClientWaitSyncAPPLE, false},
		{"nglWaitSyncAPPLE", "(JIJ)V", (void *)&Java_org_lwjgl_opengles_APPLESync_nglWaitSyncAPPLE, "glWaitSyncAPPLE", (void *)&glWaitSyncAPPLE, false},
		{"nglGetInteger64vAPPLE", "(IJ)V", (void *)&Java_org_lwjgl_opengles_APPLESync_nglGetInteger64vAPPLE, "glGetInteger64vAPPLE", (void *)&glGetInteger64vAPPLE, false},
		{"nglGetSyncivAPPLE", "(JIIJJ)V", (void *)&Java_org_lwjgl_opengles_APPLESync_nglGetSyncivAPPLE, "glGetSyncivAPPLE", (void *)&glGetSyncivAPPLE, false},

	};
	int num_functions = NUMFUNCTIONS(functions);
	extgl_InitializeClass(env, clazz, num_functions, functions);
}
