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

package org.apache.harmony.test.reliability.api.zip;

import org.apache.harmony.test.reliability.share.Test;
import java.util.zip.Deflater;

/**
 * Goal: find memory leaks caused by compressor with the default compression level.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize a new compressor in a cycle
 *    2. starts a cycle of param[0] iterations. On each iteration:
 *          - Creates a new compressor with the default compression level
 *          - runs System.gc()
 *
 */

public class ZlibDeflaterTest extends Test {

    public int callSystemGC = 1;

    public int NUMBER_OF_ITERATIONS = 5000;

    public static void main(String[] args) {
        System.exit(new ZlibDeflaterTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        for (int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {

            //if (i % 1000 == 0) {
            //    log.add("Iteration: " + i);
            //}

            new Deflater();

            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }

            if (callSystemGC != 0) {
                System.gc();
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

