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
package org.apache.harmony.test.func.jit.HLO.abcd.Test12;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 17.07.2006 
 */

public class Test12 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Test12().test(args));
    }
    
    public int test() {
        log.info("Start Test12 ...");
        long arr[][][] = new long[30001][6][3];
        try {
            loop(arr);
            return fail("TEST FAILED: ArrayIndexOutOfBoundsException " +
            "wasn't thrown");
        } catch (Exception e) {
            if (e.getClass().equals(ArrayIndexOutOfBoundsException.class)) 
                return pass();
            return fail("TEST FAILED: unexpected " + e);
        } 
    }

    void loop(long[][][] arr) {
        for(int i1=arr.length-1; i1>=0; i1--) {
            for(int i2=5; i2>=0; i2--) {
                for(int i3=2; i3>=0; i3--) {
                    arr[i1][i2][i3] += arr[i1][i2][i3]-i1;
                    i2 -= arr[i1][i2][i3];
                }
            }
        }
    }
}
    