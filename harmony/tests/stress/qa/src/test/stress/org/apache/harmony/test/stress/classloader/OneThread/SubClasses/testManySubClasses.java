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

package org.apache.harmony.test.stress.classloader.OneThread.SubClasses;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import java.lang.reflect.*;

public class testManySubClasses extends TestCase {

    public static final int ARRSIZE = 2000;

    public static final int LOOPCNT = 10000;

    public void test() {
        final int ARGS_COUNT = 2;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.OneThread."
                            + "SubClasses.testManySubClasses.arg"
                            + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        int cntLoop = testArgs.length <= 0 ? LOOPCNT : Integer.valueOf(
                testArgs[0]).intValue();
        String pkgN = testArgs.length <= 1 ? this.getClass().getPackage()
                .getName() : testArgs[1];
        ReliabilityRunner.debug("Use classes which have many superclasses");
        ReliabilityRunner.debug("Number of iterations is " + cntLoop);
        Object[] arObjs;
        Class[] arClss;
        Method[] mm;
        Field[] ff;
        boolean fnd = true;

        for (int t = 0; t < cntLoop; t++) {
            if (t % 1000 == 0) {
                ReliabilityRunner.debug("Step: " + t + "...");
            }
            arClss = new Class[ARRSIZE];
            arObjs = new Object[ARRSIZE];
            String nm = "";
            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    nm = pkgN.concat(".");
                    if (i % 201 == 200) {
                        nm = nm.concat("testManySubClasses_CA");
                    } else {
                        nm = nm.concat("testManySubClasses_C").concat(
                                Integer.toString(i % 200));
                    }
                    arClss[i] = Class.forName(nm);
                    arObjs[i] = arClss[i].newInstance();

                    mm = arClss[i].getMethods();

                    ff = arClss[i].getFields();

                    Object[] params = new Object[1];
                    params[0] = new Long((long) i);
                    long lans;

                    fnd = false;
                    for (int l = 0; l < ff.length; l++) {
                        if (ff[l].getName().equals("pubField")) {
                            String s1 = (String) ff[l].get(arObjs[i]);
                            s1 = s1.concat(Integer.toString(i));
                            ff[l].set(arObjs[i], s1);
                            String s2 = (String) ff[l].get(arObjs[i]);
                            if (!s2.equals(s1)) {
                                ReliabilityRunner.debug("Incorrect pubField: "
                                        + s2 + " must be: " + s1);
                                ReliabilityRunner.mainTest.addError(this,
                                        new ClassLoaderTestError());
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Field pubField was not found");
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
                            lans = ((Long) (mm[j].invoke(arObjs[i])))
                                    .longValue();
                            if (i != 0) {
                                if (lans != (long) i) {
                                    ReliabilityRunner.debug("Incorrect get(): "
                                            + lans + " must be " + ((long) i));
                                    ReliabilityRunner.mainTest.addError(this,
                                            new ClassLoaderTestError());
                                }
                            }
                            fnd = true;
                            break;
                        }
                    }
                    if (!fnd) {
                        ReliabilityRunner.debug("Method get() was not found");
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
    }
}
