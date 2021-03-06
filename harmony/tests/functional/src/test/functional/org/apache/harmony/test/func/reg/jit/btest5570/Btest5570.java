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
 
package org.apache.harmony.test.func.reg.jit.btest5570;

public class Btest5570 extends Thread {   
    
    public static int intValue;
    public static boolean pr = false;
    public static int step;

    public static void main(String [] args) {
        new Btest5570().start();
        while (true) {
            int ii  = intValue;
            if ((ii != 777) && (ii != -1l)) {
                System.err.println("Unknown value in ii: " + ii);
                pr = true;
                System.exit(0);            
            }
            step++;
            if (!pr && step > 1000000) {
                System.err.println("Finish");
                System.err.println("PASSED!");
                System.exit(0);                
            }
        }
    }
    
    public void run() {
        step = 0;
        while (true) {
            intValue = 777;
            intValue = -1;
        }
    }

}
