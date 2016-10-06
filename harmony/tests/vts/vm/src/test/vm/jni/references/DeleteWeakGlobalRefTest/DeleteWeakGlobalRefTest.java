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
 * @version $Revision: 1.2 $
 */  
/*
 * Created on 04.06.2005
 */
package org.apache.harmony.vts.test.vm.jni.references;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Petr Ivanov
 *
 * Test for DeleteWeakGlobalRef function.
 */
public class DeleteWeakGlobalRefTest extends JNITest {
    private native boolean nativeExecute(RefTestsClass obj);

    /**
     * Test creates the weak global ref to parameter object 
     * and checks that this object is accessible through 
     * this reference then the ref is deleted; Testing of weakness of this ref
     * is done in DeleteLocalRefTest.
     */
    public boolean execute() throws Exception {
        RefTestsClass obj = new RefTestsClass();
	    return nativeExecute(obj);
    }
    public static void main(String[] args){
        System.exit(new DeleteWeakGlobalRefTest().test());
    }
}
