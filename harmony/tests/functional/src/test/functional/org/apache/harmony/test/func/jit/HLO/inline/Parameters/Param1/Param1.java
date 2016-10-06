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
package org.apache.harmony.test.func.jit.HLO.inline.Parameters.Param1;

import java.util.Vector;

import org.apache.harmony.share.Test;


/**
 */

/*
 * Created on 25.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Param1 extends Test {

        int res;
    
        public static void main(String[] args) {
            System.exit((new Param1()).test(args));
        }

        public int test() {
            log.info("Start Param1 test...");
            try {
                Vector v = new Vector(10);
                Integer i = new Integer(1);
                for (int j=0; j<100000; j++) {
                    res = inlineMethod (
                    1234567,
                    1234567,
                    9.3f,
                    9.3f,
                    'a',
                    1234567,
                    1234567,
                    1234567,
                    new int[][]{{1},{1}},
                    9.3f,
                    9.3f,
                    'a',
                    v,
                    null,
                    i,
                    9.3f, 
                    456e45,
                    9.3f,
                    9.3f,
                    456e2,
                    456e45, 
                    (short)32767,
                    456e45,
                    1234567,
                    1234567,
                    1234567,
                    456e45,
                    1234567,
                    1234567,
                    9.3f,
                    456e45,
                    456e45,
                    null,
                    null,
                    (byte)-127,
                    (short)32767,
                    (short)32767,
                    456e45,
                    null,
                    9.3f,
                    456e45,
                    null,
                    null,
                    null,
                    (short)32767,
                    (short)32767,
                    null,
                    9.3f,
                    (short)32767,
                    (short)32767,
                    null,
                    null,
                    null,
                    (short)32767,
                    (byte)-127,
                    (short)32767,
                    (byte)-127,
                    456e45,
                    (byte)-127,
                    (byte)-127,
                    'a',
                    'a',
                    null,
                    'a',
                    "hello",
                    'a',
                    'a',
                    (byte)-127,
                    (byte)-127,
                    (byte)-127,
                    (short)32767);
                }
                if (res == 78376) return pass();
                else return fail("TEST FAILED: bad return value " + res);
            } catch (Throwable e) {
                log.add(e);
                return fail("TEST FAILED: unexpected exception " + e );
            }
        }

        public final int inlineMethod (
                int i0,
                int i1,
                float f2,
                float f3,
                char c4,
                int i5,
                int i6,
                int i7,
                int[][] ii8,
                float f9,
                float f10,
                char c11,
                Vector v12,
                Param1 o13,
                Integer int14,
                float f15,
                double d16,
                float f17,
                float f18,
                double d19,
                double d20,
                short s21,
                double d22,
                int i23,
                int i24,
                int i25,
                double d26,
                int i27,
                int i28,
                float f29,
                double d30,
                double d31,
                Param1 o32,
                Param1 o33,
                byte b34,
                short s35,
                short s36,
                double d37,
                Param1 o38,
                float f39,
                double d40,
                Param1 o41,
                Param1 o42,
                Param1 o43,
                short s44,
                short s45,
                Param1 o46,
                float f47,
                short s48,
                short s49,
                Param1 o50,
                Param1 o51,
                Param1 o52,
                short s53,
                byte b54,
                short s55,
                byte b56,
                double d57,
                byte b58,
                byte b59,
                char c60,
                char c61,
                Param1 o62,
                char c63,
                String str, 
                char c65,
                char c66,
                byte b67,
                byte b68,
                byte b69,
                short s70) {
                    return (int)f3 + (int) s55 + (int) d19;
                }
}
