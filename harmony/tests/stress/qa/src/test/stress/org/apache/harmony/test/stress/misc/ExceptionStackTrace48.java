/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

/**
 * @author Alexander D. Shipilov
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.misc.MiscTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class ExceptionStackTrace48 extends TestCase {

    public void test() {
        int STRESS_LOAD = 10;
        StackTraceElement ste[];
        String methodName;

        STRESS_LOAD = Integer.getInteger(
                "org.apache.harmony.test." + "stress.misc."
                        + "ExceptionStackTrace48.STRESS_LOAD", 10).intValue();

        ExceptionStackTrace48 est = new ExceptionStackTrace48();

        for (int i = 0; i < STRESS_LOAD; i++) {
            try {
                est.est1();
            } catch (Exception ex) {
                ste = ex.getStackTrace();
                int k = 0;
                for (int j = ste.length - 1; j > 1; j--) {
                    methodName = "est" + Integer.toString(j);
                    if (!methodName.equals(ste[k].getMethodName())) {
                        ReliabilityRunner
                                .debug("Wrong method name. Instead of ");
                        ReliabilityRunner.debug(methodName);
                        ReliabilityRunner.debug(ste[k].getMethodName());
                        ReliabilityRunner.debug("Test fail");
                        ReliabilityRunner.mainTest.addError(this,
                                new MiscTestError());
                    }
                    k++;
                }
                if (!ste[ste.length - 1].getMethodName().equals("main")) {
                    ReliabilityRunner
                            .debug("Wrong method name. Instead of main");
                    ReliabilityRunner
                            .debug(ste[ste.length - 1].getMethodName());
                    ReliabilityRunner.debug("Test fail");
                    ReliabilityRunner.mainTest
                            .addError(this, new MiscTestError());
                }
            }
        }
    }

    void est20() throws Exception {
        throw new Exception();
    }

    void est1() throws Exception {
        est2();
    }

    void est2() throws Exception {
        est3();
    }

    void est3() throws Exception {
        est4();
    }

    void est4() throws Exception {
        est5();
    }

    void est5() throws Exception {
        est6();
    }

    void est6() throws Exception {
        est7();
    }

    void est7() throws Exception {
        est8();
    }

    void est8() throws Exception {
        est9();
    }

    void est9() throws Exception {
        est10();
    }

    void est10() throws Exception {
        est11();
    }

    void est11() throws Exception {
        est12();
    }

    void est12() throws Exception {
        est13();
    }

    void est13() throws Exception {
        est14();
    }

    void est14() throws Exception {
        est15();
    }

    void est15() throws Exception {
        est16();
    }

    void est16() throws Exception {
        est17();
    }

    void est17() throws Exception {
        est18();
    }

    void est18() throws Exception {
        est19();
    }

    void est19() throws Exception {
        est20();
    }
}
