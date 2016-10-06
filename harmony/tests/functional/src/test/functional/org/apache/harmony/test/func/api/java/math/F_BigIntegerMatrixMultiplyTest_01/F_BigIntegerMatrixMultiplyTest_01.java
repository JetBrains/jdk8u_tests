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
/*
 * Created on 26.01.2006
 *  
 * testing some BigInteger methods.
 */
package org.apache.harmony.test.func.api.java.math.F_BigIntegerMatrixMultiplyTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.util.ArrayList;
import java.util.Random;
import java.math.*;

/**
 * 
 */

public class F_BigIntegerMatrixMultiplyTest_01 extends ScenarioTest {
    Object     o1        = new Object();

    Object     o2        = new Object();

    final int  matr_size = 40;

    BigInteger matr1[][] = new BigInteger[matr_size][matr_size];

    BigInteger matr2[][] = new BigInteger[matr_size][matr_size];

    BigInteger matr3[][] = new BigInteger[matr_size][matr_size];

    Object     shared    = new Object();

    Object     started   = new Object();

    class MyThread extends Thread {
        int row    = 0;

        int column = 0;

        MyThread(int i, int j) {
            this.row = i;
            this.column = j;
        }

        public void run() {
            synchronized (o1) {
                o1.notifyAll();
            }
            synchronized (o2) {
            }
            for (int k = 0; k < matr_size; k++) {
                matr3[row][column] = matr3[row][column].add(matr1[row][k]
                    .multiply(matr2[k][column]));
            }
        }
    }

    boolean task() {
        ArrayList threadsList = new ArrayList();
        Random rnd = new Random();
        BigInteger rndbi;
        BigInteger tmpbi = BigInteger.ONE;
        BigInteger sumbi = BigInteger.ZERO;

        /* Construct BigInteger */
        try {
            for (int i = 0; i < 1000; i++) {
                rndbi = new BigInteger(i, rnd);
                tmpbi = BigInteger.ONE;
                tmpbi = tmpbi.add(tmpbi);
                tmpbi = tmpbi.pow(i);
                tmpbi = tmpbi.subtract(BigInteger.ONE);
                if ((rndbi.compareTo(tmpbi) > 0)
                    || (rndbi.compareTo(BigInteger.ZERO) < 0)) {
                    System.out.println(rndbi + " is out of range [0, 2^" + i
                        + "-1]");
                    return false;
                }
                sumbi = sumbi.add(rndbi);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("Unexpected exception");
            return false;
        }
        BigInteger bi = new BigInteger(new byte[] { 1, 0, 0, 0, 1 });

        /* Initialize matrices */
        matr1[0][0] = sumbi.add(bi);
        matr2[0][0] = matr1[0][0];
        matr3[0][0] = BigInteger.ZERO;
        for (int j = 1; j < matr_size; j++) {
            matr1[0][j] = BigInteger.ZERO;
            matr2[0][j] = BigInteger.ZERO;
            matr3[0][j] = BigInteger.ZERO;
            matr1[j][0] = BigInteger.ZERO;
            matr2[j][0] = BigInteger.ZERO;
            matr3[j][0] = BigInteger.ZERO;
        }
        for (int i = 1; i < matr_size; i++) {
            for (int j = 1; j < matr_size; j++) {
                if (i == j) {
                    matr1[i][i] = matr1[i - 1][i - 1].add(BigInteger.ONE);
                    matr2[i][i] = matr1[i][i];
                } else {
                    matr1[i][j] = new BigInteger("0");
                    matr2[i][j] = new BigInteger("0");
                }
                matr3[i][j] = BigInteger.ZERO;
            }
        }

        /* Start threads */
        synchronized (o2) {
            for (int i = 0; i < matr_size; i++) {
                for (int j = 0; j < matr_size; j++) {
                    Thread mt = new MyThread(i, j);
                    synchronized (o1) {
                        mt.start();
                        try {
                            o1.wait();
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                    threadsList.add(mt);
                }
            }
        }
        /* Now all threads entered in their run() methods */

        /* Join all threads */
        for (int i = 0; i < threadsList.size(); i++) {
            Thread t = (Thread)threadsList.get(i);
            try {
                t.join();
            } catch (InterruptedException ie) {
                log
                    .info("Unexpected interrupted exception while joining thread "
                        + t);
                ie.printStackTrace();
            }
        }

        /* Check results */
        for (int i = 0; i < matr_size; i++) {
            if (matr3[i][i].compareTo(matr1[0][0].add(
                new BigInteger(new Integer(i).toString())).pow(2)) != 0) {
                System.out.println("Wrong calculations result");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.exit(new F_BigIntegerMatrixMultiplyTest_01().test(args));
    }

    public int test() {
        try {
            try {
                if (!task()) {
                    return fail("Test failed");
                }
            } catch (Exception ex) {
                System.out.println("Unexpected exception:");
                ex.printStackTrace();
                return fail("Test failed");
            }
        } catch (Error er) {
            System.out.println("Unexpected error:");
            er.printStackTrace();
            return fail("Test failed");
        }
        return pass();
    }
}