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
 * @version $Revision: 1.9 $
 */

public class OneThread extends TestCase {

    public void test() {

        /* Input parameters. */
        int numberOfClasses = 1000; // number of classes
        int j = 0; // count
        CommonClassLoader testClassLoadersArray[] = null;
        Class testClassesArray[] = null;

        Object ballast_object1 = (Object) (new int[100][10][10]);
        Object ballast_object2 = (Object) (new int[100][10][10]);

        numberOfClasses = Integer.getInteger(
                "org.apache.harmony.test."
                        + "stress.classloader.classunloading."
                        + "OneThread.numberOfClasses", 1000).intValue();

        ReliabilityRunner.debug("Number of classes to load: "
                + numberOfClasses);

        List arrayOfObjects = new LinkedList();

        try {
            /* Array of class loaders. */
            testClassLoadersArray = new CommonClassLoader[numberOfClasses];

            /* Array of classes. */
            testClassesArray = new Class[numberOfClasses];

            /* Creating class loaders. */
            for (int i = 0; i < numberOfClasses; i++) {
                testClassLoadersArray[i] = new CommonClassLoader();
                testClassesArray[i] = null;
            }
        } catch (OutOfMemoryError er) {
            ballast_object2 = null;
            ReliabilityRunner.debug("Too big value of number of classes");
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

        if (numberOfClasses > j) {
            arrayOfObjects = null;
            ReliabilityRunner.debug("Warning: too big value of number of classes."
                    + " Memory can not contain it.");
            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
        }

        try {
            /* Realising some memory. */
            for (int i = 0; i < numberOfClasses; i++) {
                arrayOfObjects.remove(0);
            }

            ReliabilityRunner.debug("Some memory released.");

            /* Loading classes. */
            ReliabilityRunner.debug("Loading classes...");

            for (int i = 0; i < numberOfClasses; i++) {
                testClassesArray[i] = testClassLoadersArray[i].loadClass("org"
                        + "/apache/harmony/test/stress/classloader/classunload"
                        + "ing/share/BigClass.class",
                        "org.apache.harmony.test.stress.classloader.classunloading"
                                + ".share.BigClass");
            }
        } catch (OutOfMemoryError er) {
            ballast_object2 = null;
            ReliabilityRunner.debug("Warning: too small value of number of classes.");
            ReliabilityRunner.mainTest.addError(this, er);
        } catch (Throwable t) {
            t.printStackTrace();
            ReliabilityRunner.mainTest.addError(this, t);
        }

        /* Static initiating classes. */
        ReliabilityRunner.debug("Initiating classes...");
        int initiator;
        Field testField;
        try {
            for (int i = 0; i < numberOfClasses; i++) {
                testField = testClassesArray[i].getField("hi");
                initiator = testField.getInt(testField);
            }
        } catch (OutOfMemoryError er) {
            ballast_object2 = null;
        } catch (Throwable t) {
            t.printStackTrace();
            ReliabilityRunner.mainTest.addError(this, t);
        }

        /* Deleting class loaders and unloading classes. */
        ReliabilityRunner.debug("Deleting class loaders and unloading classes...");
        for (int i = 0; i < numberOfClasses; i++) {
            testClassLoadersArray[i] = null;
            testClassesArray[i] = null;
        }

        /* For now memory should be prepare to contain padding objects. */
        ReliabilityRunner.debug("Checking that classes unloaded...");
        try {
            for (j = 0; j < (numberOfClasses - 1); j++) {
                arrayOfObjects.add(j, (Object) (new int[100][10][10]));
            }
        } catch (OutOfMemoryError oome) {
            arrayOfObjects = null;
            ReliabilityRunner.debug("OutOfMemoryError thrown (fail). " + (j) + " of "
                    + (numberOfClasses - 1) + " objects contained.");
            ReliabilityRunner.mainTest.addError(this, oome);
        }

        arrayOfObjects = null;
    }
}
