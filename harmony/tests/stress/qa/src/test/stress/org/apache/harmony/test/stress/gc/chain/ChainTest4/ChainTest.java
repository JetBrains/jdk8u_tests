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
 * @version $Revision: 1.6 $
 * Created on 29.11.2005
 * 
 */
package org.apache.harmony.test.stress.gc.chain.ChainTest4;

import org.apache.harmony.test.stress.gc.share.GCTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.gc.chain.share.*;
import junit.framework.TestCase;

/**
 */
public class ChainTest extends TestCase {

    public void test() {
        Chain chain = null;
        int i = 0;
        try {
            for (; i < Integer.MAX_VALUE; ++i) {
                Chain next = new Chain();
                next.prev = chain;
                chain = next;
                System.gc();
            }
            ReliabilityRunner.debug("OutOfMemoryError is not thrown");
            ReliabilityRunner.mainTest.addError(this, new GCTestError());
        } catch (OutOfMemoryError e) {
            ReliabilityRunner.debug("OutOfMemoryError is thrown on iteration " + i);
        }
    }
}
