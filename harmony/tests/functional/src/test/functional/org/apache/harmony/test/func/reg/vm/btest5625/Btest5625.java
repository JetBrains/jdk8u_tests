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
package org.apache.harmony.test.func.reg.vm.btest5625;

import java.util.logging.Logger; 
import org.apache.harmony.test.share.reg.RegressionTest;
import java.io.InputStream;

public class Btest5625 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest5625().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        //Xstats option exists only for debug version of VM.
        if (getVersion(getVersionCommand(args)).indexOf("debug") != -1) {
            return run(getCommand(args));
        } else {
            return pass();
        }
    }


    public String getVersion(String arg) {
        try {
            int b = 1;
            String str = "";
            Process process = Runtime.getRuntime().exec(arg);
            InputStream es = process.getErrorStream();
            while ((b != 10) && (b != 13)) {
                b = es.read();
                str = str + (char)b;
            }
            int ret = process.waitFor();
            synchronized (this) {
                wait(200);
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }    

    public int run(String arg) {
        try {
            Process process = startTest(arg);
            int ret = process.waitFor();    
            synchronized (this) {
                wait(200);
            }
            return ret == 0 ? pass() : fail();
        } catch (Exception e) {
            return error();
        }
    }    
    
    protected Process startTest(String arg) throws Exception {
        Process process = Runtime.getRuntime().exec(arg);
        return process;
    }

    protected String getCommand(String [] args) {
        String ret = "";
        for (int i = 0; i < args.length; i++) {
            ret = ret + args[i] + " ";
        }
        ret = ret + "-classpath " + System.getProperty("java.class.path") + " ";
        return ret + "-Xstats 1 org.apache.harmony.test.func.reg.vm.btest5625.Hi";
    }

    protected String getVersionCommand(String [] args) {
        String ret = "";
        for (int i = 0; i < args.length; i++) {
            ret = ret + args[i] + " ";
        }
        return ret + "-version";
    }

}
