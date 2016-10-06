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

package org.apache.harmony.test.func.api.java.util.Random;

import java.util.Random;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 10, 2006
 */
public class RandomTest extends MultiCase {

    public static void main(String[] args) {
        System.exit(new RandomTest().test(args));
    }

    private interface Next {
        double next();
    }

    private Result tstNext(Next provider) {
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        double avg = 0;
        int[] hits = new int[100];
        final int count = 1000;
        for (int i = 0; i < count; i++) {
            double next = provider.next();
            max = Math.max(max, next);
            min = Math.min(min, next);
            avg += next / count;
            hits[(int) (next * hits.length)] += 1;
        }

        assertFalse(max > 1);
        assertFalse(max < 1 - 2d / count);
        assertFalse(min < 0);
        assertFalse(min > 2d / count);
        assertFalse(avg > 0.6);
        assertFalse(avg < 0.4);

        double sqr = 0;
        for (int i = 0; i < hits.length; i++) {
            sqr += (hits[i] - 10) * (hits[i] - 9);
        }
        assertTrue(sqr / 1000 < 2);

        return result();
    }

    public Result testNextDouble() {
        final Random r = new Random(1234567890l);

        return tstNext(new Next() {
            public double next() {
                return r.nextDouble();
            }
        });
    }

    public Result testNextFloat() {
        final Random r = new Random(1234567890l);

        return tstNext(new Next() {
            public double next() {
                return r.nextFloat();
            }
        });
    }

    public Result testNextInt() {
        final Random r = new Random(1234567890l);

        return tstNext(new Next() {
            public double next() {
                return (double) r.nextInt(16548) / 16548d;
            }
        });
    }

    private class Accessor extends Random {

        public void setSeed(long seed) {
            this.seed = (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1);
            super.setSeed(seed);
        }

        private long seed;

        synchronized public int next(int bits) {
            return super.next(bits);
        }

        public int nextVerify(int bits) {
            seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
            return (int) (seed >>> (48 - bits));
        }
    }

    public Result testNext() {
        long[] seeds = { 1415926535897932384l, 6264338327950288419l,
                7169399375105820974l, 445923078164062862l, 
                -899862803482534211l,
                7067982148086513282l, 3066470938446095505l,
                8223172535940812848l, 1117450284102701938l,
                5211055596446229489l, 5493038196442881097l,
                5665933446128475648l, 2337867831652712019l,
                914564856692346034l, 8610454326648213393l,
                6072602491412737245l, 8700660631558817488l,
                1520920962829254091l, 7153643678925903600l,
                1133053054882046652l, 1384146951941511609l };

        boolean pass = true;
        Accessor acc = new Accessor();
        for (int i = 0; i < seeds.length; i++) {
            acc.setSeed(seeds[i]);
            for (int bits = 1; bits <= 32; bits++) {
                pass &= acc.next(bits) == acc.nextVerify(bits);
            }
        }
        return pass ? passed() : failed("error");
    }
}
