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

package org.apache.harmony.test.func.reg.jit.btest1907;

import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest1907 extends RegressionTest {
   
    public static void main(String[] args) {
         System.exit(new Btest1907().test(Logger.global, args));
    }
    
    public int test(Logger logger, String [] args) {
        boolean ret = true;
        
        String s = Double.toString(1);
        if(!s.equals("1.0")) {
            ret = false;
            System.err.println("Double.toString(1) returns \"" + s 
                    + "\" (\"1.0\" expected)!");
            System.err.println("Case 1 FAILED!");
        } else {
            System.err.println("Case 1 PASSED!");            
        }
        
        s = Double.toString(0);
        if(!s.equals("0.0")) {
            ret = false;
            System.err.println("Double.toString(0) returns \"" + s 
                    + "\" (\"0.0\" expected)!");
            System.err.println("Case 2 FAILED!");
        } else {
            System.err.println("Case 2 PASSED!");            
        }
        
        
        s = Double.toString(Double.POSITIVE_INFINITY);
        if(!s.equals("Infinity")) {
            ret = false;
            System.err.println(
                    "Double.toString(Double.POSITIVE_INFINITY) returns \""     + s
                    + "\" (\"Infinity\" expected)!");
            System.err.println("Case 3 FAILED!");
        } else {
            System.err.println("Case 3 PASSED!");            
        }
               
        s = Double.toString(Double.NEGATIVE_INFINITY);
        if(!s.equals("-Infinity")) {
            ret = false;
            System.err.println(
                    "Double.toString(Double.NEGATIVE_INFINITY) returns \"" + s
                    + "\" (\"-Infinity\" expected)!");
            System.err.println("Case 4 FAILED!");
        } else {
            System.err.println("Case 4 PASSED!");            
        }
        
        s = Double.toString(Double.NaN);
        if(!s.equals("NaN")) {
            ret = false;
            System.err.println("Double.toString(Double.NaN) returns \"" 
                    + s + "\" (\"Nan\" expected)!");
            System.err.println("Case 5 FAILED!");
        } else {
            System.err.println("Case 5 PASSED!");            
        }

        System.err.println();
        return ret ? passed() : failed();
    }
}

