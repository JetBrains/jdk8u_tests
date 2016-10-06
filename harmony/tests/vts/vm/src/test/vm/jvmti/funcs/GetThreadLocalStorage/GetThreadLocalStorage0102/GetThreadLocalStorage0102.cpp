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

/* *********************************************************************** */

/**
 * test of function GetThreadLocalStorage
 */
void GetThreadLocalStorage0102()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;

    cb_tend;
    cb_tstart;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_THREAD_START,
        JVMTI_EVENT_THREAD_END, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest GetThreadLocalStorage0102 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackThreadStart(prms_THRD_START)
{
    check_THRD_START;

    if (flag) return;

    jvmtiPhase phase;
    jvmtiThreadInfo tinfo;
    jvmtiError result;
    unsigned char* data = NULL;

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
    result = jvmti_env->Allocate(1024, &data);
    if (result != JVMTI_ERROR_NONE) return;
    util = true;
    result = jvmti_env->SetThreadLocalStorage(thread, (void*)data);
    fprintf(stderr, "\tnative: SetThreadLocalStorage result = %d (must be zero) \n",
            result);
    fprintf(stderr, "\tnative: Thread Local Storage ptr (set) is %p (must be NON-zero) \n",
            data);
    if (result != JVMTI_ERROR_NONE) return;
}

void JNICALL callbackThreadEnd(prms_THRD_END)
{
    check_THRD_END;

    jvmtiPhase phase;
    jvmtiThreadInfo tinfo;
    jvmtiError result;
    void* data = NULL;
    jthread thread_x = (jthread)jvmti_env;

    result = jvmti_env->GetPhase(&phase);
    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);
    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;
    result = jvmti_env->GetThreadInfo(thread, &tinfo);
    fprintf(stderr, "\tnative: GetThreadInfo result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current thread name is %s (must be zero) \n", tinfo.name);
    if (result != JVMTI_ERROR_NONE) return;
    if (strcmp(tinfo.name, "agent")) return;


    fprintf(stderr, "\tnative: GetThreadLocalStorage with INVALID thread \n");
    result = jvmti_env->GetThreadLocalStorage(thread_x, &data);
    fprintf(stderr, "\tnative: GetThreadLocalStorage result = %d (must be JVMTI_ERROR_INVALID_THREAD (10)) \n", result);
    if (result != JVMTI_ERROR_INVALID_THREAD) return;

    test = true;
    result = jvmti_env->Deallocate((unsigned char*)data);
    fprintf(stderr, "\tnative: Deallocate result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of function GetThreadLocalStorage0102            : ");

    if (test && util)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test GetThreadLocalStorage0102 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */

