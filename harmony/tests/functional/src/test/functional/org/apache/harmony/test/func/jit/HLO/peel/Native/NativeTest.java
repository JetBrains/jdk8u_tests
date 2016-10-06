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
package org.apache.harmony.test.func.jit.HLO.peel.Native;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 19.06.2006 
 */

public class NativeTest extends Test {
    
    public static int integer = 0;
    
    private final native void nativeMethod();
    
    public static void main(String[] args) {
        System.exit(new NativeTest().test(args));
    }

    public int test() {
        log.info("Start NativeTest test ...");
        System.loadLibrary("peelTest");
        for(int k=0; k<10000; k++) {
            for(int i=0; i<20; i++) {
                nativeMethod();
            }
        }
        log.info("Control value: " + integer);
        if (integer == 200000) return pass();
        else return fail("TEST FAILED: Control value should be equal to 200000");
    }
}
