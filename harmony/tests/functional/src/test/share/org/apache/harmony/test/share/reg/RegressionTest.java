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

package org.apache.harmony.test.share.reg;

import java.io.PrintStream;

public abstract class RegressionTest {
    
    protected static final int PASSED = 104;
    protected static final int FAILED = 105;
    protected static final int ERROR = 106;

    /* Stream for the tested process output (System.err by default) */
    protected PrintStream msgStream = System.err;

    protected int pass() {
        msgStream.println("The test PASSED");
        msgStream.println(
                "=====================================================\n");        
        return passed();
    }
    
    protected int fail() {
        msgStream.println("The test FAILED");        
        msgStream.println(
            "=====================================================\n");        
        return failed();
    }

    protected int error() {
        msgStream.println("ERROR was detected");
        msgStream.println(
            "=====================================================\n");        
        return getError();
    }
    
    protected static int passed() { return PASSED; }
    protected static int failed() { return FAILED; }
    protected static int getError() { return ERROR; }
}
