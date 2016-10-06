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
 * Created on 23.08.2005
 *  
 * Class loader for testing ClassLoader class.
 */
package org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_08;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.test.func.api.java.lang.share.*;
import java.security.AccessControlException;

/**
 * Test for ClassLoader, RuntimePermission, SecurityManager classes.
 * 
 */
public class F_ClassLoaderTest_08 extends ScenarioTest {
    private int stat;

    public static void main(String[] args) {
        System.exit(new F_ClassLoaderTest_08().test(args));
    }

    public int test() {
        if (!task0()) {
            return fail("ClassNotFoundException with no expected result");
        }
        if (!task1()) {
            return fail("Not expected result");
        }
        return pass();
    }

    private boolean task0() {
        Class cls = null;
        Object obj = null;
        try {
            cls = getClass(ClassLoader.getSystemClassLoader(),
                "F_ClassLoaderTest_08.auxiliary.SmallTestObject");
        } catch (ClassNotFoundException e) {
            log.info("ClassNotFoundException. OK\n" + e.getMessage());
            return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean task1() {
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        RuntimePermission runtimePerm0 = new RuntimePermission(
            "createSecurityManager", "true");
        RuntimePermission runtimePerm1 = new RuntimePermission(
            "setSecurityManager.*", "false");
        RuntimePermission runtimePerm2 = new RuntimePermission(
            "getClassLoader.*", "true");
        return true;
    }

    private Class getClass(ClassLoader loader, String name)
        throws ClassNotFoundException {
        TestClassLoader clsLoader = new TestClassLoader(loader);
        Class cls = null;

        try {
            cls = clsLoader.loadClass(name);
        } catch (ClassNotFoundException e) {
            stat = clsLoader.loadTracker;
            throw new ClassNotFoundException("There is no class '" + name
                + "' here", e);
        }
        stat = clsLoader.loadTracker;

        return cls;
    }
}