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


package org.apache.harmony.test.func.api.java.lang.Object.wait.wait10.wait1011;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class wait1011 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object w2Objects[] = { null, new Object(), new Object() };
        String   w2Names[] = { null, "t1", "t2" };

        int w2Counts[] = { 0, 0 };

        Thread w2t0 =  null ;

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void wait1011() {

        Threadwait1011 w2t1[] = { null,
                                  new Threadwait1011(w2Names[1]),
                                  new Threadwait1011(w2Names[2])
                                };
        int TIME = 500;
        label: {
            int base = 1;
//-1
// 1) The method throws "IllegalMonitorStateException"
//    if the current thread is not the owner of this object's monitor.

            try {
                w2Objects[1].wait(TIME);
                results[base] = false;
                break label;
            } catch (IllegalMonitorStateException e) {
            } catch (InterruptedException e) {
                addLog("ERROR: InterruptedException " +
                                               "while w2Objects[1].wait(TIME);" );
                results[results.length -1] = false;
                break label;
            }
            base++;
addLog(".......testcase 1 ended");
//-1)
//-2
// 2) The method throws "IllegalArgumentException"
//    if a value of integer argument is negative.

            try {
                w2Objects[1].wait(-TIME);
                results[base] = false;
                break label;
            } catch (IllegalArgumentException e) {
            } catch (InterruptedException e) {
                addLog("ERROR: InterruptedException " +
                                               "while w2Objects[1].wait(-TIME);" );
                results[results.length -1] = false;
                break label;
            }
            base++;
addLog(".......testcase 2 ended");
//-2)

//-3
// 3) The specified amount of real time has elapsed.

            synchronized(w2Objects[1]) {
                try {
                    w2t1[1].start();
                    int n = w2Counts[0];
                    w2Objects[1].wait(60000);
                    results[base] = ( n == w2Counts[0] );
addLog("-n="+n +" w2Counts[0]="+w2Counts[0]);
                    w2Objects[1].notify();
                    w2Objects[1].wait(60000);
                    results[base] = ( n != w2Counts[0] );

addLog("-n="+n +" w2Counts[0]="+w2Counts[0]);
                } catch (InterruptedException e) {
                    addLog("ERROR: return from w2Objects[1].wait(); ");
                    results[results.length -1] = false;
                }
            }
            base++;
addLog(".......testcase 3 ended");
//-3)
//-4
// 4) Some other thread interrupts the current thread.

            w2t0 = Thread.currentThread();
            synchronized(w2Objects[2]) {
                try {
                    w2t1[2].start();
                    w2Objects[2].wait(60000*2);
                    results[base] = false;
                } catch (InterruptedException e) {
                }
            }

addLog(".......testcase 4 ended");
//-4)
            for ( int i = 1; i < w2t1.length; i++ ) {
                try {
                    w2t1[i].join();
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException " +
                                               "while w2t1[i].join(); i=" +i );
                    results[results.length -1] = false;
                    break label;
                }
            }
        } // label
        return ;
    }

class Threadwait1011 extends Thread {
    Threadwait1011(String s) {super(s);}
    public void run() {
        label: {
            try {
                if ( this.getName().equals(w2Names[1]) ) {
                    synchronized(w2Objects[1]) {
                        w2Objects[1].wait();
                        w2Counts[0]++;
                    }
                }
                if ( this.getName().equals(w2Names[2]) ) {
                    synchronized(w2Objects[2]) {
                        w2t0.interrupt();
                    }
                }
            } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException " +
                                               "Threadwait1011.w2t1[0].interrupt(); " );
                    results[results.length -1] = false;
                    break label;
            }
        } // label
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

            addLog("*********  Test wait1011 begins ");
wait1011();
            addLog("*********  Test wait1011 results: ");

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
        System.exit(new wait1011().test());
    }
}



