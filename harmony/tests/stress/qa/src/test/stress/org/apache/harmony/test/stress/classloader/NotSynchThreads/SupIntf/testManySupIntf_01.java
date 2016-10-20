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

public class testManySupIntf_01 extends TestCase {

    public void test() {
        ReliabilityRunner.debug("Not supported");
        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
    }

    public static final int ARRSIZE = 2000;

    public static final int LOOPCNT = 10000;

    public int test1(String[] args) {
        String pkgN = args.length <= 0 ? this.getClass().getPackage().getName()
                : args[0];
        int cntLoop = args.length <= 1 ? LOOPCNT : Integer.valueOf(args[1])
                .intValue();

        // ReliabilityRunner.debug("Use classes which implements interface
        // with many
        // superinterfaces");
        // ReliabilityRunner.debug("Number of iterations: " + cntLoop);

        Object[] arObjs;
        Class[] arClss;
        //Method m;

        for (int t = 0; t < cntLoop; t++) {
            // if (t % 1000 == 0) {
            // ReliabilityRunner.debug("Step: " + t + "...");
            // }
            arClss = new Class[ARRSIZE];
            arObjs = new Object[ARRSIZE];

            String nm = "";
            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    nm = pkgN.concat(".").concat("testManySupIntf_C");
                    arClss[i] = Class.forName(nm);
                    arObjs[i] = arClss[i].newInstance();

                    Method[] mm = arClss[i].getMethods();
                    Field[] ff = arClss[i].getFields();

                    Object[] params = new Object[1];
                    params[0] = new Integer(777);

                    int ans1;
                    int ans2;
                    int finF = -1;
                    boolean fnd = false;
                    for (int l = 0; l < ff.length; l++) {
                        if (ff[l].getName().equals("PUBSTATFIN_F")) {
                            finF = ff[l].getInt(arObjs[i]);
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Field PUBSTATFIN_F was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("put")) {
                            mm[j].invoke(arObjs[i], params);
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method put() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("get")) {
                            ans1 = ((Integer) (mm[j].invoke(arObjs[i])))
                                    .intValue();
                            if (ans1 != finF) {
                                ReliabilityRunner.debug("Incorrect get(): " + ans1 + " must be "
                                        + finF);
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    params[0] = new Integer(0);

                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("put0")) {
                            mm[j].invoke(arObjs[i], params);
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method put0() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("get0")) {
                            ans1 = ((Integer) (mm[j].invoke(arObjs[i])))
                                    .intValue();
                            if (ans1 != finF) {
                                ReliabilityRunner.debug("Incorrect get1(): " + ans1 + " must be "
                                        + finF);
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get0() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("put1")) {
                            mm[j].invoke(arObjs[i], params);
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method put1() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("get1")) {
                            ans1 = ((Integer) (mm[j].invoke(arObjs[i])))
                                    .intValue();
                            if (ans1 != 100) {
                                ReliabilityRunner.debug("Incorrect get2(): " + ans1
                                        + " must be 100");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get1() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("put2")) {
                            mm[j].invoke(arObjs[i], params);
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method put2() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("get2")) {
                            ans1 = ((Integer) (mm[j].invoke(arObjs[i])))
                                    .intValue();
                            if (ans1 != 200) {
                                ReliabilityRunner.debug("Incorrect get2(): " + ans1
                                        + " must be 200");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get2() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("put3")) {
                            mm[j].invoke(arObjs[i], params);
                            fnd = true;
                            break;
                        }
                    }

                    if (!fnd) {
                        ReliabilityRunner.debug("Method put3() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("get3")) {
                            ans1 = ((Integer) (mm[j].invoke(arObjs[i])))
                                    .intValue();
                            if (ans1 != 300) {
                                ReliabilityRunner.debug("Incorrect get3(): " + ans1
                                        + " must be 300");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get3() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("put4")) {
                            mm[j].invoke(arObjs[i], params);
                            fnd = true;
                            break;
                        }
                    }

                    if (!fnd) {
                        ReliabilityRunner.debug("Method put4() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("get4")) {
                            ans1 = ((Integer) (mm[j].invoke(arObjs[i])))
                                    .intValue();
                            if (ans1 != 400) {
                                ReliabilityRunner.debug("Incorrect get4(): " + ans1
                                        + " must be 400");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get4() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("put5")) {
                            mm[j].invoke(arObjs[i], params);
                            fnd = true;
                            break;
                        }
                    }

                    if (!fnd) {
                        ReliabilityRunner.debug("Method put5() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("get5")) {
                            ans1 = ((Integer) (mm[j].invoke(arObjs[i])))
                                    .intValue();
                            if (ans1 != 500) {
                                ReliabilityRunner.debug("Incorrect get5(): " + ans1
                                        + " must be 500");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get5() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("put500")) {
                            mm[j].invoke(arObjs[i], params);
                            fnd = true;
                            break;
                        }
                    }

                    if (!fnd) {
                        ReliabilityRunner.debug("Method put500() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("get500")) {
                            ans1 = ((Integer) (mm[j].invoke(arObjs[i])))
                                    .intValue();
                            if (ans1 != 500) {
                                ReliabilityRunner.debug("Incorrect get500(): " + ans1
                                        + " must be 500");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get500() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    params[0] = new Integer(1);

                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("put499")) {
                            mm[j].invoke(arObjs[i], params);
                            fnd = true;
                            break;
                        }
                    }

                    if (!fnd) {
                        ReliabilityRunner.debug("Method put499() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("get499")) {
                            ans1 = ((Integer) (mm[j].invoke(arObjs[i])))
                                    .intValue();
                            if (ans1 != 500) {
                                ReliabilityRunner.debug("Incorrect get499(): " + ans1
                                        + " must be 500");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get499() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    params[0] = new Integer(2);
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("put498")) {
                            mm[j].invoke(arObjs[i], params);
                            fnd = true;
                            break;
                        }
                    }

                    if (!fnd) {
                        ReliabilityRunner.debug("Method put498() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int j = 0; j < mm.length; j++) {
                        if (mm[j].getName().equals("get498")) {
                            ans1 = ((Integer) (mm[j].invoke(arObjs[i])))
                                    .intValue();
                            if (ans1 != 500) {
                                ReliabilityRunner.debug("Incorrect get498(): " + ans1
                                        + " must be 500");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get498() was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    boolean bans1;
                    boolean bans2;
                    ans1 = -1;
                    ans2 = -1;
                    for (int l = 0; l < ff.length; l++) {
                        if (ff[l].getName().equals("PUBSTATFIN_F0_1")) {
                            ans1 = l;
                            break;
                        }
                    }
                    if (ans1 == -1) {
                        ReliabilityRunner.debug("Field PUBSTATFIN_F0_1 was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    for (int l = 0; l < ff.length; l++) {
                        if (ff[l].getName().equals("PUBSTATFIN_F1_1")) {
                            bans2 = ff[l].getBoolean(arObjs[i]);
                            ans2 = l;
                            break;
                        }
                    }
                    if (ans2 == -1) {
                        ReliabilityRunner.debug("Field PUBSTATFIN_F1_1 was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    bans1 = ff[ans1].getBoolean(arObjs[i]);
                    bans2 = ff[ans2].getBoolean(arObjs[i]);

                    if (!bans1 || bans2) {
                        ReliabilityRunner.debug("Incorrect PUBSTATFIN_F0_1 or PUBSTATFIN_F1_1: "
                                + bans1 + "  " + bans2
                                + " must be true ans false");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    long lans;
                    // float fans;
                    double dans;
                    String sans;
                    fnd = false;
                    for (int l = 0; l < ff.length; l++) {
                        if (ff[l].getName().equals("PUBSTATFIN_F2_1")) {
                            lans = ff[l].getLong(arObjs[i]);
                            if (lans != 2l) {
                                ReliabilityRunner.debug("Incorrect PUBSTATFIN_F2_1:" + lans + "  "
                                        + " must be 2l");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Field PUBSTATFIN_F2_1 was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int l = 0; l < ff.length; l++) {
                        if (ff[l].getName().equals("PUBSTATFIN_F5_1")) {
                            dans = ff[l].getDouble(arObjs[i]);
                            if (dans != 5.0d) {
                                ReliabilityRunner.debug("Incorrect PUBSTATFIN_F5_1:" + dans + "  "
                                        + " must be 3.0fl");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Field PUBSTATFIN_F5_1 was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    fnd = false;
                    for (int l = 0; l < ff.length; l++) {
                        if (ff[l].getName().equals("PUBSTATFIN_F4_1")) {
                            sans = (String) (ff[l].get(arObjs[i]));
                            if (!sans.equals("PUBSTATFIN_F4")) {
                                ReliabilityRunner.debug("Incorrect PUBSTATFIN_F4_1:" + sans + "  "
                                        + " must be \"PUBSTATFIN_F4\"");
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Field PUBSTATFIN_F4_1 was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    Object o1 = null;
                    Object o2 = null;
                    Object o3 = null;
                    fnd = false;
                    for (int l = 0; l < ff.length; l++) {
                        if (ff[l].getName().equals("PUBSTATFIN_F499_1")) {
                            o1 = ff[l].get(arObjs[i]);
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Field PUBSTATFIN_F499_1 was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    for (int l = 0; l < ff.length; l++) {
                        if (ff[l].getName().equals("PUBSTATFIN_F498_1")) {
                            o2 = ff[l].get(arObjs[i]);
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Field PUBSTATFIN_F498_1 was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    Object[] oo = (Object[]) o1;
                    if (oo.length != 3) {
                        ReliabilityRunner.debug("Incorrect PUBSTATFIN_F499_1.length:  "
                                + oo.length + " must be 3");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    } else if (!oo[0].equals(o2)) {
                        ReliabilityRunner.debug("Incorrect PUBSTATFIN_F499_1[0]: " + oo[0] + "  "
                                + o2);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());

                    } else if (!oo[1].equals(oo[2])) {
                        ReliabilityRunner.debug("PUBSTATFIN_F499_1[1] must be equal to PUBSTATFIN_F499_1[2], but they are :  "
                                + oo[1] + "  " + oo[2]);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    fnd = false;
                    for (int l = 0; l < ff.length; l++) {
                        if (ff[l].getName().equals("PUBSTATFIN_F500_1")) {
                            o3 = ff[l].get(arObjs[i]);
                            fnd = true;
                            break;
                        }
                    }

                    if (!fnd) {
                        ReliabilityRunner.debug("Field PUBSTATFIN_F500_1 was not found");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }

                    Object[][] ooo = (Object[][]) o3;

                    for (int j = 0; j < 3; j++) {
                        if (!ooo[0][j].equals(o2)) {
                            ReliabilityRunner.debug("Step: " + j
                                    + " Incorrect PUBSTATFIN_F500[0][j] :  "
                                    + ooo[0][j] + "  " + o2);
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                    }

                    if (!ooo[1].equals(oo)) {
                        ReliabilityRunner.debug("Incorrect PUBSTATFIN_F500_1 [1]:  " + ooo[1]
                                + "  " + oo);
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
}
