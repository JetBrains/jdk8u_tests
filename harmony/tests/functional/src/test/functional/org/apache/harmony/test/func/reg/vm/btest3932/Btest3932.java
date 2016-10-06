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
package org.apache.harmony.test.func.reg.vm.btest3932;

import java.util.logging.Logger; 
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


/**
 * Checks that VM throws ClassFormatError on attempt to load interface with
 * illegal method flags.
 */
public class Btest3932 extends MultiCase {

    public static void main(String[] args) {
        System.exit(new Btest3932().test(args));
    }

    /**
    * Tries to load specified class. 
    * Returns passed if ClassFormatError was thrown.
    */
    private Result classFormatTest(String className, String message) {
        Logger logger = Logger.global;
        
        logger.info("Verify: " + message);
        try {
            Class cl = Class.forName(className);
            cl.newInstance();
            return failed("class loaded and instantiated");
        } catch (ClassFormatError e) {
            return passed("caught ClassFormatError (expected)");
        } catch (Throwable e) {
            return failed("caught " + e);
        }
    }
    
    public Result test_0C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3932.testMethodsIntfFlags_0C", 
                "private abstract interface method");
    }

    public Result test_1C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3932.testMethodsIntfFlags_1C", 
                "protected abstract interface method");
    }

    public Result test_2C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3932.testMethodsIntfFlags_2C", 
                "public abstract final interface method");
    }

    public Result test_3C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3932.testMethodsIntfFlags_3C", 
                "public abstract native interface method");
    }

    public Result test_4C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3932.testMethodsIntfFlags_4C", 
                "public abstract synchronized interface method");
    }

    public Result test_5C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3932.testMethodsIntfFlags_5C", 
                "public abstract static interface method");
    }

}
