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
package org.apache.harmony.test.func.jit.HLO.peel.TryCatch6;

import java.util.Arrays;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 22.06.2006
 */

public class TryCatch6 extends Test {

    static final int limit1 = 80000;
    static final int limit2 = 100;
    boolean arr1[] = null;
    boolean arr2[] = null;
        
    public static void main(String[] args) {
        System.exit((new TryCatch6()).test(args));
    }

    public int test() {
        log.info("Start TryCatch6 test...");
        String str[] = new String[limit1];
        long arr[] = new long[limit1];
        try {
            for (int i=0; i<limit1; i++) {
                try {
                    if (Arrays.equals(arr1, arr2)) str[i] = String.valueOf(limit1);
                    else throw new Exception();
                } finally {
                    for (int j=0; j<limit2; ++j) {
                        for (int k=0; k<limit2; k++) {
                            arr[i] = k;
                        }
                    }
                }
            }
        } catch (Throwable e) {
            log.info("Unexpected " + e + " is occurred");
            log.add(e);
            return fail("TEST FAILED");
        }
        for (int i=0; i<limit1; i++) {
            if (!str[i].equals(String.valueOf(limit1))) 
                return fail("TEST FAILED: str[" + i + "] != " + limit1);
        }
        for (int i=0; i<limit2; i++) {
            if (arr[i] != limit2-1) 
                return fail("TEST FAILED: arr[" + i + "] != " + (limit2-1));
        }
        return pass();
    }
}


