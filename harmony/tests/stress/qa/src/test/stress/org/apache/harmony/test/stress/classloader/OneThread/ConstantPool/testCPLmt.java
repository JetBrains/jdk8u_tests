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
 * @author: Vera Y.Petrashkova
 * @version: $revision$
 */

package org.apache.harmony.test.stress.classloader.OneThread.ConstantPool;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.ConstantPoolClasses.testCPLmt_C;

public class testCPLmt extends TestCase {
    public static final int ARRSIZE = 2000;

    public static final int LOOPCNT = 10000;

    public void test() {
        final int ARGS_COUNT = 1;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.OneThread."
                            + "ConstantPool.testCPLmt.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // boolean pass = true;
        ReliabilityRunner.debug("Use class with large constant pool ");

        int cntLoop = testArgs.length <= 0 ? LOOPCNT : Integer.valueOf(
                testArgs[0]).intValue();

        testCPLmt_C[] ar;
        testCPLmt_C[] ar1;

        for (int t = 0; t < cntLoop; t++) {
            if (t % 1000 == 0) {
                ReliabilityRunner.debug("Step: " + t + "...");
            }
            ar = new testCPLmt_C[ARRSIZE];
            ar1 = new testCPLmt_C[ARRSIZE];
            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    ar[i] = new testCPLmt_C();
                    ar1[i] = new testCPLmt_C();

                    Class cl = Class.forName(this.getClass().getPackage()
                            .getName().concat(".").concat("fClass").concat(
                                    Integer.toString(i % 30)));
                    ar1[i] = (testCPLmt_C) cl.newInstance();

                    ar[i].testF0 = i;
                    ar1[i].testF0 = i;
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            for (int i = 0; i < ARRSIZE; i++) {
                if (ar[i].testF0 != ar1[i].testF0) {
                    ReliabilityRunner.debug("Test failed: incorrect result "
                            + " class " + ar[i].getClass().getName()
                            + " testF0 = " + ar[i].testF0 + " class "
                            + ar1[i].getClass().getName() + " testF0 = "
                            + ar1[i].testF0);
                    ReliabilityRunner.mainTest
                            .addError(this, new ClassLoaderTestError());
                }
            }

            ar = null;
            ar1 = null;
        }
    }
}
