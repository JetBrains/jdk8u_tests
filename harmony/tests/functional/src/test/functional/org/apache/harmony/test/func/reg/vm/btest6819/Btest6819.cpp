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

static int g_is_failed = 0;
static int g_is_error = 0;
static jvmtiEnv* jvmti = NULL;


extern "C" JNIEXPORT jboolean JNICALL Java_org_apache_harmony_test_func_reg_vm_btest6819_Btest6819_isERR(JNIEnv * env, jclass cls)
{
    return (g_is_error != 0);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_apache_harmony_test_func_reg_vm_btest6819_Btest6819_isOK(JNIEnv *env, jclass cls) 
{
    jvmtiError err;
    jmethodID mid;
    jlocation loc = 0;
    jint count;
    unsigned char* s_bc;
    unsigned char* bc;

    if (g_is_error)
        return 0;

    mid = env->GetMethodID(cls, "setInt", "(I)V");

    if (mid == NULL)
    {
        g_is_error = 1;
        return 0;
    }

    err = jvmti->SetBreakpoint(mid, loc);

    if (err != JVMTI_ERROR_NONE)
    {
        g_is_error = 1;
        return 0;
    }

    err = jvmti->GetBytecodes(mid, &count, &s_bc);

    if (err != JVMTI_ERROR_NONE)
    {
        g_is_error = 1;
        return 0;
    }
    //printf("\t set brkp, bytecodes is %X \n", *s_bc);

    err = jvmti->ClearBreakpoint(mid, loc);

    if (err != JVMTI_ERROR_NONE)
    {
        g_is_error = 1;
        return 0;
    }

    err = jvmti->GetBytecodes(mid, &count, &bc);

    if (err != JVMTI_ERROR_NONE)
    {
        g_is_error = 1;
        return 0;
    }
    //printf("\tclear brkp, bytecodes is %X \n", *bc);

    return (*s_bc == *bc);
}


JNIEXPORT jint 
JNICALL Agent_OnLoad(JavaVM *jvm, char *options, void *reserved) 
{
    jint res;
    jvmtiError err;
    jvmtiCapabilities capabilities;

    res = jvm->GetEnv((void**)&jvmti, JVMTI_VERSION_1_0);

    if (res != JNI_OK || jvmti == NULL)
    {
        g_is_error = 1;
        return JNI_ERR;
    }

    memset(&capabilities, 0, sizeof(jvmtiCapabilities));
    capabilities.can_get_bytecodes=1;
    capabilities.can_generate_breakpoint_events=1;

    err = jvmti->AddCapabilities(&capabilities);

    if (err != JVMTI_ERROR_NONE)
    {
        g_is_error = 1;
        return JNI_ERR;
    }

    return JNI_OK;
}

