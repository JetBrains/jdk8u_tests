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
package org.apache.harmony.test.func.jit.HLO.peel.TryCatch2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 21.06.2006
 */

public class TryCatch2 extends Test {
    
    int k = 0;
    boolean flag = false;
    boolean bool = true;
    int arr2[] = new int[100000];
    
    public static void main(String[] args) {
        System.exit((new TryCatch2()).test(args));
    }

    public int test() {
        log.info("Start TryCatch2 test...");
        int arr1[] = new int[100000];
        int i = 0;
        try {
            for (i=0; i<100000; i++) {
                arr1[i] = arr1[0] + arr2[i]/arr2[i];
                while(bool) {
                    k++;
                }
            }
        } catch (ArithmeticException e) {
            flag = true;
        }
        if (flag && (i==0)) return pass();
        return fail("TEST FAILED: loop execution was broken");
    }
    
}


