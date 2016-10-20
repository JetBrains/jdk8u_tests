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
#include"GlobalRefsTest7.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_globalrefs_GlobalRefsTest7_init(JNIEnv* env,
                                                                      jclass clazz) {
  GlobalRefsTest_init(env, clazz);
}

JNIEXPORT jint JNICALL
Java_org_apache_harmony_test_stress_jni_globalrefs_GlobalRefsTest7_nativeMethod(JNIEnv* env,
                                                                              jobject thisObject,
                                                                              jint cnt,
                                                                              jint len) {
  int i;
  jmethodID mid;
  jobjectArray arr;

  //get constructor ID of java.lang.Object to create objects
  mid = (*env)->GetMethodID(env, clazz, "getObjectArray", "()[Ljava/lang/Object;");
  if (mid == NULL) {
    printf("Native code: Error getting method ID\n");
    return -1;
  }

  //get object sequence
  arr = (*env)->CallObjectMethod(env, thisObject, mid);
  if ((*env)->ExceptionCheck(env)) {
    printf("Native code: An exception occurred while calling getObjectArray() method\n");
    return -1;
  }

  //create references to objects in arr
  for (i = 0; (cnt == -1) ? (i < len) : ( (i < len) && (i < cnt) ); i++) {   
    jobject ref;
    jobject obj = (*env)->GetObjectArrayElement(env, arr, i);
    if (obj == NULL) {
      if (!(*env)->ExceptionCheck(env)) {
          (*env)->Throw(env, java_lang_out_of_memory_error);
      }
      printf("Error getting array element\n");
      return i;
    }
    ref = (*env)->NewGlobalRef(env, obj);
    if (ref == NULL) {
      if (!(*env)->ExceptionCheck(env)) {
          (*env)->Throw(env, java_lang_out_of_memory_error);
      }
      printf("\nNative code: NULL returned at i=%d\n", i);
      return i;
    }

    (*env)->DeleteLocalRef(env, obj);
  }
  return cnt;
}
