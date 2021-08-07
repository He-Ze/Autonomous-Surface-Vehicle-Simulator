/*
 * Copyright (c) 2002-2012 LWJGL Project
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
package org.lwjgl.opencl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opencl.CLDeviceExtension;
import org.lwjgl.util.generator.opencl.cl_int;
import org.lwjgl.util.generator.opencl.cl_uint;
import org.lwjgl.util.generator.opencl.cl_ulong;

@CLDeviceExtension
public interface AMD_bus_addressable_memory {

	/** cl_mem flag - bitfield */
	int CL_MEM_BUS_ADDRESSABLE_AMD   = (1 << 30),
		CL_MEM_EXTERNAL_PHYSICAL_AMD = (1 << 31);

	int CL_COMMAND_WAIT_SIGNAL_AMD           = 0x4080,
		CL_COMMAND_WRITE_SIGNAL_AMD          = 0x4081,
		CL_COMMAND_MAKE_BUFFERS_RESIDENT_AMD = 0x4082;

	/*
	@Code(javaAfterNative = "\t\tif ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);")
	@cl_int
	int clEnqueueWaitSignalAMD(
		@PointerWrapper("cl_command_queue") CLCommandQueue command_queue,
		@PointerWrapper("cl_mem") CLMem mem_object,
		@cl_uint int value,
		@AutoSize(value = "event_wait_list", canBeNull = true) @cl_uint int num_events,
		@Check(canBeNull = true) @Const @NativeType("cl_event") PointerBuffer event_wait_list,
		@OutParameter @Check(value = "1", canBeNull = true) @NativeType("cl_event") PointerBuffer event
	);

	@Code(javaAfterNative = "\t\tif ( __result == CL_SUCCESS ) command_queue.registerCLEvent(event);")
	@cl_int
	int clEnqueueWriteSignalAMD(
		@PointerWrapper("cl_command_queue") CLCommandQueue command_queue,
		@PointerWrapper("cl_mem") CLMem mem_object,
		@cl_uint int value,
		@cl_ulong long offset,
		@AutoSize(value = "event_wait_list", canBeNull = true) @cl_uint int num_events,
		@Check(canBeNull = true) @Const @NativeType("cl_event") PointerBuffer event_wait_list,
		@OutParameter @Check(value = "1", canBeNull = true) @NativeType("cl_event") PointerBuffer event
	);

	// TODO: Implement
	int clEnqueueMakeBuffersResidentAMD();
	*/

}