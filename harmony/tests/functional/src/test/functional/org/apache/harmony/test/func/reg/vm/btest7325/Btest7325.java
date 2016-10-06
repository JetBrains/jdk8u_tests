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
package org.apache.harmony.test.func.reg.vm.btest7325;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.HangTest;
/**
 * Attempt to read from finished process hangs
 *
 */
public class Btest7325 extends HangTest {

    public static void main(String[] args) {
        System.exit(new Btest7325().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        setTestName("org.apache.harmony.test.func.reg.vm.btest7325.Test7325");
        setTimeout(15000);
        setShouldCheckExitCode(true);
        setShouldContainCrashString(false);
        return run(getCommand(args) + " " + (args.length == 0 ? "java" : args[0]));
    }
}

class Test7325 {
    public static void main(String[] args) throws Exception {
        Process p = Runtime.getRuntime().exec(args[0]);
        System.out.println("Subprocess launched");
        p.waitFor();
        System.out.println("Subprocess finished");
        
        String out = read(p.getInputStream());
        System.out.println("out:\n" + out);
        String err = read(p.getErrorStream());
        System.out.println("err:\n" + err);
    }
    
    private static String read(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
        StringBuffer buffer= new StringBuffer();

        while (reader.ready()) {
          int result= reader.read();
          if (result >= 0) {
            buffer.append((char) result);
          }
        }
        return buffer.toString();
    }
}
