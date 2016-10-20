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
 * @version $Revision: 1.5 $
 */

#include<stdio.h>
#include<jni.h>
#include<stdlib.h>
#include"ArraysTest11.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_arrays_ArraysTest11_init(JNIEnv *env,
                                                               jclass c) {
  ArraysTest_init(env, c);
}

//Java native method
//params: array length, maximum number of arrays
JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_arrays_ArraysTest11_doNativeCalc(
    JNIEnv *env,
    jobject thisObject,
    jint len,
    jint maxCnt,
    jobjectArray arrays) {

  int cnt,i;
  
  //allocate possible number of arrays of type int (at most maxCnt)
  for (cnt = 0; cnt < maxCnt; cnt++) {
    jintArray arr = (*env)->NewIntArray(env, len);

    if (arr == NULL) {
      (*env)->ExceptionClear(env);
      break;
    }

    (*env)->SetObjectArrayElement(env, arrays, cnt, arr);

    if ( (*env)->ExceptionCheck(env) ) {
      printf("Native code: Error writing to an array element, index=%d\n",
          cnt);
      return;
    }
  }

  printf("Native code: Performing calculations on %d arrays\n", cnt);

  for (i = 0; i < cnt; i++) {
    jintArray res;
    jintArray arr;
    arr = (*env)->GetObjectArrayElement(env, arrays, i);

    if ( (*env)->ExceptionCheck(env) ) {
      printf("Native code: Error getting int[] array element, index=%d\n", i);
      return;
    }

    res = (*env)->CallObjectMethod(env, thisObject, mid, arr);

    if ( (*env)->ExceptionCheck(env) ) {
      printf("Native code: An exception occurred calling method doCalc()\n");
      return;
    }

    (*env)->DeleteLocalRef(env, res);
    (*env)->DeleteLocalRef(env, arr);
  }
  printf("Native code: Done calculations on %d arrays\n", cnt);

  return;
}
