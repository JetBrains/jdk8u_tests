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
 * Method: org.apache.harmony.vts.test.vm.jni.array_operations.GetReleasePrimitiveArrayCriticalTest.nativeExecute([C[B[S[I[J[F[D[ZI)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_vts_test_vm_jni_array_1operations_GetReleasePrimitiveArrayCriticalTest_nativeExecute(JNIEnv *env, jobject this_object, jcharArray ca, jbyteArray bya, jshortArray sa, jintArray ia, 
	jlongArray la, jfloatArray fa, jdoubleArray da, jbooleanArray ba, jint length){
		jchar* arrayC;
		jbyte* arrayBy;
		jshort* arrayS;
		jint* arrayI;
		jlong* arrayL;
		jfloat* arrayF;
		jdouble* arrayD;
		jboolean* arrayB;

		arrayC = (*env)->GetPrimitiveArrayCritical(env, ca, NULL);
		arrayBy = (*env)->GetPrimitiveArrayCritical(env, bya, NULL);
		arrayS = (*env)->GetPrimitiveArrayCritical(env, sa, NULL);
		arrayI = (*env)->GetPrimitiveArrayCritical(env, ia, NULL);
		arrayL = (*env)->GetPrimitiveArrayCritical(env, la, NULL);
		arrayF = (*env)->GetPrimitiveArrayCritical(env, fa, NULL);
		arrayD = (*env)->GetPrimitiveArrayCritical(env, da, NULL);
		arrayB = (*env)->GetPrimitiveArrayCritical(env, ba, NULL);

		arrayC[3] = arrayC[12];
		arrayBy[3] = arrayBy[12];
		arrayS[3] = arrayS[12];
		arrayI[3] = arrayI[12];
		arrayL[3] = arrayL[12];
		arrayF[3] = arrayF[12];
		arrayD[3] = arrayD[12];
		arrayB[3] = arrayB[12];

		(*env)->ReleasePrimitiveArrayCritical(env, ca, arrayC, 0); 
		(*env)->ReleasePrimitiveArrayCritical(env, bya, arrayBy, 0); 
		(*env)->ReleasePrimitiveArrayCritical(env, sa, arrayS, 0); 
		(*env)->ReleasePrimitiveArrayCritical(env, ia, arrayI, 0); 
		(*env)->ReleasePrimitiveArrayCritical(env, la, arrayL, 0); 
		(*env)->ReleasePrimitiveArrayCritical(env, fa, arrayF, 0); 
		(*env)->ReleasePrimitiveArrayCritical(env, da, arrayD, 0); 
		(*env)->ReleasePrimitiveArrayCritical(env, ba, arrayB, 0); 
}
