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
 * Created on 02.25.2005
 */
package org.apache.harmony.vts.test.vm.jni.reflection;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;
import java.lang.reflect.*;

/** 
 * Test receives the reflections of static and object field from 
 * native and checks if they match the reflections, acquired
 * by the java means.
 */
public class ToReflectedFieldTest extends JNITest{
    public double objectField;
    static public int staticField;
    static public int staticField1;
   
    private native java.lang.reflect.Field nativeExecute(); // returns the static field reflection
    private native java.lang.reflect.Field nativeExecute1(); // returns the object field reflection

    /**
     * Test logics function
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     * @return test result true for passed, false for failed.
     */
    public boolean execute() throws Exception{
        java.lang.reflect.Field staticF1 = nativeExecute();
        java.lang.reflect.Field objectF1 = nativeExecute1();

        java.lang.reflect.Field staticF2= this.getClass().getField("staticField");
        java.lang.reflect.Field staticF2_wrong= this.getClass().getField("staticField1");
        java.lang.reflect.Field objectF2= this.getClass().getField("objectField");

        if(!staticF1.equals(staticF2)) return false;
        if(staticF1.equals(staticF2_wrong)) return false;
        if(!objectF1.equals(objectF2)) return false;

        return true;
    }
    public static void main(String[] args){
        System.exit(new ToReflectedFieldTest().test());
    }
}
