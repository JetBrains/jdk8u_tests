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
package org.apache.harmony.test.func.api.java.lang.F_StackTraceElementTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 19.11.2004
 * 
 * 
 *    Usage: 
 *        java.lang.StackTraceElement 
 *
 **/

public class F_StackTraceElementTest_01 extends ScenarioTest {

    public int test()
    {
        try {
            log.info("");
            myMethod();
        } catch (myException me) {
            StackTraceElement[] myStack = me.getStackTrace();
            for(int i=0;i<myStack.length;i++){
                log.info("StackTraceElement["+i+"] information:");
                log.info("File name: "+myStack[i].getFileName());
                log.info("Class name: "+myStack[i].getClassName());
                log.info("Method name: "+myStack[i].getMethodName());
                log.info("Line number: "+myStack[i].getLineNumber());
                log.info("**************************************");
            }
            return pass();
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }        
    public static void main(String[] args)
    {
        System.exit(new F_StackTraceElementTest_01().test(args));
    }
    public void myMethod() throws myException {
        throw new myException();
    }
    class myException extends Exception {
    }
}
