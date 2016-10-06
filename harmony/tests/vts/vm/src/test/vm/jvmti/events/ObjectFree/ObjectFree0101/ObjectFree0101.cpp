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

static bool params = false;
static bool test = false;
static bool flag = false;
static long counter = 0;

/* *********************************************************************** */

/**
 * test of event ObjectFree
 */
void ObjectFree0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_objfree;
    cb_tstart;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_OBJECT_FREE, JVMTI_EVENT_THREAD_START,
        JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest ObjectFree0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackThreadStart(prms_THRD_START)
{
    check_THRD_START;

    if (flag) return;

    jvmtiThreadInfo tinfo;
    jvmtiError result;
    jvmtiPhase phase;

    result = jvmti_env->GetPhase(&phase);

    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;

    result = jvmti_env->GetThreadInfo(thread, &tinfo);

    if (!tinfo.name) {
        fprintf(stderr, "\tnative: GetThreadInfo: pointer to tinfo is NULL \n");
        return;
    }

    if (result != JVMTI_ERROR_NONE) return;

    if (strcmp(tinfo.name, "special_thread")) return;

    result = jvmti_env->SetTag(thread, 666);

    if (result != JVMTI_ERROR_NONE)
    {
        fprintf(stderr, "\tnative: SetTag: result is not ZERO: %d\n", result);
    }

    return;
}

void JNICALL callbackObjectFree(prms_OBJ_FREE)
{
    check_OBJ_FREE;

    if (flag) return;

    fprintf(stderr, "\tnative: ObjectFree (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: ObjectFree (prms) tag       is %lld\n", (long long)tag);
    fflush(stderr);

    counter++;

    if ((jvmti_env) && (tag == 666)) params = true;

    test = true;

    flag = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event ObjectFree0101                     : ");

    if ((counter > 0) && (params) && (test))
        fprintf(stderr, " passed \n");
    else
    {
        fprintf(stderr, " failed \n");

        fprintf(stderr, " counter %lld \n", (long long)counter);
        fprintf(stderr, " params %d \n", params);
        fprintf(stderr, " test %d \n", test);
    }

    fprintf(stderr, "\n} /* test ObjectFree0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */



