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
#include <iostream>
#include <cstring>

#include "jni.h"
#include "jvmti.h"

#define LOG(msg) std::cout << msg << std::endl;

void JNICALL callbackVMInit(jvmtiEnv *jvmti, JNIEnv* jni, jthread thread) {
    LOG("  VM_INIT: thread=" << thread);
}

void JNICALL callbackVMDeath(jvmtiEnv *jvmti, JNIEnv* jni) {
    LOG("  VM_DEATH: ");
}

void JNICALL callbackDynamicCodeGenerated(jvmtiEnv *jvmti_env,
                                          const char* name,
                                          const void* address,
                                          jint len) {
    LOG("Dynamic method generated :" << name <<
        ", first byte=" << *(char *)address <<
        ", last byte=" << *((char *)address + len - 1));
}

/* ====================================================================== */

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *vm, char *options, void *reserved) {

    jvmtiError err = JVMTI_ERROR_NONE;
    jvmtiEnv* jvmti = NULL;
    
    /* initialization of JVMTI environment */
    {
        jint res = JNI_OK;
        
        res = vm->GetEnv((void**)&jvmti, JVMTI_VERSION_1_0);
        if (res != JNI_OK) {
           LOG("Error in JVMTI initialization: " << res);
           return res;
        }
        LOG("Got JVMTI environment: " << (void*)jvmti);
    }

    /* setting event callbacks */
    {
        jvmtiEventCallbacks callbacks;

        memset(&callbacks, 0, sizeof(callbacks));
        callbacks.VMInit = callbackVMInit;
        callbacks.VMDeath = callbackVMDeath;
        callbacks.DynamicCodeGenerated = callbackDynamicCodeGenerated;

        err = jvmti->SetEventCallbacks(&callbacks, sizeof(callbacks));
        if (err != JVMTI_ERROR_NONE) {
            LOG("Error in setting event callbacks: " << err);
            return JNI_ERR;
        }
        LOG("Event callbacks set");
    }
        
    /* enabling VM_INIT and VM_DEATH events */
    {
        jvmtiEvent events[] = {
                        JVMTI_EVENT_VM_INIT, 
                        JVMTI_EVENT_VM_DEATH,
                        JVMTI_EVENT_DYNAMIC_CODE_GENERATED
                   };
        
        for (int i = 0; i < sizeof(events)/sizeof(jvmtiEvent); i++) {
            err = jvmti->SetEventNotificationMode(JVMTI_ENABLE, events[i], NULL);
            if (err != JVMTI_ERROR_NONE) {
                LOG("Error in enabling event " << events[i] << ": " << err);
                return JNI_ERR;
            }
        }
        LOG("VM_INIT/VM_DEATH events enabled");
    }

    return JNI_OK;
}
