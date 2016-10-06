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


package org.apache.harmony.test.func.api.java.lang.Thread.getThreadGroup.getThreadGroup10.getThreadGroup1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class getThreadGroup1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object gtgObjects[] = { null, new Object(), new Object() };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void getThreadGroup1010() {

            ThreadgetThreadGroup1010 t = new ThreadgetThreadGroup1010();
//-1
            results[1] = ( t.getThreadGroup() != null ) ;
//-1)
//-2
            label: {
                synchronized(gtgObjects[2]) {
                    synchronized(gtgObjects[1]) {
                        t.start();
                        try {
                            gtgObjects[1].wait();
                        } catch (InterruptedException e) {
                            addLog("ERROR: InterruptedException is caught");
                            results[results.length -1] = false;
                            break label;
                        }
                    }
                    results[2] = ( t.getThreadGroup() != null ) ;
                }
            }
//-2)
//-3
            try {
                t.join();
                results[3] = ( t.getThreadGroup() == null ) ;
            } catch (InterruptedException e) {
                addLog("ERROR: InterruptedException is caught");
                results[results.length -1] = false;
            }
//-3)

        return ;
    }

class ThreadgetThreadGroup1010 extends Thread {
    ThreadgetThreadGroup1010() {super();}
    public void run() {
            synchronized(gtgObjects[1]) {
                gtgObjects[1].notify();
            }
        synchronized(gtgObjects[2]) {
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

            addLog("*********  Test getThreadGroup1010 begins ");
getThreadGroup1010();
            addLog("*********  Test getThreadGroup1010 results: ");

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
        System.exit(new getThreadGroup1010().test());
    }
}



