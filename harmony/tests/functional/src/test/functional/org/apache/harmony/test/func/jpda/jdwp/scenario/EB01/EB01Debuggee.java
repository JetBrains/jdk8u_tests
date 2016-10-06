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
package org.apache.harmony.test.func.jpda.jdwp.scenario.EB01;

import org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee;

/**
 * Created on 03.10.2006 
 */
public class EB01Debuggee extends QARawDebuggee {
    
    /* (non-Javadoc)
     * @see jpda.share.Debuggee#run()
     */
    public void run() {        
        logWriter.println("EB01Debuggee: Start...");

        logWriter.println("EB01Debuggee: Creating new object of 'EB01Debuggee_Tested_Class'...");
        EB01Debuggee_Tested_Class dummyObject = new EB01Debuggee_Tested_Class();

        logWriter.println("EB01Debuggee: Finishing...");
    }    
    
    public static void main(String[] args) {
        runDebuggee(EB01Debuggee.class);
    }
}

class EB01Debuggee_Tested_Class {
}
