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
 * Created on 22.12.2005
 * 
 */
package org.apache.harmony.test.stress.gc.chain.ChainFinalizeTest2;

import org.apache.harmony.test.stress.gc.chain.share.*;
import junit.framework.TestCase;

import org.apache.harmony.test.stress.gc.share.GCTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 */
public class ChainTest extends TestCase {

    private final static int NUM = 1024;

    boolean finFlag = false;

    public void test() {
        int i = 0;
        int j = 0;
        ChainFinalizeEx chain = null;
        try {
            for (; i < NUM; ++i) {
                j = 0;
                for (; j < NUM; ++j) {
                    ChainFinalizeEx next = new ChainFinalizeEx(this);
                    next.prev = chain;
                    chain = next;
                }
                chain = null;
                System.gc();
            }
        } catch (OutOfMemoryError e) {
            ReliabilityRunner.debug("OutOfMemoryError is thrown on iteration " + j + ", try " + i);
            ReliabilityRunner.mainTest.addError(this, e);
        }

        if (!finFlag) {
            ReliabilityRunner.debug("Non finalize function was invoked.");
            ReliabilityRunner.mainTest.addError(this, new GCTestError());
        }
    }

    class ChainFinalizeEx extends ChainFinalize {
        ChainTest test = null;

        public ChainFinalizeEx(ChainTest test) {
            super();
            this.test = test;
        }

        protected void finalize() {
            super.finalize();
            test.finFlag = true;
        }
    }
}
