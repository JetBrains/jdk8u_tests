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
package org.apache.harmony.test.func.jit.HLO.peel.Continue2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 03.07.2006 
 */

public class Continue2 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Continue2().test(args));
    }

    public int test() {
        log.info("Start Continue2 test...");
        int i = 0;
        int j = 0;
        int check = 0;
        int size = 500000;
        boolean flag = true;
        label1:
        do {
            if (j > 2*size) break;
            try {
                label2:
                while (i < size) {
                    i++;
                    if (i/(i%2) == i) continue label2;
                    check = 100;
                }
                j++;
            } finally {
                continue label1;
            }
        } while (flag);
        if (check != 0) return fail("TEST FAILED: check=100 " +
                "assignment was incorrectly hoisted outside the loop");
        log.info("i=" + i);
        log.info("j=" + j);
        if ((i != size) || (j != 2*size+1)) {
            log.info("i must be " + size);
            log.info("j must be " + 2*size+1);
            return fail("TEST FAILED: iteration number was corrupted");
        }
        return pass();
    }
    
}

