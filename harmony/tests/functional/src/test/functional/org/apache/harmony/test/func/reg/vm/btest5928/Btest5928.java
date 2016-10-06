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
package org.apache.harmony.test.func.reg.vm.btest5928;

import java.util.logging.Logger; 
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 */
public class Btest5928 extends RegressionTest {

    public int fail(String message) {
        System.err.println(message);
        return fail();
    }

    public static void main(String[] args) {
        System.exit(new Btest5928().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        String nativeSeparator = System.getProperty("file.separator");
        String otherSeparator;

        if ("/".equals(nativeSeparator)) {
            otherSeparator = "\\";
        } else if ("\\".equals(nativeSeparator)) {
            otherSeparator = "/";
        } else {
            return fail("unrecognized file.separator \"" + nativeSeparator
                    + "\"");
        }

        String userDir = System.getProperty("user.dir");

        if (userDir.indexOf(otherSeparator) >= 0) {
            return fail("user.dir contains wrong file separator \""
                    + otherSeparator + "\"");
        }

        if (userDir.indexOf(nativeSeparator) < 0) {
            return fail("user.dir doesn't contain file separator \""
                    + nativeSeparator + "\"");
        }

        return pass();
    }
}
