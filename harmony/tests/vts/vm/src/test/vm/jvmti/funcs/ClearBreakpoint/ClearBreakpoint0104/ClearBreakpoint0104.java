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
 * @version $Revision: 1.2 $
 *
 */ 
public class ClearBreakpoint0104 {

    static public void main(String args[]) {
        special_method();
        return;
    }

    static public void special_method() {
        int a, b, c, d, e, f, g, h, i, j;

        /*
         * Transfer control to native part.
         */
        try {
            throw new InterruptedException();
        } catch (Throwable tex) { }

        /*
         * Simple sequence of operations for
         * creating breakpoints here.
         */
        a = 2;
        b = a + 3;
        c = b + 4;
        d = c + 5;
        e = d + 6;
        f = e + 7;
        g = f + 9;
        h = g + 10;
        i = h + 11;
        j = i + 12;
        return;
    }
}


