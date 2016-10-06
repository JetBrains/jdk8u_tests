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
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(ZZZZ)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__ZZZZ
    (JNIEnv *env, jobject this_object, jboolean z1, jboolean z2, jboolean z3, jboolean z4)
{
    jboolean result = z1 | z2 | z3 | z4;
    jclass cl = (*env)->GetObjectClass(env, this_object);
    jfieldID fid = (*env)->GetFieldID(env, cl, "retz", "Z");
    (*env)->SetBooleanField(env, this_object, fid, result);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(BBBB)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__BBBB
    (JNIEnv *env, jobject this_object, jbyte b1, jbyte b2, jbyte b3, jbyte b4)
{
    jbyte result = b1 + b2 + b3 + b4;
    jclass cl = (*env)->GetObjectClass(env, this_object);
    jfieldID fid = (*env)->GetFieldID(env, cl, "retb", "B");
    (*env)->SetByteField(env, this_object, fid, result);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(CCCC)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__CCCC
    (JNIEnv *env, jobject this_object, jchar c1, jchar c2, jchar c3, jchar c4)
{
    jchar result = c1 + c2 + c3 + c4;
    jclass cl = (*env)->GetObjectClass(env, this_object);
    jfieldID fid = (*env)->GetFieldID(env, cl, "retc", "C");
    (*env)->SetCharField(env, this_object, fid, result);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(SSSS)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__SSSS
    (JNIEnv *env, jobject this_object, jshort s1, jshort s2, jshort s3, jshort s4)
{
    jshort result = s1 + s2 + s3 + s4;
    jclass cl = (*env)->GetObjectClass(env, this_object);
    jfieldID fid = (*env)->GetFieldID(env, cl, "rets", "S");
    (*env)->SetShortField(env, this_object, fid, result);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(IIII)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__IIII
    (JNIEnv *env, jobject this_object, jint i1, jint i2, jint i3, jint i4)
{
    jint result = i1 + i2 + i3 + i4;
    jclass cl = (*env)->GetObjectClass(env, this_object);
    jfieldID fid = (*env)->GetFieldID(env, cl, "reti", "I");
    (*env)->SetIntField(env, this_object, fid, result);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(JJJJ)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__JJJJ
    (JNIEnv *env, jobject this_object, jlong j1, jlong j2, jlong j3, jlong j4)
{
    jlong result = j1 + j2 + j3 + j4;
    jclass cl = (*env)->GetObjectClass(env, this_object);
    jfieldID fid = (*env)->GetFieldID(env, cl, "retj", "J");
    (*env)->SetLongField(env, this_object, fid, result);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(FFFF)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__FFFF
    (JNIEnv *env, jobject this_object, jfloat f1, jfloat f2, jfloat f3, jfloat f4)
{
    jfloat result = f1 + f2 + f3 + f4;
    jclass cl = (*env)->GetObjectClass(env, this_object);
    jfieldID fid = (*env)->GetFieldID(env, cl, "retf", "F");
    (*env)->SetFloatField(env, this_object, fid, result);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(DDDD)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__DDDD
    (JNIEnv *env, jobject this_object, jdouble d1, jdouble d2, jdouble d3, jdouble d4)
{
    jdouble result = d1 + d2 + d3 + d4;
    jclass cl = (*env)->GetObjectClass(env, this_object);
    jfieldID fid = (*env)->GetFieldID(env, cl, "retd", "D");
    (*env)->SetDoubleField(env, this_object, fid, result);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Z)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Z
    (JNIEnv *env, jobject this_object, jboolean z1)
{
    return z1;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(ZZ)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__ZZ
    (JNIEnv *env, jobject this_object, jboolean z1, jboolean z2)
{
    return z1 || z2;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(B)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__B
    (JNIEnv *env, jobject this_object, jbyte b1)
{
    return b1;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(BB)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__BB
    (JNIEnv *env, jobject this_object, jbyte b1, jbyte b2)
{
    return b1 + b2;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(C)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__C
    (JNIEnv *env, jobject this_object, jchar c1)
{
    return c1;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(CC)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__CC
    (JNIEnv *env, jobject this_object, jchar c1, jchar c2)
{
    return c1 + c2;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(S)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__S
    (JNIEnv *env, jobject this_object, jshort s1)
{
    return s1;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(SS)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__SS
    (JNIEnv *env, jobject this_object, jshort s1, jshort s2)
{
    return s1 + s2;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(I)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__I
    (JNIEnv *env, jobject this_object, jint i1)
{
    return i1;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(II)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__II
    (JNIEnv *env, jobject this_object, jint i1, jint i2)
{
    return i1 + i2;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(J)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__J
    (JNIEnv *env, jobject this_object, jlong j1)
{
    return j1;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(JJ)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__JJ
    (JNIEnv *env, jobject this_object, jlong j1, jlong j2)
{
    return j1 + j2;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(F)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__F
    (JNIEnv *env, jobject this_object, jfloat f1)
{
    return f1;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(FF)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__FF
    (JNIEnv *env, jobject this_object, jfloat f1, jfloat f2)
{
    return f1 + f2;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(D)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__D
    (JNIEnv *env, jobject this_object, jdouble d1)
{
    return d1;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(DD)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__DD
    (JNIEnv *env, jobject this_object, jdouble d1, jdouble d2)
{
    return d1 + d2;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([Z)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3Z
    (JNIEnv *env, jobject this_object, jbooleanArray z1)
{
    jint iii;
    jint length;
    jboolean result = JNI_FALSE;
    jboolean *array;

    array = (*env)->GetBooleanArrayElements(env, z1, NULL);
    if (NULL == array)
        return JNI_FALSE;
    length = (*env)->GetArrayLength(env, z1);

    for (iii = 0; iii < length; iii++)
        result = result || array[iii];

    (*env)->ReleaseBooleanArrayElements(env, z1, array, 0);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([Z[Z)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3Z_3Z
    (JNIEnv *env, jobject this_object, jbooleanArray z1, jbooleanArray z2)
{
    jint iii;
    jint length;
    jboolean result = JNI_FALSE;
    jboolean *array;

    array = (*env)->GetBooleanArrayElements(env, z1, NULL);
    if (NULL == array)
        return JNI_FALSE;
    length = (*env)->GetArrayLength(env, z1);

    for (iii = 0; iii < length; iii++)
        result = result || array[iii];

    (*env)->ReleaseBooleanArrayElements(env, z1, array, 0);

    array = (*env)->GetBooleanArrayElements(env, z2, NULL);
    if (NULL == array)
        return JNI_FALSE;
    length = (*env)->GetArrayLength(env, z2);

    for (iii = 0; iii < length; iii++)
        result = result || array[iii];

    (*env)->ReleaseBooleanArrayElements(env, z2, array, 0);
    return result;
}

#define ARRAY_METHOD_BEGIN(native_type) \
    jint iii;                           \
    jint length;                        \
    native_type result = 0;             \
    native_type *array;                 \

#define ARRAY_METHOD_CODE(native_type, variable)                          \
    array = (*env)->Get##native_type##ArrayElements(env, variable, NULL); \
    if (NULL == array)                                                    \
        return 0;                                                         \
    length = (*env)->GetArrayLength(env, variable);                       \
                                                                          \
    for (iii = 0; iii < length; iii++)                                    \
        result = result + array[iii];                                     \
                                                                          \
    (*env)->Release##native_type##ArrayElements(env, variable, array, 0); \

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([B)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3B
    (JNIEnv *env, jobject this_object, jbyteArray b1)
{
    ARRAY_METHOD_BEGIN(jbyte);
    ARRAY_METHOD_CODE(Byte, b1);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([B[B)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3B_3B
    (JNIEnv *env, jobject this_object, jbyteArray b1, jbyteArray b2)
{
    ARRAY_METHOD_BEGIN(jbyte);
    ARRAY_METHOD_CODE(Byte, b1);
    ARRAY_METHOD_CODE(Byte, b2);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([C)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3C
    (JNIEnv *env, jobject this_object, jcharArray c1)
{
    ARRAY_METHOD_BEGIN(jchar);
    ARRAY_METHOD_CODE(Char, c1);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([C[C)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3C_3C
    (JNIEnv *env, jobject this_object, jcharArray c1, jcharArray c2)
{
    ARRAY_METHOD_BEGIN(jchar);
    ARRAY_METHOD_CODE(Char, c1);
    ARRAY_METHOD_CODE(Char, c2);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([S)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3S
    (JNIEnv *env, jobject this_object, jshortArray s1)
{
    ARRAY_METHOD_BEGIN(jshort);
    ARRAY_METHOD_CODE(Short, s1);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([S[S)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3S_3S
    (JNIEnv *env, jobject this_object, jshortArray s1, jshortArray s2)
{
    ARRAY_METHOD_BEGIN(jshort);
    ARRAY_METHOD_CODE(Short, s1);
    ARRAY_METHOD_CODE(Short, s2);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([I)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3I
    (JNIEnv *env, jobject this_object, jintArray i1)
{
    ARRAY_METHOD_BEGIN(jint);
    ARRAY_METHOD_CODE(Int, i1);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([I[I)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3I_3I
    (JNIEnv *env, jobject this_object, jintArray i1, jintArray i2)
{
    ARRAY_METHOD_BEGIN(jint);
    ARRAY_METHOD_CODE(Int, i1);
    ARRAY_METHOD_CODE(Int, i2);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([J)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3J
    (JNIEnv *env, jobject this_object, jlongArray j1)
{
    ARRAY_METHOD_BEGIN(jlong);
    ARRAY_METHOD_CODE(Long, j1);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([J[J)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3J_3J
    (JNIEnv *env, jobject this_object, jlongArray j1, jlongArray j2)
{
    ARRAY_METHOD_BEGIN(jlong);
    ARRAY_METHOD_CODE(Long, j1);
    ARRAY_METHOD_CODE(Long, j2);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([F)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3F
    (JNIEnv *env, jobject this_object, jfloatArray f1)
{
    ARRAY_METHOD_BEGIN(jfloat);
    ARRAY_METHOD_CODE(Float, f1);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([F[F)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3F_3F
    (JNIEnv *env, jobject this_object, jfloatArray f1, jfloatArray f2)
{
    ARRAY_METHOD_BEGIN(jfloat);
    ARRAY_METHOD_CODE(Float, f1);
    ARRAY_METHOD_CODE(Float, f2);
    return result;
 }

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([D)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3D
    (JNIEnv *env, jobject this_object, jdoubleArray d1)
{
    ARRAY_METHOD_BEGIN(jdouble);
    ARRAY_METHOD_CODE(Double, d1);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method([D[D)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method___3D_3D
    (JNIEnv *env, jobject this_object, jdoubleArray d1, jdoubleArray d2)
{
    ARRAY_METHOD_BEGIN(jdouble);
    ARRAY_METHOD_CODE(Double, d1);
    ARRAY_METHOD_CODE(Double, d2);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Boolean;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Boolean_2
    (JNIEnv *env, jobject this_object, jobject z1)
{
    jmethodID mID;
    jboolean result = JNI_FALSE;

    mID = (*env)->GetMethodID(env, (*env)->GetObjectClass(env, z1),
        "booleanValue", "()Z");
    if (NULL == mID)
        return JNI_FALSE;

    result = (*env)->CallBooleanMethod(env, z1, mID);
    return result;
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Boolean;Ljava/lang/Boolean;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Boolean_2Ljava_lang_Boolean_2
    (JNIEnv *env, jobject this_object, jobject z1, jobject z2)
{
    jmethodID mID;
    jboolean result1 = JNI_FALSE, result2 = JNI_FALSE;

    mID = (*env)->GetMethodID(env, (*env)->GetObjectClass(env, z1),
        "booleanValue", "()Z");
    if (NULL == mID)
        return JNI_FALSE;

    result1 = (*env)->CallBooleanMethod(env, z1, mID);
    result2 = (*env)->CallBooleanMethod(env, z2, mID);
    return result1 || result2;
}

#define OBJECT_METHOD1(type, obj, name, sig, native_type)            \
    jmethodID mID;                                                   \
    type result = 0;                                                 \
                                                                     \
    mID = (*env)->GetMethodID(env, (*env)->GetObjectClass(env, obj), \
        name, sig);                                                  \
    if (NULL == mID)                                                 \
        return 0;                                                    \
                                                                     \
    result = (*env)->Call##native_type##Method(env, obj, mID);       \
    return result;                                                   \

#define OBJECT_METHOD2(type, obj1, obj2, name, sig, native_type)      \
    jmethodID mID;                                                    \
    type result1 = 0, result2 = 0;                                    \
                                                                      \
    mID = (*env)->GetMethodID(env, (*env)->GetObjectClass(env, obj1), \
        name, sig);                                                   \
    if (NULL == mID)                                                  \
        return 0;                                                     \
                                                                      \
    result1 = (*env)->Call##native_type##Method(env, obj1, mID);      \
    result2 = (*env)->Call##native_type##Method(env, obj2, mID);      \
    return result1 + result2;                                         \

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Byte;)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Byte_2
    (JNIEnv *env, jobject this_object, jobject b1)
{
    OBJECT_METHOD1(jbyte, b1, "byteValue", "()B", Byte);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Byte;Ljava/lang/Byte;)B
 */
JNIEXPORT jbyte JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Byte_2Ljava_lang_Byte_2
    (JNIEnv *env, jobject this_object, jobject b1, jobject b2)
{
    OBJECT_METHOD2(jbyte, b1, b2, "byteValue", "()B", Byte);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Character;)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Character_2
    (JNIEnv *env, jobject this_object, jobject c1)
{
    OBJECT_METHOD1(jchar, c1, "charValue", "()C", Char);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Character;Ljava/lang/Character;)C
 */
JNIEXPORT jchar JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Character_2Ljava_lang_Character_2
    (JNIEnv *env, jobject this_object, jobject c1, jobject c2)
{
    OBJECT_METHOD2(jchar, c1, c2, "charValue", "()C", Char);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Short;)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Short_2
    (JNIEnv *env, jobject this_object, jobject s1)
{
    OBJECT_METHOD1(jshort, s1, "shortValue", "()S", Short);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Short;Ljava/lang/Short;)S
 */
JNIEXPORT jshort JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Short_2Ljava_lang_Short_2
    (JNIEnv *env, jobject this_object, jobject s1, jobject s2)
{
    OBJECT_METHOD2(jshort, s1, s2, "shortValue", "()S", Short);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Integer;)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Integer_2
    (JNIEnv *env, jobject this_object, jobject i1)
{
    OBJECT_METHOD1(jint, i1, "intValue", "()I", Int);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Integer;Ljava/lang/Integer;)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Integer_2Ljava_lang_Integer_2
    (JNIEnv *env, jobject this_object, jobject i1, jobject i2)
{
    OBJECT_METHOD2(jint, i1, i2, "intValue", "()I", Int);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Long;)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Long_2
    (JNIEnv *env, jobject this_object, jobject j1)
{
    OBJECT_METHOD1(jlong, j1, "longValue", "()J", Long);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Long;Ljava/lang/Long;)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Long_2Ljava_lang_Long_2
    (JNIEnv *env, jobject this_object, jobject j1, jobject j2)
{
    OBJECT_METHOD2(jlong, j1, j2, "longValue", "()J", Long);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Float;)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Float_2
    (JNIEnv *env, jobject this_object, jobject f1)
{
    OBJECT_METHOD1(jfloat, f1, "floatValue", "()F", Float);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Float;Ljava/lang/Float;)F
 */
JNIEXPORT jfloat JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Float_2Ljava_lang_Float_2
    (JNIEnv *env, jobject this_object, jobject f1, jobject f2)
{
    OBJECT_METHOD2(jfloat, f1, f2, "floatValue", "()F", Float);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Double;)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Double_2
    (JNIEnv *env, jobject this_object, jobject d1)
{
    OBJECT_METHOD1(jdouble, d1, "doubleValue", "()D", Double);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(Ljava/lang/Double;Ljava/lang/Double;)D
 */
JNIEXPORT jdouble JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__Ljava_lang_Double_2Ljava_lang_Double_2
    (JNIEnv *env, jobject this_object, jobject d1, jobject d2)
{
    OBJECT_METHOD2(jdouble, d1, d2, "doubleValue", "()D", Double);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(ZZZ)Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__ZZZ
    (JNIEnv *env, jobject this_object, jboolean z1, jboolean z2, jboolean z3)
{
    jclass cl;
    jmethodID mID;
    jobject result;

    cl = (*env)->FindClass(env, "java/lang/Boolean");
    if (NULL == cl)
        return NULL;

    mID = (*env)->GetMethodID(env, cl, "<init>", "(Z)V");
    if (NULL == mID)
        return NULL;

    return (*env)->NewObject(env, cl, mID, z1 || z2 || z3);
}

#define OBJECT_METHOD3(clazz, sig, arg1, arg2, arg3)            \
    jclass cl;                                                  \
    jmethodID mID;                                              \
    jobject result;                                             \
                                                                \
    cl = (*env)->FindClass(env, clazz);                         \
    if (NULL == cl)                                             \
        return NULL;                                            \
                                                                \
    mID = (*env)->GetMethodID(env, cl, "<init>", sig);          \
    if (NULL == mID)                                            \
        return NULL;                                            \
                                                                \
    return (*env)->NewObject(env, cl, mID, arg1 + arg2 + arg3); \

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(BBB)Ljava/lang/Byte;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__BBB
    (JNIEnv *env, jobject this_object, jbyte b1, jbyte b2, jbyte b3)
{
    OBJECT_METHOD3("java/lang/Byte", "(B)V", b1, b2, b3);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(CCC)Ljava/lang/Character;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__CCC
    (JNIEnv *env, jobject this_object, jchar c1, jchar c2, jchar c3)
{
    OBJECT_METHOD3("java/lang/Character", "(C)V", c1, c2, c3);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(SSS)Ljava/lang/Short;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__SSS
    (JNIEnv *env, jobject this_object, jshort s1, jshort s2, jshort s3)
{
    OBJECT_METHOD3("java/lang/Short", "(S)V", s1, s2, s3);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(III)Ljava/lang/Integer;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__III
    (JNIEnv *env, jobject this_object, jint i1, jint i2, jint i3)
{
    OBJECT_METHOD3("java/lang/Integer", "(I)V", i1, i2, i3);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(JJJ)Ljava/lang/Long;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__JJJ
    (JNIEnv *env, jobject this_object, jlong j1, jlong j2, jlong j3)
{
    OBJECT_METHOD3("java/lang/Long", "(J)V", j1, j2, j3);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(FFF)Ljava/lang/Float;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__FFF
    (JNIEnv *env, jobject this_object, jfloat f1, jfloat f2, jfloat f3)
{
    OBJECT_METHOD3("java/lang/Float", "(F)V", f1, f2, f3);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(DDD)Ljava/lang/Double;
 */
JNIEXPORT jobject JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__DDD
    (JNIEnv *env, jobject this_object, jdouble d1, jdouble d2, jdouble d3)
{
    OBJECT_METHOD3("java/lang/Double", "(D)V", d1, d2, d3);
}

/*
 * Method: org.apache.harmony.vts.test.vm.jni.object_methods.NativeTestClass.method(BCSIJLjava/lang/Byte;Ljava/lang/Character;Ljava/lang/Short;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL
Java_org_apache_harmony_vts_test_vm_jni_object_1methods_NativeTestClass_method__BCSIJLjava_lang_Byte_2Ljava_lang_Character_2Ljava_lang_Short_2Ljava_lang_Integer_2Ljava_lang_Long_2Ljava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2(JNIEnv *env, jobject this_object, jbyte b, jchar c, jshort s, jint i, jlong j,
    jobject ob, jobject oc, jobject os, jobject oi, jobject oj,
    jstring sb, jstring ss, jstring si, jstring sj)
{
    jlong d1, d2, d3;
    jclass bc, cc, sc, ic, jc;
    jmethodID bm, cm, sm, im, jm;
    jmethodID binit, sinit, iinit, jinit;
    jobject bobj, sobj, iobj, jobj;

    d1 = b + c + s + i + j;

    bc = (*env)->GetObjectClass(env, ob);
    cc = (*env)->GetObjectClass(env, oc);
    sc = (*env)->GetObjectClass(env, os);
    ic = (*env)->GetObjectClass(env, oi);
    jc = (*env)->GetObjectClass(env, oj);

    bm = (*env)->GetMethodID(env, bc, "byteValue", "()B");
    cm = (*env)->GetMethodID(env, cc, "charValue", "()C");
    sm = (*env)->GetMethodID(env, sc, "shortValue", "()S");
    im = (*env)->GetMethodID(env, ic, "intValue", "()I");
    jm = (*env)->GetMethodID(env, jc, "longValue", "()J");

    d2 = (*env)->CallByteMethod(env, ob, bm) +
        (*env)->CallCharMethod(env, oc, cm) +
        (*env)->CallShortMethod(env, os, sm) +
        (*env)->CallIntMethod(env, oi, im) +
        (*env)->CallLongMethod(env, oj, jm);

    binit = (*env)->GetMethodID(env, bc, "<init>", "(Ljava/lang/String;)V");
    sinit = (*env)->GetMethodID(env, sc, "<init>", "(Ljava/lang/String;)V");
    iinit = (*env)->GetMethodID(env, ic, "<init>", "(Ljava/lang/String;)V");
    jinit = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");

    bobj = (*env)->NewObject(env, bc, binit, sb);
    sobj = (*env)->NewObject(env, sc, sinit, ss);
    iobj = (*env)->NewObject(env, ic, iinit, si);
    jobj = (*env)->NewObject(env, jc, jinit, sj);

    d3 = (*env)->CallByteMethod(env, bobj, bm) +
        (*env)->CallShortMethod(env, sobj, sm) +
        (*env)->CallIntMethod(env, iobj, im) +
        (*env)->CallLongMethod(env, jobj, jm);

    return d1 + d2 + d3;
}
