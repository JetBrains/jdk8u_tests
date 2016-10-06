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
 * Method: org.apache.harmony.vts.test.vm.jni.array_operations.NewObjectArrayTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/array_operations/NewObjectArrayTestClass;)Lorg/apache/harmony/vts/test/vm/jni/array_operations/NewObjectArrayTestClass;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_array_1operations_NewObjectArrayTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject class_object)
{
	jclass element_class;
	jobjectArray arr;
	jobject element1,element2;
	jfieldID fID;
	jint a1,a2;

	element_class = (*env)->FindClass(env, "org/apache/harmony/vts/test/vm/jni/array_operations/NewObjectArrayTestClass");
	arr = (*env)->NewObjectArray(env, 10, element_class, class_object); 
	element1 = (*env)->GetObjectArrayElement(env, arr, 0);
	element2 = (*env)->GetObjectArrayElement(env, arr, 9);
	fID = (*env)->GetFieldID(env, element_class, "a", "I"); 
	a1 = (*env)->GetIntField(env, element1, fID);
	a2 = (*env)->GetIntField(env, element2, fID);

	if (a1 != a2) { return NULL; }
	element1 = (*env)->GetObjectArrayElement(env, arr, 7);
    return element1;
}

