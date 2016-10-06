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

/*
*/


package org.apache.harmony.test.func.api.java.lang.ThreadGroup.destroy.destroy10.destroy1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class destroy1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
    static volatile Object sync = new Object();

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void destroy1010() {

        ThreadGroup[] tgs = new ThreadGroup[11];
        tgs[1] = new ThreadGroup("tg1"); // Single thread group without threads
        tgs[2] = new ThreadGroup("tg2"); // Single thread group with non-started thread
        tgs[3] = new ThreadGroup("tg3"); // Single thread group with started thread
        tgs[4] = new ThreadGroup("tg4");
        tgs[5] = new ThreadGroup(tgs[4], "tg5"); // subgroup with thread
        tgs[6] = new ThreadGroup("tg6");
        tgs[7] = new ThreadGroup(tgs[6], "tg7"); // subgroup without thread
        tgs[8] = new ThreadGroup(tgs[6], "tg8"); // subgroup with thread
        tgs[9] = new ThreadGroup(tgs[6], "tg9"); // subgroup without thread
        tgs[10] = new ThreadGroup(tgs[6], "tg10"); // subgroup without thread

        for (int i = 1; i < tgs.length; i++) {
            tgs[i].setDaemon(false);
        }

        Threaddestroy1010[] ts = new Threaddestroy1010[11];

        label: {

        try {
            tgs[1].destroy();
        } catch (Exception e) {
            results[1] = false;
            break label;
        }

        try {
            ts[2] = new Threaddestroy1010(tgs[2], "t2");
            tgs[2].destroy();
        } catch (Exception e) {
            results[2] = false;
            break label;
        }

        try {
            ts[3] = new Threaddestroy1010(tgs[3], "t3");
            synchronized (sync) {
                ts[3].start();
                tgs[3].destroy();
                // Destroyed with running thread
                results[3] = false;
                break label;
            }
        } catch (Exception e) {
            try {
                ts[3].join();
            } catch (InterruptedException ee) {
            }
        }

        try {
            ts[5] = new Threaddestroy1010(tgs[5], "t5");
            synchronized (sync) {
                ts[5].start();
                tgs[4].destroy();
                // Destroyed with subgroup with running thread
                results[4] = false;
                break label;
            }
        } catch (Exception e) {

            try {
                ts[5].join();
            } catch (InterruptedException ee) {
                // Unexpected InterruptedException
                results[5] = false;
                break label;
            }

            if (!tgs[4].isDestroyed()) {
                // Top empty group is not destroyed
                results[6] = false;
                break label;
            }

            if (tgs[5].isDestroyed()) {
                // Non-empty group was destroyed
                results[7] = false;
                break label;
            }

            try {
                tgs[5].destroy();
            } catch (Exception ee) {
                // Can't destroy with subgroup with finished thread
                results[8] = false;
                break label;
            }
        }


        try {
            ts[8] = new Threaddestroy1010(tgs[8], "t8");
            synchronized (sync) {
                ts[8].start();
                tgs[6].destroy();
                // Destroyed with non-empty subgroups
                results[9] = false;
                break label;
            }
        } catch (Exception e) {

            if (!tgs[6].isDestroyed()) {
                // Top empty group is not destroyed
                results[10] = false;
                break label;
            }

            if (!tgs[7].isDestroyed()) {
                // Empty subgroup was not destroyed - wrong groups order
                results[11] = false;
                break label;
            }

            if (tgs[8].isDestroyed()) {
                // Non-empty subgroup was destroyed
                results[12] = false;
                break label;
            }

            if (tgs[9].isDestroyed()) {
                // Group was destroyed - wrong groups order
                results[13] = false;
                break label;
            }

            if (tgs[10].isDestroyed()) {
                // Group was destroyed - wrong groups order
                results[14] = false;
                break label;
            }
        }

        } //label:
            
    }

    class Threaddestroy1010 extends Thread {

        Threaddestroy1010(ThreadGroup tg, String s) {
            super(tg, s);
        }

        public void run() {
            synchronized(sync) {
            }
        }
    }


    public int test() {

        logIndex = 0;

        String texts[] = { "Testcase FAILURE, results[#] = " ,
                           "Test P A S S E D"                ,
                           "Test F A I L E D"                ,
                           "#### U N E X P E C T E D : "     };

        int    failed   = 105;
        int    passed   = 104;
        int  unexpected = 106;

        int    toReturn = 0;
        String toPrint  = null;

        for ( int i = 0; i < results.length; i++ )
            results[i] = true;

        try {

            addLog("*********  Test destroy1010 begins ");
destroy1010();
            addLog("*********  Test destroy1010 results: ");

            boolean result = true;
            for ( int i = 1 ; i < results.length ; i++ ) {
                result &= results[i];
                if ( ! results[i] )
                    addLog(texts[0] + i);
            }
            if ( ! result ) {
                toPrint  = texts[2];
                toReturn = failed;
            }
            if ( result ) {
                toPrint  = texts[1];
                toReturn = passed;
            }
        } catch (Exception e) {
            toPrint  = texts[3] + e;
            toReturn = unexpected;
        }
        if ( toReturn != passed )
            for ( int i = 0; i < logIndex; i++ )
                MyLog.toMyLog(logArray[i]);

        MyLog.toMyLog(toPrint);
        return toReturn;
    }

    public static void main(String args[]) {
        System.exit(new destroy1010().test());
    }
}



