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
#include <math.h>

static bool test = false;
static bool util = false;
static bool flag = false;

static jvmtiError result;

const char test_case_name[] = "RawMonitorWait0104";

/* *********************************************************************** */

static void JNICALL function(jvmtiEnv* jvmti_env, JNIEnv* jni_env, void* arg);

static jrawMonitorID mntr;
static jint started = 0;
static jint entered = 0;
static jint finish = 0;

static jthread thread_a;
static jthread thread_b;

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

    fprintf(stderr, "\tnative: JNI: funcs start\n");
    jclass clazz = jni_env->FindClass("java/lang/Thread");
    if (clazz)
        fprintf(stderr, "\tnative: JNI: FindClass - Ok\n");
    else
        return;
    jmethodID mid = jni_env->GetMethodID(clazz, "<init>", "()V");
    if (mid)
        fprintf(stderr, "\tnative: JNI: GetMethodID - Ok\n");
    else
        return;

    thread_a = jni_env->NewObject(clazz, mid);
    thread_b = jni_env->NewObject(clazz, mid);
    if (thread_a || thread_b)
        fprintf(stderr, "\tnative: JNI: NewObjects - Ok\n");
    else
        return;

    result = jvmti_env->RunAgentThread(thread_a, proc, "First",
            JVMTI_THREAD_NORM_PRIORITY);
    fprintf(stderr, "\tnative: RunAgentThread 1 result = %d\n", result);
    fprintf(stderr, "\tnative: thread_a               is %x\n", thread_a);
    if (result != JVMTI_ERROR_NONE) return;

    result = jvmti_env->RunAgentThread(thread_b, proc, "Second",
            JVMTI_THREAD_NORM_PRIORITY);
    fprintf(stderr, "\tnative: RunAgentThread 2 result = %d\n", result);
    fprintf(stderr, "\tnative: thread_b               is %x\n", thread_b);
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
     * monitor and execute empty loop while second thread will started.
     *
     * Second started thread increase number of started thread and try to
     * enter to monitor. It (second thread) will wait because monitor at this
     * moment will entered by first started thread.
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
     * Something that only first thread must to do.
     */
    if (1 == entered)
    {
        fprintf(stderr, "\tnative: thread 1 (%d) before empty loop\n", current);
        /*
         * First thread will execute "sleep" commands while second thread will
         * not start.
         */
        while (1 == started)
        {
#ifndef LINUX
            Sleep(2000);
#else
            usleep(2);
#endif
        }

        fprintf(stderr, "\tnative: thread 1 (%d) after empty loop\n", current);

        /*
         * It is NOT the best idea to synchronize two thread using timeout
         * but I have not assurance that RawMonitorNotifyAll function work
         * correctly.
         *
         * In this place first thread must to free monitor for giving ability
         * continue work to second thread.
         */
        fprintf(stderr, "\tnative: thread 1 (%d) start RawMonitorWait\n",
                current);
        result = jvmti_env->RawMonitorWait(mntr, 10000);
        fprintf(stderr, "\tnative: RawMonitorWait result = %d \n", result);
        fprintf(stderr, "\tnative: current is %d \n", current);
        flag = true;

        /*
         * While first thread will execute "Wait with timeout" second thread
         * must
         * - up flag that test is passed.
         * - finish its work.
         *
         * So first thread Destroys Monitor
         */
        fprintf(stderr, "\tnative: thread 1 (%d) start DestroyRawMonitor\n",
                current);
        result = jvmti_env->DestroyRawMonitor(mntr);

        finish++;
    }

    /*
     * Something that only second thread must to do.
     */
    if (2 == current)
    {
        fprintf(stderr, "\tnative: thread 2 (%d) up <test> variable\n",
                current);

        fprintf(stderr, "\tnative: call wail with INVALID monitor\n");
        result = jvmti_env->RawMonitorWait((jrawMonitorID)jvmti_env, 10000);
        fprintf(stderr, "\tnative: RawMonitorWait result = %d (must be JVMTI_ERROR_INVALID_MONITOR (50))\n", result);
        fprintf(stderr, "\tnative: current is %d \n", current);

        if (result == JVMTI_ERROR_INVALID_MONITOR) test = true;

        if (!flag)
        {
            fprintf(stderr, "\tnative: thread 2 (%d) start RawMonitorExit\n",
                    current);
            result = jvmti_env->RawMonitorExit(mntr);
        }
    }

    return;
}

/* *********************************************************************** */

