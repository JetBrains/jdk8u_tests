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

#define ARGB1 ((jbyte)100ll)
#define ARGB2 ((jbyte)-1ll)
#define ARGB3 ((jbyte)-127ll)
#define ARGC1 ((jchar)10000ll)
#define ARGC2 ((jchar)1212ll)
#define ARGS1 ((jshort)-32767ll)
#define ARGS2 ((jshort)18ll)
#define ARGS3 ((jshort)2048ll)
#define ARGI1 ((jint)98234238ll)
#define ARGI2 ((jint)-91238112ll)
#define ARGI3 ((jint)1829183ll)
#define ARGJ1 ((jlong)8723487134ll)
#define ARGJ2 ((jlong)-813838218ll)
#define ARGJ3 ((jlong)87481923ll)

#define RES1 (ARGB1 + ARGC1 + ARGS1 + ARGI1 + ARGJ1)
#define RES2 (ARGB2 + ARGC2 + ARGS2 + ARGI2 + ARGJ2)
#define RES3 (ARGB3 + ARGS3 + ARGI3 + ARGJ3)
#define RES (RES1 + RES2 + RES3)

static jlong callNI(JNIEnv *env, jobject obj, jmethodID mid, ...)
{
    va_list args;
    jlong result;

    va_start(args, mid);
    result = (*env)->CallLongMethodV(env, obj, mid, args);
    va_end(args);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.CallNonvirtualComplexMethodTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/object_methods/ChildTestClass;Ljava/lang/Class;Lorg/apache/harmony/vts/test/vm/jni/object_methods/ChildNativeTestClass;Ljava/lang/Class;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_CallNonvirtualComplexMethodTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject tc, jclass parenttc, jobject ntc, jclass parentntc)
{
    jmethodID mid, nmid;
    char *sig = "(BCSIJLjava/lang/Byte;Ljava/lang/Character;Ljava/lang/Short;"
        "Ljava/lang/Integer;Ljava/lang/Long;"
        "Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;"
        "Ljava/lang/String;)J";
    jobject ob, oc, os, oi, oj;
    jstring sb, ss, si, sj;
    jclass strc, bc, cc, sc, ic, jc;
    jmethodID binit, cinit, sinit, iinit, jinit;
    jmethodID istrc, jstrc;
    jlong res, resA, resV, nres, nresA, nresV;
    jvalue arargs[14];
    jlong result = RES;
    jlong d1 = RES1, d2 = RES2, d3 = RES3;

    mid = (*env)->GetMethodID(env, parenttc, "method", sig);
    nmid = (*env)->GetMethodID(env, parentntc, "method", sig);

    if (NULL == mid || NULL == nmid)
        return JNI_FALSE;

    bc = (*env)->FindClass(env, "java/lang/Byte");
    cc = (*env)->FindClass(env, "java/lang/Character");
    sc = (*env)->FindClass(env, "java/lang/Short");
    ic = (*env)->FindClass(env, "java/lang/Integer");
    jc = (*env)->FindClass(env, "java/lang/Long");

    if (NULL == bc || NULL == cc || NULL == sc || NULL == ic || NULL == jc)
        return JNI_FALSE;

    binit = (*env)->GetMethodID(env, bc, "<init>", "(B)V");
    cinit = (*env)->GetMethodID(env, cc, "<init>", "(C)V");
    sinit = (*env)->GetMethodID(env, sc, "<init>", "(S)V");
    iinit = (*env)->GetMethodID(env, ic, "<init>", "(I)V");
    jinit = (*env)->GetMethodID(env, jc, "<init>", "(J)V");

    if (NULL == binit || NULL == cinit || NULL == sinit || NULL == iinit ||
        NULL == jinit)
        return JNI_FALSE;

    ob = (*env)->NewObject(env, bc, binit, ARGB2);
    oc = (*env)->NewObject(env, cc, cinit, ARGC2);
    os = (*env)->NewObject(env, sc, sinit, ARGS2);
    oi = (*env)->NewObject(env, ic, iinit, ARGI2);
    oj = (*env)->NewObject(env, jc, jinit, ARGJ2);

    strc = (*env)->FindClass(env, "java/lang/String");
    istrc = (*env)->GetStaticMethodID(env, strc, "valueOf", "(I)Ljava/lang/String;");
    jstrc = (*env)->GetStaticMethodID(env, strc, "valueOf", "(J)Ljava/lang/String;");

    sb = (*env)->CallStaticObjectMethod(env, strc, istrc, ARGB3);
    ss = (*env)->CallStaticObjectMethod(env, strc, istrc, ARGS3);
    si = (*env)->CallStaticObjectMethod(env, strc, istrc, ARGI3);
    sj = (*env)->CallStaticObjectMethod(env, strc, jstrc, ARGJ3);

    arargs[0].b = ARGB1;
    arargs[1].c = ARGC1;
    arargs[2].s = ARGS1;
    arargs[3].i = ARGI1;
    arargs[4].j = ARGJ1;
    arargs[5].l = ob;
    arargs[6].l = oc;
    arargs[7].l = os;
    arargs[8].l = oi;
    arargs[9].l = oj;
    arargs[10].l = sb;
    arargs[11].l = ss;
    arargs[12].l = si;
    arargs[13].l = sj;

    res = (*env)->CallLongMethod(env, tc, mid, ARGB1, ARGC1, ARGS1, ARGI1,
        ARGJ1, ob, oc, os, oi, oj, sb, ss, si, sj);
    resA = (*env)->CallLongMethodA(env, tc, mid, arargs);
    resV = callNI(env, tc, mid, ARGB1, ARGC1, ARGS1, ARGI1,
        ARGJ1, ob, oc, os, oi, oj, sb, ss, si, sj);

    nres = (*env)->CallLongMethod(env, ntc, nmid, ARGB1, ARGC1, ARGS1, ARGI1,
        ARGJ1, ob, oc, os, oi, oj, sb, ss, si, sj);
    nresA = (*env)->CallLongMethodA(env, ntc, nmid, arargs);
    nresV = callNI(env, ntc, nmid, ARGB1, ARGC1, ARGS1, ARGI1,
        ARGJ1, ob, oc, os, oi, oj, sb, ss, si, sj);

    if (res != result)
        return JNI_FALSE;

    if (resA != result)
        return JNI_FALSE;

    if (resV != result)
        return JNI_FALSE;

    if (nres != result)
        return JNI_FALSE;

    if (nresA != result)
        return JNI_FALSE;

    if (nresV != result)
        return JNI_FALSE;

    return JNI_TRUE;
}
