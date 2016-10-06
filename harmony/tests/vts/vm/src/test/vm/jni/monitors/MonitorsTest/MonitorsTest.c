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
 * Method: org.apache.harmony.vts.test.vm.jni.monitors.NativeThread.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/monitors/SyncObject;Ljava/lang/Thread;J)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_monitors_NativeThread_nativeExecute
    (JNIEnv *env, jobject this_object, jobject sync_obj, jobject jt, jlong sleep)
{
    jclass this_class, java_thread_class;
    jmethodID java_thread_start, this_sleep;
    jboolean exception;
    
    java_thread_class = (*env)->GetObjectClass(env, jt);
    this_class = (*env)->GetObjectClass(env, this_object);

    java_thread_start = (*env)->GetMethodID(env, java_thread_class,
        "start", "()V");
    if (NULL == java_thread_start)
        return JNI_FALSE;

    this_sleep = (*env)->GetStaticMethodID(env, this_class, "sleep", "(J)V");
    if (NULL == this_sleep)
        return JNI_FALSE;

    if ((*env)->MonitorEnter(env, sync_obj) < 0)
        return JNI_FALSE;

    (*env)->CallVoidMethod(env, jt, java_thread_start);
    if ((*env)->ExceptionCheck(env))
        return JNI_FALSE;

    (*env)->CallStaticVoidMethod(env, this_class, this_sleep, sleep);
    if ((*env)->ExceptionCheck(env))
        return JNI_FALSE;

    if ((*env)->MonitorExit(env, sync_obj) < 0)
        return JNI_FALSE;

    return JNI_TRUE;
}

