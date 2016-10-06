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

package org.apache.harmony.test.func.reg.jit.btest3257;

import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest3257 extends RegressionTest {
    
    public static void main(String[] args) {
         System.exit(new Btest3257().test(args));
    }
    
    public int test(String [] args) {
        boolean ret1 = testcase1();
        boolean ret2 = testcase2();
        System.err.println();
        return (ret1 && ret2) ? pass() : fail(); 
    }        

    boolean testcase1() {
        boolean ret = true;
        System.err.println("Start testcase 1...");
        
        for (long i = 0; i < 100; i++) {
            long res = i % 3;
            if (res<0 || res>2) {
                System.err.println("Error: " + res);
                ret = false;
            }
        }

        System.err.println("Testcase 1 " + (ret ? "PASSED" : "FAILED"));
        return ret;
    }

    boolean testcase2() {
        boolean ret = true;
        long d = 100;

        System.err.println("Start testcase 2...");

        for (long i = 1; i < 100; i++) {
            long res = d % i;
            if (res >= i) {
                System.err.println("Error: " +d + " % " + i + " = " + res);
                ret = false;
            }
        }

        System.err.println("Testcase 2 " + (ret ? "PASSED" : "FAILED"));
        return ret;
    }
    
}
