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

package org.apache.harmony.test.func.reg.jit.btest5017;

import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest5017 extends RegressionTest{

    public static void main(String[] args) {
        System.exit(new Btest5017().test());
    }
    
    public int test() {        
        double d;
        float f;
        boolean passed = true; 
        
        f = Float.MIN_VALUE;
        d = Double.MIN_VALUE;
        
        passed &= eq(f / f, 1.0);
        passed &= eq(d / d, 1.0);
        passed &= eq(f % f, 0.0);
        passed &= eq(f/(f - f), Float.POSITIVE_INFINITY);
        passed &= eq(d/(d - d), Double.POSITIVE_INFINITY);
        passed &= eq(-d/(d - d), Double.NEGATIVE_INFINITY);
        d = f;
        passed &= eq(d, (double)Float.MIN_VALUE);
        
        return passed ? pass() : fail();
     }

    private boolean eq(double a, double b)
    {
        System.err.println(a + (a == b?" == " : " != ") + b);
        return (a == b);
    }

}