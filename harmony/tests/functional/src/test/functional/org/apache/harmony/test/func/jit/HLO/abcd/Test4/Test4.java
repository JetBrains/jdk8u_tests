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
package org.apache.harmony.test.func.jit.HLO.abcd.Test4;

import org.apache.harmony.share.Test;
import java.lang.ArrayIndexOutOfBoundsException;

/**
 */

/*
 * Created on 19.06.2006 
 */

public class Test4 extends Test {
    
    private final int limit = 10000;
    public Object obj = null;
    
    public static void main(String[] args) {
        System.exit(new Test4().test(args));
    }

    public int test() {
        log.info("Start Test4 ...");
        int arr[] = new int[limit];
        int j=1;
        try {
            for(int k=2; k<limit; k=1+k+k*j) {
                if (k<0) log.info("---Overflow---");
                log.info("k=" + k + ": arr[" + (k-2) + "] will be called");
                arr[k] = arr[k-2];
                j = k*k;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return pass();
        } catch (Exception e) {
            log.info(e.toString());
            log.add(e);
            return fail("TEST FAILED: unexpected exception was thrown");
        }
        return fail("TEST FAILED: ArrayIndexOutOfBoundsException wasn't thrown");
    }
}


