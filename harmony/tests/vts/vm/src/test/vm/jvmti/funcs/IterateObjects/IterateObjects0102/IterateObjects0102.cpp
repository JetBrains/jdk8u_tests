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

static jvmtiIterationControl JNICALL func1(jvmtiHeapRootKind, jlong, jlong, jlong*,
        void*);

static jvmtiIterationControl JNICALL func2(jvmtiHeapRootKind, jlong, jlong, jlong*,
        jlong, jint, jmethodID, jint, void*);

static jvmtiIterationControl JNICALL func3(jvmtiObjectReferenceKind, jlong, jlong,
        jlong*, jlong, jint, void*);

/* *********************************************************************** */

/**
 * test of function IterateOverReachableObjects0101
 */
void IterateObjects0101()
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

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_EXCEPTION, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest IterateObjects0101 is started\n{\n");
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
    util = true;

    { /* block for turning off can_tag_objects capability */
        jvmtiCapabilities zero_caps;
        jvmtiError result;

        fprintf(stderr, "\tnative: make ZERO caps <--- ");
        fflush(stderr);
        make_caps_zero(&zero_caps);
        fprintf(stderr, "finish --->\n");
        fflush(stderr);

        zero_caps.can_tag_objects = 1;

        result = jvmti_env->RelinquishCapabilities(&zero_caps);
        fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
    }

    jvmtiHeapRootCallback heap_root_callback = func1;
    jvmtiStackReferenceCallback stack_ref_callback = func2;
    jvmtiObjectReferenceCallback object_ref_callback = func3;
    void* user_data = NULL;

    result = jvmti_env->IterateOverReachableObjects(
            heap_root_callback, stack_ref_callback, object_ref_callback,
            user_data);
    fprintf(stderr, "\tnative: IterateOverReachableObjects result = %d (must be JVMTI_ERROR_MUST_POSSESS_CAPABILITY (99)) \n", result);

    if (result != JVMTI_ERROR_MUST_POSSESS_CAPABILITY) return;

    test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of function IterateOverReachableObjects0101  : ");

    if (test && util)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test IterateObjects0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */

jvmtiIterationControl JNICALL func1(jvmtiHeapRootKind, jlong, jlong, jlong*, void*)
{
/**/
    return (jvmtiIterationControl)NULL;
}

jvmtiIterationControl JNICALL func2(jvmtiHeapRootKind, jlong, jlong, jlong*,
        jlong, jint, jmethodID, jint, void*)
{
/**/
    return (jvmtiIterationControl)NULL;
}

jvmtiIterationControl JNICALL func3(jvmtiObjectReferenceKind, jlong, jlong,
        jlong*, jlong, jint, void*)
{
/**/
    return (jvmtiIterationControl)NULL;
}

/* *********************************************************************** */





