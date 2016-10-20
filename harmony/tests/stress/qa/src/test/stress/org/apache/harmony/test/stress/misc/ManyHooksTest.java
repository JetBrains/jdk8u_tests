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

package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.4 $
 */

public class ManyHooksTest extends TestCase {

    private static int numberOfHooks = 200;

    public void test() {
        Runtime runtime = Runtime.getRuntime();

        numberOfHooks = Integer.getInteger(
                "org.apache.harmony.test."
                        + "stress.gc.frag.share."
                        + "ManyHooksTest.numberOfHooks", 200).intValue();

        /**
         * register specified number of shutdown hooks and exit
         */
        for (int i = 0; i < numberOfHooks; i++) {
            runtime.addShutdownHook(new HookThread());
        }
    }

    /**
     * private class implementing shutdown hook functionality
     */
    private static class HookThread extends Thread {

        /**
         * Number of shutdown hooks already executed
         */
        private static int count = 0;

        public void run() {
            synchronized (HookThread.class) {
                ++count;
            }

            if (count == numberOfHooks) {
                ReliabilityRunner.debug("PASSED: All shutdown hooks executed");
            } else if (count > numberOfHooks) {
                ReliabilityRunner.debug("FAILED: Shutdown hook counter exceeded max. value");
            }
        }
    }

}
