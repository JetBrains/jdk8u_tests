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
 
#include<stdio.h>
#include<jni.h>
#include<stdlib.h>

JNIEXPORT void JNICALL Java_org_apache_harmony_test_func_reg_vm_btest5440_Btest5440_nativeMethod(
        JNIEnv *env, jobject this, jint len, jint maxCnt) {

    int i;
    int cnt;
    jchar** arrays;

    arrays= (jchar**) malloc(sizeof(jchar*) *maxCnt);
    if(arrays==NULL) {
        printf("Native code: malloc() failed\n");
        return;
    }

    for (cnt=0; cnt < maxCnt; cnt++) {
        arrays[cnt] = (jchar*) malloc(sizeof(jchar) *len);
        if(arrays[cnt] == NULL) {
            break;
        }
    }

    printf("Native code: %d arrays allocated\n", cnt);
    for(i = 0; i < cnt; i++) {
        free(arrays[i]);
    }
    free(arrays);
    
    printf("Native code: %d arrays freed\n", cnt);
}
