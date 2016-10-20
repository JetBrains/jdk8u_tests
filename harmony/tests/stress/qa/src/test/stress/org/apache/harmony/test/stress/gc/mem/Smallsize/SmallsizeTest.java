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
 * Created on 19.01.2006
 * 
 */
package org.apache.harmony.test.stress.gc.mem.Smallsize;

import junit.framework.TestCase;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class SmallsizeTest extends TestCase {

    private int MAX_MEM = 0;

    private byte[] array = null;

    public void test() {
        MAX_MEM = Integer.getInteger(
                "org.apache.harmony.test." + "stress.gc.mem.Smallsize."
                        + "SmallsizeTest.MAX_MEM", 0).intValue();

        try {
            array = new byte[MAX_MEM];
        } catch (OutOfMemoryError e) {
            ReliabilityRunner.debug(e.toString());
            ReliabilityRunner.mainTest.addError(this, e);
        }
    }
}
