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
 * Created on 15.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.static_fields;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Gregory Shimansky
 *
 * Test for SetStaticIntField function.
 */
public class SetStaticIntFieldTest extends JNITest {
    private native boolean nativeExecute(Class cl, String field, int val); 

    /**
     * Native code tries to set a field of an object
     * to a value. Test checks that field value is correct.
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception {
        boolean res1, res2, res3;
        int arg1 = 191900, arg2 = -732222, arg3 = 17092314;
        int f1, f2, f3;

        res1 = nativeExecute(TestClass.class, "ipub", arg1);
        res2 = nativeExecute(TestClass.class, "iprot", arg2);
        res3 = nativeExecute(TestClass.class, "ipriv", arg3);
        f1 = TestClass.getIpub();
        f2 = TestClass.getIprot();
        f3 = TestClass.getIpriv();

        return res1 && res2 && res3 && f1 == arg1 && f2 == arg2 && f3 == arg3;
    }
    public static void main(String[] args){
        System.exit(new SetStaticIntFieldTest().test());
    }
}