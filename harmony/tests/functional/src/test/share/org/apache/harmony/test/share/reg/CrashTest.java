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
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class CrashTest extends RegressionTest {

    /* Default test name */
    protected String testName = "";
    
    /* Should we add testName to command string or it is always included? */
    protected boolean needTestName = true;
    
    /* Stream objects for standard input and output streams re-direction.*/
    protected PipedInputStream iErrStream;
    protected PipedInputStream iOutStream;
    protected PipedOutputStream errStream;
    protected PipedOutputStream outStream;

    /* --------------------------------------------------------------------- */
    
    /*
     * Test constructor. Creates stream objects for standard input and output 
     * stream re-direction.
     */
    public CrashTest(PrintStream stream) {
        msgStream = stream;
        
        iErrStream = new PipedInputStream();
        iOutStream = new PipedInputStream();

        try {
            errStream = new PipedOutputStream(iErrStream);
            outStream = new PipedOutputStream(iOutStream);
        } catch (IOException e) {
            msgStream.println("Unexpected exception is thrown:");
            e.printStackTrace(msgStream);
        }
    }

    public CrashTest() {
        this(System.err);
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            CrashTest tst = new CrashTest();
            System.exit(tst.run(tst.getCommand(args)));
        } 
    }

    /*
     * Run the test!
     * If arg is an empty string, we just skip the test.
     */
    public int run(String arg) {
        if (arg.equals("")) {
            msgStream.println(
                    "The test is skipped because of the parameters conflict.");
            return pass();
        };
        
        try {
            /* Start the test in separate thread and wail until it is 
               finished */
            Process process = startTest(arg);
            int ret = process.waitFor();    
            
            /* Wait some time and then re-direct test standard input and output 
               streams */
            synchronized (this) {
                wait(100);
            }
            
            msgStream.println("Process exit code is " + ret + ".");
            msgStream.println(
                    "-------------------- System.err: --------------------");
            redirectStream(iErrStream, msgStream);
            msgStream.println(
                    "-------------------- System.out: --------------------");
            redirectStream(iOutStream, msgStream);
            msgStream.println(
                    "-----------------------------------------------------");

            /* analize test process exit code */
            if (ret != 0) {
                msgStream.println("Process exit code is not successfull: " 
                        + ret + " (must be 0).");
            }
            return ret == 0 ? pass() : fail();
        } catch (Exception e) {
            msgStream.println("Unexpected Exception was thrown:");
            e.printStackTrace(msgStream);
            return error();
        }
    }    
    
    /* 
     * Call System.exec(...) for the test process and start streams for the 
     * test standard input and output streams re-direction 
     */
    protected Process startTest(String arg) throws Exception {
        msgStream.println("Execute command:\n" + arg + "\n");
        Process process = Runtime.getRuntime().exec(arg);
        
        StreamRedirector errRedirect = 
                new StreamRedirector(process.getErrorStream(), errStream);
        StreamRedirector outRedirect = 
            new StreamRedirector(process.getInputStream(), outStream);
        errRedirect.start();
        outRedirect.start();    
        return process;
    }
    
    /*
     * Re-direct one stream to another 
     */
    protected void redirectStream(InputStream stream1, PrintStream stream2) {
        try {
            int available = stream1.available();
            byte [] out = new byte [available];
            stream1.read(out, 0, available);
            stream2.write(out,0,available);
            stream2.println();
        } catch (IOException e) {
            msgStream.println ("IOException found during stream reading:");
            e.printStackTrace(msgStream);
        }
    }
    
    /*
     * Organize command line for the test process
     */
    protected String getCommand(String [] args) {
        if (args.length == 0) {
            msgStream.println("WARNING: Test command is not set!");
        }
    
        /*
         * result = "<runtime path> -cp <classpath>"
         * Please, note: working directory for tests is not the root TH 
         * directory; workdir is TH temporary directory instead. So, if we want
         * to use relative path to the tested java, we need to take in 
         * attention this fact.
         */
        String sep = System.getProperty("file.separator");
        String ret = (args.length == 0) ? "java" : args[0];
        ret += " -classpath " + getClassPath();

        /*
         * result = "<runtime path> -cp <classpath> <Jitrino switches>"
         * Process input arguments and combine used JIT switches if necessary 
         */
        int  i;
        for (i = 1; i < args.length; i++) {
            if ((args[i] != null) && (args[i].startsWith("~"))) {
                setOption(args[i], true);
            } else {
                break;
            }
        }
        
        /* Add any arguments but ~-parameters */
        for ( ; i < args.length; i++) {
            ret += " " + args[i];
        }
        
        /* Add test name at the end of command line if this is necessary */
        if (needTestName) {
            ret += " " + testName;
        }
        
        return ret;
    }
    
    /* 
     * Returns current classpath 
     */
    protected String getClassPath() {
        return System.getProperty("java.class.path");
    }

    /* 
     * ~-parameters analize
     */
    protected boolean setOption(String option, boolean displayWarning) {
        try {
            if (option.equals("~command")) {
                needTestName = false;
                return true;
            } else if (option.startsWith("~testName=")) {
                testName = option.substring(10);
                needTestName = true;
                return true;
            } else if (displayWarning) {
                msgStream.println("WARNING! " + option + " is unknown option!");
            }
        } catch (IndexOutOfBoundsException e) {
            msgStream.println("Unexpected exception: " + e);
        }
        
        return false;
    }

    protected boolean setOption(String option) {
        return setOption(option, true);
    }
    
    protected void setTestName(String name) {
        testName = name;
    }

}
