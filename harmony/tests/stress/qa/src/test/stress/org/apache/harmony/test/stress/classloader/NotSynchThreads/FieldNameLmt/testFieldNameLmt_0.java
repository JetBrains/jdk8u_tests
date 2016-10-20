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
package org.apache.harmony.test.stress.classloader.NotSynchThreads.FieldNameLmt;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import java.lang.reflect.*;

public class testFieldNameLmt_0 extends TestCase {
    public static final int CNT_LOOP = 10000;

    public static final int CNT_C = 30;

    public static final int CNT_F = 30;

    public static int cntClss;

    public static int cntFlds;

    public static int cntLoop;

    public static String pkgN;

    public void test() {
        ReliabilityRunner.debug("Not supported");
        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
    }

    public static final String TESTNN = "testFieldNameLmt_C";

    public int test1(String[] args) {

        pkgN = args.length <= 0 ? this.getClass().getPackage().getName()
                : args[0];
        cntLoop = args.length <= 1 ? CNT_LOOP : Integer.valueOf(args[1])
                .intValue();
        cntClss = args.length <= 2 ? CNT_C : Integer.valueOf(args[2])
                .intValue();
        if (cntClss > CNT_C) {
            cntClss = CNT_C;
        }
        // cntFlds = args.length <= 3 ? CNT_F :
        // Integer.valueOf(args[3]).intValue();
        // if (cntFlds > CNT_F) {
        // cntFlds = CNT_F;
        // }
        cntFlds = CNT_F;
        //int notPass = 0;
        // ReliabilityRunner.debug("Loading classes which declare fields
        // with long names");
        // ReliabilityRunner.debug("Number of iterations: " + cntLoop);
        // ReliabilityRunner.debug("Number of used classes: " + cntClss);

        Class[] clss;
        Object[] objs;
        Field[][] flds;
        String name;

        int sm1 = 0;
        int sm2 = 0;

        for (int j = 0; j < cntLoop; j++) {
            clss = new Class[cntClss];
            objs = new Object[cntClss];
            flds = new Field[cntClss][cntFlds];
            sm1 = 0;
            sm2 = 0;
            for (int i = 0; i < clss.length; i++) {
                try {
                    name = pkgN.concat(".").concat(TESTNN).concat(
                            Integer.toString(i)).concat(".").concat(TESTNN);
                    clss[i] = Class.forName(name);
                    flds[i] = clss[i].getDeclaredFields();
                    if (flds[i].length < cntFlds) {
                        ReliabilityRunner.debug("Failed. Wrong fields number in class:"
                                + clss[i].toString() + "  " + flds[i].length
                                + " must be " + cntFlds);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    objs[i] = clss[i].newInstance();
                    for (int t = 0; t < flds[i].length; t++) {
                        if (flds[i][t].getName().length() < 65500) {
                            ReliabilityRunner.debug("Failed. Wrong length of field name in "
                                    + clss[i].toString() + "  "
                                    + flds[i][t].getName().length());
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                        if (i % 2 == 0) {
                            flds[i][t].setInt(objs[i], t + 1);
                            sm1 += (t + 1);
                        } else {
                            flds[i][t].setInt(objs[i], flds[i].length - t);
                            sm1 += flds[i].length - t;
                        }
                    }
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            try {
                for (int i = 0; i < clss.length; i++) {
                    for (int t = 0; t < flds[i].length; t++) {
                        int val = flds[i][t].getInt(objs[i]);
                        if (val <= 0) {
                            ReliabilityRunner.debug("Failed. Wrong field value in class: "
                                    + clss[i].toString() + "  " + val);
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                        sm2 += val;
                    }
                }
                if (sm1 != sm2) {
                    ReliabilityRunner.debug("Failed. Wrong sum of field values in classes: " + sm2
                            + " must be " + sm1);
                    ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                }
            } catch (Throwable e) {
                ReliabilityRunner.debug(e.toString());
                ReliabilityRunner.mainTest.addError(this, e);
            }
            flds = null;
            clss = null;
            objs = null;
        }
        return ReliabilityRunner.RESULT_PASS;
    }
}
