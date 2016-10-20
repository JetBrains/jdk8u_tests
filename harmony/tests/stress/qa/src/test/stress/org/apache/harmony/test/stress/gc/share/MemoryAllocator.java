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

package org.apache.harmony.test.stress.gc.share;

import org.apache.harmony.test.share.stress.ReliabilityRunner;

import junit.framework.TestCase;

public class MemoryAllocator extends TestCase {
    private static byte[] byteArray;

    private static int FREE_MEMORY = 1048576; // 1 MB by default

    public void testAllocateMemory() {
        FREE_MEMORY = Integer.getInteger(
                "org.apache.harmony.test.stress.gc.share."
                        + "MemoryAllocator.FREE_MEMORY", FREE_MEMORY).intValue();
        try {
            byteArray = new byte[(int) (Runtime.getRuntime().freeMemory())
                    - FREE_MEMORY];
            System.out.println("Memory allocated. Free memory: "
                    + Runtime.getRuntime().freeMemory() + " bytes");
        } catch (OutOfMemoryError er) {
            byteArray = null;
            ReliabilityRunner.mainTest.addError(this, er);
        }
    }
}
