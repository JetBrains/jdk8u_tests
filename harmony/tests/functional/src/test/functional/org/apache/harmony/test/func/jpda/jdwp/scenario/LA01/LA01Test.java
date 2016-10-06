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
package org.apache.harmony.test.func.jpda.jdwp.scenario.LA01;

import org.apache.harmony.share.Result;
import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;

/**
 * Created on 18.04.2005
 */
public class LA01Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    public Result testLA01() {
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.resume();        
        return passed();
    }
    
    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {        
        return DEBUGGEE_CLASS_NAME;
    }

    public static void main(String[] args) {
        System.exit(new LA01Test().test(args));
    }
}
