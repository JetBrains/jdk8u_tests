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
/**
 */

#include <stdio.h>
#include <string.h>
#include "jvmti.h"

static int g_obj_count = 0;
static int g_class_count = 0;
static int g_is_ok = 0;
static const int g_max_obj_count = 50;

extern "C" JNIEXPORT jboolean JNICALL Java_org_apache_harmony_test_func_reg_vm_btest7157_Test7157_init(JNIEnv * env, jclass cls)
{
    if (g_obj_count > g_max_obj_count)
        return 0;

    return g_is_ok;
}


void JNICALL
ClassFileLoadHook(jvmtiEnv *jvmti_env,
            JNIEnv* jni_env,
            jclass class_being_redefined,
            jobject loader,
            const char* name,
            jobject protection_domain,
            jint class_data_len,
            const unsigned char* class_data,
            jint* new_class_data_len,
            unsigned char** new_class_data) 
{
    jvmtiEventCallbacks callbacks;

    if (g_is_ok)
        return;

    if (g_obj_count > 0 && g_class_count > 0 &&
        (g_class_count - g_obj_count) > 10)
    {
        g_is_ok = 1;
        jvmti_env->SetEventNotificationMode(JVMTI_DISABLE,JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, NULL);
        callbacks.ClassFileLoadHook = NULL;
        jvmti_env->SetEventCallbacks(&callbacks, sizeof(callbacks));
        return;
    }

    g_class_count++;

    if (strcmp(name, "java/lang/Object") == 0)
        g_obj_count++;

    if (g_obj_count > g_max_obj_count)
    {
        jvmti_env->SetEventNotificationMode(JVMTI_DISABLE,JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, NULL);
        callbacks.ClassFileLoadHook = NULL;
        jvmti_env->SetEventCallbacks(&callbacks, sizeof(callbacks));
        return;
    }
}

JNIEXPORT jint 
JNICALL Agent_OnLoad(JavaVM *jvm, char *options, void *reserved) 
{
    jint res;
    jvmtiEnv* jvmti_env = NULL;
    jvmtiError err;
    jvmtiCapabilities capabilities;
    jvmtiEventCallbacks callbacks;

    res = jvm->GetEnv((void**)&jvmti_env, JVMTI_VERSION_1_0);

    if (res != JNI_OK || jvmti_env == NULL)
        return JNI_ERR;

    memset(&capabilities, 0, sizeof(jvmtiCapabilities));
    capabilities.can_generate_all_class_hook_events = 1;

    err = jvmti_env->AddCapabilities(&capabilities);

    if (err != JVMTI_ERROR_NONE && err != JVMTI_ERROR_NOT_AVAILABLE)
        return JNI_ERR;

    callbacks.ClassFileLoadHook = &ClassFileLoadHook;
    //callbacks.VMDeath = &VMDeath;

    jvmti_env->SetEventCallbacks(&callbacks, sizeof(callbacks));
    jvmti_env->SetEventNotificationMode(JVMTI_ENABLE,JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, NULL);
    //jvmti->SetEventNotificationMode(JVMTI_ENABLE,JVMTI_EVENT_VM_DEATH, NULL);

    return JNI_OK;
}

void JNICALL
VMDeath(jvmtiEnv *jvmti_env,
            JNIEnv* jni_env)
{
}
