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

import java.io.IOException; 
import java.io.PrintStream;

public class NegativeCrashTest extends CrashTest {

    /* Default crash string */
    protected String crashString = "PASSED!";
    
    protected boolean containCrashString = true;

    protected boolean checkExitCode = false;
    protected boolean checkResult = false;
    protected boolean checkCrashString = true;

    public static void main(String[] args) {
        if (args.length > 0) {
            NegativeCrashTest tst = new NegativeCrashTest();
            System.exit(tst.run(tst.getCommand(args)));        } 
    }
    
    public NegativeCrashTest() {
        this(System.err);
    }

    public NegativeCrashTest(PrintStream stream) {
        super(stream);
    }
    
    public int run(String arg) {
        if (arg.equals("")) {
            msgStream.println(
                    "The test is skipped because of the parameters conflict.");
            return pass();
        };
        
        try {
            Process process = startTest(arg);
            int ret = process.waitFor();

            synchronized (this) {
                wait(100);
            }
            
            msgStream.println("Process exit code is " + ret + ".");
            return isCrashOK(process, ret);

        } catch (Exception e) {
            msgStream.println("Unexpected Exception was thrown:");
            e.printStackTrace(msgStream);
            return error();
        }
    }    

    protected int isCrashOK(Process process, int exitCode) {
        return (isOutputOK(process)) ? isExitCodeOK(exitCode) : fail();
    }

    int isExitCodeOK(int exitCode) {
        if (checkExitCode) {
            return exitCode == 0 ? pass() : fail();
        } else if (checkResult) {
            return (exitCode == PASSED ? pass() :
                (exitCode == FAILED ? fail() : error()));
        } else {
            return pass();
        }        
    }
    
    protected boolean isOutputOK(Process process) {
        byte [] errorOutput = null;
        msgStream.println(
        "-------------------- System.err: --------------------");
        try {
            int available = iErrStream.available();
            errorOutput = new byte [available];
            iErrStream.read(errorOutput, 0, available);
            msgStream.write(errorOutput, 0, available);    
            msgStream.println(); 
        } catch (IOException e) {
            msgStream.println ("IOException found during stream reading:");
            e.printStackTrace(msgStream);
        }
        msgStream.println(
        "-------------------- System.out: --------------------");
        redirectStream(iOutStream, msgStream);
        msgStream.println(
        "-----------------------------------------------------\n");

        if (checkCrashString) {
            String crashStr = new String(errorOutput);
            return containCrashString 
                    ? (crashStr.indexOf(crashString) >= 0) 
                    : (crashStr.indexOf(crashString) < 0) ;
        } else {
            return true;
        }    
    }
    
    protected boolean setOption(String option, boolean displayWarning) {
        if (! super.setOption(option, false)) { 
            try {
                if (option.startsWith("~crashString=")) {
                    crashString = option.substring(13);
                    return true;
                } else if (option.startsWith("~containCrashString")) {
                    containCrashString = true;
                    return true;
                } else if (option.startsWith("~notContainCrashString")) {
                    containCrashString = false;
                    return true;
                } else if (option.startsWith("~checkExitCode")) {
                    checkExitCode = true;
                    return true;
                } else if (option.startsWith("~doNotCheckExitCode")) {
                    checkExitCode = false;
                    return true;
                } else if (option.startsWith("~checkRes")) {
                    checkResult = true;
                    return true;
                } else if (option.startsWith("~doNotCheckRes")) {
                    checkResult = false;
                    return true;
                } else if (option.startsWith("~checkCrashString")) {
                    checkCrashString = true;
                    return true;
                } else if (option.startsWith("~doNotCheckCrashString")) {
                    checkCrashString = false;
                    return true;
                } else if (displayWarning) {
                    msgStream.println(
                            "WARNING! " + option + " is unknown option!");
                }
            } catch (IndexOutOfBoundsException e) {
                msgStream.println("Unexpected exception: ");
                e.printStackTrace(msgStream);
            }
            return false;
        } else {
            return true;
        }
    }

    protected void setCrashString(String str) {
        crashString = str;
    }
    
    protected void  setShouldContainCrashString(boolean val) {
        containCrashString = val;
    }

    protected void  setShouldCheckExitCode(boolean val) {
        checkExitCode = val;
    }
}
