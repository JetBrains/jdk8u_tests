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
 * Created on 11.02.2005
 */
package org.apache.harmony.vts.test.vm.jni.references;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Petr Ivanov
 *
 * Test for DeleteLocalRef function.
 */
public class DeleteLocalRefTest extends JNITest {
    private native int nativeExecute(RefTestsClass obj);

    /**
     * Test creates the local object in native and sets a weak reference 
     * to it, then deletes this local reference and invokes System.gc(); 
     * then it compares the object that is referenced by weak ref with null
     * and thereby checks that this object have been GC'ed.
     */
    public boolean execute() throws Exception {
        RefTestsClass obj = new RefTestsClass();
        RefTestsClass.a = 1;

	return nativeExecute(obj) == 1000;
    }
    public static void main(String[] args){
        System.exit(new DeleteLocalRefTest().test());
    }
}
