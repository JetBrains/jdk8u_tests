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
package org.apache.harmony.test.func.share.synctest;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.harmony.share.Test;

/**
 * SyncTestCaller provides basic interface for testing synchronizable
 * objects. It should be used when it is necessary to check whether some
 * particular method is synchronized or not.
 *
 * Note: Methods "run" and "getInstance" must be overridden
 *
 */

public abstract class SyncTestCaller extends Test implements Runnable {

    private static int iCounter;
    private static Set nonsyncMethods;
    private static HashMap oTestedMethods = new HashMap();
    protected Object lock;         //tested object
    protected SyncTestLocker[] lockers;
    private int callerId;
    private boolean callPassed;
    
    protected abstract void fillTestedMethods();
    
    public abstract void run();
    public abstract SyncTestCaller getInstance(int iIdx);

    static {
        nonsyncMethods = Collections.synchronizedSet(new HashSet());
    }

    public SyncTestCaller() {
    }

    public SyncTestCaller(int id) {
        callerId = id;
    }

    public Object getLock() {
        return lock;
    }

    protected synchronized void setCallPassed() {
        callPassed = true;
    }

    public synchronized boolean isCallPassed() {
        return callPassed;
    }

    public void markNonsynchronized() {
        nonsyncMethods.add(oTestedMethods.get(new Integer(callerId)));
    }

    protected void registerMethodName(String sMethodName) {        
        oTestedMethods.put(new Integer(iCounter), sMethodName);
        ++iCounter;
    }

    protected int getCallerId() {
        return callerId;
    }

    protected void runThreadsAndWait() {
        int n = oTestedMethods.size();
        Thread[] lockers = new Thread[n];
        for (int i = 0; i < n; ++i) {
            lockers[i] = new Thread(new SyncTestLocker(getInstance(i)));
            lockers[i].start();
        }
        for (int i = 0; i < n; ++i) {
            try {
                lockers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected String getResults() {
        String result;
        if (!nonsyncMethods.isEmpty()) {
            result = "Nonsynchronized methods found:\n";
            for (Iterator it = nonsyncMethods.iterator(); it.hasNext();) {
                result += it.next() + "\n";
            }
        } else {
            result = "All methods are synchronized";
        }
        return result;
    }

    public int test() {
        fillTestedMethods();
        runThreadsAndWait();
        String sRes = getResults();
        return nonsyncMethods.isEmpty() ? pass(sRes) : fail(sRes);
    }
}