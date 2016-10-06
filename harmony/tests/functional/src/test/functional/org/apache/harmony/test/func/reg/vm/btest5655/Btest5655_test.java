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
package org.apache.harmony.test.func.reg.vm.btest5655;

interface Btest5655_intf {
    public void test_method();
}

class Btest5655_intfimpl implements Btest5655_intf {
    public void test_method() {
        System.out.println("in test_method");
    }
}

public class Btest5655_test {
    public static void main(String argv[]) {
        Btest5655_intf inter = new Btest5655_intfimpl();

        try {
            inter.test_method();
        } catch(NoSuchMethodError e) {
        }
    }
}
