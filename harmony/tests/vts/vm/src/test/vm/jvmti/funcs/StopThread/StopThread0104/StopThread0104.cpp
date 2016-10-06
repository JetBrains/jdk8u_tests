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

static bool test = false;
static bool util = false;
static bool flag = false;

static jobject stored_exception = NULL;

const char test_case_name[] = "StopThread0104";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_THREAD_START, JVMTI_EVENT_EXCEPTION,
        JVMTI_EVENT_VM_DEATH };
    cb_exc;
    cb_tstart;
    cb_death;
    return func_for_Agent_OnLoad(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT);
}

/* *********************************************************************** */

void JNICALL callbackException(prms_EXCPT)
{
    check_EXCPT;
    if (flag) return;
    char* signature;
    char* generic;
    jvmtiError result;

    /*
     * Function separate all other exceptions in all other method
     */
    if (!check_phase_and_method_debug(jvmti_env, method, SPP_LIVE_ONLY,
                "special_method", DEBUG_OUT)) return;

    if (!stored_exception)
    {
        stored_exception = exception;
        fprintf(stderr, "\tnative: exception is %p \n", exception);
        util = true;
        return;
    }
}

void JNICALL callbackThreadStart(prms_THRD_START)
{
    check_THRD_START;

    if (flag) return;

    jvmtiPhase phase;
    jvmtiThreadInfo tinfo;
    jvmtiError result;
    jint tcount;
    jthread* threads;
    jthread my_thread = (jthread)jvmti_env;
    jint state = 0;
    long ethalon = JVMTI_THREAD_STATE_ALIVE | JVMTI_THREAD_STATE_RUNNABLE | JVMTI_THREAD_STATE_INTERRUPTED;

    /*
     * Function separate all other exceptions in all other method
     */
    if (!check_phase_and_thread_debug(jvmti_env, thread, SPP_LIVE_ONLY,
                "agent", DEBUG_OUT)) return;

    fprintf(stderr, "\tnative: test started\n");
    flag = true;

    if (!util) return;

    result = jvmti_env->StopThread(create_not_alive_thread(jni_env, jvmti_env),
            stored_exception);

    fprintf(stderr, "\tnative: StopThread result = %d (must be JVMTI_ERROR_THREAD_NOT_ALIVE (15)) \n", result);

    if (result == JVMTI_ERROR_THREAD_NOT_ALIVE) test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}
/* *********************************************************************** */
