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
 * Method: org.apache.harmony.vts.test.vm.jni.object_operations.NewObjectATest.nativeExecute(Ljava/lang/Class;I)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1operations_NewObjectATest_nativeExecute
    (JNIEnv *env, jobject this_object, jclass clazz, jint value)
{
    jmethodID mID;
    jobject obj;
    jvalue args[1];

    mID = (*env)->GetMethodID(env, clazz, "<init>", "(I)V");
    if (NULL == mID)
        return JNI_FALSE;

    args[0].i = value;
    obj = (*env)->NewObjectA(env, clazz, mID, args);
    if (NULL == obj)
        return JNI_FALSE;

    return JNI_TRUE;
}
