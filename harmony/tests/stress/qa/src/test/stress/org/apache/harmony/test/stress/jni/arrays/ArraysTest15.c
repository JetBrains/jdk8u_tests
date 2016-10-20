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
#include"ArraysTest15.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_arrays_ArraysTest15_init(JNIEnv *env,
                                                                 jclass c) {
  ArraysTest_init_with_Object_Java_method(env, c);
}

//Java native method
//params: array length, maximum number of arrays
//returns: 0 - fail, 1 - pass
JNIEXPORT jint JNICALL
Java_org_apache_harmony_test_stress_jni_arrays_ArraysTest15_doNativeCalc(
    JNIEnv *env,
    jobject thisObject,
    jint len,
    jint maxCnt,
    jobjectArray tmp1,
    jobjectArray arrays) {

  int cnt;
  int i;
  
  //allocate possible number of arrays of type java.lang.Object (not more than maxCnt)
  for (cnt = 0; cnt < maxCnt; cnt++) {
    int i;
    int ret = 0;
    jobjectArray arr;
    arr = (*env)->NewObjectArray(env, len, java_lang_object, NULL);

    if (arr == NULL) {
      (*env)->ExceptionClear(env);
      break;
    }

    if ( (*env)->MonitorEnter(env, clazz) < 0 ) {
      printf("Native code: Cannot set a monitor\n");
      return 0;
    }

    for (i = 0; i < len; i++) {
      jobject obj = (*env)->NewObject(env, java_lang_object, cid);

      if (obj == NULL) {
        (*env)->ExceptionClear(env);
        ret = 1;
        break;
      }
      (*env)->SetObjectArrayElement(env, arr, i, obj);

      if((*env)->ExceptionCheck(env)) {
        printf("Native code: Error writing to an array element, index=%d\n",
            i);

        if ( (*env)->MonitorExit(env, clazz) < 0 ) {
            printf("Native code: Cannot release a monitor\n");
        }
        return 0;
      }
      (*env)->DeleteLocalRef(env, obj);
    }

    if ( (*env)->MonitorExit(env, clazz) < 0 ) {
      printf("Native code: Cannot release a monitor\n");
      return 0;
    }
    (*env)->SetObjectArrayElement(env, arrays, cnt, arr);

    if ( (*env)->ExceptionCheck(env) ) {
      printf("Native code: Error writing to an array element, index=%d\n",
          cnt);
      return 0;
    }
    (*env)->DeleteLocalRef(env, arr);

    if(ret) break;
  }

  printf("Native code: Performing calculations on %d arrays..\n", cnt);

  for (i = 0; i < cnt; i++) {
    jobjectArray arr, res;
    int comp;
    arr = (*env)->GetObjectArrayElement(env, arrays, i);

    if ( (*env)->ExceptionCheck(env) ) {
      printf("Native code: Error getting Object[] array element, index=%d\n",
          i);
      return 0;
    }
    ArraysTest_do_calc_obj(env, arr, tmp1, len);
    res = (*env)->CallObjectMethod(env, thisObject, mid, arr);
    comp = ArraysTest_do_compare_obj(env, tmp1, res, len);

    if (!comp) {
      printf("Native code: Comparison FAILED, i=%d\n", i);
      return 0;
    }
    (*env)->DeleteLocalRef(env, arr);
    (*env)->DeleteLocalRef(env, res);
  }
  printf("Native code: Done calculations on %d arrays\n", cnt);
  return 1;
}
