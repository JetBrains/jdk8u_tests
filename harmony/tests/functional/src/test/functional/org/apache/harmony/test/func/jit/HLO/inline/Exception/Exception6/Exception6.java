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
package org.apache.harmony.test.func.jit.HLO.inline.Exception.Exception6;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 06.09.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Exception6 extends Test {
        
    public static void main(String[] args) {
        System.exit((new Exception6()).test(args));
    }

    public int test() {
        log.info("Start Exception6 test...");
        try {
            for (int i=1; i<100; i++ ) {
                new TestClass1();
            }
            return fail("TEST FAILED: expected exception wasn't thrown");
        } catch (IllegalArgumentException e) {
            return pass();
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: caught unexpected exception " + e);
        }
    }
        
}

class TestClass1 {
    
    Object obj;
    
    TestClass1() throws IllegalArgumentException {
        obj = new TestClass2();
    }
}

class TestClass2 {
    TestClass2() throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }
}
