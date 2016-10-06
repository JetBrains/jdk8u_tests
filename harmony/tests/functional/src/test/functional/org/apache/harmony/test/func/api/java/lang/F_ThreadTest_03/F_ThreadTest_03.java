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
 * Created on 29.11.2004
 */
package org.apache.harmony.test.func.api.java.lang.F_ThreadTest_03;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 *    Usage: 
 *        java.lang.Thread 
 *
 **/

public class F_ThreadTest_03 extends ScenarioTest {

    public static final int NACCOUNTS = 10;
    public static final int INITIAL_BALANCE = 10;

    public int test() {
        try {
            Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);
            int i;
            for (i = 0; i < NACCOUNTS; i++) {  
                TransferThread t = new TransferThread(b, i, INITIAL_BALANCE);
                t.setPriority(Thread.NORM_PRIORITY + i % 2);
                t.start();
            }
        } 
        catch (Exception e) 
        {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }        
    public static void main(String[] args) {
        System.exit(new F_ThreadTest_03().test(args));
    }

    class Bank {  
        public static final int NTEST = 10;
        private final int[] accounts;
        private long ntransacts = 0;

        public Bank(int n, int initialBalance) {  
            accounts = new int[n];
            int i;
            for (i = 0; i < accounts.length; i++)
                accounts[i] = initialBalance;
            ntransacts = 0;
        }

       public synchronized void transfer(int from, int to, int amount) {  
           try {
               while (accounts[from] < amount)
                   wait();
               accounts[from] -= amount;
               accounts[to] += amount;
               ntransacts++;
               notifyAll();
               log.info(Long.toString(ntransacts % NTEST));
               if (ntransacts % NTEST == 0) 
                   test();
           } catch(InterruptedException e) {
               error("test failed - " + e.getMessage());
           }
       }

       public synchronized void test() {  
           int sum = 0;
           for (int i = 0; i < accounts.length; i++)
               sum += accounts[i];
                  log.info("Transactions:" + ntransacts + " Sum: " + sum);
       }

       public int size() {  
           return accounts.length;
       }
    }

    class TransferThread extends Thread {  
        
        private Bank bank;
        private int fromAccount;
        private int maxAmount;

        public TransferThread(Bank b, int from, int max) {  
            bank = b;
            fromAccount = from;
            maxAmount = max;
        }

       public void run(){  
           try {  
               while (!interrupted()) {  
                   int toAccount = (int)(bank.size() * Math.random());
                   int amount = (int)(maxAmount * Math.random());
                   bank.transfer(fromAccount, toAccount, amount);
                   sleep(1);
               }
           } catch(InterruptedException e) {
               error("test failed - " + e.getMessage());
           }
       }
    }
}
