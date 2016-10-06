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


package org.apache.harmony.test.func.api.java.lang.reflect.Array.get.get10.get1010;


import java.lang.reflect.Array;

import org.apache.harmony.share.Test;
import org.apache.harmony.test.func.share.MyLog;


public class get1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
    Object myObjects[] = { null                             ,
                           new Boolean   (true)             ,
                           new Byte      (Byte.MAX_VALUE)   ,
                           new Character ('c')              ,
                           new Double    (Double.MAX_VALUE) ,
                           new Float     (Float.MAX_VALUE)  ,
                           new Integer   (Integer.MAX_VALUE),
                           new Long      (Long.MAX_VALUE)   ,
                           new Short     (Short.MAX_VALUE)  ,
                           (Object) new String("ObjectToTest")
                         };

    Object myObject = (Object) new String("ObjectToTest");

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void get1010() {

       int i;     
//-1
        for ( i = 0; i < myObjects.length; i++ ) {

            Object obj = Array.get(myObjects, i);
            if ( (i == 0 && obj != null) || ( i > 0 && ! obj.equals(myObjects[i]) ) )
                results[i] = false;
        }
//-1)
//-2
        i++;
        try { Array.get(null, 1);
            results[i] = false;
            addLog("ERROR: testcase2: ");
        } catch ( NullPointerException e ) {
            addLog("correct NullPointerException");
        }
//-2)
//-3
        i++;
        try { Array.get(myObjects, myObjects.length);
            results[i] = false;
            addLog("ERROR: testcase3: ");
        } catch ( ArrayIndexOutOfBoundsException e ) {
            addLog("correct ArrayIndexOutOfBoundsException");
        }
//-3)
//-4
        i++;
        try { Array.get(myObjects, -1);
            results[i] = false;
            addLog("ERROR: testcase4: ");
        } catch ( ArrayIndexOutOfBoundsException e ) {
            addLog("correct ArrayIndexOutOfBoundsException");
        }
//-4)
//-5
        i++;
        try { Array.get(myObject, 1);
            results[i] = false;
            addLog("ERROR: testcase5: ");
        } catch ( IllegalArgumentException e ) {
            addLog("correct IllegalArgumentException");
        }
//-5)
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




