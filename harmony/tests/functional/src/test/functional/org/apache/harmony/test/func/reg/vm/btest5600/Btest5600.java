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
package org.apache.harmony.test.func.reg.vm.btest5600;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest5600 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest5600().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        String[] clNames = new String[args.length];
        System.arraycopy(args, 0, clNames, 0, args.length);
        int fails = 0;
        for (int t = 0; t < clNames.length; t++) {
            try {
                Class.forName(clNames[t]);
                System.err.println(clNames[t] + " - failed. ClassFormatError was not thrown");
                fails++;
            } catch (ClassFormatError e) {
                System.out.println(clNames[t] + " - passed");
            } catch (Throwable e) {
                System.err.println(clNames[t] + " - failed: unexpected error" + e);
                e.printStackTrace(System.err);
                fails++;
            }
        }
        if (fails == 0) {
            return pass();
        } else {
            return fail();
        }        
    }
}

