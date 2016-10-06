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

package org.apache.harmony.test.func.reg.jit.btest1902;

import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest1902 extends RegressionTest {
   
    public static void main(String[] args) {
         System.exit(new Btest1902().test());
    }
    
    public int test() {
        boolean ret = true;
        
        double d = (double)Long.MAX_VALUE;
      
        if (Math.round(d) != Long.MAX_VALUE ) {
            System.err.println("Case1: FAILED");
            System.err.println("   Math.round(d)=" + Math.round(d) + 
                    " (should be " + Long.MAX_VALUE + ").");
            ret = false;
        } else {
            System.err.println("Case1: PASSED");
        }
        
        d = (double)Long.MAX_VALUE + 1.0f;        
        if (Math.round( d ) != Long.MAX_VALUE ) {
            System.err.println("Case2: FAILED");
            System.err.println("   Math.round(d)=" + Math.round(d) + 
                    " (should be " + Long.MAX_VALUE + ").");
            ret = false;
        } else {
            System.err.println("Case2: PASSED");
        }
        
        System.err.println();
        return ret ? passed() : failed();
    }    
}

