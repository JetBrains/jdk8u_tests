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
package org.apache.harmony.test.func.jit.HLO.peel.EndlessLoop1;

import java.util.ArrayList;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 15.06.2006 
 */

public class EndlessLoop1 extends Test {
    
    private final ArrayList list = new ArrayList();
    private final int size = 10010;
    private int i = 0;
    private int k = 0;
    
    public static void main(String[] args) {
        System.exit(new EndlessLoop1().test(args));
    }

    public int test() {
        log.info("Start EndlessLoop1 test...");
        for (k=1; k<20; k++) {
            k++;
            cycle();
        }
        int listSize = list.size();
        log.info("List size = " + listSize);
        if (listSize != size) 
            return fail("TEST FAILED: List size != " + size);
        for (int j=0; j<size; j++) 
            if (((Integer) list.get(j)).intValue() != 1) 
                return fail("TEST FAILED: list.get("+ j + ")!=1");
        return pass();
    }
    
    void cycle() {
        final Integer integer2 = new Integer(2);
        final Integer integer1 = new Integer(1);
        while(true) {
            i+=2;
            list.add(integer1);
            list.add(integer2);
            if (i%2 == 0) list.remove(i/2);
            if (i/k > 1000) return;
        }
    }
    
}

