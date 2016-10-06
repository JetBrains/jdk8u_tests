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
package org.apache.harmony.test.func.jit.HLO.peel.Exception1;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 22.06.2006
 */

public class Exception1 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Exception1().test(args));
    }

    public int test() {
        log.info("Start Exception1 test ...");
        int check = 0;
        int i = 200;
        try {
            if (i>0) {
                while ( (check/i) > -1000) {
                    i-=2;
                    check++;
                }
            }
            return fail("TEST FAILED: ArithmeticException wasn't thrown");
        } catch (ArithmeticException e) {
            log.info("Iteration number: " + check);
            if (check == 100) return pass();
            else return fail("TEST FAILED: iteration number != 100");
        }
    }

}
