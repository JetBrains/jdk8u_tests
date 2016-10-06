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
static bool even = false;

/* *********************************************************************** */

/**
 * test of function NotifyFramePop
 */
void NotifyFramePop0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_frpop;
    cb_exc;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_EXCEPTION, JVMTI_EVENT_VM_DEATH,
        JVMTI_EVENT_FRAME_POP };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest NotifyFramePop0101 is started\n{\n");
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
    jvmtiCapabilities caps;

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

    flag = true;
    util = true;

    result = jvmti_env->GetCapabilities(&caps);
    fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: capabilities ptr is %p (must be non-zero) \n", &caps);
    fprintf(stderr, "\tnative: can_generate_frame_pop_events is %d (must be non-zero) \n",
            caps.can_generate_frame_pop_events);

    if (result != JVMTI_ERROR_NONE) return;

    result = jvmti_env->NotifyFramePop(thread, 1);
    fprintf(stderr, "\tnative: NotifyFramePop result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: thread ptr is %p (must be non-zero) \n", thread);

    if ( result == JVMTI_ERROR_NONE ) test = true;
}

/* *********************************************************************** */

void JNICALL callbackFramePop(prms_FRM_POP)
{
    check_FRM_POP;

    even = true;
    fprintf(stderr, "\tnative: Event was  C A L L E D !!!\n");
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of function NotifyFramePop0101              : ");

    if (test && util && even)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test NotifyFramePop0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */

