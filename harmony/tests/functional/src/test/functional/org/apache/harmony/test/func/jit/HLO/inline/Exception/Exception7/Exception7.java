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
package org.apache.harmony.test.func.jit.HLO.inline.Exception.Exception7;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 06.09.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Exception7 extends Test {
    
    static int result = 0;
    
    public static void main(String[] args) {
        System.exit(new Exception7().test(args));
    }

    public int test() {
        log.info("Start Exception7 test...");
        int excCount=0;
        try {
            for (int k=0; k<100; k++) {
                Exception7.inlineMethod1();
            }
        } catch (RuntimeException e) {
            excCount++;
        } 
        if (result == 400 && excCount==0) return pass();
        else return fail("TEST FAILED " + result);
    }
    
    static void inlineMethod1() {
        try {
            inlineMethod2();
        } catch (RuntimeException e) {
            result++;
        }
    }
    
    static void inlineMethod2() {
        try {
            inlineMethod3();
        } catch (RuntimeException e) {
            result++;
            throw e;
        }
    }
    
    static void inlineMethod3() {
        try {
            inlineMethod4();
        } catch (RuntimeException e) {
            result++;
            throw e;
        }
    }
    
    static void inlineMethod4() {
        String[] param = new String[5];
        try {
            inlineMethod5(param);
        } catch (RuntimeException e) {
            result++;
            throw e;
        }
    }
    
    static void inlineMethod5(String[] str) {
        str[10] = "";
    }
    
    
    
}

