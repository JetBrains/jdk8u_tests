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
/**
 */

package org.apache.harmony.test.func.reg.jit.btest5492;

public class Btest5492 {
    public static void main(String[] args) {
        System.exit((new Btest5492()).test());
    }

    public int test() {
        int res = -1;
        try {
            for (int i = 0; i < 1000; i++) {
                for (int j = 0; j < 100; j++) {
                    res = inlineMethod(Double.MAX_VALUE);
                }
            }
            if (res == 0) {
                return 0;
            } else {
                System.err.println("result=" + res);
                System.err.println("TEST FAILED: Optimizations " +
                    "cause wrong result of comparison with Double.NaN");
                return 1;
            }
        } catch (Throwable e) {
            System.err.println("TEST FAILED: unexpected " + e);
            return 1;
        }
    }
        
    final int inlineMethod(double i) {
        if (i / 0 < Double.NaN) {
            return 1;
        } else {
            return 0;
        }
    }

}
