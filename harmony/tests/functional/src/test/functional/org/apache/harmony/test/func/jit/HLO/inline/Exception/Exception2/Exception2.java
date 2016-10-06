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
package org.apache.harmony.test.func.jit.HLO.inline.Exception.Exception2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 06.09.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Exception2 extends Test {
        
    public static void main(String[] args) {
        System.exit(new Exception2().test(args));
    }

    public int test() {
        log.info("Start Exception2 test...");
        String result = new String();
        Exception2 obj = new Exception2();
        for (int i=0; i<1024; i++) {
            try {
                result = obj.inlineMethod(); 
            } catch (ClassNotFoundException e) {
                result = "test.ClassNotFoundException";
            }
        }
        if (result.equals("inlineMethod.Finally")) return pass();
        else return fail("TEST FAILED: " + result);
    }

    String inlineMethod() throws ClassNotFoundException {
        String result = new String();
        try {
            Class.forName("NonExistingClass");
        } catch (ClassNotFoundException e) {
            result  = "inlineMethod.ClassNotFoundException";
        } finally {
            result  = "inlineMethod.Finally";
        }
        return result;
    }
    
}
