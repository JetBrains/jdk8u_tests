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
 * Created on 15.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.object_fields;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Gregory Shimansky
 *
 * Test for GetDoubleField function.
 */
public class GetDoubleFieldTest extends JNITest {
    private native double nativeExecute(Object obj, String field);

    /**
     * Native code returns value of the object field specified
     * in the argument. Test checks that this value is equal
     * to the field value.
     */
    public boolean execute() throws Exception {
        TestClass tc = new TestClass();
        double arg1 = 20923840.08923982, arg2 = -8982344.121E+114, arg3 = 191.24E-129;
        double res1, res2, res3;

        tc.setDpub(arg1);
        tc.setDprot(arg2);
        tc.setDpriv(arg3);
        res1 = nativeExecute(tc, "dpub");
        res2 = nativeExecute(tc, "dprot");
        res3 = nativeExecute(tc, "dpriv");

        return res1 == arg1 && res2 == arg2 && res3 == arg3;
    }
    public static void main(String[] args){
        System.exit(new GetDoubleFieldTest().test());
    }
}
