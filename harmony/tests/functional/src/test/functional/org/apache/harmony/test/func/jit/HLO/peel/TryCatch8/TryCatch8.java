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
package org.apache.harmony.test.func.jit.HLO.peel.TryCatch8;

import java.util.Arrays;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 22.06.2006
 */

public class TryCatch8 extends Test {
    
    public static void main(String[] args) {
        System.exit((new TryCatch8()).test(args));
    }

    public int test() {
        log.info("Start TryCatch8 test...");
        int length = 1000000;
        int arr[] = new int[length];
        final TestException e = new TestException();
        int i=-1;
        boolean flag = true;
        for (i=length-1; i>0; i--) {
            try {
                throw e;
            } catch (TestException exc) {
                if (flag) continue;
            }
            arr[i] = i;
        }
        log.info("Number of performed iterations = " + i);
        Arrays.sort(arr);
        if (arr[length-1] > 0) 
            return fail("TEST FAILED: unreachable code was executed");
        if (i != 0) 
            return fail("TEST FAILED: loop variable value was corrupted");
        else return pass();
    }
}

class TestException extends Exception{

    private static final long serialVersionUID = 1L;
    
}

