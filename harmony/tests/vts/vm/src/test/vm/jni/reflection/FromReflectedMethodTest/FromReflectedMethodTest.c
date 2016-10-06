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
 * Method: org.apache.harmony.vts.test.vm.jni.reflection.FromReflectedMethodTest.nativeExecute(Ljava/lang/reflect/Method;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_reflection_FromReflectedMethodTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject mRefl)
{
	jmethodID mID1, mID2;
	jclass strClass;

	mID1 = (*env)->FromReflectedMethod(env, mRefl);
	if(!mID1) return JNI_FALSE;

	strClass = (*env)->FindClass(env, "java/lang/String");
	mID2 = (*env)->GetMethodID(env, strClass, "length", "()I");
	if(!mID2) return JNI_FALSE;

	if(mID1 != mID2) return JNI_FALSE;
	return JNI_TRUE;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.reflection.FromReflectedMethodTest.nativeExecute1(Ljava/lang/reflect/Constructor;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_reflection_FromReflectedMethodTest_nativeExecute1
    (JNIEnv *env, jobject this_object, jobject cRefl)
{
	jmethodID mID1, mID2;
	jclass strClass;

	mID1 = (*env)->FromReflectedMethod(env, cRefl);
	if(!mID1) return JNI_FALSE;

	strClass = (*env)->FindClass(env, "java/lang/String");
	mID2 = (*env)->GetMethodID(env, strClass, "<init>", "(Ljava/lang/String;)V");
	if(!mID2) return JNI_FALSE;

	if(mID1 != mID2) return JNI_FALSE;
	return JNI_TRUE;
}
