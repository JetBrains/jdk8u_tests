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

static bool test = false;
static bool util = false;
static bool flag = false;
static bool paus = false;

const char test_case_name[] = "DestroyRawMonitor0102";

/* *********************************************************************** */

#define SHORT_TIME 5000
jrawMonitorID monitor = NULL;
void JNICALL function(jvmtiEnv* jvmti_env, JNIEnv* jni_env, void* arg);

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
    jvmtiError result;
    jvmtiStartFunction proc = function;

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
    jthread thread_x = jni_env->NewObject(clazz, mid, "native_agent_thread");
    if (thread_x)
        fprintf(stderr, "\tnative: JNI: NewObject - Ok\n");
    else
        return;

    result = jvmti_env->RunAgentThread(thread_x, proc, NULL,
                JVMTI_THREAD_NORM_PRIORITY);

    while (!util)
    {
#ifndef LINUX
        Sleep(SHORT_TIME);
#else
        usleep(SHORT_TIME * 100);
#endif
    }

    result = jvmti_env->DestroyRawMonitor(monitor);
    fprintf(stderr, "\tnative: DestroyRawMonitor result = %d (must be JVMTI_ERROR_NOT_MONITOR_OWNER (51)) \n", result);
    fflush(stderr);
    if (result == JVMTI_ERROR_NOT_MONITOR_OWNER) test = true;

    paus = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

void JNICALL function(jvmtiEnv* jvmti_env, JNIEnv* jni_env, void* arg)
{
    fprintf(stderr, "\tnative: Native thread is STARTED\n");

    if (jni_env == NULL)
        fprintf(stderr, "\tnative: func was called with jni_env = NULL\n");
    if (jvmti_env == NULL)
        fprintf(stderr, "\tnative: func was called with jvmti_env = NULL\n");
    if (arg == NULL)
        fprintf(stderr, "\tnative: func was called with arg = NULL\n");

    jvmtiError result;
    const char* name = "Tested Monitor";

    result = jvmti_env->CreateRawMonitor(name, &monitor);
    fprintf(stderr, "\tnative: CreateRawMonitor result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: name is %s \n", name);
    fprintf(stderr, "\tnative: monitor is %p \n", monitor);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) return;

    result = jvmti_env->RawMonitorEnter(monitor);
    fprintf(stderr, "\tnative: RawMonitorEnter result = %d (must be zero) \n", result);

    util = true;

    while (!paus)
    {
#ifndef LINUX
        Sleep(SHORT_TIME);
#else
        usleep(SHORT_TIME * 100);
#endif
    }

    result = jvmti_env->RawMonitorExit(monitor);
    fprintf(stderr, "\tnative: RawMonitorExit result = %d (must be zero) \n", result);

    return;
}

/* *********************************************************************** */



