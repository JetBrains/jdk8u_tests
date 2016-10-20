/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.4 $
 */

#include<stdio.h>
#include<jni.h>
#include<stdlib.h>
#include"ExceptionsTest4.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_exceptions_ExceptionsTest4_init(JNIEnv *env,
                                                                        jclass c) {
  ExceptionsTest_init(env, c);
}

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_exceptions_ExceptionsTest4_nativeMethod(
    JNIEnv* env,
    jclass c,
    jint cnt){

  jthrowable t;
  printf("cnt=%d\n", cnt);
  (*env)->CallStaticVoidMethod(env, clazz, mid, cnt + 1);
  t = (*env)->ExceptionOccurred(env);
  if (t) {
    if ((*env)->IsInstanceOf(env, t, excClass)) {
      counter--;
      (*env)->ExceptionClear(env);
      (*env)->Throw(env, t);
    } else if ((*env)->IsInstanceOf(env, t, sofClass)) {
      (*env)->ExceptionClear(env);
      (*env)->ThrowNew(env, excClass, "StackOverflow caught in native code");
      counter = cnt;
    } else if (!(*env)->IsInstanceOf(env, t, oomeClass)) {
      printf("Native code: Unexpected exception caught\n");
      (*env)->ExceptionDescribe(env);
    }
    return;
  } else {
    printf("Native code: No exception caught\n");
    return;
  }
}
