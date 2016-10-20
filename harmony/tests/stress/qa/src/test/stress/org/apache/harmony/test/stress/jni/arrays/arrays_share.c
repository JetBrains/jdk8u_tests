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

#include"share.h"
#include<stdlib.h>

jclass clazz;
jclass java_lang_object;
jclass intArray;
jclass objectArray;
jmethodID mid;
jmethodID cid;

void ArraysTest_init(JNIEnv* env,
                     jclass c) {

  clazz = (*env)->NewGlobalRef(env, c);
  if (clazz == NULL) {
      printf("Native code: Cannot create a global ref to class\n");
      return;
  }

  mid = (*env)->GetMethodID(env, clazz, "doCalc", "([I)[I");
  if (mid == NULL) {
    printf("Cannot get method ID for doCalc([I)[I\n");
    return;
  }

  intArray = (*env)->FindClass(env, "[I");

  if (intArray == NULL) {
    printf("Native code: Cannot get class for int[]\n");
    return;
  }

  intArray = (*env)->NewGlobalRef(env, intArray);

  if (intArray == NULL) {
    printf("Native code: Cannot create a global ref for int[]\n");
    return;
  }

  objectArray=(*env)->FindClass(env,"[Ljava/lang/Object;");

  if (objectArray == NULL) {
    printf("Native code: Cannot get class for java.lang.Object[]\n");
    return;
  }

  objectArray=(*env)->NewGlobalRef(env,objectArray);

  if (objectArray == NULL) {
    printf("Native code: Cannot create a global ref to java.lang.Object[]\n");
    return;
  }

  setvbuf(stdout, (char*)NULL, _IONBF, 0);
}

void ArraysTest_init_with_Object_Java_method(JNIEnv* env,
                                             jclass c) {

  clazz = (*env)->NewGlobalRef(env, c);
  
  if (clazz == NULL) {
      printf("Native code: Cannot create a global ref to class\n");
      return;
  }

  mid = (*env)->GetMethodID(env, clazz, "doCalc",
        "([Ljava/lang/Object;)[Ljava/lang/Object;");

  if (mid == NULL) {
    printf(
      "Cannot get method ID for doCalc([Ljava/lang/Object;)[Ljava/lang/Object;\n");
    return;
  }

  java_lang_object = (*env)->FindClass(env, "java/lang/Object");
  
  if (java_lang_object == NULL) {
    printf("Native code: Cannot get class for java.lang.Object\n");
    return;
  }

  java_lang_object = (*env)->NewGlobalRef(env, java_lang_object);
  
  if (java_lang_object == NULL) {
    printf("Native code: Cannot create global ref for java.lang.Object\n");
    return;
  }

  cid = (*env)->GetMethodID(env, java_lang_object,"<init>", "()V");
  
  if (cid == NULL) {
    printf("Native code: Cannot get constructor ID for java.lang.Object\n");
    return;
  }

  intArray = (*env)->FindClass(env, "[I");

  if (intArray == NULL) {
    printf("Native code: Cannot get class for int[]\n");
    return;
  }

  intArray = (*env)->NewGlobalRef(env, intArray);

  if (intArray == NULL) {
    printf("Native code: Cannot create a global ref for int[]\n");
    return;
  }

  objectArray=(*env)->FindClass(env,"[Ljava/lang/Object;");

  if (objectArray == NULL) {
    printf("Native code: Cannot get class for java.lang.Object[]\n");
    return;
  }

  objectArray=(*env)->NewGlobalRef(env,objectArray);

  if (objectArray == NULL) {
    printf("Native code: Cannot create a global ref to java.lang.Object[]\n");
    return;
  }

  setvbuf(stdout, (char*)NULL, _IONBF, 0);
}

void ArraysTest_init_with_no_Java_method(JNIEnv* env,
                                         jclass c) {

  clazz = (*env)->NewGlobalRef(env, c);

  if (clazz == NULL) {
      printf("Native code: Cannot create a global ref to class\n");
      return;
  }
  
  java_lang_object = (*env)->FindClass(env, "java/lang/Object");
  
  if (java_lang_object == NULL) {
    printf("Native code: Cannot get class for java.lang.Object\n");
    return;
  }

  java_lang_object = (*env)->NewGlobalRef(env, java_lang_object);
  
  if (java_lang_object == NULL) {
    printf("Native code: Cannot create global ref for java.lang.Object\n");
    return;
  }

  cid = (*env)->GetMethodID(env, java_lang_object, "<init>", "()V");
  
  if (cid == NULL) {
    printf("Native code: Cannot get constructor ID for java.lang.Object\n");
    return;
  }

  intArray = (*env)->FindClass(env, "[I");

  if (intArray == NULL) {
    printf("Native code: Cannot get class for int[]\n");
    return;
  }

  intArray = (*env)->NewGlobalRef(env, intArray);

  if (intArray == NULL) {
    printf("Native code: Cannot create a global ref for int[]\n");
    return;
  }

  objectArray=(*env)->FindClass(env,"[Ljava/lang/Object;");

  if (objectArray == NULL) {
    printf("Native code: Cannot get class for java.lang.Object[]\n");
    return;
  }

  objectArray=(*env)->NewGlobalRef(env,objectArray);

  if (objectArray == NULL) {
    printf("Native code: Cannot create a global ref to java.lang.Object[]\n");
    return;
  }
  
  setvbuf(stdout,(char*)NULL, _IONBF, 0);
}

void ArraysTest_do_calc(const jint* src,
                        jint* dst,
                        int len) {

  int i;
  for (i = 0; i < len; i++) {
    dst[i] = src[len - 1 - i];
  }
  return;
}

int ArraysTest_do_compare(const jint* arr1,
                          const jint* arr2,
                          int len) {

  int i;
  for (i = 0; i < len; i++) {
    if (arr1[i] != arr2[i]) {
      return 0;
    }
  }
  return 1;
}

void ArraysTest_do_calc_obj(JNIEnv* env,
                            jobjectArray src,
                            jobjectArray dst,
                            int len) {

  int i;
  for (i = 0; i < len; i++) {
      jobject obj = (*env)->GetObjectArrayElement(env, src, i);
      if( (*env)->ExceptionCheck(env) ) {
          printf("Native code: Error getting array element of type java.lang.Object, index=%d\n",
              i);
          return;
      }
      (*env)->SetObjectArrayElement(env, dst, len - i - 1, obj);
  }
}

int ArraysTest_do_compare_obj(JNIEnv* env,
                              jobjectArray arr1,
                              jobjectArray arr2,
                              int len) {
  int i;
  for(i = 0;i < len; i++) {
      jobject obj1, obj2;

      obj1 = (*env)->GetObjectArrayElement(env, arr1, i);

      if ( (*env)->ExceptionCheck(env) ) {
          printf("Native code: Error getting array element of type java.lang.Object, index=%d\n",
              i);
          return 0;
      }
      
      obj2 = (*env)->GetObjectArrayElement(env, arr2, i);

      if ( (*env)->ExceptionCheck(env) ) {
          printf("Native code: Error getting array element of type java.lang.Object, index=%d\n",
              i);
          return 0;
      }

      if ( !(*env)->IsSameObject(env, obj1, obj2) ) {
          return 0;
      }
  }

  return 1;
}

/*void ArraysTest_alloc_arrays(JNIEnv* env,
                             int len,...) {
    
  jint** arr;
  va_list ap;
  va_start(ap, len);
  arr=va_arg(ap, jint**);

  while (arr) {
    *arr = malloc(sizeof(jint) * len);
    if (*arr == NULL) {
      jclass exc;
      exc=(*env)->FindClass(env, "java/lang/OutOfMemoryError");

      if (exc == NULL) {
        printf("Native code: Cannot find java.lang.OutOfMemoryError\n");
        va_end(ap);
        return;
      }

      (*env)->ThrowNew(env, exc, "malloc() failed in native code");
      va_end(ap);
      return;
    }
    arr = va_arg(ap, jint**);
  }

  va_end(ap);
}

void ArraysTest_free_arrays(JNIEnv* env,...) {
    
  jint* arr;
  va_list ap;
  va_start(ap, env);
  arr = va_arg(ap, jint*);

  while (arr) {
    free(arr);
    arr = va_arg(ap, jint*);
  }

  va_end(ap);
}*/

JNIEXPORT jlong JNICALL
Java_org_apache_harmony_test_stress_jni_arrays_share_STArraysTest_allocIntArray(
    JNIEnv *env,
    jclass c,
    jint len) {

  return (jlong) malloc(sizeof(jint) * len);
}

JNIEXPORT jlong JNICALL
Java_org_apache_harmony_test_stress_jni_arrays_share_MTArraysTest_allocIntArray(
    JNIEnv *env,
    jclass c,
    jint len) {

  return (jlong) malloc(sizeof(jint) * len);
}
