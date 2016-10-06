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
 * test of event FramePop
 */
void FramePop0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_frpop;
    cb_mentry;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_METHOD_ENTRY, JVMTI_EVENT_FRAME_POP,
        JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest FramePop0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackMethodEntry(prms_METHOD_ENTRY)
{
    check_METHOD_ENTRY;

    if (flag) return;

    char* name = NULL;
    char* signature = NULL;
    char* generic = NULL;
    jvmtiError result;
    jvmtiPhase phase;

    result = jvmti_env->GetPhase(&phase);

    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) {
        fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
        fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);

        flag = true;
        return;
    }

    result = jvmti_env->GetMethodName(method, &name, &signature, &generic);

    if (result != JVMTI_ERROR_NONE) return;
    if (strcmp(name, "special_method")) return;

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);
    fprintf(stderr, "\tnative: GetMethodName result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: method name is %s \n", name);
    fprintf(stderr, "\tnative: signature name is %s \n", signature);
    fprintf(stderr, "\tnative: generic name is %s \n", generic);
    fflush(stderr);

    result = jvmti_env->NotifyFramePop(thread, 1);

    flag = true;

    fprintf(stderr, "\tnative: NotifyFramePop result = %d (must be zero) \n", result);
    fflush(stderr);
}

void JNICALL callbackFramePop(prms_FRM_POP)
{
    check_FRM_POP;

    params = true;
    test = true;
    counter++;

    fprintf(stderr, "\tnative: FramePop (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: FramePop (prms) jni_env   is %p\n", jni_env);
    fprintf(stderr, "\tnative: FramePop (prms) thread    is %p\n", thread);
    fprintf(stderr, "\tnative: FramePop (prms) method    is %p\n", method);
    fprintf(stderr, "\tnative: FramePop (prms) was_popped_by_exception is %d\n",
            was_popped_by_exception);

    fflush(stderr);
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event FramePop0101                       : ");

    if ((counter > 0) && (params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test FramePop0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */



