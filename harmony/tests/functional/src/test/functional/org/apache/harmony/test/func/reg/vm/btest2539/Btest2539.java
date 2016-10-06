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
package org.apache.harmony.test.func.reg.vm.btest2539;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

// TODO: 
// IPF only
public class Btest2539 extends RegressionTest {

        public native int foo(int i0);

        private int call(int i0, int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
            return i0 +  i1 +  i3 +  i4 + i5 + i6 + i7 + i8 +i9 + i2;
        }

        public static void main(String[] args) {
            System.exit(new Btest2539().test(Logger.global, args));
        }

        public int test(Logger logger, String[] args) {
             Btest2539 t = new Btest2539();
             System.loadLibrary("Btest2539");

             return t.foo(1) !=10 ? fail() : pass();
            
        }

}
