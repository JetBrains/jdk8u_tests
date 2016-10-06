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
 * test of event ClassPrepare
 */
void ClassPrepare0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_clprep;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_CLASS_PREPARE, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest ClassPrepare0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackClassPrepare(prms_CLS_PRPR)
{
    check_CLS_PRPR;

    if (flag) return;

    char* signature;
    char* generic;
    jvmtiPhase phase;
    jvmtiError result;

    result = jvmti_env->GetPhase(&phase);
    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 or 6 (LIVE-phase of START-phase)) \n", phase);
    if (result != JVMTI_ERROR_NONE) return;

    if ((phase != JVMTI_PHASE_LIVE) && (phase != JVMTI_PHASE_START))
    {
        fprintf(stderr, "\tnative: INCORRECT phase for this event. Test failed\n");
        flag = true;
        return;
    }

    result = jvmti_env->GetClassSignature(klass, &signature, &generic);

    if (strcmp(signature,
                "Lorg/apache/harmony/vts/test/vm/jvmti/ClassPrepare0101;"))
        return;

    flag = true;

    fprintf(stderr, "\tnative: ClassPrepare (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: ClassPrepare (prms) jni_env   is %p\n", jni_env);
    fprintf(stderr, "\tnative: ClassPrepare (prms) thread    is %p\n", thread);
    fprintf(stderr, "\tnative: ClassPrepare (prms) klass     is %p\n", klass);
    fflush(stderr);

    counter++;
    params = true;
    test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\nTest of event ClassPrepare0101                   : ");

    if ((counter == 1) && (params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test ClassPrepare0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */




