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
 * Method: org.apache.harmony.vts.test.vm.jni.references.DeleteLocalRefTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/references/RefTestsClass;)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_references_DeleteLocalRefTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject parameter)
{
	jfieldID fID;
	jmethodID mID;
	jclass className, system;
	jobject obj;
	jint result = 0;
	jweak ref;

	className = (*env)->GetObjectClass(env, parameter);
	fID = (*env)->GetStaticFieldID(env, className, "a", "I");
	(*env)->SetStaticIntField(env, className, fID, 1000);
	result = (*env)->GetStaticIntField(env, className, fID);

	obj = (*env)->AllocObject(env, className);
	ref = (*env)->NewWeakGlobalRef(env, obj);
	if(!ref){
		(*env)->ExceptionClear(env);
		return 0;
	}
	if( (*env)->IsSameObject(env, ref, NULL) == JNI_TRUE ) return 0;
	(*env)->DeleteLocalRef(env, obj);

	system = (*env)->FindClass(env, "java/lang/System");
	if(!system) return 0;
	mID = (*env)->GetStaticMethodID(env, system, "gc", "()V");
	if(!mID) return 0;
	(*env)->CallStaticVoidMethod(env, system, mID);

	if( (*env)->IsSameObject(env, ref, NULL) == JNI_FALSE ) return 0;
    (*env)->DeleteWeakGlobalRef(env, ref);

	return 1000;
//	return result;
}
