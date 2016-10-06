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
package org.apache.harmony.test.func.jit.HLO.abcd.Test10;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 12.07.2006 
 */

public class Test10 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Test10().test(args));
    }
    
    public int test() {
        log.info("Start Test10 ...");
        int i = 0;
        int arr[] = new int[300000];
        try {
            for(i=0; i<100000; i++) {
                while(i<3) {
                    arr[i-1] = i;
                    i++;
                }
            }
            return fail("TEST FAILED: ArrayIndexOutOfBoundsException " +
                    "wasn't thrown");
        } catch (ArrayIndexOutOfBoundsException ae) {
            return pass();
        } catch (Exception e) {
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }

}