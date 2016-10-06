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

/*
 * Method: org.apache.harmony.vts.test.vm.jni.exceptions.ExceptionClearTest.nativeExecute()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_exceptions_ExceptionClearTest_nativeExecute
    (JNIEnv *env, jobject this_object)
{
    jclass exception_class;
    jobject exception_object;

    exception_class = (*env)->FindClass(env, "org/apache/harmony/vts/test/vm/jni/exceptions/ExceptionClass");
    if (NULL == exception_class)
        return JNI_FALSE;

    if ((*env)->ThrowNew(env, exception_class, NULL) < 0)
        return JNI_FALSE;

    (*env)->ExceptionClear(env);

    return JNI_TRUE;
}
