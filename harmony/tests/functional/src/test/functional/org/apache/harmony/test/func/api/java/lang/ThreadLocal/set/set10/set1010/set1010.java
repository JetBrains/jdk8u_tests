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


package org.apache.harmony.test.func.api.java.lang.ThreadLocal.set.set10.set1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class set1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object tlsObjects[] = { null, new Object(), new Object() };
        String  tlsNames[]  = { null, "t1", "t2" };

            ThreadLocal tlsObjects2[][] = { { null, null },
                                            { null, null }
                                          };
        int tlsNextSerialNumber = 0;

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void set1010() {

            Threadset1010 t1[] = { null,
                                     new Threadset1010(tlsNames[1] ),
                                     new Threadset1010(tlsNames[2] )
                                   };

             Object sets[][] = { { (Object) new Integer(tlsNextSerialNumber++),
                                   (Object) new Integer(tlsNextSerialNumber++) },
                                 { (Object) new Integer(tlsNextSerialNumber++),
                                   (Object) new Integer(tlsNextSerialNumber++) }
                               };
        label: {
            synchronized(tlsObjects[2]) {
                synchronized(tlsObjects[1]) {
                    for (int j = 1; j < t1.length; j++ ) {
                        try {
                            t1[j].start();
                            tlsObjects[1].wait(60000);
                            tlsObjects[2].wait(60000);
                        } catch (InterruptedException e) {
                            addLog("ERROR: unexpectead InterruptedException");
                            results[results.length -1] = false;
                            break label;
                        }
                    }
//-1
                    for ( int i = 1; i <= 2; i++ ) {
                        for ( int j = 1; j <= 2; j++ ) {
                            tlsObjects2[i-1][j-1].set( sets[i-1][j-1] );
                            boolean b = tlsObjects2[i-1][j-1].get().equals( sets[i-1][j-1] );
                            results[i* 2 + j] = b;

addLog("--- i="+i +" j="+j +" res="+b );
                        }
                    }
//-1)
                }
            }
        } // label
        return ;
    }

class Threadset1010 extends Thread {
    Threadset1010(String s) {super(s);}
    public void run() {

        synchronized(tlsObjects[1]) {
            tlsObjects[1].notify();
        }

        if ( this.getName().equals(tlsNames[1] ) ) {
            tlsObjects2[0][0] = new ThreadLocal() ;
            tlsObjects2[0][1] = new ThreadLocal() {
                    protected synchronized Object initialValue() {
                        return new Integer(tlsNextSerialNumber++);
                    }
                };
        }
        if ( this.getName().equals(tlsNames[2] ) ) {
            tlsObjects2[1][0] = new ThreadLocal() ;
            tlsObjects2[1][1] = new ThreadLocal() {
                    protected synchronized Object initialValue() {
                        return new Integer(tlsNextSerialNumber++);
                    }
                };
        }

        synchronized(tlsObjects[2]) {
            tlsObjects[2].notify();
        }
        synchronized(tlsObjects[1]) {
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

            addLog("*********  Test set1010 begins ");
set1010();
            addLog("*********  Test set1010 results: ");

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
        System.exit(new set1010().test());
    }
}



