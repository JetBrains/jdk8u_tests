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
 * Method: org.apache.harmony.vts.test.vm.jni.references.PushPopLocalFrameTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/references/RefTestsClass;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_references_PushPopLocalFrameTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject obj)
{
	jobject refs[26];
	int i;

	for(i = 0; i < 15; i++){//loop fills only 15 local refs, one more is required for return value from frame pop
		refs[i] = (*env)->NewLocalRef(env, obj);
		if(!refs[i]) return JNI_FALSE;
	}
	if((*env)->PushLocalFrame(env, 10) < 0){
		(*env)->ExceptionClear(env);
		return JNI_FALSE;
	}
	for(i = 15; i < 25; i++){
		refs[i] = (*env)->NewLocalRef(env, obj);
		if(!refs[i]) return JNI_FALSE;
	}
	if( (*env)->IsSameObject(env, refs[0],refs[24]) == JNI_FALSE ) return JNI_FALSE;
    refs[25] = (*env)->PopLocalFrame(env, refs[24]);
	return (*env)->IsSameObject(env, refs[0],refs[25]);
}
