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
package org.apache.harmony.test.func.jit.HLO.inline.ControlFlow.IfElse.IfElse1;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 10.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class IfElseTest1 extends Test {
    
    public static void main(String[] args) {
        System.exit(new IfElseTest1().test(args));
    }
        
    static boolean flag = true;
    
    public int test() {
        log.info("Start IfElseTest1 ...");
        try {
            int check = 0;
            for (int i=0; i<1000; i++) {
                for (int j=0; j<1000; j++) {
                    check = inlineMethod1(i, true);
                }
            }
            log.info("Result: " + check);
            if (check == 999) return pass();
            else return fail("TEST FAILED: test result != 999");
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unxpected " + e);
        }
    }

    final void inlineMethod2(int param) {
        param++;
        if (param > 1000) inlineMethod1(param, false);
        else inlineMethod1(param, true);
    }
    
    final int inlineMethod1(int param, boolean flag) {
        if (flag) inlineMethod2(param);
        return param;
    }
    
    
}