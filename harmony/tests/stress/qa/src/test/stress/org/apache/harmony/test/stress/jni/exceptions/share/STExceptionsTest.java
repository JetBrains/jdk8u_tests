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

package org.apache.harmony.test.stress.jni.exceptions.share;

import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.jni.share.STTest;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.6 $
 */

public abstract class STExceptionsTest extends STTest {

    /**
     * List of arrays padding the heap
     */
    private static java.util.List list;

    /**
     * Variable to reserve some memory to handle OOME correctly
     */
    private static int[] buffer;

    public void test() {
        int fillMemory = Integer.getInteger(
                "org.apache.harmony.test."
                        + "stress.jni.exceptions.share."
                        + "STExceptionsTest.fillMemory", 1).intValue();

        if (fillMemory == 1) {
            fillMemory();
        }
        super.test();
        if (fillMemory == 1) {
            freeMemory();
        }
    }

    protected static native int getCounter();

    protected static void fillMemory() {
        int objectSize = Integer.getInteger(
                "org.apache.harmony.test."
                        + "stress.jni.exceptions.share."
                        + "STExceptionsTest.objectSize", 1000).intValue();

        int bufSize = Integer.getInteger(
                "org.apache.harmony.test."
                        + "stress.jni.exceptions.share."
                        + "STExceptionsTest.bufSize", 10000).intValue();

        testLog("Main thread", "Padding memory...");
        buffer = new int[bufSize];
        list = new java.util.LinkedList();
        int i = 0;
        try {
            while (true) {
                list.add(new Object[objectSize]);
                list.add(new int[bufSize]);
                i += 2;
            }
        } catch (OutOfMemoryError e) {
            buffer = null;
        }
        testLog("Main thread", "Memory filled (" + i + " objects allocated)");
    }

    protected static void freeMemory() {
        list = null;
        System.gc();
    }
}
