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
 * @version: $Revision: 1.7 $
 */
package org.apache.harmony.test.stress.classloader.OneThread.ManyClasses;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.CorrectClasses.testManyClasses_CA;

public class testManyClasses extends TestCase {

    public static final int CNTLOOP = 1000;

    public static final int CNTCLSS = 200;

    public static final String TESTNAME_C = "testManyClasses_C";

    public void test() {
        final int ARGS_COUNT = 3;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.OneThread."
                            + "ManyClasses.testManyClasses.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - correct classes package name
        // testArgs[1] - number of steps
        // testArgs[2] - number of used classes

        String pkgN = testArgs.length <= 0 ? this.getClass().getPackage()
                .getName() : testArgs[0];
        int lmtLoop = testArgs.length <= 1 ? CNTLOOP : (Integer
                .valueOf(testArgs[1])).intValue();
        int lmtClss = testArgs.length <= 2 ? CNTCLSS : (Integer
                .valueOf(testArgs[2])).intValue();

        Object[] arClss;
        int[] ar;
        int[] ar1;
        String lastStr = "";
        String name = "";

        ReliabilityRunner.debug("Start testManyClasses");
        ReliabilityRunner.debug("Number of used classes: " + lmtClss);
        ReliabilityRunner.debug("Number of iterations: " + lmtLoop);
        for (int t = 0; t < lmtLoop; t++) {
            arClss = new Object[lmtClss];
            ar = new int[lmtClss];
            ar1 = new int[lmtClss];

            for (int i = 0; i < lmtClss; i++) {
                try {
                    name = pkgN.concat(".").concat(TESTNAME_C).concat(
                            Integer.toString(i));
                    arClss[i] = Class.forName(name).newInstance();

                    if (((testManyClasses_CA) arClss[i]).PUBSTATFIN_F != 777) {
                        ReliabilityRunner.debug("Class: " + name);
                        ReliabilityRunner.debug("Test failed:  incorrect PUBSTATFIN_F="
                                + ((testManyClasses_CA) arClss[i]).PUBSTATFIN_F);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    if (((testManyClasses_CA) arClss[i]).getPUBSTAT_F() == -1) {
                        ReliabilityRunner.debug("Class: " + name);
                        ReliabilityRunner.debug("Test failed:  incorrect getPUBSTAT_F()="
                                + ((testManyClasses_CA) arClss[i])
                                        .getPUBSTAT_F());
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    ((testManyClasses_CA) arClss[i]).putStrF(name);

                    lastStr = ((testManyClasses_CA) arClss[i]).getStrF();
                    if (!lastStr.equals(name)) {
                        ReliabilityRunner.debug("Class: " + name);
                        ReliabilityRunner.debug("Test failed:  incorrect strF=" + lastStr
                                + " must be: " + lastStr);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    int tt = i % 10;
                    ar[i] = tt == 0 ? 10 : tt + 1;

                    if (!((testManyClasses_CA) arClss[i]).initArObj(ar[i])) {
                        ReliabilityRunner.debug("Class: " + name);
                        ReliabilityRunner.debug("Test failed:  incorrect initArObj()=" + ar[i]);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    ar1[i] = (ar[i] + 1) * (t + 1);

                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            for (int i = 0; i < lmtClss; i++) {
                name = arClss[i].toString();
                if (((testManyClasses_CA) arClss[i]).getCntArObj() != ar[i]) {
                    ReliabilityRunner.debug("Class: " + name);
                    ReliabilityRunner.debug("Test failed:  incorrect getCntArObj()="
                            + ((testManyClasses_CA) arClss[i]).getCntArObj()
                            + " must be: " + ar[i]);
                    ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                }
                if (((testManyClasses_CA) arClss[i]).getCntClss() != ar1[i]) {
                    ReliabilityRunner.debug("Class: " + name);
                    ReliabilityRunner.debug("Test failed:  incorrect getCntClss()="
                            + ((testManyClasses_CA) arClss[i]).getCntClss()
                            + " must be: " + ar1[i]);
                    ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                }
                String ss = ((testManyClasses_CA) arClss[i]).getStrF();
                if (!ss.equals(lastStr)) {
                    ReliabilityRunner.debug("Class: " + name);
                    ReliabilityRunner.debug("Test failed:  incorrect strF="
                            + ss + " must be: " + lastStr);
                    ReliabilityRunner.mainTest
                            .addError(this, new ClassLoaderTestError());
                }
            }
            arClss = null;
            ar1 = null;
            ar = null;
        }
    }
}
