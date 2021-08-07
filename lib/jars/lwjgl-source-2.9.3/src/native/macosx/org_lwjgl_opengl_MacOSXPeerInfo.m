/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * $Id$
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 */

#import <jni.h>
#import <Cocoa/Cocoa.h>
#import "org_lwjgl_opengl_MacOSXPeerInfo.h"
#import "context.h"
#import "common_tools.h"

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_MacOSXPeerInfo_createHandle
  (JNIEnv *env, jclass clazz) {
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	jobject handle = newJavaManagedByteBuffer(env, sizeof(MacOSXPeerInfo));
	[pool release];
	return handle;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_MacOSXPeerInfo_nChoosePixelFormat
  (JNIEnv *env, jclass clazz, jobject peer_info_handle, jobject pixel_format, jboolean gl32, jboolean use_display_bpp, jboolean support_window, jboolean support_pbuffer, jboolean double_buffered) {
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	MacOSXPeerInfo *peer_info = (MacOSXPeerInfo *)(*env)->GetDirectBufferAddress(env, peer_info_handle);
	NSOpenGLPixelFormat *macosx_pixel_format = choosePixelFormat(env, pixel_format, gl32, use_display_bpp, support_window, support_pbuffer, double_buffered);
	if (pixel_format == nil) {
		throwException(env, "Could not find pixel format");
		return;
	}
	peer_info->pixel_format = macosx_pixel_format;
	[pool release];
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_MacOSXPeerInfo_nDestroy
  (JNIEnv *env, jclass clazz, jobject peer_info_handle) {
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	MacOSXPeerInfo *peer_info = (MacOSXPeerInfo *)(*env)->GetDirectBufferAddress(env, peer_info_handle);
	[peer_info->pixel_format release];
	[pool release];
}
