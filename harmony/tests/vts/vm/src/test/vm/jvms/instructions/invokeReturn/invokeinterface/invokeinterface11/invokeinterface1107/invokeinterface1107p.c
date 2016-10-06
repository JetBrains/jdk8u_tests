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
 * Method: org.apache.harmony.vts.test.vm.jvms.instructions.invokeReturn.invokeinterface.invokeinterface11.invokeinterface1107.invokeinterface1107p.nativeMethod(FFFFFF)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jvms_instructions_invokeReturn_invokeinterface_invokeinterface11_invokeinterface1107_invokeinterface1107p_nativeMethod(JNIEnv *, jobject, jfloat, jfloat, jfloat, jfloat, jfloat, jfloat);


JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jvms_instructions_invokeReturn_invokeinterface_invokeinterface11_invokeinterface1107_invokeinterface1107p_nativeMethod
    (JNIEnv *env, jobject obj, jfloat f0, jfloat f1, jfloat f2, jfloat f3, jfloat f4, jfloat f5)
{

  jfieldID fid0, fid1, fid2, fid3, fid4;
  jfloat jf0, jf1, jf2, jf3, jf4;
  jmethodID mid;

  jclass this_cls = (*env)->GetObjectClass(env, obj);
  
  jclass cls = (*env)->FindClass(env, "java/lang/Float");
  if (cls == NULL) 
  {
     fprintf (stderr,"\tjava/lang/Float is not found\n");
     return 105;
  }
  fid0 = (*env)->GetStaticFieldID(env,cls,"MIN_VALUE","F");
  fid1 = (*env)->GetStaticFieldID(env,cls,"MAX_VALUE","F");
  fid2 = (*env)->GetStaticFieldID(env,cls,"NEGATIVE_INFINITY","F");
  fid3 = (*env)->GetStaticFieldID(env,cls,"POSITIVE_INFINITY","F");
  fid4 = (*env)->GetStaticFieldID(env,cls,"NaN","F");

  if (fid0 == NULL || fid1 == NULL || fid2 == NULL || fid3 == NULL || fid4 == NULL) 
  {
     fprintf (stderr,"\tcan not get a fieldID\n");
     return 105;
  }

  jf0 = (*env)->GetStaticFloatField(env,this_cls,fid0); 
  jf1 = (*env)->GetStaticFloatField(env,this_cls,fid1); 
  jf2 = (*env)->GetStaticFloatField(env,this_cls,fid2); 
  jf3 = (*env)->GetStaticFloatField(env,this_cls,fid3); 
  jf4 = (*env)->GetStaticFloatField(env,this_cls,fid4); 

  
  mid =  (*env)->GetStaticMethodID(env,cls,"isNaN","(F)Z");

  if (jf0 == f0 && jf1 == f1 && jf2 == f2 && jf3 == f3 && (*env)->CallStaticBooleanMethod(env,cls,mid, f4) && f5 == (jfloat)3.14) 
  {
    return 104;
  }

  return 105;

}
