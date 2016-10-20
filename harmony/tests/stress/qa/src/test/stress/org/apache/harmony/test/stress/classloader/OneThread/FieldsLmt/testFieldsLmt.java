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

package org.apache.harmony.test.stress.classloader.OneThread.FieldsLmt;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.FieldsLmtClasses.*;

public class testFieldsLmt extends TestCase {
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
                            + "FieldsLmt.testFieldsLmt.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        int lmtLoop = testArgs.length <= 0 ? LOOPCNT : (Integer
                .valueOf(testArgs[0])).intValue();

        ReliabilityRunner.debug("Use class with many fields ");

        testFieldsLmt_C0[] ar;
        testFieldsLmt_C0[] ar1;
        boolean pass = true;
        for (int t = 0; t < lmtLoop; t++) {
            if (t % 200 == 0) {
                ReliabilityRunner.debug("Step: " + t + "...");
            }
            ar = new testFieldsLmt_C0[ARRSIZE];
            ar1 = new testFieldsLmt_C0[ARRSIZE];
            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    ar[i] = new testFieldsLmt_C0();
                    ar1[i] = new testFieldsLmt_C0();

                    Class cl = Class
                            .forName("org.apache.harmony.test.stress.classloader.OneThread.FieldsLmt.fClass"
                                    .concat(Integer.toString(i % 30)));
                    ar1[i] = (testFieldsLmt_C0) cl.newInstance();

                    ar[i].put();
                    ar1[i].put();

                    for (int j = 0; j <= i; j++) {
                        pass = ((ar[j].getFirst() == 333) && (ar[j].getLast() == 555));
                        if (!pass) {
                            ReliabilityRunner
                                    .debug("Test fails: incorrect results class:"
                                            + ar[j].getClass().getName()
                                            + "  getFirst() "
                                            + ar[j].getFirst()
                                            + "  getLast() = "
                                            + +ar[j].getLast());
                            ReliabilityRunner.mainTest.addError(this,
                                    new ClassLoaderTestError());
                        }
                    }
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            for (int i = 0; i < ARRSIZE; i++) {
                pass = ((ar1[i].getFirst() == 333) && (ar[0].getLast() == 555));
                if (!pass) {
                    ReliabilityRunner.debug("Test fails: incorrect results class:"
                            + ar1[i].getClass().getName() + "  getFirst() "
                            + ar1[i].getFirst() + "  getLast() = "
                            + +ar1[i].getLast());
                    ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                }
            }

            ar = null;
            ar1 = null;
        }
    }

}

class fClass0 extends testFieldsLmt_C0 {
}

class fClass1 extends testFieldsLmt_C0 {
}

class fClass2 extends testFieldsLmt_C0 {
}

class fClass3 extends testFieldsLmt_C0 {
}

class fClass4 extends testFieldsLmt_C0 {
}

class fClass5 extends testFieldsLmt_C0 {
}

class fClass6 extends testFieldsLmt_C0 {
}

class fClass7 extends testFieldsLmt_C0 {
}

class fClass8 extends testFieldsLmt_C0 {
}

class fClass9 extends testFieldsLmt_C0 {
}

class fClass10 extends testFieldsLmt_C0 {
}

class fClass11 extends testFieldsLmt_C0 {
}

class fClass12 extends testFieldsLmt_C0 {
}

class fClass13 extends testFieldsLmt_C0 {
}

class fClass14 extends testFieldsLmt_C0 {
}

class fClass15 extends testFieldsLmt_C0 {
}

class fClass16 extends testFieldsLmt_C0 {
}

class fClass17 extends testFieldsLmt_C0 {
}

class fClass18 extends testFieldsLmt_C0 {
}

class fClass19 extends testFieldsLmt_C0 {
}

class fClass20 extends testFieldsLmt_C0 {
}

class fClass21 extends testFieldsLmt_C0 {
}

class fClass22 extends testFieldsLmt_C0 {
}

class fClass23 extends testFieldsLmt_C0 {
}

class fClass24 extends testFieldsLmt_C0 {
}

class fClass25 extends testFieldsLmt_C0 {
}

class fClass26 extends testFieldsLmt_C0 {
}

class fClass27 extends testFieldsLmt_C0 {
}

class fClass28 extends testFieldsLmt_C0 {
}

class fClass29 extends testFieldsLmt_C0 {
}
