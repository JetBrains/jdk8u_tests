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
package org.apache.harmony.test.func.jit.HLO.abcd.Test13;

import org.apache.harmony.share.Test;



/**
 */

/*
 * Created on 17.07.2006 
 */

public class Test13 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Test13().test(args));
    }
    
    public int test() {
        log.info("Start Test13 ...");
        int sum = 0;
        int length = 1000;
        int[][] array = new int[length][length]; 
        for (int i = 0; i < length; i++)  {
            array[i] = new int[i]; 
        }
        try {
            for (int i = 0; i < length; i++)  {  
                sum += check(array, i); 
            } 
            return fail("TEST FAILED: ArrayIndexOutOfBoundsException " +
                        "wasn't thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            return pass();
        }
    }
   
    int check(int[][] array, int length) { 
        int sum = 0;
        for (int i = length-1; i >= 0; i--) {
            if (i < length-1) {
                // ArrayIndexOutOfBoundsException should be thrown 
                // when call array[0][0], because array[0] = int[0]
                // log.info("Call array[" + i + "][" + i + "]");
                i = array[i][i]; 
                sum += i;
            }
        }
        return sum;
    }
}