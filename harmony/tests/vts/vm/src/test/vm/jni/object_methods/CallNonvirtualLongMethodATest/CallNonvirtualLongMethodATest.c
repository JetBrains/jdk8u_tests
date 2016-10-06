/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
  
#include <jni.h>

#define ARG1 ((jlong)10098374ll)
#define RES1 ((jlong)ARG1)
#define ARG22 ((jlong)150034513451ll)
#define RES2 ((jlong)(ARG1 + ARG22))
#define ARG3_LENGTH 2
#define ARG31 ((jlong)-32755134541351ll)
#define ARG32 ((jlong)1321113451345134ll)
#define RES3 ((jlong)(ARG31 + ARG32))
#define ARG42_LENGTH 3
#define ARG421 ((jlong)881345134514513ll)
#define ARG422 ((jlong)-1ll)
#define ARG423 ((jlong)32756353151545143ll)
#define RES4 ((jlong)((ARG31 + ARG32) + (ARG421 + ARG422 + ARG423)))
#define ARG5 ((jlong)1002131454113145ll)
#define RES5 ((jlong)ARG5)
#define ARG62 ((jlong)-4800234123412341234ll)
#define RES6 ((jlong)(ARG5 + ARG62))

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.CallNonvirtualLongMethodATest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/object_methods/ChildTestClass;Ljava/lang/Class;Lorg/apache/harmony/vts/test/vm/jni/object_methods/ChildNativeTestClass;Ljava/lang/Class;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_CallNonvirtualLongMethodATest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject tc, jclass parenttc, jobject ntc, jclass parentntc)
{
    jclass jc;
    char *sig1 = "(J)J";
    char *sig2 = "(JJ)J";
    char *sig3 = "([J)J";
    char *sig4 = "([J[J)J";
    char *sig5 = "(Ljava/lang/Long;)J";
    char *sig6 = "(Ljava/lang/Long;Ljava/lang/Long;)J";
    jobject jo5, jo62;
    jlongArray ar3, ar42;
    jlong *elements3, *elements42;
    jlong result1, result2, result3, result4, result5, result6;
    jlong nresult1, nresult2, nresult3, nresult4, nresult5, nresult6;
    jvalue args1[1], args2[2], args3[1], args4[2], args5[1], args6[2];

    jmethodID m1, m2, m3, m4, m5, m6;
    jmethodID nm1, nm2, nm3, nm4, nm5, nm6;
    jmethodID long_init;

    ar3 = (*env)->NewLongArray(env, ARG3_LENGTH);
    ar42 = (*env)->NewLongArray(env, ARG42_LENGTH);

    if (NULL == ar3 || NULL == ar42)
        return JNI_FALSE;

    elements3 = (*env)->GetLongArrayElements(env, ar3, NULL);
    elements42 = (*env)->GetLongArrayElements(env, ar42, NULL);

    if (NULL == elements3 || NULL == elements42)
        return JNI_FALSE;

    elements3[0] = ARG31;
    elements3[1] = ARG32;

    elements42[0] = ARG421;
    elements42[1] = ARG422;
    elements42[2] = ARG423;

    (*env)->ReleaseLongArrayElements(env, ar3, elements3, 0);
    (*env)->ReleaseLongArrayElements(env, ar42, elements42, 0);

    jc = (*env)->FindClass(env, "java/lang/Long");
    if (NULL == jc)
        return JNI_FALSE;
    long_init = (*env)->GetMethodID(env, jc, "<init>", "(J)V");
    if (NULL == long_init)
        return JNI_FALSE;
    jo5 = (*env)->NewObject(env, jc, long_init, ARG5);
    jo62 = (*env)->NewObject(env, jc, long_init, ARG62);
    if (NULL == jo5 || NULL == jo62)
        return JNI_FALSE;

    m1 = (*env)->GetMethodID(env, parenttc, "method", sig1);
    m2 = (*env)->GetMethodID(env, parenttc, "method", sig2);
    m3 = (*env)->GetMethodID(env, parenttc, "method", sig3);
    m4 = (*env)->GetMethodID(env, parenttc, "method", sig4);
    m5 = (*env)->GetMethodID(env, parenttc, "method", sig5);
    m6 = (*env)->GetMethodID(env, parenttc, "method", sig6);

    if (NULL == m1 || NULL == m2 || NULL == m3 ||
        NULL == m4 || NULL == m5 || NULL == m6)
        return JNI_FALSE;

    nm1 = (*env)->GetMethodID(env, parentntc, "method", sig1);
    nm2 = (*env)->GetMethodID(env, parentntc, "method", sig2);
    nm3 = (*env)->GetMethodID(env, parentntc, "method", sig3);
    nm4 = (*env)->GetMethodID(env, parentntc, "method", sig4);
    nm5 = (*env)->GetMethodID(env, parentntc, "method", sig5);
    nm6 = (*env)->GetMethodID(env, parentntc, "method", sig6);

    if (NULL == nm1 || NULL == nm2 || NULL == nm3 ||
        NULL == nm4 || NULL == nm5 || NULL == nm6)
        return JNI_FALSE;

    args1[0].j = ARG1;
    args2[0].j = ARG1;
    args2[1].j = ARG22;
    args3[0].l = ar3;
    args4[0].l = ar3;
    args4[1].l = ar42;
    args5[0].l = jo5;
    args6[0].l = jo5;
    args6[1].l = jo62;

    result1 = (*env)->CallNonvirtualLongMethodA(env, tc, parenttc, m1, args1);
    result2 = (*env)->CallNonvirtualLongMethodA(env, tc, parenttc, m2, args2);
    result3 = (*env)->CallNonvirtualLongMethodA(env, tc, parenttc, m3, args3);
    result4 = (*env)->CallNonvirtualLongMethodA(env, tc, parenttc, m4, args4);
    result5 = (*env)->CallNonvirtualLongMethodA(env, tc, parenttc, m5, args5);
    result6 = (*env)->CallNonvirtualLongMethodA(env, tc, parenttc, m6, args6);

    nresult1 = (*env)->CallNonvirtualLongMethodA(env, ntc, parentntc, nm1, args1);
    nresult2 = (*env)->CallNonvirtualLongMethodA(env, ntc, parentntc, nm2, args2);
    nresult3 = (*env)->CallNonvirtualLongMethodA(env, ntc, parentntc, nm3, args3);
    nresult4 = (*env)->CallNonvirtualLongMethodA(env, ntc, parentntc, nm4, args4);
    nresult5 = (*env)->CallNonvirtualLongMethodA(env, ntc, parentntc, nm5, args5);
    nresult6 = (*env)->CallNonvirtualLongMethodA(env, ntc, parentntc, nm6, args6);

    if (result1 != RES1 || nresult1 != RES1 ||
        result2 != RES2 || nresult2 != RES2 ||
        result3 != RES3 || nresult3 != RES3 ||
        result4 != RES4 || nresult4 != RES4 ||
        result5 != RES5 || nresult5 != RES5 ||
        result6 != RES6 || nresult6 != RES6)
        return JNI_FALSE;
    else
        return JNI_TRUE;
}
