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
/**
 */

package org.apache.harmony.test.func.reg.vm.btest1284;

import org.apache.harmony.share.Test;

/**
 * This test valid for IPF
 */
public class Btest1284 extends Test {

    public static void main(String[] args) {
        System.exit(new Btest1284().test()); 
    } 

    public int test() {
        if (fToD(0.1f, 0.1f) == (double)dToF(0.1, 0.1)) {
            return pass();  
        } 
        return fail("test fails");
    }

    public static double fToD (float f1, float f2) {
        return f1 + f2; 
    }

    public static float dToF (double d1, double d2) {
        return (float) (d1 + d2); 
    }
}
