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


package org.apache.harmony.test.func.api.java.lang.F_InheritableThreadLocalTest_02;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class F_InheritableThreadLocalTest_02 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object itlcvObjects[] = { null, new Object(), new Object() };
        String   itlcvNames[] = { null, "t1", "t2" };

        MyThreadLocal            itlcvObjects1[] = { null, null };
        MyInheritableThreadLocal itlcvObjects2[] = { null, null };

        int itlcvNextSerialNumber = 1;

        ThreadchildValue1010 itlcvt1[] = { null,
                                      new ThreadchildValue1010(itlcvNames[1]),
                                      new ThreadchildValue1010(itlcvNames[2])
                                    };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void F_InheritableThreadLocalTest_02() {

        label: {
//-1
            synchronized(itlcvObjects[2]) {
                synchronized(itlcvObjects[1]) {
                    try {
                        itlcvt1[1].start();
                        itlcvObjects[1].wait(60000);
                        itlcvObjects[1].wait(60000);
                    } catch (InterruptedException e) {
                        addLog("ERROR: unexpectead InterruptedException");
                        results[results.length -1] = false;
                        break label;
                    }

                    int expected[][] = { {2,4}, {5,6} };
                    int returned[][] = { {0,0}, {0,0} };

returned[0][0] = ((Integer) itlcvObjects2[0].getCV( itlcvObjects1[0].get() )).intValue();
returned[0][1] = ((Integer) itlcvObjects2[1].getCV( itlcvObjects1[1].get() )).intValue();

//itlcvObjects1[0].set( (Object) String.valueOf(4) );
itlcvObjects2[1].set( (Object) String.valueOf(5) );

returned[1][0] = ((Integer) itlcvObjects2[0].getCV( itlcvObjects1[0].get() )).intValue();
returned[1][1] = ((Integer) itlcvObjects2[1].getCV( itlcvObjects1[1].get() )).intValue();

                    for ( int i = 0; i < 2; i++) {
                        for ( int j = 0; j < 2; j++) {
                            results[i*2+j] = expected[i][j] == returned[i][j];

addLog("---- i="+i +" j="+j +" expected="+expected[i][j] +
                                         " returned="+returned[i][j] );
                        }
                    }
                }
            }
//-1)
        } // label
        return ;
    }

class ThreadchildValue1010 extends Thread {
    ThreadchildValue1010(String s) {super(s);}
    public void run() {

        if ( this.getName().equals(itlcvNames[1]) ) {
            itlcvObjects1[0] = new MyThreadLocal() ;
            itlcvObjects1[1] = new MyThreadLocal() ;
        }
        if ( this.getName().equals(itlcvNames[2]) ) {
            itlcvObjects2[0] = new MyInheritableThreadLocal() ;
            itlcvObjects2[1] = new MyInheritableThreadLocal() ;
        }
        synchronized(itlcvObjects[1]) {
            itlcvObjects[1].notify();
        }
        if ( this.getName().equals(itlcvNames[1]) ) {
            itlcvt1[2].start();
        }

        synchronized(itlcvObjects[2]) {
            itlcvObjects[2].notify();
        }
        synchronized(itlcvObjects[1]) {
        }
    }
}

class MyThreadLocal extends ThreadLocal {
    MyThreadLocal() {super();}

    protected synchronized Object initialValue() {
        return new Integer(itlcvNextSerialNumber++);
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
class MyInheritableThreadLocal extends InheritableThreadLocal {
    MyInheritableThreadLocal() {super();}

    protected synchronized Object childValue(Object parentValue) {
        return new Integer(itlcvNextSerialNumber++);
    }

    private class Test extends Object {
        Test() {super();}

        public Object getChildValue(Object parentValue) {
            return new MyInheritableThreadLocal().childValue(parentValue);
        }
    }

    public Object getCV(Object parentValue) {
        Test obj = new Test();
        return obj.getChildValue(parentValue);
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

            addLog("*********  Test F_InheritableThreadLocalTest_02 begins ");
F_InheritableThreadLocalTest_02();
            addLog("*********  Test F_InheritableThreadLocalTest_02 results: ");

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
        System.exit(new F_InheritableThreadLocalTest_02().test());
    }
}



