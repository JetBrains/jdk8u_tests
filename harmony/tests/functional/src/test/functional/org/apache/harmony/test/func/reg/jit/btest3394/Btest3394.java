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

package org.apache.harmony.test.func.reg.jit.btest3394;

import java.text.DecimalFormat;

import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest3394 extends RegressionTest {
   
    public static void main(String[] args) {
        long t = 122;
        DecimalFormat df = new DecimalFormat ("#####.##");
        double d = t/60.;
        String s = df.format(d);
        System.err.println("not formatted: " + d);
        System.err.println("formatted: " + s + " (2.03 expected).");
        System.exit((s.equals("2.03") || s.equals("2,03")) ? passed() : failed());
    }
}

