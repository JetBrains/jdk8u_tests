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
 * Created on 12.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.exceptions;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Gregory Shimansky
 * 
 * Test for ThrowNew function.
 */
public class ThrowNewTest extends JNITest {
    private native void nativeExecute(String msg) throws ExceptionClass;

    /**
     * Test calls native method that has to throw exception with
     * with constructor initialized with value argument.
     * 
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception {
        String msg = "test exception message";

        try {
            nativeExecute(msg);
        } catch (ExceptionClass e) {
            return e.getMessage().equals(msg);
        }
        return false;
    }
    public static void main(String[] args){
        System.exit(new ThrowNewTest().test());
    }
}