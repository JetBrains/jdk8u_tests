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
package org.apache.harmony.test.func.reg.vm.btest7998.a;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest7998_case2 extends RegressionTest {
    public static void main( String [] args) throws Exception {
        System.exit(new Btest7998_case2().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        return ((Btest7998_case2_aux1) new Btest7998_case2_aux3()).m() == 799823
            ? pass():fail();
    }
}
