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

package org.apache.harmony.test.func.reg.jit.btest928;

import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest928 extends RegressionTest{

    public static void main(String[] args) {
        System.exit(new Btest928().test(Logger.global, args));
    }
    
    public int test(Logger logger, String [] args) {        
        try {
             Interface928 i = new Test928();
            i.test();
            System.err.println("FAILED: IllegalAccessError was not thrown!");
            return failed();
        } catch (IllegalAccessError e) {
             System.err.println(
             "PASSED: Expected IllegalAccessError exception wasthrown:" + e);
             return pass();
        } catch (Exception e) {
            System.out.println("FAILED: Unexpected exception was thrown:");
            e.printStackTrace();
            return fail();
        }
     }
}