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


package org.apache.harmony.test.func.api.java.lang.Thread.start.start10.start1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class start1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
    Object      start1Objects[] = { null, new Object(), new Object() };
    ThreadGroup start1Tgs[]     = { null, new ThreadGroup("tg1"),
                                          new ThreadGroup("tg2"),
                                          new ThreadGroup("tg3") };
    String      start1Names[]   = { null, "t1", "t2", "t3", "t4", "t5" };
    int         start1Counts[]  = { 0, 0, 0 };
    boolean     start1B[]       = { false, true, true, true, true };
    Thread      start1T[]       = {null, null, null, null, null, null };
    Runnable    start1R[]       = { null, (Runnable) new Threadstart10101(),
                                          (Runnable) new Threadstart10102()
                               };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void start1010() {

            start1T[1] = new Thread( start1Tgs[1], start1R[1], start1Names[1]);
            start1T[2] = new Thread( start1Tgs[2], start1R[2], start1Names[2]);
            start1T[3] = new Thread( start1Tgs[3], start1R[0], start1Names[3]);

        label: {
            try {
                synchronized(start1Objects[1]) {
                    synchronized(start1Objects[2]) {
                        start1T[1].start();
                        start1Objects[1].wait();
                    }
                    start1T[1].join();
                    start1T[2].join();
                    start1T[3].join();
addLog("-----start1Counts[1] = " + start1Counts[1]);
addLog("-----start1Counts[2] = " + start1Counts[2]);
                    start1B[2] = ( start1Counts[1] == 1 );
                    start1B[3] = ( start1Counts[2] == 1 );
                }
            } catch (InterruptedException e) {
                addLog("ERROR: InterruptedException is caught");
                results[results.length -1] = false;
            }
//-1
            results[1] = start1B[3];
//-1)
//-2
            results[2] = start1B[2];
//-2)
        }
        return ;
    }

class Threadstart10102 implements Runnable {
    Threadstart10102() {}

    public void run() {
addLog(Thread.currentThread().getName());

        start1Counts[2]++;
        if ( Thread.currentThread().getName().equals(start1Names[5]))
             start1B[3] = Thread.currentThread().isDaemon();
            
    }
}
class Threadstart10101 implements Runnable {
    Threadstart10101() {}

    public void run() {
addLog(Thread.currentThread().getName());
        start1Counts[1]++;
        String s = Thread.currentThread().getName();
        if ( s.equals(start1Names[1]) ) {
            synchronized(start1Objects[1]) {
                start1Objects[1].notify();
                start1T[2].start();
            }
            synchronized(start1Objects[2]) {
            }
            start1T[3].start();
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

            addLog("*********  Test start1010 begins ");
start1010();
            addLog("*********  Test start1010 results: ");

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
        System.exit(new start1010().test());
    }
}



