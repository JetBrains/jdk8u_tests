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
package org.apache.harmony.vts.test.vm.jvmti;

/** 
 * @author Valentin Al. Sitnick
 * @version $Revision: 1.1 $
 *
 */ 
public class GetAllStackTraces0104 {

    static public void main(String args[]) {

        method_deep_1();
        return;
    }

    private static void method_deep_1() {
        method_deep_2();
        return;
    }
    
    private static void method_deep_2() {
        method_deep_3();
        return;
    }
    
    private static void method_deep_3()  {
        method_deep_4();
        return;
    }

    private static void method_deep_4()  {
        method_deep_5();
        return;
    }

    private static void method_deep_5()  {
        special_method();
        return;
    }

    private static void special_method()  {
        /*
         * Transfer control to native part.
         */
        try {
             throw new InterruptedException();
        } catch (Throwable tex ) { }
        return;
    }
}


