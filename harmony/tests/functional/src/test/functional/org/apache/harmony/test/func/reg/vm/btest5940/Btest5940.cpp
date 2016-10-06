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

extern "C" {
    JNIEXPORT void JNICALL Java_org_apache_harmony_test_func_reg_vm_btest5940_Btest5940_foo(JNIEnv *, jclass);
    JNIEXPORT void JNICALL Java_org_apache_harmony_test_func_reg_vm_btest5940_Btest5940_foo1(JNIEnv *, jclass);
    JNIEXPORT void JNICALL Java_org_apache_harmony_test_func_reg_vm_btest5940_TestBtest5940_1I_foo2(JNIEnv *, jclass);

}


JNIEXPORT void JNICALL Java_org_apache_harmony_test_func_reg_vm_btest5940_Btest5940_foo(JNIEnv *env, jclass cls)
{
    cls = env->FindClass("org/apache/harmony/test/func/reg/vm/btest5940/Btest5940_01");
    return;
}

JNIEXPORT void JNICALL Java_org_apache_harmony_test_func_reg_vm_btest5940_Btest5940_foo1(JNIEnv *env, jclass cls)
{
    // aka: class test extends test
    unsigned char buffer[34]= {
        0xca, 0xfe, 0xba, 0xbe, 0x00, 0x03, 0x00, 0x2d, 
        0x00, 0x03, 0x07, 0x00, 0x02, 0x01, 0x00, 0x04, 
        0x74, 0x65, 0x73, 0x74, 0x00, 0x01, 0x00, 0x01, 
        0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
        0x00, 0x00
    };
    env->DefineClass("test", 0, (const jbyte*)buffer, 34);    

}

JNIEXPORT void JNICALL Java_org_apache_harmony_test_func_reg_vm_btest5940_TestBtest5940_1I_foo2(JNIEnv *env, jclass cls)
{
    cls = env->FindClass("org/apache/harmony/test/func/reg/vm/btest5940/TestBtest5940_I_01");
    return;
}
