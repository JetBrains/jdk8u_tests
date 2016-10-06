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
package org.apache.harmony.test.func.jit.HLO.peel.Break2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 30.06.2006 
 */

public class Break2 extends Test {
    
    Object obj;
    
    public static void main(String[] args) {
        System.exit(new Break2().test(args));
    }

    public int test() {
        log.info("Start Break2 test...");
        int count = 0;
        final int limit = 100000;
        int A[] = new int[limit];
        int B[] = new int[limit];
        int C[] = new int[limit];
        int D[] = new int[limit];
        for (int i=0; i<limit; i++) B[i] = C[i] = i;
        int check1 = 0, check2 = 0;
        label1:
        do {
            label2:
            for (int i=0; i<limit; i++) {
                A[i] = B[i] + C[i];
                count++;
                if (count == limit*2) {
                    check1++;
                    break label1;
                }
                if (i >= 100) D[i] = A[i] - A[i-100];
                if (count > limit) {
                    check2++;
                    break label2; 
                }
            }
        } while (count < limit*2);
        log.info("Count = " + count);
        log.info("Check1 = " + check1);
        log.info("Check2 = " + check2);
        if ((count != limit*2) || (check1 !=1 ) || (check2 != limit-1))  
            return fail("TEST FAILED: check if loop optimization " +
                    "was done incorrectly");
        for (int i=100; i<limit; i++) {
            if ((A[i] != 2*i) || (D[i] != 200)) {
                log.info("A[" + i + "] = " + A[i] + " != " + 2*i);
                log.info("D[" + i + "] = " + D[i] + " != 200");
                return fail("TEST FAILED: check if loop optimization " +
                        "was done incorrectly");
            }
        }
        return pass();
    }
    
}

