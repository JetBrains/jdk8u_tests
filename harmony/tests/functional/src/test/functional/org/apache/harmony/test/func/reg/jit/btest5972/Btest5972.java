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

package org.apache.harmony.test.func.reg.jit.btest5972;

import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest5972 extends RegressionTest {
    
    public static void main(String[] args) {
        System.exit(new Btest5972().test(Logger.global, args));
    }
    
    public int test(Logger logger, String [] args) {
        
        int ret1=0;
        int ret2=0;
        
        try { 
            System.err.println("Use protected method (testcase1):");
            int res = new Test5972().test1();
            System.err.println("result is " + res);
            System.err.println("Testcase #1 FAILED!");
            ret1 = 1;
        } catch (IllegalAccessError e) {       
            System.err.println("Expected exception was thrown: " + e);
            System.err.println("Testcase #1 PASSED!");
        } catch (Throwable e) {
            System.out.println("Unexpected exception was thrown: ");
            e.printStackTrace();
            System.err.println("ERROR in testcase #1!");
            ret1 = 2;
        } 
        
        System.err.println();
        
        try { 
            System.err.println("Use private method (testcase 2):");
            int res = new Test5972().test2();
            System.err.println("result is " + res);
            System.err.println("Testcase #2 FAILED!");
            ret2 = 1;
        } catch (IllegalAccessError e) {       
            System.err.println("Expected exception was thrown: " + e);
            System.err.println("Testcase #2 PASSED!");
        } catch (Throwable e) {
            System.out.println("Unexpected exception was thrown: ");
            e.printStackTrace();
            System.err.println("ERROR in testcase #2!");
            ret2 = 2;
        }
        
        return ((ret1 ==0) && (ret2 == 0)) 
            ? passed() 
            : ((ret1==2) || (ret2==2)) ? getError() : failed();
    }
    
}
