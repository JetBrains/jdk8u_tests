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
package org.apache.harmony.test.func.reg.vm.btest8284;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * checks if the NoClassDefFoundError is thrown correctly
 * on missing runtime dependency.
 *
 */
public class NoClassDefFoundTest extends RegressionTest {
    public static void main(String[] args) {
        System.exit(new NoClassDefFoundTest().test(Logger.global, args)); 
    }

    public int test(Logger logger, String[] args) {
        try {
            Class A = new NoClassDefFoundLoader().loadClass("A");
            Object a = A.newInstance();
            logger.info("FAILED, no exception");
            return fail();
        } catch (NoClassDefFoundError e) {
            logger.info("PASSED, " + e);
            return pass();
        } catch (Throwable e) {
            logger.info("FAILED, " + e);
            return fail();
        }
    }
}

class NoClassDefFoundLoader extends ClassLoader {
    public static final byte[] A_class = new byte[] {
        /* compiled from

            public class A {
                B b;
                public A() {
                    b = new B();
                }
            }

            class B {
            }

        class B is deliberately omitted.
        */
        -54, -2, -70, -66, 0, 0, 0, 46, 0, 20, 10, 0, 6, 0, 15, 7, 0, 16, 10,
        0, 2, 0, 15, 9, 0, 5, 0, 17, 7, 0, 18, 7, 0, 19, 1, 0, 1, 98, 1, 0, 3,
        76, 66, 59, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86,
        1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109,
        98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 10, 83, 111, 117, 114, 99,
        101, 70, 105, 108, 101, 1, 0, 6, 65, 46, 106, 97, 118, 97, 12, 0, 9, 0,
        10, 1, 0, 1, 66, 12, 0, 7, 0, 8, 1, 0, 1, 65, 1, 0, 16, 106, 97, 118,
        97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 5,
        0, 6, 0, 0, 0, 1, 0, 0, 0, 7, 0, 8, 0, 0, 0, 1, 0, 1, 0, 9, 0, 10, 0,
        1, 0, 11, 0, 0, 0, 48, 0, 3, 0, 1, 0, 0, 0, 16, 42, -73, 0, 1, 42, -69,
        0, 2, 89, -73, 0, 3, -75, 0, 4, -79, 0, 0, 0, 1, 0, 12, 0, 0, 0, 14, 0,
        3, 0, 0, 0, 3, 0, 4, 0, 4, 0, 15, 0, 5, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14
    };

    public Class loadClass(String name) throws ClassNotFoundException {
        if ("A".equals(name)) {
            return defineClass("A", A_class, 0, A_class.length);
        }
        throw new ClassNotFoundException(name);
    }
}

