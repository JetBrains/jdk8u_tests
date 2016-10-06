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
package org.apache.harmony.test.func.jit.HLO.lazyexc.NoSideEffect;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 22.04.2006
 */

public class NoSideEffect extends Test {

    public static void main(String[] args) {
        System.exit((new NoSideEffect()).test(args));
    }
        
    public int test() {
        log.info("Start NoSideEffect lazyexc test...");
        int[] array = new int[100000];
        for (int i=99999; i>=0; i--) {
            try {
                array[i] = i;
                NoSideEffect.class.getClass();
                //TODO? insert some not invariant code: throw new TestException(i,j)
                throw new TestException();
            } catch (TestException e) {
                array[i] = i; 
                array[i] = -i; 
            }
        }
        for (int i=99999; i>=0; i--) {
            if(array[i] != -i) {
                return fail("TEST FAILED: incorrect value in the pattern arrray.\n" +
                        "Check if lazyexc corrupts the code.");
            }
        }
        return pass();
    }
        
}

class TestException extends Exception {
     
}

