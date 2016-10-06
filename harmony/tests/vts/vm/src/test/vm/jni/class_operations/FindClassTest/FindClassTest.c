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
 * Method: org.apache.harmony.vts.test.vm.jni.class_operations.FindClassTest.nativeExecute()Ljava/lang/Class;
 */
JNIEXPORT jclass JNICALL
Java_org_apache_harmony_vts_test_vm_jni_class_1operations_FindClassTest_nativeExecute
    (JNIEnv *env, jobject this_class)
{
	jclass classMath;
	jmethodID mid;
	jint res;

	classMath = (*env)->FindClass(env, "java/lang/Math");
	if(classMath == NULL) return NULL;
	mid = (*env)->GetStaticMethodID(env, classMath, "abs", "(I)I");
	if (mid == NULL) return NULL;
    res = (*env)->CallStaticIntMethod(env,classMath, mid, 123);
	if(res != 123) return NULL;
    return (*env)->FindClass(env, "org/apache/harmony/vts/test/vm/jni/class_operations/FindClassTest");
}
