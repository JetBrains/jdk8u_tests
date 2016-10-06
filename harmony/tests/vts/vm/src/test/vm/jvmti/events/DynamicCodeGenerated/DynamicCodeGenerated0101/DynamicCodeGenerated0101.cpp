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
static bool flag_1 = false;
static bool flag_2 = false;

/* *********************************************************************** */

/**
 * test of event DynamicCodeGenerated
 */
void DynamicCodeGenerated0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_codegen;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_DYNAMIC_CODE_GENERATED,
        JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest DynamicCodeGenerated0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */
void JNICALL callbackException(prms_EXCPT)
{
    check_EXCPT;

    if (flag_1) return;

    char* name;
    char* signature;
    char* generic;
    jvmtiError result;
    jvmtiPhase phase;

    result = jvmti_env->GetPhase(&phase);
    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;
    result = jvmti_env->GetMethodName(method, &name, &signature, &generic);
    fprintf(stderr, "\tnative: GetMethodName result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: method name is %s \n", name);
    fprintf(stderr, "\tnative: signature name is %s \n", signature);
    fprintf(stderr, "\tnative: generic name is %s \n", generic);
    fflush(stderr);

    if (result != JVMTI_ERROR_NONE) return;
    if (strcmp(name, "special_method")) return;

    flag_1 = true;

    result = jvmti_env->GenerateEvents(JVMTI_EVENT_DYNAMIC_CODE_GENERATED);
    fprintf(stderr, "\tnative: GenerateEvents result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: JVMTI_EVENT_DYNAMIC_CODE_GENERATED \n");

    return;
}

void JNICALL callbackDynamicCodeGenerated(prms_DYN_CODE_GEN)
{
    check_DYN_CODE_GEN;

    if (flag_2) return;

    jvmtiError result;
    jvmtiPhase phase;

    result = jvmti_env->GetPhase(&phase);

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 2 (PRIMORDIAL-phase) or\n",
            phase);
    fprintf(stderr, "\tnative: \t 6 (START-phase) or 4 (LIVE-phase)) \n");
    fflush(stderr);

    if (result != JVMTI_ERROR_NONE) return;
    if ((phase != JVMTI_PHASE_PRIMORDIAL) &&
            (phase != JVMTI_PHASE_START) &&
            (phase != JVMTI_PHASE_LIVE)) {
        fprintf(stderr, "\tnative: INCORRECT PHASE for this event - test FAILED\n");
        fflush(stderr);
        return;
    }

    fprintf(stderr, "\tnative: DynamicCodeGenerated (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: DynamicCodeGenerated (prms) name      is %s\n", name);
    fprintf(stderr, "\tnative: DynamicCodeGenerated (prms) address   is %p\n", address);
    fprintf(stderr, "\tnative: DynamicCodeGenerated (prms) length    is %d\n", length);
    fflush(stderr);

    flag_2 = true;

    //this check must be fixed
    params = true;

    test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event DynamicCodeGenerated0101           : ");

    if ((params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test DynamicCodeGenerated0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */





