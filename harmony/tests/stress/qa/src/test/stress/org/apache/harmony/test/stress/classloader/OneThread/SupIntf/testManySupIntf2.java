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

package org.apache.harmony.test.stress.classloader.OneThread.SupIntf;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.SupIntfClasses.*;

public class testManySupIntf2 extends TestCase {

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
                            + "SupIntf.testManySupIntf2.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        int cntLoop = testArgs.length <= 0 ? LOOPCNT : Integer.valueOf(
                testArgs[0]).intValue();

        ReliabilityRunner.debug("Use classes which implements interface with many superinterfaces");
        ReliabilityRunner.debug("Number of iterations: " + cntLoop);

        Object[] arObjs;
        Class[] arClss;

        for (int t = 0; t < cntLoop; t++) {
            if (t % 1000 == 0) {
                ReliabilityRunner.debug("Step: " + t + "...");
            }
            arClss = new Class[ARRSIZE];
            arObjs = new Object[ARRSIZE];
            String nm = "";
            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    nm = this.getClass().getPackage().getName().concat(".")
                            .concat("tt_cl").concat(Integer.toString(i % 30));
                    arClss[i] = Class.forName(nm);
                    arObjs[i] = arClss[i].newInstance();
                    testIntfLmt_C cl = (testIntfLmt_C) arObjs[i];

                    for (int l = 0; l < 100; l++) {
                        cl.mPut0(l);
                        if (cl.mGet0(l + 1) != (2 * l + 1)) {
                            ReliabilityRunner.debug("Incorrect mGet0(" + (l + 1) + "): "
                                    + cl.mGet0(l + 1) + " must be "
                                    + (2 * l + 1));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }

                        cl.mPut1(l);
                        if (cl.mGet1(l + 2) != (2 * l + 3)) {
                            ReliabilityRunner.debug("Incorrect mGet1(" + (l + 2) + "): "
                                    + cl.mGet1(l + 2) + " must be "
                                    + (2 * l + 3));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }

                        cl.mPut2(l);
                        if (cl.mGet2(l + 3) != (2 * l + 5)) {
                            ReliabilityRunner.debug("Incorrect mGet2(" + (l + 3) + "): "
                                    + cl.mGet2(l + 3) + " must be "
                                    + (2 * l + 5));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                        cl.mPut3(l);
                        if (cl.mGet3(l + 4) != (2 * l + 7)) {
                            ReliabilityRunner.debug("Incorrect mGet3(" + (l + 4) + "): "
                                    + cl.mGet3(l + 4) + " must be "
                                    + (2 * l + 7));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                        cl.mPut4(l);
                        if (cl.mGet4(l + 5) != (2 * l + 9)) {
                            ReliabilityRunner.debug("Incorrect mGet4(" + (l + 5) + "): "
                                    + cl.mGet4(l + 5) + " must be "
                                    + (2 * l + 9));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }

                        cl.mPut5(l);
                        if (cl.mGet5(l + 6) != (2 * l + 11)) {
                            ReliabilityRunner.debug("Incorrect mGet5(" + (l + 6) + "): "
                                    + cl.mGet5(l + 6) + " must be "
                                    + (2 * l + 11));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                        cl.mPut6(l);
                        if (cl.mGet6(l + 7) != (2 * l + 13)) {
                            ReliabilityRunner.debug("Incorrect mGet6(" + (l + 7) + "): "
                                    + cl.mGet6(l + 7) + " must be "
                                    + (2 * l + 13));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                        cl.mPut7(l);
                        if (cl.mGet7(l + 8) != (2 * l + 15)) {
                            ReliabilityRunner.debug("Incorrect mGet7(" + (l + 8) + "): "
                                    + cl.mGet7(l + 8) + " must be "
                                    + (2 * l + 15));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                        cl.mPut8(l);
                        if (cl.mGet8(l + 9) != (2 * l + 17)) {
                            ReliabilityRunner.debug("Incorrect mGet8(" + (l + 9) + "): "
                                    + cl.mGet8(l + 9) + " must be "
                                    + (2 * l + 17));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                        cl.mPut9(l);
                        if (cl.mGet9(l + 10) != (2 * l + 19)) {
                            ReliabilityRunner.debug("Incorrect mGet9(" + (l + 10) + "): "
                                    + cl.mGet9(l + 10) + " must be "
                                    + (2 * l + 19));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                        cl.mPut10(l * 10);
                        if (cl.mGet10(l + 11) != (11 * l + 21)) {
                            ReliabilityRunner.debug("Incorrect mGet10(" + (l + 11) + "): "
                                    + cl.mGet10(l + 11) + " must be "
                                    + (11 * l + 21));
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                    }
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            arClss = null;
            arObjs = null;
        }
    }
}

class tt_cl0 extends testIntfLmt_C {
}

class tt_cl1 extends testIntfLmt_C {
}

class tt_cl2 extends testIntfLmt_C {
}

class tt_cl3 extends testIntfLmt_C {
}

class tt_cl4 extends testIntfLmt_C {
}

class tt_cl5 extends testIntfLmt_C {
}

class tt_cl6 extends testIntfLmt_C {
}

class tt_cl7 extends testIntfLmt_C {
}

class tt_cl8 extends testIntfLmt_C {
}

class tt_cl9 extends testIntfLmt_C {
}

class tt_cl10 extends testIntfLmt_C {
}

class tt_cl11 extends testIntfLmt_C {
}

class tt_cl12 extends testIntfLmt_C {
}

class tt_cl13 extends testIntfLmt_C {
}

class tt_cl14 extends testIntfLmt_C {
}

class tt_cl15 extends testIntfLmt_C {
}

class tt_cl16 extends testIntfLmt_C {
}

class tt_cl17 extends testIntfLmt_C {
}

class tt_cl18 extends testIntfLmt_C {
}

class tt_cl19 extends testIntfLmt_C {
}

class tt_cl20 extends testIntfLmt_C {
}

class tt_cl21 extends testIntfLmt_C {
}

class tt_cl22 extends testIntfLmt_C {
}

class tt_cl23 extends testIntfLmt_C {
}

class tt_cl24 extends testIntfLmt_C {
}

class tt_cl25 extends testIntfLmt_C {
}

class tt_cl26 extends testIntfLmt_C {
}

class tt_cl27 extends testIntfLmt_C {
}

class tt_cl28 extends testIntfLmt_C {
}

class tt_cl29 extends testIntfLmt_C {
}
