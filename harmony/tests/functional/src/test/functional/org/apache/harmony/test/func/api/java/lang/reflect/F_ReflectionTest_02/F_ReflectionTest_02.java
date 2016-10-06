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
 * Created on 3.12.2004
 * Last modification G.Seryakova
 * Last modified on 8.12.2004
 * 
 * Manipulating Objects.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_ReflectionTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.test.func.api.java.lang.reflect.share.*;
import java.lang.reflect.*;

/**
 * Manipulating Objects.
 * 
 */

public class F_ReflectionTest_02 extends ScenarioTest {
    private static String ls   = System.getProperty("line.separator");
    private String        stat = new String("");

    public static void main(String[] args) {
        System.exit(new F_ReflectionTest_02().test(args));
    }

    public int test() {
        int count = 0;
        ReflectionTestObject objs[] = new ReflectionTestObject[100];

        try {
            objs[0] = (ReflectionTestObject)ReflectionTestLib
                .createObject(
                    "org.apache.harmony.test.func.api.java.lang.reflect.share.ReflectionTestObject",
                    F_ReflectionTest_02.class.getClassLoader());
            incrementField(objs[0], "counter");
        } catch (Exception e) {
            return fail(e.getMessage());
        }

        for (byte i = 1; i < objs.length; i++) {
            Class types[] = new Class[] { byte.class,
                ReflectionTestObject.class };
            Object args[] = new Object[] { new Byte(i), objs[i - 1] };
            try {
                objs[i] = (ReflectionTestObject)ReflectionTestLib
                    .createObject(
                        "org.apache.harmony.test.func.api.java.lang.reflect.share.ReflectionTestObject",
                        F_ReflectionTest_02.class.getClassLoader(), args, types);
                incrementField(objs[i], "counter");
            } catch (Exception e) {
                return fail(e.getMessage());
            }
        }

        try {
            count = ((Integer)ReflectionTestLib.getFieldValue(objs[0],
                "counter")).intValue();
        } catch (Exception e) {
            return fail(e.getMessage());
        }
        if (count != 100) {
            return fail("Object count not equal.");
        }

        for (byte i = 1; i < objs.length; i++) {
            if (i > 1) {
                try {
                    Object obj = ReflectionTestLib.invokeMethod(objs[i - 1],
                        "getPrevObj", new Object[] {}, new Class[] {});

                    if (obj.equals(ReflectionTestLib.invokeMethod(objs[i],
                        "getPrevObj", new Object[] {}, new Class[] {}))) {
                        return fail("Objects are not sequential.");
                    }
                } catch (Exception e) {
                    return fail(e.getMessage());
                }
            }

            try {
                if (((Byte)ReflectionTestLib
                    .getFieldValue(objs[i], "objNumber")).byteValue() != i) {
                    return fail("Object was not created correctly." + i);
                }
            } catch (Exception e) {
                return fail(e.getMessage());
            }

            Class types[] = new Class[] { ReflectionTestObject.class };
            Object args[] = new Object[] { objs[0] };
            try {
                ReflectionTestLib.invokeMethod(objs[i], "setPrevObj", args,
                    types);
            } catch (Exception e) {
                return fail(e.getMessage());
            }
        }

        for (byte i = 1; i < objs.length; i++) {
            try {
                Object obj = ReflectionTestLib.invokeMethod(objs[i],
                    "getPrevObjPriv", new Object[] {}, new Class[] {});
                if (!obj.equals(objs[0])) {
                    return fail("Objects are not updated correctly." + i);
                }
            } catch (Exception e) {
                return fail(e.getMessage());
            }
        }

        return pass();
    }

    private void incrementField(Object obj, String name)
        throws NoSuchFieldException {
        try {
            Field fld = ReflectionTestLib.getField(obj.getClass(), name);
            fld.set(obj, new Integer(fld.getInt(obj) + 1));
        } catch (NoSuchFieldException e) {
            throw new NoSuchFieldException("In incrementField (" + name + "):"
                + e.getMessage());
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }
}