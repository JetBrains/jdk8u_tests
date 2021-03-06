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
#include"LocalRefsTest8.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_localrefs_LocalRefsTest8_init(JNIEnv* env,
                                                                      jclass clazz) {
  LocalRefsTest_init(env, clazz);
}

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_localrefs_LocalRefsTest8_nativeMethod(JNIEnv* env,
                                                                              jobject thisObject,
                                                                              jint cnt,
                                                                              jint len) {
  int i;
  jmethodID mid;
  jobjectArray arr;

  mid = (*env)->GetMethodID(env, clazz, "getObjectArray", "()[Ljava/lang/Object;");
  if (mid == NULL) {
    printf("Native code: Error getting method ID\n");
    return;
  }

  //get object sequence
  arr = (*env)->CallObjectMethod(env, thisObject, mid);
  if ((*env)->ExceptionCheck(env)) {
    printf("Native code: An exception occurred calling getObjectArray method\n");
    return;
  }

  //create references to objects in arr
  for (i = 0; (i < len) && (i < cnt); i++) {   
    jobject obj, ref;

    obj = (*env)->GetObjectArrayElement(env, arr, i);
    if (obj == NULL) {
      printf("Error getting array element\n");
      return;
    }

    if( (*env)->EnsureLocalCapacity(env, 1) ) {
      ref = NULL;
    } else {
      ref = (*env)->NewLocalRef(env, obj);
    }
    if (ref == NULL) {
      printf("\nNative code: NULL returned at i=%d\n", i);
      return;
    }
    (*env)->DeleteLocalRef(env, obj);
  }
  return;
}
