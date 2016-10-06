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

public class HangTest extends NegativeCrashTest {

    /* Test timeout in millyseconds */
    protected int timeout=45000;
    
    protected int hangTimeoutFactor = 700;
    
    public static void main(String[] args) {
        if (args.length > 0) {
            HangTest tst = new HangTest();
            System.exit(tst.run(tst.getCommand(args)));        } 
    }

    public HangTest() {
        this(System.err);
    }

    public HangTest(PrintStream stream) {
        super(stream);
    }    
    
    /* 
     * Run the test! 
     */
    public int run(String arg) {
        if (arg.equals("")) {
            msgStream.println(
                    "The test is skipped because of the parameters conflict.");
            return pass();
        };

        msgStream.println("Test timeout is " + timeout + ".");

        try {
            /* Start the test process and wait for timeout */
            Process process = startTest(arg);
            
            synchronized (this) {
                wait(timeout);
            }

            /* Analize the test process output and/or exit code*/
            boolean crashOK = isOutputOK(process);
            
            try {
                int ret = process.exitValue();                                
                msgStream.println("Process exit code is " + ret);
                return crashOK ? isExitCodeOK(ret) : fail(); 
            } catch (IllegalThreadStateException e) {
                process.destroy();
                msgStream.println("Process hangs!");
                if (checkExitCode ||checkResult) {
                    return fail();
                } else {
                    return crashOK ? pass() : fail();
                }            
            }        
        } catch (Exception e) {
            msgStream.println("Unexpected Exception was thrown:");
            e.printStackTrace(msgStream);
            return error();
        }
    }    

    protected boolean setOption(String option, boolean displayWarning) {
        if (! super.setOption(option, false)) { 
            if (option.startsWith("~timeout=")) {
                timeout = Integer.parseInt(option.substring(9)) * hangTimeoutFactor;
                return true;
            } else if (displayWarning) {
                msgStream.println(
                        "WARNING! " + option + " is unknown option!");
            }
        } else {
            return true;
        }
        return false;
    }

    protected void setTimeout(int val) {
        timeout = val;
    }

}
