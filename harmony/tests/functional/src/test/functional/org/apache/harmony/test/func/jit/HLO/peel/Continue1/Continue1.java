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
package org.apache.harmony.test.func.jit.HLO.peel.Continue1;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 03.07.2006 
 */

public class Continue1 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Continue1().test(args));
    }

    public int test() {
        log.info("Start Continue1 test...");
        int limit = 50000;
        int even[] = new int[limit];
        int odd[] = new int[limit];
        for (int i=0; i<limit; i++) {
            if (i%2 == 0) continue;
            odd[i] = i;
            for (int j=limit-1; j>=0; j--) {
                if(j%2 != 0) continue;
                even[j] = j;
            }
        }
        for (int k=2; k<limit; k+=2) {
            if ((odd[k]!= 0) || (even[k-1]!= 0) || 
                    (odd[k-1] == 0) || (even[k] == 0)) {
                return fail("TEST FAILED: loop execution was broken on "
                                + k + " iteretion");
            }
        }
        return pass();
    }
    

}


