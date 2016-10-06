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
package org.apache.harmony.test.func.reg.vm.btest6883;

import java.util.logging.Logger; 
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * Tries to load class which static initializer creates instance of an abstract
 * class. 
 * Checks that InstantiationError is thrown on first attempt and
 * NoClassDefFoundError on every succeeding attempt.
 */
public class Btest6883 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest6883().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        String className = "org.apache.harmony.test.func.reg.vm.btest6883.TestInstantiationError2";

        try {
            logger.info("attempt: 1");
            Class cl = Class.forName(className);  
            logger.warning("succeded to load class");
            return fail();
        } catch (InstantiationError e) {
            logger.info("caught InstantiationError (expected)");
            // continue 
        } catch (Throwable e) {
            logger.warning("caught " + e);
            return fail();
        } 

        for (int i = 2; i <= 5; i++) {
            try {
                logger.info("attempt: " + i);
                Class cl = Class.forName(className);  
                logger.warning("succeded to load class");
                return fail();
            } catch (NoClassDefFoundError e) {
                logger.info("caught NoClassDefFoundError (expected)");
                // continue loop
            } catch (Throwable e) {
                logger.warning("caught " + e);
                return fail();
            } 
        }

        logger.info("caught InstantiationError on first attempt and" +
                " NoClassDefFoundError on every succeeding attempt (expected)");
        return pass();
    }
}
