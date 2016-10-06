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
package org.apache.harmony.test.func.jit.HLO.peel.TryCatch3;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 21.06.2006
 */

public class TryCatch3 extends Test {
    
    static final int limit = 1000;
    static int[] check = new int[limit];
    
    public static void main(String[] args) {
        System.exit((new TryCatch3()).test(args));
    }

    public int test() {
        log.info("Start TryCatch3 test...");
        int zero = 0;
        for (int i=0; i<100; i++) {
            for (int j=0; j<limit; j++) {
                try {
                    try {
                        zero = zero/zero;
                    } catch (ArithmeticException e) {
                        throw new TestException(j);
                    }
                } catch (TestException e) {
        
                }
            }
        }
        for (int j=0; j<limit; j++) {
            if (check[j] != j) return fail("TEST FAILED: check if " +
                "try...catch block was incorrectly hoisted outside a loop");
        }
        return pass();
    }
}

class TestException extends Exception {
    
    private static final long serialVersionUID = 1L;

    TestException(int i) {
        TryCatch3.check[i] = i;
    }
}
