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


package org.apache.harmony.test.func.api.java.lang.F_ThreadGroup_setMaxPriority1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class setMaxPriority1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object smpObjects[] = { null, new Object(), new Object() };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void setMaxPriority1010() {

            ThreadGroup tgObjects[] = { null,
                                       new ThreadGroup("tg1"),
                                       new ThreadGroup("tg1"),
                                       new ThreadGroup("tg2"),
                                       null, null, null
                                     };
                       tgObjects[4] =   new ThreadGroup(tgObjects[1], "tg1");
                       tgObjects[5] =   new ThreadGroup(tgObjects[1], "tg1");
                       tgObjects[6] =   new ThreadGroup(tgObjects[2], "tg2");
            int p1, p2;
            boolean b;
            label: {
//-1
                if ( Thread.MAX_PRIORITY == Thread.MIN_PRIORITY ) {
                    tgObjects[1].setMaxPriority(Thread.MAX_PRIORITY);
                    addLog("ATTENTION: THE TEST IS DEGENERATE");
                    break label;
                }
//-1)
//-2
            ThreadsetMaxPriority1010 t1[] = { null,
                           new ThreadsetMaxPriority1010(tgObjects[1], "t11"),
                           new ThreadsetMaxPriority1010(tgObjects[2], "t12"),
                           new ThreadsetMaxPriority1010(tgObjects[3], "t13"),
                           new ThreadsetMaxPriority1010(tgObjects[4], "t14"),
                           new ThreadsetMaxPriority1010(tgObjects[5], "t15"),
                           new ThreadsetMaxPriority1010(tgObjects[6], "t16"),
                         };
                for ( int i = 1; i < t1.length; i++ ) {
                    t1[i].setPriority(Thread.MAX_PRIORITY);
                }
                synchronized(smpObjects[2]) {
                    synchronized(smpObjects[1]) {
                        for (int j = 1; j < t1.length; j++ ) {
                            try {
                                t1[j].start();
                                smpObjects[1].wait(60000);
                            } catch (InterruptedException e) {
                                addLog("ERROR: unexpectead InterruptedException");
                                results[results.length -1] = false;
                                break label;
                            }
                        }
                    }
                    for ( int i = 1 ; i <= 3 ; i++ ) {
                        p1 = tgObjects[i].getMaxPriority();
                        tgObjects[i].setMaxPriority(Thread.MAX_PRIORITY -1);
                        p2 = tgObjects[i].getMaxPriority();
                    }
                    for ( int i = 1; i < t1.length ; i++ ) {
                        results[1] |= ( t1[i].getPriority() == Thread.MAX_PRIORITY );
                        b = ( t1[i].getPriority() == Thread.MAX_PRIORITY );
                        results[1] |= b;
                    }
                }
//-2)
//-3
            ThreadsetMaxPriority1010 t2[] = { null,
                           new ThreadsetMaxPriority1010(tgObjects[1], "t21"),
                           new ThreadsetMaxPriority1010(tgObjects[2], "t22"),
                           new ThreadsetMaxPriority1010(tgObjects[3], "t23"),
                           new ThreadsetMaxPriority1010(tgObjects[4], "t24"),
                           new ThreadsetMaxPriority1010(tgObjects[5], "t25"),
                           new ThreadsetMaxPriority1010(tgObjects[6], "t26"),
                         };
                for ( int i = 1; i < t1.length; i++ ) {
                    t2[i].setPriority(Thread.MAX_PRIORITY);
                }
                synchronized(smpObjects[2]) {
                    synchronized(smpObjects[1]) {
                        for (int j = 1; j < t1.length; j++ ) {
                            try {
                                t2[j].start();
                                smpObjects[1].wait(60000);
                            } catch (InterruptedException e) {
                                addLog("ERROR: unexpectead InterruptedException");
                                results[results.length -1] = false;
                                break label;
                            }
                        }
                    }
                    for ( int i = 1; i < t1.length ; i++ ) {
                        p1 = t2[i].getPriority();
                        b = ( p1 == (Thread.MAX_PRIORITY -1) );
                        results[2] |= b;
                    }
                }
//-3)
            } //label:
        return ;
    }

class ThreadsetMaxPriority1010 extends Thread {
    ThreadsetMaxPriority1010(ThreadGroup tg, String s) {super(tg, s);}
    public void run() {
        synchronized(smpObjects[1]) {
            smpObjects[1].notify();
        }
        synchronized(smpObjects[2]) {
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

            addLog("*********  Test setMaxPriority1010 begins ");
setMaxPriority1010();
            addLog("*********  Test setMaxPriority1010 results: ");

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
        System.exit(new setMaxPriority1010().test());
    }
}



