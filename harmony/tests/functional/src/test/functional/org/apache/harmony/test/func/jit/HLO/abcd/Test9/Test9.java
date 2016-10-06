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
package org.apache.harmony.test.func.jit.HLO.abcd.Test9;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

import java.lang.ArrayIndexOutOfBoundsException;

/**
 */

/*
 * Created on 14.07.2006 
 */

public class Test9 extends MultiCase {
    
    public static void main(String[] args) {
        log.info("Start Test9 ...");
        System.exit(new Test9().test(args));
    }

    public Result test1() {
        int length = 20000;
        int index[] = new int[length];
        byte arr[] = new byte[length];
        try {
            for (int k = 0; k < length; k=+2) {
                arr[index[k] - 10000000] -= index[k];
            }
            return failed("TEST FAILED: ArrayIndexOutOfBoundsException " +
                    "wasn't thrown");
        } catch (ArrayIndexOutOfBoundsException ae) {
            return passed();
        } catch (Throwable e) {
            log.add(e);
            return failed("TEST FAILED: unexpected " + e);
        }
    }
    
    public Result test2() {
        int length = 10000;
        int index[] = new int[length];
        long arr[] = new long[length];
        index[1] = length-1;
        int neg = -1;
        try {
            for (int k = length-1; k>0; k--) {
                arr[index[k] - neg] = 0;
            }
            return failed("TEST FAILED: ArrayIndexOutOfBoundsException " +
                    "wasn't thrown");
        } catch (ArrayIndexOutOfBoundsException ae) {
            return passed();
        } catch (Throwable e) {
            log.add(e);
            return failed("TEST FAILED: unexpected " + e);
        }
    }
    
}
