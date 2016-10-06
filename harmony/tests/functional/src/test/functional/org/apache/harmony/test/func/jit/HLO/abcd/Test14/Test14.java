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
package org.apache.harmony.test.func.jit.HLO.abcd.Test14;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 17.07.2006 
 */

public class Test14 extends Test {
    
    byte neg = -1;
    
    public static void main(String[] args) {
        System.exit(new Test14().test(args));
    }
    
    public int test() {
        log.info("Start Test14 ...");
        int arr[][][][][] = new int[100000][][][][];
        try {
            for(int i=0; i<100000; i++) {
                if (i%2 == 0) arr[i] = new int[1][0][neg*i][1];
                else arr[i] = new int[1][0][i][1];
            }
            return fail("TEST FAILED: NegativeArraySizeException " +
                    "wasn't thrown");
        } catch (NegativeArraySizeException ae) {
            return pass();
        } 
    }

}