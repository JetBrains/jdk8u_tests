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
#include <stdlib.h>
/*
 * Method: org.apache.harmony.vts.test.vm.jni.array_operations.GetSetIntArrayRegionTest.nativeExecute([II)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_array_1operations_GetSetIntArrayRegionTest_nativeExecute
    (JNIEnv *env, jobject this_object, jintArray array, jint length)
{
	jint* buf;
	buf = malloc (length / 2 * sizeof(jint) );
	(*env)->GetIntArrayRegion(env, array, length / 2, length / 2, buf);
	(*env)->SetIntArrayRegion(env, array, 0, length/2, buf);
	free(buf);
}
