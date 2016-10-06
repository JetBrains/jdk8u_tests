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
 * Test for Get/ReleaseByteArrayElements function.
 */
public class GetReleaseByteArrayElementsTest extends JNITest {
    private native void nativeExecute(byte [] array, int length);

    /**
     * Test creates the array of primitive type, provides it
     * to native, then native gets its elements, modifies them,
     * releases the array and exits. Method then checks if 
     * the modifications are as expected.
     */
    public boolean execute() throws Exception {
        int length = 15;
        byte mass[] = new byte[length];
        for (int i = 0; i < length; i++){ mass[i] = (byte)i; }
        nativeExecute(mass, length);

        if (mass[3] != mass[12]) return false;

        return true;
    }
    public static void main(String[] args){
        System.exit(new GetReleaseByteArrayElementsTest().test());
    }
}