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
package org.apache.harmony.test.func.reg.vm.btest4217;
import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest4217 extends RegressionTest {
    public static void main(String[] args) {
       System.exit(new Btest4217().test(Logger.global, args));
    }

    public int test(Logger logger, String [] args) {
        try {        
            new testC().test1();
            return fail();
        } catch (VerifyError e) {
            return pass();
        } catch (Throwable t) {
            return fail();
        }
   }
}

class testA {}

class testB extends testA {}

class testC {
    static testA a1;

    public void test1() {
        testA a2 = new testA();

        for (int i = 0; i < 10; i++) {
             a1 = a2;
             a2 = new testB();
        }
    }
}
