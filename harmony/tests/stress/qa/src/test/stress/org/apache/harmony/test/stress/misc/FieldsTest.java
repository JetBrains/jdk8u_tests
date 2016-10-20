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
 * @author Vladimir Nenashev
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.misc.MiscTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class FieldsTest extends TestCase {
    private static volatile long l;

    private static volatile double d;

    private static final int ITERATIONS_COUNT = 100000000;

    public void test() {
        new FieldsTestThread().start();
        new FieldsTestThread().start();
        new FieldsTestThread().start();
        for (int i = 0; i < ITERATIONS_COUNT; i++) {
            long longVar = l;
            if (longVar != 0 && longVar != -1) {
                ReliabilityRunner.debug("Unknown value in l = " + longVar
                        + " (i=" + i + ")");
                ReliabilityRunner.debug("Test failed");
                ReliabilityRunner.mainTest.addError(this, new MiscTestError());
            }

            double doubleVar = d;
            if (doubleVar != 0 && doubleVar != 1.234567890123e-123) {
                ReliabilityRunner.debug("Unknown value in doubleVar = "
                        + doubleVar + " (i=" + i + ")");
                ReliabilityRunner.debug("Test failed");
                ReliabilityRunner.mainTest.addError(this, new MiscTestError());
            }
        }
    }

    private class FieldsTestThread extends java.lang.Thread {
        public void run() {
            while (true) {
                l = 0;
                d = 0.;
                l = -1;
                d = 1.234567890123e-123;
            }
        }
    }
}
