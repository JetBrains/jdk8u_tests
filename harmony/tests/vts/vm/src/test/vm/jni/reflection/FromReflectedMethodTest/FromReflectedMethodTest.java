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
 * @version $Revision: 1.5 $
 */  
/*
 * Created on 03.25.2005
 */
package org.apache.harmony.vts.test.vm.jni.reflection;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;
import java.lang.reflect.*;

/**
 * Test creates the reflections of String method and constructor and
 * provides them to native; in native, methodIDs are derived and 
 * it is checked that they are the same as those derived
 * by the ordinary JNI means.
 */
public class FromReflectedMethodTest extends JNITest{
    private native boolean nativeExecute(java.lang.reflect.Method mReflection);
    private native boolean nativeExecute1(java.lang.reflect.Constructor cReflection);

    /**
     * Test logics function
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     * @return test result true for passed, false for failed.
     */
    public boolean execute() throws Exception{
        java.lang.reflect.Method methodR1, methodR2;
        java.lang.reflect.Constructor constructorR1, constructorR2;
        String str = new String("Test");

        methodR1 = (str.getClass()).getMethod("length",(Class[])null);
        methodR2 = (str.getClass()).getMethod("hashCode",(Class[])null);
        Class[] list = new Class[1];
        list[0] = str.getClass();
        constructorR1 = (str.getClass()).getConstructor(list);
        constructorR2 = (str.getClass()).getConstructor((Class[])null);

        if(nativeExecute(methodR1) == false) return false;
        if(nativeExecute(methodR2) == true) return false;
        if(nativeExecute1(constructorR1) == false) return false;
        if(nativeExecute1(constructorR2) == true) return false;
        return true;
    }
    public static void main(String[] args){
        System.exit(new FromReflectedMethodTest().test());
    }
}
