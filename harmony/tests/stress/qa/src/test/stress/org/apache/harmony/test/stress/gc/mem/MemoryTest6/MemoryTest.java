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

/*
 * @author Alexander V. Esin
 * @version $Revision: 1.5 $
 * Created on 29.11.2005
 * 
 */
package org.apache.harmony.test.stress.gc.mem.MemoryTest6;

import junit.framework.TestCase;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class MemoryTest extends TestCase {

    // sum should be 8 x 512 K = 8 x 512 x 1024 = 4194304;
    private final static int[] arraySizes = { 4, 100, 200, 4000, 90000, 100000,
            1000000, 3000000 };

    private final static int NUM = 1024;

    private byte[][] arrays = new byte[NUM][];

    int totalSize = 0;

    public void test() {
        int i = 0;
        try {
            for (; i < NUM; ++i) {
                int size = arraySizes[(i + 8) % 8];
                totalSize += size;
                arrays[i] = new byte[size];
            }
        } catch (OutOfMemoryError e) {
            ReliabilityRunner.debug("OutOfMemoryError is thrown on iteration "
                    + i + ". Total size = " + totalSize);
            ReliabilityRunner.mainTest.addError(this, e);
        }
    }

}
