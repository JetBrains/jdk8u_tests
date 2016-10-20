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

package org.apache.harmony.test.stress.classloader.share.CorrectClasses;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import java.util.Random;
import java.io.File;

public class testCorrectClass extends TestCase {

    public testCorrectClass() {
        super();
        random = new Random();
    }

    private Random random;

    public static final int LOOPCNT = 10000;

    private static final char[] SEPARATOR = { File.separatorChar };

    private static final String TESTNN = "testCorrectClass_";

    private static final String TESTNN0 = "testCorrectClass_0";

    public static boolean logIn = true;

    public int test(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                System.setProperty("org.apache.harmony.test."
                        + "stress.classloader.share."
                        + "CorrectClasses.testCorrectClass.arg" + i, args[i]);
            }
        }
        test();
        return ReliabilityRunner.RESULT_PASS;
    }

    public void test() {
        final int ARGS_COUNT = 4;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.share."
                            + "CorrectClasses.testCorrectClass.arg"
                            + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - mode
        // testArgs[1] - number of iterations
        // testArgs[2] - number of used classes
        // testArgs[3] - classes location

        int mode = testArgs.length <= 0 ? 1 : Integer.valueOf(testArgs[0])
                .intValue();

        int cntLoop = testArgs.length <= 1 ? LOOPCNT : Integer.valueOf(
                testArgs[1]).intValue();

        int cntClss = testArgs.length <= 2 ? 0 : Integer.valueOf(testArgs[2])
                .intValue();
        if (mode != 1) {
            mode = cntClss;
        }
        String path = testArgs.length <= 3 ? null : testArgs[3];

        if (path == null) {
            ReliabilityRunner.debug("Class files location is not defined");
            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
        }
        String sep = new String(SEPARATOR);

        String pkgN = this.getClass().getPackage().getName();
        pkgN = pkgN.replace('.', SEPARATOR[0]);

        pkgN = path.concat(sep).concat(pkgN).concat(sep);
        String clName = TESTNN0;

        if (logIn) {
            ReliabilityRunner.debug("Try to load a lot of correct classes ");
            ReliabilityRunner.debug("Number of used classes: " + cntClss);
            ReliabilityRunner.debug("Number of iterations: " + cntLoop);
        }
        Class[] arrCls;
        // Object[] arrObj;
        newClassLoader[] cLoad;

        for (int i = 0; i < cntLoop; i++) {
            arrCls = new Class[cntClss];
            cLoad = new newClassLoader[mode];
            int nn = -1;
            try {
                if (mode == 1) {
                    cLoad[0] = new newClassLoader();
                    cLoad[0].setPath(pkgN);
                }
                for (int j = 0; j < cntClss; j++) {
                    nn = (mode == 1 ? 0 : j);
                    if (mode != 1) {
                        cLoad[nn] = new newClassLoader();
                        cLoad[nn].setPath(pkgN);
                    }

                    int randomInt = random.nextInt();
                    if (randomInt < 0) {
                        randomInt = 0 - randomInt;
                    }
                    String name = TESTNN.concat(Integer.toString(randomInt));
                    arrCls[j] = cLoad[nn].loadClass(clName, name);
                    if (arrCls[j] == null) {
                        ReliabilityRunner.debug("Null class. Step: " + j
                                + "  iteration:" + i);
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    int ll = arrCls[j].getDeclaredFields().length;
                    if (ll != 1) {
                        ReliabilityRunner.debug("Incorrect fields number " + ll
                                + " class " + arrCls[j] + " Step: " + j
                                + "  iteration:" + i);
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                }
            } catch (OutOfMemoryError e) {
            } catch (Throwable e) {
                ReliabilityRunner.debug(e.toString());
                ReliabilityRunner.mainTest.addError(this, e);
            }
            arrCls = null;
            cLoad = null;
        }
    }
}
