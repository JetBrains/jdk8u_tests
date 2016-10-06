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
 * Test provides class object to native, then native
 * derives the reflection of its method and returns it;
 * java checks that it matches the reflection, created by
 * the means of java level.
 */
public class ToReflectedMethodTest extends JNITest{
    private native java.lang.reflect.Method nativeExecute(String str);
    private native java.lang.reflect.Constructor nativeExecute1(String str);

    /**
     * Test passes if GetSuperclass for object representing this class 
     * returns class handle that is equal to what Class.forName returns.
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     * @return test result true for passed, false for failed.
     */
    public boolean execute() throws Exception{
        String str = new String("abc");
        java.lang.reflect.Method m1,m2;
        java.lang.reflect.Constructor c1,c2,c3;

        m1 = nativeExecute(str);
        c1 = nativeExecute1(str);

        m2 = (str.getClass()).getMethod("length",(Class[])null);
        Class[] list = new Class[1];
        list[0] = str.getClass();
        c2 = (str.getClass()).getConstructor((Class[])null);
        c3 = (str.getClass()).getConstructor(list);

        if(!m1.equals(m2)) return false;
        if(c1.equals(c2)) return false;
        if(!c1.equals(c3)) return false;

        return true;
    }
    public static void main(String[] args){
        System.exit(new ToReflectedMethodTest().test());
    }
}
