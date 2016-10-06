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
 * @version $Revision: 1.4 $
 */  
/*
 * Created on 9.02.2005
 */
package org.apache.harmony.vts.test.vm.jni.array_operations;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * Test for SetObjectArrayElement function.
 */
public class SetObjectArrayElementTest extends JNITest {
    private native NewObjectArrayTestClass[] nativeExecute(NewObjectArrayTestClass[] element);

    /**
     * Method invokes native method with the array parameter of
     * known class, native sets an element of this array and
     * returns the array. Method then checks that the modified element
     * is an expected one.
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception {
        NewObjectArrayTestClass[] array = new NewObjectArrayTestClass[5];
        for(int i = 0; i < 5; i++){array[i] = new NewObjectArrayTestClass(i * i);}
        NewObjectArrayTestClass[] result = nativeExecute(array);
        if(array[2].geta() != array[4].geta()) return false;
        if(array[3].geta() == array[4].geta()) return false;
        return true;
    }
    public static void main(String[] args){
        System.exit(new SetObjectArrayElementTest().test());
    }
}

