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

package org.apache.harmony.test.stress.classloader.NotSynchThreads.SubClasses;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import java.lang.reflect.*;
import org.apache.harmony.test.stress.classloader.OneThread.SubClasses.testManySubClasses_CE70;

public class testManySubClasses_0 extends TestCase {

    public static final int ARRSIZE = 2000;

    public static final int LOOPCNT = 10000;

    public void test() {
        ReliabilityRunner.debug("Not supported");
        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
    }

    public int test1(String[] args) {
        int cntLoop = args.length <= 0 ? LOOPCNT : Integer.valueOf(args[0])
                .intValue();
        String pkgN = args.length <= 1 ? this.getClass().getPackage().getName()
                : args[1];
        // ReliabilityRunner.debug("Use classes which implements interface
        // with "
        // .concat("many superinterfaces"));
        // ReliabilityRunner.debug("Number of iterations: " + cntLoop);
        Object[] arObjs;
        Class[] arClss;
        Method[] mm;
        Field[] ff;
        boolean fnd = true;

        for (int t = 0; t < cntLoop; t++) {
            if (t % 1000 == 0) {
                // ReliabilityRunner.debug("Step: " + t + "...");
            }
            arClss = new Class[ARRSIZE];
            arObjs = new Object[ARRSIZE];
            String nm = "";
            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    nm = pkgN.concat(".").concat("testS");
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
                                ReliabilityRunner.debug("Incorrect pubField: " + s2 + " must be: "
                                        + s1);
                                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
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
                                    ReliabilityRunner.debug("Incorrect get(): " + lans
                                            + " must be " + ((long) i));
                                    ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
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
        return ReliabilityRunner.RESULT_PASS;
    }
}

class testS extends testManySubClasses_CE70 {
}
