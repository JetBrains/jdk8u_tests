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
#include"LocalRefsTest10.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_localrefs_LocalRefsTest10_init(JNIEnv* env,
                                                                       jclass clazz) {
  LocalRefsTest_init(env, clazz);
}

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_localrefs_LocalRefsTest10_nativeMethod(JNIEnv* env,
                                                                               jobject thisObject,
                                                                               jint cnt) {
  int i;
  
  //instantiate  objects and create refs to them
  for (i = 0; i < cnt; i += 1000) {    
    int k;
    if ((*env)->MonitorEnter(env, clazz) < 0) {
      printf("Native code: Cannot setup monitor\n");
      return;
    }
    for (k = i; k < i + 1000; k++) {
      jobject ref, obj;
      obj = (*env)->NewObject(env, java_lang_object, cid);
      if (obj == NULL) {
        printf("\nNative code: NULL returned at i=%d\n", i);
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
    }
    if ((*env)->MonitorExit(env, clazz) < 0) {
      printf("Native code: Cannot release monitor\n");
      return;
    }
  }
  return;
}
