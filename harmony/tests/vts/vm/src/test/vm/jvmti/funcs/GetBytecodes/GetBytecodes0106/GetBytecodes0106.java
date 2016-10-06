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
public class GetBytecodes0106 {

    static public void main(String args[]) {

        CustomClassLoader_GetBytecodes0106 ccl = new CustomClassLoader_GetBytecodes0106();

        try {
            Class specclass = ccl.loadClass(
                "org.apache.harmony.vts.test.vm.jvmti.SpecialClass003");

            SpecialClass003 MyClass
                = (SpecialClass003) specclass.newInstance();

            MyClass.superMegaMethod();
            MyClass = null;
            specclass = null;
            ccl = null;
        } catch (Throwable tex) { }

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

class CustomClassLoader_GetBytecodes0106 extends ClassLoader {
    int fake = 0;
}

