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

#ifndef __STRINGS_SHARE_H
#define __STRINGS_SHARE_H

#include<jni.h>

extern jclass clazz;            // Class object for StringsTest* class
extern jmethodID mid;           // Method ID for doCalc() method

/**
 * Initialization method for global data
 */
void StringsTest_init(JNIEnv* env,
                      jclass c);

/**
 * Initialization method for global data
 * (doCalc() method is NOT initialized)
 */
void StringsTest_init_no_Java_method(JNIEnv* env,
                                     jclass c);

/**
 * Function to perform calculations on strings
 * params: src - source char array, dst - resulting array
 * len - string length
 */
void StringsTest_do_calc(jchar* src,
                         jchar* dst,
                         int len);

/**
 * Function to compare two strings
 * params: str1, str2 - strings to compare, len - length of strings
 * returns: <0 if str1<str2, =0 if str1==str2, >0 if str1>str2
 */
int StringsTest_do_compare(const jchar* str1,
                           const jchar* str2,
                           int len);

/**
 * Function to allocate jchar arrays,
 * params: len - length of arrays to allocate,
 * a list of pointers where to store pointers to allocated arrays
 * terminated by NULL pointer
 */
void StringsTest_alloc_arrays(JNIEnv* env,
                              int len,...);

/**
 * Function to free jchar arrays allocated by StringTest_alloc_arrays()
 * function.
 * params: list of arrays to free terminated by NULL
 */
void StringsTest_free_arrays(JNIEnv* env,...);

/**
 * Function to allocated 2-dimensional (jchar**) array
 * params: len - length of jchar arrays (number of columns),
 * cnt - max. number of jchar arrays (number of rows)
 * res - resulting array
 */
int StringsTest_alloc_2d_array(JNIEnv* env,
                               int len,
                               int cnt,
                               jchar*** res);

/**
 * Function to free 2-dimensional array allocated by
 * StringsTest_alloc_2d_array() function
 * params: arr - array to be freed, cnt - number of rows in 2d-array
 */
void StringsTest_free_2d_array(JNIEnv* env,
                               jchar** arr,
                               int cnt);


#endif
