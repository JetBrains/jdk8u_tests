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

static bool testStarted = false;
//check with ethalon_state depends on it
static bool successful = true; 

/* *********************************************************************** */

/**
 * test of function SuspendThreadList
 */
void SuspendThreadList0101()
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
    fprintf(stderr, "\ntest SuspendThreadList0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackThreadStart(prms_THRD_START)
{
    check_THRD_START;

    if (testStarted) return;

    jvmtiPhase phase;
    jvmtiThreadInfo tinfo;
    jint tcount = 0;
    jint ind = 0;
    int i = 0, j = 0;
    jthread* threads;
    jthread my_threads[3] = {NULL, NULL, NULL};
    jvmtiError result;
    jint state[3];

    int ethalon_state = JVMTI_THREAD_STATE_SUSPENDED;

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

    testStarted = true;


    result = jvmti_env->GetAllThreads(&tcount, &threads);
    fprintf(stderr, "\tnative: GetAllThreads result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return;
    for ( i = 0; i < tcount; i++ )
        if (jvmti_env->GetThreadInfo(threads[i], &tinfo) == JVMTI_ERROR_NONE)
            if (!strncmp(tinfo.name, "SuperPuperTest", 14))
            {
                my_threads[ind] = threads[i];
                if ((ind++) == 4) return;
            }

    if (ind != 3) return;

    fprintf(stderr, "\tnative: all tested threads were found \n");
    ind = 0;
    jvmtiError results[3] = { JVMTI_ERROR_NONE, JVMTI_ERROR_NONE, JVMTI_ERROR_NONE};
    result = jvmti_env->SuspendThreadList(3, my_threads, results);
    fprintf(stderr, "\tnative: SuspendThreadList result = %d (must be zero) \n", result);
    for ( i = 0; i < 3; i++)
        fprintf(stderr, "\tnative: result[%d] = %d (must be zero) \n", i, results[i]);

    fflush(stderr);

    if (result != JVMTI_ERROR_NONE) return;

    for ( j = 0; j < 3; j++ )
    {
		if (results[j] != JVMTI_ERROR_NONE){
			jvmti_env->GetThreadState(my_threads[j], &(state[j]));		
			continue;
		}
        result = jvmti_env->GetThreadState(my_threads[j], &(state[j]));
        fprintf(stderr, "\tnative: GetThreadState result = %d (must be zero) \n", result);
		if (result != JVMTI_ERROR_NONE) continue;
        fprintf(stderr, "\tnative: state of thread %d is %d\n", j, state[j]);
        fprintf(stderr, "\tnative: state of thread %d is ", j);
        thread_state_output_as_string(state[j]);
        fprintf(stderr, "\n"); fflush(stderr);
//compare with ethalon state	
	successful = (state[j]&ethalon_state)&&successful;
    }

    //if ((state[0] & ethalon_state) && (state[1] & ethalon_state) && (state[2] & ethalon_state)) successful = true;
    if (jvmti_env->ResumeThreadList(successful, my_threads, results) ==
            JVMTI_ERROR_NONE) return;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of function SuspendThreadList0101           : ");
    fflush(stderr);

    if (testStarted && successful)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test SuspendThreadList0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */


