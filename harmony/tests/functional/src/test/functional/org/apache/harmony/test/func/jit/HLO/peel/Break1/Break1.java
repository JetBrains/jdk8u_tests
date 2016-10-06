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
package org.apache.harmony.test.func.jit.HLO.peel.Break1;

import java.util.Arrays;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 30.06.2006 
 */

public class Break1 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Break1().test(args));
    }

    public int test() {
        log.info("Start Break1 test...");
        int check1 = 0, check2 = 0, check3 = 0;
        int limit1 = 100000;
        int limit2 = 70;
        int result[][] = new int[limit1][limit2];
        int arr[][] = new int[limit1][limit2];
        Arrays.fill(arr[0], 0);
        Arrays.fill(result[0], 0);
        Arrays.fill(arr[1], 1);
        Arrays.fill(result[1], -1);
        for (int j=1; j<limit2; j++) {
            for (int i=0; i<limit1; i++) {
                result[i][j] = result[i][j-1] + arr[i][j];
                check1+=result[i][j];
                if (result[i][j] > 0) {
                    check2 = i;
                    check3 = j;
                    break;
                }
            }
        }
        log.info("Check1 = " + check1);
        log.info("Check2 = " + check2);
        log.info("Check3 = " + check3);
        if ((check1==2346) && (check2==1) && (check3==69)) return pass();
        else return fail("TEST FAILED: check if loop optimization was done incorrectly");
    }    
}

