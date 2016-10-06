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
/*
 * Created on 9.12.2004
 * Last modification G.Seryakova
 * Last modified on 21.12.2004
 * 
 * Create array of objects of dynamically loading class for testing Array and Constructor.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_ReflectionTest_04;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.reflect.*;

/**
 * Create array of objects of dynamically loading class for testing Array and
 * Constructor.
 * 
 */

public class F_ReflectionTest_04 extends ScenarioTest {
    private Object arr;

    public static void main(String[] args) {
        System.exit(new F_ReflectionTest_04().test(args));
    }

    public int test() {
        String name = "org.apache.harmony.test.func.api.java.lang.reflect.share.ReflectionTestObject";
        int count = 100;        
        ClassLoader clsLoad = new TestClassLoader();
        Class cls = null;
        Constructor constr = null;
        
        try {
            cls = clsLoad.loadClass(name);
        } catch (ClassNotFoundException e) {
            return fail("ClassNotFoundException : in ClassLoader.loadClass(name) for name = "
                + name);
        } catch (Exception e) {
            return fail(e.toString()
                + ": in ClassLoader.loadClass(name) for name = " + name);
        }
        
        try {
            constr = cls.getConstructor(new Class[] {});
        } catch (Exception e) {
            return fail(e.toString() + ": in getConstructor() for " + name);
        }

        try {
            if (!createArray(constr, count)) {
                return fail("Exception during array creation.");
            }
        } catch (Exception e) {
            return fail(e.toString() + ": in createArray() for " + constr + " and " + count);
        }

        try {
            for (int i = 0; i < Array.getLength(arr); i++) {
                if (!cls.isInstance(Array.get(arr, i))) {
                    return fail("array contain not expected element (it's not instance of expected class)");
                }
            }
        } catch (Exception e) {
            return fail(e.toString() + ": in array check for class - " + cls + ", constructor - " + constr + " and count - " + count);
        }

        return pass();
    }

    private boolean createArray(Constructor constr, int count) {
        arr = Array.newInstance(constr.getDeclaringClass(), count);
        for (int i = 0; i < Array.getLength(arr); i++) {
            try {
                Array.set(arr, i, constr.newInstance(new Object[] {}));
            } catch (Exception e) {
                fail(e.toString() + ": in Constructor.newInstance() for class "
                    + constr.getDeclaringClass().getName());
                return false;
            }
        }
        return true;
    }
}
class TestClassLoader extends ClassLoader {

    TestClassLoader() {
        super();
    }
}