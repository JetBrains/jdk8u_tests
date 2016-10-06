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


package org.apache.harmony.test.func.api.java.lang.ThreadLocal.initialValue.initialValue10.initialValue1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class initialValue1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object tlivObjects[] = { null, new Object(), new Object() };
        String   tlivNames[] = { null, "t1", "t2" };

            MyThreadLocal tlivObjects2[][] = { { null, null },
                                              { null, null }
                                            };
        int tlivNextSerialNumber = 1;

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void initialValue1010() {

            ThreadinitialValue1010 t1[] = { null,
                                     new ThreadinitialValue1010(tlivNames[1] ),
                                     new ThreadinitialValue1010(tlivNames[2] )
                                   };
        label: {
            synchronized(tlivObjects[2]) {
                synchronized(tlivObjects[1]) {
                    for (int j = 1; j < t1.length; j++ ) {
                        try {
                            t1[j].start();
                            tlivObjects[1].wait(60000);
                            tlivObjects[2].wait(60000);
                        } catch (InterruptedException e) {
                            addLog("ERROR: unexpectead InterruptedException");
                            results[results.length -1] = false;
                            break label;
                        }
                    }
//-1
                    int expected[][] = { {1,2}, {3,4} };
                    for ( int i = 1; i <= 2; i++ ) {
                        for ( int j = 1; j <= 2; j++ ) {
                            int n = ((Integer) tlivObjects2[i-1][j-1].getIV() ).intValue();
                            boolean b = n == expected[i-1][j-1];
                            results[i* 2 + j] = b;

addLog("--- i="+i +" j="+j +" res="+b +" n="+n +" exp="+expected[i-1][j-1]);
                        }
                    }
//-1)
                }
            }
        } // label
        return ;
    }

class ThreadinitialValue1010 extends Thread {
    ThreadinitialValue1010(String s) {super(s);}
    public void run() {

        synchronized(tlivObjects[1]) {
            tlivObjects[1].notify();
        }

        if ( this.getName().equals(tlivNames[1] ) ) {
            tlivObjects2[0][0] = new MyThreadLocal() ;
            tlivObjects2[0][1] = new MyThreadLocal() ;
        }
        if ( this.getName().equals(tlivNames[2] ) ) {
            tlivObjects2[1][0] = new MyThreadLocal() ;
            tlivObjects2[1][1] = new MyThreadLocal() ;
        }

        synchronized(tlivObjects[2]) {
            tlivObjects[2].notify();
        }
        synchronized(tlivObjects[1]) {
        }
    }
}

class MyThreadLocal extends ThreadLocal {
    MyThreadLocal() {super();}

    protected synchronized Object initialValue() {
        return new Integer(tlivNextSerialNumber++);
    }

    private class Test extends Object {
        Test() {super();}

        public Object getInitialValue() {
            return new MyThreadLocal().initialValue();
        }
    }

    public Object getIV() {
        Test obj = new Test();
        return obj.getInitialValue();
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

            addLog("*********  Test initialValue1010 begins ");
initialValue1010();
            addLog("*********  Test initialValue1010 results: ");

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
        System.exit(new initialValue1010().test());
    }
}



