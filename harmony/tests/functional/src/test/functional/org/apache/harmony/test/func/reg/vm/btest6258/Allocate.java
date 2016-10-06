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
package org.apache.harmony.test.func.reg.vm.btest6258;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * checks if the 128k object allocation
 * can allocate 120 M (less than default heap maximum 256Mb).
 *
 */
public class Allocate extends RegressionTest {

    final static int Mb = 1048576;
    final static int kb = 1024;

    public static void main(String[] args) {
        System.exit(new Allocate().test(Logger.global, args)); 
    }

    public int test(Logger logger, String[] args) {

        int target = 120*Mb;
        int block_size = 128*kb - 4096 - 12 + 1;

        logger.info("allocating " + mb(target) + " Mb");
        logger.info(" in byte[" + block_size + "] blocks, " 
            + (block_size + 12 + 4096) + " bytes each");

        int total = 0;
        ArrayList a = new ArrayList();
        try {
            while (total < target) {
                a.add(new byte[block_size]);
                total += block_size + 12 + 4096;
            }
            logger.info("PASSED, " + mb(total) + " Mb allocated");
            return pass();
        } catch (OutOfMemoryError e) {
            logger.info("FAILED, allocated " + mb(total) + " Mb and got " + e);
            return fail();
        }
    }

    /** converts byte size to megabyte size with rounding. */
    public static int mb(int a) {
        return (a + Mb/2) / Mb;
    }
}
