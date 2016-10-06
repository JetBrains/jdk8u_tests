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
 * @version $Revision: 1.5 $
 */  
/*
 * Created on 05.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.class_operations;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * Tests that returned superclass of a class is correct.
 */
public class GetSuperclassTest extends JNITest {
    private native Class nativeExecute(Class cl);

    /**
     * Test passes if GetSuperclass for object representing 
     * this class returns class handle that is equal to what Class.forName returns.
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     * @return test result true for passed, false for failed.
     */
    public boolean execute() {
        Class cl = nativeExecute(getClass());
        Class parent = org.apache.harmony.vts.test.vm.jni.share.JNITest.class;
        return cl == parent;
    }
    public static void main(String[] args){
        System.exit(new GetSuperclassTest().test());
    }
}
