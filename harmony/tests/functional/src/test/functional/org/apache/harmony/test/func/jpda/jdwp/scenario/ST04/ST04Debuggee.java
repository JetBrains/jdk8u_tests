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
package org.apache.harmony.test.func.jpda.jdwp.scenario.ST04;

import org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee;

/**
 * Created on 04.10.2006 
 */
public class ST04Debuggee extends QARawDebuggee {
    /* 
     * RELATIVE_LINE_NUMBER_WITH_stepMethod_CALL constant
     * must correspond to the line number with 'stepMethod' call
     * inside 'run' method. 
     */
    public static final int RELATIVE_LINE_NUMBER_WITH_stepMethod_CALL = 6;
    
    /** 
     * The relative line (inside 'run' method) number with 
     * 'stepMethod' call must correspond to
     * RELATIVE_LINE_NUMBER_WITH_stepMethod_CALL constant.
     */
    /* (non-Javadoc)
     * @see jpda.share.Debuggee#run()
     */
    public void run() {        
        int dummyInt_01 = 1;
        int dummyInt_02 = 2;
        int dummyInt_03 = 3;
        int dummyInt_04 = 4;
        int dummyInt_05 = 5;
        stepMethod();        
        int dummyInt_06 = 6;
    }    
    
    private void stepMethod() {
        logWriter.print("ST04Debuggee: inside method 'stepMethod'!");
        logWriter.print("ST04Debuggee: Exiting from method 'stepMethod'...");
    }
    
    public static void main(String[] args) {
        runDebuggee(ST04Debuggee.class);
    }
}

