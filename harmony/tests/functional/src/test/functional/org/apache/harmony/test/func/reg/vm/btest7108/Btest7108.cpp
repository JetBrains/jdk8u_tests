/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
#include <stdio.h>
#include <string.h>
#include "jvmti.h"


void JNICALL
ThreadEnd(jvmtiEnv *jvmti_env,
            JNIEnv* jni_env,
            jthread thread);


JNIEXPORT jint 
JNICALL Agent_OnLoad(JavaVM *jvm, char *options, void *reserved)\
{
    jvmtiError err;
    jvmtiEnv *jvmti = NULL;
    jvmtiEventCallbacks callbacks;
    jint res;
    char *errn;
    jvmtiPhase phase;

    res = jvm->GetEnv((void**)&jvmti, JVMTI_VERSION_1_0);

    if (res != JNI_OK || jvmti == NULL)
    {
        return JNI_ERR;
    }

    callbacks.ThreadEnd = &ThreadEnd;
    
    err = jvmti->SetEventCallbacks(&callbacks, sizeof(callbacks));
    err = jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_THREAD_END, NULL);

    return JNI_OK;
}

void JNICALL
ThreadEnd(jvmtiEnv *jvmti_env,
            JNIEnv* jni_env,
            jthread thread)
{
    jvmtiPhase phase;
    jvmtiError result;

    result = jvmti_env->GetPhase(&phase);

    if (result != JVMTI_ERROR_NONE ||
        phase != JVMTI_PHASE_LIVE)
    {
        fprintf(stderr, "\tnative: INCORRECT phase for ThreadEnd event: %d\n", phase);
        return;
    }

    jvmtiThreadInfo thread_info;

    result = jvmti_env->GetThreadInfo(thread, &thread_info);

    if (result !=JVMTI_ERROR_NONE)
    {
        fprintf(stderr, "\tnative: jvmti->GetThreadInfo() FAILED: error = %d\n", result);
        return;
    }

    if (strcmp(thread_info.name, "Btest7108_thread") != 0)
        return;

    jint frame_count = 0;

    result = jvmti_env->GetFrameCount(thread, &frame_count);

    if (result !=JVMTI_ERROR_NONE)
    {
        fprintf(stderr, "\tnative: jvmti->GetFrameCount() FAILED: error = %d\n", result);
        return;
    }

    printf("\tnative: GetFrameCount(): frame_count = %d\n", frame_count);
}

