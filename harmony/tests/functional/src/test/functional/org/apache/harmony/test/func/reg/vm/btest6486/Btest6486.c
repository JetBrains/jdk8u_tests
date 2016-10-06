/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 */

#include <jni.h>
JNIEXPORT jboolean JNICALL Java_org_apache_harmony_test_func_reg_vm_btest6486_Btest6486_nativeFunction(JNIEnv *, jobject);

JNIEXPORT jboolean JNICALL Java_org_apache_harmony_test_func_reg_vm_btest6486_Btest6486_nativeFunction(JNIEnv *jenv,
    jobject t)
{
    jclass oome_class = (*jenv)->FindClass(jenv, "java/lang/OutOfMemoryError");
    jclass obj_class;

    if (!oome_class)
        return JNI_FALSE;

    obj_class = (*jenv)->FindClass(jenv, "org/apache/harmony/test/func/reg/vm/btest6486/Btest6486");

    if (!obj_class)
        return JNI_FALSE;

    for (;;)
    {
        jobject obj = (*jenv)->AllocObject(jenv, obj_class);
        if (!obj)
        {
            jobject exn = (*jenv)->ExceptionOccurred(jenv);
            jclass exn_class;

            if (!exn)
                return JNI_FALSE;

            (*jenv)->ExceptionClear(jenv);
            exn_class = (*jenv)->GetObjectClass(jenv, exn);

            if ((*jenv)->IsAssignableFrom(jenv, exn_class, oome_class))
                return JNI_TRUE;
            else
                return JNI_FALSE;
        }
    }
}
