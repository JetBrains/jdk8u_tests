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
#define INVALID_VALUE -1

/*
 * Method: org.apache.harmony.vts.test.vm.jni.references.NewGlobalRefTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/references/RefTestsClass;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_references_NewGlobalRefTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject object)
{
	static jclass obj = NULL;
	static jint hold = 0;
    jfieldID fID;
	jclass className;
	jint result;

	if (obj == NULL)
	{
		className = (*env)->GetObjectClass(env, object);

		if (className == NULL) return JNI_FALSE;
		obj = (*env)->NewGlobalRef(env, className);
		fID = (*env)->GetStaticFieldID(env, obj, "a", "I");
		
		if (fID == NULL) 
		{
	       (*env)->DeleteGlobalRef(env, obj);
			return JNI_FALSE;
		}
		hold = (*env)->GetStaticIntField(env, obj, fID);
		return JNI_TRUE;
	}
	else
	{
	   fID = (*env)->GetStaticFieldID(env, obj, "a", "I");
	   result = fID != NULL ? (*env)->GetStaticIntField(env, obj, fID) : INVALID_VALUE;
       (*env)->DeleteGlobalRef(env, obj);
	   return result != hold + 1 ? JNI_FALSE : JNI_TRUE;
	}
}
