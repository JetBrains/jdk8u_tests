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


package org.apache.harmony.test.func.api.java.lang.ThreadLocal.get.get10.get1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class get1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object tlgObjects[] = { null, new Object(), new Object() };
        String   tlgNames[] = { null, "t1", "t2" };

            ThreadLocal tlgObjects2[][] = { { null, null },
                                            { null, null }
                                          };
        int tlgNextSerialNumber = 0;

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void get1010() {

            Threadget1010 t1[] = { null,
                                     new Threadget1010(tlgNames[1] ),
                                     new Threadget1010(tlgNames[2] )
                                   };
            boolean gets[][] = { { true, false },
                                 { false, false }
                               };
            Object o = (Object) new Integer(tlgNextSerialNumber++);

        label: {
            synchronized(tlgObjects[2]) {
                synchronized(tlgObjects[1]) {
                    for (int j = 1; j < t1.length; j++ ) {
                        try {
                            t1[j].start();
                            tlgObjects[1].wait(60000);
                            tlgObjects[2].wait(60000);
                        } catch (InterruptedException e) {
                            addLog("ERROR: unexpectead InterruptedException");
                            results[results.length -1] = false;
                            break label;
                        }
                    }
//-1
                    for ( int i = 1; i <= 1; i++ ) {
                        for ( int j = 1; j <= 2; j++ ) {
                            boolean b = tlgObjects2[i-1][j-1].get() == null;
                            results[i* 2 + j] = b == gets[i-1][j-1] ;

addLog("--- i="+i +" j="+j +" res="+b  +" gets="+gets[i-1][j-1]);
                        }
                    }
//1)
//-2
                    for ( int i = 1; i <= 1; i++ ) {
                        for ( int j = 1; j <= 2; j++ ) {
                            boolean b = tlgObjects2[i-1][j-1].get() == null;
                            results[i* 2 + j] = b == gets[i-1][j-1] ;

addLog("--- i="+i +" j="+j +" res="+b  +" gets="+gets[i-1][j-1]);
                        }
                    }
//-2)
//-3
                    for ( int i = 1; i <= 2; i++ ) {
                        for ( int j = 1; j <= 2; j++ ) {
                            tlgObjects2[i-1][j-1].set(o);
                            boolean b = tlgObjects2[i-1][j-1].get() == null;
                            results[i* 2 + j] = b == gets[1][1] ;

addLog("--- i="+i +" j="+j +" res="+b  +" gets="+gets[1][1]);
                        }
                    }
//-3)
                }
            }
        } // label
        return ;
    }

class Threadget1010 extends Thread {
    Threadget1010(String s) {super(s);}
    public void run() {

        synchronized(tlgObjects[1]) {
            tlgObjects[1].notify();
        }

        if ( this.getName().equals(tlgNames[1] ) ) {
            tlgObjects2[0][0] = new ThreadLocal() ;
            tlgObjects2[0][1] = new ThreadLocal() {
                    protected synchronized Object initialValue() {
                        return new Integer(tlgNextSerialNumber++);
                    }
                };
        }
        if ( this.getName().equals(tlgNames[2] ) ) {
            tlgObjects2[1][0] = new ThreadLocal() ;
            tlgObjects2[1][1] = new ThreadLocal() {
                    protected synchronized Object initialValue() {
                        return new Integer(tlgNextSerialNumber++);
                    }
                };
        }

        synchronized(tlgObjects[2]) {
            tlgObjects[2].notify();
        }
        synchronized(tlgObjects[1]) {
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

            addLog("*********  Test get1010 begins ");
get1010();
            addLog("*********  Test get1010 results: ");

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
        System.exit(new get1010().test());
    }
}



