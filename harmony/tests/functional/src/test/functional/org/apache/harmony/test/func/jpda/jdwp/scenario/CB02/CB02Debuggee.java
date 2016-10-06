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
package org.apache.harmony.test.func.jpda.jdwp.scenario.CB02;

import org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee;

/**
 * Created on 03.10.2006 
 */
public class CB02Debuggee extends QARawDebuggee {
    /* 
     * RELATIVE_LINE_NUMBER_WITH_exceptionMethod_CALL constant
     * must correspond to the line number with 'exceptionMethod' call
     * inside 'run' method. 
     */
    public static final int RELATIVE_LINE_NUMBER_WITH_exceptionMethod_CALL = 12;
    /* 
     * RELATIVE_LINE_NUMBER_WITH_loopMethod_CALL constant
     * must correspond to the line number with 'loopMethod' call 
     * inside 'run' method. 
     */
    public static final int RELATIVE_LINE_NUMBER_WITH_loopMethod_CALL = 23;
    
    public static final CB02Debuggee_ClassNotFoundException DUMMY_OBJECT = 
        new CB02Debuggee_ClassNotFoundException("Dummy_Class_Name");
    
    
    /* (non-Javadoc)
     * @see jpda.share.Debuggee#run()
     */
    public void run() {
        logWriter.println("CB02Debuggee: Start...");
        int exceptionMethodIteration = 0;
        int loopMethodIteration = 0;
        for (exceptionMethodIteration = 1; exceptionMethodIteration <= 10; exceptionMethodIteration++) {
            logWriter.print("CB02Debuggee: Call 'exceptionMethod': Iteration #" + exceptionMethodIteration);
            try {
                /* 
                 * The relative line (inside 'run' method) number with 
                 * 'exceptionMethod' call must correspond to
                 * RELATIVE_LINE_NUMBER_WITH_exceptionMethod_CALL constant.
                 */
                exceptionMethod("org.apache.harmony.test.func.jpda.jdwp.scenario.CB02.NonExistentClass");
            } catch (ClassNotFoundException e) {
                logWriter.print("CB02Debuggee: Dummy Exception is caught: '" + e.getMessage() + "'");
            }
        }
        for (loopMethodIteration = 1; loopMethodIteration <= 10; loopMethodIteration++) {
            /* 
             * The relative line (inside 'run' method) number with 
             * 'loopMethod' call must correspond to
             * RELATIVE_LINE_NUMBER_WITH_loopMethod_CALL constant.
             */
            loopMethod(loopMethodIteration);
        }
        CB02Debuggee_ClassNotFoundException dummy_Exception = DUMMY_OBJECT;
        logWriter.println("CB02Debuggee: Finishing...");
    }    
    
    private void exceptionMethod(String clazz) throws ClassNotFoundException {
        throw new CB02Debuggee_ClassNotFoundException("A dummy class '" + clazz + "' can't be loaded.");
    }
    
    
    private void loopMethod(int i) {
        logWriter.print("CB02Debuggee: A loop method has been called for the " + i + " time");
    }
    
    public static void main(String[] args) {
        runDebuggee(CB02Debuggee.class);
    }
}

class CB02Debuggee_ClassNotFoundException extends ClassNotFoundException {
    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public CB02Debuggee_ClassNotFoundException(String message) {
        super(message);
    }
}

