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
package org.apache.harmony.test.func.jit.HLO.inline.Synchronization.Synchronize3;

import java.util.ArrayList;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 16.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Synchronize3 extends Test {
    
    final TestThread[] thread = new TestThread[7];
    
    ArrayList listSync = new ArrayList();
    ArrayList listNotSync = new ArrayList(500000);
    
    public static void main(String[] args) {
        System.exit((new Synchronize3()).test(args));
    }
    
    public int test() {    
        log.info("Start Synchronize3 test...");
        try {
            int threadCount = thread.length;
            log.info("threadCount = " + threadCount);
            for (int i=0; i<threadCount; i++) {
                thread[i] = new TestThread(i);
            }
            for (int i=0; i<threadCount; i++) {
                thread[i].join();
                if (thread[i].isAlive())
                    return fail("TEST FAILED: some thread is alive after join()");
            }
            
            log.info("listNotSync.size() = " + listNotSync.size());
            log.info("listSync.size() = " + listSync.size());
            
            boolean diff = false;
            listNotSync.trimToSize();
            Integer[] arrNotSync = (Integer[]) listNotSync
                    .toArray(new Integer[listNotSync.size()]);
            int first = arrNotSync[0].intValue();
            for (int i=0; i<arrNotSync.length; i++) {
                if (arrNotSync[i].intValue() != first) diff = true;
            }
            if (diff) return pass();
            else return fail("TEST FAILED: ArrayList listNotSync " +
                    "must be unsynchronized");
            
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }
    
    class TestThread extends Thread {
        
        Integer threadNumber;
        
        TestThread(int i) {
            super();
            threadNumber = new Integer(i);
            start();
        }
        
        public void run() {
            int size = 0;
            for (int j=0; j<500; j++) {
                for (int k=0; k<100; k++) {
                    synchronized (listSync) {
                        listSync.add(threadNumber);
                    }
                    listNotSync.add(threadNumber);
                    inlineMethod();
                }
            }
        }
        
        final void inlineMethod() {
            synchronized (listSync) {
                listSync.remove(threadNumber);
            }
        }
    }
}
