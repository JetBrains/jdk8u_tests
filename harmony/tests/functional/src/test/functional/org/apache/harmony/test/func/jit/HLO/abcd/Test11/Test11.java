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
package org.apache.harmony.test.func.jit.HLO.abcd.Test11;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 17.07.2006 
 */

public class Test11 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Test11().test(args));
    }
    
    public int test() {
        log.info("Start Test11 ...");
        final int arr[][][][] = new int[20][20][20][10];
        final int index[] = new int[10];
        try {
            loop(arr, index);
            return fail("TEST FAILED: ArrayIndexOutOfBoundsException " +
                        "wasn't thrown");
        } catch (Exception e) {
            if (e.getClass().equals(ArrayIndexOutOfBoundsException.class)) 
                return pass();
            return fail("TEST FAILED: unexpected " + e);
        } 
    }
    
    void loop(int[][][][] arr, int[] index) {
        for(int i1=0; i1<index.length; i1++) {
            for(int i2=0; i2<index.length; i2++) {
                for(int i3=0; i3<index.length; i3++) {
                    for(int i4=0; i4<index.length; i4++) {
                        arr[i1][i2][i3][i4] = i1;
                    }
                }
            }
            index = new int[15];
        }
    }
}