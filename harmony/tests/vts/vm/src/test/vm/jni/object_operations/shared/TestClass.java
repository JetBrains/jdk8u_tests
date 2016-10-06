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
 * @version $Revision: 1.1.1.1 $
 */  
/*
 * Created on 11.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.object_operations;

/**
 * @author Gregory Shimansky
 *
 * Test class that sets its static field in constructor to be used in
 * NewObject tests.
 */
class TestClass {
    private static int aaa = 0;

    public static void init() {
        aaa = 0;
    }

    public TestClass(int a) {
        aaa = a;
    }

    public static int getValue() {
        return aaa;
    }
}
