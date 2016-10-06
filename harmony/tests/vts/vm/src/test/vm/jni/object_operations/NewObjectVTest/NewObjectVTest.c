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

static jobject callNI(JNIEnv *env, jclass clazz, jmethodID mID, ...)
{
    va_list args;
    jobject obj;

    va_start(args, mID);
    obj = (*env)->NewObjectV(env, clazz, mID, args);
    va_end(args);
    return obj;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_operations.NewObjectVTest.nativeExecute(Ljava/lang/Class;I)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1operations_NewObjectVTest_nativeExecute
    (JNIEnv *env, jobject this_object, jclass clazz, jint value)
{
    jmethodID mID;
    jobject obj;

    mID = (*env)->GetMethodID(env, clazz, "<init>", "(I)V");
    if (NULL == mID)
        return JNI_FALSE;

    obj = callNI(env, clazz, mID, value);
    if (NULL == obj)
        return JNI_FALSE;

    return JNI_TRUE;
}
