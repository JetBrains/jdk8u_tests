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

#include<stdio.h>
#include<jni.h>
#include<stdlib.h>
#include"StringsTest6.h"
#include"share.h"

JNIEXPORT void JNICALL
Java_org_apache_harmony_test_stress_jni_strings_StringsTest6_init(JNIEnv* env,
                                                                  jclass c) {
  StringsTest_init_no_Java_method(env,c);
}

JNIEXPORT jint JNICALL
Java_org_apache_harmony_test_stress_jni_strings_StringsTest6_nativeMethod(JNIEnv *env,
                                                                          jobject this,
                                                                          jint len,
                                                                          jint maxCnt) {
  int i,cnt;
  jchar* calc_res;
  jchar** arrays;
  
  StringsTest_alloc_arrays(env,len,&calc_res,NULL);
  if(calc_res==NULL) {
    printf("Native code: Cannot allocate temporary array\n");
    return 0;
  }

  //allocate cnt arrays of type jchar
  cnt=StringsTest_alloc_2d_array(env,len,maxCnt,&arrays);
  if(cnt==-1) {
    printf("Native code: Cannot allocate temporary arrays\n");
    return 0;
  }

  //perform calculations on allocated arrays
  printf("Native code: Performing calculations on %d strings\n",cnt);
  for(i=0;i<cnt;i++) {
    const jchar* tmp;
    jstring string;
    jboolean isCopy;
    int critical=1;
    StringsTest_do_calc(arrays[i],calc_res,len);
    string=(*env)->NewString(env,calc_res,len);
    if(string==NULL) {
      printf("Native code: Cannot allocate java.lang.String object\n");
      StringsTest_free_2d_array(env,arrays,cnt);
      StringsTest_free_arrays(env,calc_res,NULL);
      return 0;
    }
    tmp=(*env)->GetStringCritical(env,string,&isCopy);
    if(tmp==NULL) {
      printf("Native code: GetStringCritical() failed\n");
      StringsTest_free_2d_array(env,arrays,cnt);
      StringsTest_free_arrays(env,calc_res,NULL);
      return 0;
    }
    if(isCopy==JNI_TRUE) {
        (*env)->ReleaseStringCritical(env,string,tmp);
        tmp=(*env)->GetStringChars(env,string,NULL);
        if(tmp==NULL) {
          printf("Native code: GetStringChars() failed\n");
          StringsTest_free_2d_array(env,arrays,cnt);
          StringsTest_free_arrays(env,calc_res,NULL);
          return 0;
        }
        critical=0;
    }
    if(!StringsTest_do_compare(calc_res,tmp,len)) {
      if(critical)
        (*env)->ReleaseStringCritical(env,string,tmp);
      else
        (*env)->ReleaseStringChars(env,string,tmp);

      printf("Native code: String comparison failed\n");
      StringsTest_free_2d_array(env,arrays,cnt);
      StringsTest_free_arrays(env,calc_res,NULL);
      return 0;
    }
    if(critical)
        (*env)->ReleaseStringCritical(env,string,tmp);
      else
        (*env)->ReleaseStringChars(env,string,tmp);

    (*env)->DeleteLocalRef(env,string);
  }
  printf("Native code: Done calculations on %d strings\n",cnt);

  StringsTest_free_2d_array(env,arrays,cnt);
  StringsTest_free_arrays(env,calc_res,NULL);

  return 1;
}
