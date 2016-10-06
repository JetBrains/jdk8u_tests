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
#include "jvmti.h"

extern "C"
{
    JNIEXPORT jboolean JNICALL Java_org_apache_harmony_test_func_reg_vm_btest5217_Btest5217_init(JNIEnv *, jclass);
}


static jvmtiEnv *jvmti = NULL;
static jvmtiCapabilities capabilities;
static jvmtiEventCallbacks callbacks;

jvmtiPhase phase;
jvmtiError result;


bool status = false;


int ex_count = 0;
int exc_count = 0;
int ment_count = 0;
int mex_count = 0;
int b_count = 0;
int f_count = 0;

void JNICALL
Exception(jvmtiEnv * jvmti_env,
          JNIEnv * jni_env,
          jthread thread,
          jmethodID method,
          jlocation location,
          jobject exception, jmethodID catch_method, jlocation catch_location)
{

    result = jvmti->GetPhase(&phase);

    if ((phase != JVMTI_PHASE_LIVE)) {
        ex_count--;
        return;
    }
    else {
        ex_count++;
    }
}

void JNICALL
MethodEntry(jvmtiEnv * jvmti_env,
            JNIEnv * jni_env, jthread thread, jmethodID method)
{

    result = jvmti->GetPhase(&phase);

    if ((phase != JVMTI_PHASE_LIVE)) {
        ment_count--;
        return;
    }
    else {
        ment_count++;
    }

}

void JNICALL
MethodExit(jvmtiEnv * jvmti_env,
           JNIEnv * jni_env,
           jthread thread,
           jmethodID method,
           jboolean was_popped_by_exception, jvalue return_value)
{

    result = jvmti->GetPhase(&phase);

    if ((phase != JVMTI_PHASE_LIVE)) {
        mex_count--;
        return;
    }
    else {
        mex_count++;
    }

}


JNIEXPORT void JNICALL VMDeath(jvmtiEnv * jvmti_env, JNIEnv * env)
{

    if (ex_count > 0 && ment_count > 0 && mex_count > 0) {
        fprintf(stderr, " passed \n");
    }
    else {
        fprintf(stderr, " failed \n");
    }


    fprintf(stderr, "ex_count %d\n", ex_count);
    fprintf(stderr, "ment_count %d\n", ment_count);
    fprintf(stderr, "mex_count %d\n", mex_count);


}


JNIEXPORT jint
    JNICALL Agent_OnLoad(JavaVM * jvm, char *options, void *reserved)
{
    jvmtiError err;
    jint res;
    char *errn;
    jvmtiPhase phase;

    capabilities.can_generate_exception_events = 1;     //Exception ExceptionCatch 
    capabilities.can_generate_method_entry_events = 1;  // MethodEntry 
    capabilities.can_generate_method_exit_events = 1;   // MethodExit 

    res = jvm->GetEnv((void **) &jvmti, JVMTI_VERSION_1_0);

    if (res != JNI_OK || jvmti == NULL) {
        return JNI_ERR;
    }
    err = jvmti->AddCapabilities(&capabilities);
    if (err != JVMTI_ERROR_NONE && err != JVMTI_ERROR_NOT_AVAILABLE) {
        return JNI_ERR;
    }

    callbacks.Exception = &Exception;
    callbacks.MethodEntry = &MethodEntry;
    callbacks.MethodExit = &MethodExit;
    callbacks.VMDeath = &VMDeath;

    jvmti->SetEventCallbacks(&callbacks, sizeof(callbacks));

    jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_EXCEPTION, NULL);
    jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_METHOD_ENTRY, NULL);
    jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_METHOD_EXIT, NULL);
    jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_VM_DEATH, NULL);

    return JNI_OK;
}


JNIEXPORT jboolean JNICALL Java_org_apache_harmony_test_func_reg_vm_btest5217_Btest5217_init(JNIEnv * env, jclass cls)
{
    if (ex_count > 0 && ment_count > 0 && mex_count > 0) {
        return true;
    }
    else {
        return false;
    }

}
