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
package org.apache.harmony.test.func.reg.vm.btest7140;

//import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.CrashTest;
import org.apache.harmony.test.share.reg.StreamRedirector;

/**
 * VM crashes on Runtime.exec()
 * (due to environment buffer overrun)
 *
 */
public class Btest7140 extends CrashTest {

    public static void main(String[] args) {
        System.exit(new Btest7140().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        setTestName("org.apache.harmony.test.func.reg.vm.btest7140.Test7140");
        return run(getCommand(args) 
                     + " " +(args.length == 0 ? "java" : args[0])
                     + " NonExistingClass" );
    }
}

class Test7140 {

    // runs in TestedRuntime
    public static void main(String[] args) throws Exception {
        
        Map realEnv = System.getenv();
        final int SIZE = 50 + realEnv.size();
        String[] env = new String[SIZE];
        int i = 0;
        for (Iterator it =realEnv.keySet().iterator();it.hasNext(); i++)
        {
            String key = (String)it.next();
            env[i] = key + "=" + (String)realEnv.get(key);
        }
        for (;i<SIZE; i++) {
            env[i] = i + "=" + "12345678901234567890123456789012345678901234567890";
            env[i] += "12345678901234567890123456789012345678901234567890";
            env[i] += "12345678901234567890123456789012345678901234567890";
        }

        System.out.println("-- starting child process - " + args[0] + " " + args[1]);
        Process p = Runtime.getRuntime().exec( args, env, new File("."));

        StreamRedirector err = new StreamRedirector(p.getErrorStream(), System.err);
        StreamRedirector out = new StreamRedirector(p.getInputStream(), System.out);
        err.start();
        out.run();

        int exitCode = p.waitFor();
        System.out.println("-- exitCode: " + exitCode);
    }

}
