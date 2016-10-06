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

static bool test_phase_onload = false;
static bool test_phase_init   = false;
static bool test_phase_start  = false;
static bool test_phase_live   = false;

static jvmtiPhase phase_onload;
static jvmtiPhase phase_init;
static jvmtiPhase phase_start;
static jvmtiPhase phase_live;

static jvmtiError err_phase_onload = JVMTI_ERROR_NONE;
static jvmtiError err_phase_init   = JVMTI_ERROR_NONE;
static jvmtiError err_phase_start  = JVMTI_ERROR_NONE;
static jvmtiError err_phase_live   = JVMTI_ERROR_NONE;

const char test_case_name[] = "GetPhase0101";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_VM_INIT, JVMTI_EVENT_VM_START, JVMTI_EVENT_VM_DEATH };
    cb_start;
    cb_init;
    cb_death;
    jvmtiEnv *jvmti;
    jvmtiError result;
    jint res = func_for_Agent_OnLoad_JVMTI(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT, &jvmti);

    err_phase_onload = jvmti->GetPhase(&phase_onload);
    fprintf(stderr, "\tnative: GetPhase (onload) result = %d (must be zero) \n", err_phase_onload);
    fprintf(stderr, "\tnative: phase_onload is %d \n", phase_onload);
    fflush(stderr);

    if ((err_phase_onload == JVMTI_ERROR_NONE) &&
            (phase_onload == JVMTI_PHASE_ONLOAD))
        test_phase_onload = true;

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackVMInit(prms_VMINIT)
{
    check_VMINIT;

    err_phase_init = jvmti_env->GetPhase(&phase_init);
    fprintf(stderr, "\tnative: GetPhase (init) result = %d (must be zero) \n", err_phase_init);
    fprintf(stderr, "\tnative: phase_init is %d \n", phase_init);
    if ((err_phase_init == JVMTI_ERROR_NONE) &&
            (phase_init == JVMTI_PHASE_LIVE))
        test_phase_init = true;
}

void JNICALL callbackVMStart(prms_VMSTART)
{
    check_VMSTART;

    err_phase_start = jvmti_env->GetPhase(&phase_start);
    fprintf(stderr, "\tnative: GetPhase (start) result = %d (must be zero) \n", err_phase_start);
    fprintf(stderr, "\tnative: phase_start is %d \n", phase_start);
    if ((err_phase_start == JVMTI_ERROR_NONE) &&
            (phase_start == JVMTI_PHASE_START))
        test_phase_start = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    err_phase_live = jvmti_env->GetPhase(&phase_live);
    fprintf(stderr, "\tnative: GetPhase (death) result = %d (must be zero) \n", err_phase_live);
    fprintf(stderr, "\tnative: phase_live is %d \n", phase_live);
    if ((err_phase_live == JVMTI_ERROR_NONE) &&
            (phase_live == JVMTI_PHASE_LIVE))
        test_phase_live = true;

    util = true;

    if (test_phase_onload &&
            test_phase_init &&
            test_phase_start &&
            test_phase_live ) test = true;

    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

