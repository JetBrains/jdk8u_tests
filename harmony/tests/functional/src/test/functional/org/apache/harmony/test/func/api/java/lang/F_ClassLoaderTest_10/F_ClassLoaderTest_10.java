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
 * Created on 03.02.2006
 *  
 * testing ClassLoader methods; checking that LinkageError is thrown when necessary.
 */

package org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_10;

import org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_10.auxiliary.MyClassLoader;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * testing ClassLoader methods; checking that
 *          LinkageError is thrown when necessary.
 */

public class F_ClassLoaderTest_10 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ClassLoaderTest_10().test(args));
    }

    public int test() {
        String prefix = testArgs[0];
        try {
            MyClassLoader mcl = new MyClassLoader(prefix);
            Class mct = mcl.loadClass("FirstClass");
            Object obj = mct.newInstance();
            if (!((Boolean)mct.getMethod("start", null).invoke(obj, null))
                .booleanValue()) {
                return fail("Invoked method failed");
            } else {
                return pass();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail("Test failed (unexpected exception)");
        } catch (Error err) {
            err.printStackTrace();
            return fail("Test failed (unexpected error)");
        }
    }
}