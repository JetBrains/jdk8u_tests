/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.reg.vm.btest6954;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.CrashTest;
/**
 * Failed assert when calling JNI functions
 * FromReflectedField()/FromReflectedMethod() 
 *
 */
public class Btest6954 extends CrashTest {

    public static void main(String[] args) {
        System.exit(new Btest6954().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        setTestName("org.apache.harmony.test.func.reg.vm.btest6954.Test6954");
        return run(getCommand(args));
    }
}

class Test6954 {
    public static void main(String[] args) throws Exception {
        System.loadLibrary("Btest6954");
        Field f = Test6954.class.getDeclaredField("name");
        Method m = Test6954.class.getDeclaredMethod("foo", 
                new Class[] {Object.class, Boolean.TYPE});
        Constructor c = Test6954.class.getDeclaredConstructor(
                new Class[]{String.class});
        
        if (!f.equals(testField(f)) 
                || !m.equals(testMethod(m)) 
                || !c.equals(testMethod(c))) {
            System.exit(123);
        }
    }    
    
    static native Field testField(Field f);
    static native Member testMethod(Member m);

    public Test6954(String name) {}
    
    public int foo(Object bar, boolean flag) {return 0;}
    
    public String name;
}
