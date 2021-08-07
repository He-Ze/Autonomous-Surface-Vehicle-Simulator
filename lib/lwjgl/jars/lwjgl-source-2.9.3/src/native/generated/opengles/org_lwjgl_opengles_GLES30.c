/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GL_APICALL void (GL_APIENTRY *glReadBufferPROC) (GLenum mode);
typedef GL_APICALL void (GL_APIENTRY *glDrawRangeElementsPROC) (GLenum mode, GLuint start, GLuint end, GLsizei count, GLenum type, const GLvoid * indices);
typedef GL_APICALL void (GL_APIENTRY *glTexImage3DPROC) (GLenum target, GLint level, GLint internalFormat, GLsizei width, GLsizei height, GLsizei depth, GLint border, GLenum format, GLenum type, const GLvoid * pixels);
typedef GL_APICALL void (GL_APIENTRY *glTexSubImage3DPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLenum type, const GLvoid * pixels);
typedef GL_APICALL void (GL_APIENTRY *glCopyTexSubImage3DPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLint x, GLint y, GLsizei width, GLsizei height);
typedef GL_APICALL void (GL_APIENTRY *glCompressedTexImage3DPROC) (GLenum target, GLint level, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth, GLint border, GLsizei imageSize, const GLvoid * data);
typedef GL_APICALL void (GL_APIENTRY *glCompressedTexSubImage3DPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef GL_APICALL void (GL_APIENTRY *glGenQueriesPROC) (GLsizei n, GLuint * ids);
typedef GL_APICALL void (GL_APIENTRY *glDeleteQueriesPROC) (GLsizei n, GLuint * ids);
typedef GL_APICALL GLboolean (GL_APIENTRY *glIsQueryPROC) (GLuint id);
typedef GL_APICALL void (GL_APIENTRY *glBeginQueryPROC) (GLenum target, GLuint id);
typedef GL_APICALL void (GL_APIENTRY *glEndQueryPROC) (GLenum target);
typedef GL_APICALL void (GL_APIENTRY *glGetQueryivPROC) (GLenum target, GLenum pname, GLint * params);
typedef GL_APICALL void (GL_APIENTRY *glGetQueryObjectuivPROC) (GLenum id, GLenum pname, GLuint * params);
typedef GL_APICALL GLboolean (GL_APIENTRY *glUnmapBufferPROC) (GLenum target);
typedef GL_APICALL void (GL_APIENTRY *glGetBufferPointervPROC) (GLenum target, GLenum pname, GLvoid ** pointer);
typedef GL_APICALL void (GL_APIENTRY *glDrawBuffersPROC) (GLsizei size, const GLenum * buffers);
typedef GL_APICALL void (GL_APIENTRY *glUniformMatrix2x3fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef GL_APICALL void (GL_APIENTRY *glUniformMatrix3x2fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef GL_APICALL void (GL_APIENTRY *glUniformMatrix2x4fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef GL_APICALL void (GL_APIENTRY *glUniformMatrix4x2fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef GL_APICALL void (GL_APIENTRY *glUniformMatrix3x4fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef GL_APICALL void (GL_APIENTRY *glUniformMatrix4x3fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef GL_APICALL void (GL_APIENTRY *glBlitFramebufferPROC) (GLint srcX0, GLint srcY0, GLint srcX1, GLint srcY1, GLint dstX0, GLint dstY0, GLint dstX1, GLint dstY1, GLbitfield mask, GLenum filter);
typedef GL_APICALL void (GL_APIENTRY *glRenderbufferStorageMultisamplePROC) (GLenum target, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height);
typedef GL_APICALL void (GL_APIENTRY *glFramebufferTextureLayerPROC) (GLenum target, GLenum attachment, GLuint texture, GLint level, GLint layer);
typedef GL_APICALL GLvoid * (GL_APIENTRY *glMapBufferRangePROC) (GLenum target, GLintptr offset, GLsizeiptr length, GLbitfield access);
typedef GL_APICALL void (GL_APIENTRY *glFlushMappedBufferRangePROC) (GLenum target, GLintptr offset, GLsizeiptr length);
typedef GL_APICALL void (GL_APIENTRY *glBindVertexArrayPROC) (GLuint array);
typedef GL_APICALL void (GL_APIENTRY *glDeleteVertexArraysPROC) (GLsizei n, const GLuint * arrays);
typedef GL_APICALL void (GL_APIENTRY *glGenVertexArraysPROC) (GLsizei n, GLuint * arrays);
typedef GL_APICALL GLboolean (GL_APIENTRY *glIsVertexArrayPROC) (GLuint array);
typedef GL_APICALL void (GL_APIENTRY *glGetIntegeri_vPROC) (GLenum value, GLuint index, GLint * data);
typedef GL_APICALL void (GL_APIENTRY *glBeginTransformFeedbackPROC) (GLenum primitiveMode);
typedef GL_APICALL void (GL_APIENTRY *glEndTransformFeedbackPROC) ();
typedef GL_APICALL void (GL_APIENTRY *glBindBufferRangePROC) (GLenum target, GLuint index, GLuint buffer, GLintptr offset, GLsizeiptr size);
typedef GL_APICALL void (GL_APIENTRY *glBindBufferBasePROC) (GLenum target, GLuint index, GLuint buffer);
typedef GL_APICALL void (GL_APIENTRY *glTransformFeedbackVaryingsPROC) (GLuint program, GLsizei count, const GLchar ** varyings, GLenum bufferMode);
typedef GL_APICALL void (GL_APIENTRY *glGetTransformFeedbackVaryingPROC) (GLuint program, GLuint index, GLsizei bufSize, GLsizei * length, GLsizei * size, GLenum * type, GLchar * name);
typedef GL_APICALL void (GL_APIENTRY *glVertexAttribIPointerPROC) (GLuint index, GLint size, GLenum type, GLsizei stride, const GLvoid * buffer);
typedef GL_APICALL void (GL_APIENTRY *glGetVertexAttribIivPROC) (GLuint index, GLenum pname, GLint * params);
typedef GL_APICALL void (GL_APIENTRY *glGetVertexAttribIuivPROC) (GLuint index, GLenum pname, GLuint * params);
typedef GL_APICALL void (GL_APIENTRY *glVertexAttribI4iPROC) (GLuint index, GLint x, GLint y, GLint z, GLint w);
typedef GL_APICALL void (GL_APIENTRY *glVertexAttribI4uiPROC) (GLuint index, GLuint x, GLuint y, GLuint z, GLuint w);
typedef GL_APICALL void (GL_APIENTRY *glVertexAttribI4ivPROC) (GLuint index, const GLint * v);
typedef GL_APICALL void (GL_APIENTRY *glVertexAttribI4uivPROC) (GLuint index, const GLuint * v);
typedef GL_APICALL void (GL_APIENTRY *glGetUniformuivPROC) (GLuint program, GLint location, GLuint * params);
typedef GL_APICALL GLint (GL_APIENTRY *glGetFragDataLocationPROC) (GLuint program, const GLchar * name);
typedef GL_APICALL void (GL_APIENTRY *glUniform1uiPROC) (GLint location, GLuint v0);
typedef GL_APICALL void (GL_APIENTRY *glUniform2uiPROC) (GLint location, GLuint v0, GLuint v1);
typedef GL_APICALL void (GL_APIENTRY *glUniform3uiPROC) (GLint location, GLuint v0, GLuint v1, GLuint v2);
typedef GL_APICALL void (GL_APIENTRY *glUniform4uiPROC) (GLint location, GLuint v0, GLuint v1, GLuint v2, GLuint v3);
typedef GL_APICALL void (GL_APIENTRY *glUniform1uivPROC) (GLint location, GLsizei count, const GLuint * value);
typedef GL_APICALL void (GL_APIENTRY *glUniform2uivPROC) (GLint location, GLsizei count, const GLuint * value);
typedef GL_APICALL void (GL_APIENTRY *glUniform3uivPROC) (GLint location, GLsizei count, const GLuint * value);
typedef GL_APICALL void (GL_APIENTRY *glUniform4uivPROC) (GLint location, GLsizei count, const GLuint * value);
typedef GL_APICALL void (GL_APIENTRY *glClearBufferfvPROC) (GLenum buffer, GLint drawbuffer, const GLfloat * value);
typedef GL_APICALL void (GL_APIENTRY *glClearBufferivPROC) (GLenum buffer, GLint drawbuffer, const GLint * value);
typedef GL_APICALL void (GL_APIENTRY *glClearBufferuivPROC) (GLenum buffer, GLint drawbuffer, const GLint * value);
typedef GL_APICALL void (GL_APIENTRY *glClearBufferfiPROC) (GLenum buffer, GLint drawbuffer, GLfloat depth, GLint stencil);
typedef GL_APICALL GLubyte * (GL_APIENTRY *glGetStringiPROC) (GLenum name, GLuint index);
typedef GL_APICALL void (GL_APIENTRY *glCopyBufferSubDataPROC) (GLenum readtarget, GLenum writetarget, GLintptr readoffset, GLintptr writeoffset, GLsizeiptr size);
typedef GL_APICALL void (GL_APIENTRY *glGetUniformIndicesPROC) (GLuint program, GLsizei uniformCount, const GLchar ** uniformNames, GLuint * uniformIndices);
typedef GL_APICALL void (GL_APIENTRY *glGetActiveUniformsivPROC) (GLuint program, GLsizei uniformCount, const GLuint * uniformIndices, GLenum pname, GLint * params);
typedef GL_APICALL GLuint (GL_APIENTRY *glGetUniformBlockIndexPROC) (GLuint program, const GLchar * uniformBlockName);
typedef GL_APICALL void (GL_APIENTRY *glGetActiveUniformBlockivPROC) (GLuint program, GLuint uniformBlockIndex, GLenum pname, GLint * params);
typedef GL_APICALL void (GL_APIENTRY *glGetActiveUniformBlockNamePROC) (GLuint program, GLuint uniformBlockIndex, GLsizei bufSize, GLsizei * length, GLchar * uniformBlockName);
typedef GL_APICALL void (GL_APIENTRY *glUniformBlockBindingPROC) (GLuint program, GLuint uniformBlockIndex, GLuint uniformBlockBinding);
typedef GL_APICALL void (GL_APIENTRY *glDrawArraysInstancedPROC) (GLenum mode, GLint first, GLsizei count, GLsizei primcount);
typedef GL_APICALL void (GL_APIENTRY *glDrawElementsInstancedPROC) (GLenum mode, GLsizei count, GLenum type, const GLvoid * indices, GLsizei primcount);
typedef GL_APICALL GLsync (GL_APIENTRY *glFenceSyncPROC) (GLenum condition, GLbitfield flags);
typedef GL_APICALL GLboolean (GL_APIENTRY *glIsSyncPROC) (GLsync sync);
typedef GL_APICALL void (GL_APIENTRY *glDeleteSyncPROC) (GLsync sync);
typedef GL_APICALL GLenum (GL_APIENTRY *glClientWaitSyncPROC) (GLsync sync, GLbitfield flags, GLuint64 timeout);
typedef GL_APICALL void (GL_APIENTRY *glWaitSyncPROC) (GLsync sync, GLbitfield flags, GLuint64 timeout);
typedef GL_APICALL void (GL_APIENTRY *glGetInteger64vPROC) (GLenum pname, GLint64 * data);
typedef GL_APICALL void (GL_APIENTRY *glGetInteger64i_vPROC) (GLenum value, GLuint index, GLint64 * data);
typedef GL_APICALL void (GL_APIENTRY *glGetSyncivPROC) (GLsync sync, GLenum pname, GLsizei bufSize, GLsizei * length, GLint * values);
typedef GL_APICALL void (GL_APIENTRY *glGetBufferParameteri64vPROC) (GLenum target, GLenum pname, GLint64 * params);
typedef GL_APICALL void (GL_APIENTRY *glGenSamplersPROC) (GLsizei count, GLuint * samplers);
typedef GL_APICALL void (GL_APIENTRY *glDeleteSamplersPROC) (GLsizei count, const GLuint * samplers);
typedef GL_APICALL GLboolean (GL_APIENTRY *glIsSamplerPROC) (GLuint sampler);
typedef GL_APICALL void (GL_APIENTRY *glBindSamplerPROC) (GLenum unit, GLuint sampler);
typedef GL_APICALL void (GL_APIENTRY *glSamplerParameteriPROC) (GLuint sampler, GLenum pname, GLint param);
typedef GL_APICALL void (GL_APIENTRY *glSamplerParameterfPROC) (GLuint sampler, GLenum pname, GLfloat param);
typedef GL_APICALL void (GL_APIENTRY *glSamplerParameterivPROC) (GLuint sampler, GLenum pname, const GLint * params);
typedef GL_APICALL void (GL_APIENTRY *glSamplerParameterfvPROC) (GLuint sampler, GLenum pname, const GLfloat * params);
typedef GL_APICALL void (GL_APIENTRY *glGetSamplerParameterivPROC) (GLuint sampler, GLenum pname, GLint * params);
typedef GL_APICALL void (GL_APIENTRY *glGetSamplerParameterfvPROC) (GLuint sampler, GLenum pname, GLfloat * params);
typedef GL_APICALL void (GL_APIENTRY *glVertexAttribDivisorPROC) (GLuint index, GLuint divisor);
typedef GL_APICALL void (GL_APIENTRY *glBindTransformFeedbackPROC) (GLenum target, GLuint id);
typedef GL_APICALL void (GL_APIENTRY *glDeleteTransformFeedbacksPROC) (GLsizei n, const GLuint * ids);
typedef GL_APICALL void (GL_APIENTRY *glGenTransformFeedbacksPROC) (GLsizei n, GLuint * ids);
typedef GL_APICALL GLboolean (GL_APIENTRY *glIsTransformFeedbackPROC) (GLuint id);
typedef GL_APICALL void (GL_APIENTRY *glPauseTransformFeedbackPROC) ();
typedef GL_APICALL void (GL_APIENTRY *glResumeTransformFeedbackPROC) ();
typedef GL_APICALL void (GL_APIENTRY *glGetProgramBinaryPROC) (GLuint program, GLsizei bufSize, GLsizei * length, GLenum * binaryFormat, GLvoid * binary);
typedef GL_APICALL void (GL_APIENTRY *glProgramBinaryPROC) (GLuint program, GLenum binaryFormat, const GLvoid * binary, GLsizei length);
typedef GL_APICALL void (GL_APIENTRY *glProgramParameteriPROC) (GLuint program, GLenum pname, GLint value);
typedef GL_APICALL void (GL_APIENTRY *glInvalidateFramebufferPROC) (GLenum target, GLsizei numAttachments, const GLenum * attachments);
typedef GL_APICALL void (GL_APIENTRY *glInvalidateSubFramebufferPROC) (GLenum target, GLsizei numAttachments, const GLenum * attachments, GLint x, GLint y, GLsizei width, GLsizei height);
typedef GL_APICALL void (GL_APIENTRY *glTexStorage2DPROC) (GLenum target, GLsizei levels, GLenum internalformat, GLsizei width, GLsizei height);
typedef GL_APICALL void (GL_APIENTRY *glTexStorage3DPROC) (GLenum target, GLsizei levels, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth);
typedef GL_APICALL void (GL_APIENTRY *glGetInternalformativPROC) (GLenum target, GLenum internalformat, GLenum pname, GLsizei bufSize, GLint * params);

static glReadBufferPROC glReadBuffer;
static glDrawRangeElementsPROC glDrawRangeElements;
static glTexImage3DPROC glTexImage3D;
static glTexSubImage3DPROC glTexSubImage3D;
static glCopyTexSubImage3DPROC glCopyTexSubImage3D;
static glCompressedTexImage3DPROC glCompressedTexImage3D;
static glCompressedTexSubImage3DPROC glCompressedTexSubImage3D;
static glGenQueriesPROC glGenQueries;
static glDeleteQueriesPROC glDeleteQueries;
static glIsQueryPROC glIsQuery;
static glBeginQueryPROC glBeginQuery;
static glEndQueryPROC glEndQuery;
static glGetQueryivPROC glGetQueryiv;
static glGetQueryObjectuivPROC glGetQueryObjectuiv;
static glUnmapBufferPROC glUnmapBuffer;
static glGetBufferPointervPROC glGetBufferPointerv;
static glDrawBuffersPROC glDrawBuffers;
static glUniformMatrix2x3fvPROC glUniformMatrix2x3fv;
static glUniformMatrix3x2fvPROC glUniformMatrix3x2fv;
static glUniformMatrix2x4fvPROC glUniformMatrix2x4fv;
static glUniformMatrix4x2fvPROC glUniformMatrix4x2fv;
static glUniformMatrix3x4fvPROC glUniformMatrix3x4fv;
static glUniformMatrix4x3fvPROC glUniformMatrix4x3fv;
static glBlitFramebufferPROC glBlitFramebuffer;
static glRenderbufferStorageMultisamplePROC glRenderbufferStorageMultisample;
static glFramebufferTextureLayerPROC glFramebufferTextureLayer;
static glMapBufferRangePROC glMapBufferRange;
static glFlushMappedBufferRangePROC glFlushMappedBufferRange;
static glBindVertexArrayPROC glBindVertexArray;
static glDeleteVertexArraysPROC glDeleteVertexArrays;
static glGenVertexArraysPROC glGenVertexArrays;
static glIsVertexArrayPROC glIsVertexArray;
static glGetIntegeri_vPROC glGetIntegeri_v;
static glBeginTransformFeedbackPROC glBeginTransformFeedback;
static glEndTransformFeedbackPROC glEndTransformFeedback;
static glBindBufferRangePROC glBindBufferRange;
static glBindBufferBasePROC glBindBufferBase;
static glTransformFeedbackVaryingsPROC glTransformFeedbackVaryings;
static glGetTransformFeedbackVaryingPROC glGetTransformFeedbackVarying;
static glVertexAttribIPointerPROC glVertexAttribIPointer;
static glGetVertexAttribIivPROC glGetVertexAttribIiv;
static glGetVertexAttribIuivPROC glGetVertexAttribIuiv;
static glVertexAttribI4iPROC glVertexAttribI4i;
static glVertexAttribI4uiPROC glVertexAttribI4ui;
static glVertexAttribI4ivPROC glVertexAttribI4iv;
static glVertexAttribI4uivPROC glVertexAttribI4uiv;
static glGetUniformuivPROC glGetUniformuiv;
static glGetFragDataLocationPROC glGetFragDataLocation;
static glUniform1uiPROC glUniform1ui;
static glUniform2uiPROC glUniform2ui;
static glUniform3uiPROC glUniform3ui;
static glUniform4uiPROC glUniform4ui;
static glUniform1uivPROC glUniform1uiv;
static glUniform2uivPROC glUniform2uiv;
static glUniform3uivPROC glUniform3uiv;
static glUniform4uivPROC glUniform4uiv;
static glClearBufferfvPROC glClearBufferfv;
static glClearBufferivPROC glClearBufferiv;
static glClearBufferuivPROC glClearBufferuiv;
static glClearBufferfiPROC glClearBufferfi;
static glGetStringiPROC glGetStringi;
static glCopyBufferSubDataPROC glCopyBufferSubData;
static glGetUniformIndicesPROC glGetUniformIndices;
static glGetActiveUniformsivPROC glGetActiveUniformsiv;
static glGetUniformBlockIndexPROC glGetUniformBlockIndex;
static glGetActiveUniformBlockivPROC glGetActiveUniformBlockiv;
static glGetActiveUniformBlockNamePROC glGetActiveUniformBlockName;
static glUniformBlockBindingPROC glUniformBlockBinding;
static glDrawArraysInstancedPROC glDrawArraysInstanced;
static glDrawElementsInstancedPROC glDrawElementsInstanced;
static glFenceSyncPROC glFenceSync;
static glIsSyncPROC glIsSync;
static glDeleteSyncPROC glDeleteSync;
static glClientWaitSyncPROC glClientWaitSync;
static glWaitSyncPROC glWaitSync;
static glGetInteger64vPROC glGetInteger64v;
static glGetInteger64i_vPROC glGetInteger64i_v;
static glGetSyncivPROC glGetSynciv;
static glGetBufferParameteri64vPROC glGetBufferParameteri64v;
static glGenSamplersPROC glGenSamplers;
static glDeleteSamplersPROC glDeleteSamplers;
static glIsSamplerPROC glIsSampler;
static glBindSamplerPROC glBindSampler;
static glSamplerParameteriPROC glSamplerParameteri;
static glSamplerParameterfPROC glSamplerParameterf;
static glSamplerParameterivPROC glSamplerParameteriv;
static glSamplerParameterfvPROC glSamplerParameterfv;
static glGetSamplerParameterivPROC glGetSamplerParameteriv;
static glGetSamplerParameterfvPROC glGetSamplerParameterfv;
static glVertexAttribDivisorPROC glVertexAttribDivisor;
static glBindTransformFeedbackPROC glBindTransformFeedback;
static glDeleteTransformFeedbacksPROC glDeleteTransformFeedbacks;
static glGenTransformFeedbacksPROC glGenTransformFeedbacks;
static glIsTransformFeedbackPROC glIsTransformFeedback;
static glPauseTransformFeedbackPROC glPauseTransformFeedback;
static glResumeTransformFeedbackPROC glResumeTransformFeedback;
static glGetProgramBinaryPROC glGetProgramBinary;
static glProgramBinaryPROC glProgramBinary;
static glProgramParameteriPROC glProgramParameteri;
static glInvalidateFramebufferPROC glInvalidateFramebuffer;
static glInvalidateSubFramebufferPROC glInvalidateSubFramebuffer;
static glTexStorage2DPROC glTexStorage2D;
static glTexStorage3DPROC glTexStorage3D;
static glGetInternalformativPROC glGetInternalformativ;

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglReadBuffer(JNIEnv *env, jclass clazz, jint mode) {
	glReadBuffer(mode);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDrawRangeElements(JNIEnv *env, jclass clazz, jint mode, jint start, jint end, jint count, jint type, jlong indices) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawRangeElements(mode, start, end, count, type, indices_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDrawRangeElementsBO(JNIEnv *env, jclass clazz, jint mode, jint start, jint end, jint count, jint type, jlong indices_buffer_offset) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawRangeElements(mode, start, end, count, type, indices_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglTexImage3D(JNIEnv *env, jclass clazz, jint target, jint level, jint internalFormat, jint width, jint height, jint depth, jint border, jint format, jint type, jlong pixels) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, pixels_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglTexImage3DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalFormat, jint width, jint height, jint depth, jint border, jint format, jint type, jlong pixels_buffer_offset) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, pixels_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglTexSubImage3D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jlong pixels) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglTexSubImage3DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jlong pixels_buffer_offset) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglCopyTexSubImage3D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint x, jint y, jint width, jint height) {
	glCopyTexSubImage3D(target, level, xoffset, yoffset, zoffset, x, y, width, height);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglCompressedTexImage3D(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint imageSize, jlong data) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTexImage3D(target, level, internalformat, width, height, depth, border, imageSize, data_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglCompressedTexImage3DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint imageSize, jlong data_buffer_offset) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTexImage3D(target, level, internalformat, width, height, depth, border, imageSize, data_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglCompressedTexSubImage3D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jlong data) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glCompressedTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglCompressedTexSubImage3DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jlong data_buffer_offset) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glCompressedTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGenQueries(JNIEnv *env, jclass clazz, jint n, jlong ids) {
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	glGenQueries(n, ids_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDeleteQueries(JNIEnv *env, jclass clazz, jint n, jlong ids) {
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	glDeleteQueries(n, ids_address);
}

static jboolean JNICALL Java_org_lwjgl_opengles_GLES30_nglIsQuery(JNIEnv *env, jclass clazz, jint id) {
	GLboolean __result = glIsQuery(id);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglBeginQuery(JNIEnv *env, jclass clazz, jint target, jint id) {
	glBeginQuery(target, id);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglEndQuery(JNIEnv *env, jclass clazz, jint target) {
	glEndQuery(target);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetQueryiv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetQueryiv(target, pname, params_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetQueryObjectuiv(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetQueryObjectuiv(id, pname, params_address);
}

static jboolean JNICALL Java_org_lwjgl_opengles_GLES30_nglUnmapBuffer(JNIEnv *env, jclass clazz, jint target) {
	GLboolean __result = glUnmapBuffer(target);
	return __result;
}

static jobject JNICALL Java_org_lwjgl_opengles_GLES30_nglGetBufferPointerv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong result_size) {
	GLvoid * __result;
	glGetBufferPointerv(target, pname, &__result);
	return safeNewBuffer(env, __result, result_size);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDrawBuffers(JNIEnv *env, jclass clazz, jint size, jlong buffers) {
	const GLenum *buffers_address = (const GLenum *)(intptr_t)buffers;
	glDrawBuffers(size, buffers_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniformMatrix2x3fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix2x3fv(location, count, transpose, matrices_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniformMatrix3x2fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix3x2fv(location, count, transpose, matrices_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniformMatrix2x4fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix2x4fv(location, count, transpose, matrices_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniformMatrix4x2fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix4x2fv(location, count, transpose, matrices_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniformMatrix3x4fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix3x4fv(location, count, transpose, matrices_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniformMatrix4x3fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix4x3fv(location, count, transpose, matrices_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglBlitFramebuffer(JNIEnv *env, jclass clazz, jint srcX0, jint srcY0, jint srcX1, jint srcY1, jint dstX0, jint dstY0, jint dstX1, jint dstY1, jint mask, jint filter) {
	glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglRenderbufferStorageMultisample(JNIEnv *env, jclass clazz, jint target, jint samples, jint internalformat, jint width, jint height) {
	glRenderbufferStorageMultisample(target, samples, internalformat, width, height);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglFramebufferTextureLayer(JNIEnv *env, jclass clazz, jint target, jint attachment, jint texture, jint level, jint layer) {
	glFramebufferTextureLayer(target, attachment, texture, level, layer);
}

static jobject JNICALL Java_org_lwjgl_opengles_GLES30_nglMapBufferRange(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong length, jint access, jobject old_buffer) {
	GLvoid * __result = glMapBufferRange(target, offset, length, access);
	return safeNewBufferCached(env, __result, length, old_buffer);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglFlushMappedBufferRange(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong length) {
	glFlushMappedBufferRange(target, offset, length);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglBindVertexArray(JNIEnv *env, jclass clazz, jint array) {
	glBindVertexArray(array);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDeleteVertexArrays(JNIEnv *env, jclass clazz, jint n, jlong arrays) {
	const GLuint *arrays_address = (const GLuint *)(intptr_t)arrays;
	glDeleteVertexArrays(n, arrays_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGenVertexArrays(JNIEnv *env, jclass clazz, jint n, jlong arrays) {
	GLuint *arrays_address = (GLuint *)(intptr_t)arrays;
	glGenVertexArrays(n, arrays_address);
}

static jboolean JNICALL Java_org_lwjgl_opengles_GLES30_nglIsVertexArray(JNIEnv *env, jclass clazz, jint array) {
	GLboolean __result = glIsVertexArray(array);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetIntegeri_1v(JNIEnv *env, jclass clazz, jint value, jint index, jlong data) {
	GLint *data_address = (GLint *)(intptr_t)data;
	glGetIntegeri_v(value, index, data_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglBeginTransformFeedback(JNIEnv *env, jclass clazz, jint primitiveMode) {
	glBeginTransformFeedback(primitiveMode);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglEndTransformFeedback(JNIEnv *env, jclass clazz) {
	glEndTransformFeedback();
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglBindBufferRange(JNIEnv *env, jclass clazz, jint target, jint index, jint buffer, jlong offset, jlong size) {
	glBindBufferRange(target, index, buffer, offset, size);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglBindBufferBase(JNIEnv *env, jclass clazz, jint target, jint index, jint buffer) {
	glBindBufferBase(target, index, buffer);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglTransformFeedbackVaryings(JNIEnv *env, jclass clazz, jint program, jint count, jlong varyings, jint bufferMode) {
	const GLchar *varyings_address = (const GLchar *)(intptr_t)varyings;
	int _str_i;
	GLchar *_str_address;
	GLchar **varyings_str = (GLchar **) malloc(count * sizeof(GLchar *));
	_str_i = 0;
	_str_address = (GLchar *)varyings_address;
	while ( _str_i < count ) {
		varyings_str[_str_i++] = _str_address;
		_str_address += strlen((const char *)_str_address) + 1;
	}
	glTransformFeedbackVaryings(program, count, (const GLchar **)varyings_str, bufferMode);
	free(varyings_str);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetTransformFeedbackVarying(JNIEnv *env, jclass clazz, jint program, jint index, jint bufSize, jlong length, jlong size, jlong type, jlong name) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLsizei *size_address = (GLsizei *)(intptr_t)size;
	GLenum *type_address = (GLenum *)(intptr_t)type;
	GLchar *name_address = (GLchar *)(intptr_t)name;
	glGetTransformFeedbackVarying(program, index, bufSize, length_address, size_address, type_address, name_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglVertexAttribIPointer(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong buffer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)buffer;
	glVertexAttribIPointer(index, size, type, stride, buffer_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglVertexAttribIPointerBO(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong buffer_buffer_offset) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)offsetToPointer(buffer_buffer_offset);
	glVertexAttribIPointer(index, size, type, stride, buffer_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetVertexAttribIiv(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVertexAttribIiv(index, pname, params_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetVertexAttribIuiv(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetVertexAttribIuiv(index, pname, params_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglVertexAttribI4i(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jint z, jint w) {
	glVertexAttribI4i(index, x, y, z, w);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglVertexAttribI4ui(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jint z, jint w) {
	glVertexAttribI4ui(index, x, y, z, w);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglVertexAttribI4iv(JNIEnv *env, jclass clazz, jint index, jlong v) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glVertexAttribI4iv(index, v_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglVertexAttribI4uiv(JNIEnv *env, jclass clazz, jint index, jlong v) {
	const GLuint *v_address = (const GLuint *)(intptr_t)v;
	glVertexAttribI4uiv(index, v_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetUniformuiv(JNIEnv *env, jclass clazz, jint program, jint location, jlong params) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetUniformuiv(program, location, params_address);
}

static jint JNICALL Java_org_lwjgl_opengles_GLES30_nglGetFragDataLocation(JNIEnv *env, jclass clazz, jint program, jlong name) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	GLint __result = glGetFragDataLocation(program, name_address);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniform1ui(JNIEnv *env, jclass clazz, jint location, jint v0) {
	glUniform1ui(location, v0);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniform2ui(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1) {
	glUniform2ui(location, v0, v1);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniform3ui(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jint v2) {
	glUniform3ui(location, v0, v1, v2);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniform4ui(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jint v2, jint v3) {
	glUniform4ui(location, v0, v1, v2, v3);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniform1uiv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform1uiv(location, count, value_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniform2uiv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform2uiv(location, count, value_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniform3uiv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform3uiv(location, count, value_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniform4uiv(JNIEnv *env, jclass clazz, jint location, jint count, jlong value) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform4uiv(location, count, value_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglClearBufferfv(JNIEnv *env, jclass clazz, jint buffer, jint drawbuffer, jlong value) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glClearBufferfv(buffer, drawbuffer, value_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglClearBufferiv(JNIEnv *env, jclass clazz, jint buffer, jint drawbuffer, jlong value) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glClearBufferiv(buffer, drawbuffer, value_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglClearBufferuiv(JNIEnv *env, jclass clazz, jint buffer, jint drawbuffer, jlong value) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glClearBufferuiv(buffer, drawbuffer, value_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglClearBufferfi(JNIEnv *env, jclass clazz, jint buffer, jint drawbuffer, jfloat depth, jint stencil) {
	glClearBufferfi(buffer, drawbuffer, depth, stencil);
}

static jobject JNICALL Java_org_lwjgl_opengles_GLES30_nglGetStringi(JNIEnv *env, jclass clazz, jint name, jint index) {
	GLubyte * __result = glGetStringi(name, index);
	return NewStringNativeUnsigned(env, __result);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglCopyBufferSubData(JNIEnv *env, jclass clazz, jint readtarget, jint writetarget, jlong readoffset, jlong writeoffset, jlong size) {
	glCopyBufferSubData(readtarget, writetarget, readoffset, writeoffset, size);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetUniformIndices(JNIEnv *env, jclass clazz, jint program, jint uniformCount, jlong uniformNames, jlong uniformIndices) {
	const GLchar *uniformNames_address = (const GLchar *)(intptr_t)uniformNames;
	int _str_i;
	GLchar *_str_address;
	GLchar **uniformNames_str = (GLchar **) malloc(uniformCount * sizeof(GLchar *));
	GLuint *uniformIndices_address = (GLuint *)(intptr_t)uniformIndices;
	_str_i = 0;
	_str_address = (GLchar *)uniformNames_address;
	while ( _str_i < uniformCount ) {
		uniformNames_str[_str_i++] = _str_address;
		_str_address += strlen((const char *)_str_address) + 1;
	}
	glGetUniformIndices(program, uniformCount, (const GLchar **)uniformNames_str, uniformIndices_address);
	free(uniformNames_str);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetActiveUniformsiv(JNIEnv *env, jclass clazz, jint program, jint uniformCount, jlong uniformIndices, jint pname, jlong params) {
	const GLuint *uniformIndices_address = (const GLuint *)(intptr_t)uniformIndices;
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetActiveUniformsiv(program, uniformCount, uniformIndices_address, pname, params_address);
}

static jint JNICALL Java_org_lwjgl_opengles_GLES30_nglGetUniformBlockIndex(JNIEnv *env, jclass clazz, jint program, jlong uniformBlockName) {
	const GLchar *uniformBlockName_address = (const GLchar *)(intptr_t)uniformBlockName;
	GLuint __result = glGetUniformBlockIndex(program, uniformBlockName_address);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetActiveUniformBlockiv(JNIEnv *env, jclass clazz, jint program, jint uniformBlockIndex, jint pname, jlong params) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetActiveUniformBlockiv(program, uniformBlockIndex, pname, params_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetActiveUniformBlockName(JNIEnv *env, jclass clazz, jint program, jint uniformBlockIndex, jint bufSize, jlong length, jlong uniformBlockName) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *uniformBlockName_address = (GLchar *)(intptr_t)uniformBlockName;
	glGetActiveUniformBlockName(program, uniformBlockIndex, bufSize, length_address, uniformBlockName_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglUniformBlockBinding(JNIEnv *env, jclass clazz, jint program, jint uniformBlockIndex, jint uniformBlockBinding) {
	glUniformBlockBinding(program, uniformBlockIndex, uniformBlockBinding);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDrawArraysInstanced(JNIEnv *env, jclass clazz, jint mode, jint first, jint count, jint primcount) {
	glDrawArraysInstanced(mode, first, count, primcount);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDrawElementsInstanced(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices, jint primcount) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawElementsInstanced(mode, count, type, indices_address, primcount);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDrawElementsInstancedBO(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices_buffer_offset, jint primcount) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawElementsInstanced(mode, count, type, indices_address, primcount);
}

static jlong JNICALL Java_org_lwjgl_opengles_GLES30_nglFenceSync(JNIEnv *env, jclass clazz, jint condition, jint flags) {
	GLsync __result = glFenceSync(condition, flags);
	return (intptr_t)__result;
}

static jboolean JNICALL Java_org_lwjgl_opengles_GLES30_nglIsSync(JNIEnv *env, jclass clazz, jlong sync) {
	GLboolean __result = glIsSync((GLsync)(intptr_t)sync);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDeleteSync(JNIEnv *env, jclass clazz, jlong sync) {
	glDeleteSync((GLsync)(intptr_t)sync);
}

static jint JNICALL Java_org_lwjgl_opengles_GLES30_nglClientWaitSync(JNIEnv *env, jclass clazz, jlong sync, jint flags, jlong timeout) {
	GLenum __result = glClientWaitSync((GLsync)(intptr_t)sync, flags, timeout);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglWaitSync(JNIEnv *env, jclass clazz, jlong sync, jint flags, jlong timeout) {
	glWaitSync((GLsync)(intptr_t)sync, flags, timeout);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetInteger64v(JNIEnv *env, jclass clazz, jint pname, jlong data) {
	GLint64 *data_address = (GLint64 *)(intptr_t)data;
	glGetInteger64v(pname, data_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetInteger64i_1v(JNIEnv *env, jclass clazz, jint value, jint index, jlong data) {
	GLint64 *data_address = (GLint64 *)(intptr_t)data;
	glGetInteger64i_v(value, index, data_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetSynciv(JNIEnv *env, jclass clazz, jlong sync, jint pname, jint bufSize, jlong length, jlong values) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLint *values_address = (GLint *)(intptr_t)values;
	glGetSynciv((GLsync)(intptr_t)sync, pname, bufSize, length_address, values_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetBufferParameteri64v(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params) {
	GLint64 *params_address = (GLint64 *)(intptr_t)params;
	glGetBufferParameteri64v(target, pname, params_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGenSamplers(JNIEnv *env, jclass clazz, jint count, jlong samplers) {
	GLuint *samplers_address = (GLuint *)(intptr_t)samplers;
	glGenSamplers(count, samplers_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDeleteSamplers(JNIEnv *env, jclass clazz, jint count, jlong samplers) {
	const GLuint *samplers_address = (const GLuint *)(intptr_t)samplers;
	glDeleteSamplers(count, samplers_address);
}

static jboolean JNICALL Java_org_lwjgl_opengles_GLES30_nglIsSampler(JNIEnv *env, jclass clazz, jint sampler) {
	GLboolean __result = glIsSampler(sampler);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglBindSampler(JNIEnv *env, jclass clazz, jint unit, jint sampler) {
	glBindSampler(unit, sampler);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglSamplerParameteri(JNIEnv *env, jclass clazz, jint sampler, jint pname, jint param) {
	glSamplerParameteri(sampler, pname, param);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglSamplerParameterf(JNIEnv *env, jclass clazz, jint sampler, jint pname, jfloat param) {
	glSamplerParameterf(sampler, pname, param);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglSamplerParameteriv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glSamplerParameteriv(sampler, pname, params_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglSamplerParameterfv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glSamplerParameterfv(sampler, pname, params_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetSamplerParameteriv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetSamplerParameteriv(sampler, pname, params_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetSamplerParameterfv(JNIEnv *env, jclass clazz, jint sampler, jint pname, jlong params) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetSamplerParameterfv(sampler, pname, params_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglVertexAttribDivisor(JNIEnv *env, jclass clazz, jint index, jint divisor) {
	glVertexAttribDivisor(index, divisor);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglBindTransformFeedback(JNIEnv *env, jclass clazz, jint target, jint id) {
	glBindTransformFeedback(target, id);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglDeleteTransformFeedbacks(JNIEnv *env, jclass clazz, jint n, jlong ids) {
	const GLuint *ids_address = (const GLuint *)(intptr_t)ids;
	glDeleteTransformFeedbacks(n, ids_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGenTransformFeedbacks(JNIEnv *env, jclass clazz, jint n, jlong ids) {
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	glGenTransformFeedbacks(n, ids_address);
}

static jboolean JNICALL Java_org_lwjgl_opengles_GLES30_nglIsTransformFeedback(JNIEnv *env, jclass clazz, jint id) {
	GLboolean __result = glIsTransformFeedback(id);
	return __result;
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglPauseTransformFeedback(JNIEnv *env, jclass clazz) {
	glPauseTransformFeedback();
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglResumeTransformFeedback(JNIEnv *env, jclass clazz) {
	glResumeTransformFeedback();
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetProgramBinary(JNIEnv *env, jclass clazz, jint program, jint bufSize, jlong length, jlong binaryFormat, jlong binary) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLenum *binaryFormat_address = (GLenum *)(intptr_t)binaryFormat;
	GLvoid *binary_address = (GLvoid *)(intptr_t)binary;
	glGetProgramBinary(program, bufSize, length_address, binaryFormat_address, binary_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglProgramBinary(JNIEnv *env, jclass clazz, jint program, jint binaryFormat, jlong binary, jint length) {
	const GLvoid *binary_address = (const GLvoid *)(intptr_t)binary;
	glProgramBinary(program, binaryFormat, binary_address, length);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglProgramParameteri(JNIEnv *env, jclass clazz, jint program, jint pname, jint value) {
	glProgramParameteri(program, pname, value);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglInvalidateFramebuffer(JNIEnv *env, jclass clazz, jint target, jint numAttachments, jlong attachments) {
	const GLenum *attachments_address = (const GLenum *)(intptr_t)attachments;
	glInvalidateFramebuffer(target, numAttachments, attachments_address);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglInvalidateSubFramebuffer(JNIEnv *env, jclass clazz, jint target, jint numAttachments, jlong attachments, jint x, jint y, jint width, jint height) {
	const GLenum *attachments_address = (const GLenum *)(intptr_t)attachments;
	glInvalidateSubFramebuffer(target, numAttachments, attachments_address, x, y, width, height);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglTexStorage2D(JNIEnv *env, jclass clazz, jint target, jint levels, jint internalformat, jint width, jint height) {
	glTexStorage2D(target, levels, internalformat, width, height);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglTexStorage3D(JNIEnv *env, jclass clazz, jint target, jint levels, jint internalformat, jint width, jint height, jint depth) {
	glTexStorage3D(target, levels, internalformat, width, height, depth);
}

static void JNICALL Java_org_lwjgl_opengles_GLES30_nglGetInternalformativ(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint pname, jint bufSize, jlong params) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetInternalformativ(target, internalformat, pname, bufSize, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengles_GLES30_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nglReadBuffer", "(I)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglReadBuffer, "glReadBuffer", (void *)&glReadBuffer, false},
		{"nglDrawRangeElements", "(IIIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDrawRangeElements, "glDrawRangeElements", (void *)&glDrawRangeElements, false},
		{"nglDrawRangeElementsBO", "(IIIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDrawRangeElementsBO, "glDrawRangeElements", (void *)&glDrawRangeElements, false},
		{"nglTexImage3D", "(IIIIIIIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglTexImage3D, "glTexImage3D", (void *)&glTexImage3D, false},
		{"nglTexImage3DBO", "(IIIIIIIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglTexImage3DBO, "glTexImage3D", (void *)&glTexImage3D, false},
		{"nglTexSubImage3D", "(IIIIIIIIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglTexSubImage3D, "glTexSubImage3D", (void *)&glTexSubImage3D, false},
		{"nglTexSubImage3DBO", "(IIIIIIIIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglTexSubImage3DBO, "glTexSubImage3D", (void *)&glTexSubImage3D, false},
		{"nglCopyTexSubImage3D", "(IIIIIIIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglCopyTexSubImage3D, "glCopyTexSubImage3D", (void *)&glCopyTexSubImage3D, false},
		{"nglCompressedTexImage3D", "(IIIIIIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglCompressedTexImage3D, "glCompressedTexImage3D", (void *)&glCompressedTexImage3D, false},
		{"nglCompressedTexImage3DBO", "(IIIIIIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglCompressedTexImage3DBO, "glCompressedTexImage3D", (void *)&glCompressedTexImage3D, false},
		{"nglCompressedTexSubImage3D", "(IIIIIIIIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglCompressedTexSubImage3D, "glCompressedTexSubImage3D", (void *)&glCompressedTexSubImage3D, false},
		{"nglCompressedTexSubImage3DBO", "(IIIIIIIIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglCompressedTexSubImage3DBO, "glCompressedTexSubImage3D", (void *)&glCompressedTexSubImage3D, false},
		{"nglGenQueries", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGenQueries, "glGenQueries", (void *)&glGenQueries, false},
		{"nglDeleteQueries", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDeleteQueries, "glDeleteQueries", (void *)&glDeleteQueries, false},
		{"nglIsQuery", "(I)Z", (void *)&Java_org_lwjgl_opengles_GLES30_nglIsQuery, "glIsQuery", (void *)&glIsQuery, false},
		{"nglBeginQuery", "(II)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglBeginQuery, "glBeginQuery", (void *)&glBeginQuery, false},
		{"nglEndQuery", "(I)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglEndQuery, "glEndQuery", (void *)&glEndQuery, false},
		{"nglGetQueryiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetQueryiv, "glGetQueryiv", (void *)&glGetQueryiv, false},
		{"nglGetQueryObjectuiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetQueryObjectuiv, "glGetQueryObjectuiv", (void *)&glGetQueryObjectuiv, false},
		{"nglUnmapBuffer", "(I)Z", (void *)&Java_org_lwjgl_opengles_GLES30_nglUnmapBuffer, "glUnmapBuffer", (void *)&glUnmapBuffer, false},
		{"nglGetBufferPointerv", "(IIJ)Ljava/nio/ByteBuffer;", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetBufferPointerv, "glGetBufferPointerv", (void *)&glGetBufferPointerv, false},
		{"nglDrawBuffers", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDrawBuffers, "glDrawBuffers", (void *)&glDrawBuffers, false},
		{"nglUniformMatrix2x3fv", "(IIZJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniformMatrix2x3fv, "glUniformMatrix2x3fv", (void *)&glUniformMatrix2x3fv, false},
		{"nglUniformMatrix3x2fv", "(IIZJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniformMatrix3x2fv, "glUniformMatrix3x2fv", (void *)&glUniformMatrix3x2fv, false},
		{"nglUniformMatrix2x4fv", "(IIZJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniformMatrix2x4fv, "glUniformMatrix2x4fv", (void *)&glUniformMatrix2x4fv, false},
		{"nglUniformMatrix4x2fv", "(IIZJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniformMatrix4x2fv, "glUniformMatrix4x2fv", (void *)&glUniformMatrix4x2fv, false},
		{"nglUniformMatrix3x4fv", "(IIZJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniformMatrix3x4fv, "glUniformMatrix3x4fv", (void *)&glUniformMatrix3x4fv, false},
		{"nglUniformMatrix4x3fv", "(IIZJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniformMatrix4x3fv, "glUniformMatrix4x3fv", (void *)&glUniformMatrix4x3fv, false},
		{"nglBlitFramebuffer", "(IIIIIIIIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglBlitFramebuffer, "glBlitFramebuffer", (void *)&glBlitFramebuffer, false},
		{"nglRenderbufferStorageMultisample", "(IIIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglRenderbufferStorageMultisample, "glRenderbufferStorageMultisample", (void *)&glRenderbufferStorageMultisample, false},
		{"nglFramebufferTextureLayer", "(IIIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglFramebufferTextureLayer, "glFramebufferTextureLayer", (void *)&glFramebufferTextureLayer, false},
		{"nglMapBufferRange", "(IJJIJLjava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;", (void *)&Java_org_lwjgl_opengles_GLES30_nglMapBufferRange, "glMapBufferRange", (void *)&glMapBufferRange, false},
		{"nglFlushMappedBufferRange", "(IJJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglFlushMappedBufferRange, "glFlushMappedBufferRange", (void *)&glFlushMappedBufferRange, false},
		{"nglBindVertexArray", "(I)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglBindVertexArray, "glBindVertexArray", (void *)&glBindVertexArray, false},
		{"nglDeleteVertexArrays", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDeleteVertexArrays, "glDeleteVertexArrays", (void *)&glDeleteVertexArrays, false},
		{"nglGenVertexArrays", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGenVertexArrays, "glGenVertexArrays", (void *)&glGenVertexArrays, false},
		{"nglIsVertexArray", "(I)Z", (void *)&Java_org_lwjgl_opengles_GLES30_nglIsVertexArray, "glIsVertexArray", (void *)&glIsVertexArray, false},
		{"nglGetIntegeri_v", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetIntegeri_1v, "glGetIntegeri_v", (void *)&glGetIntegeri_v, false},
		{"nglBeginTransformFeedback", "(I)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglBeginTransformFeedback, "glBeginTransformFeedback", (void *)&glBeginTransformFeedback, false},
		{"nglEndTransformFeedback", "()V", (void *)&Java_org_lwjgl_opengles_GLES30_nglEndTransformFeedback, "glEndTransformFeedback", (void *)&glEndTransformFeedback, false},
		{"nglBindBufferRange", "(IIIJJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglBindBufferRange, "glBindBufferRange", (void *)&glBindBufferRange, false},
		{"nglBindBufferBase", "(III)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglBindBufferBase, "glBindBufferBase", (void *)&glBindBufferBase, false},
		{"nglTransformFeedbackVaryings", "(IIJI)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglTransformFeedbackVaryings, "glTransformFeedbackVaryings", (void *)&glTransformFeedbackVaryings, false},
		{"nglGetTransformFeedbackVarying", "(IIIJJJJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetTransformFeedbackVarying, "glGetTransformFeedbackVarying", (void *)&glGetTransformFeedbackVarying, false},
		{"nglVertexAttribIPointer", "(IIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglVertexAttribIPointer, "glVertexAttribIPointer", (void *)&glVertexAttribIPointer, false},
		{"nglVertexAttribIPointerBO", "(IIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglVertexAttribIPointerBO, "glVertexAttribIPointer", (void *)&glVertexAttribIPointer, false},
		{"nglGetVertexAttribIiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetVertexAttribIiv, "glGetVertexAttribIiv", (void *)&glGetVertexAttribIiv, false},
		{"nglGetVertexAttribIuiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetVertexAttribIuiv, "glGetVertexAttribIuiv", (void *)&glGetVertexAttribIuiv, false},
		{"nglVertexAttribI4i", "(IIIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglVertexAttribI4i, "glVertexAttribI4i", (void *)&glVertexAttribI4i, false},
		{"nglVertexAttribI4ui", "(IIIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglVertexAttribI4ui, "glVertexAttribI4ui", (void *)&glVertexAttribI4ui, false},
		{"nglVertexAttribI4iv", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglVertexAttribI4iv, "glVertexAttribI4iv", (void *)&glVertexAttribI4iv, false},
		{"nglVertexAttribI4uiv", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglVertexAttribI4uiv, "glVertexAttribI4uiv", (void *)&glVertexAttribI4uiv, false},
		{"nglGetUniformuiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetUniformuiv, "glGetUniformuiv", (void *)&glGetUniformuiv, false},
		{"nglGetFragDataLocation", "(IJ)I", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetFragDataLocation, "glGetFragDataLocation", (void *)&glGetFragDataLocation, false},
		{"nglUniform1ui", "(II)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniform1ui, "glUniform1ui", (void *)&glUniform1ui, false},
		{"nglUniform2ui", "(III)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniform2ui, "glUniform2ui", (void *)&glUniform2ui, false},
		{"nglUniform3ui", "(IIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniform3ui, "glUniform3ui", (void *)&glUniform3ui, false},
		{"nglUniform4ui", "(IIIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniform4ui, "glUniform4ui", (void *)&glUniform4ui, false},
		{"nglUniform1uiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniform1uiv, "glUniform1uiv", (void *)&glUniform1uiv, false},
		{"nglUniform2uiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniform2uiv, "glUniform2uiv", (void *)&glUniform2uiv, false},
		{"nglUniform3uiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniform3uiv, "glUniform3uiv", (void *)&glUniform3uiv, false},
		{"nglUniform4uiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniform4uiv, "glUniform4uiv", (void *)&glUniform4uiv, false},
		{"nglClearBufferfv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglClearBufferfv, "glClearBufferfv", (void *)&glClearBufferfv, false},
		{"nglClearBufferiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglClearBufferiv, "glClearBufferiv", (void *)&glClearBufferiv, false},
		{"nglClearBufferuiv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglClearBufferuiv, "glClearBufferuiv", (void *)&glClearBufferuiv, false},
		{"nglClearBufferfi", "(IIFI)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglClearBufferfi, "glClearBufferfi", (void *)&glClearBufferfi, false},
		{"nglGetStringi", "(II)Ljava/lang/String;", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetStringi, "glGetStringi", (void *)&glGetStringi, false},
		{"nglCopyBufferSubData", "(IIJJJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglCopyBufferSubData, "glCopyBufferSubData", (void *)&glCopyBufferSubData, false},
		{"nglGetUniformIndices", "(IIJJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetUniformIndices, "glGetUniformIndices", (void *)&glGetUniformIndices, false},
		{"nglGetActiveUniformsiv", "(IIJIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetActiveUniformsiv, "glGetActiveUniformsiv", (void *)&glGetActiveUniformsiv, false},
		{"nglGetUniformBlockIndex", "(IJ)I", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetUniformBlockIndex, "glGetUniformBlockIndex", (void *)&glGetUniformBlockIndex, false},
		{"nglGetActiveUniformBlockiv", "(IIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetActiveUniformBlockiv, "glGetActiveUniformBlockiv", (void *)&glGetActiveUniformBlockiv, false},
		{"nglGetActiveUniformBlockName", "(IIIJJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetActiveUniformBlockName, "glGetActiveUniformBlockName", (void *)&glGetActiveUniformBlockName, false},
		{"nglUniformBlockBinding", "(III)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglUniformBlockBinding, "glUniformBlockBinding", (void *)&glUniformBlockBinding, false},
		{"nglDrawArraysInstanced", "(IIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDrawArraysInstanced, "glDrawArraysInstanced", (void *)&glDrawArraysInstanced, false},
		{"nglDrawElementsInstanced", "(IIIJI)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDrawElementsInstanced, "glDrawElementsInstanced", (void *)&glDrawElementsInstanced, false},
		{"nglDrawElementsInstancedBO", "(IIIJI)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDrawElementsInstancedBO, "glDrawElementsInstanced", (void *)&glDrawElementsInstanced, false},
		{"nglFenceSync", "(II)J", (void *)&Java_org_lwjgl_opengles_GLES30_nglFenceSync, "glFenceSync", (void *)&glFenceSync, false},
		{"nglIsSync", "(J)Z", (void *)&Java_org_lwjgl_opengles_GLES30_nglIsSync, "glIsSync", (void *)&glIsSync, false},
		{"nglDeleteSync", "(J)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDeleteSync, "glDeleteSync", (void *)&glDeleteSync, false},
		{"nglClientWaitSync", "(JIJ)I", (void *)&Java_org_lwjgl_opengles_GLES30_nglClientWaitSync, "glClientWaitSync", (void *)&glClientWaitSync, false},
		{"nglWaitSync", "(JIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglWaitSync, "glWaitSync", (void *)&glWaitSync, false},
		{"nglGetInteger64v", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetInteger64v, "glGetInteger64v", (void *)&glGetInteger64v, false},
		{"nglGetInteger64i_v", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetInteger64i_1v, "glGetInteger64i_v", (void *)&glGetInteger64i_v, false},
		{"nglGetSynciv", "(JIIJJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetSynciv, "glGetSynciv", (void *)&glGetSynciv, false},
		{"nglGetBufferParameteri64v", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetBufferParameteri64v, "glGetBufferParameteri64v", (void *)&glGetBufferParameteri64v, false},
		{"nglGenSamplers", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGenSamplers, "glGenSamplers", (void *)&glGenSamplers, false},
		{"nglDeleteSamplers", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDeleteSamplers, "glDeleteSamplers", (void *)&glDeleteSamplers, false},
		{"nglIsSampler", "(I)Z", (void *)&Java_org_lwjgl_opengles_GLES30_nglIsSampler, "glIsSampler", (void *)&glIsSampler, false},
		{"nglBindSampler", "(II)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglBindSampler, "glBindSampler", (void *)&glBindSampler, false},
		{"nglSamplerParameteri", "(III)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglSamplerParameteri, "glSamplerParameteri", (void *)&glSamplerParameteri, false},
		{"nglSamplerParameterf", "(IIF)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglSamplerParameterf, "glSamplerParameterf", (void *)&glSamplerParameterf, false},
		{"nglSamplerParameteriv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglSamplerParameteriv, "glSamplerParameteriv", (void *)&glSamplerParameteriv, false},
		{"nglSamplerParameterfv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglSamplerParameterfv, "glSamplerParameterfv", (void *)&glSamplerParameterfv, false},
		{"nglGetSamplerParameteriv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetSamplerParameteriv, "glGetSamplerParameteriv", (void *)&glGetSamplerParameteriv, false},
		{"nglGetSamplerParameterfv", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetSamplerParameterfv, "glGetSamplerParameterfv", (void *)&glGetSamplerParameterfv, false},
		{"nglVertexAttribDivisor", "(II)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglVertexAttribDivisor, "glVertexAttribDivisor", (void *)&glVertexAttribDivisor, false},
		{"nglBindTransformFeedback", "(II)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglBindTransformFeedback, "glBindTransformFeedback", (void *)&glBindTransformFeedback, false},
		{"nglDeleteTransformFeedbacks", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglDeleteTransformFeedbacks, "glDeleteTransformFeedbacks", (void *)&glDeleteTransformFeedbacks, false},
		{"nglGenTransformFeedbacks", "(IJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGenTransformFeedbacks, "glGenTransformFeedbacks", (void *)&glGenTransformFeedbacks, false},
		{"nglIsTransformFeedback", "(I)Z", (void *)&Java_org_lwjgl_opengles_GLES30_nglIsTransformFeedback, "glIsTransformFeedback", (void *)&glIsTransformFeedback, false},
		{"nglPauseTransformFeedback", "()V", (void *)&Java_org_lwjgl_opengles_GLES30_nglPauseTransformFeedback, "glPauseTransformFeedback", (void *)&glPauseTransformFeedback, false},
		{"nglResumeTransformFeedback", "()V", (void *)&Java_org_lwjgl_opengles_GLES30_nglResumeTransformFeedback, "glResumeTransformFeedback", (void *)&glResumeTransformFeedback, false},
		{"nglGetProgramBinary", "(IIJJJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetProgramBinary, "glGetProgramBinary", (void *)&glGetProgramBinary, false},
		{"nglProgramBinary", "(IIJI)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglProgramBinary, "glProgramBinary", (void *)&glProgramBinary, false},
		{"nglProgramParameteri", "(III)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglProgramParameteri, "glProgramParameteri", (void *)&glProgramParameteri, false},
		{"nglInvalidateFramebuffer", "(IIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglInvalidateFramebuffer, "glInvalidateFramebuffer", (void *)&glInvalidateFramebuffer, false},
		{"nglInvalidateSubFramebuffer", "(IIJIIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglInvalidateSubFramebuffer, "glInvalidateSubFramebuffer", (void *)&glInvalidateSubFramebuffer, false},
		{"nglTexStorage2D", "(IIIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglTexStorage2D, "glTexStorage2D", (void *)&glTexStorage2D, false},
		{"nglTexStorage3D", "(IIIIII)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglTexStorage3D, "glTexStorage3D", (void *)&glTexStorage3D, false},
		{"nglGetInternalformativ", "(IIIIJ)V", (void *)&Java_org_lwjgl_opengles_GLES30_nglGetInternalformativ, "glGetInternalformativ", (void *)&glGetInternalformativ, false},

	};
	int num_functions = NUMFUNCTIONS(functions);
	extgl_InitializeClass(env, clazz, num_functions, functions);
}
