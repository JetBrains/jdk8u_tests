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

package org.apache.harmony.test.stress.classloader.unloading;

import java.util.Vector;
import java.util.logging.Logger;

import org.apache.harmony.test.share.stress.ReliabilityRunner;

import junit.framework.TestCase;

/**
 * @author Nikolay Chugunov
 * @version: $Revision: 1.4 $
 */
public class ComplexTest extends TestCase {
    private final Vector classLoaders           = new Vector();
    static Logger        log                    = Logger.global;
    // isStop is used only by testMultiThread
    private boolean      isStop                 = false;
    private long   memoryBefore;
    private final long   totalMemory            = Runtime.getRuntime()
                                                    .totalMemory();
    private final long   niose                  = 10000;
    private Vector       threads                = new Vector();
    int                  numberOfElements;
    static int           numberOfElementInArray;
    private static int   numberOfStartedOfTests = 0;

    protected void setUp() throws Exception {
        numberOfStartedOfTests++;
        numberOfElements = Integer.parseInt(System
            .getProperty("stresstests.numberOfElements"));
        numberOfElementInArray = (int)totalMemory / numberOfElements;
        assertTrue(totalMemory < Integer.MAX_VALUE);
        assertEquals(Runtime.getRuntime().maxMemory() + "", totalMemory + "");
        System.gc();
        memoryBefore= Runtime.getRuntime().freeMemory();
        log.info("Total memory: " + totalMemory);
        log.info("Free memory before: " + memoryBefore);
    }

    public void testOneThread() throws Exception {

        try {
            while (ReliabilityRunner.isActive()) {
                ClassLoader classLoader = new SimpleClassLoader();
                classLoaders.add(classLoader);
                classLoader.loadClass(LoadedClass.class.getName())
                    .newInstance();
            }
        } catch (OutOfMemoryError e) {
        }
        verify(20000 * numberOfElements);
    }

    public void testMultiThread() throws Exception {
        // numberOfElementInArray = (int)totalMemory / numberOfElements;
        // log.info(numberOfElementInArray + "");
        try {
            while (ReliabilityRunner.isActive() && !isStop) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        try {
                            ClassLoader classLoader = new SimpleClassLoader();
                            classLoaders.add(classLoader);
                            classLoader.loadClass(LoadedClass.class.getName())
                                .newInstance();
                        } catch (OutOfMemoryError e) {
                            isStop = true;
                        } catch (Throwable e) {
                            isStop = true;
                            assertTrue(false);
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
                threads.add(thread);
            }
        } catch (OutOfMemoryError e) {
            isStop = true;
        }
        classLoaders.removeAllElements();
        for (int i = 0; i < threads.size(); i++) {
            ((Thread)threads.get(i)).join();
        }
        verify(60000 * numberOfElements);
    }

    protected void tearDown() throws Exception {
        if (numberOfStartedOfTests == 2) {
            System.exit(ReliabilityRunner.RESULT_PASS);
        }
    }

    private void verify(double error) {
        classLoaders.removeAllElements();
        System.gc();
        long memoryAfter = Runtime.getRuntime().freeMemory();
        log.info("Free memory after: " + memoryAfter);
        log.info((memoryBefore - memoryAfter) + "");
        log.info((error + niose) + "");
        assertTrue(memoryBefore - memoryAfter < error + niose);
    }
}
