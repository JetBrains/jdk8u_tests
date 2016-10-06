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
#include <stdio.h>
#include <malloc.h>

/*
 * Method: org.apache.harmony.vts.test.vm.jni.NIOSupport.NewDirectByteBufferTest.nativeExecute()Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_NIOSupport_NewDirectByteBufferTest_nativeExecute
    (JNIEnv *env, jobject this_object)
{
	char *arr = malloc(10 * sizeof(char));
	int i; 
    for(i = 0; i < 10; i++){
		arr[i] = (char)i;
	}
	return (*env)->NewDirectByteBuffer(env, arr, 10);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.NIOSupport.NewDirectByteBufferTest.nativeExecuteRelease(Ljava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_NIOSupport_NewDirectByteBufferTest_nativeExecuteRelease
    (JNIEnv *env, jobject this_object, jobject buf)
{
	int i;
	jboolean result = JNI_TRUE;
	char *ptr = (char*)(*env)->GetDirectBufferAddress(env, buf);
	if(!ptr) result = JNI_FALSE;
	for(i = 0; i < 10; i++){
		if(ptr[i] != (char)(i + 1)) result = JNI_FALSE;
	}
	free(ptr);
	return result;
}
