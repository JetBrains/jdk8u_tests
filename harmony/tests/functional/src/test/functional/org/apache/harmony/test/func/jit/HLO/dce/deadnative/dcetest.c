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
#include <jni.h>
#include "dcetest.h"
#include <stdio.h>

static jstring o;
static int refWasSet = 0;

JNIEXPORT void JNICALL 
Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_nativeMethodSet
  (JNIEnv * env, jobject par, jstring obj)
{
    char * ch;
    o = obj;
    ch = (*env)->GetStringUTFChars(env, obj, NULL);
    //printf("### Set: object = %p, value = %s\n", obj ,ch);
    return;
}

JNIEXPORT void JNICALL 
Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_nativeMethodFakeSet
  (JNIEnv * env, jobject par, jstring obj)
{
    //printf("Fake Set: Just doing nothing\n");
    return;
}

JNIEXPORT void JNICALL 
Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_nativeMethodSetAndStore
  (JNIEnv * env, jobject par, jstring obj)
{
    char * ch;

    if(refWasSet)
        Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_nativeDeleteRef(env, par);

    o = (jstring) (*env)->NewGlobalRef(env, obj);
    refWasSet = 1;
    ch = (*env)->GetStringUTFChars(env, o, NULL);
    //printf("### SetAndStore: object = %p, value = %s\n", o, ch);
    return;
}

JNIEXPORT jstring JNICALL 
Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_nativeMethodGet
  (JNIEnv * env, jobject par)
{
    char * ch; 
    ch = (*env)->GetStringUTFChars(env, o, NULL); 
    //printf("### Get: object = %p, value = %s\n", o, ch);
    return o;
}

JNIEXPORT void JNICALL 
Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_nativeDeleteRef
  (JNIEnv * env, jobject par)
{
    //printf("Delete: object = %p\n", o);
    if(o == NULL || !refWasSet) return;
    (*env)->DeleteGlobalRef(env, o);
    refWasSet = 0;
    o= NULL;
    return;
}