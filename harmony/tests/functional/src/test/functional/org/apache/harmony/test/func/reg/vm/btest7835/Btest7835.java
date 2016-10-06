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
package org.apache.harmony.test.func.reg.vm.btest7835;

import org.apache.harmony.test.share.reg.RegressionTest;
import java.util.logging.Logger;

public class Btest7835 extends RegressionTest {
    boolean failed = false;
    public static void main(String[] args) {
        System.exit(new Btest7835().test(Logger.global, args));
    }
    
    public int test(final Logger logger, String[] args)
    {
        Thread mt = new Thread() {
            public void run() {
                try {
                    Class.forName("org.apache.harmony.test.func.reg.vm.btest7835.SubClass");
                } catch(ExceptionInInitializerError e) {
                } catch(Throwable e) {
                    failed = true;
                }
            }
        };
        try {
            mt.start();
            mt.join();
            if(failed) return fail();
            Class.forName("org.apache.harmony.test.func.reg.vm.btest7835.SubClass");
        } catch(NoClassDefFoundError e) {
            return pass();
        } catch(Throwable e) {
            logger.severe(e.toString());
        }
        return fail();
    }
}

class SubClass extends SuperClass {
}

class SuperClass {
    static {
        int a = 1;
        int b = 0;
        int c = a/b;
    }
}
