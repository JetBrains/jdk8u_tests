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
#include <malloc.h>
#include <stdio.h>

/*
 * Method: org.apache.harmony.vts.test.vm.jni.string_operations.GetStringUTFRegionTest.nativeExecute(Ljava/lang/String;III)[B
 */
JNIEXPORT jbyteArray JNICALL
Java_org_apache_harmony_vts_test_vm_jni_string_1operations_GetStringUTFRegionTest_nativeExecute
    (JNIEnv *env, jobject this_object, jstring str, jint start, jint length, jint count)
{
    jbyteArray result;
    jbyte *elements;
    char *buf = malloc((*env)->GetStringUTFLength(env, str) + 1);
    int bytes_number, iii;

    if (NULL == buf)
        return NULL;

    (*env)->GetStringUTFRegion(env, str, start, length, buf);

    result = (*env)->NewByteArray(env, count);
    if (NULL == result)
        return NULL;

    elements = (*env)->GetByteArrayElements(env, result, NULL);
    if (NULL == elements)
        return NULL;

    for (iii = 0; iii < count; iii++)
        elements[iii] = buf[iii];

    (*env)->ReleaseByteArrayElements(env, result, elements, 0);
    free(buf);

    return result;
}
