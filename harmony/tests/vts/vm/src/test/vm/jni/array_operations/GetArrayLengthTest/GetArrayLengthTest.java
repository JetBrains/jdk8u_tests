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
 * @author Gregory Shimansky, Petr Ivanov
 * @version $Revision: 1.3 $
 */  
/*
 * Created on 08.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.array_operations;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Gregory Shimansky
 *
 * Test that returned array length is correct
 */
public class GetArrayLengthTest extends JNITest {
    private native int nativeExecute(Object array);

    /**
     * Check that array length returned by the native method is correct
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() {
        int []inta = new int[10];
	char []chara = new char[12];
	long []longa = new long[7];
	short []shorta = new short[14];
	float []floata = new float[9];
	double []doublea = new double[4];
	boolean []booleana = new boolean[100];
	byte []bytea = new byte[15];
        Object []obja = new Object[15];
        double [][]double2a = new double[6][];
        return nativeExecute(inta) == inta.length &&
            nativeExecute(chara) == chara.length &&
            nativeExecute(longa) == longa.length &&
            nativeExecute(shorta) == shorta.length &&
            nativeExecute(floata) == floata.length &&
            nativeExecute(doublea) == doublea.length &&
            nativeExecute(booleana) == booleana.length &&
            nativeExecute(bytea) == bytea.length &&
            nativeExecute(obja) == obja.length &&
            nativeExecute(double2a) == double2a.length;
    }
    public static void main(String[] args){
        System.exit(new GetArrayLengthTest().test());
    }
}