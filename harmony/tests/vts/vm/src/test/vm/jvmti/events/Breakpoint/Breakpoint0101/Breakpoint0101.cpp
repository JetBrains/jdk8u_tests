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
 * @version $Revision: 1.2 $
 *
 */

/* *********************************************************************** */

#include "events.h"
#include "utils.h"
#include "fake.h"

static bool flag = false;
static bool params = false;
static jint counter = 0;

static bool execution_stopped = false;

/* *********************************************************************** */

/**
 * test of event Breakpoint
 */
void Breakpoint0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_exc;
    cb_death;
    cb_brk;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_EXCEPTION,
        JVMTI_EVENT_VM_DEATH, JVMTI_EVENT_BREAKPOINT };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest Breakpoint0101 is started\n{\n");
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

    jlocation loc = 14;      /* Value was got as a result of experiment with
                                decoding ta_SetBreakpoint.class by jcf-dump */

    result = jvmti_env->SetBreakpoint(method, loc);
    fprintf(stderr, "\tnative: SetBreakpoint result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: methodID is %p \n", method);
    fprintf(stderr, "\tnative: breakpoint location is %lld \n", loc);
}

void JNICALL callbackBreakpoint(prms_BRKPOINT)
{
    check_BRKPOINT;

    execution_stopped = true;
    counter++;
    params = true;

    fprintf(stderr, "\tnative: Breakpoint (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: Breakpoint (prms) jni_env   is %p\n", jni_env);
    fprintf(stderr, "\tnative: Breakpoint (prms) thread    is %p\n", thread);
    fprintf(stderr, "\tnative: Breakpoint (prms) method    is %p\n", method);
    fprintf(stderr, "\tnative: Breakpoint (prms) location  is %lld\n", location);
    fflush(stderr);

    return;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event Breakpoint0101                     : ");

    if ((counter == 1) && (params) && (execution_stopped))
        fprintf(stderr, " passed \n");
    else
    {
        fprintf(stderr, " failed \n");
    }

    fprintf(stderr, "\n} /* test Breakpoint0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */




