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
#include"StackTest7.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_stack_StackTest7_init(JNIEnv *env,
                                                              jobject c,
                                                              jint cnt) {
  StackTest_init(env, c, cnt, 1);
}

JNIEXPORT jintArray JNICALL
Java_org_apache_harmony_test_stress_jni_stack_StackTest7_nativeMethod(JNIEnv* env,
                                                                      jclass c,
                                                                      jintArray intArray,
                                                                      jint len,
                                                                      jint cnt,
                                                                      jint threadId){
  jint* arr, *calc_res;
  jintArray res, res2 = NULL;
  int i;

  StackTest_alloc_arrays(env, len, &arr, &calc_res, NULL);
  if (arr == NULL || calc_res == NULL) {
    return NULL;
  }

  (*env)->GetIntArrayRegion(env, intArray, 0, len, arr);
  if((*env)->ExceptionCheck(env)) {
    printf("Native code (thread %d): Cannot get array elements\n", (int)threadId);
    StackTest_free_arrays(env, arr, calc_res, NULL);
    return NULL;
  }

  StackTest_do_calc(arr, calc_res, len);

  res = (*env)->NewIntArray(env, len);
  if (res == NULL) {
    StackTest_free_arrays(env, arr, calc_res, NULL);
    return NULL;
  }

  (*env)->SetIntArrayRegion(env, res, 0, len, calc_res);
  if((*env)->ExceptionCheck(env)) {
    printf("Native code (thread %d): Cannot set array elements\n", (int)threadId);
    StackTest_free_arrays(env, arr, calc_res, NULL);
    return NULL;
  }

  res2 = (*env)->CallStaticObjectMethod(env, clazz, mid, res, len, 0, threadId);
  if ((*env)->ExceptionCheck(env)) {
    (*env)->ExceptionClear(env);
  }

  for (i = 0; i < cnt; i++) {
    res2 = (*env)->CallStaticObjectMethod(env, clazz, mid, res, len, 0, threadId);
    if ((*env)->ExceptionCheck(env)) {
      (*env)->ExceptionClear(env);
    }
  }

  StackTest_free_arrays(env, arr, calc_res, NULL);
  return res2;
}
