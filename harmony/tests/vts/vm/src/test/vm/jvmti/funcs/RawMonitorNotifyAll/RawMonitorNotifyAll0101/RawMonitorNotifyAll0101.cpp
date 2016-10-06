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
#include <math.h>

static bool test = false;
static bool util = false;
static bool flag = false;

static jvmtiError result;

const char test_case_name[] = "RawMonitorNotifyAll0101";

/* *********************************************************************** */

static void JNICALL function(jvmtiEnv* jvmti_env, JNIEnv* jni_env, void* arg);

static jrawMonitorID mntr;
static jint counter = 0;
static jint entered = 0;
static jint finish = 0;

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_VM_INIT, JVMTI_EVENT_VM_DEATH };
    cb_init;
    cb_death;
    return func_for_Agent_OnLoad(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT);
}

/* *********************************************************************** */

void JNICALL callbackVMInit(prms_VMINIT)
{
    check_VMINIT;

    jvmtiStartFunction proc = function;
    const char* name = "Tested Monitor";

    result = jvmti_env->CreateRawMonitor(name, &mntr);
    fprintf(stderr, "\tnative: CreateRawMonitor result = %d\n", result);
    fprintf(stderr, "\tnative: monitor name   is %s \n", name);
    fprintf(stderr, "\tnative: monitor        is %p \n", mntr);
    fflush(stderr);

    result = jvmti_env->RunAgentThread(create_not_alive_thread(jni_env, jvmti_env),
            proc, "a", JVMTI_THREAD_NORM_PRIORITY);
    if (result != JVMTI_ERROR_NONE) return;

    result = jvmti_env->RunAgentThread(create_not_alive_thread(jni_env, jvmti_env),
            proc, "b", JVMTI_THREAD_NORM_PRIORITY);
    if (result != JVMTI_ERROR_NONE) return;

    result = jvmti_env->RunAgentThread(create_not_alive_thread(jni_env, jvmti_env),
            proc, "c", JVMTI_THREAD_NORM_PRIORITY);
    if (result != JVMTI_ERROR_NONE) return;

    result = jvmti_env->RunAgentThread(create_not_alive_thread(jni_env, jvmti_env),
            proc, "d", JVMTI_THREAD_NORM_PRIORITY);
    if (result != JVMTI_ERROR_NONE) return;

    result = jvmti_env->RunAgentThread(create_not_alive_thread(jni_env, jvmti_env),
            proc, "t", JVMTI_THREAD_NORM_PRIORITY);
    if (result != JVMTI_ERROR_NONE) return;

    while (!finish)
    {
#ifndef LINUX
        Sleep(1000);
#else
        usleep(1);
#endif
    }

    util = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

void JNICALL function(jvmtiEnv* jvmti_env, JNIEnv* jni_env, void* arg)
{
    /*
     * Control point 1.
     * First started thread increase counter of started threads, enter to
     * monitor change thread counters and start RawMonitorWait w/o timeout.
     *
     * Last started thread increase number of started thread and try to
     * enter to monitor. It (last thread) will sent NotifyAll signal to all
     * threads which are already waited.
     */
    result = jvmti_env->RawMonitorEnter(mntr);
    entered++;
    jint current = entered;
    fprintf(stderr, "\tnative: RawMonitorEnter (%d) result = %d\n",
            current, result);
    fprintf(stderr, "\tnative: entered is %d \n", entered);
    fprintf(stderr, "\tnative: jni_env is %p \n", jni_env);
    fprintf(stderr, "\tnative: arg is %p \n", arg);

    /*
     * Something that only NON-last threads must to do.
     */
    if (entered < 5)
    {
        /*
         * In this place NON-last threads must to free monitor for giving ability
         * continue work to other threads.
         */
        fprintf(stderr, "\tnative: thread -> (%d) start RawMonitorWait\n",
                current);
        result = jvmti_env->RawMonitorWait(mntr, 0);
        fprintf(stderr, "\tnative: thread -> (%d) continue work\n",
                current);
        counter++;
        fprintf(stderr, "\tnative: thread -> (%d) start RawMonitorExit\n",
                current);
        result = jvmti_env->RawMonitorExit(mntr);
    }

    /*
     * Something that only last thread must to do.
     */
    if (5 == current)
    {
        fprintf(stderr, "\tnative: thread 5 (%d) up <test> variable\n",
                current);

        /*
         * When NON-last threads will execute "Wait with timeout" second thread
         * up flag that test is passed.
         * After this it free entered monitor and finish its work.
         */

        fprintf(stderr, "\tnative: thread 5 (%d) start RawMonitorNotifyAll\n",
                current);
        result = jvmti_env->RawMonitorNotifyAll(mntr);
        fprintf(stderr, "\tnative: RawMonitorNotifyAll result = %d \n", result);
        fprintf(stderr, "\tnative: current is %d \n", current);

        fprintf(stderr, "\tnative: thread 5 (%d) start RawMonitorExit\n",
                current);
        result = jvmti_env->RawMonitorExit(mntr);
    }

    if (counter == 4)
    {
        finish++;
        test = true;
    }

    return;
}

/* *********************************************************************** */

