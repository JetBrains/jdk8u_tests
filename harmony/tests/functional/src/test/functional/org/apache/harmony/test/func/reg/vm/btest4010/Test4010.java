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

package org.apache.harmony.test.func.reg.vm.btest4010;

import java.util.*;

public class Test4010 {

    public static void main(String[] args) {
        System.err.println("Start Test4010...");
        Test4010 test = new Test4010();
        test.test();
    }

    public void test() {

        Hashtable ht = new Hashtable();
        Object o = new Object();
        int step = 100000;
        int sz = 0;
        int i = 0;
        try {
            while(true) {
                ht.put("Key".concat(String.valueOf(i)), o);
                i++;
                sz = ht.size();
                //System.err.println(" size of hash table " + sz);
                if (sz >= step) {
                    System.err.println(sz);
                    if (ht.size() == 1500000) {
                         System.err.println("Size == 1500000. Stop");
                         break;
                    }
                    step += 100000;
                }
            }        
        } catch (OutOfMemoryError e) {
            System.err.println("PASS OutOfMemoryError");
        } catch (StackOverflowError e) {
            System.err.println("PASS StackOverflowError");
        }
    }
}
