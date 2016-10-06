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

#define ARG1 ((jfloat)100.00f)
#define RES1 ((jfloat)ARG1)
#define ARG22 ((jfloat)-1.500E-11f)
#define RES2 ((jfloat)(ARG1 + ARG22))
#define ARG3_LENGTH 2
#define ARG31 ((jfloat)-32.755E+22f)
#define ARG32 ((jfloat)132.11f)
#define RES3 ((jfloat)(ARG31 + ARG32))
#define ARG42_LENGTH 3
#define ARG421 ((jfloat)8.81E-19f)
#define ARG422 ((jfloat)-1.f)
#define ARG423 ((jfloat)327.56E+4f)
#define RES4 ((jfloat)((ARG31 + ARG32) + (ARG421 + ARG422 + ARG423)))
#define ARG5 ((jfloat)1002.001f)
#define RES5 ((jfloat)ARG5)
#define ARG62 ((jfloat)48.007E-1f)
#define RES6 ((jfloat)(ARG5 + ARG62))

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.CallNonvirtualFloatMethodTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/object_methods/ChildTestClass;Ljava/lang/Class;Lorg/apache/harmony/vts/test/vm/jni/object_methods/ChildNativeTestClass;Ljava/lang/Class;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_CallNonvirtualFloatMethodTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject tc, jclass parenttc, jobject ntc, jclass parentntc)
{
    jclass fc;
    char *sig1 = "(F)F";
    char *sig2 = "(FF)F";
    char *sig3 = "([F)F";
    char *sig4 = "([F[F)F";
    char *sig5 = "(Ljava/lang/Float;)F";
    char *sig6 = "(Ljava/lang/Float;Ljava/lang/Float;)F";
    jobject fo5, fo62;
    jfloatArray ar3, ar42;
    jfloat *elements3, *elements42;
    jfloat result1, result2, result3, result4, result5, result6;
    jfloat nresult1, nresult2, nresult3, nresult4, nresult5, nresult6;

    jmethodID m1, m2, m3, m4, m5, m6;
    jmethodID nm1, nm2, nm3, nm4, nm5, nm6;
    jmethodID float_init;

    ar3 = (*env)->NewFloatArray(env, ARG3_LENGTH);
    ar42 = (*env)->NewFloatArray(env, ARG42_LENGTH);

    if (NULL == ar3 || NULL == ar42)
        return JNI_FALSE;

    elements3 = (*env)->GetFloatArrayElements(env, ar3, NULL);
    elements42 = (*env)->GetFloatArrayElements(env, ar42, NULL);

    if (NULL == elements3 || NULL == elements42)
        return JNI_FALSE;

    elements3[0] = ARG31;
    elements3[1] = ARG32;

    elements42[0] = ARG421;
    elements42[1] = ARG422;
    elements42[2] = ARG423;

    (*env)->ReleaseFloatArrayElements(env, ar3, elements3, 0);
    (*env)->ReleaseFloatArrayElements(env, ar42, elements42, 0);

    fc = (*env)->FindClass(env, "java/lang/Float");
    if (NULL == fc)
        return JNI_FALSE;
    float_init = (*env)->GetMethodID(env, fc, "<init>", "(F)V");
    if (NULL == float_init)
        return JNI_FALSE;
    fo5 = (*env)->NewObject(env, fc, float_init, ARG5);
    fo62 = (*env)->NewObject(env, fc, float_init, ARG62);
    if (NULL == fo5 || NULL == fo62)
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

    result1 = (*env)->CallNonvirtualFloatMethod(env, tc, parenttc, m1, ARG1);
    result2 = (*env)->CallNonvirtualFloatMethod(env, tc, parenttc, m2, ARG1, ARG22);
    result3 = (*env)->CallNonvirtualFloatMethod(env, tc, parenttc, m3, ar3);
    result4 = (*env)->CallNonvirtualFloatMethod(env, tc, parenttc, m4, ar3, ar42);
    result5 = (*env)->CallNonvirtualFloatMethod(env, tc, parenttc, m5, fo5);
    result6 = (*env)->CallNonvirtualFloatMethod(env, tc, parenttc, m6, fo5, fo62);

    nresult1 = (*env)->CallNonvirtualFloatMethod(env, ntc, parentntc, nm1, ARG1);
    nresult2 = (*env)->CallNonvirtualFloatMethod(env, ntc, parentntc, nm2, ARG1, ARG22);
    nresult3 = (*env)->CallNonvirtualFloatMethod(env, ntc, parentntc, nm3, ar3);
    nresult4 = (*env)->CallNonvirtualFloatMethod(env, ntc, parentntc, nm4, ar3, ar42);
    nresult5 = (*env)->CallNonvirtualFloatMethod(env, ntc, parentntc, nm5, fo5);
    nresult6 = (*env)->CallNonvirtualFloatMethod(env, ntc, parentntc, nm6, fo5, fo62);

    if (result1 - RES1 >0  || nresult1 - RES1 >0  ||
        result2 - RES2 >0  || nresult2 - RES2 >0  ||
        result3 - RES3 >0  || nresult3 - RES3 >0  ||
        result4 - RES4 >0  || nresult4 - RES4 >0  ||
        result5 - RES5 >0  || nresult5 - RES5 >0  ||
        result6 - RES6 >0  || nresult6 - RES6 >0 )
        return JNI_FALSE;
    else
        return JNI_TRUE;
}
