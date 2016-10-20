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

/**
 * @author Alexander D. Shipilov
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.misc;

public class Sort41C extends Thread {
    int result = 0;

    int init;

    int[] arrayToSort;

    Sort41C(int i, int[] array) {
        init = i;
        arrayToSort = array;
    }

    public void run() {
        int k;
        if (init < 2) {
            for (int j = 0; j < arrayToSort.length - 1; j++) {
                if (arrayToSort[j] > arrayToSort[j + 1]) {
                    k = arrayToSort[j];
                    arrayToSort[j] = arrayToSort[j + 1];
                    arrayToSort[j + 1] = k;
                }
            }
            return;
        }
        Sort41C sort1 = new Sort41C(init / 2, arrayToSort);
        Sort41C sort2 = new Sort41C(init / 2, arrayToSort);
        sort1.start();
        sort2.start();
        try {
            sort1.join();
            sort2.join();
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
        synchronized (Sort41C.class) {
            for (int j = 0; j < arrayToSort.length - 1; j++) {
                if (arrayToSort[j] > arrayToSort[j + 1]) {
                    k = arrayToSort[j];
                    arrayToSort[j] = arrayToSort[j + 1];
                    arrayToSort[j + 1] = k;
                }
            }
        }
    }

    public static void main(String args[]) {
        int STRESS_LOAD = 10;
        int[] arrayToSort;

        try {
            STRESS_LOAD = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException exc) {
            System.out
                    .println("You should to indicate one int parameter. Default value set (10).");
        }

        arrayToSort = new int[STRESS_LOAD];
        java.util.Random randomizer = new java.util.Random();
        for (int i = 0; i < STRESS_LOAD; i++) {
            arrayToSort[i] = randomizer.nextInt(STRESS_LOAD * 100);
        }
        Sort41C sort = new Sort41C(STRESS_LOAD, arrayToSort);
        sort.start();
        try {
            sort.join();
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
        System.out.println("Sorted random array:");
        System.out.print(" ");
        for (int i = 0; i < STRESS_LOAD; i++) {
            System.out.print(arrayToSort[i] + " ");
        }
    }
}
