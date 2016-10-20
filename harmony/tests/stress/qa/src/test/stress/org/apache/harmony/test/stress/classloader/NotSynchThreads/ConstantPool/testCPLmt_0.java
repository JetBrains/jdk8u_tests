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

package org.apache.harmony.test.stress.classloader.NotSynchThreads.ConstantPool;

import junit.framework.TestCase;
import java.lang.reflect.*;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
//import org.apache.harmony.test.stress.classloader.share.ConstantPoolClasses.*;

public class testCPLmt_0 extends TestCase {

    public static final int ARRSIZE = 2000;

    public static final int LOOPCNT = 10000;

    public int test1(String[] args) {

        //boolean pass = true;
        String pkgN = args.length <= 0 ? this.getClass().getPackage().getName()
                : args[0];
        int cntLoop = args.length <= 1 ? LOOPCNT : Integer.valueOf(args[1])
                .intValue();

        Class[] arClss;
        Object[] objs;
        Field[][] ff;
        Field[] ff1;
        int sm = 0;
        for (int t = 0; t < cntLoop; t++) {
            arClss = new Class[ARRSIZE];
            objs = new Object[ARRSIZE];
            ff = new Field[ARRSIZE][10];
            sm = 0;

            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    arClss[i] = Class.forName(pkgN.concat(".").concat(
                            "testCPLmt_C1"));

                    ff1 = arClss[i].getDeclaredFields();

                    if (ff1.length != 10) {
                        ReliabilityRunner.debug("Failed: " + arClss[i].toString() + "  "
                                + ff1.length);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    ff[i] = ff1;
                    objs[i] = arClss[i].newInstance();
                    for (int n = 0; n < ff[i].length; n++) {
                        if (i % 2 == 0) {
                            ff[i][n].setInt(objs[i], n + 1);
                            sm += (n + 1);
                        } else {
                            ff[i][n].setInt(objs[i], ff[i].length - n);
                            sm += ff[i].length - n;
                        }
                    }
                } catch (Throwable e) { 
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            int sm1 = 0;
            for (int i = 0; i < ARRSIZE; i++) {
                for (int n = 0; n < ff[i].length; n++) {
                    try {
                        sm1 += ff[i][n].getInt(objs[i]);
                    } catch (Throwable e) {
                        ReliabilityRunner.debug(e.toString());
                        ReliabilityRunner.mainTest.addError(this, e);
                    }
                }

            }
            if (sm1 != sm) {
                ReliabilityRunner.debug("Unexpected result: sm= " + sm + "  sm1=" + sm1);
                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
            }
            ff = null;
            arClss = null;

        }
        return ReliabilityRunner.RESULT_PASS;
    }

    public void test() {
        ReliabilityRunner.debug("Not supported");
        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
    }
}
