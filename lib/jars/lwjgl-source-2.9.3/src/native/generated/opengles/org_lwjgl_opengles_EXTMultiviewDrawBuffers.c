/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GL_APICALL void (GL_APIENTRY *glReadBufferIndexedEXTPROC) (GLenum src, GLint index);
typedef GL_APICALL void (GL_APIENTRY *glDrawBuffersIndexedEXTPROC) (GLint n, const GLenum * location, const GLint * indices);
typedef GL_APICALL void (GL_APIENTRY *glGetIntegeri_vEXTPROC) (GLenum target, GLuint index, GLint * data);

static glReadBufferIndexedEXTPROC glReadBufferIndexedEXT;
static glDrawBuffersIndexedEXTPROC glDrawBuffersIndexedEXT;
static glGetIntegeri_vEXTPROC glGetIntegeri_vEXT;

static void JNICALL Java_org_lwjgl_opengles_EXTMultiviewDrawBuffers_nglReadBufferIndexedEXT(JNIEnv *env, jclass clazz, jint src, jint index) {
	glReadBufferIndexedEXT(src, index);
}

static void JNICALL Java_org_lwjgl_opengles_EXTMultiviewDrawBuffers_nglDrawBuffersIndexedEXT(JNIEnv *env, jclass clazz, jint n, jlong location, jlong indices) {
	const GLenum *location_address = (const GLenum *)(intptr_t)location;
	const GLint *indices_address = (const GLint *)(intptr_t)indices;
	glDrawBuffersIndexedEXT(n, location_address, indices_address);
}

static void JNICALL Java_org_lwjgl_opengles_EXTMultiviewDrawBuffers_nglGetIntegeri_1vEXT(JNIEnv *env, jclass clazz, jint target, jint index, jlong data) {
	GLint *data_address = (GLint *)(intptr_t)data;
	glGetIntegeri_vEXT(target, index, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengles_EXTMultiviewDrawBuffers_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nglReadBufferIndexedEXT", "(II)V", (void *)&Java_org_lwjgl_opengles_EXTMultiviewDrawBuffers_nglReadBufferIndexedEXT, "glReadBufferIndexedEXT", (void *)&glReadBufferIndexedEXT, false},
		{"nglDrawBuffersIndexedEXT", "(IJJ)V", (void *)&Java_org_lwjgl_opengles_EXTMultiviewDrawBuffers_nglDrawBuffersIndexedEXT, "glDrawBuffersIndexedEXT", (void *)&glDrawBuffersIndexedEXT, false},
		{"nglGetIntegeri_vEXT", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_EXTMultiviewDrawBuffers_nglGetIntegeri_1vEXT, "glGetIntegeri_vEXT", (void *)&glGetIntegeri_vEXT, false},

	};
	int num_functions = NUMFUNCTIONS(functions);
	extgl_InitializeClass(env, clazz, num_functions, functions);
}
