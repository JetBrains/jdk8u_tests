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

#define ZARG1 ((jboolean)(JNI_FALSE))
#define ZARG2 ((jboolean)(JNI_FALSE))
#define ZARG3 ((jboolean)(JNI_TRUE))
#define ZRES ((jboolean)(ZARG1 | ZARG2 | ZARG3))

#define BARG1 ((jbyte)(1))
#define BARG2 ((jbyte)(12))
#define BARG3 ((jbyte)(-127))
#define BRES ((jbyte)(BARG1 + BARG2 + BARG3))

#define CARG1 ((jchar)(1000))
#define CARG2 ((jchar)(11212))
#define CARG3 ((jchar)(12721))
#define CRES ((jchar)(CARG1 + CARG2 + CARG3))

#define SARG1 ((jshort)(1231))
#define SARG2 ((jshort)(3412))
#define SARG3 ((jshort)(-12722))
#define SRES ((jshort)(SARG1 + SARG2 + SARG3))

#define IARG1 ((jint)(1345131))
#define IARG2 ((jint)(154151342))
#define IARG3 ((jint)(-1254157))
#define IRES ((jint)(IARG1 + IARG2 + IARG3))

#define JARG1 ((jlong)(134513451134351ll))
#define JARG2 ((jlong)(113451345431132ll))
#define JARG3 ((jlong)(-13451354341527ll))
#define JRES ((jlong)(JARG1 + JARG2 + JARG3))

#define FARG1 ((jfloat)(1345.1E-1f))
#define FARG2 ((jfloat)(-11232.001E+4f))
#define FARG3 ((jfloat)(-1.27E-11f))
#define FRES ((jfloat)(FARG1 + FARG2 + FARG3))

#define DARG1 ((jdouble)(-31231E+11))
#define DARG2 ((jdouble)(1.200003E+8))
#define DARG3 ((jdouble)(-21.27E+98))
#define DRES ((jdouble)(DARG1 + DARG2 + DARG3))

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.CallObjectMethodATest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/object_methods/TestClass;Lorg/apache/harmony/vts/test/vm/jni/object_methods/NativeTestClass;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_CallObjectMethodATest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject tc, jobject ntc)
{
    jmethodID zm, bm, cm, sm, im, jm, fm, dm;
    jmethodID nzm, nbm, ncm, nsm, nim, njm, nfm, ndm;
    jmethodID zl, bl, cl, sl, il, jl, fl, dl;
    jmethodID nzl, nbl, ncl, nsl, nil, njl, nfl, ndl;
    jobject oz, ob, oc, os, oi, oj, of, od;
    jobject noz, nob, noc, nos, noi, noj, nof, nod;
    jclass zc, bc, cc, sc, ic, jc, fc, dc;
    jclass nzc, nbc, ncc, nsc, nic, njc, nfc, ndc;
    jclass clazz, nclazz;
    char *sigz = "(ZZZ)Ljava/lang/Boolean;";
    char *sigb = "(BBB)Ljava/lang/Byte;";
    char *sigc = "(CCC)Ljava/lang/Character;";
    char *sigs = "(SSS)Ljava/lang/Short;";
    char *sigi = "(III)Ljava/lang/Integer;";
    char *sigj = "(JJJ)Ljava/lang/Long;";
    char *sigf = "(FFF)Ljava/lang/Float;";
    char *sigd = "(DDD)Ljava/lang/Double;";
        jvalue zargs[3], bargs[3], cargs[3], sargs[3], iargs[3],
                jargs[3], fargs[3], dargs[3];

    clazz = (*env)->GetObjectClass(env, tc);
    nclazz = (*env)->GetObjectClass(env, ntc);

    zm = (*env)->GetMethodID(env, clazz, "method", sigz);
    bm = (*env)->GetMethodID(env, clazz, "method", sigb);
    cm = (*env)->GetMethodID(env, clazz, "method", sigc);
    sm = (*env)->GetMethodID(env, clazz, "method", sigs);
    im = (*env)->GetMethodID(env, clazz, "method", sigi);
    jm = (*env)->GetMethodID(env, clazz, "method", sigj);
    fm = (*env)->GetMethodID(env, clazz, "method", sigf);
    dm = (*env)->GetMethodID(env, clazz, "method", sigd);

    if (NULL == zm || NULL == bm || NULL == cm || NULL == sm ||
        NULL == im || NULL == jm || NULL == fm || NULL == dm)
        return JNI_FALSE;

    nzm = (*env)->GetMethodID(env, nclazz, "method", sigz);
    nbm = (*env)->GetMethodID(env, nclazz, "method", sigb);
    ncm = (*env)->GetMethodID(env, nclazz, "method", sigc);
    nsm = (*env)->GetMethodID(env, nclazz, "method", sigs);
    nim = (*env)->GetMethodID(env, nclazz, "method", sigi);
    njm = (*env)->GetMethodID(env, nclazz, "method", sigj);
    nfm = (*env)->GetMethodID(env, nclazz, "method", sigf);
    ndm = (*env)->GetMethodID(env, nclazz, "method", sigd);

    if (NULL == nzm || NULL == nbm || NULL == ncm || NULL == nsm ||
        NULL == nim || NULL == njm || NULL == nfm || NULL == ndm)
        return JNI_FALSE;

        zargs[0].z = ZARG1;
        zargs[1].z = ZARG2;
        zargs[2].z = ZARG3;

        bargs[0].b = BARG1;
        bargs[1].b = BARG2;
        bargs[2].b = BARG3;

        cargs[0].c = CARG1;
        cargs[1].c = CARG2;
        cargs[2].c = CARG3;

        sargs[0].s = SARG1;
        sargs[1].s = SARG2;
        sargs[2].s = SARG3;

        iargs[0].i = IARG1;
        iargs[1].i = IARG2;
        iargs[2].i = IARG3;

        jargs[0].j = JARG1;
        jargs[1].j = JARG2;
        jargs[2].j = JARG3;

        fargs[0].f = FARG1;
        fargs[1].f = FARG2;
        fargs[2].f = FARG3;

        dargs[0].d = DARG1;
        dargs[1].d = DARG2;
        dargs[2].d = DARG3;

    oz = (*env)->CallObjectMethodA(env, tc, zm, zargs);
    ob = (*env)->CallObjectMethodA(env, tc, bm, bargs);
    oc = (*env)->CallObjectMethodA(env, tc, cm, cargs);
    os = (*env)->CallObjectMethodA(env, tc, sm, sargs);
    oi = (*env)->CallObjectMethodA(env, tc, im, iargs);
    oj = (*env)->CallObjectMethodA(env, tc, jm, jargs);
    of = (*env)->CallObjectMethodA(env, tc, fm, fargs);
    od = (*env)->CallObjectMethodA(env, tc, dm, dargs);

    noz = (*env)->CallObjectMethodA(env, ntc, nzm, zargs);
    nob = (*env)->CallObjectMethodA(env, ntc, nbm, bargs);
    noc = (*env)->CallObjectMethodA(env, ntc, ncm, cargs);
    nos = (*env)->CallObjectMethodA(env, ntc, nsm, sargs);
    noi = (*env)->CallObjectMethodA(env, ntc, nim, iargs);
    noj = (*env)->CallObjectMethodA(env, ntc, njm, jargs);
    nof = (*env)->CallObjectMethodA(env, ntc, nfm, fargs);
    nod = (*env)->CallObjectMethodA(env, ntc, ndm, dargs);

    zc = (*env)->GetObjectClass(env, oz);
    bc = (*env)->GetObjectClass(env, ob);
    cc = (*env)->GetObjectClass(env, oc);
    sc = (*env)->GetObjectClass(env, os);
    ic = (*env)->GetObjectClass(env, oi);
    jc = (*env)->GetObjectClass(env, oj);
    fc = (*env)->GetObjectClass(env, of);
    dc = (*env)->GetObjectClass(env, od);

    nzc = (*env)->GetObjectClass(env, noz);
    nbc = (*env)->GetObjectClass(env, nob);
    ncc = (*env)->GetObjectClass(env, noc);
    nsc = (*env)->GetObjectClass(env, nos);
    nic = (*env)->GetObjectClass(env, noi);
    njc = (*env)->GetObjectClass(env, noj);
    nfc = (*env)->GetObjectClass(env, nof);
    ndc = (*env)->GetObjectClass(env, nod);

    zl = (*env)->GetMethodID(env, zc, "booleanValue", "()Z");
    bl = (*env)->GetMethodID(env, bc, "byteValue", "()B");
    cl = (*env)->GetMethodID(env, cc, "charValue", "()C");
    sl = (*env)->GetMethodID(env, sc, "shortValue", "()S");
    il = (*env)->GetMethodID(env, ic, "intValue", "()I");
    jl = (*env)->GetMethodID(env, jc, "longValue", "()J");
    fl = (*env)->GetMethodID(env, fc, "floatValue", "()F");
    dl = (*env)->GetMethodID(env, dc, "doubleValue", "()D");

    nzl = (*env)->GetMethodID(env, nzc, "booleanValue", "()Z");
    nbl = (*env)->GetMethodID(env, nbc, "byteValue", "()B");
    ncl = (*env)->GetMethodID(env, ncc, "charValue", "()C");
    nsl = (*env)->GetMethodID(env, nsc, "shortValue", "()S");
    nil = (*env)->GetMethodID(env, nic, "intValue", "()I");
    njl = (*env)->GetMethodID(env, njc, "longValue", "()J");
    nfl = (*env)->GetMethodID(env, nfc, "floatValue", "()F");
    ndl = (*env)->GetMethodID(env, ndc, "doubleValue", "()D");

    if (ZRES != (*env)->CallBooleanMethod(env, oz, zl) ||
        ZRES != (*env)->CallBooleanMethod(env, noz, nzl))
        return JNI_FALSE;

    if (BRES != (*env)->CallByteMethod(env, ob, bl) ||
        BRES != (*env)->CallByteMethod(env, nob, nbl))
        return JNI_FALSE;

    if (CRES != (*env)->CallCharMethod(env, oc, cl) ||
        CRES != (*env)->CallCharMethod(env, noc, ncl))
        return JNI_FALSE;

    if (SRES != (*env)->CallShortMethod(env, os, sl) ||
        SRES != (*env)->CallShortMethod(env, nos, nsl))
        return JNI_FALSE;

    if (IRES != (*env)->CallIntMethod(env, oi, il) ||
        IRES != (*env)->CallIntMethod(env, noi, nil))
        return JNI_FALSE;

    if (JRES != (*env)->CallLongMethod(env, oj, jl) ||
        JRES != (*env)->CallLongMethod(env, noj, njl))
        return JNI_FALSE;

    if (FRES - (*env)->CallFloatMethod(env, of, fl) > 0 ||
        FRES - (*env)->CallFloatMethod(env, nof, nfl) > 0 )
        return JNI_FALSE;

    if (DRES - (*env)->CallDoubleMethod(env, od, dl) > 0  ||
        DRES - (*env)->CallDoubleMethod(env, nod, ndl) > 0 )
        return JNI_FALSE;

    return JNI_TRUE;
}
