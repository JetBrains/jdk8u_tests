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


package org.apache.harmony.test.func.api.java.lang.Object.notifyAll.notifyAll10.notifyAll1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class notifyAll1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object naObjects[] = { null, new Object(), new Object() };
        String   naNames[] = { null, "t1", "t2" };

        int naCounts[] = { 0, 0 };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void notifyAll1010() {

            ThreadnotifyAll1010 nat1[] = { null,
                                      new ThreadnotifyAll1010(naNames[1]),
                                      new ThreadnotifyAll1010(naNames[2])
                                    };
        label: {

            synchronized(naObjects[2]) {
                for ( int i = 1; i < nat1.length; i++ ) {
                    try {
                            nat1[i].start();
                            naObjects[2].wait();
                    } catch (InterruptedException e) {
                            addLog("ERROR: unexpectead InterruptedException");
                            results[results.length -1] = false;
                            break label;
                    }
                }
            }
//-1
            try {
                naObjects[1].notifyAll();
                results[1] = false;
            } catch (IllegalMonitorStateException e) {
            }
//-1)
//-2
            synchronized(naObjects[1]) {
                naCounts[0]++; naCounts[1]++;
                naObjects[1].notifyAll();
                try {
                    naObjects[1].wait(60000);
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException " +
                                               "while naObects[1].wait();" );
                    results[results.length -1] = false;
                    break label;
                }
                results[2] = ( (naCounts[0] == 2) & (naCounts[1] == 2) ) ;

addLog("---- naCounts[0] = "+naCounts[0] 
+" naCounts[1] = "+naCounts[1] +" res="+results[2] );

                naObjects[1].notifyAll();
            }
            for ( int i = 1; i < nat1.length; i++ ) {
                try {
                    nat1[i].join();
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException " +
                                               "while nt1[i].join(); i=" +i );
                    results[results.length -1] = false;
                    break label;
                }
            }
//-2)
        } // label
        return ;
    }

class ThreadnotifyAll1010 extends Thread {
    ThreadnotifyAll1010(String s) {super(s);}
    public void run() {
        label: {
            synchronized(naObjects[1]) {
                synchronized(naObjects[2]) {
                    naObjects[2].notify();
                }
                try {
                    if ( this.getName().equals(naNames[1]) ) {
                        while ( naCounts[0] < 1 ) {
                            naObjects[1].wait();
                            naCounts[0]++;
                        }
                    }
                    if ( this.getName().equals(naNames[2]) ) {
                        while ( naCounts[1] < 1 ) {
                            naObjects[1].wait();
                            naCounts[1]++;
                        }
                    }
                } catch (InterruptedException e) {
                    addLog("ERROR: InterruptedException " +
                                       "while naObjects[1].wait(); thread=" +this.getName() );
                    results[results.length -1] = false;
                    break label;
                }
            }
        } // label
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

            addLog("*********  Test notifyAll1010 begins ");
notifyAll1010();
            addLog("*********  Test notifyAll1010 results: ");

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
        System.exit(new notifyAll1010().test());
    }
}



