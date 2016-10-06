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
package org.apache.harmony.test.func.reg.vm.btest5590;

import org.apache.harmony.test.share.reg.RegressionTest;
import java.util.logging.Logger;
import java.io.*;

/**
 */

public class Btest5590 extends RegressionTest {

    private static final int MAX_BUFFER_SIZE = 400000;

    private static final String START = "--------- begin native method execution counts (total and slow-path):";
    private static final String END = "--------- end method execution counts:";
    private static final String STATS_STRING = "org/apache/harmony/test/func/reg/vm/btest5590/Btest5590.nativeMethod()V";

    public int test(Logger logger, String[] args) {
        String classPath = System.getProperty("java.class.path");
        String cmd = System.getProperty("java.home") + File.separator + "bin" + File.separator;
        if (new File(cmd + "java.exe").exists()) {
            cmd = cmd + "java.exe";
        } else if (new File(cmd + "java").exists()) {
            cmd = cmd + "java";
        } else if (new File(cmd + "ij.exe").exists()) {
            cmd = cmd + "ij.exe";
        } else {
            cmd = cmd + "ij";
        }
        String cmdLine = cmd + " -version";
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmdLine);
        } catch(IOException e) {
            return error();
        }
        InputStream os = p.getErrorStream();

        int c;
        int i =0;
        byte[] osString = new byte[MAX_BUFFER_SIZE];
        try {
            for (; (c = os.read()) != -1; i++) {
                osString[i] = (byte)c;
            }
        } catch(IOException e) {
            return error();
        }
        String version = new String(osString, 0, i);
        logger.info("Version info: " + version);
        if (version.indexOf("debug") == -1) {
            logger.info("-Xstats option not implemented in release mode, returing PASS");
            return pass();
        }

        cmdLine = cmd + " -cp " + classPath + " -XcleanupOnExit -Xstats:3 org.apache.harmony.test.func.reg.vm.btest5590.Btest5590 -test";
        logger.info(cmdLine);        
        try {
            p = Runtime.getRuntime().exec(cmdLine);
            os = p.getInputStream();

            while ( (c = os.read()) != -1 ) {
                osString[i++] = (byte)c;
            }            
            String[] lines = new String(osString, 0, i).split("(\\r)?(\\n)");
            int startIndex = -1;
            int endIndex = -1;
            for (i = 0; i < lines.length; i++) {
                if (lines[i].equals(START)) {
                    startIndex = i;
                } else if (lines[i].equals(END)) {
                    endIndex = i;
                }
            }
            if (startIndex == -1 || endIndex == -1) {
                logger.info("Starting and/or ending lines not found");
                return fail();
            } else {
                logger.info("Start at " + startIndex + ", end at " + endIndex);
            }
            startIndex++;
            endIndex--;
            if (startIndex > endIndex) {
                logger.info("Methods stats are empty");
                return fail();
            }
            for (i = startIndex; i <= endIndex; i++) {
                if (lines[i].indexOf(STATS_STRING) != -1) {
                    // Found method stats string
                    return pass();
                }
            }            
            logger.info("Method stats string not found");
            return fail();
        } catch (Throwable t) {
            logger.info("Cannot execute a subprocess");
            t.printStackTrace();
            return error();
        }
    }
    
    public static void main(String[] args) {
        if (args.length == 1 && "-test".equals(args[0])) {
            // The VM is executed in a subprocess created in test() method
            System.loadLibrary("Btest5590");
            nativeMethod();
            System.exit(0);
        } else {
            // Main VM is executed - invoke test()
            System.exit(new Btest5590().test(Logger.global, args));
        }
    }

    private static native void nativeMethod();
}
