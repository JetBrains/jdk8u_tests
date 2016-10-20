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
 * @version: $Revision: 1.7 $
 */

package org.apache.harmony.test.stress.classloader.share.LargeClassName;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.*;

public class testLCN_R extends TestCase {

    public static final int ARRSIZE = 1000;

    public static final String TESTNN = "testLCN";

    public static final int LOOPCNT = 1000;

    public static final int PASSANS = 104;

    public static boolean logInfo = true;

    private static final String S_APP = "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeee";

    public void test() {
        // boolean pass = true;
        final int ARGS_COUNT = 3;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.share."
                            + "LargeClassName.testLCN_R.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - number of iterations
        // testArgs[1] - number of opened classes
        // testArgs[2] - mode (one classloader or many will be used)

        int cntLoop = testArgs.length <= 0 ? LOOPCNT : Integer.valueOf(
                testArgs[0]).intValue();
        int cntClss = testArgs.length <= 1 ? ARRSIZE : Integer.valueOf(
                testArgs[1]).intValue();

        int mode = testArgs.length <= 2 ? 0 : Integer.valueOf(testArgs[2])
                .intValue();

        if (mode == 1) {
            cntLoop = 5;
        }
        if (cntLoop > LOOPCNT) {
            cntLoop = LOOPCNT;
        }
        if (cntClss > ARRSIZE) {
            cntClss = ARRSIZE;
        }

        if (logInfo) {
            ReliabilityRunner.debug("Use classes with large name");
            ReliabilityRunner.debug("Number of used classes: " + cntClss);
            ReliabilityRunner.debug("Number of iterations: " + cntLoop);
        }
        Class[] arrClss;
        Object[] arrObjs;
        Field[][] ff;

        String pkgN = this.getClass().getPackage().getName();
        String name = "";
        String name1 = "";
        Class firstCl = null;
        String s = S_APP;
        int smThrow = 0;
        byte[] bbb1 = new byte[11];
        byte[] bbb2 = new byte[500];
        int lastBytes = 0;
        try {
            name = pkgN.concat(".").concat(TESTNN);
            firstCl = Class.forName(name);

            InputStream is = firstCl.getResourceAsStream(TESTNN
                    .concat(".class"));
            if (is == null) {
                ReliabilityRunner.debug("Can not open InputStream : " + TESTNN);
                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
            }
            DataInputStream dIn = new DataInputStream(is);
            dIn.read(bbb1, 0, 11);
            dIn.readUTF();
            lastBytes = dIn.read(bbb2, 0, bbb2.length);
            if (lastBytes <= 0) {
                ReliabilityRunner.debug("Incorrect reading value. Byte array is empty ");
                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
            }
            StringBuffer sBuf = new StringBuffer(TESTNN);
            for (int t = 0; t < 1300; t++) {
                sBuf.append(S_APP);
            }
            s = sBuf.toString();
            dIn.close();
        } catch (OutOfMemoryError e) {
            ReliabilityRunner.debug("Test failed: unexpected OutOfMemoryError during data reading");
            ReliabilityRunner.mainTest.addError(this, e);
        } catch (Throwable e) {
            ReliabilityRunner.debug("Creating byte array: unexpected error" + e.toString());
            ReliabilityRunner.mainTest.addError(this, e);
        }
        myClassLoader cLoad = new myClassLoader();

        for (int t = 0; t < cntLoop; t++) {
            arrClss = new Class[cntClss];
            arrObjs = new Object[cntClss];
            ff = new Field[cntClss][2];

            try {

                for (int i = 0; i < cntClss; i++) {
                    name = "ttt/".concat(s).concat(Integer.toString(i));
                    name1 = "ttt.".concat(s).concat(Integer.toString(i));

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    DataOutputStream dOut = new DataOutputStream(bos);
                    dOut.write(bbb1, 0, 11);
                    dOut.writeUTF(name);
                    dOut.write(bbb2, 0, lastBytes);
                    dOut.close();
                    byte[] b1 = bos.toByteArray();
                    try {
                        arrClss[i] = cLoad.defineKlass(name1, b1, 0, b1.length);
                    } catch (LinkageError e) {
                        smThrow++;
                    }
                    if (smThrow == 0) {
                        if (!arrClss[i].getName().equals(name1)) {
                            ReliabilityRunner.debug("Incorrect name step: " + i);
                            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                        }
                        arrObjs[i] = arrClss[i].newInstance();
                        ff[i] = arrClss[i].getFields();
                        for (int j = 0; j < ff[i].length; j++) {
                            ff[i][j].setInt(arrObjs[i], 777 + i + j);
                        }
                    }
                }
                if (smThrow == 0) {
                    for (int i = 0; i < cntClss; i++) {
                        for (int j = 0; j < ff[i].length; j++) {

                            if (ff[i][j].getInt(arrObjs[i]) != (777 + i + j)) {
                                ReliabilityRunner
                                        .debug("Incorrect value step: " + j
                                                + " field " + i + "   must be "
                                                + (777 + i + j) + "    but: "
                                                + ff[i][j].getInt(arrObjs[j]));
                                ReliabilityRunner.mainTest.addError(this,
                                        new ClassLoaderTestError());
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                ReliabilityRunner.debug(e.toString());
                ReliabilityRunner.mainTest.addError(this, e);
            }

            arrClss = null;
            arrObjs = null;
            ff = null;

            if (mode == 1) {
                cLoad = null;
                cLoad = new myClassLoader();
            }
        }
        if (smThrow != 0) {
            if (smThrow != (cntLoop * cntClss - cntClss)) {
                ReliabilityRunner.debug("Unexpected number of LinkageError: " + smThrow
                        + " must be: " + (cntLoop * cntClss - cntClss));
                ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
            }
        }
    }
}
