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

package org.apache.harmony.test.share.stress.generator;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.share.stress.util.CriticalSectionController;
import org.apache.harmony.test.share.stress.util.Parser;

/**
 * Class <code>Generator</code> is a main part of stressing system. It's heirs
 * could performs tests and generators, stored in <code>ParametersStorage</code>.
 * 
 * @author Alexander D. Shipilov
 * @version $Revision: 1.11 $
 */
public abstract class Generator extends TestCase {
    protected String params = null;
    
    private static int[] objectToRelease = new int[100];

    public void testGenerator() {
    }

    /**
     * Receives a string with list of activities from
     * <code>ParametersStorage.storage</code> hash map. This string defines
     * the configuration of tests to be run.
     * 
     * Example of string:
     * <code>generator.Thread { generator.Loop { Test1 {}}} generator.Thread { generator.Loop { Test1 {}}}</code>
     * 
     */
    public void runTest() {
        /* Get parameters as string */
        params = (String) ReliabilityRunner.storage.get(java.lang.Thread
                .currentThread());
        assertNotNull("Parameters not set", params);

        setContext();
    }

    /**
     * Produce context of test execution.
     * 
     */
    protected abstract void setContext();

    /**
     * Performs all tests in a list using <code>junit.textui.TestRunner</code>.
     * 
     */
    protected void execute() {
        try {
            String[][] listToRun = null;
            ReliabilityRunner.debug("Generator.execute()");
            /* Parse parameters */
            try {
                listToRun = Parser.generateToRunList(params);
            } catch (Exception exc) {
                ReliabilityRunner.debug("Exception " + exc.toString()
                        + " during parsing string: " + params);
                ReliabilityRunner.mainTest.addError(this, exc);
            }

            /* Run all tests */
            ClassNotFoundException expc = null;
            for (int i = 0; i < listToRun.length; i++) {
                Class classToRun = null;
                ReliabilityRunner.debug("class = " + listToRun[i][0]);
                for (int j = 0; j < ReliabilityRunner.packageList.length; j++) {
                    try {
                        classToRun = Class
                                .forName(ReliabilityRunner.packageList[j]
                                        + listToRun[i][0]);
                        break;
                    } catch (ClassNotFoundException exc) {
                        expc = exc;
                    }

                    try {
                        classToRun = Class
                                .forName(ReliabilityRunner.packageList[j] + "."
                                        + listToRun[i][0]);
                        break;
                    } catch (ClassNotFoundException exc) {
                        expc = exc;
                    }
                }

                if (classToRun == null) {
                    ReliabilityRunner.debug("Class " + listToRun[i][0]
                            + " not found in packages:");
                    for (int j = 0; j < ReliabilityRunner.packageList.length; j++) {
                        ReliabilityRunner.debug(ReliabilityRunner.packageList[j]);
                    }
                    ReliabilityRunner.mainTest.addError(this, expc);
                }

                String parametersAsString = "";
                for (int j = 1; j < listToRun[i].length; j++) {
                    parametersAsString = parametersAsString
                            .concat(listToRun[i][j] + " ");
                }
                ReliabilityRunner.storage.put(java.lang.Thread.currentThread(),
                        parametersAsString);
                ReliabilityRunner.debug(classToRun.getName() + " { "
                        + parametersAsString + "}");
                (new TestSuite(classToRun)).run(ReliabilityRunner.result);
            }
        } catch (OutOfMemoryError err) {
            CriticalSectionController.setCriticalSection(true);
            objectToRelease = null;
            System.gc();
            ReliabilityRunner.debug("Test has attained memory limit");
            ReliabilityRunner.result.stop();
        } catch (Throwable thr) {
            ReliabilityRunner.debug("Error during test generation");
            ReliabilityRunner.mainTest.addError(this, thr);
        }
    }
}
