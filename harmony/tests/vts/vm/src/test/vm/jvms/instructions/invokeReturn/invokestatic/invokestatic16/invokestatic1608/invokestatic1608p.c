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
 * Method: org.apache.harmony.vts.test.vm.jvms.instructions.invokeReturn.invokestatic.invokestatic16.invokestatic1608.invokestatic1608p.nativeMethod(DDDDDD)I
 */
JNIEXPORT int JNICALL
Java_org_apache_harmony_vts_test_vm_jvms_instructions_invokeReturn_invokestatic_invokestatic16_invokestatic1608_invokestatic1608p_nativeMethod(JNIEnv *, jclass, jdouble, jdouble, jdouble, jdouble, jdouble, jdouble);

JNIEXPORT int JNICALL
Java_org_apache_harmony_vts_test_vm_jvms_instructions_invokeReturn_invokestatic_invokestatic16_invokestatic1608_invokestatic1608p_nativeMethod
    (JNIEnv *env, jclass this_cls, jdouble f0, jdouble f1, jdouble f2, jdouble f3, jdouble f4, jdouble f5)
{

  jfieldID fid0, fid1, fid2, fid3, fid4;
  jdouble jf0, jf1, jf2, jf3, jf4;
  jmethodID mid;

  
  jclass cls = (*env)->FindClass(env, "java/lang/Double");
  if (cls == NULL) 
  {
     fprintf (stderr,"\tjava/lang/Double is not found\n");
     return 105;
  }
  fid0 = (*env)->GetStaticFieldID(env,cls,"MIN_VALUE","D");
  fid1 = (*env)->GetStaticFieldID(env,cls,"MAX_VALUE","D");
  fid2 = (*env)->GetStaticFieldID(env,cls,"NEGATIVE_INFINITY","D");
  fid3 = (*env)->GetStaticFieldID(env,cls,"POSITIVE_INFINITY","D");
  fid4 = (*env)->GetStaticFieldID(env,cls,"NaN","D");

  if (fid0 == NULL || fid1 == NULL || fid2 == NULL || fid3 == NULL || fid4 == NULL) 
  {
     fprintf (stderr,"\tcan not get a fieldID\n");
     return 105;
  }

  jf0 = (*env)->GetStaticDoubleField(env,this_cls,fid0); 
  jf1 = (*env)->GetStaticDoubleField(env,this_cls,fid1); 
  jf2 = (*env)->GetStaticDoubleField(env,this_cls,fid2); 
  jf3 = (*env)->GetStaticDoubleField(env,this_cls,fid3); 
  jf4 = (*env)->GetStaticDoubleField(env,this_cls,fid4); 

  
  mid =  (*env)->GetStaticMethodID(env,cls,"isNaN","(D)Z");

  if (jf0 == f0 && jf1 == f1 && jf2 == f2 && jf3 == f3 && (*env)->CallStaticBooleanMethod(env,cls,mid, f4)) 
  {
    return 104;
  }

  return 105;
}
