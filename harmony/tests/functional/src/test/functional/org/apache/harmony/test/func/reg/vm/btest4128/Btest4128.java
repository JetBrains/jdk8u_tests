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
package org.apache.harmony.test.func.reg.vm.btest4128;

import java.util.logging.Logger; 
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 */
public class Btest4128 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest4128().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        for (int i = 0; i < 5; i++) {
            try {
                logger.info("iteration: " + i);
                Class cl = Class.forName("org.apache.harmony.test.func.reg.vm.btest4128.aa1");  
                cl.newInstance();
                logger.warning("succeded to load and instantiate class");
                return fail();
            } catch (VerifyError e) {
                logger.info("caught VerifyError (expected)");
                // continue loop
            } catch (Throwable e) {
                logger.warning("caught Throwable");
                return fail();
            } 
        }

        logger.info("caught VerifyError on every iteration (expected)");
        return pass();
    }
}
