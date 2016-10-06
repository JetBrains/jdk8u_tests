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
package org.apache.harmony.test.func.jit.HLO.lazyexc.Finalize;

/**
 */

/*
 * Created on 19.04.2006
 */

import org.apache.harmony.share.Test;


public class Finalize extends Test {
    
    static boolean flag = false;
    
    public static void main(String[] args) {
        System.exit((new Finalize()).test(args));
    }
    
    public int test() {
        log.info("Start Finalize lazyexc test...");
        for (int i=0; i<10000; i++) {
            for (int j=0; j<100; j++) {
                try {
                    //TODO? insert some not invariant code: throw new TestException(i,j)
                    throw new TestException();
                } catch (TestException e) {
                
                }
            }
        }
        System.runFinalization();
        if (flag) return pass();
        else return fail("TEST FAILED: method finalize() wasn't called " +
                "on exception object.\n" +
                "Check if lazyexc was incorrectly performed.");
    }
}

class TestException extends Exception {

    protected void finalize() throws Throwable {
        Finalize.flag=true;
    }
}
