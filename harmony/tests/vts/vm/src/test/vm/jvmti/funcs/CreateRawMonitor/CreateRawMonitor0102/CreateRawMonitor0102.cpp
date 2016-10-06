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
 * @author Valentin Al. Sitnick
 * @version $Revision: 1.1 $
 *
 */

/* *********************************************************************** */

#include "events.h"
#include "utils.h"
#include "fake.h"

static bool test = false;
static bool util = false;
static bool flag = false;

const char test_case_name[] = "CreateRawMonitor0102";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_VM_INIT, JVMTI_EVENT_VM_DEATH };
    cb_init;
    cb_death;
    return func_for_Agent_OnLoad(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT);
}

/* *********************************************************************** */

void JNICALL callbackVMInit(prms_VMINIT)
{
    check_VMINIT;

    jrawMonitorID monitor = NULL;
    jvmtiError result;
    const char* name = "Tested Monitor";

    util = true;

    result = jvmti_env->CreateRawMonitor(NULL, &monitor);
    fprintf(stderr, "\tnative: CreateRawMonitor result = %d (must be JVMTI_ERROR_NULL_POINTER (100)) \n", result);
    fflush(stderr);
    if (result == JVMTI_ERROR_NULL_POINTER) test = true;

    result = jvmti_env->DestroyRawMonitor(monitor);
    fprintf(stderr, "\tnative: DestroyRawMonitor result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: name is %s \n", name);
    fprintf(stderr, "\tnative: monitor is %p \n", monitor);
    fflush(stderr);
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

