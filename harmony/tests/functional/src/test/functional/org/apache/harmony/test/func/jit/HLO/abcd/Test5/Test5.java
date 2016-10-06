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
package org.apache.harmony.test.func.jit.HLO.abcd.Test5;

import org.apache.harmony.share.Test;


/**
 */

/*
 * Created on 27.07.2006 
 */

public class Test5 extends Test {
    
    int num = 0;
    
    public static void main(String[] args) {
        System.exit(new Test5().test(args));
    }

    public int test() {
        log.info("Start Test5 ...");
        int[][] array = new int[1000][100];
        try {
            num = check(array);
            return fail("TEST FAILED: ArrayIndexOutOfBoundsException " +
                        "wasn't thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            return pass();
        }
    }
    
    int check(int[][] array) {
        int sum = 0;
        int j = array.length-1;
        for(int i = 0; i < array.length; i++) {
            j = array[i].length-1;
            if (j < array[i].length) {
                while(j >= 0) {
                    sum += array[i][j];
                    j--;
                    if (i == 10) {
                        j =  array.length-1;
                        continue;
                    }
                }
            }
        }
        return sum;
    }

}



