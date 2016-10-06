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


package org.apache.harmony.test.func.api.java.lang.Thread.join.join10.join1012;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class join1012 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object j3Objects[] = { null, new Object(), new Object() };
        boolean j3lock = false;

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void join1012() {

            Threadjoin1012 t = new Threadjoin1012("t1");
            long millis_1 = 0, millis_2 = 0;
            boolean b = true;
            label: {
                synchronized(j3Objects[2]) {
                    synchronized(j3Objects[1]) {
                        try {
                            t.start();
                            j3Objects[1].wait();
                        } catch (InterruptedException e) {
                            addLog("ERROR: unexpectead InterruptedException");
                            results[results.length -1] = false;
                            break label;
                        }
                    }
                    millis_1 = System.currentTimeMillis();
                }
                try {
                    t.join(60000, 0);
                    millis_2 = System.currentTimeMillis();
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException is caught");
                    results[results.length -1] = false;
                }
//-1
                results[1] = ( (millis_2 - millis_1 + 38) >= 60000 );
                System.err.println(millis_2 - millis_1 + 38);
//-1)
                j3lock = true;
                try {
                    t.join();
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException is caught");
                    results[results.length -1] = false;
                }
            }
        return ;
    }

class Threadjoin1012 extends Thread {
    Threadjoin1012(String s) {super(s);}
    public void run() {
        synchronized(j3Objects[1]) {
            j3Objects[1].notify();
        }
        synchronized(j3Objects[2]) {
        }
        while ( !j3lock ) {
            Thread.yield();
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

            addLog("*********  Test join1012 begins ");
join1012();
            addLog("*********  Test join1012 results: ");

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
        System.exit(new join1012().test());
    }
}



