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
#include"ArraysTest2.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_arrays_ArraysTest2_init(JNIEnv *env,
                                                              jclass c) {
  ArraysTest_init(env, c);
}

JNIEXPORT jint JNICALL
Java_org_apache_harmony_test_stress_jni_arrays_ArraysTest2_doNativeCalc(
    JNIEnv *env,
    jobject thisObject,
    jint len,
    jint maxCnt,
    jobjectArray arrays,
    jlong tmpPtr) {

  int cnt,i;
  jint* tmp1 = (jint*) tmpPtr;

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
      free(tmp1);
      return 0;
    }
  }

  printf("Native code: Performing calculations on %d arrays...\n", cnt);

  for (i = 0; i < cnt; i++) {
    jintArray res;
    jintArray arr;
    jint* intArr1;
    jint* intArr2;
    int comp;

    arr = (*env)->GetObjectArrayElement(env, arrays, i);

    if ( (*env)->ExceptionCheck(env) ) {
      printf("Native code: Error getting int[] array element, index=%d\n", i);
      free(tmp1);
      return 0;
    }

    intArr1 = (*env)->GetIntArrayElements(env, arr, NULL);

    if (intArr1 == NULL) {
      printf("Native code: Error getting array of type int\n");
      free(tmp1);
      return 0;
    }

    ArraysTest_do_calc(intArr1, tmp1, len);

    res = (*env)->CallObjectMethod(env, thisObject, mid, arr);

    if ( (*env)->ExceptionCheck(env) ) {
      printf("Native code: An exception occurred calling method doCalc()\n");
      free(tmp1);
      (*env)->ReleaseIntArrayElements(env, arr, intArr1, JNI_ABORT);
      return 0;
    }

    intArr2 = (*env)->GetIntArrayElements(env, res, NULL);

    if (intArr2 == NULL) {
      printf("Native code: Error getting array of type int\n");
      free(tmp1);
      (*env)->ReleaseIntArrayElements(env, arr, intArr1, JNI_ABORT);
      return 0;
    }

    comp=ArraysTest_do_compare(tmp1, intArr2, len);
    (*env)->ReleaseIntArrayElements(env, arr, intArr1, JNI_ABORT);
    (*env)->ReleaseIntArrayElements(env, res, intArr2, JNI_ABORT);

    if (!comp) {
      printf("FAILED, i=%d\n", i);
      free(tmp1);
      return 0;
    }

    (*env)->DeleteLocalRef(env, arr);
    (*env)->DeleteLocalRef(env, res);
  }

  printf("Native code: Done calculations on %d arrays\n", cnt);
  free(tmp1);
  return 1;
}
