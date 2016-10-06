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
static long counter = 0;

/* *********************************************************************** */

/**
 * test of event SingleStep
 */
void SingleStep0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_step;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_SINGLE_STEP, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest SingleStep0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackSingleStep(prms_SINGLE_STEP)
{
	jvmtiCapabilities pc1;

    check_SINGLE_STEP;

    if (counter >= 5) { 
		jvmti_env->SetEventNotificationMode(JVMTI_DISABLE, JVMTI_EVENT_SINGLE_STEP, NULL);
    	return;
    }

    jvmtiError result;
    jvmtiPhase phase;

    counter ++;

    fprintf(stderr, "\tnative: SingleStep (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: SingleStep (prms) jni_env   is %p\n", jni_env);
    fprintf(stderr, "\tnative: SingleStep (prms) thread    is %p\n", thread);
    fprintf(stderr, "\tnative: SingleStep (prms) method    is %p\n", method);
    fprintf(stderr, "\tnative: SingleStep (prms) location  is %lld\n", location);
    fflush(stderr);

    result = jvmti_env->GetPhase(&phase);

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (ONLY) _LIVE)\n", phase);

    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;

    test = true;

    params = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event SingleStep0101                     : ");

    if ((counter > 0) && (params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test SingleStep0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */

