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

JNIEXPORT jint JNICALL Java_org_apache_harmony_vts_test_vm_jni_register_1natives_ConstructedClass_nativeExecute
(JNIEnv *env, jobject this_object){
	return 1;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.register_natives.RegisterNativesTest.nativeExecute([BILjava/lang/Object;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_register_1natives_RegisterNativesTest_nativeExecute
    (JNIEnv *env, jobject this_object, jbyteArray rawData, jint length, jobject transferredClassLoader)
{
	jbyte* data = NULL;
	jclass testClass = NULL;
	jobject testElement = NULL;
	jfieldID fID = NULL;
	jmethodID mID = NULL;
	jdouble result;
	jint (*fnPtr)(JNIEnv *, jobject);
	JNINativeMethod method = {"","",NULL};

	data = (*env)->GetByteArrayElements(env, rawData, NULL);
	if (data == NULL) return JNI_FALSE;
	testClass = (*env)->DefineClass(env, "org/apache/harmony/vts/test/vm/jni/register_natives/ConstructedClass", transferredClassLoader, data, length);
	(*env)->ReleaseByteArrayElements(env, rawData, data, 0);
	if (testClass == NULL) return JNI_FALSE;

	testElement = (*env)->AllocObject(env, testClass);
	if (testElement == NULL) return JNI_FALSE;

	fID = (*env)->GetFieldID(env, testClass, "doubleBallast", "D");
	if(fID == NULL) return JNI_FALSE;
	(*env)->SetDoubleField(env, testElement, fID, 3.15);
	result = (*env)->GetDoubleField(env, testElement, fID);
	if(result > 3.15) return JNI_FALSE;

	//fnPtr = &Java_org_apache_harmony_vts_test_vm_jni_register_1natives_ConstructedClass_nativeExecute;
	method.fnPtr = Java_org_apache_harmony_vts_test_vm_jni_register_1natives_ConstructedClass_nativeExecute;
	method.signature = "()I";
	method.name = "nativeExecute";
	if((*env)->RegisterNatives(env, testClass, &method, 1) != 0) return JNI_FALSE;

	mID = (*env)->GetMethodID(env, testClass, "nativeExecute", "()I");
	result = (*env)->CallIntMethod(env, testElement, mID);
	if(result > 1) return JNI_FALSE;

	if((*env)->UnregisterNatives(env, testClass) != 0) return JNI_FALSE;

	return JNI_TRUE;
}

