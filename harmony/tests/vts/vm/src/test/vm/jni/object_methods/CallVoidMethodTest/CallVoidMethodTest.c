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
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.CallVoidMethodTest.nativeExecute(Lorg/apache/harmony/vts/test/vm/jni/object_methods/TestClass;Lorg/apache/harmony/vts/test/vm/jni/object_methods/NativeTestClass;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_CallVoidMethodTest_nativeExecute
    (JNIEnv *env, jobject this_object, jobject tc, jobject ntc)
{
    jmethodID zm, bm, cm, sm, im, jm, fm, dm;
    jmethodID nzm, nbm, ncm, nsm, nim, njm, nfm, ndm;
    jfieldID zf, bf, cf, sf, _if, jf, ff, df;
    jfieldID nzf, nbf, ncf, nsf, nif, njf, nff, ndf;
    jclass cl, ncl;
    char *sigz = "(ZZZZ)V";
    char *sigb = "(BBBB)V";
    char *sigc = "(CCCC)V";
    char *sigs = "(SSSS)V";
    char *sigi = "(IIII)V";
    char *sigj = "(JJJJ)V";
    char *sigf = "(FFFF)V";
    char *sigd = "(DDDD)V";

    cl = (*env)->GetObjectClass(env, tc);
    ncl = (*env)->GetObjectClass(env, ntc);

    zm = (*env)->GetMethodID(env, cl, "method", sigz);
    bm = (*env)->GetMethodID(env, cl, "method", sigb);
    cm = (*env)->GetMethodID(env, cl, "method", sigc);
    sm = (*env)->GetMethodID(env, cl, "method", sigs);
    im = (*env)->GetMethodID(env, cl, "method", sigi);
    jm = (*env)->GetMethodID(env, cl, "method", sigj);
    fm = (*env)->GetMethodID(env, cl, "method", sigf);
    dm = (*env)->GetMethodID(env, cl, "method", sigd);

    if (NULL == zm || NULL == bm || NULL == cm || NULL == sm ||
        NULL == im || NULL == jm || NULL == fm || NULL == dm)
        return JNI_FALSE;

    nzm = (*env)->GetMethodID(env, ncl, "method", sigz);
    nbm = (*env)->GetMethodID(env, ncl, "method", sigb);
    ncm = (*env)->GetMethodID(env, ncl, "method", sigc);
    nsm = (*env)->GetMethodID(env, ncl, "method", sigs);
    nim = (*env)->GetMethodID(env, ncl, "method", sigi);
    njm = (*env)->GetMethodID(env, ncl, "method", sigj);
    nfm = (*env)->GetMethodID(env, ncl, "method", sigf);
    ndm = (*env)->GetMethodID(env, ncl, "method", sigd);

    if (NULL == nzm || NULL == nbm || NULL == ncm || NULL == nsm ||
        NULL == nim || NULL == njm || NULL == nfm || NULL == ndm)
        return JNI_FALSE;

    zf = (*env)->GetFieldID(env, cl, "retz", "Z");
    bf = (*env)->GetFieldID(env, cl, "retb", "B");
    cf = (*env)->GetFieldID(env, cl, "retc", "C");
    sf = (*env)->GetFieldID(env, cl, "rets", "S");
    _if = (*env)->GetFieldID(env, cl, "reti", "I");
    jf = (*env)->GetFieldID(env, cl, "retj", "J");
    ff = (*env)->GetFieldID(env, cl, "retf", "F");
    df = (*env)->GetFieldID(env, cl, "retd", "D");

    if (NULL == zf || NULL == bf || NULL == cf || NULL == sf ||
        NULL == _if || NULL == jf || NULL == ff || NULL == df)
        return JNI_FALSE;

    nzf = (*env)->GetFieldID(env, ncl, "retz", "Z");
    nbf = (*env)->GetFieldID(env, ncl, "retb", "B");
    ncf = (*env)->GetFieldID(env, ncl, "retc", "C");
    nsf = (*env)->GetFieldID(env, ncl, "rets", "S");
    nif = (*env)->GetFieldID(env, ncl, "reti", "I");
    njf = (*env)->GetFieldID(env, ncl, "retj", "J");
    nff = (*env)->GetFieldID(env, ncl, "retf", "F");
    ndf = (*env)->GetFieldID(env, ncl, "retd", "D");

    if (NULL == nzf || NULL == nbf || NULL == ncf || NULL == nsf ||
        NULL == nif || NULL == njf || NULL == nff || NULL == ndf)
        return JNI_FALSE;

    (*env)->CallVoidMethod(env, tc, zm, ZARG1, ZARG2, ZARG3, ZARG4);
    (*env)->CallVoidMethod(env, tc, bm, BARG1, BARG2, BARG3, BARG4);
    (*env)->CallVoidMethod(env, tc, cm, CARG1, CARG2, CARG3, CARG4);
    (*env)->CallVoidMethod(env, tc, sm, SARG1, SARG2, SARG3, SARG4);
    (*env)->CallVoidMethod(env, tc, im, IARG1, IARG2, IARG3, IARG4);
    (*env)->CallVoidMethod(env, tc, jm, JARG1, JARG2, JARG3, JARG4);
    (*env)->CallVoidMethod(env, tc, fm, FARG1, FARG2, FARG3, FARG4);
    (*env)->CallVoidMethod(env, tc, dm, DARG1, DARG2, DARG3, DARG4);

    (*env)->CallVoidMethod(env, ntc, nzm, ZARG1, ZARG2, ZARG3, ZARG4);
    (*env)->CallVoidMethod(env, ntc, nbm, BARG1, BARG2, BARG3, BARG4);
    (*env)->CallVoidMethod(env, ntc, ncm, CARG1, CARG2, CARG3, CARG4);
    (*env)->CallVoidMethod(env, ntc, nsm, SARG1, SARG2, SARG3, SARG4);
    (*env)->CallVoidMethod(env, ntc, nim, IARG1, IARG2, IARG3, IARG4);
    (*env)->CallVoidMethod(env, ntc, njm, JARG1, JARG2, JARG3, JARG4);
    (*env)->CallVoidMethod(env, ntc, nfm, FARG1, FARG2, FARG3, FARG4);
    (*env)->CallVoidMethod(env, ntc, ndm, DARG1, DARG2, DARG3, DARG4);

    if (ZRES != (*env)->GetBooleanField(env, tc, zf) ||
        ZRES != (*env)->GetBooleanField(env, ntc, nzf))
        return JNI_FALSE;

    if (BRES != (*env)->GetByteField(env, tc, bf) ||
        BRES != (*env)->GetByteField(env, ntc, nbf))
        return JNI_FALSE;

    if (CRES != (*env)->GetCharField(env, tc, cf) ||
        CRES != (*env)->GetCharField(env, ntc, ncf))
        return JNI_FALSE;

    if (SRES != (*env)->GetShortField(env, tc, sf) ||
        SRES != (*env)->GetShortField(env, ntc, nsf))
        return JNI_FALSE;

    if (IRES != (*env)->GetIntField(env, tc, _if) ||
        IRES != (*env)->GetIntField(env, ntc, nif))
        return JNI_FALSE;

    if (JRES != (*env)->GetLongField(env, tc, jf) ||
        JRES != (*env)->GetLongField(env, ntc, njf))
        return JNI_FALSE;

    if (FRES - (*env)->GetFloatField(env, tc, ff) > 0 ||
        FRES - (*env)->GetFloatField(env, ntc, nff) > 0 )
        return JNI_FALSE;

    if (DRES - (*env)->GetDoubleField(env, tc, df) > 0 ||
        DRES - (*env)->GetDoubleField(env, ntc, ndf) > 0 )
        return JNI_FALSE;

    return JNI_TRUE;
}
