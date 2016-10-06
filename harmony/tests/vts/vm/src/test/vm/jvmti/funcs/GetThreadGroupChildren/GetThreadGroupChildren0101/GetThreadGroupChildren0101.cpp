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
 * test of function GetThreadGroupChildren
 */
void GetThreadGroupChildren0101()
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
    fprintf(stderr, "\ntest GetThreadGroupChildren0101 is started\n{\n");
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
    jint group_counter;
    jthreadGroup* group_ptr;
    jint thread_counter;
    jthread* thread_ptr;
    jthread* threads;
    jint tcount;

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
    util = true;

    result = jvmti_env->GetAllThreads(&tcount, &threads);
    fprintf(stderr, "\tnative: GetAllThreads result = %d (must be zero) \n", result);

    if (result != JVMTI_ERROR_NONE) return;

    for (int i = 0; i < tcount; i++)
    {
        result = jvmti_env->GetThreadInfo(threads[i], &tinfo);
        fprintf(stderr, "\tnative: GetThreadInfo result = %d (must be zero) \n", result);
        fprintf(stderr, "\tnative: current thread name is %s (must be zero) \n", tinfo.name);
        if (result != JVMTI_ERROR_NONE) continue;
        if (strcmp(tinfo.name, "FirstThreadGroup_FirstThread")) continue;
        result = jvmti_env->GetThreadGroupChildren( tinfo.thread_group,
                &thread_counter, &thread_ptr, &group_counter, &group_ptr);
        fprintf(stderr, "\tnative: GetThreadInfo result = %d (must be zero) \n",
                result);
        fprintf(stderr, "\tnative: tinfo.thread_group is %p \n",
                tinfo.thread_group);
        fprintf(stderr, "\tnative: thread_counter is %d (must be 3) \n",
                thread_counter);
        fprintf(stderr, "\tnative: thread_ptr is %p \n",
                thread_ptr);
        fprintf(stderr, "\tnative: group_counter is %d (must be 0)\n",
                group_counter);
        fprintf(stderr, "\tnative: group_ptr is %p \n",
                group_ptr);

        if (result != JVMTI_ERROR_NONE) return;
        if ((thread_counter != 3) || (group_counter != 0)) return;

        test = true;
    }
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\nTest of function GetThreadGroupChildren0101      : ");

    if (test && util)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test GetThreadGroupChildren0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */

