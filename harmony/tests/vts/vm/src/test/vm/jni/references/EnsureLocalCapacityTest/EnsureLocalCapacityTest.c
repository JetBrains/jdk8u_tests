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
 * Method: org.apache.harmony.vts.test.vm.jni.references.EnsureLocalCapacityTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/references/RefTestsClass;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_references_EnsureLocalCapacityTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject obj)
{
	jobject locRef[50] = {NULL};
	jint result = 0;
	int i;

	result = (*env)->EnsureLocalCapacity(env, 50);
	if(result < 0){
		(*env)->ExceptionClear(env);
		return JNI_FALSE;
	}
	for(i = 0; i<50; i++){
		locRef[i] = (*env)->NewLocalRef(env, obj);
		if(!locRef[i]) return JNI_FALSE;
	}
	return JNI_TRUE;
}
