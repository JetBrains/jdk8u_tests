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
package org.apache.harmony.test.func.reg.vm.btest3900;

import java.util.logging.Logger; 
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


/**
 * Checks that VM throws ClassFormatError on attempt to load class which has
 * constructor with illegal access flags.
 */
public class Btest3900 extends MultiCase {

    public static void main(String[] args) {
        System.exit(new Btest3900().test(args));
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
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3900.testInitFlags_0C", 
                "public private <init>");
    }

    public Result test_1C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3900.testInitFlags_1C", 
                "public protected <init>");
    }

    public Result test_2C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3900.testInitFlags_2C", 
                "protected private <init>");
    }

    public Result test_3C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3900.testInitFlags_3C", 
                "public final  <init>");
    }

    public Result test_4C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3900.testInitFlags_4C", 
                "public static  <init>");
    }

    public Result test_5C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3900.testInitFlags_5C", 
                "public native  <init>");
    }

    public Result test_6C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3900.testInitFlags_6C", 
                "public synchronized  <init>");
    }

    public Result test_7C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3900.testInitFlags_7C", 
                "public abstract <init> with code");
    }

    public Result test_8C() {
        return classFormatTest("org.apache.harmony.test.func.reg.vm.btest3900.testInitFlags_8C", 
                "public abstract <init> with empty body");
    }

}
