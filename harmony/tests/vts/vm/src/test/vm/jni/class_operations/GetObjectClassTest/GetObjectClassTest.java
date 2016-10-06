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
 * @version $Revision: 1.6 $
 */  
/*
 * Created on 3.2.2004
 */
package org.apache.harmony.vts.test.vm.jni.class_operations;
import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * Test for GetObjectClass.
 */
public class GetObjectClassTest extends JNITest {
    private native Class nativeExecute(GetObjectClassTest obj);
    /**
     * Test gives the object to native, native returns the
     * class of object, java checks that the result is
     * the same as by the means of java level getClass() method
     *
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception {
	    GetObjectClassTest testObj = new GetObjectClassTest();
	    Class classObj = nativeExecute(testObj);
	    return classObj == testObj.getClass();
    }
    public static void main(String[] args) {
	System.exit(new GetObjectClassTest().test());
    }
}
