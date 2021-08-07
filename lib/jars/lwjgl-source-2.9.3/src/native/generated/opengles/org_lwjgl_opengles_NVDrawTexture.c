/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GL_APICALL void (GL_APIENTRY *glDrawTextureNVPROC) (GLuint texture, GLuint sampler, GLfloat x0, GLfloat y0, GLfloat x1, GLfloat y1, GLfloat z, GLfloat s0, GLfloat t0, GLfloat s1, GLfloat t1);

static glDrawTextureNVPROC glDrawTextureNV;

static void JNICALL Java_org_lwjgl_opengles_NVDrawTexture_nglDrawTextureNV(JNIEnv *env, jclass clazz, jint texture, jint sampler, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jfloat z, jfloat s0, jfloat t0, jfloat s1, jfloat t1) {
	glDrawTextureNV(texture, sampler, x0, y0, x1, y1, z, s0, t0, s1, t1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengles_NVDrawTexture_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nglDrawTextureNV", "(IIFFFFFFFFF)V", (void *)&Java_org_lwjgl_opengles_NVDrawTexture_nglDrawTextureNV, "glDrawTextureNV", (void *)&glDrawTextureNV, false}
	};
	int num_functions = NUMFUNCTIONS(functions);
	extgl_InitializeClass(env, clazz, num_functions, functions);
}
