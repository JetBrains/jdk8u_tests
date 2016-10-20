/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.4 $
 */

#include<jni.h>
#include<stdlib.h>
#include<stdarg.h>

jclass clazz;
jmethodID mid;
jint maxCnt;
jthrowable oomeObj;

void StackTest_init(JNIEnv* env,
                    jobject obj,
                    jint cnt,
                    int multithread) {
  jclass oomeClass;
  jmethodID oomeConstrID;
  clazz = (*env)->GetObjectClass(env, obj);
  clazz = (*env)->NewGlobalRef(env, clazz);
  if (clazz == NULL) {
      printf("Native code: Cannot create a global ref to class\n");
      return;
  }

  if (!multithread) {
    mid = (*env)->GetStaticMethodID(env, clazz, "nativeMethod", "([III)[I");
  } else {
    mid = (*env)->GetStaticMethodID(env, clazz, "nativeMethod", "([IIII)[I");
  }
  if (mid == NULL) {
    printf("Cannot get method ID for nativeMethod()\n");
    return;
  }
  maxCnt = cnt;
  setvbuf(stdout, (char*)NULL, _IONBF, 0);
  oomeClass = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
  if (oomeClass == NULL) {
    printf("Native code: Cannot initialize java.lang.Class object for OOME\n");
  }
  oomeConstrID = (*env)->GetMethodID(env, oomeClass, "<init>", "()V");
  oomeObj = (*env)->NewObject(env, oomeClass, oomeConstrID);
  if (oomeObj == NULL) {
    printf("Native code: Cannot allocate OOME object\n");
  }
  oomeObj = (*env)->NewGlobalRef(env, oomeObj);
  if (oomeObj == NULL) {
    printf("Native code: Cannot create a global ref to an OOME object\n");
  }
}

void StackTest_init_with_Java_method(JNIEnv* env,
                                     jobject obj,
                                     jint cnt) {
  clazz = (*env)->GetObjectClass(env, obj);
  clazz = (*env)->NewGlobalRef(env, clazz);
  if (clazz == NULL) {
      printf("Native code: Cannot create a global ref to class\n");
      return;
  }

  mid = (*env)->GetMethodID(env, clazz, "doCalc", "([II)[I");
  if (mid == NULL) {
    printf("Cannot get method ID for doCalc()\n");
    return;
  }
  maxCnt = cnt;
  setvbuf(stdout, (char*)NULL, _IONBF, 0);
}

void StackTest_do_calc(jint* arr,
                       jint* res,
                       jint len) {
  int i;
  for(i=0;i<len;i++) res[len-i-1]=arr[i];
}

void StackTest_alloc_arrays(JNIEnv* env,
                            int len,...) {
    
  jint** arr;
  va_list ap;
  va_start(ap, len);
  arr = va_arg(ap, jint**);
  
  while (arr) {
    *arr = malloc(sizeof(jint) * len);
    if (*arr == NULL) {
      (*env)->Throw(env, oomeObj);
      va_end(ap);
      return;
    }
    arr = va_arg(ap, jint**);
  }  
  va_end(ap);
}

void StackTest_free_arrays(JNIEnv* env,...) {
    
  jint* arr;
  va_list ap;
  va_start(ap, env);
  arr = va_arg(ap, jint*);
  
  while( arr) {
    free(arr);
    arr = va_arg(ap, jint*);
  }
  va_end(ap);
}
