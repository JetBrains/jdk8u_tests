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
/**
 */
package org.apache.harmony.test.func.reg.vm.btest7228;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 */
public class Btest7228 extends RegressionTest {
    public static void main(String[] args) {
        System.exit(new Btest7228().test(Logger.global, args));
    }

    public void booleanParam1(Boolean param) {
        return;
    }

    public void booleanParam2(boolean param) {
        return;
    }

    public int test(Logger logger, String[] args) {
        Method method1, method2;

        try {
            method1 = Btest7228.class.getMethod("booleanParam1",
                                              new Class[] {
                                                  Boolean.class
                                              }
                                              );
            method2 = Btest7228.class.getMethod("booleanParam2",
                                              new Class[] {
                                                  boolean.class
                                              }
                                              );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return fail();
        }

        try {
            method1.invoke(new Btest7228(), new Object[] {
                               new Boolean(false)
                           }
                           );
            method2.invoke(new Btest7228(), new Object[] {
                               new Boolean(false)
                           }
                           );
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return fail();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return fail();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return fail();
        }
        return pass();
    }
}
