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


package org.apache.harmony.test.func.api.java.lang.reflect.Array.set.set10.set1010;


import java.lang.reflect.Array;

import org.apache.harmony.share.Test;
import org.apache.harmony.test.func.share.MyLog;


public class set1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    Object myObjects1[] = { null                             ,
                            new Boolean   (true)             ,
                            new Byte      (Byte.MAX_VALUE)   ,
                            new Character ('c')              ,
                            new Double    (Double.MAX_VALUE) ,
                            new Float     (Float.MAX_VALUE)  ,
                            new Integer   (Integer.MAX_VALUE),
                            new Long      (Long.MAX_VALUE)   ,
                            new Short     (Short.MAX_VALUE)  ,
                            (Object) new String("ObjectToTest1")
                          };

    Object myObjects2[] = { null                             ,
                            new Boolean   (false)             ,
                            new Byte      (Byte.MIN_VALUE)   ,
                            new Character ('d')              ,
                            new Double    (Double.MIN_VALUE) ,
                            new Float     (Float.MIN_VALUE)  ,
                            new Integer   (Integer.MIN_VALUE),
                            new Long      (Long.MIN_VALUE)   ,
                            new Short     (Short.MIN_VALUE)  ,
                            (Object) new String("ObjectToTest2")
                          };

    boolean myObjects3[] = { true, false }; 

    Object myObject = (Object) new String("ObjectToTest");

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void set1010() {

       int i;     
//-1
        for ( i = 0; i < myObjects1.length; i++ ) {

            Array.set(myObjects1, i, myObjects2[i]);
            if ( ( i != 0 && ! myObjects1[i].equals(myObjects2[i]) ) || 
                 ( i == 0 && myObjects1[i] != null ) )
                results[i] = false;
        }
//-1)
//-2
        i++;
        try { Array.set(null, 1, new Object());
            results[i] = false;
            addLog("ERROR: testcase2: ");
        } catch ( NullPointerException e ) {
            addLog("correct NullPointerException");
        }
//-2)
//-3
        i++;
        try { Array.set(myObjects3, 1, null);
            results[i] = false;
            addLog("ERROR: testcase3: ");
        } catch ( IllegalArgumentException e ) {
            addLog("correct IllegalArgumentException");
        }
//-3)
//-4
        i++;
        try { Array.set(myObjects1, myObjects1.length, new Object());
            results[i] = false;
            addLog("ERROR: testcase4: ");
        } catch ( ArrayIndexOutOfBoundsException e ) {
            addLog("correct ArrayIndexOutOfBoundsException");
        }
//-4)
//-5
        i++;
        try { Array.set(myObjects1, -1, new Object());
            results[i] = false;
            addLog("ERROR: testcase5: ");
        } catch ( ArrayIndexOutOfBoundsException e ) {
            addLog("correct ArrayIndexOutOfBoundsException");
        }
//-5)
//-6
        i++;
        try { Array.set(myObject, 1, new Object());
            results[i] = false;
            addLog("ERROR: testcase6: ");
        } catch ( IllegalArgumentException e ) {
            addLog("correct IllegalArgumentException");
        }
//-6)
        return ;
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



