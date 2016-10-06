/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Nikolay Bannikov
 * @version $Revision: 1.2 $
 */

package org.apache.harmony.test.reliability.api.kernel.throwable;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Random;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 *  Goal: find resource leaks or intermittent errors caused by operations with Exceptions
 *        and methods of Throwable.
 *        
 *  Idea: A number of Threads is started, each starts recursive method which 
 *        throws/catches exceptions, prints stack trace into own PrintWriter,
 *        checks the length of exception cause chain.
 *
 *  The test does:
 *      Creates and runs a Thread which calls runRecursiveException(..) method AND starts 
 *      N_OF_THREADS of analoguous Threads.
 *          
 *      runRecursiveException(..) is recursive method 
 *      which throws/catches exceptions. Recursion depth is defined by RECURSION_DEPTH.
 *
 */

public class StackTraceExcptsTest extends Test {

    static final int N_OF_THREADS = 1;

    static final int RECURSION_DEPTH = 50;

    public static void main(String[] args) {
        System.exit(new StackTraceExcptsTest().test(args));
    }


    public int test(String[] params) {

        Thread t = new ExceptionThrowingThread(RECURSION_DEPTH, 0);
        t.start();

        try {
            t.join();
        } catch (InterruptedException ie) {
        }

        return pass("OK");
    }


}


class ExceptionThrowingThread extends Thread {

    int depth = 0;
    int id = -1;

    ExceptionThrowingThread(int depth, int number){
        this.depth = depth;
        this.id = number;
    }

    public void run() {

        if (depth == 0){
            return;
        }

        // System.out.println(" Thread depth: " + depth + ", ID: " + id);

        // Each Thread:
        //   - starts N_OF_THREADS of the this Thread type
        //   - starts exception-throwing runRecursiveException(..)

        Thread[] t = new Thread[StackTraceExcptsTest.N_OF_THREADS];

        for (int i = 0; i < t.length; ++i) {
            t[i] = new ExceptionThrowingThread(depth - 1, i);
            t[i].start();
        }

        try {

            // Start recursive exception-throwing method from this thread.

            runRecursiveException(depth - 1);

        } catch (RuntimeException e) {

            Throwable cause = e.getCause();

            // This is actually a check - length of chain of causing exceptions
            // should be 'depth - 2', if less, then, NPE will be thrown:

            for (int i = 0; i < depth - 2; ++i) {
                cause = ((RuntimeException) cause.getCause());
            }
        }

        for (int i = 0; i < t.length; ++i) {
            try {
                t[i].join();
            } catch (InterruptedException ie) {
            }
        }
            
    }

    public int runRecursiveException(int depth) {

        int i = 100;

        // The method calls itself recursively. On each recursion level it
        // expects (catches) RuntimeException thrown from lower level, then,
        // re-throws new RuntimeException higher, specifies caught exception as cause.

        if (depth > 0) {

            try {

                // R E C U R S I V E   C A L L:

                runRecursiveException(depth - 1);

            } catch (RuntimeException e) {

                // We create a PrintWriter just to call printStackTrace(..) and then loose 
                // reference to it. printStackTrace(..) is called to stimulate resource 
                // leaks or intermittent errors:

                PrintWriter pw = new PrintWriter(new StringWriter(), true);
                e.printStackTrace(pw);
                pw.flush();

                // Throw RuntimeException upper, specify caught from lower level exception as cause:

                RuntimeException re = new RuntimeException("" + depth + " caused by " + e.toString(), e);

                re.fillInStackTrace();

                throw re;
            }

        } else {

            // depth is 0, causing ArithmeticException (division by zero), depth can not be < 0.

            i = i / depth;
        }

        return i;
    }

}

