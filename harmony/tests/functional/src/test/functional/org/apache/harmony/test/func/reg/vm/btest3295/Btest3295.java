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
package org.apache.harmony.test.func.reg.vm.btest3295;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

// need to improve
public class Btest3295 extends RegressionTest {

    public static final class_a inst = new class_b();

    public static void main(String[] args) {
        System.exit(new Btest3295().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        if (inst.print()) {
            return pass();
        } else {
            return fail();
        }
    }
}

final class class_b extends class_a {
    public boolean print() {
        return true;
    }
}

abstract class class_a extends class_c {
}

abstract class class_c implements I1 {
}

interface I1 extends I {
}

interface I {
    public boolean print();
}
