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

package org.apache.harmony.test.stress.classloader.NotSynchThreads.SupIntf;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import java.lang.reflect.*;

public class testManySupIntf_02 extends TestCase {

    public void test() {
        ReliabilityRunner.debug("Not supported");
        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
    }

    public static final int ARRSIZE = 2000;

    public static final int LOOPCNT = 10000;

    public static String msgErr = "";

    public int test1(String[] args) {
        String pkgN = args.length <= 0 ? this.getClass().getPackage().getName()
                : args[0];
        int cntLoop = args.length <= 1 ? LOOPCNT : Integer.valueOf(args[1])
                .intValue();

        Class[] arClss;
        Object[] arObjs;

        // ReliabilityRunner.debug("Use classes which implements interface
        // with many
        // superinterfaces");
        // ReliabilityRunner.debug("Number of iterations: " + cntLoop);

        for (int t = 0; t < cntLoop; t++) {
            if (t % 1000 == 0) {
                // ReliabilityRunner.debug("Step: " + t + "...");
            }
            arClss = new Class[ARRSIZE];
            arObjs = new Object[ARRSIZE];
            String nm = "";
            nm = pkgN.concat(".").concat("testIntfLmt_C");

            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    arClss[i] = Class.forName(nm);
                    arObjs[i] = arClss[i].newInstance();
                    Method[] mm = arClss[i].getMethods();
                    // int mInd;
                    // int mInd1;
                    // int ans;
                    // Object[] par = new Object[1];
                    if (verifyRes(mm, i, arObjs[i]) == 0) {
                        ReliabilityRunner.debug("Class: " + arClss[i] + msgErr);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            arClss = null;
            arObjs = null;
        }
        return ReliabilityRunner.RESULT_PASS;
    }

    private int fndMethod(Method[] mm, String nm) {
        for (int i = 0; i < mm.length; i++) {
            if (mm[i].getName().equals(nm)) {
                return i;
            }
        }
        return -1;
    }

    public int verifyRes(Method[] mm, int l, Object arO) {
        int mInd;
        int mInd1;
        String nm1;
        String nm2;
        msgErr = "";
        Object[] par = new Object[1];
        int ans;
        try {
            for (int i = 0; i <= 9; i++) {
                nm1 = "mPut".concat(Integer.toString(i));
                nm2 = "mGet".concat(Integer.toString(i));
                mInd = fndMethod(mm, nm1);
                if (mInd != -1) {
                    par[0] = new Integer(l);
                    mm[mInd].invoke(arO, par);
                    mInd1 = fndMethod(mm, nm2);
                    if (mInd1 != -1) {
                        par[0] = new Integer(l + i + 1);
                        ans = ((Integer) (mm[mInd1].invoke(arO, par)))
                                .intValue();
                        if (ans != (2 * l + (2 * i + 1))) {
                            msgErr = "Incorrect "
                                    .concat(nm2)
                                    .concat("(")
                                    .concat(Integer.toString(l + i + 1))
                                    .concat("): ")
                                    .concat(Integer.toString(ans))
                                    .concat(" must be ")
                                    .concat(
                                            Integer
                                                    .toString((2 * l + (2 * i + 1))));
                            return 0;
                        }
                    } else {
                        msgErr = "Method ".concat(nm2).concat(" was not found");
                        return 0;
                    }
                } else {
                    msgErr = "Method ".concat(nm1).concat(" was not found");
                    return 0;
                }
            }
            mInd = fndMethod(mm, "mPut10");
            if (mInd != -1) {
                par[0] = new Integer(l * 10);
                mm[mInd].invoke(arO, par);
                mInd1 = fndMethod(mm, "mGet10");
                if (mInd1 != -1) {
                    par[0] = new Integer(l + 11);
                    ans = ((Integer) (mm[mInd1].invoke(arO, par))).intValue();
                    if (ans != (11 * l + 21)) {
                        msgErr = "Incorrect mGet10(".concat(
                                Integer.toString(l + 11)).concat("): ").concat(
                                Integer.toString(ans)).concat(" must be ")
                                .concat(Integer.toString(11 * l + 21));
                        return 0;
                    }
                } else {
                    msgErr = "Method mGet10() was not found";
                    return 0;
                }
            } else {
                msgErr = "Method mPut10() was not found";
                return 0;
            }
        } catch (Throwable e) {
            ReliabilityRunner.debug("Test failed: unexpected error " + e);
            ReliabilityRunner.debug(e.toString());
            msgErr = "Test fails: unexpected error was thrown";
            return 0;
        }
        return 1;
    }
}
