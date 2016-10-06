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
#include <stdlib.h>
#include <string.h>
#include "jvmti.h"

static bool stop_after_constructor = false;

static void JNICALL 
callbackSingleStep( jvmtiEnv * jvmti_env,
                    JNIEnv * jni_env,
                    jthread thread,
                    jmethodID method,
                    jlocation location)
{
    char *name;
    char *descr;
    jvmtiError result = jvmti_env->GetMethodName(method, &name, &descr, NULL);
    if( result != JVMTI_ERROR_NONE
        || strcmp(name, "main") || strcmp(descr, "([Ljava/lang/String;)V" ) )
    {
        return;
    }
    if( location == 8 ) {
        printf("Disabling SingleStep event... ");
        result = jvmti_env->SetEventNotificationMode(JVMTI_DISABLE,
            JVMTI_EVENT_SINGLE_STEP, NULL);
        if(result != JVMTI_ERROR_NONE) {
            printf("Cannot disable SingleStep event\n");
            exit(1);
        }
        printf("done\n");
        stop_after_constructor = true;
    }
    return;
}

static void JNICALL 
callbackMethodEntry( jvmtiEnv* jvmti_env,
                     JNIEnv* jni_env,
                     jthread thread,
                     jmethodID method)
{
    char *name;
    char *descr;
    jvmtiError result = jvmti_env->GetMethodName(method, &name, &descr, NULL);
    if( result != JVMTI_ERROR_NONE
        || strcmp(name, "test") || strcmp(descr, "()V" ) )
    {
        return;
    }
    printf("Entried to main(String[])\n");
    printf("Disable MethodEntryEvent... ");
    result = jvmti_env->SetEventNotificationMode(JVMTI_DISABLE,
        JVMTI_EVENT_METHOD_ENTRY, NULL);
    if(result != JVMTI_ERROR_NONE) {
        printf("Cannot disable MethodEntry event\n");
        exit(1);
    }
    printf("done\n");
    printf("Enabling Breakpoint event... ");
    result = jvmti_env->SetEventNotificationMode(JVMTI_ENABLE,
        JVMTI_EVENT_BREAKPOINT, NULL);
    if(result != JVMTI_ERROR_NONE) {
        printf("Cannot enable Breakpoint event\n");
        exit(1);
    }
    printf("done\n");
    printf("Setting breakpoint on line 16... ");
    result = jvmti_env->SetBreakpoint(method, 0);
    if(result != JVMTI_ERROR_NONE) {
        printf("Cannot set breakpoint on line 16: Object obj = new Object();\n");
        exit(1);
    }
    printf("done\n");
    printf("Setting breakpoint on line 20... ");
    result = jvmti_env->SetBreakpoint(method, 15);
    if(result != JVMTI_ERROR_NONE) {
        printf("Cannot set breakpoint on line 20: System.out.println(\"done: \" + z + obj);\n");
        exit(1);
    }
    printf("done\n");
    return;
}

static void JNICALL
callbackBreakpoint( jvmtiEnv* jvmti_env,
                    JNIEnv* jni_env,
                    jthread thread,
                    jmethodID method,
                    jlocation location)
{
    char *name;
    char *descr;
    jvmtiError result = jvmti_env->GetMethodName(method, &name, &descr, NULL);
    if( result != JVMTI_ERROR_NONE
        || strcmp(name, "test") || strcmp(descr, "()V" ) )
    {
        return;
    }
    printf("Breakpoint occupied in line... ");
    if(location == 0) {
        printf("5\n");
        printf("Enabling SingleStep event... ");
        result = jvmti_env->SetEventNotificationMode(JVMTI_ENABLE,
            JVMTI_EVENT_SINGLE_STEP, NULL);
        if(result != JVMTI_ERROR_NONE) {
            printf("Cannot enable SingleStep event\n");
            exit(1);
        }
        printf("done\n");
    } else if( location == 15 ) {
        printf("9\n");
        printf("Disabling SingleStep event... ");
        result = jvmti_env->SetEventNotificationMode(JVMTI_DISABLE,
            JVMTI_EVENT_SINGLE_STEP, NULL);
        if(result != JVMTI_ERROR_NONE) {
            printf("Cannot disable SingleStep event\n");
            exit(1);
        }
        printf("done\n");
        printf("Getting method declaring class... ");
        jclass klass;
        result = jvmti_env->GetMethodDeclaringClass(method, &klass);
        if(result != JVMTI_ERROR_NONE) {
            printf("Cannot get method class\n");
            exit(1);
        }
        printf("done\n");
        printf("Getting field StepObject.status ... ");
        jfieldID field = jni_env->GetStaticFieldID(klass, "status", "Z");
        if( !field ) {
            printf("Cannot get field\n");
            exit(1);
        }
        printf("done\n");
        printf("Setting StepObject.status = true");
        jni_env->SetStaticBooleanField(klass, field, JNI_TRUE);
    } else {
        printf("unknown\n");
        exit(1);
    }
    return;
}

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *vm, char *options, void *reserved)
{
    jvmtiEnv * jvmti_env;
    if( vm->GetEnv( (void**)&jvmti_env, JVMTI_VERSION_1_0) != JNI_OK ) {
        return JNI_ERR;
    }
    
    jvmtiCapabilities capabilities;
    jvmtiError result = jvmti_env->GetPotentialCapabilities(&capabilities);
    if (result != JVMTI_ERROR_NONE) {
        return JNI_ERR;
    }

    result = jvmti_env->AddCapabilities(&capabilities);
    if (result != JVMTI_ERROR_NONE && result != JVMTI_ERROR_NOT_AVAILABLE) {
        return JNI_ERR;
    }

    jvmtiEventCallbacks callbacks;
    memset(&callbacks, 0, sizeof(callbacks));

    // setting callbacks
    printf("Setting callbacks... ");
    callbacks.SingleStep = &callbackSingleStep;
    callbacks.MethodEntry = &callbackMethodEntry;
    callbacks.Breakpoint = &callbackBreakpoint;
    result = jvmti_env->SetEventCallbacks(&callbacks, (jint)sizeof(callbacks));
    if( result != JVMTI_ERROR_NONE ) {
        printf("Cannot set callbacks\n");
        return JNI_ERR;
    }
    printf("done!\n");
    

    printf("Enabling MethodEntry event... ");
    result = jvmti_env->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_METHOD_ENTRY, NULL);
    if( result != JVMTI_ERROR_NONE ) {
        printf("Cannot enable event\n");
        return JNI_ERR;
    }
    printf("done!\n");

    return JNI_OK;
}

