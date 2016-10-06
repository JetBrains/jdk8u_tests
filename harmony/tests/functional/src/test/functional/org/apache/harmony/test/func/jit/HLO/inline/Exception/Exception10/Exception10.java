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
package org.apache.harmony.test.func.jit.HLO.inline.Exception.Exception10;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 11.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Exception10 extends Test {
    
    public static void main(String[] args) {
        System.exit((new Exception10()).test(args));
    }
    
    public int test() {
        log.info("Start Exception10 test...");
        try {
            for (int i=0; i<1000; i++) 
                for (int j=0; i<1000; j++) 
                    inlineMethod1();
            return fail("TEST FAILED: StackOverflowError was expected");
        } catch (StackOverflowError e) {
            return pass();
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }
        
    final void inlineMethod1() {
        inlineMethod2();
    }
    
    final void inlineMethod2() {
        inlineMethod1();
    }
}

