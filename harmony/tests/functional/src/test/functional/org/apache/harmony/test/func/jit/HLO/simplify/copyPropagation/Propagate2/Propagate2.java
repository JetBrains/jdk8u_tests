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
package org.apache.harmony.test.func.jit.HLO.simplify.copyPropagation.Propagate2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 31.05.2006
 */

public class Propagate2 extends Test {
    
    private Object obj;
    protected static float var1 = 0f;
    
    public static void main(String[] args) {
        System.exit((new Propagate2()).test(args));
    }
        
    public int test() {
        log.info("Start Propagate2 test...");
        float[] arr = new float[10];    
        float var2 = 3.14f;
        for (int i=0; i<100; i++) {
            var1 = var2;
            arr[0] = var1 + var1;
            try {
                var1 = var2;
                obj.toString();
                var1 = var2;
            } catch (RuntimeException e) {
                var1 = 3f;
            }
            arr[0] = var1 + var1;
            var1 = var2;
        }
        if (arr[0] == 6) return pass();
        else return fail("TEST FAILED: copy propagation was incorrectly done");
    }
}


