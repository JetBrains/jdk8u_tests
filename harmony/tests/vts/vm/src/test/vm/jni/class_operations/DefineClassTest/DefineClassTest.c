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
 * Method: org.apache.harmony.vts.test.vm.jni.class_operations.DefineClassTest.nativeExecute([BILjava/lang/Object;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_class_1operations_DefineClassTest_nativeExecute
    (JNIEnv *env, jobject this_object, jbyteArray rawData, jint length, jobject transferredClassLoader)
{
	jbyte* data = NULL;
	jclass testClass = NULL;
	jobject testElement = NULL;
	jfieldID fID = NULL;
	jmethodID mID= NULL;
	jint result;

	data = (*env)->GetByteArrayElements(env, rawData, NULL);
	if (data == NULL) return JNI_FALSE;
	testClass = (*env)->DefineClass(env, "org/apache/harmony/vts/test/vm/jni/class_operations/LoadedClass", transferredClassLoader, data, length);
	(*env)->ReleaseByteArrayElements(env, rawData, data, 0);
	if (testClass == NULL) return JNI_FALSE;

	testElement = (*env)->AllocObject(env, testClass);
	if (testElement == NULL) return JNI_FALSE;

	fID = (*env)->GetFieldID(env, testClass, "a", "I");
	if(fID == NULL) return JNI_FALSE;
	(*env)->SetIntField(env, testElement, fID, 50);
	result = (*env)->GetIntField(env, testElement, fID);
	if(result != 50) return JNI_FALSE;

	mID = (*env)->GetMethodID(env, testClass, "geta", "()I");
	result = (*env)->CallIntMethod(env, testElement, mID);
	if(result != 50) return JNI_FALSE;

	return JNI_TRUE;
}
