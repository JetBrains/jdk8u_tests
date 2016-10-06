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
package org.apache.harmony.test.func.reg.vm.btest9660;

import org.apache.harmony.test.share.reg.RegressionTest;
import java.util.logging.Logger;

public class Btest9660 extends RegressionTest {
    public int test(Logger logger, String[] args) {
        try {
            new InitError();
            System.out.println("Expected error wasn't thrown");
            return fail();
        } catch (VerifyError e ) {
            System.out.println("Expected error was thrown: "+e);
            return pass();
        } catch (Throwable e ) {
            System.out.println("Unexpected error was thrown");
            e.printStackTrace(System.out);
            return fail();
        }
    }

    // to run test from console
    public static void main(String[] args) {
        System.exit(new Btest9660().test(Logger.global, args));
    }
}

