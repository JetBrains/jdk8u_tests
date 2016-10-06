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
#include<jni.h>

#ifndef __GNUC__
#include <windows.h>

extern "C" {

JNIEXPORT jlong JNICALL Java_org_apache_harmony_test_func_reg_vm_btest3010_Btest3010_initCallBack
  (JNIEnv *, jclass);

JNIEXPORT jint JNICALL Java_org_apache_harmony_test_func_reg_vm_btest3010_Btest3010_runCallBack
  (JNIEnv *, jclass);

}


static jmethodID javaCallback;
static jclass javaClass;
static JavaVM* jvm;

LRESULT __stdcall CallBackNative(HWND p1, UINT p2, WPARAM p3, LPARAM p4) {
    JNIEnv* env;
    jvm->GetEnv((void**)&env, JNI_VERSION_1_2);
    return env->CallStaticLongMethod(javaClass, javaCallback, (jlong)p1, (jint)p2, (jlong)p3, (jlong)p4);
}

void runCallback(LPARAM p1, UINT p2, WPARAM p3, LPARAM p4) {

    long result = CallBackNative((HWND)p1, p2, p3, p4);
    fprintf(stderr,"CallBackNative(%d, %d, %d, %d) returned %d\n", p1, p2, p3, p4, result);
    fflush(stdout);
}

JNIEXPORT jlong JNICALL Java_org_apache_harmony_test_func_reg_vm_btest3010_Btest3010_initCallBack  (JNIEnv * env, jclass cls) {

    javaClass = (jclass)env->NewGlobalRef(cls);
    javaCallback = env->GetStaticMethodID(cls, "runCallback", "(JIJJ)J");
    env->GetJavaVM(&jvm);
    return (jlong)(void *)CallBackNative;
}

JNIEXPORT jint JNICALL Java_org_apache_harmony_test_func_reg_vm_btest3010_Btest3010_runCallBack  (JNIEnv *, jclass) {

    fprintf(stderr,"enter Java_CallBack_runCallBack\n");
    fflush(stdout);

    runCallback(100, 1, 1, 2);
    runCallback(100, 2, 3, 4);
    runCallback(100, 3, 5, 6);
    runCallback(100, 4, 7, 8);

    fprintf(stderr,"exit Java_CallBack_runCallBack\n");
    fflush(stdout);

    return 4;
}

#endif


