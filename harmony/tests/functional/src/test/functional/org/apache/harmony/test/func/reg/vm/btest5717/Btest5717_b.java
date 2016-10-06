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
package org.apache.harmony.test.func.reg.vm.btest5717;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 */
public class Btest5717_b extends RegressionTest {

    private final static int OS_WIN = 1;

    private final static int OS_LIN = 2;

    private final static int OS_UNKNOWN = -1;

    public static void main(String[] args) {
        System.exit(new Btest5717_b().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        try {
            switch (getOS()) {
            case OS_WIN:
                Runtime.getRuntime().exec(new String[] {"cmd", "/C", "move ./../../bin/classes/bug5717 ."});
                break;
            case OS_LIN:
                Runtime.getRuntime().exec(new String[] {"sh", "-c", "mv ./../../bin/classes/bug5717 ."});
            }
            return pass();
        } catch (Throwable e) {
            e.printStackTrace();
            return fail();
        }
    }
    private static int getOS() {
        String os = System.getProperty("os.name").toUpperCase();
        if (os.indexOf("WIN") != -1) {
            return OS_WIN;
        }
        if (os.indexOf("LIN") != -1) {
            return OS_LIN;
        }
        return OS_UNKNOWN;
    }
}
