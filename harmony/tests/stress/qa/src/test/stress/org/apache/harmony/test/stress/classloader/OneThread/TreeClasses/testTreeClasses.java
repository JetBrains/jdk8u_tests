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
 * @version: $Revision: 1.8 $
 */

package org.apache.harmony.test.stress.classloader.OneThread.TreeClasses;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class testTreeClasses extends TestCase {

    private static int cntCl = 0;

    private String st = "";

    private int sumM = 0;

    public static int sumAll = 0;

    public testTreeClasses() {
        super();
        ++cntCl;
    }

    public static int getCntCl() {
        return cntCl;
    }

    public void putSt(String s) {
        if (s != null) {
            st = st.concat(s);
        }
    }

    public String getSt() {
        return st;
    }

    public int getSumM() {
        return sumM;
    }

    public void putSumM(int t) {
        if (t >= 0) {
            sumM += t;
            sumAll += t;
        }
    }

    private static String[] clsN = { "testTreeClasses", "cl1", "cl2", "cl11",
            "cl12", "cl21", "cl22", "cl111", "cl112", "cl121", "cl122",
            "cl211", "cl212", "cl221", "cl222", "cl1111", "cl1112", "cl1121",
            "cl1122", "cl1211", "cl1212", "cl1221", "cl1222", "cl2111",
            "cl2112", "cl2121", "cl2122", "cl2211", "cl2212", "cl2221",
            "cl2222" };

    public static int getNmbUsedClss() {
        return clsN.length;
    }

    public static final int LOOPCNT = 10000;

    public static boolean logIn = true;

    public static boolean checkIn = true;

    private static int cntLoop = 0;

    public static int getCntLoop() {
        return cntLoop;
    }

    public int test1(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if(args[i] != null) {
                System.setProperty("org.apache.harmony.test."
                        + "stress.classloader.OneThread."
                        + "TreeClasses.testTreeClasses.arg" + i, args[i]);
            }
        }
        test();
        return 104;
    }
    
    public void test() {
        final int ARGS_COUNT = 2;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.OneThread."
                            + "TreeClasses.testTreeClasses.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        cntLoop = testArgs.length <= 0 ? LOOPCNT : Integer.valueOf(testArgs[0])
                .intValue();

        if (logIn) {
            ReliabilityRunner.debug("Try to load tree of classes ");
        }

        String pkgN = testArgs.length <= 1 ? this.getClass().getPackage()
                .getName() : testArgs[1];

        String name;
        String[] prm = new String[1];
        prm[0] = Integer.toString(1);
        testTreeClasses[] tt;
        int smm = 0;
        for (int t = 0; t < cntLoop; t++) {
            tt = new testTreeClasses[clsN.length];
            for (int i = (clsN.length - 1); i >= 0; i--) {
                try {
                    name = pkgN.concat(".").concat(clsN[i]);
                    Class cl = Class.forName(name);
                    tt[i] = (testTreeClasses) cl.newInstance();
                    tt[i].putSt(name);
                    tt[i].putSt(null);
                    if (!tt[i].getSt().equals(name)) {
                        ReliabilityRunner.debug("Incorrect getST(): " + tt[i].getSt()
                                + " must be: " + name);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    tt[i].putSumM(i + 1);
                    smm += (i + 1);
                    tt[i].putSumM(0 - i);
                    if (tt[i].getSumM() != (i + 1)) {
                        ReliabilityRunner.debug("Incorrect getSumM(): " + tt[i].getSumM()
                                + "  must be: " + (i + 1));
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            tt = null;
        }
        if (checkIn) {
            if (testTreeClasses.getCntCl() != (clsN.length * cntLoop + 1)) {
                ReliabilityRunner.debug("Incorrect result: getCntCl() = "
                        + testTreeClasses.getCntCl() + " must be "
                        + (clsN.length * cntLoop + 1));
                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
            }
            if (testTreeClasses.sumAll != smm) {
                ReliabilityRunner.debug("Incorrect result: sumAll = " + testTreeClasses.sumAll
                        + " must be " + smm);
                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
            }
        }
    }
}

class cl1 extends testTreeClasses {
}

class cl11 extends cl1 {
}

class cl12 extends cl1 {

}

class cl111 extends cl11 {
}

class cl112 extends cl11 {
}

class cl121 extends cl12 {
}

class cl122 extends cl12 {
}

class cl1111 extends cl111 {
}

class cl1112 extends cl111 {
}

class cl1121 extends cl112 {
}

class cl1122 extends cl112 {
}

class cl1211 extends cl121 {
}

class cl1212 extends cl121 {
}

class cl1221 extends cl122 {
}

class cl1222 extends cl122 {
}

class cl2 extends testTreeClasses {
}

class cl21 extends cl2 {
}

class cl22 extends cl2 {
}

class cl211 extends cl21 {
}

class cl212 extends cl21 {
}

class cl221 extends cl22 {
}

class cl222 extends cl22 {
}

class cl2111 extends cl211 {
}

class cl2112 extends cl211 {
}

class cl2121 extends cl212 {
}

class cl2122 extends cl212 {
}

class cl2211 extends cl221 {
}

class cl2212 extends cl221 {
}

class cl2221 extends cl222 {
}

class cl2222 extends cl222 {
}
