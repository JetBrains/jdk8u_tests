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

package org.apache.harmony.test.stress.classloader.NotSynchThreads.LargeCode;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import java.lang.reflect.*;

public class testCodeLmt_0 extends TestCase {

    public void test() {
        ReliabilityRunner.debug("Unsupported method");
        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
    }

    public static final int ARRSIZE = 2000;

    public static final int LOOPCNT = 10000;

    public int test1(String[] args) {

        String pkgN = args.length <= 0 ? this.getClass().getPackage().getName()
                : args[0];
        int cntLoop = args.length <= 1 ? LOOPCNT : Integer.valueOf(args[1])
                .intValue();

        Object[] arObjs;
        Class[] arClss;
        Method m;
        int pass = 0;
        int fail = 0;
        int pass1 = 0;
        int fail1 = 0;

        for (int t = 0; t < cntLoop; t++) {
            if (t % 1000 == 0) {
                //              ReliabilityRunner.debug("Step: " + t + "...");
            }
            arClss = new Class[ARRSIZE];
            arObjs = new Object[ARRSIZE];
            pass = 0;
            fail = 0;
            pass1 = 0;
            fail1 = 0;
            String nm = "";
            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    if (i % 2 == 0) {
                        pass1++;
                        nm = "testCodeLmt_C";
                    } else {
                        fail1++;
                        nm = "testCodeLmt_CWr";
                    }

                    arClss[i] = Class.forName(pkgN.concat(".").concat(nm));
                    arObjs[i] = arClss[i].newInstance();

                    m = arClss[i].getMethod("get");
                    int ans = ((Integer) (m.invoke(arObjs[i])))
                            .intValue();
                    if (i % 2 == 0) {
                        if (ans == 104) {
                            pass++;
                        }
                    }
                } catch (InvocationTargetException e) {
                    if (i % 2 != 0) {
                        fail++;
                    } else {
                        ReliabilityRunner.debug(e.toString());
                        ReliabilityRunner.mainTest.addError(this, e);
                    }
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            if ((pass != pass1) || (fail != fail1)) {
                ReliabilityRunner.debug("Test fails: incorrect results: pass = " + pass
                        + " must be: " + pass1 + "  fail = " + fail
                        + " must be: " + fail1);
                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
            }
            arClss = null;
            arObjs = null;
        }
        return ReliabilityRunner.RESULT_PASS;
    }
}
