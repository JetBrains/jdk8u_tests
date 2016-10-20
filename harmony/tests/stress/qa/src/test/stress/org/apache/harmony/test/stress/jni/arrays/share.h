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

#ifndef __ARRAYS_SHARE_H
#define __ARRAYS_SHARE_H

#include<jni.h>

extern jclass clazz;                    // Class object for ArraysTestX class
extern jmethodID mid;                   // ID for doCalc() method
extern jclass java_lang_object;         // Class object for java.lang.Object
extern jmethodID cid;                   // ID for java.lang.Object.<init>
extern jclass intArray;                 // Class representing int[] class
extern jclass objectArray;              // Class representing Object[] class

/**
 * Initializes clazz with Class object for ArraysTestX,
 * mid with doCalc([I)[I method
 */
void ArraysTest_init(JNIEnv* env,
                     jclass c);

/**
 * Initializes clazz with Class object for ArraysTestX,
 * mid is NOT initialized
 */
void ArraysTest_init_with_no_Java_method(JNIEnv* env,
                                         jclass c);

/**
 * Initializes clazz with Class object for ArraysTestX,
 * mid with doCalc([Ljava/lang/Object;)[Ljava/lang/Object; method
 */
void ArraysTest_init_with_Object_Java_method(JNIEnv* env,
                                             jclass c);

/**
 * Performs calculations on src array with specified length len,
 * result is stored in dst array (dst must be allocated)
 */
void ArraysTest_do_calc(const jint* src,
                        jint* dst,
                        int len);

/**
 * Compares arr1 and arr2,
 * Result zero if arrays are equal,
 * nonzero if there is at least one index i, where arr1[i] != arr2[i]
 */
int ArraysTest_do_compare(const jint* arr1,
                          const jint* arr2,
                          int len);

/**
 * Allocates several arrays of type jint* with length len.
 * Other parameters are pointers to variables, where arrays will be stored,
 * the list must be terminated by NULL pointer
 */
void ArraysTest_alloc_arrays(JNIEnv* env,
                             int len,...);

/**
 * Frees the arrays allocated by ArraysTest_alloc_arrays() function.
 * Arrays are specified by pointer to their first element (themselves),
 * the list must be terminated by NULL pointer
 */
void ArraysTest_free_arrays(JNIEnv* env,...);

/**
 * Performs calculations on src array with specified length len,
 * result is stored in dst array
 */
void ArraysTest_do_calc_obj(JNIEnv* env,
                            jobjectArray src,
                            jobjectArray dst,
                            int len);

/**
 * Compares arr1 and arr2,
 * Result zero if arrays are equal,
 * nonzero if there is at least one index i, where arr1[i] != arr2[i]
 * Objects are compared with IsSameObject() JNI function
 */
int ArraysTest_do_compare_obj(JNIEnv* env,
                              jobjectArray arr1,
                              jobjectArray arr2,
                              int len);

#endif
