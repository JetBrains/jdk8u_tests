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
 * test of event ThreadEnd
 */
void ThreadEnd0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_tend;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_THREAD_END, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest ThreadEnd0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackThreadEnd(prms_THRD_END)
{
    check_THRD_END;

    if (flag) return;

    jvmtiThreadInfo tinfo;
    jvmtiError result;
    jvmtiPhase phase;

    fprintf(stderr, "\tnative: ThreadEnd (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: ThreadEnd (prms) jni_env   is %p\n", jni_env);
    fprintf(stderr, "\tnative: ThreadEnd (prms) thread    is %p\n", thread);
    fflush(stderr);

    result = jvmti_env->GetPhase(&phase);

    fprintf(stderr, "\tnative: GetPhase result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: current phase is %d \n", phase);

    if ((result != JVMTI_ERROR_NONE) || (phase != JVMTI_PHASE_LIVE)) return;

    result = jvmti_env->GetThreadInfo(thread, &tinfo);

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

    if (strcmp(tinfo.name, "special_thread")) return;

    flag = true;

    counter++;

    test = true;

    params = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event ThreadEnd0101                      : ");

    if ((counter == 1) && (params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test ThreadEnd0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */





