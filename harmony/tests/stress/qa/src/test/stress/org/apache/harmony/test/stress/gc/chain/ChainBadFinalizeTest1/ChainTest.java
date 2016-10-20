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
 * Created on 31.01.2006
 * 
 */
package org.apache.harmony.test.stress.gc.chain.ChainBadFinalizeTest1;

import org.apache.harmony.test.stress.gc.chain.share.*;
import junit.framework.TestCase;

import org.apache.harmony.test.stress.gc.share.GCTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 */
public class ChainTest extends TestCase {

    private final static int NUM = 1024;

    long finCount = 0l;

    public void test() {
        int i = 0;
        int j = 0;
        ChainFinalizeEx chain = null;
        try {
            for (; i < NUM; ++i) {
                chain = null;
                j = 0;
                for (; j < NUM; ++j) {
                    String s = "" + j;
                    ChainFinalizeEx next = new ChainFinalizeEx(this, s);
                    s = null;
                    next.prev = chain;
                    chain = next;
                }
            }
        } catch (OutOfMemoryError e) {
            ReliabilityRunner.debug("OutOfMemoryError is thrown on iteration " + j + ", try " + i);
            ReliabilityRunner.mainTest.addError(this, e);
        }

        /*
         * for(int k = 0; k < 1000; k++) { //small delay Thread.yield(); }
         */

        if (finCount < 1l) {
            ReliabilityRunner.debug("Non finalize function was invoked.");
            ReliabilityRunner.mainTest.addError(this, new GCTestError());
        }
    }

    class ChainFinalizeEx extends ChainFinalize {
        ChainTest test = null;

        String s = null;

        public ChainFinalizeEx(ChainTest test, String s) {
            super();
            this.test = test;
            this.s = s;
        }

        protected void finalize() {
            super.finalize();
            for (int i = 0; i < 1000; i++) {
                Thread.yield();
            }
            test.finCount += s.length();
            if (test.finCount < 1l)
                test.finCount = 1l;
        }
    }
}
