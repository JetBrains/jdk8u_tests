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

/* *********************************************************************** */

/**
 * test of event MonitorWaited
 */
void MonitorWaited0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_mwaited;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_MONITOR_WAITED, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest MonitorWaited0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackMonitorWaited(prms_MON_WAITED)
{
    check_MON_WAITED;

    jvmtiPhase phase;
    jvmtiError result;

    test = true;

    result = jvmti_env->GetPhase(&phase);

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);

    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;

    fprintf(stderr, "\tnative: MonitorWaited (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: MonitorWaited (prms) jni_env   is %p\n", jni_env);
    fprintf(stderr, "\tnative: MonitorWaited (prms) thread    is %p\n", thread);
    fprintf(stderr, "\tnative: MonitorWaited (prms) object    is %p\n", object);
    fprintf(stderr, "\tnative: MonitorWaited (prms) timed_out is %d\n", timed_out);
    fflush(stderr);

    params = true;

    test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event MonitorWaited0101                    : ");

    if ((params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test MonitorWaited0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */


