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
package org.apache.harmony.test.stress.classloader.NotSynchThreads.MethodNameLmt;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.MethodNameLmtClasses.testMNL_CA.testMNL_C;

public class testMethodNameLmt_0 extends TestCase {

    public static final int CNTLOOP = 1000;

    public static final int CNTCLSS = 30;

    public static final String TESTNAME_C = "testMNL_C";

    public int test1(String[] args) {
        String pkgN = args.length <= 0 ? this.getClass().getPackage().getName()
                : args[0];
        int lmtLoop = args.length <= 1 ? CNTLOOP : (Integer.valueOf(args[1]))
                .intValue();
        int lmtClss = args.length <= 2 ? CNTCLSS : (Integer.valueOf(args[2]))
                .intValue();

        if (lmtClss > CNTCLSS) {
            lmtClss = CNTCLSS;
        }
        Object[] arClss;
        int[] ar;
        String name = "";

        for (int t = 0; t < lmtLoop; t++) {
            arClss = new Object[lmtClss];
            ar = new int[lmtClss];

            for (int i = 0; i < lmtClss; i++) {
                try {
                    name = pkgN.concat(".").concat(TESTNAME_C).concat(
                            Integer.toString(i).concat(".").concat(TESTNAME_C));
                    arClss[i] = Class.forName(name).newInstance();

                    ar[i] = ((testMNL_C) arClss[i]).allSum();
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            for (int i = 0; i < lmtClss; i++) {
                try {
                    name = arClss[i].getClass().getName();
                    int tt = ((testMNL_C) arClss[i]).allSum();
                    if (tt != ar[i]) {
                        ReliabilityRunner.debug("Test fails: class " + name
                                + ": allSum must return " + ar[i]
                                + " but returns " + tt);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }

            arClss = null;
            ar = null;
        }
        return ReliabilityRunner.RESULT_PASS;
    }

    public void test() {
        ReliabilityRunner.debug("Not supported");
        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
    }
}
