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
package org.apache.harmony.test.func.jit.HLO.peel.Switch;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 04.07.2006 
 */

public class Switch extends Test {
    
    public static void main(String[] args) {
        System.exit(new Switch().test(args));
    }

    public int test() {
        log.info("Start Switch test ...");
        boolean error = false;
        int check0 = 0;
        int check1 = 0;
        int checkDef = 0;
        for (int i=0, j=0;  j<100000; j++, i=j%3) {
            switch (i) {
                case 0:
                    for (int k=0; ; k++) {
                        check0++;
                        if (k > 1000) break;
                    }
                case 1:
                    for (int k=0; ; k++) {
                        check1++;
                        if (k > 1000) break;
                    }
                    break;
                default:
                    checkDef++;
                    if (i==2) continue;    
    
                error = true;
            }
        }
        log.info("Check0 = " + check0);
        log.info("Check1 = " + check1);
        log.info("CheckDef = " + checkDef);
        if (error) return fail("TEST FAILED: unreachable code was executed");
        if ((check0 != 33400668) || (check1 != 66800334) || (checkDef != 33333)) 
            return fail("TEST FAILED: loop execution was broken");
        return pass();
    }
}
