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
public class CompiledMethodUnload0101 {

    static public void main(String args[]) {
        /*
         * Compiled method will unload only then its classloader will
         * unloaded
         */
        CustomClassLoader_CompiledMethodUnload0101 ccl = new CustomClassLoader_CompiledMethodUnload0101();
        try {
            Class specclass
                = ccl.loadClass(
                "org.apache.harmony.vts.test.vm.jvmti.SpecialClass001");

            System.err.print("\tJAVA: Special class is loaded\n");
            
            SpecialClass001 MyClass 
                = (SpecialClass001) specclass.newInstance();

            System.err.print("\tJAVA: New instance for Special class is created\n");            
            System.err.print("\tJAVA: call special method {\n");

            for (int i = 1; i < 100; i++) {

                MyClass.superMegaMethod();
            }

            System.err.print("\t}\n");
            
            /*
             * Kill all references to unloading method and
             * class and classloader
             */
            MyClass = null;
            specclass = null;
            ccl = null;
        } catch (Throwable tex) {}

        /*
         * Call to GC many times
         */
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        return;
    }
}

class CustomClassLoader_CompiledMethodUnload0101 extends ClassLoader {
    int fake = 0;
}

