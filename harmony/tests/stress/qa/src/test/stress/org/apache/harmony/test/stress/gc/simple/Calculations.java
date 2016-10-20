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

package org.apache.harmony.test.stress.gc.simple;

import org.apache.harmony.test.stress.gc.share.GCTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

import junit.framework.TestCase;

/**
 * Produce some calculations based on current time.
 * 
 * @author Alexander D. Shipilov
 * @version $Revision: 1.6 $
 * 
 */
public class Calculations extends TestCase {

    /**
     * Perform some calculations
     */
    public void testCalculations() {
        long j = System.currentTimeMillis();
        try {
            Thread.sleep(10);
        } catch (InterruptedException exc) {
            ReliabilityRunner.debug(exc.toString());
            ReliabilityRunner.mainTest.addError(this, exc);
        }
        long k = System.currentTimeMillis();
        long i = k;
        i -= j;
        if (i != (k - j)) {
            ReliabilityRunner.debug("Calculations failed");
            ReliabilityRunner.mainTest.addError(this, new GCTestError());
        }
        ReliabilityRunner.debug("Calculations performed");
    }
}
