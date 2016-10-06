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
 * @version $Revision: 1.4 $
 */  
/*
 * Created on 11.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.object_operations;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/** 
 * Test that new object is created when calling NewObjectA
 */
public class NewObjectATest extends JNITest {
    /**
     * Return true if object was successfully created, false otherwise/
     * @param cl Class of the object to create
     * @param value Int value to pass to constructor
     * @return true on success, false otherwise
     */
    private native boolean nativeExecute(Class cl, int value);

    /**
     * Native code calls object constructor which sets a static field to a
     * value, test checks that this field has been set.
     * 
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() {
        /* TODO add test for new object of an abstract class and
         * interface, this should result in InstantiationException.
         */
        TestClass.init();
        if (TestClass.getValue() != 0)
            return false;
        if (!nativeExecute(TestClass.class, 10))
            return false;
        return TestClass.getValue() == 10;
    }
    public static void main(String[] args){
        System.exit(new NewObjectATest().test());
    }
}