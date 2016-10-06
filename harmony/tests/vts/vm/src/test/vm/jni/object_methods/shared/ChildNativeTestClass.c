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

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(ZZZZ)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__ZZZZ
    (JNIEnv *env, jobject this_object, jboolean z1, jboolean z2, jboolean z3, jboolean z4)
{
    jfieldID fid;
    fid = (*env)->GetFieldID(env, (*env)->GetObjectClass(env, this_object),
                             "retz", "Z");
    (*env)->SetBooleanField(env, this_object, fid, JNI_FALSE);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(BBBB)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__BBBB
    (JNIEnv *env, jobject this_object, jbyte b1, jbyte b2, jbyte b3, jbyte b4)
{
    jfieldID fid;
    fid = (*env)->GetFieldID(env, (*env)->GetObjectClass(env, this_object),
                             "retb", "B");
    (*env)->SetByteField(env, this_object, fid, 0);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(CCCC)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__CCCC
    (JNIEnv *env, jobject this_object, jchar c1, jchar c2, jchar c3, jchar c4)
{
    jfieldID fid;
    fid = (*env)->GetFieldID(env, (*env)->GetObjectClass(env, this_object),
                             "retc", "C");
    (*env)->SetCharField(env, this_object, fid, 0);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(SSSS)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__SSSS
    (JNIEnv *env, jobject this_object, jshort s1, jshort s2, jshort s3, jshort s4)
{
    jfieldID fid;
    fid = (*env)->GetFieldID(env, (*env)->GetObjectClass(env, this_object),
                             "rets", "S");
    (*env)->SetShortField(env, this_object, fid, 0);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(IIII)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__IIII
    (JNIEnv *env, jobject this_object, jint i1, jint i2, jint i3, jint i4)
{
    jfieldID fid;
    fid = (*env)->GetFieldID(env, (*env)->GetObjectClass(env, this_object),
                             "reti", "I");
    (*env)->SetIntField(env, this_object, fid, 0);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(JJJJ)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__JJJJ
    (JNIEnv *env, jobject this_object, jlong j1, jlong j2, jlong j3, jlong j4)
{
    jfieldID fid;
    fid = (*env)->GetFieldID(env, (*env)->GetObjectClass(env, this_object),
                             "retj", "J");
    (*env)->SetLongField(env, this_object, fid, 0);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(FFFF)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__FFFF
    (JNIEnv *env, jobject this_object, jfloat f1, jfloat f2, jfloat f3, jfloat f4)
{
    jfieldID fid;
    fid = (*env)->GetFieldID(env, (*env)->GetObjectClass(env, this_object),
                             "retf", "F");
    (*env)->SetFloatField(env, this_object, fid, 0);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(DDDD)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__DDDD
    (JNIEnv *env, jobject this_object, jdouble d1, jdouble d2, jdouble d3, jdouble d4)
{
    jfieldID fid;
    fid = (*env)->GetFieldID(env, (*env)->GetObjectClass(env, this_object),
                             "retd", "D");
    (*env)->SetDoubleField(env, this_object, fid, 0);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Z)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Z
    (JNIEnv *env, jobject this_object, jboolean z1)
{
    return JNI_FALSE;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(ZZ)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__ZZ
    (JNIEnv *env, jobject this_object, jboolean z1, jboolean z2)
{
    return JNI_FALSE;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(B)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__B
    (JNIEnv *env, jobject this_object, jbyte b1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(BB)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__BB
    (JNIEnv *env, jobject this_object, jbyte b1, jbyte b2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(C)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__C
    (JNIEnv *env, jobject this_object, jchar c1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(CC)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__CC
    (JNIEnv *env, jobject this_object, jchar c1, jchar c2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(S)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__S
    (JNIEnv *env, jobject this_object, jshort s1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(SS)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__SS
    (JNIEnv *env, jobject this_object, jshort s1, jshort s2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(I)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__I
    (JNIEnv *env, jobject this_object, jint i1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(II)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__II
    (JNIEnv *env, jobject this_object, jint i1, jint i2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(J)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__J
    (JNIEnv *env, jobject this_object, jlong j1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(JJ)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__JJ
    (JNIEnv *env, jobject this_object, jlong j1, jlong j2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(F)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__F
    (JNIEnv *env, jobject this_object, jfloat f1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(FF)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__FF
    (JNIEnv *env, jobject this_object, jfloat f1, jfloat f2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(D)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__D
    (JNIEnv *env, jobject this_object, jdouble d1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(DD)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__DD
    (JNIEnv *env, jobject this_object, jdouble d1, jdouble d2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([Z)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3Z
    (JNIEnv *env, jobject this_object, jbooleanArray z1)
{
    return JNI_FALSE;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([Z[Z)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3Z_3Z
    (JNIEnv *env, jobject this_object, jbooleanArray z1, jbooleanArray z2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([B)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3B
    (JNIEnv *env, jobject this_object, jbyteArray b1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([B[B)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3B_3B
    (JNIEnv *env, jobject this_object, jbyteArray b1, jbyteArray b2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([C)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3C
    (JNIEnv *env, jobject this_object, jcharArray c1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([C[C)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3C_3C
    (JNIEnv *env, jobject this_object, jcharArray c1, jcharArray c2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([S)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3S
    (JNIEnv *env, jobject this_object, jshortArray s1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([S[S)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3S_3S
    (JNIEnv *env, jobject this_object, jshortArray s1, jshortArray s2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([I)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3I
    (JNIEnv *env, jobject this_object, jintArray i1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([I[I)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3I_3I
    (JNIEnv *env, jobject this_object, jintArray i1, jintArray i2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([J)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3J
    (JNIEnv *env, jobject this_object, jlongArray j1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([J[J)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3J_3J
    (JNIEnv *env, jobject this_object, jlongArray j1, jlongArray j2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([F)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3F
    (JNIEnv *env, jobject this_object, jfloatArray f1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([F[F)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3F_3F
    (JNIEnv *env, jobject this_object, jfloatArray f1, jfloatArray f2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([D)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3D
    (JNIEnv *env, jobject this_object, jdoubleArray d1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method([D[D)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method___3D_3D
    (JNIEnv *env, jobject this_object, jdoubleArray d1, jdoubleArray d2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Boolean;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Boolean_2
    (JNIEnv *env, jobject this_object, jobject z1)
{
    return JNI_FALSE;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Boolean;Ljava/lang/Boolean;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Boolean_2Ljava_lang_Boolean_2
    (JNIEnv *env, jobject this_object, jobject z1, jobject z2)
{
    return JNI_FALSE;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Byte;)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Byte_2
    (JNIEnv *env, jobject this_object, jobject b1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Byte;Ljava/lang/Byte;)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Byte_2Ljava_lang_Byte_2
    (JNIEnv *env, jobject this_object, jobject b1, jobject b2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Character;)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Character_2
    (JNIEnv *env, jobject this_object, jobject c1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Character;Ljava/lang/Character;)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Character_2Ljava_lang_Character_2
    (JNIEnv *env, jobject this_object, jobject c1, jobject c2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Short;)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Short_2
    (JNIEnv *env, jobject this_object, jobject s1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Short;Ljava/lang/Short;)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Short_2Ljava_lang_Short_2
    (JNIEnv *env, jobject this_object, jobject s1, jobject s2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Integer;)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Integer_2
    (JNIEnv *env, jobject this_object, jobject i1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Integer;Ljava/lang/Integer;)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Integer_2Ljava_lang_Integer_2
    (JNIEnv *env, jobject this_object, jobject i1, jobject i2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Long;)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Long_2
    (JNIEnv *env, jobject this_object, jobject j1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Long;Ljava/lang/Long;)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Long_2Ljava_lang_Long_2
    (JNIEnv *env, jobject this_object, jobject j1, jobject j2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Float;)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Float_2
    (JNIEnv *env, jobject this_object, jobject f1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Float;Ljava/lang/Float;)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Float_2Ljava_lang_Float_2
    (JNIEnv *env, jobject this_object, jobject f1, jobject f2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Double;)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Double_2
    (JNIEnv *env, jobject this_object, jobject d1)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(Ljava/lang/Double;Ljava/lang/Double;)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__Ljava_lang_Double_2Ljava_lang_Double_2
    (JNIEnv *env, jobject this_object, jobject d1, jobject d2)
{
    return 0;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(ZZZ)Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__ZZZ
    (JNIEnv *env, jobject this_object, jboolean z1, jboolean z2, jboolean z3)
{
    return NULL;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(BBB)Ljava/lang/Byte;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__BBB
    (JNIEnv *env, jobject this_object, jbyte b1, jbyte b2, jbyte b3)
{
    return NULL;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(CCC)Ljava/lang/Character;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__CCC
    (JNIEnv *env, jobject this_object, jchar c1, jchar c2, jchar c3)
{
    return NULL;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(SSS)Ljava/lang/Short;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__SSS
    (JNIEnv *env, jobject this_object, jshort s1, jshort s2, jshort s3)
{
    return NULL;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(III)Ljava/lang/Integer;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__III
    (JNIEnv *env, jobject this_object, jint i1, jint i2, jint i3)
{
    return NULL;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(JJJ)Ljava/lang/Long;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__JJJ
    (JNIEnv *env, jobject this_object, jlong j1, jlong j2, jlong j3)
{
    return NULL;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(FFF)Ljava/lang/Float;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__FFF
    (JNIEnv *env, jobject this_object, jfloat f1, jfloat f2, jfloat f3)
{
    return NULL;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(DDD)Ljava/lang/Double;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__DDD
    (JNIEnv *env, jobject this_object, jdouble d1, jdouble d2, jdouble d3)
{
    return NULL;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.ChildNativeTestClass.method(BCSIJLjava/lang/Byte;Ljava/lang/Character;Ljava/lang/Short;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_ChildNativeTestClass_method__BCSIJLjava_lang_Byte_2Ljava_lang_Character_2Ljava_lang_Short_2Ljava_lang_Integer_2Ljava_lang_Long_2Ljava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2(JNIEnv *env, jobject this_object, jbyte b, jchar c, jshort s, jint i, jlong j,
    jobject ob, jobject oc, jobject os, jobject oi, jobject oj,
    jstring sb, jstring ss, jstring si, jstring sj)
{
    return 0;
}
