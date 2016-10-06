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
package org.apache.harmony.test.func.reg.vm.btest7394;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 */
public class Btest7394 extends RegressionTest {
    static {
        System.loadLibrary("Btest7394");
    }

    public static void main(String[] args) {
        System.exit(new Btest7394().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        Class cl = null;
        try {
            cl = runTest();
        } catch (NoClassDefFoundError e) {
            return cl == null ? pass() : fail();
        }

        return fail();
    }

    static native Class runTest();
}
