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
package org.apache.harmony.test.func.jit.HLO.peel.LoopVar2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 19.06.2006
 */

public class LoopVar2 extends Test {
        
        boolean flag = false;
    
        public static void main(String[] args) {
            System.exit((new LoopVar2()).test(args));
        }
        
        public int test() {
            log.info("Start LoopVar2 peel test...");
            int limit = 10000;
            int i = -1;
            int k = -1;
            while(k<limit) {
                k++;
                for(i=0; i<k; i++) {
                    flag = true;
                }
            }
            log.info("" + i);
            log.info("" + k);
            if ((k == limit) && (i == limit)) return pass();
            else return fail("TEST FAILED: ");
        }
}
