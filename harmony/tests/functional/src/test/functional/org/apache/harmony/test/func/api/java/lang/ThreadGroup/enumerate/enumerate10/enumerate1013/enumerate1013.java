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


package org.apache.harmony.test.func.api.java.lang.ThreadGroup.enumerate.enumerate10.enumerate1013;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class enumerate1013 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object enum4Objects[] = { null, new Object(), new Object() };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void enumerate1013() {

            ThreadGroup tgObjects[] = { null,
                                       new ThreadGroup("tg1"),
                                       new ThreadGroup("tg1"),
                                       new ThreadGroup("tg2"),
                                       null, null, null
                                     };
                       tgObjects[4] =   new ThreadGroup(tgObjects[1], "tg1");
                       tgObjects[5] =   new ThreadGroup(tgObjects[1], "tg1");
                       tgObjects[6] =   new ThreadGroup(tgObjects[2], "tg2");
            Threadenumerate1013 t[] = { null,
                           new Threadenumerate1013(tgObjects[1], "t1"),
                           new Threadenumerate1013(tgObjects[2], "t2"),
                           new Threadenumerate1013(tgObjects[3], "t3"),
                           new Threadenumerate1013(tgObjects[4], "t4"),
                           new Threadenumerate1013(tgObjects[5], "t5"),
                           new Threadenumerate1013(tgObjects[6], "t6")
                         };
            ThreadGroup[] list =  {null,null};

            label:
            synchronized(enum4Objects[2]) {
                synchronized(enum4Objects[1]) {
                    for (int j = 1; j < t.length; j++ ) {
                        try {
                            t[j].start();
                            enum4Objects[1].wait(60000);
                        } catch (InterruptedException e) {
                            addLog("ERROR: unexpectead InterruptedException");
                            results[results.length -1] = false;
                            break label;
                        }
                        for ( int k = 1; k < tgObjects.length; k++ ) {
                            int n = tgObjects[k].enumerate(list, true);
                            results[j*t.length + k] = ( n <= list.length ) ;
                            n = tgObjects[k].enumerate(list, false);
                            results[j*t.length + k ] |= ( n <= list.length ) ;
                        }
                    }
                }
            }
        return ;
    }

class Threadenumerate1013 extends Thread {
    Threadenumerate1013(ThreadGroup tg, String s) {super(tg, s);}
    public void run() {
        synchronized(enum4Objects[1]) {
            enum4Objects[1].notify();
        }
        synchronized(enum4Objects[2]) {
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

            addLog("*********  Test enumerate1013 begins ");
enumerate1013();
            addLog("*********  Test enumerate1013 results: ");

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
        System.exit(new enumerate1013().test());
    }
}



