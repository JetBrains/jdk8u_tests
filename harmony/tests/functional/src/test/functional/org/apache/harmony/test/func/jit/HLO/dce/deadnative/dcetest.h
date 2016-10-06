/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
#include <jni.h>

#ifndef _Included_WeakRefTest
#define _Included_WeakRefTest

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_NativeMethodSet
  (JNIEnv *, jobject, jstring obj);

JNIEXPORT void JNICALL Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_NativeMethodFakeSet
  (JNIEnv *, jobject, jstring obj);

JNIEXPORT jstring JNICALL Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_NativeMethodGet
  (JNIEnv *, jobject);

JNIEXPORT jstring JNICALL Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_NativeMethodSetAndStore
  (JNIEnv *, jobject);

JNIEXPORT void JNICALL Java_org_apache_harmony_test_func_jit_HLO_dce_deadnative_DeadNativeTest_nativeDeleteRef
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif

#endif

