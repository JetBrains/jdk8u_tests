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

package org.apache.harmony.test.stress.classloader.classunloading;

import java.lang.reflect.Field;
import java.util.List;
import java.util.LinkedList;
import junit.framework.TestCase;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.stress.classloader.share.CommonClassLoader;

/**
 * @author Alexander D. Shipilov
 * @version $Revision: 1.10 $
 */

public class ManyThreads extends TestCase {
    public static Object ballast_object2 = (Object) (new int[100][10][10]);

    public void test() {

        /* Input parameters. */
        int numberOfThreads = 1000; // threads to run
        int j = 0; // count

        StressClassunloading02Thread[] testThreadsArray = null;

        Object ballast_object1 = (Object) (new int[100][10][10]);

        numberOfThreads = Integer.getInteger(
                "org.apache.harmony.test."
                        + "stress.classloader.classunloading."
                        + "ManyThreads.numberOfThreads", 1000).intValue();

        ReliabilityRunner.debug("Number of threads: " + numberOfThreads);

        List arrayOfObjects = new LinkedList();

        try {
            /* Creating threads. */
            testThreadsArray = new StressClassunloading02Thread[numberOfThreads];

            for (int i = 0; i < numberOfThreads; i++) {
                testThreadsArray[i] = new StressClassunloading02Thread();
            }

        } catch (OutOfMemoryError er) {
            ballast_object2 = null;
            ReliabilityRunner.debug("Too big value of threads");
            ReliabilityRunner.mainTest.addError(this, er);
        }

        /* Padding memory. */
        ReliabilityRunner.debug("Padding memory...");
        try {
            for (j = 0; true; j++) {
                arrayOfObjects.add((Object) (new int[100][10][10]));
            }
        } catch (OutOfMemoryError oome) {
            ballast_object1 = null;
        }

        if (numberOfThreads > j) {
            arrayOfObjects = null;
            ReliabilityRunner.debug("Warning: too big value of number of threads."
                    + " Memory can not contain it.");
            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
        }

        try {
            /* Realising some memory. */
            for (int i = 0; i < numberOfThreads; i++) {
                arrayOfObjects.remove(0);
            }

            ReliabilityRunner.debug("Some memory released.");

            /* Starting threads. */
            ReliabilityRunner.debug("Loading classes...");

            for (int i = 0; i < numberOfThreads; i++) {
                testThreadsArray[i].start();
            }

            /* Wait while all threads finish their work. */
            for (int i = 0; i < numberOfThreads; i++) {
                testThreadsArray[i].join();
            }

        } catch (OutOfMemoryError er) {
            ballast_object2 = null;
            ReliabilityRunner.debug("Warning: too small value of number of threads.");
            ReliabilityRunner.mainTest.addError(this, er);
        } catch (Throwable t) {
            t.printStackTrace();
            ReliabilityRunner.mainTest.addError(this, t);
        }

        /* For now memory should be prepare to contain padding objects. */
        ReliabilityRunner.debug("Checking that classes unloaded...");
        try {
            for (j = 0; j < (numberOfThreads - 1); j++) {
                arrayOfObjects.add(j, (Object) (new int[100][10][10]));
            }
        } catch (OutOfMemoryError oome) {
            arrayOfObjects = null;
            ReliabilityRunner.debug("OutOfMemoryError thrown (fail). " + (j) + " of "
                    + (numberOfThreads - 1) + " objects contained.");
            ReliabilityRunner.mainTest.addError(this, oome); 
        }

        arrayOfObjects = null;
    }
}

/*
 * Thread which creates classloader, load classes and unloads them.
 */
class StressClassunloading02Thread extends Thread {

    public void run() {

        CommonClassLoader testClassLoader;
        Class testClass = null;

        /* Load classes. */
        try {
            testClassLoader = new CommonClassLoader();
            testClass = testClassLoader.loadClass("org"
                    + "/apache/harmony/test/stress/classloader/classunload"
                    + "ing/share/BigClass.class",
                    "org.apache.harmony.test.stress.classloader.classunloading"
                            + ".share.BigClass");
        } catch (OutOfMemoryError er) {
            ManyThreads.ballast_object2 = null;
        } catch (Throwable t) {
            t.printStackTrace();
        }

        /* Static initiate classes. */
        try {
            Field testField = testClass.getField("hi");
            int initiator = testField.getInt(testField);
            testField = null;
        } catch (OutOfMemoryError er) {
            ManyThreads.ballast_object2 = null;
        } catch (Throwable t) {
            t.printStackTrace();
        }

        /* Delete class loader and unload classes. */
        testClassLoader = null;
        testClass = null;
    }
}
