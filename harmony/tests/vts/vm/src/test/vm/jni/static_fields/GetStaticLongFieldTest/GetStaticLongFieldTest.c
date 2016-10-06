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
 * Method: org.apache.harmony.vts.test.vm.jni.static_fields.GetStaticLongFieldTest.nativeExecute(Ljava/lang/Class;Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_static_1fields_GetStaticLongFieldTest_nativeExecute
    (JNIEnv *env, jobject this_object, jclass clazz, jstring field_name)
{
    const char *name;
    jfieldID fID;

    name = (*env)->GetStringUTFChars(env, field_name, NULL);
    if (NULL == name)
        return JNI_FALSE;

    fID = (*env)->GetStaticFieldID(env, clazz, name, "J");
    (*env)->ReleaseStringUTFChars(env, field_name, name);

    if (NULL == fID)
        return JNI_FALSE;

    return (*env)->GetStaticLongField(env, clazz, fID);
}
