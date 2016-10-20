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
 * @version: $Revision: 1.6 $
 */
package org.apache.harmony.test.stress.classloader.NotSynchThreads.FieldsLmt;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.FieldsLmtClasses.*;

public class testFieldsLmt_0 extends TestCase {

    public static final int CNTLOOP = 10000;

    public static final int ARRSIZE = 2000;

    private testFieldsLmt_C0 cl0;

    private testFieldsLmt_C1 cl1;

    private testFieldsLmt_C2 cl2;

    private testFieldsLmt_C3 cl3;

    private testFieldsLmt_CE_0 cl00;

    private testFieldsLmt_CE_1 cl10;

    private testFieldsLmt_CE_2 cl20;

    private testFieldsLmt_CE_3 cl30;

    public int test1(String[] args) {

        int lmtLoop = args.length <= 0 ? CNTLOOP : (Integer.valueOf(args[0]))
                .intValue();
        testFieldsLmt_C[] ar;

        for (int t = 0; t < lmtLoop; t++) {
            ar = new testFieldsLmt_C[ARRSIZE];
            try {
                cl0 = new testFieldsLmt_C0();
                cl1 = new testFieldsLmt_C1();
                cl2 = new testFieldsLmt_C2();
                cl3 = new testFieldsLmt_C3();
                cl00 = new testFieldsLmt_CE_0();
                cl10 = new testFieldsLmt_CE_1();
                cl20 = new testFieldsLmt_CE_2();
                cl30 = new testFieldsLmt_CE_3();
                cl0.put();
                cl1.put();
                cl2.put();
                cl3.put();
                cl00.put();
                cl10.put();
                cl20.put();
                cl30.put();

                int s = check_fields();
                if (s != 0) {
                    ReliabilityRunner.debug("Test failed: incorrect results - " + s);
                    ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                }

                for (int i = 0; i < ARRSIZE; i++) {
                    switch (i % 8) {
                    case 0:
                        ar[i] = new testFieldsLmt_C0();
                        break;
                    case 1:
                        ar[i] = new testFieldsLmt_C1();
                        break;
                    case 2:
                        ar[i] = new testFieldsLmt_C2();
                        break;
                    case 3:
                        ar[i] = new testFieldsLmt_C3();
                        break;
                    case 4:
                        ar[i] = new testFieldsLmt_CE_0();
                        break;
                    case 5:
                        ar[i] = new testFieldsLmt_CE_1();
                        break;
                    case 6:
                        ar[i] = new testFieldsLmt_CE_2();
                        break;
                    case 7:
                        ar[i] = new testFieldsLmt_CE_3();
                        break;
                    default:
                        break;
                    }
                }
                for (int i = 0; i < ARRSIZE; i++) {
                    int n1 = 0;
                    int n2 = 0;
                    switch (i % 8) {
                    case 0:
                        ((testFieldsLmt_C0) ar[i]).put();
                        n1 = ((testFieldsLmt_C0) ar[i]).getFirst();
                        n2 = ((testFieldsLmt_C0) ar[i]).getLast();
                        break;
                    case 1:
                        ((testFieldsLmt_C1) ar[i]).put();
                        n1 = ((testFieldsLmt_C1) ar[i]).getFirst();
                        n2 = ((testFieldsLmt_C1) ar[i]).getLast();
                        break;
                    case 2:
                        ((testFieldsLmt_C2) ar[i]).put();
                        n1 = ((testFieldsLmt_C2) ar[i]).getFirst();
                        n2 = ((testFieldsLmt_C2) ar[i]).getLast();
                        break;
                    case 3:
                        ((testFieldsLmt_C3) ar[i]).put();
                        n1 = ((testFieldsLmt_C3) ar[i]).getFirst();
                        n2 = ((testFieldsLmt_C3) ar[i]).getLast();
                        break;
                    case 4:
                        ((testFieldsLmt_CE_0) ar[i]).put();
                        n1 = ((testFieldsLmt_CE_0) ar[i]).getFirst();
                        n2 = ((testFieldsLmt_CE_0) ar[i]).getLast();
                        break;
                    case 5:
                        ((testFieldsLmt_CE_1) ar[i]).put();
                        n1 = ((testFieldsLmt_CE_1) ar[i]).getFirst();
                        n2 = ((testFieldsLmt_CE_1) ar[i]).getLast();
                        break;
                    case 6:
                        ((testFieldsLmt_CE_2) ar[i]).put();
                        n1 = ((testFieldsLmt_CE_2) ar[i]).getFirst();
                        n2 = ((testFieldsLmt_CE_2) ar[i]).getLast();
                        break;
                    case 7:
                        ((testFieldsLmt_CE_3) ar[i]).put();
                        n1 = ((testFieldsLmt_CE_3) ar[i]).getFirst();
                        n2 = ((testFieldsLmt_CE_3) ar[i]).getLast();
                        break;
                    default:
                        break;
                    }
                    if ((n1 == 0) || (n2 == 0)) {
                        fail ("Test failed: class "
                                + ar[i].getClass().getName() + "   " + n1
                                + "   " + n2);
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

    public int check_fields() {
        //boolean pass;
        if (cl0.getFirst() != cl1.getFirst()) {
            return 1;
        }
        if (cl0.getFirst() != cl2.getFirst()) {
            return 2;
        }
        if (cl0.getFirst() != cl3.getFirst()) {
            return 3;
        }
        if (cl0.getLast() != cl1.getLast()) {
            return 4;
        }
        if (cl0.getLast() != cl2.getLast()) {
            return 5;
        }
        if (cl0.getLast() != cl3.getLast()) {
            return 6;
        }
        if (cl00.getFirst() != cl10.getLast()) {
            return 7;
        }
        if (cl00.getFirst() != cl20.getLast()) {
            return 8;
        }
        if (cl00.getFirst() != cl30.getLast()) {
            return 9;
        }
        if (cl00.getLast() != cl10.getFirst()) {
            return 10;
        }
        if (cl00.getLast() != cl20.getFirst()) {
            return 11;
        }
        if (cl00.getLast() != cl30.getFirst()) {
            return 12;
        }
        return 0;
    }

    public void test() {
        ReliabilityRunner.debug("Not supported");
        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
    }

}
