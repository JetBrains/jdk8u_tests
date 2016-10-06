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
package org.apache.harmony.test.func.jit.HLO.simplify.constPropagation.Propagate2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 31.05.2006
 */

public class Propagate2 extends Test {
    
    private double var = 0;
    
    public static void main(String[] args) {
        System.exit((new Propagate2()).test(args));
    }
        
    public int test() {
        log.info("Start Propagate2 test...");
        var = 2.22;
        double result1 = ((var%2)+(var*var*var*var))-(var*200);
        //log.info("result1=" + result1);
        var = 2.22;
        Object obj = new TestClass();
        double result2 = ((var%2)+(var*var*var*var))-(var*200);
        //log.info("result2=" + result2);
        if (Double.compare(var, 4.44)==0 
                && Double.compare(result1, -419.49087344000003)==0 
                && Double.compare(result2, -498.93397504)==0) 
            return pass();
        else return fail("TEST FAILED: some of check values is incorrect.\n" +
                "Possibly constant propagation pass work incorrectly.");
    }
    
    class TestClass {
        protected TestClass() {
            var = 4.44;
            // Redundant code to increase method size
            System.out.println("ignore this line");
            System.out.println("ignore this line");
            System.out.println("ignore this line");
            System.out.println("ignore this line");
            System.out.println("ignore this line");
            System.out.println("ignore this line");
            System.out.println("ignore this line");
            System.out.println("ignore this line");
            System.out.println("ignore this line");
            System.out.println("ignore this line");
        }
    }


}

