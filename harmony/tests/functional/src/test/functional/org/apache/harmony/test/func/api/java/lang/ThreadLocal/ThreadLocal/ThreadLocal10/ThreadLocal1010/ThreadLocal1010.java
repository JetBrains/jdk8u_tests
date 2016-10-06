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


package org.apache.harmony.test.func.api.java.lang.ThreadLocal.ThreadLocal.ThreadLocal10.ThreadLocal1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class ThreadLocal1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object tlObjects[] = { null, new Object(), new Object() };
        String   tlNames[] = { null, "t1", "t2"};

            ThreadLocal tlObjects2[][] = { { null, null },
                                           { null, null }
                                         };
        int tlNextSerialNumber = 0;

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void ThreadLocal1010() {

//-1
            ThreadThreadLocal1010 t1[] = { null,
                                     new ThreadThreadLocal1010(tlNames[1] ),
                                     new ThreadThreadLocal1010(tlNames[2] )
                                   };
        label: {
            synchronized(tlObjects[2]) {
                synchronized(tlObjects[1]) {
                    for (int j = 1; j < t1.length; j++ ) {
                        try {
                            t1[j].start();
                            tlObjects[1].wait(60000);
                            tlObjects[2].wait(60000);
                        } catch (InterruptedException e) {
                            addLog("ERROR: unexpectead InterruptedException");
                            results[results.length -1] = false;
                            break label;
                        }
                    }
                    for ( int i = 1; i <= 2; i++ ) {
                        for ( int j = 1; j <= 2; j++ ) {
                            boolean b = tlObjects2[i-1][j-1] != null ;
                            results[i* 2 + j] = b ;

addLog("--- i="+i +" j="+j + " res="+b );
                        }
                    }
                }
            }
        } // label
//-1)

        return ;
    }

class ThreadThreadLocal1010 extends Thread {
    ThreadThreadLocal1010(String s) {super(s);}
    public void run() {

        synchronized(tlObjects[1]) {
            tlObjects[1].notify();
        }

        if ( this.getName().equals(tlNames[1] ) ) {
            tlObjects2[0][0] = new ThreadLocal() ;
            tlObjects2[0][1] = new ThreadLocal() {
                    protected synchronized Object initialValue() {
                        return new Integer(tlNextSerialNumber++);
                    }
                };
        }
        if ( this.getName().equals(tlNames[2] ) ) {
            tlObjects2[1][0] = new ThreadLocal() ;
            tlObjects2[1][1] = new ThreadLocal() {
                    protected synchronized Object initialValue() {
                        return new Integer(tlNextSerialNumber++);
                    }
                };
        }

        synchronized(tlObjects[2]) {
            tlObjects[2].notify();
        }
        synchronized(tlObjects[1]) {
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

            addLog("*********  Test ThreadLocal1010 begins ");
ThreadLocal1010();
            addLog("*********  Test ThreadLocal1010 results: ");

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
        System.exit(new ThreadLocal1010().test());
    }
}



