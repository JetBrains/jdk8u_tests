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
package org.apache.harmony.test.func.reg.vm.btest1007;

import org.apache.harmony.share.Test;

/**
 */
public class Btest1007 extends Test {

    public static void main(String[] args) {
        System.exit(new Btest1007().test());
    }

    public static void foo(String s) {
        System.err.println(s.length());
    }

    public int test() {
        try {
            foo(null);
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                return pass();
            }
            return fail("test fails with " + e);
        }
        return fail("no exceptions");
    }
}
