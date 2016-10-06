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
 * Method: org.apache.harmony.vts.test.vm.jni.reflection.FromReflectedFieldTest.nativeExecute(Ljava/lang/reflect/Field;Ljava/lang/reflect/Field;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_reflection_FromReflectedFieldTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject objField, jobject statField)
{
	jfieldID fID_s1,fID_s2,fID_o1,fID_o2;
	jclass cls;

	fID_s1 = (*env)->FromReflectedField(env, statField);
	fID_o1 = (*env)->FromReflectedField(env, objField);
	if(!fID_o1 || !fID_s1) return JNI_FALSE;

	cls  = (*env)->GetObjectClass(env, this_object);
	if(!cls) return JNI_FALSE;
	fID_s2 = (*env)->GetStaticFieldID(env, cls, "staticField", "I");
	fID_o2 = (*env)->GetFieldID(env, cls, "objectField", "D");
	if(!fID_o2 || !fID_s2) return JNI_FALSE;

    if(fID_o1 != fID_o2) return JNI_FALSE;
    if(fID_s1 != fID_s2) return JNI_FALSE;
	return JNI_TRUE;
}
