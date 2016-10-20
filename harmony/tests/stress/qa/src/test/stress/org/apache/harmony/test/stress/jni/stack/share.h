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

#ifndef __STACK_SHARE_H
#define __STACK_SHARE_H

extern jclass clazz;
extern jmethodID mid;
extern jint maxCnt;

void StackTest_do_calc(jint* arr,
                       jint* res,
                       jint len);

void StackTest_init(JNIEnv* env,
                    jclass c,
                    jint cnt,
                    int multithread);

void StackTest_init_with_Java_method(JNIEnv* env,
                                     jclass c,
                                     jint cnt);

void StackTest_alloc_arrays(JNIEnv* env,
                            int len,...);

void StackTest_free_arrays(JNIEnv* env,...);

#endif
