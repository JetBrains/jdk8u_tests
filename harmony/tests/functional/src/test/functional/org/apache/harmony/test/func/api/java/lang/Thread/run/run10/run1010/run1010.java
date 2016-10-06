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


package org.apache.harmony.test.func.api.java.lang.Thread.run.run10.run1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class run1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
    Object  runObjects[] = { null, new Object(), new Object() };

    int runCount[] = { 0, 0, 0 };


    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void run1010() {

        Threadrun1010 t = new Threadrun1010("t") ;

//-1
        int n1 = runCount[1];
        t.run();
        results[1] = runCount[1] == (n1 + 1) ;

        int n2 = runCount[1];
        synchronized(runObjects[1]) {
            synchronized(runObjects[2]) {
                t.start();
                try {
                    runObjects[1].wait();
                    t.run();
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException is caught");
                    results[results.length -1] = false;
                }
            }
        }
        results[2] = runCount[1] == (n2 + 2) ;

        try {
            t.join();
        } catch (InterruptedException e) {
                addLog("ERROR: InterruptedException is caught");
                results[results.length -1] = false;
        }

        int n3 = runCount[1];
        t.run();
        results[3] = runCount[1] == (n3 + 1) ;
//-1)
        return ;
    }

class Threadrun1010 extends Thread {
    Threadrun1010(String name) {super(name);}

    public void run() {
addLog(Thread.currentThread().getName());
        runCount[1]++;
        synchronized(runObjects[1]) {
            runObjects[1].notify();
        }
        synchronized(runObjects[2]) {
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

            addLog("*********  Test run1010 begins ");
run1010();
            addLog("*********  Test run1010 results: ");

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
        System.exit(new run1010().test());
    }
}



