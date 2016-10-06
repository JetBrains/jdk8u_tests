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

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_tstart;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_THREAD_START, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest ResumeThreadList0101 is started\n{\n");
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
    jthread* threads;
    jint tcount;
    jthread my_thread = NULL;

    result = jvmti_env->GetPhase(&phase);
    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);
    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;
    result = jvmti_env->GetThreadInfo(thread, &tinfo);
    fprintf(stderr, "\tnative: GetThreadInfo result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return;
    fprintf(stderr, "\tnative: started thread name is %s (must be \"agent\") \n", tinfo.name);
    if (strcmp(tinfo.name, "agent")) return;
    fprintf(stderr, "\tnative: test is started\n");

    flag = true;
    util = true;

    result = jvmti_env->GetAllThreads(&tcount, &threads);
    fprintf(stderr, "\tnative: GetAllThreads result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return;

    for ( int i = 0; i < tcount; i++ )
        if (jvmti_env->GetThreadInfo(threads[i], &tinfo) == JVMTI_ERROR_NONE)
            if (!strcmp(tinfo.name, "SuperPuperTest"))
            {
                fprintf(stderr, "\tnative: tested thread was found\n");
                my_thread = threads[i];
                break;
            }

    if (!my_thread)
    {
        fprintf(stderr, "\tnative: tested thread is NULL. Test stopped.\n");
        return;
    }

    result = jvmti_env->SuspendThread(my_thread);
    fprintf(stderr, "\tnative: SuspendThread result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return;
    jint state = 0;
    result = jvmti_env->GetThreadState(my_thread, &state);
    fprintf(stderr, "\tnative: GetThreadState result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: state of tested thread is %d \n", state);
    fprintf(stderr, "\tnative: state of tested thread is ");
    thread_state_output_as_string(state);
    fprintf(stderr, "\n"); fflush(stderr);
    if ( result != JVMTI_ERROR_NONE) return;
    int ethalon_state = JVMTI_THREAD_STATE_ALIVE |
            JVMTI_THREAD_STATE_RUNNABLE;
    result = jvmti_env->ResumeThread(my_thread);
    fprintf(stderr, "\tnative: ResumeThread result = %d (must be zero) \n", result);
    if ( result != JVMTI_ERROR_NONE) return;
    result = jvmti_env->GetThreadState(my_thread, &state);
    fprintf(stderr, "\tnative: GetThreadState result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: state of tested thread is %d \n", state);
    fprintf(stderr, "\tnative: state of tested thread is ");
    thread_state_output_as_string(state);
    fprintf(stderr, "\n"); fflush(stderr);
    if (state == ethalon_state) test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of function ResumeThread0101                : ");

    if (test && util)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test ResumeThread0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */




