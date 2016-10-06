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
/**
 */
package org.apache.harmony.test.func.reg.vm.btest6080;

import org.apache.harmony.share.Test;

/**
 */
public class Btest6080 extends Test {

    public static native boolean foo();

    static {
        System.loadLibrary("Btest6080");
    }

    public static void main(String[] args) {
        System.exit(new Btest6080().test());
    }


    public int test() {
        try {
            foo();
            return fail("Exception was expected");
        } catch (Exception e) {
            return pass();
        }
    }

    public static void Call() throws Exception {
        throw new InterruptedException("Expected InterruptedException");
    }   

}

