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

const char test_case_name[] = "SetVerboseFlag0101";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = {JVMTI_EVENT_VM_DEATH };
    cb_death;
    jvmtiEnv *jvmti;
    jvmtiError result;
    jint count_test = 0;
    jint res = func_for_Agent_OnLoad_JVMTI(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT, &jvmti);

    util = true;

    result = jvmti->SetVerboseFlag(JVMTI_VERBOSE_OTHER, true);
    fprintf(stderr, "\tnative: SetVerboseFlag [0] result = %d (must be zero) \n", result);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) count_test++;
    result = jvmti->SetVerboseFlag(JVMTI_VERBOSE_GC, true);
    fprintf(stderr, "\tnative: SetVerboseFlag [1] result = %d (must be zero) \n", result);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) count_test++;
    result = jvmti->SetVerboseFlag(JVMTI_VERBOSE_CLASS, true);
    fprintf(stderr, "\tnative: SetVerboseFlag [2] result = %d (must be zero) \n", result);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) count_test++;
    result = jvmti->SetVerboseFlag(JVMTI_VERBOSE_JNI, true);
    fprintf(stderr, "\tnative: SetVerboseFlag [3] result = %d (must be zero) \n", result);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) count_test++;
    result = jvmti->SetVerboseFlag(JVMTI_VERBOSE_OTHER, false);
    fprintf(stderr, "\tnative: SetVerboseFlag [4] result = %d (must be zero) \n", result);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) count_test++;
    result = jvmti->SetVerboseFlag(JVMTI_VERBOSE_GC, false);
    fprintf(stderr, "\tnative: SetVerboseFlag [5] result = %d (must be zero) \n", result);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) count_test++;
    result = jvmti->SetVerboseFlag(JVMTI_VERBOSE_CLASS, false);
    fprintf(stderr, "\tnative: SetVerboseFlag [6] result = %d (must be zero) \n", result);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) count_test++;
    result = jvmti->SetVerboseFlag(JVMTI_VERBOSE_JNI, false);
    fprintf(stderr, "\tnative: SetVerboseFlag [7] result = %d (must be zero) \n", result);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) count_test++;

    if (count_test == 0) test = true;

    return JNI_OK;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

