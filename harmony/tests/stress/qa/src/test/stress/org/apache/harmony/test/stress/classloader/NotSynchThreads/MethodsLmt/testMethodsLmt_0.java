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
package org.apache.harmony.test.stress.classloader.NotSynchThreads.MethodsLmt;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.MethodsLmtClasses.*;

public class testMethodsLmt_0 extends TestCase {

    public static final int CNTLOOP = 10000;

    public static final int ARRSIZE = 2000;

    private testMethodsLmt_C cl0;

    private testMethodsLmt_C1 cl1;

    private testMethodsLmt_C2 cl2;

    private testMethodsLmt_C3 cl3;

    public int test1(String[] args) {

        int lmtLoop = args.length <= 0 ? CNTLOOP : (Integer.valueOf(args[0]))
                .intValue();
        testMethodsLmt_C[] ar;

        for (int t = 0; t < lmtLoop; t++) {
            ar = new testMethodsLmt_C[ARRSIZE];
            try {
                cl0 = new testMethodsLmt_C();
                cl1 = new testMethodsLmt_C1();
                cl2 = new testMethodsLmt_C2();
                cl3 = new testMethodsLmt_C3();

                if (!check_fields()) {
                    ReliabilityRunner.debug("Test fails: incorrect results");
                    ReliabilityRunner.debug("cl0.getFirst(): "
                            + cl0.getFirst() + " cl1.getFirst(): "
                            + cl1.getFirst() + " cl2.getFirst(): "
                            + cl2.getFirst() + "cl2.getFirst1(): "
                            + cl2.getFirst1() + " cl3.getFirst(): "
                            + cl3.getFirst());
                    ReliabilityRunner.debug("cl0.getLast(): "
                            + cl0.getLast() + " cl1.getLast(): "
                            + cl1.getLast() + " cl2.getLast(): "
                            + cl2.getLast() + " cl2.getLast1(): "
                            + cl2.getLast1() + " cl3.getLast(): "
                            + cl3.getLast());
                    ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                }
                for (int i = 0; i < ARRSIZE; i++) {
                    switch (i % 4) {
                    case 0:
                        ar[i] = (testMethodsLmt_C) new testMethodsLmt_C();
                        break;
                    case 1:
                        ar[i] = (testMethodsLmt_C) new testMethodsLmt_C1();
                        break;
                    case 2:
                        ar[i] = (testMethodsLmt_C) new testMethodsLmt_C2();
                        break;
                    case 3:
                        ar[i] = (testMethodsLmt_C) new testMethodsLmt_C3();
                        break;
                    default:
                        break;
                    }
                    ar[i].putF(i);
                }
                for (int i = 0; i < ARRSIZE; i++) {
                    int n = ar[i].getF();
                    if (n != i) {
                        ReliabilityRunner.debug("Test failed: class " + ar[i].getClass().getName()
                                + " getF() = " + n + " must be " + i);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                }
            } catch (Throwable e) {
                ReliabilityRunner.debug(e.toString());
                ReliabilityRunner.mainTest.addError(this, e);
            }

            cl0 = null;
            cl1 = null;
            cl2 = null;
            cl3 = null;
            ar = null;
        }
        return ReliabilityRunner.RESULT_PASS;
    }

    public boolean check_fields() {
        // boolean pass;
        return ((cl0.getFirst() == cl1.getLast())
                && (cl0.getFirst() == cl2.getFirst())
                && (cl0.getFirst() == cl2.getLast1() && (cl0.getFirst() == cl3
                        .getFirst())) && (cl0.getLast() == cl1.getFirst())
                && (cl0.getLast() == cl2.getLast())
                && (cl0.getLast() == cl2.getFirst1()) && (cl0.getLast() == cl3
                .getLast()));
    }

    public void test() {
        ReliabilityRunner.debug("Not supported");
        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
    }
}
