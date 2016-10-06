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
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.kernel.object;

import org.apache.harmony.test.reliability.share.Test;


/**
 * Goal: find resource leaks or intermittent failures caused by Object.finalize() operation.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize in a cycle
 *       2. Excutes a cycle of param[0] iterations, on each iteration:
 *            creates the new Finalizer object
 *    3. The finalize() (called by the garbage collector) does:  
 *                - invokes method in the finalize 
 *            - assigns the string to the class variable
 *            - invokes super.finalize()
 */

public class ObjectFinalizeTest extends Test {

    public int NUMBER_OF_ITERATIONS = 1000000;


    public static void main(String[] args) {
        System.exit(new ObjectFinalizeTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {

            new Finalizer();

            if (i % 10000 == 0) {
                System.gc();
                // log.add("Iteration: " + i);
            }
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }

    }

}

class Finalizer extends Thread {
    String str = "";
    int id;

    String toStr() {
        return Integer.toString(id++);
    }

    Finalizer() {
        str = toStr();
    }
    public void finalize() throws Throwable {
        str = toStr();
        super.finalize();
    }

}
