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
package org.apache.harmony.test.func.reg.vm.btest10305;

import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest10305 extends RegressionTest {

    static boolean status = false;

    public static void main(String[] args) {
        Btest10305 inst = new Btest10305();
        inst.test();
        System.exit(status ? inst.pass() : inst.fail());
    }

    public void test() {
        // set first breakpoint to the next line
        Object obj = new Object();
        int z = 10;
        z = z * 2;
        // set second breakpoint to the next line
        System.out.println("done: " + z + obj);
    }
}