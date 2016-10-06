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


package org.apache.harmony.test.func.api.java.lang.Thread.currentThread.currentThread10.currentThread1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class currentThread1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object ctObjects[] = { null, new Object(), new Object() };

        ThreadcurrentThread1010 ctt1 = new ThreadcurrentThread1010();

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void currentThread1010() {

//-1
            results[1] = ! Thread.currentThread().equals(ctt1) ;
//-1)
//-2
            label: {
                synchronized(ctObjects[1]) {
                        ctt1.start();
                        try {
                            ctObjects[1].wait();
                        } catch (InterruptedException e) {
                            addLog("ERROR: Exception : " + e );
                            results[results.length -1] = false;
                            break label;
                        }
                    synchronized(ctObjects[2]) {
                    }
                }
            }
//-2)
        return ;
    }

class ThreadcurrentThread1010 extends Thread {
    ThreadcurrentThread1010() {super();}
    public void run() {
        synchronized(ctObjects[2]) {
            results[results.length -2] = ( 
                    Thread.currentThread().equals(ctt1) ) ;
            synchronized(ctObjects[1]) {
                ctObjects[1].notify();
            }
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

            addLog("*********  Test currentThread1010 begins ");
currentThread1010();
            addLog("*********  Test currentThread1010 results: ");

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
        System.exit(new currentThread1010().test());
    }
}



