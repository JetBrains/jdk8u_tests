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
 * Created on 08.11.2004
 */

package org.apache.harmony.vts.test.vm.jni.class_operations;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

interface IsAssignableFromTestInterface{
public void doNothing();
}

class TestClassForHierarchyCheck extends JNITest{
public boolean execute(){ return true; }
}
/**
 * @author Gregory Shimansky
 * 
 * Check that child class is assignable to the parent class
 */
public class IsAssignableFromTest extends TestClassForHierarchyCheck implements IsAssignableFromTestInterface{
    private native boolean nativeExecute(Class child, Class parent);

    /**
     * Check that child class IsAssignableFromTest is assignable to JNITest that
     * is its parent class. Check that reverse is true. Check that
     * IsAssignableFromTest is assignable to itself.
     * 
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() {
        boolean test1 = nativeExecute(
                org.apache.harmony.vts.test.vm.jni.class_operations.IsAssignableFromTest.class,
                org.apache.harmony.vts.test.vm.jni.share.JNITest.class);
        boolean test2 = nativeExecute(
                org.apache.harmony.vts.test.vm.jni.share.JNITest.class,
                org.apache.harmony.vts.test.vm.jni.class_operations.IsAssignableFromTest.class);
        boolean test3 = nativeExecute(
                org.apache.harmony.vts.test.vm.jni.class_operations.IsAssignableFromTest.class,
                org.apache.harmony.vts.test.vm.jni.class_operations.IsAssignableFromTest.class);
       boolean test6 = nativeExecute(java.lang.Byte.class, java.lang.Long.class);

        return test1 && !test2 && test3 && !test6;
    }
    public void doNothing(){ return; }
    public static void main(String[] args){
        System.exit(new IsAssignableFromTest().test());
    }
}
