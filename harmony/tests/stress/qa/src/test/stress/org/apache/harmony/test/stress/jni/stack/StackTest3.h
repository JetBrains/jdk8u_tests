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
/*
 * THE FILE HAS BEEN AUTOGENERATED BY THE IJH TOOL.
 * Please be aware that all changes made to this file manually
 * will be overwritten by the tool if it runs again.
 */

#include <jni.h>


/* Header for class org.apache.harmony.test.stress.jni.stack.StackTest3 */

#ifndef _ORG_APACHE_HARMONY_TEST_STRESS_JNI_STACK_STACKTEST3_H
#define _ORG_APACHE_HARMONY_TEST_STRESS_JNI_STACK_STACKTEST3_H

#ifdef __cplusplus
extern "C" {
#endif


/* Static final fields */

#undef org_apache_harmony_test_stress_jni_stack_StackTest3_TM_ERROR_NONE
#define org_apache_harmony_test_stress_jni_stack_StackTest3_TM_ERROR_NONE 0L

#undef org_apache_harmony_test_stress_jni_stack_StackTest3_TM_ERROR_INTERRUPT
#define org_apache_harmony_test_stress_jni_stack_StackTest3_TM_ERROR_INTERRUPT 52L

#undef org_apache_harmony_test_stress_jni_stack_StackTest3_TM_ERROR_ILLEGAL_STATE
#define org_apache_harmony_test_stress_jni_stack_StackTest3_TM_ERROR_ILLEGAL_STATE 118L


/* Native methods */

/*
 * Method: org.apache.harmony.test.stress.jni.stack.StackTest3.nativeMethod([III)[I
 */
JNIEXPORT jintArray JNICALL
Java_org_apache_harmony_test_stress_jni_stack_StackTest3_nativeMethod(JNIEnv *, jclass, 
    jintArray, jint, jint);

/*
 * Method: org.apache.harmony.test.stress.jni.stack.StackTest3.init(I)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_stack_StackTest3_init(JNIEnv *, jobject, 
    jint);


#ifdef __cplusplus
}
#endif

#endif /* _ORG_APACHE_HARMONY_TEST_STRESS_JNI_STACK_STACKTEST3_H */

