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
/**
 */
#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jobject JNICALL Java_org_apache_harmony_test_func_reg_vm_btest6954_Test6954_testField
  (JNIEnv *jenv, jclass owner, jobject field) {
      jfieldID f_id = (*jenv)->FromReflectedField(jenv, field);
      jobject reflected = (*jenv)->ToReflectedField(jenv, owner, f_id, JNI_FALSE);
      return reflected;
}

JNIEXPORT jobject JNICALL Java_org_apache_harmony_test_func_reg_vm_btest6954_Test6954_testMethod
(JNIEnv *jenv, jclass owner, jobject method) {
    jmethodID m_id = (*jenv)->FromReflectedMethod(jenv, method);
    jobject reflected = (*jenv)->ToReflectedMethod(jenv, owner, m_id, JNI_FALSE);
    return reflected;
}

#ifdef __cplusplus
}
#endif
