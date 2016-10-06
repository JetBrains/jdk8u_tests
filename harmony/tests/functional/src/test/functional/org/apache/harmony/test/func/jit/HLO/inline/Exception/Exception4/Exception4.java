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
package org.apache.harmony.test.func.jit.HLO.inline.Exception.Exception4;

import org.apache.harmony.share.Test;


/**
 */

/*
 * Created on 06.09.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Exception4 extends Test{
    
    public static void main(String[] args) {
        System.exit(new Exception4().test(args));
    }

    public int test() {
        log.info("Start Exception4 test...");
        String check = new String();
        Throwable exc = null;
        Exception4 obj = new Exception4();
        try {
            for (int i=0; i<5000; i++) {
                obj.inlineMethod();
            }
            check = "test.NoException";
        } catch (IndexOutOfBoundsException e) {
            exc = e.getCause();
            check = "test.Exception";
        }
        if (check.equals("test.Exception")
                && exc.getClass().equals(ArrayIndexOutOfBoundsException.class)) 
            return pass();
        else return fail("TEST FAILED: " + check);
    }

    void inlineMethod() throws IndexOutOfBoundsException {
        int[] arr = new int[5];
        try {
            int error = arr[10];
        } catch (ArrayIndexOutOfBoundsException e) {
            IndexOutOfBoundsException iobe = new IndexOutOfBoundsException();
            iobe.initCause(e);
            throw iobe;
        } catch (Exception exc) {
            
        }
    }
}
