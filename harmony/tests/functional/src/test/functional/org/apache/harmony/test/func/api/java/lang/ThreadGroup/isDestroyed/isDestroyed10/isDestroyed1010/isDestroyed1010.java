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


package org.apache.harmony.test.func.api.java.lang.ThreadGroup.isDestroyed.isDestroyed10.isDestroyed1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class isDestroyed1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object isdObjects[] = { null, new Object(), new Object() };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void isDestroyed1010() {

            ThreadGroup tgObjects[] = { null,
                                        new ThreadGroup("tg1"),
                                        new ThreadGroup("tg1"),
                                        new ThreadGroup("tg2"),
                                        null, null, null
                                      };
                       tgObjects[4] = new ThreadGroup(tgObjects[2], "tg1");
                       tgObjects[5] = new ThreadGroup(tgObjects[3], "tg1");
                       tgObjects[6] = new ThreadGroup(tgObjects[3], "tg2");
            label: {
//-1
            ThreadisDestroyed1010 t1[] = { null,
                           new ThreadisDestroyed1010(tgObjects[1], "t1"),
                           new ThreadisDestroyed1010(tgObjects[2], "t2"),
                           new ThreadisDestroyed1010(tgObjects[3], "t3"),
                           new ThreadisDestroyed1010(tgObjects[4], "t4"),
                           new ThreadisDestroyed1010(tgObjects[5], "t5"),
                           new ThreadisDestroyed1010(tgObjects[6], "t6")
                         };

                synchronized(isdObjects[2]) {
                    synchronized(isdObjects[1]) {
                        for (int j = 1; j < t1.length; j++ ) {
                            try {
                                t1[j].start();
                                isdObjects[1].wait(60000);
                            } catch (InterruptedException e) {
                                addLog("ERROR: unexpectead InterruptedException");
                                results[results.length -1] = false;
                                break label;
                            }
                        }
                    }
                }
                for ( int i = 1; i < t1.length; i++) {
                    try {
                        t1[i].join();
                    } catch (InterruptedException e) {
                        addLog("ERROR: unexpectead InterruptedException");
                        results[results.length -1] = false;
                        break label;
                    }
                }

                boolean 
                states1[][] = { { true, false, false, false, false, false }, // tg1[1]
                                { true,  true, false,  true, false, false }, // tg1[2]
                                { true,  true,  true,  true,  true,  true }  // tg1[3]
                              };

                for (int i = 1; i < tgObjects.length; i++)
                    results[1] &= ! tgObjects[i].isDestroyed();
                    
                for ( int i = 1 ; i < 4 ; i++) {
                    int j;
                    int k = (i-1) * t1.length ;
                    try {
                        tgObjects[i].destroy();
                        for ( j = 1; j < t1.length; j++ ) {

                            boolean b = tgObjects[j].isDestroyed();
                            results[2 + (k + j-1)] = ( states1[i-1][j-1] == b );
                        }
                    } catch (IllegalThreadStateException e) {
                    }
                }
//-1)
            } //label:
        return ;
    }

class ThreadisDestroyed1010 extends Thread {
    ThreadisDestroyed1010(ThreadGroup tg, String s) {super(tg, s);}
    public void run() {
        synchronized(isdObjects[1]) {
            isdObjects[1].notify();
        }
        synchronized(isdObjects[2]) {
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

            addLog("*********  Test isDestroyed1010 begins ");
isDestroyed1010();
            addLog("*********  Test isDestroyed1010 results: ");

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
        System.exit(new isDestroyed1010().test());
    }
}



