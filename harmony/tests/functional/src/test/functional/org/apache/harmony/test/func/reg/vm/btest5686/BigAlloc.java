/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.reg.vm.btest5686;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * checks if the it is possible to allocate large byte array
 *
 */
public class BigAlloc extends RegressionTest {
    public static void main(String[] args) {
        System.exit(new BigAlloc().test(Logger.global, args)); 
    }

    public int test(Logger logger, String[] args) {
        try {
            final int Mb = 1048576;
            byte[] array = new byte[128*Mb];
            // array allocated successfully,
            // now check for its validity
            // by writing to the start and to the end of the array
            array[0] = 1;
            array[array.length-1] = 2;
            return pass();
        } catch(OutOfMemoryError e) {
            // Can't allocate large byte array,
            // probably heap resize didn't work out
            return fail();
        }
    }
}
