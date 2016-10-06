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
/*
 * Created on 19.11.2004
 */
package org.apache.harmony.test.func.api.java.lang.F_ThrowableTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * 
 *    Usage: 
 *        java.lang.Throwable 
 *
 **/

public class F_ThrowableTest_01 extends ScenarioTest {

    public int task1()
    {
        try {
            highLewelCalculation1();
        } catch (myHighException mhe) {
            StackTraceElement[] myStackTrace = mhe.getStackTrace();
            for(int i=0;i<myStackTrace.length;i++) {
                log.info(myStackTrace[i].toString());
            }
            log.info("***************************");
            return pass();
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }        
    public int task2()
    {
        try {
            highLewelCalculation2();
        } catch (myHighException mhe) {
            StackTraceElement[] myStackTrace = mhe.getStackTrace();
            for(int i=0;i<myStackTrace.length;i++) {
                log.info(myStackTrace[i].toString());
            }
            log.info("***************************");
            return pass();
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }        
    public int test()
    {
        try {
            if (task1() != Result.PASS || task2() != Result.PASS) 
                return fail("test NOT passed");
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }        
    public static void main(String[] args)
    {
        System.exit(new F_ThrowableTest_01().test(args));
    }
    public void highLewelCalculation1() throws myHighException {
        try {
            middleLewelCalculation1();
        } catch (myMiddleException mme) {
            throw new myHighException(mme.fillInStackTrace());
        }
    }
    public void highLewelCalculation2() throws myHighException {
        try {
            middleLewelCalculation2();
        } catch (myMiddleException mme) {
            throw new myHighException(mme.fillInStackTrace());
        }
    }
    public void middleLewelCalculation1() throws myMiddleException {
        try {
            lowLewelCalculation1();
        } catch (myLowException mle) {
            Throwable myThrowable = new Throwable(mle.fillInStackTrace());
            throw new myMiddleException(myThrowable);
        }
    }
    public void middleLewelCalculation2() throws myMiddleException {
        try {
            lowLewelCalculation2();
        } catch (myLowException mle) {
            Throwable myThrowable = new Throwable("middleLewelThrowable", mle.fillInStackTrace());
            throw new myMiddleException(myThrowable);
        }
    }
    public void lowLewelCalculation1() throws myLowException {
        Throwable myThrowable = new Throwable();
        throw new myLowException(myThrowable);
    }
    public void lowLewelCalculation2() throws myLowException {
        Throwable myThrowable = new Throwable("lowLewelThrowable");
        throw new myLowException(myThrowable);
    }
    class myHighException extends Exception {
        public myHighException(Throwable throwable) {
            super(throwable);
        }
    }
    class myMiddleException extends Exception {
        public myMiddleException(Throwable throwable) {
            super(throwable);
        }
    }
    class myLowException extends Exception {
        public myLowException(Throwable throwable) {
            super(throwable);
        }
    }
}