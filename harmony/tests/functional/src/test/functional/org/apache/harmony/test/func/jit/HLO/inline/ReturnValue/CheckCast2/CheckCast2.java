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
package org.apache.harmony.test.func.jit.HLO.inline.ReturnValue.CheckCast2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 8.11.2005
 * 
 * Jitrino HLO test
 *  
 */

public class CheckCast2 extends Test {

    public static void main(String[] args) {
        System.exit(new CheckCast2().test(args));
    }

    public int test() {
        log.info("Start CheckCast2 test...");
        try {
            float f = 0f;
            for(int i=0; i<1000000; i++) {
                f = (((float) inlineMethod()) % 1);     
            } 
            log.info("Result of (float)" + inlineMethod() + "%1 = " + f);
            if (f == 0.0f) return pass();
            else return fail("TEST FAILED");
        } catch (Throwable e) { 
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }

    final long inlineMethod() {
        return 100000000000L;
    }

}
