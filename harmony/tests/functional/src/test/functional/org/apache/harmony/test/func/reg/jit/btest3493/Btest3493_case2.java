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

package org.apache.harmony.test.func.reg.jit.btest3493;

import org.apache.harmony.test.share.reg.RegressionTest;

import java.math.BigDecimal;

public class Btest3493_case2 extends RegressionTest {
   
    public static void main(String[] args) {
        float f = Float.valueOf("-0.0").floatValue();
        System.err.println("Float: -0.0 = " + f);
           double d = Double.valueOf("-0.0").doubleValue();
        System.err.println("Double: -0.0 = " + d);
        System.exit(String.valueOf(f).equals("-0.0") 
                        && String.valueOf(d).equals("-0.0") 
                ? passed() : failed());
    }
}