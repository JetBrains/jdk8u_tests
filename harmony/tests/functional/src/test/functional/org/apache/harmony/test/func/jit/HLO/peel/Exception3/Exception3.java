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
package org.apache.harmony.test.func.jit.HLO.peel.Exception3;

import java.util.Arrays;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 03.07.2006
 */

public class Exception3 extends Test {
    
    private int check = 0;
    private final int limit = 100000;
    
    public static void main(String[] args) {
        System.exit(new Exception3().test(args));
    }

    public int test() {
        log.info("Start Exception3 test ...");
        try {
            loop();
            return fail("TEST FAILED: ArrayIndexOutOfBoundsException wasn't thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            log.info("Iteration number: " + check);
            if (check == (limit-1)) return pass();
            else return fail("TEST FAILED: iteration number != " + (limit-1));
        }
    }
    
    void loop() {
        Integer integer[] = new Integer[limit];
        Arrays.fill(integer, new Integer(limit));
        for (int i = 0; i < integer[i].intValue(); i++) {
            check = i;
        }
    }

}
