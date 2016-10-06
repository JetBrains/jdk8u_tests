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
package org.apache.harmony.test.func.jit.HLO.inline.ControlFlow.IfElse.IfElse2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 10.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class IfElseTest2 extends Test {
    
    public static void main(String[] args) {
        System.exit(new IfElseTest2().test());
    }
    
    long check = 0;

    public int test() {
        log.info("Start IfElseTest2 ...");
        try {
            for (int i=0, j=0; i*j<100000000; ++i, ++j) {
                inlineMethod(i, j);
            }
            log.info("Result: " + check);
            if (check == 10000) return pass();
            else return fail("TEST FAILED: test result is incorrect");
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unxpected " + e);
        }
    }

    final void inlineMethod(long param1, long param2) {
        if (((param1>param2) && (param1<param2)) || (param1==param2)) check+=2;
        check--;
    }
}