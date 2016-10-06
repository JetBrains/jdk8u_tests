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

package org.apache.harmony.test.func.reg.vm.btest1784;

import org.apache.harmony.share.Test;

/**
 */

public class Btest1784 extends Test {
    public static void main(String[] args) {
        System.exit(new Btest1784().test());
    }

    public int test() {
        String s = new String("aaaa");
        StringBuffer sb = new StringBuffer("aaaa");
        try {
            s.indexOf("");
            s.indexOf("", 0);
            s.lastIndexOf("");
            s.lastIndexOf("", 0);
            sb.indexOf("");
            sb.indexOf("", 0);
            sb.lastIndexOf("");
            sb.lastIndexOf("", 0);
            return pass();
        } catch (Exception ex) {
            return fail("test fails");
        }
    }
}

