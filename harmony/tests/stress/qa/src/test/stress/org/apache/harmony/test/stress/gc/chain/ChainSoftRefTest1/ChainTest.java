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
package org.apache.harmony.test.stress.gc.chain.ChainSoftRefTest1;

import org.apache.harmony.test.stress.gc.chain.share.*;
import junit.framework.TestCase;
import java.lang.ref.*;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 */
public class ChainTest extends TestCase {

    private final static int NUM = 1024;

    SoftReference saved = null;

    public void test() {
        SoftReference[] sr = new SoftReference[4];

        int i = 0;
        int j = 0;
        Chain chain = null;
        try {
            for (; i < NUM; ++i) {
                chain = null;

                if (sr[0] != null) {
                    saved = sr[0];
                }

                j = 0;
                for (; j < NUM; ++j) {
                    Chain next = new Chain();
                    next.prev = chain;
                    chain = next;

                    if (j == 0) {
                        sr[0] = new SoftReference(chain);
                    } else if (j == (int) (0.25 * NUM)) {
                        sr[1] = new SoftReference(chain);
                    } else if (j == (int) (0.5 * NUM)) {
                        sr[2] = new SoftReference(chain);
                    } else if (j == (int) (0.75 * NUM)) {
                        sr[3] = new SoftReference(chain);
                    }

                }
            }
        } catch (OutOfMemoryError e) {
            ReliabilityRunner.debug("OutOfMemoryError is thrown on iteration " + j + ", try " + i);
            ReliabilityRunner.mainTest.addError(this, e);
        }
    }

}
