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
 * Created on 02.25.2005
 */
package org.apache.harmony.vts.test.vm.jni.reflection;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;
import java.lang.reflect.*;

/**
 * Test creates the reflections of this method's 
 * object and static fields and transfers it to java.
 * java checks if the fieldID, derived from these reflections, 
 * match those derived by usual JNI means.
 */
public class FromReflectedFieldTest extends JNITest{
    public double objectField;
    static public int staticField;
   
    private native boolean nativeExecute(java.lang.reflect.Field objField, java.lang.reflect.Field statField);

    /**
     * Test logics function
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     * @return test result true for passed, false for failed.
     */
    public boolean execute() throws Exception{
        FromReflectedFieldTest obj = new FromReflectedFieldTest();

        java.lang.reflect.Field field1 = obj.getClass().getField("objectField");
        java.lang.reflect.Field field2 = obj.getClass().getField("staticField");

        return nativeExecute(field1, field2);
    }
    public static void main(String[] args){
        System.exit(new FromReflectedFieldTest().test());
    }
}

