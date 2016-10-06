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

/*
 * Method: org.apache.harmony.vts.test.vm.jni.string_operations.NewStringUTFTest.nativeExecute([B)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_apache_harmony_vts_test_vm_jni_string_1operations_NewStringUTFTest_nativeExecute
    (JNIEnv *env, jobject this_object, jbyteArray bytes)
{
    jbyte *elements;
    char *string;
    jstring str;
    int iii;
    int length;

    elements = (*env)->GetByteArrayElements(env, bytes, NULL);
    if (NULL == elements)
        return NULL;

    length = (*env)->GetArrayLength(env, bytes);
    string = malloc(length + 1);
    if (NULL == string)
        return NULL;

    for(iii = 0; iii < length; iii++)
        string[iii] = elements[iii];
    string[length] = 0;

    str = (*env)->NewStringUTF(env, string);
    free(string);

    (*env)->ReleaseByteArrayElements(env, bytes, elements, 0);
    return str;
}
