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
 * Method: org.apache.harmony.vts.test.vm.jni.references.DeleteWeakGlobalRefTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/references/RefTestsClass;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_references_DeleteWeakGlobalRefTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject obj)
{
	jweak ref;
	jfieldID fID;
	jclass className;
	jint result = 0;

	ref = (*env)->NewWeakGlobalRef(env, obj);
	if(!ref) return JNI_FALSE;
	className = (*env)->GetObjectClass(env, ref);
	if(!className) return JNI_FALSE;
	fID = (*env)->GetFieldID(env, className, "d", "C");
	if(!fID) return JNI_FALSE;
	(*env)->SetIntField(env, ref, fID, (jint)2);
	result = (*env)->GetIntField(env, ref, fID);
	(*env)->DeleteWeakGlobalRef(env, ref);

	return result == 2;
}
