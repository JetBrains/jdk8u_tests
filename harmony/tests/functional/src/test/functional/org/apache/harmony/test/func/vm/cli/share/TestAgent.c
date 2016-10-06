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
#include "jvmti.h"


static jvmtiEnv *jvmti = NULL;
static jvmtiCapabilities capabilities;
static jvmtiEventCallbacks callbacks;

void JNICALL VMStart(jvmtiEnv *jvmti_env, JNIEnv* jni_env) {
    printf("VMStart\n");            
}

void JNICALL VMInit(jvmtiEnv *jvmti_env, JNIEnv* jni_env, jthread thread) {
    printf("VMInit\n");
}

void JNICALL VMDeath(jvmtiEnv *jvmti_env, JNIEnv* jni_env) {
    printf("VMDeath\n");
}

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *jvm, char *options, void *reserved) {
     jvmtiError err;
     jint res;
     char *errn;
     jvmtiPhase phase;

     if (options != NULL) {
        printf("Options= %s\n", options);

     }

     memset(&capabilities, 0, sizeof(jvmtiCapabilities));

     res = (*jvm)->GetEnv(jvm, (void **) &jvmti, JVMTI_VERSION_1_0);

     if (res != JNI_OK || jvmti == NULL) {
         return JNI_ERR;
     }
     err = (*jvmti)->AddCapabilities(jvmti, &capabilities);
     if (err != JVMTI_ERROR_NONE && err != JVMTI_ERROR_NOT_AVAILABLE) {
         return JNI_ERR;
     }

    printf("Agent load\n");

    callbacks.VMStart = &VMStart; 
    callbacks.VMInit = &VMInit; 
    callbacks.VMDeath = &VMDeath; 
    
    err = (*jvmti)->SetEventCallbacks(jvmti, &callbacks, sizeof(callbacks));
    (*jvmti)->GetErrorName(jvmti, err, &errn);
    printf("SetEventCallbacks, error code: %s\n", errn);

    err = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE, JVMTI_EVENT_VM_START, NULL);
    err = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE, JVMTI_EVENT_VM_INIT, NULL);
    err = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE, JVMTI_EVENT_VM_DEATH, NULL);

   (*jvmti)->GetErrorName(jvmti, err, &errn);
    printf("SetEventNotificationMode, error code: %s\n", errn);
    printf("Finish events initialization \n");
    return JNI_OK;
}


JNIEXPORT void JNICALL Agent_OnUnload(JavaVM *vm) {
    printf("Agent unload");
}
