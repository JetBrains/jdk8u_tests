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
 * Created on 22.12.2004
 * Last modification G.Seryakova
 * Last modified on 22.12.2004
 *  
 * Class loader for testing ClassLoader class.
 */
package org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.test.func.api.java.lang.share.*;
import java.lang.reflect.*;

/**
 * Test for ClassLoader class.
 * 
 */
public class F_ClassLoaderTest_01 extends ScenarioTest {
    private int stat;

    public static void main(String[] args) {
        System.exit(new F_ClassLoaderTest_01().test(args));
    }

    public int test() {

        if (!task1()) {
            return fail("Not expected result");
        }

        if (!task2()) {
            return fail("Not expected result");
        }

        if (!task3()) {
            return fail("Not expected result");
        }

        if (!task4()) {
            return fail("Not expected result");
        }

        return pass();
    }

    private boolean task1() {
        Class cls = null;
        Object obj = null;

        try {
            cls = getClass(ClassLoader.getSystemClassLoader(),
                "F_ClassLoaderTest_01.auxiliary.SmallTestObject");
        } catch (ClassNotFoundException e) {
            return false;
        }

        try {
            obj = cls.newInstance();
            Field fld = cls.getField("i");
            fld.set(obj, new Integer(10));
            if (fld.getInt(obj) != 10) {
                fail("Wrong field value.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        
        if (cls.getPackage().isSealed()) {
            fail("Package mustn't be sealed.");
            return false;
        }

        log.info("OK");

        return true;
    }

    private boolean task2() {
        try {
            getClass(null, "F_ClassLoaderTest_01.auxiliary.SmallTestObject");
        } catch (ClassNotFoundException e) {
            if (stat == 3) {
                log.info("OK");
                return true;
            }
        }

        return false;
    }

    private boolean task3() {
        try {
            getClass(ClassLoader.getSystemClassLoader(),
                "F_ClassLoaderTest_01.aauxiliary.SmallTestObject");
        } catch (ClassNotFoundException e) {
            if (stat == 3) {
                log.info("OK");
                return true;
            }
        }

        return false;
    }

    private boolean task4() {
        try {
            getClass(ClassLoader.getSystemClassLoader(),
                "F_ClassLoaderTest_01.auxiliary.F_LongTest");
        } catch (ClassNotFoundException e) {
            if (stat == 1) {
                log.info("OK");
                return true;
            }
        }

        return false;
    }

    private Class getClass(ClassLoader loader, String name)
        throws ClassNotFoundException {
        TestClassLoader clsLoader = new TestClassLoader(loader);
        Class cls = null;

        try {
            cls = clsLoader.loadClass(name);//from
            // org.apache.harmony.test.func.api.java.lang.
        } catch (ClassNotFoundException e) {
            stat = clsLoader.loadTracker;
            throw e;
        }
        stat = clsLoader.loadTracker;

        return cls;
    }
}