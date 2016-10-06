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


package org.apache.harmony.test.func.api.java.lang.Object.wait.wait10.wait1012;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class wait1012 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object w1Objects[] = { null, new Object(), new Object() };
        String w1Names[] = { "t1", "t2" };

        Thread w1t0 =  null ;

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void wait1012() {

        Threadwait1012 w1t1[] = { null,
                                  new Threadwait1012(w1Names[0]),
                                  new Threadwait1012(w1Names[1])
                                };
        label: {
            int base = 0;
//-1
// 1) The method throws "IllegalMonitorStateException"
//    if the current thread is not the owner of this object's monitor.

            try {
                w1Objects[1].wait();
                results[base +1] = false;
                break label;
            } catch (IllegalMonitorStateException e) {
            } catch (InterruptedException e) {
                addLog("ERROR: InterruptedException " +
                                               "while w1Objects[1].wait();" );
                results[results.length -1] = false;
                break label;
            }
            base++;
addLog(".......testcase 1 ended");
//-1)
//-2

            w1t0 = Thread.currentThread();
            synchronized(w1Objects[1]) {
                for ( int i = 1; i < w1t1.length; i++ ) {
                    try {
                        w1t1[i].start();
                        w1Objects[1].wait();
                        addLog("ERROR: return from w1Objects[1].wait(); ");
                        results[base + i] = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
            for ( int i = 1; i < w1t1.length; i++ ) {
                try {
                    w1t1[i].join();
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException " +
                                               "while w1t1[i].join(); i=" +i );
                    results[results.length -1] = false;
                    break label;
                }
            }
addLog(".......testcase 2 ended");
//-2)
        } // label
        return ;
    }

class Threadwait1012 extends Thread {
    Threadwait1012(String s) {super(s);}
    public void run() {
        label: {
            synchronized(w1Objects[1]) {
                try {
                    w1Objects[1].wait(60000);
                    w1t0.interrupt();
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException " +
                                               "Threadwait1012.w1t1[0].interrupt(); " );
                    results[results.length -1] = false;
                    break label;
                }
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

            addLog("*********  Test wait1012 begins ");
wait1012();
            addLog("*********  Test wait1012 results: ");

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
        System.exit(new wait1012().test());
    }
}



