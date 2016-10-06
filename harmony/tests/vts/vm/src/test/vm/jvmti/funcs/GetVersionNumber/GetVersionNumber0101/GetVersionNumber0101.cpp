/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Maxim V. Makarov, Valentin Al. Sitnick
 * @version $Revision: 1.2 $
 *
 */

/* *********************************************************************** */

#include "events.h"
#include "utils.h"
#include "fake.h"

static bool test = false;
static bool util = false;


const char test_case_name[] = "GetVersionNumber0101";

/* *********************************************************************** */


void JNICALL callbackVMStart(prms_VMSTART)
{

    jvmtiError result;

	jint rt_ver;                  // runtime jvmti version
    jint ct_ver  = JVMTI_VERSION; // (compile version, from JVMTI.H)

    jint ct_major, rt_major;
    jint ct_minor, rt_minor;
    jint ct_micro, rt_micro; 	// Runtime micro version may be lagger then a compile micro version

	jint ct_type, rt_type;


    result = jvmti_env->GetVersionNumber(&rt_ver);

    if (result != JVMTI_ERROR_NONE) return;


    ct_type = (ct_ver & JVMTI_VERSION_MASK_INTERFACE_TYPE);
	rt_type = (rt_ver & JVMTI_VERSION_MASK_INTERFACE_TYPE);

	if (ct_type != JVMTI_VERSION_INTERFACE_JVMTI || rt_type != JVMTI_VERSION_INTERFACE_JVMTI) 
	{
		fprintf(stderr, "\tnative: Versions are not type of JVMTI %d\t%d\n", ct_type, rt_type);
	    fflush(stderr);
		return;
	}

    ct_major = (ct_ver & JVMTI_VERSION_MASK_MAJOR) >> JVMTI_VERSION_SHIFT_MAJOR;
    rt_major = (rt_ver & JVMTI_VERSION_MASK_MAJOR) >> JVMTI_VERSION_SHIFT_MAJOR;

    ct_minor = (ct_ver & JVMTI_VERSION_MASK_MINOR) >> JVMTI_VERSION_SHIFT_MINOR;
	rt_minor = (rt_ver & JVMTI_VERSION_MASK_MINOR) >> JVMTI_VERSION_SHIFT_MINOR;

    ct_micro = (ct_ver & JVMTI_VERSION_MASK_MICRO) >> JVMTI_VERSION_SHIFT_MICRO;
	rt_micro = (rt_ver & JVMTI_VERSION_MASK_MICRO) >> JVMTI_VERSION_SHIFT_MICRO;


	if (ct_major != rt_major || ct_minor != rt_minor || ct_micro > rt_micro)  
	{
	    fprintf(stderr, "\tnative: GetVersionNumber result = %d (must be zero) \n", result);
	    fprintf(stderr, "\tnative: Runtime version and compile version are incompatible\n");
	    fprintf(stderr, "\tnative: Runtime version: %d.%d.%d\n", rt_major, rt_minor, rt_micro);
	    fprintf(stderr, "\tnative: Compile version: %d.%d.%d\n", ct_major, ct_minor, ct_micro);
	    fflush(stderr);
		return;

	}


	test = true;
	util = true;

    fprintf(stderr, "\tnative: GetVersionNumber result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: Runtime version: %d.%d.%d\n", rt_major, rt_minor, rt_micro);
    fprintf(stderr, "\tnative: Compile version: %d.%d.%d\n", ct_major, ct_minor, ct_micro);

}

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_VM_START, JVMTI_EVENT_VM_DEATH };

    cb_start;
    cb_death;
    return func_for_Agent_OnLoad(vm, options, reserved, &CB, events,
            sizeof(events)/4, test_case_name, DEBUG_OUT);
}



void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

