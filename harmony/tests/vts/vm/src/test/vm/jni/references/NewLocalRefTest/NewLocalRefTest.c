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
 * Method: org.apache.harmony.vts.test.vm.jni.references.NewLocalRefTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/references/RefTestsClass;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_references_NewLocalRefTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject obj)
{
	jfieldID fID;
	jclass className;
	jobject glRef, locRef1, locRef2;
	jint result = 0;

	className = (*env)->GetObjectClass(env, obj);
	fID = (*env)->GetFieldID(env, className, "d", "C");

    glRef = (*env)->NewGlobalRef(env, obj);
	locRef1 = (*env)->NewLocalRef(env, obj);
	locRef2 = (*env)->NewLocalRef(env, glRef);
	
	(*env)->SetIntField(env, locRef1, fID, (jint)2);
	result = (*env)->GetIntField(env, locRef2, fID);
	if(result != (jint)2) return JNI_FALSE;
	return !( (*env)->NewLocalRef(env, NULL) );
}
