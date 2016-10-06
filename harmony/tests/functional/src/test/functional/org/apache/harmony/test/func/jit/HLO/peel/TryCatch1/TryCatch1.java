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
package org.apache.harmony.test.func.jit.HLO.peel.TryCatch1;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 06.06.2006
 */

public class TryCatch1 extends Test {

    static int limit = 11;
    
    public static void main(String[] args) {
        System.exit((new TryCatch1()).test(args));
    }

    public int test() {
        log.info("Start TryCatch1 loop peeling test...");
        float arr[] = new float[10];
        try {
            for (int i=0; i<limit; i++) {
                try {
                    arr[i] = i;
                } catch (ArrayIndexOutOfBoundsException e) {
        
                } finally {
                    arr[-1] = -1f;
                }
            }
            return fail("TEST FAILED: ArrayIndexOutOfBoundsException " +
                    "was expected");
        } catch (ArrayIndexOutOfBoundsException e) {
            return pass();
        }
    }
}


