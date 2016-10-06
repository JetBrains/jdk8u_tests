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
package org.apache.harmony.test.func.jit.HLO.inline.Exception.Exception9;

import org.apache.harmony.share.Test;
import org.apache.harmony.test.func.jit.HLO.share.UsefulMethods;

/**
 */

/*
 * Created on 25.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Exception9 extends Test {
    
    boolean flag = true;
    int count;
    
    public static void main(String[] args) {
        System.exit((new Exception9()).test(args));
    }
    
    public int test() {
        log.info("Start Exception9 test...");
        Error error = null;
        while (flag) {    
            try {
                inlineMethod();
            } catch (UnsatisfiedLinkError e) {
                error = e;
                continue;
            }
        }
        if (!UsefulMethods.checkStackTrace(error.getStackTrace(), 
                 "jit.HLO.inline.Exception.Exception9.Exception9.inlineMethod")) {
            log.add("Stack Trace: " + error);
            log.add(error);
            return fail("TEST FAILED: incorrect stack trace");
        }
        return pass();
    }
    
    final void inlineMethod() {
        if (count < 1000) {
            count++;
            System.loadLibrary("non_existing_lib");
        } else {
            flag = false;
        }
    }
}
