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
 * Created on 01.02.2006
 *  
 * testing loading class with custom classloader and checking whether this class is not loaded earlier than they should be loaded.
 */

package org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_09;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_09.auxiliary.MyClassLoader;

/**
 * 
 * testing loading class with custom classloader and
 *          checking whether this class is not loaded earlier than they should
 *          be loaded.
 */

public class F_ClassLoaderTest_09 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ClassLoaderTest_09().test(args));
    }

    public int test() {
        String prefix = testArgs[0];
        try {
            MyClassLoader mcl = new MyClassLoader(prefix);
            Class mct = mcl.loadClass("FirstClass");
            Object obj = mct.newInstance();
            if (((Boolean)mct.getMethod("test", null).invoke(obj, null))
                .booleanValue()) {
                return pass();
            } else {
                return fail("Invoked method FirstClass.test() returned false");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail("Test failed (unexpected exception)");
        }
    }
}