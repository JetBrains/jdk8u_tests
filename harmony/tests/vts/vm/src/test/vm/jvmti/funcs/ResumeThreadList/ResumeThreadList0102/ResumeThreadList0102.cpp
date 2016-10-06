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
 * @version $Revision $
 *
 */

/* *********************************************************************** */

#include "events.h"
#include "utils.h"
#include "fake.h"

//two flag is enough
static bool testStarted = false;
static bool successful = false;

/* *********************************************************************** */

/**
 * test of function ResumeThreadList
 */
void ResumeThreadList0102()
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
    fprintf(stderr, "\ntest ResumeThreadList0102 is started\n{\n");
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
    int ethalon_state = JVMTI_THREAD_STATE_ALIVE | JVMTI_THREAD_STATE_RUNNABLE;

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
    jint state[3];
    jvmtiError results[3] = { JVMTI_ERROR_NONE, JVMTI_ERROR_NONE, JVMTI_ERROR_NONE};

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
    }

    { /*** Turn OFF capability ***/
        jvmtiCapabilities zero_caps;
        jvmtiError result;
        make_caps_zero(&zero_caps);
        zero_caps.can_suspend = 1;
        result = jvmti_env->RelinquishCapabilities(&zero_caps);
        if (result != JVMTI_ERROR_NONE)
        {
            fprintf(stderr,
                "Error during RelinquishCapabilities. Test Stopped\n");
            fflush(stderr);
            return;
        }
    }

     //useless
		/*{ Check of capability 
        jvmtiCapabilities full_caps;
        jvmtiCapabilities caps;
        make_caps_set_all(&full_caps);
        jvmtiError result = jvmti_env->GetCapabilities(&caps);
        if (result != JVMTI_ERROR_NONE)
        {
            fprintf(stderr,
                "Error during GetCapabilities. Test Stopped\n");
            fflush(stderr);
            return;
        }
        
		int diff = cmp_caps_and_output_results(full_caps, caps,"potential", "current");
		}*/
    
    fprintf(stderr, "\tnative: ResumeThreadList w/o capability \n");
    result = jvmti_env->ResumeThreadList(3, my_threads, results);
    fprintf(stderr, "\tnative: ResumeThreadList result = %d (must be JVMTI_ERROR_MUST_POSSESS_CAPABILITY (99)) \n", result);
    fflush(stderr);
    if ( result != JVMTI_ERROR_MUST_POSSESS_CAPABILITY) return;

    successful = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of function ResumeThreadList0102           : ");

    if (testStarted && successful)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test ResumeThreadList0102 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */


