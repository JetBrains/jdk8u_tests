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
 * test of function ResumeThread
 */
void ResumeThread0102()
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
    fprintf(stderr, "\ntest ResumeThread0102 is started\n{\n");
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

//    result = jvmti_env->SuspendThread(my_thread);
  //  fprintf(stderr, "\tnative: SuspendThread result = %d (must be zero) \n", result);
    //if (result != JVMTI_ERROR_NONE) return;
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

    { /*** Check of capability ***/
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
        int diff = cmp_caps_and_output_results(full_caps, caps,
            "potential", "current");
    }

    fprintf(stderr, "\tnative: ResumeThread w/o capability \n");
    fflush(stderr);

    result = jvmti_env->ResumeThread(my_thread);

    fprintf(stderr, "\tnative: ResumeThread result = %d (must be JVMTI_ERROR_MUST_POSSESS_CAPABILITY (99)) \n", result);
    fflush(stderr);

    if ( result != JVMTI_ERROR_MUST_POSSESS_CAPABILITY) return;

    test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of function ResumeThread0102                : ");

    if (test && util)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test ResumeThread0102 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */




