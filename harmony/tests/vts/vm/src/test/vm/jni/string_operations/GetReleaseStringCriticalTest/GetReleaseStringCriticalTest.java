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
 * Created on 24.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.string_operations;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Gregory Shimansky
 *
 * Test for GetStringCritical and ReleaseStringCritical functions
 */
public class GetReleaseStringCriticalTest extends JNITest {
    private native char[] nativeExecute(String str);

    /**
     * Native code returns characters array from argument string.
     * Test checks that they are correct.
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception {
        String str = "dkk\u13ff\uffff\u0000\ubebe";
        char[] str1 = str.toCharArray();
        char[] str2 = nativeExecute(str);

        if (str1.length != str2.length)
            return false;
        for (int iii = 0; iii < str1.length; iii++)
            if (str1[iii] != str2[iii])
                return false;

        return true;
    }
    public static void main(String[] args){
        System.exit(new GetReleaseStringCriticalTest().test());
    }
}