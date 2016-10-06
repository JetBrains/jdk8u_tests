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

/* *********************************************************************** */

/**
 * test of function StopThread
 */
void StopThread0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_exc;
    cb_tstart;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_THREAD_START, JVMTI_EVENT_EXCEPTION,
        JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest StopThread0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackException(prms_EXCPT)
{
    check_EXCPT;

    if (flag) return;

    char* name;
    char* signature;
    char* generic;
    jvmtiPhase phase;
    jvmtiError result;

    result = jvmti_env->GetPhase(&phase);
    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);
    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;
    result = jvmti_env->GetMethodName(method, &name, &signature, &generic);
    fprintf(stderr, "\tnative: GetMethodName result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: method name is %s \n", name);
    fprintf(stderr, "\tnative: signature name is %s \n", signature);
    fprintf(stderr, "\tnative: generic name is %s \n", generic);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) return;
    if (strcmp(name, "special_method")) return;

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
    jthread my_thread = NULL;
    long ethalon = JVMTI_THREAD_STATE_ALIVE | JVMTI_THREAD_STATE_RUNNABLE | JVMTI_THREAD_STATE_INTERRUPTED;

    /* Anti-Warnings code */
    if (jni_env == NULL)
        fprintf(stderr, "\tnative: callbackThreadStart was called with jni_env = NULL\n");

    result = jvmti_env->GetPhase(&phase);

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);
    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;
    result = jvmti_env->GetThreadInfo(thread, &tinfo);
    fprintf(stderr, "\tnative: GetThreadInfo result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current thread name is %s (must be zero) \n", tinfo.name);
    if (result != JVMTI_ERROR_NONE) return;
    if (strcmp(tinfo.name, "agent")) return;
    fprintf(stderr, "\tnative: test started\n");

    flag = true;

    if (!util) return;

    result = jvmti_env->GetAllThreads(&tcount, &threads);
    fprintf(stderr, "\tnative: GetAllThreads result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return;

    for ( int i = 0; i < tcount; i++ )
    {
        result = jvmti_env->GetThreadInfo(threads[i], &tinfo);
        fprintf(stderr, "\tnative: GetThreadInfo result = %d (must be zero) \n", result);
        fprintf(stderr, "\tnative: current thread name is %s (must be zero) \n", tinfo.name);
        if (result != JVMTI_ERROR_NONE) continue;
        if (strcmp(tinfo.name, "SuperPuperTest")) continue;
        my_thread = threads[i];
        fprintf(stderr, "\tnative: tested thread was found = %p\n", my_thread);
        break;
    }

    jint state = 0;

    if (!my_thread) return;
    result = jvmti_env->StopThread(my_thread, stored_exception);
    fprintf(stderr, "\tnative: StopThread result = %d (must be zero) \n", result);

    if ((int)result == 666)
        fprintf(stderr, "\n\tStopThread: FUNCTION IS NOT IMPLEMENTED NOW !!!!!");

    if (result != JVMTI_ERROR_NONE) return;
    result = jvmti_env->GetThreadState(my_thread, &state);
    fprintf(stderr, "\tnative: GetThreadState result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: state of tested thread is %d \n", state);
    fprintf(stderr, "\tnative: state of tested thread is ");
    thread_state_output_as_string(state);
    fprintf(stderr, "\n"); fflush(stderr);

    if (result != JVMTI_ERROR_NONE) return;
    if (state == ethalon) test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of function StopThread001                  : ");

    if (test && util)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test StopThread0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */
