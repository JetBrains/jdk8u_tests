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
 * Created on 12.01.2004
 * Last modification G.Seryakova
 * Last modified on 12.01.2004
 *  
 * testing ClassLoader delegation model.
 */
package org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Test for delegation model of Class loading.
 * 
 */
public class F_ClassLoaderTest_02 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ClassLoaderTest_02().test());
    }

    public int test() {
        log.info("IN");
        Class cls = null;

        try {
            TestClassLoader clsLoader = new TestClassLoader(ClassLoader
                .getSystemClassLoader());
            cls = clsLoader
                .loadClass("F_ClassLoaderTest_01.auxiliary.SmallTestObject");
        } catch (ClassNotFoundException e) {
            return pass("Passed. file not found. It is expected result.");
        }
        
        return fail("file was found. It is not expected result.");
    }
}

class TestClassLoader extends ClassLoader {

    public TestClassLoader() {
        super();
    }

    public TestClassLoader(ClassLoader clsLoader) {
        super(clsLoader);
    }

    public Class findClass(String name) throws ClassNotFoundException {
        System.err.println("IN findClass");
        return super.findClass(name);
    }
}