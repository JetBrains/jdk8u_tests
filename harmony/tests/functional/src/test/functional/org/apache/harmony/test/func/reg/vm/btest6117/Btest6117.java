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
package org.apache.harmony.test.func.reg.vm.btest6117;

import java.util.logging.Logger; 
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * Verifies that classpath is equal to "absent_path" value.
 * Checks that VM is able to run main class which is not in classpath
 * (it is expected to be in bootclasspath).
 * Precondition: VM must be run with expected value in -classpath (-cp) option
 */
public class Btest6117 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest6117().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        String class_path = System.getProperty("java.class.path");
        logger.info("java.class.path = " + class_path);

        if ("absent_path".equals(class_path)) {
            return pass();
        }

        logger.warning("This test requires VM to be run with unexisting " +
                "class path value equal to \"absent_path\"");
        return error();
    }
}

