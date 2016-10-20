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

package org.apache.harmony.test.stress.classloader.share.WrongClasses;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

public class testCFE_RBytes extends TestCase {

    public testCFE_RBytes() {
        super();
        random = new Random();
    }

    private Random random;

    public static final int LOOPCNT = 10000;

    private static final char[] SEPARATOR = { File.separatorChar };

    private static final String TESTNN = "testCFE_";

    public static boolean logIn = true;

    public void test() {
        final int ARGS_COUNT = 3;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.share."
                            + "WrongClasses.testCFE_RBytes.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - number of iterations
        // testArgs[1] - number of used classes
        // testArgs[2] - classes location

        //int notPass = 0;

        int cntLoop = testArgs.length <= 0 ? LOOPCNT : Integer.valueOf(
                testArgs[0]).intValue();

        int cntClss = testArgs.length <= 1 ? 0 : Integer.valueOf(testArgs[1])
                .intValue();

        String path = testArgs.length <= 2 ? null : testArgs[2];
        if (cntClss == 0) {
            cntClss = 5;
        }
        if (path == null) {
            ReliabilityRunner.debug("Class files location is not defined");
            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
        }
        if (logIn) {
            ReliabilityRunner.debug("Try to load classes with wrong format.");
            ReliabilityRunner.debug("Number of used classes: " + cntClss);
            ReliabilityRunner.debug("Number of iterations: " + cntLoop);
        }
        int smCFE = 0;
        int smNCFE = 0;
        String sep = new String(SEPARATOR);

        byte[] firstBytes = new byte[500];
        byte[] lastBytes = new byte[500];
        int n1 = 0;
        int n2 = 0;
        String pkgN = this.getClass().getPackage().getName();
        pkgN = pkgN.replace('.', SEPARATOR[0]);

        pkgN = path.concat(sep).concat(pkgN).concat(sep);

        myClassLoader cLoad = new myClassLoader();
        byte[] randomBytes = new byte[100];

        try {
            for (int i = 0; i < cntClss; i++) {
                File f = new File(pkgN.concat(TESTNN).concat(
                        Integer.toString(i%5)).concat(".class"));
                byte b[] = new byte[(int) f.length()];
                FileInputStream stream = new FileInputStream(f);
                stream.read(b);
                stream.close();
                ByteArrayInputStream is = new ByteArrayInputStream(b);
                DataInputStream dIn = new DataInputStream(is);
                ByteArrayOutputStream os = null;
                DataOutputStream dOut = null;

                n1 = dIn.read(firstBytes, 0, 11);
                dIn.readUTF();
                n2 = dIn.read(lastBytes, 0, lastBytes.length);
                dIn.close();
                for (int j = 0; j < cntLoop; j++) {
                    random.nextBytes(randomBytes);
                    String name = TESTNN.concat(new String(randomBytes, 0,
                            randomBytes.length));
                    os = new ByteArrayOutputStream();
                    dOut = new DataOutputStream(os);
                    dOut.write(firstBytes, 0, n1);
                    dOut.writeUTF(name);
                    dOut.write(lastBytes, 0, n2);
                    dOut.close();
                    byte[] bRes = os.toByteArray();
                    try {
                        cLoad.defineKlass(name, bRes, 0, bRes.length);
                        ReliabilityRunner.debug("Test failed: ClassFormatError or NoClassDefFoundError must be thrown");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    } catch (ClassFormatError e) {
                        smCFE++;
                    } catch (NoClassDefFoundError e) {
                        smNCFE++;
                    } catch (OutOfMemoryError e) {
                        ReliabilityRunner.debug(e.toString());
                        ReliabilityRunner.mainTest.addError(this, e);
                    } catch (Throwable e) {
                        ReliabilityRunner.debug("Test failed: unexpected error "
                                + e
                                + " instead of ClassFormatError or NoClassDefFoundError");
                        ReliabilityRunner.mainTest.addError(this, e);
                    }
                }
            }
        } catch (Throwable e) {
            ReliabilityRunner.debug(e.toString());
            ReliabilityRunner.mainTest.addError(this, e);
        }
        if ((smCFE + smNCFE) != (cntLoop * cntClss)) {
            ReliabilityRunner.debug("testCFE_RBytes: incorrect number of errors "
                    + (smCFE + smNCFE) + "   must be " + (cntLoop * cntClss));
            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
        }
    }
}
