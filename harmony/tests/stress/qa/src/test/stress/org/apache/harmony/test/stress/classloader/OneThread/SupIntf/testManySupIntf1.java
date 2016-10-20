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
import org.apache.harmony.test.stress.classloader.share.SupIntfClasses.testManySupIntf_C;
import java.lang.reflect.Method;

public class testManySupIntf1 extends TestCase {
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
                            + "SupIntf.testManySupIntf1.arg" + testArgsCount);
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

        ReliabilityRunner
                .debug("Use classes which implements interface with many superinterfaces");
        ReliabilityRunner.debug("Number of iterations: " + cntLoop);

        Object[] arObjs;
        Class[] arClss;
        Method m;

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
                            .concat("t_cl").concat(Integer.toString(i % 30));
                    arClss[i] = Class.forName(nm);
                    arObjs[i] = arClss[i].newInstance();
                    testManySupIntf_C cl = (testManySupIntf_C) arObjs[i];
                    cl.put(777);
                    if (cl.get() != cl.PUBSTATFIN_F) {
                        ReliabilityRunner.debug("Incorrect get(): " + cl.get()
                                + " must be " + cl.PUBSTATFIN_F);
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }

                    cl.put0(0);
                    if (cl.get0() != cl.PUBSTATFIN_F) {
                        ReliabilityRunner.debug("Incorrect get0(): "
                                + cl.get0() + "  must be " + cl.PUBSTATFIN_F);
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    cl.put1(0);
                    if (cl.get1() != 100) {
                        ReliabilityRunner.debug("Incorrect get1(): "
                                + cl.get1() + " must be 100");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    cl.put2(0);
                    if (cl.get2() != 200) {
                        ReliabilityRunner.debug("Incorrect get2(): "
                                + cl.get2() + " must be 200");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    cl.put3(0);
                    if (cl.get3() != 300) {
                        ReliabilityRunner.debug("Incorrect get3(): "
                                + cl.get3() + " must be 300");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    cl.put4(0);
                    if (cl.get4() != 400) {
                        ReliabilityRunner.debug("Incorrect get4(): "
                                + cl.get4() + " must be 400");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }

                    cl.put5(0);
                    if (cl.get5() != 500) {
                        ReliabilityRunner.debug("Incorrect get5(): "
                                + cl.get5() + " must be 500");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    cl.put500(0);
                    if (cl.get500() != 500) {
                        ReliabilityRunner.debug("Incorrect get500(): "
                                + cl.get500() + " must be 500");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }

                    cl.put499(1);
                    if (cl.get499() != 500) {
                        ReliabilityRunner.debug("Incorrect get499(): "
                                + cl.get499() + " must be 500");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }

                    cl.put498(2);
                    if (cl.get498() != 500) {
                        ReliabilityRunner.debug("Incorrect get498(): "
                                + cl.get498() + " must be 500");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    if (!cl.PUBSTATFIN_F0_1 || cl.PUBSTATFIN_F1_1) {
                        ReliabilityRunner
                                .debug("Incorrect cl.PUBSTATFIN_F0_1 or cl.PUBSTATFIN_F1_1: "
                                        + cl.PUBSTATFIN_F0_1
                                        + "  "
                                        + cl.PUBSTATFIN_F1_1
                                        + " must be true ans false");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    if (cl.PUBSTATFIN_F2_1 != 2l) {
                        ReliabilityRunner
                                .debug("Incorrect cl.PUBSTATFIN_F2_:  "
                                        + cl.PUBSTATFIN_F2_1 + " must be 2l");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }

                    if (cl.PUBSTATFIN_F3_1 != 3.0f) {
                        ReliabilityRunner
                                .debug("Incorrect cl.PUBSTATFIN_F3_1: "
                                        + cl.PUBSTATFIN_F3_1 + " must be 3.0f");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    if (!cl.PUBSTATFIN_F4_1.equals("PUBSTATFIN_F4")) {
                        ReliabilityRunner
                                .debug("Incorrect cl.PUBSTATFIN_F4_1:  "
                                        + cl.PUBSTATFIN_F4_1
                                        + " must be \"PUBSTATFIN_F4\"");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    if (cl.PUBSTATFIN_F5_1 != 5.0d) {
                        ReliabilityRunner
                                .debug("Incorrect cl.PUBSTATFIN_F5_1:  "
                                        + cl.PUBSTATFIN_F5_1 + " must be 5.0d");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }

                    if (cl.PUBSTATFIN_F499_1.length != 3) {
                        ReliabilityRunner
                                .debug("Incorrect PUBSTATFIN_F499_1.length:  "
                                        + cl.PUBSTATFIN_F499_1.length
                                        + " must be 3");
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    } else if (!(cl.PUBSTATFIN_F499_1)[0]
                            .equals(cl.PUBSTATFIN_F498_1)) {
                        ReliabilityRunner
                                .debug("Incorrect PUBSTATFIN_F499_1[0]:  "
                                        + cl.PUBSTATFIN_F499_1[0] + " must be "
                                        + cl.PUBSTATFIN_F498_1);
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());

                    } else if (!(cl.PUBSTATFIN_F499_1)[1]
                            .equals((cl.PUBSTATFIN_F499_1)[2])) {
                        ReliabilityRunner
                                .debug("PUBSTATFIN_F499_1[1] must be equal to PUBSTATFIN_F499_1[2], but they are:  "
                                        + (cl.PUBSTATFIN_F499_1)[1]
                                        + (cl.PUBSTATFIN_F499_1)[2]);
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                    for (int j = 0; j < 3; j++) {
                        if (!(cl.PUBSTATFIN_F500_1)[0][j]
                                .equals(cl.PUBSTATFIN_F498_1)) {
                            ReliabilityRunner.debug("Step: " + j
                                    + " Incorrect PUBSTATFIN_F500[0][j] :  "
                                    + (cl.PUBSTATFIN_F500_1)[0][j]
                                    + "  must be " + cl.PUBSTATFIN_F498_1);
                            ReliabilityRunner.mainTest.addError(this,
                                    new ClassLoaderTestError());
                        }
                    }

                    if (!(cl.PUBSTATFIN_F500_1)[1].equals(cl.PUBSTATFIN_F499_1)) {
                        ReliabilityRunner
                                .debug("Incorrect PUBSTATFIN_F500 [1]:  "
                                        + (cl.PUBSTATFIN_F500_1)[1]
                                        + " must be " + cl.PUBSTATFIN_F499_1);
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
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

class t_cl0 extends testManySupIntf_C {
}

class t_cl1 extends testManySupIntf_C {
}

class t_cl2 extends testManySupIntf_C {
}

class t_cl3 extends testManySupIntf_C {
}

class t_cl4 extends testManySupIntf_C {
}

class t_cl5 extends testManySupIntf_C {
}

class t_cl6 extends testManySupIntf_C {
}

class t_cl7 extends testManySupIntf_C {
}

class t_cl8 extends testManySupIntf_C {
}

class t_cl9 extends testManySupIntf_C {
}

class t_cl10 extends testManySupIntf_C {
}

class t_cl11 extends testManySupIntf_C {
}

class t_cl12 extends testManySupIntf_C {
}

class t_cl13 extends testManySupIntf_C {
}

class t_cl14 extends testManySupIntf_C {
}

class t_cl15 extends testManySupIntf_C {
}

class t_cl16 extends testManySupIntf_C {
}

class t_cl17 extends testManySupIntf_C {
}

class t_cl18 extends testManySupIntf_C {
}

class t_cl19 extends testManySupIntf_C {
}

class t_cl20 extends testManySupIntf_C {
}

class t_cl21 extends testManySupIntf_C {
}

class t_cl22 extends testManySupIntf_C {
}

class t_cl23 extends testManySupIntf_C {
}

class t_cl24 extends testManySupIntf_C {
}

class t_cl25 extends testManySupIntf_C {
}

class t_cl26 extends testManySupIntf_C {
}

class t_cl27 extends testManySupIntf_C {
}

class t_cl28 extends testManySupIntf_C {
}

class t_cl29 extends testManySupIntf_C {
}
