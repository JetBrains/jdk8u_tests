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
 * @version $Revision: 1.5 $
 */  
/*
 * Created on 09.29.2005
 */
package org.apache.harmony.vts.test.vm.jni.static_methods;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;
import org.apache.harmony.vts.test.vm.jni.static_methods.TestClass;

/**
 * Test for GetStaticMethodID function.
 */
public class GetStaticMethodIDTest01 extends JNITest {
    private native boolean nativeExecute(Class cl, String name, String sig);

    /**
     * Test calls native code that tries to return if it can find
     * a specified method. Test passes if all necessary methods can be found.
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception {
        boolean test4 = nativeExecute(NativeTestClass.class, "method", "(Z)Z"); 
        boolean test5 = nativeExecute(NativeTestClass.class, "method", "([Z[Z)Z"); 
        boolean test6 = nativeExecute(NativeTestClass.class, "method",
                "(Ljava/lang/Boolean;Ljava/lang/Boolean;)Z");

        return test4 && test5 && test6;
    }
    public static void main(String[] args){
        System.exit(new GetStaticMethodIDTest01().test());
    }
}
