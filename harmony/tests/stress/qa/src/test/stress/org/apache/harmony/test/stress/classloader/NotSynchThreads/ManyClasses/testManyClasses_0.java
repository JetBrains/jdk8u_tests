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
package org.apache.harmony.test.stress.classloader.NotSynchThreads.ManyClasses;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.CorrectClasses.testManyClasses_CA;

public class testManyClasses_0 extends TestCase {

    public static final int CNTLOOP = 1000;

    public static final int CNTCLSS = 200;

    public static final String TESTNAME_C = "testManyClasses_C";

    public void test() {
        ReliabilityRunner.debug("Unsupported method");
        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
    }

    public int test1(String[] args) {
        // args[0] - correct classes package name
        // args[1] - number of steps
        // args[2] - number of used classes

        String pkgN = args.length <= 0 ? this.getClass().getPackage().getName()
                : args[0];
        int lmtLoop = args.length <= 1 ? CNTLOOP : (Integer.valueOf(args[1]))
                .intValue();
        int lmtClss = args.length <= 2 ? CNTCLSS : (Integer.valueOf(args[2]))
                .intValue();

        Object[] arClss;

        String name = "";

        for (int t = 0; t < lmtLoop; t++) {

            arClss = new Object[lmtClss];

            for (int i = 0; i < lmtClss; i++) {
                try {
                    name = pkgN.concat(".").concat(TESTNAME_C).concat(
                            Integer.toString(i));
                    arClss[i] = Class.forName(name).newInstance();
                    if (((testManyClasses_CA) arClss[i]).PUBSTATFIN_F != 777) {
                        ReliabilityRunner.debug("Class: " + name);
                        ReliabilityRunner.debug("Test failed:  incorrect PUBSTATFIN_F="
                                + ((testManyClasses_CA) arClss[i]).PUBSTATFIN_F);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    if (((testManyClasses_CA) arClss[i]).getPUBSTAT_F() == -1) {
                        ReliabilityRunner.debug("Class: " + name);
                        ReliabilityRunner.debug("Test failed:  incorrect getPUBSTAT_F()="
                                + ((testManyClasses_CA) arClss[i])
                                        .getPUBSTAT_F());
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                } catch (OutOfMemoryError e) {
                    ReliabilityRunner.debug("Unexpected OutOfMemoryError: i=" + i + " t=" + t);
                    ReliabilityRunner.mainTest.addError(this, e);
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            arClss = null;
        }
        return ReliabilityRunner.RESULT_PASS;
    }
}
