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
#include"StackTest9.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_stack_StackTest9_init(JNIEnv *env,
                                                              jobject thisObject,
                                                              jint cnt) {
  StackTest_init_with_Java_method(env, thisObject, cnt);
}

JNIEXPORT jintArray JNICALL
Java_org_apache_harmony_test_stress_jni_stack_StackTest9_nativeMethod(JNIEnv* env,
                                                                      jobject thisObject,
                                                                      jintArray intArray,
                                                                      jint len,
                                                                      jint cnt){
  jint* arr;
  jintArray res, res2 = NULL;
  jint* calc_res;

  StackTest_alloc_arrays(env, len, &arr, &calc_res, NULL);
  if (arr == NULL || calc_res == NULL) {
    printf("Native code: Cannot allocate jint* arrays\n");
    return NULL;
  }

  (*env)->GetIntArrayRegion(env, intArray, 0, len, arr);
  if ((*env)->ExceptionCheck(env)) {
    printf("Native code: Cannot get array elements\n");
    StackTest_free_arrays(env, arr, calc_res, NULL);
    return NULL;
  }

  StackTest_do_calc(arr, calc_res, len);

  res2 = res = (*env)->NewIntArray(env, len);
  if (res == NULL) {
    printf("Native code: Cannot create an array\n");
    StackTest_free_arrays(env, arr, calc_res, NULL);
    return NULL;
  }

  (*env)->SetIntArrayRegion(env, res, 0, len, calc_res);
  if ((*env)->ExceptionCheck(env)) {
    printf("Native code: Cannot set array elements\n");
    StackTest_free_arrays(env, arr, calc_res, NULL);
    return NULL;
  }

  if (cnt < maxCnt) {
    res2 = (*env)->CallObjectMethod(env, thisObject, mid, res, cnt + 1);
    if((*env)->ExceptionCheck(env)) {
      printf("Native code: exception in caught in native method\n");
      StackTest_free_arrays(env, arr, calc_res, NULL);
      return NULL;
    }
  }
  StackTest_free_arrays(env, arr, calc_res, NULL);
  return res2;
}
