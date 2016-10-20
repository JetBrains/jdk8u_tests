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
import java.util.LinkedList;

import org.apache.harmony.test.stress.misc.MiscTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class StringsIntern53 extends TestCase {

    public void test() {
        int capacity = 0;
        int capacity2 = 0;
        int i = 0;
        String str;
        LinkedList objects_array = new LinkedList();
        LinkedList strings_array = new LinkedList();

        // pad memory to recognize memory capacity
        try {
            while (true) {
                objects_array.add(capacity, new StringsIntern53Obj());
                capacity++;
            }
        } catch (OutOfMemoryError er) {
            // release memory
            objects_array = null;
            System.gc();
            ReliabilityRunner.debug("Memory capacity is " + capacity);
        }

        // create strings and intern it
        try {
            while (true) {
                strings_array.add(i, new String("String#" + i));
                str = (String) (strings_array.get(i));
                str.intern();
            }
        } catch (OutOfMemoryError er) {
            // release memory. Interned strings should be deleted also.
            strings_array = null;
            System.gc();
            ReliabilityRunner.debug("Memory padded by interned strings.");
            ReliabilityRunner.debug("Memory released.");
        }

        // pad memory to recognize memory capacity second time
        objects_array = new LinkedList();
        try {
            while (true) {
                objects_array.add(capacity2, new StringsIntern53Obj());
                capacity2++;
            }
        } catch (OutOfMemoryError er) {
            // release memory
            objects_array = new LinkedList();
            System.gc();
            ReliabilityRunner
                    .debug("Memory capacity after interned strings is "
                            + capacity2);
        }

        if (capacity2 + 5 < capacity) {
            ReliabilityRunner.debug("Test failed");
            ReliabilityRunner.mainTest.addError(this, new MiscTestError());
        }
    }
}

class StringsIntern53Obj {
    int testArray[][][] = new int[100][10][10];
}
