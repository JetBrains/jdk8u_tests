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


package org.apache.harmony.test.func.api.java.lang.Thread.start.start10.start1011;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class start1011 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    Object  start2Objects[] = { null, new Object(), new Object() };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void start1011() {


        Threadstart1011 t[] = { null, new Threadstart1011("t1"),  
                                      new Threadstart1011("t2") };
//-1
        synchronized(start2Objects[1]) {
            try {
                t[1].start();
            } catch (IllegalThreadStateException e) {
                results[1] = false;
            }
        }
//-1)
//-2
        synchronized(start2Objects[1]) {
            synchronized(start2Objects[2]) {
                t[2].start();
                try {
                    start2Objects[1].wait();
                    t[2].start();
                    results[2] = false;
                } catch (IllegalThreadStateException e) {
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException is caught");
                    results[results.length -1] = false;
                }
            }
        }
//-2)
        try {
            t[1].join();
            t[2].join();
        } catch (InterruptedException e) {
                addLog("ERROR: InterruptedException is caught");
                results[results.length -1] = false;
        }
        return ;
    }

class Threadstart1011 extends Thread {
    Threadstart1011(String name) {super(name);}

    public void run() {
addLog(Thread.currentThread().getName());
        synchronized(start2Objects[1]) {
            start2Objects[1].notify();
        }
        synchronized(start2Objects[2]) {
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

            addLog("*********  Test start1011 begins ");
start1011();
            addLog("*********  Test start1011 results: ");

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
        System.exit(new start1011().test());
    }
}



