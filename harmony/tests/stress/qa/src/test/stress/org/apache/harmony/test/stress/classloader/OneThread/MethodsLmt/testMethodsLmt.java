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

package org.apache.harmony.test.stress.classloader.OneThread.MethodsLmt;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.MethodsLmtClasses.testMethodsLmt_C;

public class testMethodsLmt extends TestCase {

    public static final int ARRSIZE = 2000;

    public static final int CNTLOOP = 10000;

    public void test() {
        final int ARGS_COUNT = 1;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.OneThread."
                            + "MethodsLmt.testMethodsLmt.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - number of iterations

        int lmtLoop = testArgs.length <= 0 ? CNTLOOP : (Integer
                .valueOf(testArgs[0])).intValue();

        ReliabilityRunner.debug("Use classes which declare more than 10000 methods ");

        int i = 0;
        int j = 0;
        testMethodsLmt_C[] ar;
        testMethodsLmt_C[] ar1;
        boolean pass = true;

        for (int t = 0; t < lmtLoop; t++) {
            if (t % 1000 == 0) {
                ReliabilityRunner.debug("Step: " + t + "...");
            }
            ar = new testMethodsLmt_C[ARRSIZE];
            ar1 = new testMethodsLmt_C[ARRSIZE];

            for (i = 0; i < ARRSIZE; i++) {
                try {
                    Class cl = Class
                            .forName("org.apache.harmony.test.stress.classloader.OneThread.MethodsLmt.a"
                                    .concat(Integer.toString(i % 30)));
                    ar1[i] = (testMethodsLmt_C) (cl.newInstance());

                    ar[i] = new testMethodsLmt_C();
                    for (j = 1; j <= i; j++) {
                        pass = ((ar[j - 1].getFirst() == ar[j].getFirst()) && (ar[j - 1]
                                .getLast() == ar[j].getLast()));
                        if (!pass) {
                            ReliabilityRunner.debug("Step: " + j
                                    + " incorrect results");
                            ReliabilityRunner.debug("Class: "
                                    + ar[j - 1].getClass().getName()
                                    + " getFirst()=" + ar[j - 1].getFirst());
                            ReliabilityRunner.debug("Class: "
                                    + ar[j - 1].getClass().getName()
                                    + " getLast()=" + ar[j - 1].getLast());
                            ReliabilityRunner.debug("Class: "
                                    + ar[j].getClass().getName()
                                    + " getFirst()=" + ar[j].getFirst());
                            ReliabilityRunner.debug("Class: "
                                    + ar[j].getClass().getName()
                                    + " getLast()=" + ar[j].getLast());
                            ReliabilityRunner.debug("Test fails: incorrect returned values");
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                    }
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            for (i = 0; i < ARRSIZE; i++) {
                try {
                    if (ar1[i].getFirst() != ar[0].getFirst()) {
                        ReliabilityRunner.debug("Step: " + i
                                + " incorrect results");
                        ReliabilityRunner.debug("Class: "
                                + ar1[i].getClass().getName() + " getFirst()="
                                + ar1[i].getFirst());
                        ReliabilityRunner.debug("Class: "
                                + ar1[i].getClass().getName() + " getLast()="
                                + ar1[i].getLast());
                        ReliabilityRunner.debug("Class: "
                                + ar[0].getClass().getName() + " getFirst()="
                                + ar[0].getFirst());
                        ReliabilityRunner.debug("Class: "
                                + ar[0].getClass().getName() + " getLast()="
                                + ar[0].getLast());
                        ReliabilityRunner.debug("Test fails: incorrect returned values");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                } catch (OutOfMemoryError e) {
                    ReliabilityRunner.debug(e.toString() + " on step: 2");
                    ReliabilityRunner.mainTest.addError(this, e);
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            ar = null;
            ar1 = null;
        }
    }
}

class a0 extends testMethodsLmt_C {
}

class a1 extends testMethodsLmt_C {
}

class a2 extends testMethodsLmt_C {
}

class a3 extends testMethodsLmt_C {
}

class a4 extends testMethodsLmt_C {
}

class a5 extends testMethodsLmt_C {
}

class a6 extends testMethodsLmt_C {
}

class a7 extends testMethodsLmt_C {
}

class a8 extends testMethodsLmt_C {
}

class a9 extends testMethodsLmt_C {
}

class a10 extends testMethodsLmt_C {
}

class a11 extends testMethodsLmt_C {
}

class a12 extends testMethodsLmt_C {
}

class a13 extends testMethodsLmt_C {
}

class a14 extends testMethodsLmt_C {
}

class a15 extends testMethodsLmt_C {
}

class a16 extends testMethodsLmt_C {
}

class a17 extends testMethodsLmt_C {
}

class a18 extends testMethodsLmt_C {
}

class a19 extends testMethodsLmt_C {
}

class a20 extends testMethodsLmt_C {
}

class a21 extends testMethodsLmt_C {
}

class a22 extends testMethodsLmt_C {
}

class a23 extends testMethodsLmt_C {
}

class a24 extends testMethodsLmt_C {
}

class a25 extends testMethodsLmt_C {
}

class a26 extends testMethodsLmt_C {
}

class a27 extends testMethodsLmt_C {
}

class a28 extends testMethodsLmt_C {
}

class a29 extends testMethodsLmt_C {
}
