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
package org.apache.harmony.test.func.jit.HLO.inline.ReturnValue.CheckCast3;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 8.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class CheckCast3 extends Test {

    public static void main(String[] args) {
        System.exit(new CheckCast3().test(args));
    }

    public int test() {
        log.info("Start CheckCast3 test...");
        try {
            byte b = 0;
            for(int i=0; i<100000; i++) {
                b = (byte) inlineMethod();    
            }
            log.info("byte b == " + b);
            if (b >= Byte.MIN_VALUE && b <= Byte.MAX_VALUE) return pass();
            else return fail("TEST FAILED: result value " + 
                    "is't lie in 'byte' range");
        } catch (Throwable e) { 
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }

    final short inlineMethod() {
        return Short.MIN_VALUE;
    }

    
}
