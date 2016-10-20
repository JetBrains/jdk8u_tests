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

/**
 * @author Alexander D. Shipilov
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class LargeObjects47 extends TestCase {

    public void test() {
        int STRESS_LOAD = 10;
        STRESS_LOAD = Integer.getInteger(
                "org.apache.harmony.test."
                        + "stress.misc."
                        + "LargeObjects47.STRESS_LOAD", 10).intValue();

        for (int i = 0; i < STRESS_LOAD; i++) {
            try {
                new LargeObjects47lo();
            } catch (OutOfMemoryError er) {
                ReliabilityRunner.debug(".");
            }
        }
    }
}

class LargeObjects47lo {
    int testArray[][][] = new int[100000][100000][100000];
}
