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
package org.apache.harmony.test.func.reg.vm.btest5030;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;
import org.apache.harmony.test.share.reg.StreamRedirector;

public class Btest5030 extends RegressionTest {
    public static void main(String[] args) {
        System.exit(new Btest5030().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        StreamRedirector out;
        StreamRedirector err;
        try {
            System.out.println("Checking no args...");
            Process p = Runtime.getRuntime().exec(args[0]);
            out = new StreamRedirector(p.getInputStream(), System.out);
            err = new StreamRedirector(p.getErrorStream(), System.err);
            out.start();
            err.run();
            p.waitFor();
            Thread.sleep(1000);
            if(p.exitValue() != 1) return fail();

            System.out.println("Checking invalid arg...");
            String[] args_new = new String[2];
            args_new[0] = args[0];
            args_new[1] = args[1];
            p = Runtime.getRuntime().exec(args_new);
            out = new StreamRedirector(p.getInputStream(), System.out);
            err = new StreamRedirector(p.getErrorStream(), System.err);
            out.start();
            err.run();
            p.waitFor();
            if(p.exitValue() != 1) return fail();
            return pass();
        } catch(Throwable th) {
            th.printStackTrace();
            return fail();
        }
    }
}
