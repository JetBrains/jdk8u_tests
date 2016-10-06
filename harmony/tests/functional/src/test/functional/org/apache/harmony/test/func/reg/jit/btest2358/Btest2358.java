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

/*
 * I was able to reproduce it using 20050330 Windows IA-32 build
 */

package org.apache.harmony.test.func.reg.jit.btest2358;

/*
 * The test fails with the following Windows IA32 builds:
 *         20050330
 *         combined_20051025_win_ia32_0000_icl
 * The test passes with the following Windows IA32 builds:
 *         combined_20051005_win_ia32_0101_icl_M05
 */
public class Btest2358 {
    public static void main(String[] args) {
        while (true) {}
    }
}
