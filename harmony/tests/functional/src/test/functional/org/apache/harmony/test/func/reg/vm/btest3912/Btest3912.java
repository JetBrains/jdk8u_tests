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
package org.apache.harmony.test.func.reg.vm.btest3912;

import java.util.logging.Logger; 
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


/**
 * Checks that VM successfully loads class which static initializer has useless
 * access flags
 */
public class Btest3912 extends MultiCase {

    public static void main(String[] args) {
        System.exit(new Btest3912().test(args));
    }

    /**
    * Tries to load and istanciate specified class. 
    * Returns passed if no exception was thrown.
    */
    private Result classFormatTest(String className, String message) {
        Logger logger = Logger.global;
        
        logger.info("Verify: " + message);
        try {
            Class cl = Class.forName(className);
            cl.newInstance();
            return passed("class loaded and instantiated");
        } catch (Throwable e) {
            return failed("caught " + e);
        }
    }
    
    public Result test_8C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3912.testClInitFlags_8C", 
                "native static <clinit>");
    }

    public Result test_10C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3912.testClInitFlags_10C", 
                "abstract static <clinit>");
    }
}
