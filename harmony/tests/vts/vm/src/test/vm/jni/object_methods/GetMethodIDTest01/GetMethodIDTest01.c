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
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.GetMethodIDTest01.nativeExecute(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_GetMethodIDTest01_nativeExecute
    (JNIEnv *env, jobject this_object, jclass clazz, jstring name, jstring sig)
{
    const char *cname, *csig;
    jmethodID mID;

    cname = (*env)->GetStringUTFChars(env, name, NULL);
    if (NULL == cname)
        return JNI_FALSE;

    csig = (*env)->GetStringUTFChars(env, sig, NULL);
    if (NULL == csig)
    {
        (*env)->ReleaseStringUTFChars(env, name, cname);
        return JNI_FALSE;
    }

    mID = (*env)->GetMethodID(env, clazz, cname, csig);
    (*env)->ExceptionClear(env);
    (*env)->ReleaseStringUTFChars(env, name, cname);
    (*env)->ReleaseStringUTFChars(env, sig, csig);

    return NULL == mID ? JNI_FALSE : JNI_TRUE;
}
