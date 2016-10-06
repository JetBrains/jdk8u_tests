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
 * Created on 8.12.2004
 * Last modification G.Seryakova
 * Last modified on 9.12.2004
 * 
 * Tests Method, Field, Constructor classes and ClassNotFoundException, NoSuchMethodException, IllegalArgumentException
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_ReflectionTest_03;

import org.apache.harmony.test.func.api.java.lang.reflect.share.ReflectionTestLib;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Tests Method, Field, Constructor classes and ClassNotFoundException,
 * NoSuchMethodException, IllegalArgumentException
 * 
 */
public class F_ReflectionTest_03 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ReflectionTest_03().test(args));
    }

    public int test() {
        String input[] = new String[] { "Boolean", "Byte", "Type", "Character",
            "Double", "Float", "Integer", "Long", "Short" };

        for (int i = 0; i < input.length; i++) {
            if (!task("java.lang." + input[i], i)) {
                return fail("");
            }
        }

        return pass();
    }

    private boolean task(String name, int ind) {
        Object arr[] = new Object[10];

        try {
            for (int i = 0; i < arr.length; i++) {
                if (ind == 0) {
                    arr[i] = ReflectionTestLib.createObject(name, arr
                        .getClass().getClassLoader(), new Object[] {},
                        new Class[] { String.class });
                } else {
                    arr[i] = ReflectionTestLib.createObject(name, arr
                        .getClass().getClassLoader(), new Object[] { Integer
                        .toString(i) }, new Class[] { String.class });
                }
            }
        } catch (ClassNotFoundException e) {
            if (ind != 2) {
                fail(e.toString());
                return false;
                //                expected result.
            } else {
                return true;
            }
        } catch (NoSuchMethodException e) {
            if (ind != 3) {
                fail(e.toString());
                return false;
                //                expected result.
            } else {
                return true;
            }
        } catch (IllegalArgumentException e) {
            if (ind != 0) {
                fail(e.toString());
                return false;
                //                expected result.
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        for (int i = 0; i < arr.length; i++) {
            try {
                if (i != ((Integer)ReflectionTestLib.invokeMethod(arr[i],
                    "intValue", new Object[] {}, new Class[] {})).intValue()) {
                    fail("Object created incorrectly for " + name
                        + " and value " + i);
                    return false;
                }
            } catch (Exception e) {
                fail(e.toString());
                return false;
            }
        }

        return true;
    }
}