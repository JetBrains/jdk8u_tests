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
package org.apache.harmony.test.func.jpda.jdwp.scenario.ST07;

import java.awt.image.VolatileImage;

import org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee;

/**
 * Created on 19.04.2005 
 */
public class ST07Debuggee extends QARawDebuggee {
    TestClass1 cls = new TestClass1();
    /* (non-Javadoc)
     * @see jpda.share.Debuggee#run()
     */
    public void run() {    
        logWriter.println("=> First message from not filtered class");
        cls.testMethod();
        cls.testMethod();
        logWriter.println("=> Last message from not filtered class");
        //wrap_up();
    }    
        
    private void wrap_up() {
        logWriter.println("Finished");
    }
      
    public static void main(String[] args) {
        runDebuggee(ST07Debuggee.class);
    }
    
    class TestClass1 {
                
        public void testMethod() {
            logWriter.println("=> Message1: steps on this lines should be filtered");
            logWriter.println("=> Message2: steps on this lines should be filtered");
            logWriter.println("=> Message3: steps on this lines should be filtered");
            logWriter.println("=> Message4: steps on this lines should be filtered");
            logWriter.println("=> Message5: steps on this lines should be filtered");
        }
    }
      
}

