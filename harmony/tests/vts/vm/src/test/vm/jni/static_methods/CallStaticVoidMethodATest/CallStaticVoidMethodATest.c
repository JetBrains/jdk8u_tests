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
#define ZARG3 ((jboolean)(JNI_FALSE))
#define ZARG4 ((jboolean)(JNI_FALSE))
#define ZRES ((jboolean)(ZARG1 | ZARG2 | ZARG3 | ZARG4))

#define BARG1 ((jbyte)(1))
#define BARG2 ((jbyte)(12))
#define BARG3 ((jbyte)(-127))
#define BARG4 ((jbyte)(0))
#define BRES ((jbyte)(BARG1 + BARG2 + BARG3 + BARG4))

#define CARG1 ((jchar)(1000))
#define CARG2 ((jchar)(11212))
#define CARG3 ((jchar)(12721))
#define CARG4 ((jchar)(832))
#define CRES ((jchar)(CARG1 + CARG2 + CARG3 + CARG4))

#define SARG1 ((jshort)(1231))
#define SARG2 ((jshort)(3412))
#define SARG3 ((jshort)(-12722))
#define SARG4 ((jshort)(3410))
#define SRES ((jshort)(SARG1 + SARG2 + SARG3 + SARG4))

#define IARG1 ((jint)(1345131))
#define IARG2 ((jint)(154151342))
#define IARG3 ((jint)(-1254157))
#define IARG4 ((jint)(541540))
#define IRES ((jint)(IARG1 + IARG2 + IARG3 + IARG4))

#define JARG1 ((jlong)(134513451134351ll))
#define JARG2 ((jlong)(113451345431132ll))
#define JARG3 ((jlong)(-13451354341527ll))
#define JARG4 ((jlong)(1345134513513450ll))
#define JRES ((jlong)(JARG1 + JARG2 + JARG3 + JARG4))

#define FARG1 ((jfloat)(1345.1E-1f))
#define FARG2 ((jfloat)(-11232.001E+4f))
#define FARG3 ((jfloat)(-1.27E-11f))
#define FARG4 ((jfloat)(0.0012E+2f))
#define FRES ((jfloat)(FARG1 + FARG2 + FARG3 + FARG4))

#define DARG1 ((jdouble)(-31231E+11))
#define DARG2 ((jdouble)(1.200003E+8))
#define DARG3 ((jdouble)(-21.27E+98))
#define DARG4 ((jdouble)(12.121213212))
#define DRES ((jdouble)(DARG1 + DARG2 + DARG3 + DARG4))

/*
 * Method: org.apache.harmony.vts.test.vm.jni.static_methods.CallStaticVoidMethodATest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/static_methods/TestClass;Lorg/apache/harmony/vts/test/vm/jni/static_methods/NativeTestClass;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_static_1methods_CallStaticVoidMethodATest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject tc, jobject ntc)
{
    jmethodID zm, bm, cm, sm, im, jm, fm, dm;
    jmethodID nzm, nbm, ncm, nsm, nim, njm, nfm, ndm;
    jfieldID zf, bf, cf, sf, _if, jf, ff, df;
    jfieldID nzf, nbf, ncf, nsf, nif, njf, nff, ndf;
    jclass class1, class2;
    char *sigz = "(ZZZZ)V";
    char *sigb = "(BBBB)V";
    char *sigc = "(CCCC)V";
    char *sigs = "(SSSS)V";
    char *sigi = "(IIII)V";
    char *sigj = "(JJJJ)V";
    char *sigf = "(FFFF)V";
    char *sigd = "(DDDD)V";
        jvalue zargs[4], bargs[4], cargs[4], sargs[4], iargs[4],
                jargs[4], fargs[4], dargs[4];

    class1 = (*env)->GetObjectClass(env, tc);
    class2 = (*env)->GetObjectClass(env, ntc);

    zm = (*env)->GetStaticMethodID(env, class1, "method", sigz);
    bm = (*env)->GetStaticMethodID(env, class1, "method", sigb);
    cm = (*env)->GetStaticMethodID(env, class1, "method", sigc);
    sm = (*env)->GetStaticMethodID(env, class1, "method", sigs);
    im = (*env)->GetStaticMethodID(env, class1, "method", sigi);
    jm = (*env)->GetStaticMethodID(env, class1, "method", sigj);
    fm = (*env)->GetStaticMethodID(env, class1, "method", sigf);
    dm = (*env)->GetStaticMethodID(env, class1, "method", sigd);

    if (NULL == zm || NULL == bm || NULL == cm || NULL == sm ||
        NULL == im || NULL == jm || NULL == fm || NULL == dm)
        return JNI_FALSE;

    nzm = (*env)->GetStaticMethodID(env, class2, "method", sigz);
    nbm = (*env)->GetStaticMethodID(env, class2, "method", sigb);
    ncm = (*env)->GetStaticMethodID(env, class2, "method", sigc);
    nsm = (*env)->GetStaticMethodID(env, class2, "method", sigs);
    nim = (*env)->GetStaticMethodID(env, class2, "method", sigi);
    njm = (*env)->GetStaticMethodID(env, class2, "method", sigj);
    nfm = (*env)->GetStaticMethodID(env, class2, "method", sigf);
    ndm = (*env)->GetStaticMethodID(env, class2, "method", sigd);

    if (NULL == nzm || NULL == nbm || NULL == ncm || NULL == nsm ||
        NULL == nim || NULL == njm || NULL == nfm || NULL == ndm)
        return JNI_FALSE;

    zf = (*env)->GetStaticFieldID(env, class1, "retz", "Z");
    bf = (*env)->GetStaticFieldID(env, class1, "retb", "B");
    cf = (*env)->GetStaticFieldID(env, class1, "retc", "C");
    sf = (*env)->GetStaticFieldID(env, class1, "rets", "S");
    _if = (*env)->GetStaticFieldID(env, class1, "reti", "I");
    jf = (*env)->GetStaticFieldID(env, class1, "retj", "J");
    ff = (*env)->GetStaticFieldID(env, class1, "retf", "F");
    df = (*env)->GetStaticFieldID(env, class1, "retd", "D");

    if (NULL == zf || NULL == bf || NULL == cf || NULL == sf ||
        NULL == _if || NULL == jf || NULL == ff || NULL == df)
        return JNI_FALSE;

    nzf = (*env)->GetStaticFieldID(env, class2, "retz", "Z");
    nbf = (*env)->GetStaticFieldID(env, class2, "retb", "B");
    ncf = (*env)->GetStaticFieldID(env, class2, "retc", "C");
    nsf = (*env)->GetStaticFieldID(env, class2, "rets", "S");
    nif = (*env)->GetStaticFieldID(env, class2, "reti", "I");
    njf = (*env)->GetStaticFieldID(env, class2, "retj", "J");
    nff = (*env)->GetStaticFieldID(env, class2, "retf", "F");
    ndf = (*env)->GetStaticFieldID(env, class2, "retd", "D");

    if (NULL == nzf || NULL == nbf || NULL == ncf || NULL == nsf ||
        NULL == nif || NULL == njf || NULL == nff || NULL == ndf)
        return JNI_FALSE;

        zargs[0].z = ZARG1;
        zargs[1].z = ZARG2;
        zargs[2].z = ZARG3;
        zargs[3].z = ZARG4;

        bargs[0].b = BARG1;
        bargs[1].b = BARG2;
        bargs[2].b = BARG3;
        bargs[3].b = BARG4;

        cargs[0].c = CARG1;
        cargs[1].c = CARG2;
        cargs[2].c = CARG3;
        cargs[3].c = CARG4;

        sargs[0].s = SARG1;
        sargs[1].s = SARG2;
        sargs[2].s = SARG3;
        sargs[3].s = SARG4;

        iargs[0].i = IARG1;
        iargs[1].i = IARG2;
        iargs[2].i = IARG3;
        iargs[3].i = IARG4;

        jargs[0].j = JARG1;
        jargs[1].j = JARG2;
        jargs[2].j = JARG3;
        jargs[3].j = JARG4;

        fargs[0].f = FARG1;
        fargs[1].f = FARG2;
        fargs[2].f = FARG3;
        fargs[3].f = FARG4;

        dargs[0].d = DARG1;
        dargs[1].d = DARG2;
        dargs[2].d = DARG3;
        dargs[3].d = DARG4;

    (*env)->CallStaticVoidMethodA(env, class1, zm, zargs);
    (*env)->CallStaticVoidMethodA(env, class1, bm, bargs);
    (*env)->CallStaticVoidMethodA(env, class1, cm, cargs);
    (*env)->CallStaticVoidMethodA(env, class1, sm, sargs);
    (*env)->CallStaticVoidMethodA(env, class1, im, iargs);
    (*env)->CallStaticVoidMethodA(env, class1, jm, jargs);
    (*env)->CallStaticVoidMethodA(env, class1, fm, fargs);
    (*env)->CallStaticVoidMethodA(env, class1, dm, dargs);

    (*env)->CallStaticVoidMethodA(env, class2, nzm, zargs);
    (*env)->CallStaticVoidMethodA(env, class2, nbm, bargs);
    (*env)->CallStaticVoidMethodA(env, class2, ncm, cargs);
    (*env)->CallStaticVoidMethodA(env, class2, nsm, sargs);
    (*env)->CallStaticVoidMethodA(env, class2, nim, iargs);
    (*env)->CallStaticVoidMethodA(env, class2, njm, jargs);
    (*env)->CallStaticVoidMethodA(env, class2, nfm, fargs);
    (*env)->CallStaticVoidMethodA(env, class2, ndm, dargs);

    if (ZRES != (*env)->GetStaticBooleanField(env, class1, zf) ||
        ZRES != (*env)->GetStaticBooleanField(env, class2, nzf))
        return JNI_FALSE;

    if (BRES != (*env)->GetStaticByteField(env, class1, bf) ||
        BRES != (*env)->GetStaticByteField(env, class2, nbf))
        return JNI_FALSE;

    if (CRES != (*env)->GetStaticCharField(env, class1, cf) ||
        CRES != (*env)->GetStaticCharField(env, class2, ncf))
        return JNI_FALSE;

    if (SRES != (*env)->GetStaticShortField(env, class1, sf) ||
        SRES != (*env)->GetStaticShortField(env, class2, nsf))
        return JNI_FALSE;

    if (IRES != (*env)->GetStaticIntField(env, class1, _if) ||
        IRES != (*env)->GetStaticIntField(env, class2, nif))
        return JNI_FALSE;

    if (JRES != (*env)->GetStaticLongField(env, class1, jf) ||
        JRES != (*env)->GetStaticLongField(env, class2, njf))
        return JNI_FALSE;

    if (FRES - (*env)->GetStaticFloatField(env, class1, ff) > 0 ||
        FRES - (*env)->GetStaticFloatField(env, class2, nff) > 0 )
        return JNI_FALSE;

    if (DRES - (*env)->GetStaticDoubleField(env, class1, df) > 0 ||
        DRES - (*env)->GetStaticDoubleField(env, class2, ndf) > 0 )
        return JNI_FALSE;

    return JNI_TRUE;
}
