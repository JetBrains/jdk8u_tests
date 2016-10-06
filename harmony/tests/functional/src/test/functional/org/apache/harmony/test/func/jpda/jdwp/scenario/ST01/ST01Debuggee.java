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
package org.apache.harmony.test.func.jpda.jdwp.scenario.ST01;

import org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee;

/**
 * Created on 19.09.2006 
 */
public class ST01Debuggee extends QARawDebuggee {
        
    int j = 0;
    int k = 4;
    
    public ST01Debuggee() {
       
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    protected void finalize() throws Throwable {
       super.finalize();
    }
    
    
    public void run() {        
        logWriter.print("Before 'stepMethod'");
        stepMethod();
        k = 2;
        j = j * k;
        stepMethod();
        logWriter.println("j = " + j);
        wrap_up();
    }    
    
    private void stepMethod() {
        j = j + 9;
        logWriter.print("A method 'stepMethod' has been called, j = " + j);
    }
    
    
    
    private void wrap_up() {
        logWriter.println("Finished");
    }
    
    public static void main(String[] args) {
        int i = 5;
        runDebuggee(ST01Debuggee.class);
    }
}

