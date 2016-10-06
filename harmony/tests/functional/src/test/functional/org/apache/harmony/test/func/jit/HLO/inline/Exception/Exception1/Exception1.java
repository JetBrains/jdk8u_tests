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
package org.apache.harmony.test.func.jit.HLO.inline.Exception.Exception1;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 06.09.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Exception1 extends Test {
    
    Object obj;
    
    public static void main(String[] args) {
        System.exit(new Exception1().test(args));
    }

    public int test() {
        log.info("Start Exception1 test...");
        String result = "";
        Exception1 obj = new Exception1();
        for (int i=0; i<1024; i++) {
            try {
                result = obj.inlineMethod();
            } catch (NullPointerException e) {
                result = "test.NullPointerException";
            }
        }
        if (result.equals("inlineMethod.NullPointerException")) 
            return pass();
        else return fail("TEST FAILED: " + result);
    }

    String inlineMethod() {
        try {
            obj.getClass();
            return "inlineMethod.NoException";
        } catch (NullPointerException e) {
            return "inlineMethod.NullPointerException";
        }
    }
    
}
