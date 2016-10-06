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
package org.apache.harmony.test.func.jit.HLO.peel.EndlessLoop3;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class EndlessLoop3 extends MultiCase {

    int dependent = 0;
    boolean flag = true;
    
    Class cl;
    Object obj = new Object();
    
    public static void main(String[] args) {
        log.info("Start EndlessLoop3 Test ...");
        System.exit(new EndlessLoop3().test(args));
    }

    public Result testDead() {
        try {
            for(int k=1; k>0; k++) {
                for(int l=1; l>0; l++) {
                    dependent = k+l;
                    while(flag) {
                        final Integer i = Integer.valueOf("nonnumeric", k);
                    }
                }
            }
            return failed("TEST FAILED: NumberFormatException was expected");
        } catch (NumberFormatException e) {
            if (dependent == 2) return passed();
            else return failed("TEST FAILED: loop dependent variable " +
                    "was calculated incorrectly.");
        }
    }
    
    public Result testInvariant() {
        try {
            for(int j=1; j>0; j++) {
                while(flag) {
                    for(int k=1; k>0; k++) {
                        cl = (Class) obj;
                    }
                }
            }
            return failed("TEST FAILED: ClassCastException was expected");
        } catch (ClassCastException e) {
            return passed();
        }
    }
    
}
