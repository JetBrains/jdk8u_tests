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
package org.apache.harmony.test.func.jit.HLO.simplify.copyPropagation.Propagate1;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 31.05.2006
 */

public class Propagate1 extends Test {
    
    public static int i = 0;
    int result1 = 0;
    private final native void nativeMethod();
    
    public static void main(String[] args) {
        System.exit((new Propagate1()).test(args));
    }
        
    public int test() {
        log.info("Start Propagate1 test...");
        System.loadLibrary("copyPropagation");
        int j = 10;
        i = j;
        while(result1<i && result1<j) {
            result1+=2;
        }
        i = j;
        //log.info("result1=" + result1);
        int result2 = i*j+100*i-i%7-i/2;
        //log.info("result2=" + result2);
        i = j;
        nativeMethod();
        if (i==21 && result1==10 && result2==1092) return pass();
        else return fail("TEST FAILED: some of check values is incorrect.\n" +
                "Possibly copy propagation pass work incorrectly.");
    }
}
