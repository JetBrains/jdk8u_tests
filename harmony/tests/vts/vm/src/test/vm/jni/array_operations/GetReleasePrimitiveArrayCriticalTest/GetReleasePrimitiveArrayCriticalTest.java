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
/** 
 * @author Petr Ivanov
 * @version $Revision: 1.3 $
 */  
/*
 * Created on 9.02.2005
 */
package org.apache.harmony.vts.test.vm.jni.array_operations;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Petr Ivanov
 *
 * Test for GetReleasePrimitiveArrayCritical function.
 */
public class GetReleasePrimitiveArrayCriticalTest extends JNITest {
    private native void nativeExecute(char[] arrayC, byte[] arrayBy,  short[] arrayS, int[] arrayI, long[] arrayL, 
            float[] arrayF, double[] arrayD, boolean[] arrayB, int length);

    /**
     * Test creates the array of primitive type, provides it
     * to native, then native gets its elements, modifies them,
     * releases the array and exits. Method then checks if 
     * the modifications are as expected.
     */
    public boolean execute() throws Exception {
        int length = 15;
        int massI[] = new int[length];
        char massC[] = new char[length];
        byte massBy[] = new byte[length];
        long massL[] = new long[length];
        float massF[] = new float[length];
        short massS[] = new short[length];
        boolean massB[] = new boolean[length];
        double massD[] = new double[length];
        for (int i = 0; i < length; i++){ 
            massI[i] = i;
            massC[i] = (char)i;
            massBy[i] = (byte)i;
            massL[i] = (long)i;
            massF[i] = (float)i;
            massS[i] = (short)i;
            massB[i] = (i < 10);
            massD[i] = (double)i;
        }
        
        nativeExecute(massC, massBy, massS, massI, massL, massF, massD, massB, length);

        if (massI[3] != massI[12]) return false;
        if (massC[3] != massC[12]) return false;
        if (massBy[3] != massBy[12]) return false;
        if (massL[3] != massL[12]) return false;
        if (massF[3] != massF[12]) return false;
        if (massS[3] != massS[12]) return false;
        if (massB[3] != massB[12]) return false;
        if (massD[3] != massD[12]) return false;

        return true;
    }
    public static void main(String[] args){
        System.exit(new GetReleasePrimitiveArrayCriticalTest().test());
    }
}

