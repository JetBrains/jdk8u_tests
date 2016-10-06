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


package org.apache.harmony.test.func.api.java.lang.Thread.activeCount.activeCount10.activeCount1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class activeCount1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
    Object acObjects[] = { null, new Object(), new Object() };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void activeCount1010() {

            ThreadactiveCount1010 t[] = { null, null, null };
            int base = 1;
//-1
            results[base] = Thread.currentThread().activeCount() >= 1 ;
            base++;
//-1)
//-2
            t[1] = new ThreadactiveCount1010("tg1");
            t[2] = new ThreadactiveCount1010("tg1");
            results[base] = Thread.currentThread().activeCount() >= 1;
            base++;
//-2)
//-3
            label: {
                synchronized(acObjects[1]) {
                    synchronized(acObjects[2]) {
                        t[1].start();
                        try {
                            acObjects[1].wait();
                        } catch (InterruptedException e) {
                            addLog("ERROR: Exception : " + e );
                            results[results.length -1] = false;
                            break label;
                        }
                        results[base] = ( Thread.currentThread().activeCount() >= 2 ) ;
                    }
                }
                base++;
            }
//-3)

        return ;
    }

class ThreadactiveCount1010 extends Thread {
    ThreadactiveCount1010(String s) {super(s);}
    public void run() {
        synchronized(acObjects[1]) {
            acObjects[1].notify();
        }
        synchronized(acObjects[2]) {
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

            addLog("*********  Test activeCount1010 begins ");
activeCount1010();
            addLog("*********  Test activeCount1010 results: ");

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
        System.exit(new activeCount1010().test());
    }
}



