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
package org.apache.harmony.test.func.jit.HLO.hvn.Branch;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 27.07.2006
 */

public class Branch extends Test {
    
    boolean flag = false;
    
    public static long l1 = 10000000000L;
    public long l2 =  10000000000L;
    
    private final native void nativeMethod();
    
    public static void main(String[] args) {
        System.loadLibrary("branch");
        System.exit((new Branch()).test(args));
    }
        
    public int test() {
        log.info("Start Branch test...");
        long value1 = 0;
        long value2 = 0;
        int i = 0;
        while(i < 10000) {
            if (flag) {
                value1 = l1/l2;
                i++;
            } else {
                nativeMethod();
            }
            value2 = l1/l2;
            i++;
        }
        log.info("value1=" + value1);
        log.info("value2=" + value2);
        if((value1 == 0) && (value2 == 2)) return pass();
        else return fail("TEST FAILED: possibly hash value numbering " +
                         "was incorrectly performed");
    }
    
}