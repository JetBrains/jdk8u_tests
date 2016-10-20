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

public class FibonacciNum41B extends Thread {
    int result = 0;

    int init;

    FibonacciNum41B(int i) {
        init = i;
    }

    public void run() {
        if (init < 2) {
            result = 1;
            return;
        }
        FibonacciNum41B fn1 = new FibonacciNum41B(init - 1);
        FibonacciNum41B fn2 = new FibonacciNum41B(init - 2);
        fn1.start();
        fn2.start();
        try {
            fn1.join();
            fn2.join();
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
        synchronized (FibonacciNum41B.class) {
            result = fn1.result + fn2.result;
        }
    }

    public static void main(String args[]) {
        int STRESS_LOAD = 10;
        try {
            STRESS_LOAD = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException exc) {
            System.out
                    .println("You should to indicate one int parameter. Default value set (10).");
        }

        FibonacciNum41B fn = new FibonacciNum41B(STRESS_LOAD);
        fn.start();
        try {
            fn.join();
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
        System.out.println(fn.result);
    }
}
