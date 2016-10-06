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
package org.apache.harmony.test.func.jit.HLO.abcd.Test2;

import org.apache.harmony.share.Test;
import java.lang.ArrayIndexOutOfBoundsException;

/**
 */

/*
 * Created on 19.06.2006 
 */

public class Test2 extends Test {

    private static int i = 0;
    private static final int limit = 100000;
    
    public static void main(String[] args) {
        System.exit(new Test2().test(args));
    }

    public int test() {
        log.info("Start Test2 ...");
        try {
            Thread[] threads = new Thread[limit];
            for (i=0; i<limit; i++) {
                if (i == (limit-1))  meth();
                threads[i] = threads[i]; 
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return pass();
        }
        return fail("TEST FAILED: ArrayIndexOutOfBoundsException " +
                " wasn't thrown");
    }
    
    private final void meth() {
        Test2.i = limit;
        log.info("ignore this line");
        log.info("ignore this line");
        log.info("ignore this line");
        log.info("ignore this line");
        log.info("ignore this line");
    }
}


