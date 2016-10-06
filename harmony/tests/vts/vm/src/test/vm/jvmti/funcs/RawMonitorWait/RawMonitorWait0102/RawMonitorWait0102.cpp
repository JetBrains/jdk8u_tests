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
 * @version $Revision: 1.2 $
 *
 */

/* *********************************************************************** */

#include "events.h"
#include "utils.h"
#include "fake.h"
#include <math.h>

static bool test = false;
static bool util = false;

static jvmtiError result;

const char test_case_name[] = "RawMonitorWait0102";

/* *********************************************************************** */

static jrawMonitorID mntr;

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
    const char* name = "Tested Monitor";

    result = jvmti_env->CreateRawMonitor(name, &mntr);
    fprintf(stderr, "\tnative: CreateRawMonitor result = %d\n", result);
    fprintf(stderr, "\tnative: monitor name   is %s \n", name);
    fprintf(stderr, "\tnative: monitor        is %p \n", mntr);
    fflush(stderr);

    result = jvmti_env->RawMonitorWait(mntr, 10000);
    fprintf(stderr, "\tnative: RawMonitorWait result = %d (must be JVMTI_ERROR_NOT_MONITOR_OWNER (51))\n", result);
    if (result == JVMTI_ERROR_NOT_MONITOR_OWNER) test = true;

    result = jvmti_env->DestroyRawMonitor(mntr);
    fprintf(stderr, "\tnative: DestroyRawMonitor result = %d\n", result);

    util = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */


