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


package org.apache.harmony.test.func.api.java.lang.ThreadGroup.interrupt.interrupt10.interrupt1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class interrupt1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object itrObjects[] = { null, new Object(), new Object() };

        boolean ba[] = {true,true,true,true,true,true,true};

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void interrupt1010() {

            ThreadGroup tgObjects[] = { null,
                                       new ThreadGroup("tg1"),
                                       new ThreadGroup("tg1"),
                                       new ThreadGroup("tg2"),
                                       null, null, null
                                     };
                       tgObjects[4] =   new ThreadGroup(tgObjects[2], "tg1");
                       tgObjects[5] =   new ThreadGroup(tgObjects[3], "tg1");
                       tgObjects[6] =   new ThreadGroup(tgObjects[3], "tg2");
          label: {
            Threadinterrupt1010 t1[] = { null,
                           new Threadinterrupt1010(tgObjects[1], "t1"),
                           new Threadinterrupt1010(tgObjects[2], "t2"),
                           new Threadinterrupt1010(tgObjects[3], "t3"),
                           new Threadinterrupt1010(tgObjects[4], "t4"),
                           new Threadinterrupt1010(tgObjects[5], "t5"),
                           new Threadinterrupt1010(tgObjects[6], "t6")
                         };

            synchronized(itrObjects[2]) {
                synchronized(itrObjects[1]) {
                    for ( int i = 1; i < t1.length; i++ ) {
                        try {
                            t1[i].start();
                            itrObjects[1].wait(60000);
                        } catch (InterruptedException e) {
                            addLog("ERROR: unexpectead InterruptedException");
                            results[results.length -1] = false;
                            break label;
                        }
                    }
                }
                tgObjects[1].interrupt();
            }
            boolean expected1[] = {true, true,false,false,false,false,false};
            for (int i = 1; i < t1.length; i++ ) { 
                try {
                    t1[i].join();
                    results[i] = ba[i] == expected1[i];
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException is caught");
                    results[results.length -1] = false;
                }
            }
/*-----------------------*/
            Threadinterrupt1010 t2[] = { null,
                           new Threadinterrupt1010(tgObjects[1], "t1"),
                           new Threadinterrupt1010(tgObjects[2], "t2"),
                           new Threadinterrupt1010(tgObjects[3], "t3"),
                           new Threadinterrupt1010(tgObjects[4], "t4"),
                           new Threadinterrupt1010(tgObjects[5], "t5"),
                           new Threadinterrupt1010(tgObjects[6], "t6")
                         };

            synchronized(itrObjects[2]) {
                synchronized(itrObjects[1]) {
                    for ( int i = 1; i < t1.length; i++ ) {
                        try {
                            t2[i].start();
                            itrObjects[1].wait(60000);
                        } catch (InterruptedException e) {
                            addLog("ERROR: unexpectead InterruptedException");
                            results[results.length -1] = false;
                            break label;
                        }
                    }
                }
                tgObjects[2].interrupt();
            }
            boolean expected2[] = {true, false,true,false,true,false,false};
            for (int i = 1; i < t2.length; i++ ) { 
                try {
                    t2[i].join();
                    results[i + 7] = ba[i] == expected2[i];
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException is caught");
                    results[results.length -1] = false;
                }
            }
/*-----------------------*/
            Threadinterrupt1010 t3[] = { null,
                           new Threadinterrupt1010(tgObjects[1], "t1"),
                           new Threadinterrupt1010(tgObjects[2], "t2"),
                           new Threadinterrupt1010(tgObjects[3], "t3"),
                           new Threadinterrupt1010(tgObjects[4], "t4"),
                           new Threadinterrupt1010(tgObjects[5], "t5"),
                           new Threadinterrupt1010(tgObjects[6], "t6")
                         };

            synchronized(itrObjects[2]) {
                synchronized(itrObjects[1]) {
                    for ( int i = 1; i < t3.length; i++ ) {
                        try {
                            t3[i].start();
                            itrObjects[1].wait(60000);
                        } catch (InterruptedException e) {
                            addLog("ERROR: unexpectead InterruptedException");
                            results[results.length -1] = false;
                            break label;
                        }
                    }
                }
                tgObjects[3].interrupt();
            }
            boolean expected3[] = {true, false,false,true,false,true,true};
            for (int i = 1; i < t3.length; i++ ) { 
                try {
                    t3[i].join();
                    results[i + 14] = ba[i] == expected3[i];
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException is caught");
                    results[results.length -1] = false;
                }
            }
        }
        return ;
    }

class Threadinterrupt1010 extends Thread {
    Threadinterrupt1010(ThreadGroup tg, String s) {super(tg, s);}
    public void run() {
        synchronized(itrObjects[1]) {
            itrObjects[1].notify();
        }
        synchronized(itrObjects[2]) {
        }
        String s = this.getName();
        if ( s.equals("t1") ) 
            ba[1] = this.isInterrupted();
        if ( s.equals("t2") ) 
            ba[2] = this.isInterrupted();
        if ( s.equals("t3") ) 
            ba[3] = this.isInterrupted();
        if ( s.equals("t4") ) 
            ba[4] = this.isInterrupted();
        if ( s.equals("t5") ) 
            ba[5] = this.isInterrupted();
        if ( s.equals("t6") ) 
            ba[6] = this.isInterrupted();
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

            addLog("*********  Test interrupt1010 begins ");
interrupt1010();
            addLog("*********  Test interrupt1010 results: ");

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
        System.exit(new interrupt1010().test());
    }
}



