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
#include <stdio.h>


JNIEXPORT jlong JNICALL Java_org_apache_harmony_test_func_reg_vm_btest4673_Test4673_foo(
        JNIEnv *env, jobject obj, jlong f)
{ 
    jclass cls = (*env)->GetObjectClass(env, obj);
    jmethodID mid = (*env)->GetMethodID(env, cls, "call", "(J)J");
    printf("in c, fl = %lld, enter Java\n", f);
    (*env)->CallLongMethod(env, obj, mid, f);
    return f;
}

JNIEXPORT jdouble JNICALL Java_org_apache_harmony_test_func_reg_vm_btest4673_Test4673_foo1(
        JNIEnv *env, jobject obj, jdouble f)
{
    jclass cls = (*env)->GetObjectClass(env, obj);
    jmethodID mid = (*env)->GetMethodID(env, cls, "call", "(D)D");
    printf("in c, fi = %f, enter Java\n", f);
    (*env)->CallDoubleMethod(env, obj, mid, f);   
    return f;
}

JNIEXPORT jfloat JNICALL Java_org_apache_harmony_test_func_reg_vm_btest4673_Test4673_foo2(
        JNIEnv *env, jobject obj, jfloat f)
{  
    jclass cls = (*env)->GetObjectClass(env, obj);
    jmethodID mid = (*env)->GetMethodID(env, cls, "call", "(F)F");\
    printf("in c, fi = %f, enter Java\n", f);
    return (*env)->CallFloatMethod(env, obj, mid, f);
}
