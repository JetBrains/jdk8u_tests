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
package org.apache.harmony.test.func.reg.vm.btest6936;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

import java.lang.reflect.AccessibleObject;

/**
 */
public class Btest6936 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest6936().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        try {
            Class.class.getDeclaredConstructor(null).setAccessible(false);
        } catch (Exception e) {
            fail();
        }
        try {
            AccessibleObject.setAccessible((new AccessibleObject[]{Class.class.getDeclaredConstructor(null)}), false);
        } catch (Exception e) {
            fail();
        }
        try {
            Class.class.getDeclaredConstructor(null).setAccessible(true);
            fail();
        } catch (SecurityException e) {
        } catch (Exception e) {
            fail();
        }
        try {
            AccessibleObject.setAccessible((new AccessibleObject[]{Class.class.getDeclaredConstructor(null)}), true);
            fail();
        } catch (SecurityException e) {
        } catch (Exception e) {
            fail();
        }
        return pass();
    }
}
