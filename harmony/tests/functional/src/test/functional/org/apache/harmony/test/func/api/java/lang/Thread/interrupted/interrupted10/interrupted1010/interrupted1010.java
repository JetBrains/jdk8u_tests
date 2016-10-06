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


package org.apache.harmony.test.func.api.java.lang.Thread.interrupted.interrupted10.interrupted1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class interrupted1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object itrdObjects[] = { null, new Object(), new Object() };

        boolean bitrd[] = { true, true, true, true };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void interrupted1010() {

            Threadinterrupted1010 t = new Threadinterrupted1010("t1");
            label: {
                synchronized(itrdObjects[1]) {
                    synchronized(itrdObjects[2]) {
                        try {

                            t.start();
                            itrdObjects[1].wait();

                            t.interrupt();
                            itrdObjects[2].wait();

                            if ( ! bitrd[1] || ! bitrd[2] )
                                break label;

                            t.interrupt();
                            itrdObjects[1].wait();

                        } catch (InterruptedException e) {
                            addLog("ERROR: InterruptedException while wait()");
                            results[results.length -1] = false;
                            break label;
                        }
                    }
                }
//-1
                results[1] = bitrd[1];
//-1)
//-2
                results[2] = bitrd[2];
//-2)
//-3
                results[3] = bitrd[3];
//-3)
            }



        return ;
    }

class Threadinterrupted1010 extends Thread {
    Threadinterrupted1010(String s) {super(s);}
    public void run() {
        label: {
            synchronized(itrdObjects[1]) {
                itrdObjects[1].notify();
            }
            synchronized(itrdObjects[2]) {
                itrdObjects[2].notify();
                if ( Thread.interrupted() ) {
                    if ( Thread.interrupted() ) {
                        bitrd[2] = false;
                        break label;
                    }
                } else {
                    bitrd[1] = false;
                    break label;
                }
            }
            synchronized(itrdObjects[1]) {
                itrdObjects[1].notify();
                if ( ! Thread.interrupted() )
                    bitrd[3] = false;
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

            addLog("*********  Test interrupted1010 begins ");
interrupted1010();
            addLog("*********  Test interrupted1010 results: ");

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
        System.exit(new interrupted1010().test());
    }
}



