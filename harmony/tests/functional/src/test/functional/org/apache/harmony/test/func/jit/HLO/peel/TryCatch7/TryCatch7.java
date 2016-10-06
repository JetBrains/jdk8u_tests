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
package org.apache.harmony.test.func.jit.HLO.peel.TryCatch7;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 22.06.2006
 */

public class TryCatch7 extends Test {

    static int limit1 = 10000;
    static int limit2 = 10;
    
    public static void main(String[] args) {
        System.exit((new TryCatch7()).test(args));
    }

    public int test() {
        log.info("Start TryCatch7 test...");
        for (int i=0; i<limit1; i++) {
            try {
                throw new TestException();
            } catch (TestException e) {
                for (int k=0; k<limit2; k++) {
                    try {
                        throw e;
                    } catch (Exception exc) {
                        
                    }
                }
            } finally {
                for (int k=0; k<limit2; k++) {
                    label:
                    for (int l=0; l<limit2; l++) {
                        continue label;
                    }
                }
            }
        }
        return pass();
    }
}

class TestException extends Exception {

    private static final long serialVersionUID = 1L;
    
}


