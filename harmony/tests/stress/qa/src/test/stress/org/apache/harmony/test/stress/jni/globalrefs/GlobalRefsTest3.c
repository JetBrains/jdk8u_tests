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
#include"GlobalRefsTest3.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_globalrefs_GlobalRefsTest3_init(JNIEnv* env,
                                                                        jclass clazz) {
  GlobalRefsTest_init(env, clazz);
}

JNIEXPORT jint JNICALL
Java_org_apache_harmony_test_stress_jni_globalrefs_GlobalRefsTest3_nativeMethod(JNIEnv* env,
                                                                                jobject thisObject,
                                                                                jint cnt) {
  int i;
  for (i = 0; (cnt == -1) ? (i >= 0) : (i < cnt); i++) {
    jobject obj = (*env)->NewGlobalRef(env, thisObject);
    if (obj == NULL) {
      if (!(*env)->ExceptionCheck(env)) {
          (*env)->Throw(env, java_lang_out_of_memory_error);
      }
      printf("\nNative code: NULL returned at i=%d\n", i);
      return i;
    }    
  }
  return cnt;
}
