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

#ifndef __EXCEPTIONS_SHARE_H
#define __EXCEPTIONS_SHARE_H

#include<jni.h>

extern jclass clazz;            // Class object for ExceptionsTest* class
extern jclass excClass;         // Class object for JNITestException class
extern jclass sofClass;         // Class object for StackOverflowError class
extern jclass oomeClass;        // Class object for OutOfMemoryError class
extern jmethodID mid;           // Method id for nativeMethod()
extern jmethodID cid;           // Method id for JNITestException.<init>
extern jint counter;            // recursion depth counter for ST-tests
extern jint* counters;          // recursion depth counters array for MT-tests

/**
 * Initialize global data when single thread is used in the test
 */
void ExceptionsTest_init(JNIEnv* env,
                         jclass c);

/**
 * Initialize global data when multiple threads are used in the test
 */
void ExceptionsTest_init_MT(JNIEnv* env,
                            jclass c,
                            jint cnt);


#endif
