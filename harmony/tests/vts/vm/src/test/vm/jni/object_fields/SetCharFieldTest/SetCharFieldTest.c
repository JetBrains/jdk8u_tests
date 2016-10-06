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
 * Method: org.apache.harmony.vts.test.vm.jni.object_fields.SetCharFieldTest.nativeExecute(Ljava/lang/Object;Ljava/lang/String;C)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1fields_SetCharFieldTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject obj, jstring field_name, jchar value)
{
    const char *name;
    jclass clazz = (*env)->GetObjectClass(env, obj);
    jfieldID fID;

    name = (*env)->GetStringUTFChars(env, field_name, NULL);
    if (NULL == name)
        return JNI_FALSE;

    fID = (*env)->GetFieldID(env, clazz, name, "C");
    (*env)->ReleaseStringUTFChars(env, field_name, name);

    if (NULL == fID)
        return JNI_FALSE;

    (*env)->SetCharField(env, obj, fID, value);
    return JNI_TRUE;
}
