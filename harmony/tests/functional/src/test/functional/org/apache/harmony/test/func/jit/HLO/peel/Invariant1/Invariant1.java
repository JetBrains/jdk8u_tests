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
package org.apache.harmony.test.func.jit.HLO.peel.Invariant1;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 06.06.2006
 */

public class Invariant1 extends Test {
        
        public static void main(String[] args) {
            System.exit((new Invariant1()).test(args));
        }
        
        public int test() {
            log.info("Start Invariant1 test...");
            int invariant = 0;
            int limit = 300000;
            int[] arr1 = new int[limit];
            int[] arr2 = new int[limit];
            for (int i=0; i<limit; i++) {
                    invariant = new Integer(3).intValue();
                    arr1[i] = invariant*i;
                    invariant = new Integer(5).intValue();
                    arr2[i] = invariant*i*2;
                    invariant = new Integer(7).intValue();
            }
            for (int i=0; i<limit; i++) {
                if ((arr1[i] != 3*i) || (arr2[i] != 10*i)) {
                    log.info("arr1[" + i + "] = " + arr1[i] + " != " + 3*i);
                    log.info("arr2[" + i + "] = " + arr2[i] + " != " + 10*i);
                    return fail("TEST FAILED: check if load hoisting " +
                            "was performed incorrectly");
                }
            }
            return pass();
        }                  
}
