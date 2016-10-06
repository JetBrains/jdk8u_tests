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
public class ClassFileLoadHook0101 {
    static int TEST = 0xDEADBEEF;
   
    static public void main(String args[]) {

        System.err.print("\tJAVA: ClassFileLoadHook is loaded\n");

        String a = "agent_second_passed";
        String b = "agent_second_failed";

        /*
         * During of loading file TEST variable
         * must be changed.
         */
        if (TEST != 0xBABEF00D) {
            a = b;
        }

        /*
         * Transfer control to native part.
         */
        new Thread( a ) {
            public void run() {
                return;
            }
        }.start();
        return;
    }
}

