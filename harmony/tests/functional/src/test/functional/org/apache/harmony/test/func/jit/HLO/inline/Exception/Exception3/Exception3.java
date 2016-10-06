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
package org.apache.harmony.test.func.jit.HLO.inline.Exception.Exception3;

import org.apache.harmony.share.Test;


/**
 */

/*
 * Created on 06.09.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Exception3 extends Test{
    
    public static void main(String[] args) {
        System.exit(new Exception3().test(args));
    }

    public int test() {
        log.info("Start Exception3 test...");
        String check = new String();
        Exception3 obj = new Exception3();
        try {
            for (int i=0; i<100; i++) {
                obj.inlineMethod();
            }
            check = "test.NoException";
        } catch (Exception e) {
            check = "test.Exception";
        } 
        if (check.equals("test.Exception")) return pass();
        else return fail("TEST FAILED: " + check);
    }

    String inlineMethod() throws NegativeArraySizeException {
        String result;
        try {
            Object[] obj = new Object[-1];
            result = new String("inlineMethod.NoException");
        } catch (NegativeArraySizeException negativeArraySizeException) {
            result = new String("inlineMethod.NegativeArraySizeException");
            throw negativeArraySizeException;
        } catch (Exception e) {
            result = new String("inlineMethod.Exception");
        }
        return result;
    }
}
