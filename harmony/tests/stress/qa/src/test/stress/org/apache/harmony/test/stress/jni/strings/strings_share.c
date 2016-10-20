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

#include<stdlib.h>
#include"share.h"

jclass clazz;
jmethodID mid;

void StringsTest_init(JNIEnv* env,
                      jclass c) {

  clazz = (*env)->NewGlobalRef(env, c);

  if (clazz == NULL) {
      printf("Native code: Cannot create a global ref to class\n");
      return;
  }
  mid = (*env)->GetStaticMethodID(env, clazz, "doCalc",
        "(Ljava/lang/String;)Ljava/lang/String;");

  if (mid == NULL) {
    printf("Cannot get method ID for doCalc(Ljava/lang/String;)Ljava/lang/String;\n");
    return;
  }
}

void StringsTest_init_no_Java_method(JNIEnv* env,
                                     jclass c) {

  clazz = (*env)->NewGlobalRef(env, c);

  if (clazz == NULL) {
      printf("Native code: Cannot create a global ref to class\n");
      return;
  }
}

void StringsTest_do_calc(jchar* src,
                         jchar* dst,
                         int len) {

  int i;

  for (i = 0; i < len; i++) {
    dst[i] = src[len - 1 - i];
  }
  return;
}

int StringsTest_do_compare(const jchar* str1,
                           const jchar* str2,
                           int len) {
  int i;

  for (i = 0; i < len; i++) {
    if (str1[i] != str2[i]) {
      printf("Native code: Symbols %d and %d at index %d (of %d) are different\n",
          (int) str1[i], (int) str2[i], i, len);
      return 0;
    }
  }
  return 1;
}

void StringsTest_alloc_arrays(JNIEnv* env,
                              int len,...) {

  jchar** arr;
  va_list ap;
  va_start(ap, len);
  arr = va_arg(ap, jchar**);

  while (arr) {
    *arr = malloc(sizeof(jchar) * len);

    if (*arr == NULL) {
      jclass exc;
      exc = (*env)->FindClass(env, "java/lang/OutOfMemoryError");

      if (exc == NULL) {
        printf("Native code: Cannot find java.lang.OutOfMemoryError\n");
        va_end(ap);
        return;
      }
      (*env)->ThrowNew(env, exc, "malloc() failed in native code");
      va_end(ap);
      return;
    }
    arr = va_arg(ap, jchar**);
  }  
  va_end(ap);
  return;
}

void StringsTest_free_arrays(JNIEnv* env,...) {
  jchar* arr;
  va_list ap;
  va_start(ap, env);
  arr = va_arg(ap, jchar*);

  while (arr) {
    free(arr);
    arr = va_arg(ap, jchar*);
  }
  va_end(ap);
  return;
}

int StringsTest_alloc_2d_array(JNIEnv* env,
                               int len,
                               int cnt,
                               jchar*** res) {

  int i;
  *res = (jchar**) malloc(cnt * sizeof(jchar*));

  if (res == NULL) {
    jclass exc;
    exc=(*env)->FindClass(env, "java/lang/OutOfMemoryError");

    if (exc == NULL) {
      printf("Native code: Cannot find java.lang.OutOfMemoryError\n");
      return -1;
    }
    (*env)->ThrowNew(env, exc, "malloc() failed in native code");   
    return -1;
  }

  for (i = 0; i < cnt; i++) {
    int k;
    (*res)[i] = (jchar*) malloc(len * sizeof(jchar));

    if ( (*res)[i] == NULL ) {
      return i;
    }

    for (k = 0; k < len; k++) {
      (*res)[i][k] = 'A' + k % 25;
    }
  }
  return cnt;
}

void StringsTest_free_2d_array(JNIEnv* env,
                               jchar** arr,
                               int cnt) {

  int i;

  for (i = 0; i < cnt; i++) {
    free(arr[i]);
  }
  free(arr);
}
