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
 * Created on 30.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.object_methods;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Gregory Shimansky
 *
 * Test for CallFloatMethod function.
 */
public class CallFloatMethodVTest extends JNITest {
    private native boolean nativeExecute(TestClass tc, NativeTestClass ntc);

    /**
     * Test calls various methods that return float of supplied
     * classes and checks that returned values are correct.
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception {
        TestClass tc = new TestClass();
        NativeTestClass ntc = new NativeTestClass();
        return nativeExecute(tc, ntc);
    }
    public static void main(String[] args){
        System.exit(new CallFloatMethodVTest().test());
    }
}