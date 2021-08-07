/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GL_APICALL void (GL_APIENTRY *glCopyTextureLevelsAPPLEPROC) (GLuint destinationTexture, GLuint sourceTexture, GLint sourceBaseLevel, GLsizei sourceLevelCount);

static glCopyTextureLevelsAPPLEPROC glCopyTextureLevelsAPPLE;

static void JNICALL Java_org_lwjgl_opengles_EXTCopyTextureLevels_nglCopyTextureLevelsAPPLE(JNIEnv *env, jclass clazz, jint destinationTexture, jint sourceTexture, jint sourceBaseLevel, jint sourceLevelCount) {
	glCopyTextureLevelsAPPLE(destinationTexture, sourceTexture, sourceBaseLevel, sourceLevelCount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengles_EXTCopyTextureLevels_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nglCopyTextureLevelsAPPLE", "(IIII)V", (void *)&Java_org_lwjgl_opengles_EXTCopyTextureLevels_nglCopyTextureLevelsAPPLE, "glCopyTextureLevelsAPPLE", (void *)&glCopyTextureLevelsAPPLE, false}
	};
	int num_functions = NUMFUNCTIONS(functions);
	extgl_InitializeClass(env, clazz, num_functions, functions);
}
