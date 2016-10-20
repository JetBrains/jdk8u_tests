/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    


package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;

/**
 * @author Vladimir Nenashev, Alexei Fedotov
 * @version $Revision: 1.3 $
 */
public class CallArgsTest extends TestCase implements Callee {
    static int _i0 = 1234567;

    static int _i1 = 7654321;

    static float _f2 = 3.141592f;

    static float _f3 = -3.141592f;

    static char _c4 = 'a';

    static int _i5 = -1234567;

    static int _i6 = -7654321;

    static int _i7 = 1024 * 1024;

    static float _f8 = 2.718281f;

    static float _f9 = 2.718281f * 2;

    static char _c10 = 'b';

    static byte[] _o11 = new byte[255];

    static float _f12 = 12345.67f;

    static double _d13 = 12345e67;

    static float _f14 = -12345.67f;

    static float _f15 = 123.45f;

    static double _d16 = -12345e67;

    static double _d17 = -123456e-7;

    static short _s18 = (short) 32767;

    static double _d19 = -1d / 0d;

    static int _i20 = 320;

    static int _i21 = -640;

    static int _i22 = 1280;

    static double _d23 = 123456e-78;

    static int _i24 = 2147483647;

    static int _i25 = -2147483648;

    static float _f26 = -123.45f;

    static double _d27 = 4.9E-324d;

    static double _d28 = 1.7976931348623157E308d;

    static CallArgsTest _o29 = null;

    static TestCase _o30 = null;

    static byte _b31 = -127;

    static short _s32 = -32768;

    static short _s33 = 32767;

    static double _d34 = Double.NEGATIVE_INFINITY;

    static CallArgsTest[] _o35 = null;

    static float _f36 = Float.MIN_VALUE;

    static double _d37 = Double.POSITIVE_INFINITY;

    static TestCase _o38[][][] = null;

    static double[][][][][][][][][][] _o39 = new double[2][2][2][1][1][1][1][1][1][1];

    static short[][][][][][][][][] _o40 = new short[1][2][2][2][1][1][1][1][1];

    static short _s41 = Short.MAX_VALUE;

    static short _s42 = Short.MIN_VALUE;

    static int _o43[][][][][][][][] = new int[1][1][2][2][2][1][1][1];

    static float _f44 = Float.MAX_VALUE;

    static long _l45 = 9223372036854775807l;

    static long _l46 = -9223372036854775808l;

    static Object[][][][][][] _o47 = new Object[1][1][1][2][2][2];

    static Thread[][][][][] _o48 = new Thread[2][1][1][2][2];

    static Exception[][][][] _o49 = new RuntimeException[2][2][1][2];

    static long _l50 = Long.MAX_VALUE;

    static long _l51 = Long.MIN_VALUE;

    static short _s52 = 1023;

    static byte _b53 = -64;

    static double _d54 = Double.MAX_VALUE;

    static double _d55 = Double.MIN_VALUE;

    static double _d56 = 1d / 0d;

    static char _c57 = 'c';

    static char _c58 = 'd';

    static String[][][] _o59 = new String[2][4][2];

    static char _c60 = 'e';

    static char _c61 = 'f';

    static char _c62 = '\u0000';

    static byte _b63 = 63;

    static byte _b64 = -128;

    static byte _b65 = 127;

    static short _s66 = 127;

    static byte _b67 = Byte.MIN_VALUE;

    static byte _b68 = Byte.MAX_VALUE;

    static char _c69 = Character.MIN_VALUE;

    static char _c70 = Character.MAX_VALUE;

    static char _c71 = '\uFFFF';

    public void test() {
        Callee c = this; /* Prevent optimizations */
        assertTrue(c.call(_i0, _i1, _f2, _f3, _c4, _i5, _i6, _i7, _f8, _f9,
                _c10, _o11, _f12, _d13, _f14, _f15, _d16, _d17, _s18, _d19,
                _i20, _i21, _i22, _d23, _i24, _i25, _f26, _d27, _d28, _o29,
                _o30, _b31, _s32, _s33, _d34, _o35, _f36, _d37, _o38, _o39,
                _o40, _s41, _s42, _o43, _f44, _l45, _l46, _o47, _o48, _o49,
                _l50, _l51, _s52, _b53, _d54, _d55, _d56, _c57, _c58, _o59,
                _c60, _c61, _c62, _b63, _b64, _b65, _s66, _b67, _b68, _c69,
                _c70, _c71));
    }

    public boolean call(int i0, int i1, float f2, float f3, char c4, int i5,
            int i6, int i7, float f8, float f9, char c10, byte[] o11,
            float f12, double d13, float f14, float f15, double d16,
            double d17, short s18, double d19, int i20, int i21, int i22,
            double d23, int i24, int i25, float f26, double d27, double d28,
            CallArgsTest o29, TestCase o30, byte b31, short s32, short s33,
            double d34, CallArgsTest[] o35, float f36, double d37,
            TestCase o38[][][], double[][][][][][][][][][] o39,
            short[][][][][][][][][] o40, short s41, short s42,
            int[][][][][][][][] o43, float f44, long l45, long l46,
            Object[][][][][][] o47, Thread[][][][][] o48,
            Exception[][][][] o49, long l50, long l51, short s52, byte b53,
            double d54, double d55, double d56, char c57, char c58,
            String[][][] o59, char c60, char c61, char c62, byte b63, byte b64,
            byte b65, short s66, byte b67, byte b68, char c69, char c70,
            char c71) {
        assertEquals(_i0, i0);
        assertEquals(_i1, i1);
        assertEquals(_f2, f2, 0f);
        assertEquals(_f3, f3, 0f);
        assertEquals(_c4, c4);
        assertEquals(_i5, i5);
        assertEquals(_i6, i6);
        assertEquals(_i7, i7);
        assertEquals(_f8, f8, 0f);
        assertEquals(_f9, f9, 0f);
        assertEquals(_c10, c10);
        assertEquals(_o11, o11);
        assertEquals(_f12, f12, 0);
        assertEquals(_d13, d13, 0);
        assertEquals(_f14, f14, 0);
        assertEquals(_f15, f15, 0);
        assertEquals(_d16, d16, 0);
        assertEquals(_d17, d17, 0);
        assertEquals(_s18, s18);
        assertEquals(_d19, d19, 0);
        assertEquals(_i20, i20);
        assertEquals(_i21, i21);
        assertEquals(_i22, i22);
        assertEquals(_d23, d23, 0);
        assertEquals(_i24, i24);
        assertEquals(_i25, i25);
        assertEquals(_f26, f26, 0);
        assertEquals(_d27, d27, 0);
        assertEquals(_d28, d28, 0);
        assertEquals(_o29, o29);
        assertEquals(_o30, o30);
        assertEquals(_b31, b31);
        assertEquals(_s32, s32);
        assertEquals(_s33, s33);
        assertEquals(_d34, d34, 0);
        assertEquals(_o35, o35);
        assertEquals(_f36, f36, 0);
        assertEquals(_d37, d37, 0);
        assertEquals(_o38, o38);
        assertEquals(_o39, o39);
        assertEquals(_o40, o40);
        assertEquals(_s41, s41);
        assertEquals(_s42, s42);
        assertEquals(_o43, o43);
        assertEquals(_f44, f44, 0);
        assertEquals(_l45, l45);
        assertEquals(_l46, l46);
        assertEquals(_o47, o47);
        assertEquals(_o48, o48);
        assertEquals(_o49, o49);
        assertEquals(_l50, l50);
        assertEquals(_l51, l51);
        assertEquals(_s52, s52);
        assertEquals(_b53, b53);
        assertEquals(_d54, d54, 0);
        assertEquals(_d55, d55, 0);
        assertEquals(_d56, d56, 0);
        assertEquals(_c57, c57);
        assertEquals(_c58, c58);
        assertEquals(_o59, o59);
        assertEquals(_c60, c60);
        assertEquals(_c61, c61);
        assertEquals(_c62, c62);
        assertEquals(_b63, b63);
        assertEquals(_b64, b64);
        assertEquals(_b65, b65);
        assertEquals(_s66, s66);
        assertEquals(_b67, b67);
        assertEquals(_b68, b68);
        assertEquals(_c69, c69);
        assertEquals(_c70, c70);
        assertEquals(_c71, c71);
        return true;
    }
}

interface Callee {
    boolean call(int i0, int i1, float f2, float f3, char c4, int i5, int i6,
            int i7, float f8, float f9, char c10, byte[] o11, float f12,
            double d13, float f14, float f15, double d16, double d17,
            short s18, double d19, int i20, int i21, int i22, double d23,
            int i24, int i25, float f26, double d27, double d28,
            CallArgsTest o29, TestCase o30, byte b31, short s32, short s33,
            double d34, CallArgsTest[] o35, float f36, double d37,
            TestCase o38[][][], double[][][][][][][][][][] o39,
            short[][][][][][][][][] o40, short s41, short s42,
            int[][][][][][][][] o43, float f44, long l45, long l46,
            Object[][][][][][] o47, Thread[][][][][] o48,
            Exception[][][][] o49, long l50, long l51, short s52, byte b53,
            double d54, double d55, double d56, char c57, char c58,
            String[][][] o59, char c60, char c61, char c62, byte b63, byte b64,
            byte b65, short s66, byte b67, byte b68, char c69, char c70,
            char c71);
}
