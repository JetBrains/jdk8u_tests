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
 * test of event MonitorContendedEntered
 */
void MonitorContendedEntered0102()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_mcentered;
    cb_death;
    cb_mcenter;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_MONITOR_CONTENDED_ENTERED, JVMTI_EVENT_MONITOR_CONTENDED_ENTER,
        JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest MonitorContendedEntered0102 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackMonitorContendedEnter(prms_MON_ENTER)
{
    check_MON_ENTER;

    jvmtiPhase phase;
    jvmtiError result;
    jvmtiThreadInfo tinfo;

    result = jvmti_env->GetPhase(&phase);

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);

    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;

    fprintf(stderr, "\tnative: MonitorContendedEnter (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: MonitorContendedEnter (prms) jni_env   is %p\n", jni_env);
    fprintf(stderr, "\tnative: MonitorContendedEnter (prms) thread    is %p\n", thread);
    fprintf(stderr, "\tnative: MonitorContendedEnter (prms) object    is %p\n", object);
    fprintf(stderr, "\tnative: MonitorContendedEnter counter is %lld\n", (long long)counter);
    fflush(stderr);

    result = jvmti_env->GetThreadInfo(thread, &tinfo);

    if (result != JVMTI_ERROR_NONE) return;

    if (!tinfo.name) {
        fprintf(stderr, "\tnative: GetThreadInfo: pointer to tinfo is NULL \n");
        return;
    }

    fprintf(stderr, "\tnative: GetThreadInfo result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: tinfo.name                 is %s \n", tinfo.name);
    fprintf(stderr, "\tnative: tinfo.priority             is %d \n", tinfo.priority);
    fprintf(stderr, "\tnative: tinfo.is_daemon            is %d \n", tinfo.is_daemon);
    fprintf(stderr, "\tnative: tinfo.thread_group         is %p \n", tinfo.thread_group);
    fprintf(stderr, "\tnative: tinfo.context_class_loader is %p \n", tinfo.context_class_loader);
    fflush(stderr);


}


void JNICALL callbackMonitorContendedEntered(prms_MON_ENTERED)
{
    check_MON_ENTERED;

    jvmtiPhase phase;
    jvmtiError result;
    jvmtiThreadInfo tinfo;

    result = jvmti_env->GetPhase(&phase);

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d (must be 4 (LIVE-phase)) \n", phase);

    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;

    fprintf(stderr, "\tnative: MonitorContendedEntered (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: MonitorContendedEntered (prms) jni_env   is %p\n", jni_env);
    fprintf(stderr, "\tnative: MonitorContendedEntered (prms) thread    is %p\n", thread);
    fprintf(stderr, "\tnative: MonitorContendedEntered (prms) object    is %p\n", object);
    fprintf(stderr, "\tnative: MonitorContendedEntered counter is %lld\n", (long long)counter);
    fflush(stderr);

    result = jvmti_env->GetThreadInfo(thread, &tinfo);

    if (result != JVMTI_ERROR_NONE) return;

    if (!tinfo.name) {
        fprintf(stderr, "\tnative: GetThreadInfo: pointer to tinfo is NULL \n");
        return;
    }

    fprintf(stderr, "\tnative: GetThreadInfo result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: tinfo.name                 is %s \n", tinfo.name);
    fprintf(stderr, "\tnative: tinfo.priority             is %d \n", tinfo.priority);
    fprintf(stderr, "\tnative: tinfo.is_daemon            is %d \n", tinfo.is_daemon);
    fprintf(stderr, "\tnative: tinfo.thread_group         is %p \n", tinfo.thread_group);
    fprintf(stderr, "\tnative: tinfo.context_class_loader is %p \n", tinfo.context_class_loader);
    fflush(stderr);

    if (result != JVMTI_ERROR_NONE) return;

    if (strncmp(tinfo.name, "MonitorContendedEntered0102.thread_", 35)==0) {
		/* ignore unknown threads */
    	counter++;
    }

    if (jvmti_env && jni_env) params = true;

    test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest params  = %d    : ", params);
    fprintf(stderr, "\n\tTest test    = %d    : ", test);
    fprintf(stderr, "\n\tTest counter = %d    : ", counter);

    fprintf(stderr, "\n\tTest of event MonitorContendedEntered0102        : ");

    if ((counter == 2) && (params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test MonitorContendedEntered0102 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */



