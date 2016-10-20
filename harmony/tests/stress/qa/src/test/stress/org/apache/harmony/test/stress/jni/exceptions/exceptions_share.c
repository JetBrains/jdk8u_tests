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

#include<stdlib.h>
#include"share.h"

jclass clazz;
jclass excClass;
jclass sofClass;
jclass oomeClass;
jmethodID mid;
jmethodID cid;
jint counter;
jint* counters;

void ExceptionsTest_init(JNIEnv* env,
                         jclass c) {

  printf("Native code: Initializing global data...\n");
  excClass = (*env)->FindClass(env,
    "org/apache/harmony/test/stress/jni/exceptions/share/JNITestException");

  if (excClass == NULL) {
      printf("Native code: Cannot find exception class\n");
      return;
  }
  excClass = (*env)->NewGlobalRef(env, excClass);

  if (excClass == NULL) {
      printf("Native code: Cannot create global ref to excClass\n");
      return;
  }

  sofClass=(*env)->FindClass(env, "java/lang/StackOverflowError");

  if (sofClass == NULL) {
      printf("Native code: Cannot find exception class\n");
      return;
  }
  sofClass=(*env)->NewGlobalRef(env, sofClass);

  if (sofClass == NULL) {
      printf("Native code: Cannot create global ref to SOF class\n");
      return;
  }

  oomeClass=(*env)->FindClass(env, "java/lang/OutOfMemoryError");

  if (oomeClass == NULL) {
      printf("Native code: Cannot find OOME class\n");
      return;
  }
  oomeClass=(*env)->NewGlobalRef(env, oomeClass);

  if (oomeClass == NULL) {
      printf("Native code: Cannot create global ref to OOME class\n");
      return;
  }

  clazz = (*env)->NewGlobalRef(env, c);

  if (clazz == NULL) {
    printf("Native code: Cannot create global ref\n");
    return;
  }
  mid = (*env)->GetStaticMethodID(env, c, "nativeMethod", "(I)V");

  if (mid == NULL) {
    printf("Native code: Cannot get method ID for nativeMethod(I)V\n");
    return;
  }
  cid = (*env)->GetMethodID(env, excClass, "<init>", "()V");

  if (cid == NULL) {
    printf("Native code: Cannot get constructor ID for JNITestException\n");
    return;
  }
  counter = 0;
  printf("Native code: Global data initialized\n");
}

void ExceptionsTest_init_MT(JNIEnv* env,
                            jclass c,
                            jint cnt) {

  int i;
  printf("Native code: Initializing global data...\n");
  counters = malloc(cnt * sizeof(jint));
  for (i = 0; i < cnt; i++) {
    counters[i] = 0;
  }
    
  excClass = (*env)->FindClass(env,
    "org/apache/harmony/test/stress/jni/exceptions/share/JNITestException");
  if (excClass == NULL) {
      printf("Native code: Cannot find exception class\n");
      return;
  }

  excClass = (*env)->NewGlobalRef(env, excClass);
  if (excClass == NULL) {
      printf("Native code: Cannot create global ref to excClass\n");
      return;
  }

  sofClass = (*env)->FindClass(env, "java/lang/StackOverflowError");
  if (sofClass == NULL) {
      printf("Native code: Cannot find exception class\n");
      return;
  }
  sofClass = (*env)->NewGlobalRef(env, sofClass);
  if (sofClass == NULL) {
      printf("Native code: Cannot create global ref to SOF class\n");
      return;
  }

  oomeClass = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
  if (oomeClass == NULL) {
      printf("Native code: Cannot find OOME class\n");
      return;
  }
  oomeClass = (*env)->NewGlobalRef(env, oomeClass);
  if (oomeClass == NULL) {
      printf("Native code: Cannot create global ref to OOME class\n");
      return;
  }

  clazz = (*env)->NewGlobalRef(env, c);
  if (clazz == NULL) {
    printf("Native code: Cannot create global ref\n");
    return;
  }
  mid = (*env)->GetStaticMethodID(env, c, "nativeMethod", "(II)V");

  if (mid == NULL) {
    printf("Native code: Cannot get method ID for nativeMethod(II)V\n");
    return;
  }
  cid = (*env)->GetMethodID(env, excClass, "<init>", "()V");

  if (cid == NULL) {
    printf("Native code: Cannot get constructor ID for JNITestException\n");
    return;
  }
  printf("Native code: Global data initialized\n");
}

/**
 * Method to get native variable "counter" from Java code
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_test_stress_jni_exceptions_share_STExceptionsTest_getCounter(
    JNIEnv* env,
    jclass c) {

  return counter;
}

JNIEXPORT jint JNICALL
Java_org_apache_harmony_test_stress_jni_exceptions_share_MTExceptionsTest_getCounter(
    JNIEnv* env,
    jclass c,
    jint id) {

  return counters[id];
}
