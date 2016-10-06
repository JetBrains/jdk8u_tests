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


package org.apache.harmony.test.func.api.java.lang.Thread.Thread.Thread10.Thread1015;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class Thread1015 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
    Object      trsObjects[] = { null, new Object(), new Object() };
    ThreadGroup trsTgs[]     = { null, new ThreadGroup("tg1"),
                                       new ThreadGroup("tg2"),
                                       new ThreadGroup("tg3") };
    String      trsNames[]   = { null, "t1", "t2", "t3", "t4", "t5" };
    int         trsCounts[]  = { 0, 0, 0 };
    boolean     trsB[]       = { false, true, true, true, true };
    Thread      trsT[]       = {null, null, null, null, null, null };
    Runnable    trsR[]       = { null, (Runnable) new ThreadThread10151(),
                                       (Runnable) new ThreadThread10152()
                               };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void Thread1015() {

            trsT[1] = new Thread( trsTgs[1], trsR[1], trsNames[1]);
            trsT[2] = new Thread( trsTgs[2], trsR[2], trsNames[2]);
            trsT[3] = new Thread( trsTgs[3], trsR[0], trsNames[3]);
            trsT[4] = new Thread( trsTgs[2], trsR[1], trsNames[4]);
            trsT[4].setDaemon(true);

//-1
            results[1] = trsT[1] != null ;
//-1)
//-2
            results[2] = trsT[1].getName().equals(trsNames[1]);
//-2)
//-3
            results[3] = trsT[1].getThreadGroup().equals(trsTgs[1]);
//-3)
//-4
            results[4] = trsT[1].getPriority() == Thread.currentThread().getPriority() ;
//-4)


        label: {
            try {
                trsT[4].start();
                trsT[4].join();
            } catch (InterruptedException e) {
                addLog("ERROR: InterruptedException is caught");
                results[results.length -1] = false;
            }
//-5
            results[5] = ( ! trsT[1].isDaemon() ) & trsT[5].isDaemon() ;
//-5)
        }
        return ;
    }

class ThreadThread10152 implements Runnable {
    ThreadThread10152() {}

    public void run() {
addLog(Thread.currentThread().getName());

        if ( Thread.currentThread().getName().equals(trsNames[5]))
             trsB[3] = Thread.currentThread().isDaemon();
            
    }
}
class ThreadThread10151 implements Runnable {
    ThreadThread10151() {}

    public void run() {
addLog(Thread.currentThread().getName());

        trsT[5] = new Thread( trsTgs[3], trsR[2], trsNames[5]);
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

            addLog("*********  Test Thread1015 begins ");
Thread1015();
            addLog("*********  Test Thread1015 results: ");

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
        System.exit(new Thread1015().test());
    }
}



