/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GL_APICALL void (GL_APIENTRY *glDebugMessageControlPROC) (GLenum source, GLenum type, GLenum severity, GLsizei count, const GLuint * ids, GLboolean enabled);
typedef GL_APICALL void (GL_APIENTRY *glDebugMessageInsertPROC) (GLenum source, GLenum type, GLuint id, GLenum severity, GLsizei length, const GLchar * buf);
typedef GL_APICALL void (GL_APIENTRY *glDebugMessageCallbackPROC) (GLDEBUGPROC callback, GLvoid * userParam);
typedef GL_APICALL GLuint (GL_APIENTRY *glGetDebugMessageLogPROC) (GLuint count, GLsizei bufsize, GLenum * sources, GLenum * types, GLuint * ids, GLenum * severities, GLsizei * lengths, GLchar * messageLog);
typedef GL_APICALL void (GL_APIENTRY *glPushDebugGroupPROC) (GLenum source, GLuint id, GLsizei length, const GLchar * message);
typedef GL_APICALL void (GL_APIENTRY *glPopDebugGroupPROC) ();
typedef GL_APICALL void (GL_APIENTRY *glObjectLabelPROC) (GLenum identifier, GLuint name, GLsizei length, const GLchar * label);
typedef GL_APICALL void (GL_APIENTRY *glGetObjectLabelPROC) (GLenum identifier, GLuint name, GLsizei bufSize, GLsizei * length, GLchar * label);
typedef GL_APICALL void (GL_APIENTRY *glObjectPtrLabelPROC) (GLvoid * ptr, GLsizei length, const GLchar * label);
typedef GL_APICALL void (GL_APIENTRY *glGetObjectPtrLabelPROC) (GLvoid * ptr, GLsizei bufSize, GLsizei * length, GLchar * label);

static glDebugMessageControlPROC glDebugMessageControl;
static glDebugMessageInsertPROC glDebugMessageInsert;
static glDebugMessageCallbackPROC glDebugMessageCallback;
static glGetDebugMessageLogPROC glGetDebugMessageLog;
static glPushDebugGroupPROC glPushDebugGroup;
static glPopDebugGroupPROC glPopDebugGroup;
static glObjectLabelPROC glObjectLabel;
static glGetObjectLabelPROC glGetObjectLabel;
static glObjectPtrLabelPROC glObjectPtrLabel;
static glGetObjectPtrLabelPROC glGetObjectPtrLabel;

static void JNICALL Java_org_lwjgl_opengles_KHRDebug_nglDebugMessageControl(JNIEnv *env, jclass clazz, jint source, jint type, jint severity, jint count, jlong ids, jboolean enabled) {
	const GLuint *ids_address = (const GLuint *)(intptr_t)ids;
	glDebugMessageControl(source, type, severity, count, ids_address, enabled);
}

static void JNICALL Java_org_lwjgl_opengles_KHRDebug_nglDebugMessageInsert(JNIEnv *env, jclass clazz, jint source, jint type, jint id, jint severity, jint length, jlong buf) {
	const GLchar *buf_address = (const GLchar *)(intptr_t)buf;
	glDebugMessageInsert(source, type, id, severity, length, buf_address);
}

static void JNICALL Java_org_lwjgl_opengles_KHRDebug_nglDebugMessageCallback(JNIEnv *env, jclass clazz, jlong callback, jlong userParam) {
	glDebugMessageCallback((GLDEBUGPROC)(intptr_t)callback, (GLvoid *)(intptr_t)userParam);
}

static jint JNICALL Java_org_lwjgl_opengles_KHRDebug_nglGetDebugMessageLog(JNIEnv *env, jclass clazz, jint count, jint bufsize, jlong sources, jlong types, jlong ids, jlong severities, jlong lengths, jlong messageLog) {
	GLenum *sources_address = (GLenum *)(intptr_t)sources;
	GLenum *types_address = (GLenum *)(intptr_t)types;
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	GLenum *severities_address = (GLenum *)(intptr_t)severities;
	GLsizei *lengths_address = (GLsizei *)(intptr_t)lengths;
	GLchar *messageLog_address = (GLchar *)(intptr_t)messageLog;
	GLuint __result = glGetDebugMessageLog(count, bufsize, sources_address, types_address, ids_address, severities_address, lengths_address, messageLog_address);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_KHRDebug_nglPushDebugGroup(JNIEnv *env, jclass clazz, jint source, jint id, jint length, jlong message) {
	const GLchar *message_address = (const GLchar *)(intptr_t)message;
	glPushDebugGroup(source, id, length, message_address);
}

static void JNICALL Java_org_lwjgl_opengles_KHRDebug_nglPopDebugGroup(JNIEnv *env, jclass clazz) {
	glPopDebugGroup();
}

static void JNICALL Java_org_lwjgl_opengles_KHRDebug_nglObjectLabel(JNIEnv *env, jclass clazz, jint identifier, jint name, jint length, jlong label) {
	const GLchar *label_address = (const GLchar *)(intptr_t)label;
	glObjectLabel(identifier, name, length, label_address);
}

static void JNICALL Java_org_lwjgl_opengles_KHRDebug_nglGetObjectLabel(JNIEnv *env, jclass clazz, jint identifier, jint name, jint bufSize, jlong length, jlong label) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *label_address = (GLchar *)(intptr_t)label;
	glGetObjectLabel(identifier, name, bufSize, length_address, label_address);
}

static void JNICALL Java_org_lwjgl_opengles_KHRDebug_nglObjectPtrLabel(JNIEnv *env, jclass clazz, jlong ptr, jint length, jlong label) {
	const GLchar *label_address = (const GLchar *)(intptr_t)label;
	glObjectPtrLabel((GLvoid *)(intptr_t)ptr, length, label_address);
}

static void JNICALL Java_org_lwjgl_opengles_KHRDebug_nglGetObjectPtrLabel(JNIEnv *env, jclass clazz, jlong ptr, jint bufSize, jlong length, jlong label) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *label_address = (GLchar *)(intptr_t)label;
	glGetObjectPtrLabel((GLvoid *)(intptr_t)ptr, bufSize, length_address, label_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengles_KHRDebug_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nglDebugMessageControl", "(IIIIJZ)V", (void *)&Java_org_lwjgl_opengles_KHRDebug_nglDebugMessageControl, "glDebugMessageControl", (void *)&glDebugMessageControl, false},
		{"nglDebugMessageInsert", "(IIIIIJ)V", (void *)&Java_org_lwjgl_opengles_KHRDebug_nglDebugMessageInsert, "glDebugMessageInsert", (void *)&glDebugMessageInsert, false},
		{"nglDebugMessageCallback", "(JJ)V", (void *)&Java_org_lwjgl_opengles_KHRDebug_nglDebugMessageCallback, "glDebugMessageCallback", (void *)&glDebugMessageCallback, false},
		{"nglGetDebugMessageLog", "(IIJJJJJJ)I", (void *)&Java_org_lwjgl_opengles_KHRDebug_nglGetDebugMessageLog, "glGetDebugMessageLog", (void *)&glGetDebugMessageLog, false},
		{"nglPushDebugGroup", "(IIIJ)V", (void *)&Java_org_lwjgl_opengles_KHRDebug_nglPushDebugGroup, "glPushDebugGroup", (void *)&glPushDebugGroup, false},
		{"nglPopDebugGroup", "()V", (void *)&Java_org_lwjgl_opengles_KHRDebug_nglPopDebugGroup, "glPopDebugGroup", (void *)&glPopDebugGroup, false},
		{"nglObjectLabel", "(IIIJ)V", (void *)&Java_org_lwjgl_opengles_KHRDebug_nglObjectLabel, "glObjectLabel", (void *)&glObjectLabel, false},
		{"nglGetObjectLabel", "(IIIJJ)V", (void *)&Java_org_lwjgl_opengles_KHRDebug_nglGetObjectLabel, "glGetObjectLabel", (void *)&glGetObjectLabel, false},
		{"nglObjectPtrLabel", "(JIJ)V", (void *)&Java_org_lwjgl_opengles_KHRDebug_nglObjectPtrLabel, "glObjectPtrLabel", (void *)&glObjectPtrLabel, false},
		{"nglGetObjectPtrLabel", "(JIJJ)V", (void *)&Java_org_lwjgl_opengles_KHRDebug_nglGetObjectPtrLabel, "glGetObjectPtrLabel", (void *)&glGetObjectPtrLabel, false},

	};
	int num_functions = NUMFUNCTIONS(functions);
	extgl_InitializeClass(env, clazz, num_functions, functions);
}
