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

package org.apache.harmony.test.reliability.api.kernel.string;

import org.apache.harmony.test.reliability.share.Test;

/*
 * Goal: find resource leaks or other problems caused by invocation of String.intern() method.
 *
 * Idea: being run in a cycle in a single JVM the test will lead to creation big number of different strings 
 *         references to which are lost and they can be finalized and removed from a pull of internal string representations.
 *         There can be failure caused by operations done in intern() and internal representation clean-up by GC.
 *
 * The test does:
 *    1. Creates a string.
 *    2. Invokes intern().
 *
 */



public class StringInternTest extends Test {

    public int NUMBER_OF_ITERATIONS = 1000;

    String s = "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww";

    public static void main(String[] args) {
        System.exit(new StringInternTest().test(args));
    }

    public int test(String[] params) {
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            String str = String.valueOf(i) + s;
            str.intern();
        }
        return pass("OK");
    }

}

