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
 * Method: org.apache.harmony.vts.test.vm.jvms.instructions.invokeReturn.invokevirtual.invokevirtual21.invokevirtual2103.invokevirtual2103p.nativeMethod(ZBCSIJLjava/lang/Object;[I)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jvms_instructions_invokeReturn_invokevirtual_invokevirtual21_invokevirtual2103_invokevirtual2103p_nativeMethod(JNIEnv *, jobject, jboolean, jbyte, jchar, jshort, jint, jlong, jobject, jarray);

JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jvms_instructions_invokeReturn_invokevirtual_invokevirtual21_invokevirtual2103_invokevirtual2103p_nativeMethod
    (JNIEnv *env, jobject obj, jboolean a0, jbyte a1, jchar a2, jshort a3, jint a4, jlong a5, jobject this_obj, jarray arr)
{
    jclass cls, this_cls;
	jfieldID fid;

	if (a0 == JNI_TRUE && a1 == -127 && a2 == 0xffff && a3 == 104 && a4 == 2 && a5 == 1) 
    {
	  if((*env)->IsSameObject(env, this_obj, obj) && 
			(*env)->GetArrayLength(env, arr) == 3) 
      {
         cls = (*env)->GetObjectClass(env, obj);
	     fid = (*env)->GetStaticFieldID(env,cls,"testField","I");
	     (*env)->SetStaticIntField(env, obj, fid, (jint)104);
      }
    }

  return;
}
