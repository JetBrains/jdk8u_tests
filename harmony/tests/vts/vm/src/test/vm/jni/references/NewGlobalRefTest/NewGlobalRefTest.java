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
 * Test for NewGlobalRef function.
 */
public class NewGlobalRefTest extends JNITest {
    private native boolean nativeExecute(RefTestsClass object);

    /**
     * Test creates the object and transfers it 
     * to native, which stores it in the static variable.
     * Then the method is called again and it is checked
     * that the reference is valid, and it points the same 
     * object as before.
     */
    public boolean execute() throws Exception {
        RefTestsClass obj = new RefTestsClass();
        RefTestsClass.a = 1;

        if (!nativeExecute(obj)) {
		// cant not prepare test
		// ... return false;
		return false;
	  }
        RefTestsClass.a++;
	  return nativeExecute(obj);
    }
    public static void main(String[] args){
        System.exit(new NewGlobalRefTest().test());
    }
}