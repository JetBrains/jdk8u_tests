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
 * test of event MonitorWait
 */
void MonitorWait0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_mwait;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_MONITOR_WAIT, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest MonitorWait0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackMonitorWait(prms_MON_WAIT)
{
    check_MON_WAIT;

    jvmtiPhase phase;
    jvmtiError result;

    result = jvmti_env->GetPhase(&phase);

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);

    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;

    fprintf(stderr, "\tnative: MonitorWait (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: MonitorWait (prms) jni_env   is %p\n", jni_env);
    fprintf(stderr, "\tnative: MonitorWait (prms) thread    is %p\n", thread);
    fprintf(stderr, "\tnative: MonitorWait (prms) object    is %p\n", object);
    fprintf(stderr, "\tnative: MonitorWait (prms) timeout   is %lld\n", (long long)timeout);
    fflush(stderr);

    params = true;

    if (timeout == 5000) test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event MonitorWait0101                    : ");

    if ((params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test MonitorWait0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */

