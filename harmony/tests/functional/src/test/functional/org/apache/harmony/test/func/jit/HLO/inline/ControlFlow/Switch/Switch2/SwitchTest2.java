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
package org.apache.harmony.test.func.jit.HLO.inline.ControlFlow.Switch.Switch2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 9.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class SwitchTest2 extends Test {
    
    public static void main(String[] args) {
        System.exit(new SwitchTest2().test(args));
    }
    
    String result = new String();
    
    public int test() {
        log.info("Start SwitchTest2 ...");
        try {
            int j = 0;
            int k = j;
            for (int i=0; i<10000; i++) {
                switch((int) Double.MIN_VALUE*0) {
                    case 0: 
                        for (; j<1; j++) 
                        inlineMethod();
                    default:
                        for (; k<1; k++) 
                        result = result.concat("!");
                }
                break;
            }
            log.info("Result == " + result);
            if (result.equals("Hello World!")) return pass();
            else return fail("TEST FAILED: result != \"Hello World!\"");
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unxpected " + e);
        }
    }

    final void inlineMethod() {
            switch ((int) Double.MAX_VALUE*0) {
            case 0:
                result = result.concat("Hello ");
            default:
                result = result.concat("World");
            }
    }
}
