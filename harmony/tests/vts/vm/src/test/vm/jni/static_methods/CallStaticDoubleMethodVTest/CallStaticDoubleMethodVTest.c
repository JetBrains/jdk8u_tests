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

#define ARG1 ((jdouble)100.00)
#define RES1 ((jdouble)ARG1)
#define ARG22 ((jdouble)-1.500E-110)
#define RES2 ((jdouble)(ARG1 + ARG22))
#define ARG3_LENGTH 2
#define ARG31 ((jdouble)-32.755E+232)
#define ARG32 ((jdouble)132.1123423423)
#define RES3 ((jdouble)(ARG31 + ARG32))
#define ARG42_LENGTH 3
#define ARG421 ((jdouble)8.81E-191)
#define ARG422 ((jdouble)-1.)
#define ARG423 ((jdouble)3223143127.561234E+4)
#define RES4 ((jdouble)((ARG31 + ARG32) + (ARG421 + ARG422 + ARG423)))
#define ARG5 ((jdouble)112432312002.00000001)
#define RES5 ((jdouble)ARG5)
#define ARG62 ((jdouble)48.012307E-1)
#define RES6 ((jdouble)(ARG5 + ARG62))

static jdouble callNI(JNIEnv *env, jclass par_cl, jmethodID mid, ...)
{
    va_list args;
        jdouble result;

        va_start(args, mid);
        result = (*env)->CallStaticDoubleMethodV(env, par_cl, mid, args);
        va_end(args);
        return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.static_methods.CallStaticDoubleMethodVTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/static_methods/TestClass;Lorg/apache/harmony/vts/test/vm/jni/static_methods/NativeTestClass;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_static_1methods_CallStaticDoubleMethodVTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject tc, jobject ntc)
{
    jclass dc, class1, class2;
    char *sig1 = "(D)D";
    char *sig2 = "(DD)D";
    char *sig3 = "([D)D";
    char *sig4 = "([D[D)D";
    char *sig5 = "(Ljava/lang/Double;)D";
    char *sig6 = "(Ljava/lang/Double;Ljava/lang/Double;)D";
    jobject do5, do62;
    jdoubleArray ar3, ar42;
    jdouble *elements3, *elements42;
    jdouble result1, result2, result3, result4, result5, result6;
    jdouble nresult1, nresult2, nresult3, nresult4, nresult5, nresult6;

    jmethodID m1, m2, m3, m4, m5, m6;
    jmethodID nm1, nm2, nm3, nm4, nm5, nm6;
    jmethodID double_init;

    ar3 = (*env)->NewDoubleArray(env, ARG3_LENGTH);
    ar42 = (*env)->NewDoubleArray(env, ARG42_LENGTH);

    if (NULL == ar3 || NULL == ar42)
        return JNI_FALSE;

    elements3 = (*env)->GetDoubleArrayElements(env, ar3, NULL);
    elements42 = (*env)->GetDoubleArrayElements(env, ar42, NULL);

    if (NULL == elements3 || NULL == elements42)
        return JNI_FALSE;

    elements3[0] = ARG31;
    elements3[1] = ARG32;

    elements42[0] = ARG421;
    elements42[1] = ARG422;
    elements42[2] = ARG423;

    (*env)->ReleaseDoubleArrayElements(env, ar3, elements3, 0);
    (*env)->ReleaseDoubleArrayElements(env, ar42, elements42, 0);

    dc = (*env)->FindClass(env, "java/lang/Double");
    if (NULL == dc)
        return JNI_FALSE;
    double_init = (*env)->GetMethodID(env, dc, "<init>", "(D)V");
    if (NULL == double_init)
        return JNI_FALSE;
    do5 = (*env)->NewObject(env, dc, double_init, ARG5);
    do62 = (*env)->NewObject(env, dc, double_init, ARG62);
    if (NULL == do5 || NULL == do62)
        return JNI_FALSE;

    class1 = (*env)->GetObjectClass(env, tc);
    class2 = (*env)->GetObjectClass(env, ntc);

    m1 = (*env)->GetStaticMethodID(env, class1, "method", sig1);
    m2 = (*env)->GetStaticMethodID(env, class1, "method", sig2);
    m3 = (*env)->GetStaticMethodID(env, class1, "method", sig3);
    m4 = (*env)->GetStaticMethodID(env, class1, "method", sig4);
    m5 = (*env)->GetStaticMethodID(env, class1, "method", sig5);
    m6 = (*env)->GetStaticMethodID(env, class1, "method", sig6);

    if (NULL == m1 || NULL == m2 || NULL == m3 ||
        NULL == m4 || NULL == m5 || NULL == m6)
        return JNI_FALSE;

    nm1 = (*env)->GetStaticMethodID(env, class2, "method", sig1);
    nm2 = (*env)->GetStaticMethodID(env, class2, "method", sig2);
    nm3 = (*env)->GetStaticMethodID(env, class2, "method", sig3);
    nm4 = (*env)->GetStaticMethodID(env, class2, "method", sig4);
    nm5 = (*env)->GetStaticMethodID(env, class2, "method", sig5);
    nm6 = (*env)->GetStaticMethodID(env, class2, "method", sig6);

    if (NULL == nm1 || NULL == nm2 || NULL == nm3 ||
        NULL == nm4 || NULL == nm5 || NULL == nm6)
        return JNI_FALSE;

    result1 = callNI(env, class1, m1, ARG1);
    result2 = callNI(env, class1, m2, ARG1, ARG22);
    result3 = callNI(env, class1, m3, ar3);
    result4 = callNI(env, class1, m4, ar3, ar42);
    result5 = callNI(env, class1, m5, do5);
    result6 = callNI(env, class1, m6, do5, do62);

    nresult1 = callNI(env, class2, nm1, ARG1);
    nresult2 = callNI(env, class2, nm2, ARG1, ARG22);
    nresult3 = callNI(env, class2, nm3, ar3);
    nresult4 = callNI(env, class2, nm4, ar3, ar42);
    nresult5 = callNI(env, class2, nm5, do5);
    nresult6 = callNI(env, class2, nm6, do5, do62);

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
