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
static long counter = 0;

/* *********************************************************************** */

/**
 * test of event VMDeath
 */
void VMDeath0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest VMDeath0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    jvmtiPhase phase;
    jvmtiError result;

    counter++;

    result = jvmti_env->GetPhase(&phase);

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);

    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;

    fprintf(stderr, "\tnative: VMDeath (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: VMDeath (prms) jni_env is %p\n", jni_env);
    fprintf(stderr, "\tnative: VMDeath counter is %lld\n",  (long long)counter);
    fflush(stderr);
    fprintf(stderr, "\n\tTest of event VMDeath0101                       : ");

    if (jvmti_env && jni_env)
        params = true;

    if ((counter == 1) && (params))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test VMDeath0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */



