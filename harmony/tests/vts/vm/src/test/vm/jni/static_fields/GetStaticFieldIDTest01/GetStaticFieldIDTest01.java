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
 * Created on 09.29.2005
 */
package org.apache.harmony.vts.test.vm.jni.static_fields;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Petr Ivanov
 *
 * Test for GetFieldID function.
 */
public class GetStaticFieldIDTest01 extends JNITest {
    private int a;
    private native boolean nativeExecute(Class cl, String name, String sig);

    /**
     * Native code calls GetFieldID. The function should return valid
     * value.
     * 
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception {
        boolean test2 = nativeExecute(TestClass.class, "ipub", "V"); 
        
        return !test2;
    }
    public static void main(String[] args){
        System.exit(new GetStaticFieldIDTest01().test());
    }
}
