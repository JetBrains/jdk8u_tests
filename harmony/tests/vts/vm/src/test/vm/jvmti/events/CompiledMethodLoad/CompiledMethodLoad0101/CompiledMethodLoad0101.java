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
public class CompiledMethodLoad0101 {

    private static long sum = 0;

    static public void main(String args[]) {

        for (int i = 0; i < 1000; i++) {
            special_method();
        }
        return;
    }

    /*
     * Special method which must be compiled during
     * test build and start.
     */
    static public void special_method() {

        for (int i = 0; i < 10000; i++) {
            sum += i;
        }
        sum -= 100;
        return;
    }
}

