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
package org.apache.harmony.test.func.jit.HLO.lazyexc.IO1;

import java.io.File;
import java.io.IOException;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 19.04.2006
 */

public class IO1 extends Test {

        boolean flag = true;
        int count;
        
        public static void main(String[] args) {
            System.exit((new IO1()).test(args));
        }
        
        public int test() {
            log.info("Start IO1 lazyexc test...");
            for (int i=0; i<10000; i++) {
                for (int j=0; j<10; j++) {
                    try {
                        //TODO? insert some not invariant code: throw new TestException(i,j)
                        throw new TestException();
                    } catch (TestException e) {
                        
                    }
                }
            }
            File file = new File("HLO_laxyexc_Test.tmp");
            if (file.exists()) {
                file.delete();
                return pass();
            } else return fail("TEST FAILED: check if exception object was created. " 
                    + "Possible lazyexc was incorrectly performed.");
        }
}

class TestException extends Exception {
    
    TestException() {
        try {
            new File("HLO_laxyexc_Test.tmp").createNewFile();
        } catch (IOException e) {
            System.err.println("Unexpected exception " + e);
            e.printStackTrace();
        }
    }
}
