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
 * test of function GetThreadState
 */
void GetThreadState0101()
{
    //Fake method for docletting only
}

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
    fprintf(stderr, "\ntest GetThreadState0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackThreadStart(prms_THRD_START)
{
    check_THRD_START;

    if (flag) return;

    jint thread_state;
    jvmtiPhase phase;
    jvmtiThreadInfo tinfo;
    jvmtiError result;

    result = jvmti_env->GetPhase(&phase);

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);

    if (result != JVMTI_ERROR_NONE) return;

    if (phase != JVMTI_PHASE_LIVE) return;

    result = jvmti_env->GetThreadInfo(thread, &tinfo);
    fprintf(stderr, "\tnative: GetThreadInfo result = %d (must be zero) \n", result);

    if (result != JVMTI_ERROR_NONE) return;

    fprintf(stderr,
            "\tnative: started thread name is %s (must be \"Thread_for_testing\") \n",
            tinfo.name);
    if (strcmp(tinfo.name, "Thread_for_testing")) return;

    fprintf(stderr, "\tnative: test is started\n");

    flag = true;

    result = jvmti_env->GetThreadState(thread, &thread_state);
    fprintf(stderr, "\n\tnative: GetThreadState with right thread \n");
    fprintf(stderr, "\tnative: GetThreadState result = %d (must be zero) \n", result);
    fflush(stderr);

    if ( result != JVMTI_ERROR_NONE) return;

    if (thread_state == 0x5) test = true;

    fprintf(stderr, "\tnative: thread state is %d (must be 0x5) \n", thread_state);
    fprintf(stderr, "\tnative: thread state is ");

    thread_state_output_as_string(thread_state);

    util = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of function GetThreadState0101              : ");

    if (test && util)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test GetThreadState0101 is finished */ \n\n");
    fflush(stderr);
}

/* *********************************************************************** */

