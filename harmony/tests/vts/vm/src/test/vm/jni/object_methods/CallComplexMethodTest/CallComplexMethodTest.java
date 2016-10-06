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
 * @version $Revision: 1.2 $
 */  
/*
 * Created on 02.12.2004
 */
package org.apache.harmony.vts.test.vm.jni.object_methods;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Gregory Shimansky
 *
 * Test for calling a method with many complex arguments
 */
public class CallComplexMethodTest extends JNITest {
    private native boolean nativeExecute(TestClass tc, NativeTestClass ntc);

    /**
     * Test calls native code which calls native methods and returns
     * result whether it is correct.
     */
    public boolean execute() throws Exception {
        return nativeExecute(new TestClass(), new NativeTestClass());
    }
    public static void main(String[] args){
        System.exit(new CallComplexMethodTest().test());
    }
}