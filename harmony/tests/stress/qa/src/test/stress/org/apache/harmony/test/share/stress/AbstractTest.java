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

package org.apache.harmony.test.share.stress;

import java.io.PrintStream;

import org.punit.assertion.Assert;
import org.punit.listener.FieldSetter;
import org.punit.runner.ConcurrentRunner;
import org.punit.runner.Runner;
import org.punit.type.Loop;
import org.punit.type.Test;
import org.punit.util.MemoryUtil;

/**
 * Class <code>ReliabilityTest</code> runs a PUnit stress test.
 * 
 * Test timeout in seconds is specified by <code>timeToWork</code> property.
 * After the timeout is expired, tests are signaled to stop gracefully.
 * 
 * @author Alexei Fedotov
 */
public abstract class AbstractTest implements Loop, Test {

    public static int concurrentCount = 8;

    public long timeToWork = 30000;

    public long toWork() {
        return timeToWork;
    }

    protected static Runner _runner;
    
    /**
     * Is called from the <code>main()</code> method of a test to run the test
     * in a stand-alone mode.
     */
    protected static void runTestClass() {
        _runner = new ConcurrentRunner(concurrentCount);
        _runner.addEventListener(new FieldSetter(System.getProperties()));

        try {
            _runner.run(getClassFromStack());
        } catch (Throwable t) {
            error(t);
        }

        // DRL harness fail and pass codes
        System.exit((_runner.testResult().failures().size() == 0) ? 104 : 105);
    }

    /**
     * @return a child class which calls the {@link #runTestClass()} method.
     */
    private static Class<?> getClassFromStack() {
        final int CLASS_STACK_POS = 4;

        final StackTraceElement ste = Thread.currentThread().getStackTrace()[CLASS_STACK_POS];
        final String testClassName = ste.getClassName();
        Assert.assertEquals("main", ste.getMethodName());

        try {
            return Class.forName(testClassName);
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }

    private static final PrintStream log = System.out;

    public static boolean debug = true;

    public static synchronized void debug(String msg) {
        if (debug) {
            MemoryUtil.releaseWilderness();
            log.println("[debug] " + msg);
            MemoryUtil.allocateWilderness();
        }
    }

    public static synchronized void error(Throwable t) {
        MemoryUtil.releaseWilderness();
        log.println("[error] Unexpected " + t);
        t.printStackTrace(log);
        MemoryUtil.allocateWilderness();

        // DRL harness error code
        System.exit(106);
    }

    public void setUpBeforeWatchers() throws Exception {
    }

    public void setUpAfterWatchers() throws Exception {
    }

    public void tearDownBeforeWatchers() throws Exception {
    }

    public void tearDownAfterWatchers() throws Exception {
    }    
}
