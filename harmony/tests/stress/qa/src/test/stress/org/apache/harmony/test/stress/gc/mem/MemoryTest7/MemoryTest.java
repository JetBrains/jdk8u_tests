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
package org.apache.harmony.test.stress.gc.mem.MemoryTest7;

import junit.framework.TestCase;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 */
public class MemoryTest extends TestCase {

    private final static int NUM = 1024;

    private byte[][] arrays1 = new byte[1024 * NUM][];

    private byte[][] arrays2 = new byte[NUM][];

    int totalSize = 0;

    public void test() {

        int i = 0;
        try {
            for (; i < (1024 * NUM); ++i) {
                totalSize += 1024;
                arrays1[i] = new byte[1024];
            }
        } catch (OutOfMemoryError e) {
            ReliabilityRunner.debug("OutOfMemoryError is thrown on iteration "
                    + i + " while eating memory by 1024 bytes. Total size = "
                    + totalSize);
            ReliabilityRunner.mainTest.addError(this, e);
        }
        ReliabilityRunner.debug("allocation phase by 1024 bytes is completed");
        arrays1 = null;
        totalSize = 0;
        i = 0;
        try {
            for (; i < NUM; ++i) {
                totalSize += 1048576;
                arrays2[i] = new byte[1048576]; // 1024 K = 1 M
            }
        } catch (OutOfMemoryError e) {
            ReliabilityRunner.debug("OutOfMemoryError is thrown on iteration "
                    + i + " while eating memory by 1 MB. Total size = "
                    + totalSize);
            ReliabilityRunner.mainTest.addError(this, e);
        }
    }

}
