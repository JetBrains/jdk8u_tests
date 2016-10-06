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
package org.apache.harmony.test.func.jit.HLO.peel.Fusion;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 26.06.2006
 */

public class Fusion extends Test {
        
        public static void main(String[] args) {
            System.exit((new Fusion()).test(args));
        }
        
        public int test() {
            log.info("Start Fusion test...");
            int limit = 200000;
            int length = 200001;
            int[] arr1 = new int[length];
            int[] arr2 = new int[length];
            int[] result = new int[length];

            for (int i=0; i<length; i++) {
                arr1[i] = i;
                arr2[i] = -2*i;
                result[i] = 5*i;
            }
            
            //--------------------------------------//
            int k = 0;
            for (k=0; k<limit; k++) {
                arr1[k] = arr1[0] + arr1[k]+ arr2[k]; 
            }
            for (k=0; k<limit; k++) {
                result[k] = result[0] + arr1[k+1];
            }
            //--------------------------------------//
            
            if(result[0] != -1) {
                log.info("result[0] = " + result[0]);
                return fail("TEST FAILED: result[0] != -1");
            }
            for (int i=1; i<limit-1; i++) {
                if(result[i] != -(i+2)) {
                    log.info("result[" + i + "] = " + result[i]);
                    return fail("TEST FAILED: result[" + i + "] != " + -(i+2));
                }
            }
            if(result[limit-1] != limit-1) {
                log.info("result[" + (limit-1) + "] = " + result[limit-1]);
                return fail("TEST FAILED: result[" + (limit-1) + "] != " + (limit-1));
            }
            return pass();
        }                  
}
