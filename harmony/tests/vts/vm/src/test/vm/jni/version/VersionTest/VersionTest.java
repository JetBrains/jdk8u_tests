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
 * Created on 03.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.version;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * Test on correct results of jint GetVersion(JNIEnv *env) function
 */
public class VersionTest extends JNITest {
    private native boolean nativeExecute();

    /**
     * Test is successful
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     * @return test result true for passed, false for failed.
     */
    public boolean execute() {
        return nativeExecute();
    }
    public static void main(String[] args){
        System.exit(new VersionTest().test());
    }
}
