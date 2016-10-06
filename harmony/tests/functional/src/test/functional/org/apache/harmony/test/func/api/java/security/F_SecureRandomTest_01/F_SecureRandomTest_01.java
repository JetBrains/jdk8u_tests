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
package org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_01;

/**
 * 
 * Let it is necessary to test the generator which is giving out some sequence
 * of bits concerning which the zero-hypothesis that this sequence has uniform
 * distribution is put forward. We shall designate volume of sample n. Let we
 * have generated 3000 bits, then n=3000. Let sample is divided on k classes.
 * If, for example, we research frequencies of occurrence from-127 - 128 - then
 * quantity of classes 256. Let B_i - observable frequency = quantity of
 * occurrences of some attribute in sample. Let E_i - expected frequency of an
 * attribute i. For our case 1/256*3000. The formula the hi-square for
 * calculation of distinction between experimental and theoretical distributions
 * following: i=k-1 ____ 2 \ (B_i - E_i) hi-square = /___ ------------- i=0 E
 * Let's choose a significance value = probability of a mistake, for example 0.1% we
 * open the book on Math.Stat the table of borders for the hi-square. If value
 * the hi-square is less than or equal tabulared the zero-hypothesis is
 * accepted. Otherwise - deviates.
 */
import java.util.*;
import java.security.*;
import org.apache.harmony.test.func.share.ScenarioTest;

public class F_SecureRandomTest_01 extends ScenarioTest {
    static SecureRandom random;

    static int n;

    static double h2;

    byte seed[];

    public static void method_h2(int n, int qua_array[][]) {
        int k = 256;
        double exp_freq = 0.00390625 * n;
        h2 = 0;
        for (int i = 0; i < k; i++) {
            h2 = h2 + (qua_array[1][i] - exp_freq)
                    * (qua_array[1][i] - exp_freq) / exp_freq;
        }
    }

    public static void quantity(byte seed[], int qua_array[][]) {
        int i = 0;
        int j = 1;
        int l = 0;
        while (i < seed.length) {
            if (i != seed.length - 1 && seed[i] == seed[i + 1]) {
                j++;
            } else {
                qua_array[0][l] = seed[i];
                qua_array[1][l] = j;
                l++;
                j = 1;
            }
            i++;
        }
    }

    public int test() {
        int m;
        String provider_name;
        Object alg[] = Security.getAlgorithms("SecureRandom").toArray();
        for (int i = 0; i < alg.length; i++) {
            try {
                random = SecureRandom.getInstance(alg[i].toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return fail("Algorithm " + alg[i] + " must exist.");
            }
            provider_name = random.getProvider().getName();
            log.info("-------Provider: " + provider_name + ", algorithm: " + alg[i] + "-------");
            log.info("A first cycle is started...");
            for (m = 1; m <= 10; m++) {
                log.info(m + " iteration started.");
                n = 10;
                log.info("Seed generation..."); 
                seed = random.generateSeed(n);            
                int qua_array[][] = new int[2][256];
                log.info("OK\nSeed array sorting...");
                Arrays.sort(seed);
                log.info("OK\nSeeding pseudo-random number generator the first way...");
                random.setSeed(m);
                log.info("OK\nSeeding pseudo-random number generator the second way...");
                random.setSeed(seed);
                log.info("OK");
                quantity(seed, qua_array);
                method_h2(n, qua_array);
                log.info(m + " iteration finished.");
                if (h2 < 160) {
                    return error("Distribution of random numbers by PRNG does not pass criterion h2(hi kvadrat)(generateSeed)");
                }
            }
            log.info("A second cycle is started...");
            for (m = 1; m <= 10; m++) {
                log.info(m + " iteration started.");
                n = 10;
                log.info("Seed generation...");
                seed = SecureRandom.getSeed(n);
                int qua_array[][] = new int[2][256];
                log.info("OK\nSeed array sorting...");
                Arrays.sort(seed);
                log.info("OK");
                quantity(seed, qua_array);
                method_h2(n, qua_array);
                log.info(m + " iteration finished.");
                if (h2 < 160) {
                    return error("Distribution of random numbers by PRNG does not pass criterion h2(hi kvadrat)(getSeed)");
                }
            }
        }
        return pass("Passed");
    }

    public static void main(String[] args) {
        System.exit(new F_SecureRandomTest_01().test(args));
    }
}