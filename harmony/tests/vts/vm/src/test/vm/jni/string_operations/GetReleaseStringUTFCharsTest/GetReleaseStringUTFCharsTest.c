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
 * Method: org.apache.harmony.vts.test.vm.jni.string_operations.GetReleaseStringUTFCharsTest.nativeExecute(Ljava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL
Java_org_apache_harmony_vts_test_vm_jni_string_1operations_GetReleaseStringUTFCharsTest_nativeExecute
    (JNIEnv *env, jobject this_object, jstring str)
{
    jbyteArray result;
    jbyte *elements;
    const char *telements;
    jsize length;
    int iii;

    length = (*env)->GetStringUTFLength(env, str);
    result = (*env)->NewByteArray(env, length);
    if (NULL == result)
        return NULL;

    elements = (*env)->GetByteArrayElements(env, result, NULL);
    if (NULL == elements)
        return NULL;

    telements = (*env)->GetStringUTFChars(env, str, NULL);
    if (NULL == telements)
        return NULL;

    for (iii = 0; iii < length; iii++)
        elements[iii] = telements[iii];

    (*env)->ReleaseStringUTFChars(env, str, telements);
    (*env)->ReleaseByteArrayElements(env, result, elements, 0);

    return result;
}

