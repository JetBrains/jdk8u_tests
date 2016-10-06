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
    JNIEXPORT jboolean JNICALL Java_Test_init(JNIEnv *, jclass);
}


static jvmtiEnv *jvmti = NULL;
static jvmtiCapabilities capabilities;
static jvmtiEventCallbacks callbacks;

bool status = false;

JNIEXPORT void JNICALL VMDeath(jvmtiEnv * jvmti_env, JNIEnv * env)
{
    status = true;
    fprintf(stderr, "VMDeath \n");
}


JNIEXPORT jint
    JNICALL Agent_OnLoad(JavaVM * jvm, char *options, void *reserved)
{

    jint res;

    res = jvm->GetEnv((void **) &jvmti, JVMTI_VERSION_1_0);

    if (res != JNI_OK || jvmti == NULL) {
        return JNI_ERR;
    }

    callbacks.VMDeath = &VMDeath;

    jvmti->SetEventCallbacks(&callbacks, sizeof(callbacks));
    jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_VM_DEATH, NULL);
    fprintf(stderr, "Agent load ...\n");
    return JNI_OK;
}


JNIEXPORT void JNICALL 
Agent_OnUnload(JavaVM *vm) 
{

  if (status) {
        fprintf(stderr, " passed \n");
  } else {
        fprintf(stderr, " failed \n");
  }
}
