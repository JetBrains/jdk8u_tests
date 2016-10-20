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

package org.apache.harmony.test.stress.misc.stressloads;

import junit.framework.TestCase;
import java.util.Vector;
import java.util.StringTokenizer;

import org.apache.harmony.test.stress.misc.MiscTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Alexander D. Shipilov
 * @version $Revision: 1.8 $
 */

public class StressLoadsRunner extends TestCase {

    /* Stress loads to run. */
    static String loads = null;

    /* Stress tests to run. */
    static String tests = null;

    public static StressLoadsRunner loadsRunnerTest;

    public static void main(String[] args) {
        new StressLoadsRunner().test();
    }

    public void test() {
        
        loadsRunnerTest = this;

        StringTokenizer parser = null;

        int workTime = Integer.getInteger(
                "org.apache.harmony.test.share.stress."
                        + "ReliabilityRunner.timeToWork", 10000).intValue();

        loads = System.getProperty("org.apache.harmony.test."
                + "stress.misc.stressloads.StressLoadsRunner.loads");

        tests = System.getProperty("org.apache.harmony.test."
                + "stress.misc.stressloads.StressLoadsRunner.tests");

        // build Vector of stress loads classes
        Vector classes = new Vector();
        if (loads != null) {
            loads = loads.trim();
            parser = new StringTokenizer(loads, ",");
        } else {
            ReliabilityRunner.debug("Error: Stress loads list is undefined.");
            ReliabilityRunner.mainTest.addError(this, new MiscTestError());
        }

        try {
            while (parser.hasMoreTokens()) {
                String cl = parser.nextToken();
                if (cl != null) {
                    classes.add((Thread) (Class
                            .forName("org.apache.harmony.test.stress.misc."
                                    + "stressloads.StressLoadsThread" + cl))
                            .newInstance());
                }
            }

            if (tests != null) {
                tests = tests.trim();
                parser = new StringTokenizer(tests, ",");
                while (parser.hasMoreTokens()) {
                    String cl = parser.nextToken();
                    if (cl != null) {
                        classes.add((Thread) (Class.forName(cl)).newInstance());
                    }
                }
            }
        } catch (ClassNotFoundException c) {
            ReliabilityRunner.debug("Can't load class: " + c.getMessage());
            ReliabilityRunner.mainTest.addError(this, c);
            return;
        } catch (Throwable t) {
            ReliabilityRunner
                    .debug("Unexpected exception during threads creation: "
                            + t.toString());
            ReliabilityRunner.mainTest.addError(this, t);
            return;
        }

        try {
            ReliabilityRunner.debug("Tests to run:");
            for (int i = 0; i < classes.size(); i++) {
                ReliabilityRunner.debug(classes.elementAt(i).getClass()
                        .getName());
            }
            ReliabilityRunner.debug("Time to work: " + workTime);
        } catch (Throwable t) {
            ReliabilityRunner.debug("Unexpected exception during logging: "
                    + t.toString());
            ReliabilityRunner.mainTest.addError(this, t);
            return;
        }

        try {
            for (int i = 0; i < classes.size(); i++) {
                Thread toRun = (Thread) classes.elementAt(i);
                toRun.setDaemon(true);
                toRun.start();
            }
        } catch (Throwable t) {
            ReliabilityRunner
                    .debug("Unexpected exception during threads start: "
                            + t.toString());
            ReliabilityRunner.mainTest.addError(this, t);
            return;
        }

        try {
            Thread.sleep(workTime);
        } catch (Throwable t) {
            ReliabilityRunner.debug("Unexpected exception during sleep: "
                    + t.toString());
            ReliabilityRunner.mainTest.addError(this, t);
            return;
        }
    }
}
