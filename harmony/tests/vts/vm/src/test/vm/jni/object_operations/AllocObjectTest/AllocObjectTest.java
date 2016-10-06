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
 * @version $Revision: 1.3 $
 */  
/*
 * Created on 11.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.object_operations;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Gregory Shimansky
 *
 * Test for object creation with AllocObject. This should call static
 * constructor for the object but not call its default constructor.
 */
public class AllocObjectTest extends JNITest {
    private native boolean nativeExecute(Class cl);

    /**
     * Native code calls AllocObject and then test checks that
     * static constructor was called but constructor was not.
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception {
        if (!nativeExecute(AllocObjectTestClass.class))
            return false;

        return AllocObjectTestClass.getF1() == 10 &&
            AllocObjectTestClass.getF2() == 0;
    }
    public static void main(String[] args){
        System.exit(new AllocObjectTest().test());
    }
}
class AllocObjectTestClass {
    private static int f1 = 0;
    private static int f2 = 0;

    static {
        f1 = 10;
    }

    public AllocObjectTestClass() {
        f2 = 15;
    }

    public static int getF1() {
        return f1;
    }

    public static int getF2() {
        return f2;
    }
}
