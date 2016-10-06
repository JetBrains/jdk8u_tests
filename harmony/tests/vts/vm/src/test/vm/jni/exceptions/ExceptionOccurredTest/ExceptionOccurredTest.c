/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
  
#include <jni.h>
#include <stdio.h>

/*
 * Method: org.apache.harmony.vts.test.vm.jni.exceptions.ExceptionOccurredTest.nativeExecute()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_exceptions_ExceptionOccurredTest_nativeExecute
    (JNIEnv *env, jobject this_object)
{
    const int arg = 10;
    jclass exception_class;
    jmethodID constructor_method;
    jobject exception_object;
    jthrowable exception;
    jclass clazz;
    jmethodID method;
    int value;

    exception = (*env)->ExceptionOccurred(env);
    if (NULL != exception)
        return JNI_FALSE;

    exception_class = (*env)->FindClass(env, "org/apache/harmony/vts/test/vm/jni/exceptions/ExceptionClass");
    if (NULL == exception_class)
        return JNI_FALSE;

    constructor_method = (*env)->GetMethodID(env, exception_class, "<init>", "(I)V");
    if (NULL == constructor_method)
        return JNI_FALSE;

    exception_object = (*env)->NewObject(env, exception_class, constructor_method, arg);
    if (NULL == exception_object)
        return JNI_FALSE;

    if ((*env)->Throw(env, exception_object) < 0)
        return JNI_FALSE;

    exception = (*env)->ExceptionOccurred(env);
    if (NULL == exception)
        return JNI_FALSE;

    (*env)->ExceptionClear(env);

    clazz = (*env)->GetObjectClass(env, exception);
    if (NULL == clazz)
        return JNI_FALSE;

    method = (*env)->GetMethodID(env, clazz, "getField", "()I");
    if (NULL == method)
        return JNI_FALSE;

    value = (*env)->CallIntMethod(env, exception, method);
    if (arg != value)
        return JNI_FALSE;

    exception = (*env)->ExceptionOccurred(env);
    if (NULL != exception)
        return JNI_FALSE;

    return JNI_TRUE;
}
