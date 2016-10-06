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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyDiv.AlgebraicIdentity;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 26.07.2006
 */

public class AlgebraicIdentity extends MultiCase {

    short s = Short.MIN_VALUE;
    long l = Long.MAX_VALUE;
        
    int zero = 0;
    
    public static void main(String[] args) {
        log.info("Start AlgebraicIdentity test...");
        System.exit((new AlgebraicIdentity()).test(args));
    }
    
    public Result test1() {
        try {
            final long one = 1;
            long result = (l/zero)/one;
            log.info("result = " + result);
            return failed("TEST FAILED: ArithmeticException wasn't thrown");
        } catch (ArithmeticException e) {
            return passed();
        }
    }
    
    public Result test2() {
        try {
            final short one = -1;
            short result = (short) ((s/zero)/one);
            log.info("result = " + result);
            return failed("TEST FAILED: ArithmeticException wasn't thrown");
        } catch (ArithmeticException e) {
            return passed();
        }
    }
    
    public Result test3() {
        try {
            final short one = -1;
            short result = (short) ((s%zero)/one);
            log.info("result = " + result);
            return failed("TEST FAILED: ArithmeticException wasn't thrown");
        } catch (ArithmeticException e) {
            return passed();
        }
    }
}
