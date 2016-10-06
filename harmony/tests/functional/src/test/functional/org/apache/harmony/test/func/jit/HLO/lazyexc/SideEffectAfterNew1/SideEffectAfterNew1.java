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
package org.apache.harmony.test.func.jit.HLO.lazyexc.SideEffectAfterNew1;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 22.04.2006
 */

public class SideEffectAfterNew1 extends Test {
    
    public static void main(String[] args) {
        System.exit((new SideEffectAfterNew1()).test(args));
    }
        
    public int test() {
        log.info("Start SideEffectAfterNew1 lazyexc test...");
        String str = "";
        for (int i=0; i<100000; i++) {
            try {
                TestException exc = new TestException("TestException message");
                str = exc.getMessage();
                throw exc;
            } catch (TestException e) {
        
            }
        }
        if (str.equals("TestException message")) return pass();
        else return fail("TEST FAILED: TestException message wasn't stored at variable str.\n" +
                "Check if TestException wasn't created, " +
                "i.e. lazyexc was incorrectly performed.");
    }
        
}

class TestException extends Exception {
    
    TestException(String message) {
        super(message);
    }
     
}