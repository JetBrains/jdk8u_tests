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


package org.apache.harmony.test.func.api.java.lang.Object.toString.toString10.toString1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class toString1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void toString1010() {

        int base = 0;
//-1

            String sStrings[] = { "String1", "String2" };
            Object sObjects[] = { 
                               (Object) sStrings[0], (Object) sStrings[0],
                               (Object) sStrings[1], (Object) sStrings[1]
                                 };
            boolean expected1[][] = { { true, true,   false, false },
                                      { true, true,   false, false },
                                      { false, false,  true,  true },
                                      { false, false,  true,  true }
                                    };

            int n1 = sObjects.length;
            for ( int i = 0; i < n1; i++ ) {
                String s = sObjects[i].toString();
                for ( int j = 0; j < n1; j++ ) {
                    boolean b = s.equals(sStrings[j/2]);
                    results[ base + i*n1+j ] =  b==expected1[i][j] ;
                }
            }
//-1)
            base += 20;
//-2
            Object classObjects[] = { 
                       new XYZNAME1toString1010(), new XYZNAME1toString1010(),
                       new XYZNAME2toString1010(), new XYZNAME2toString1010()
                                     };

            for ( int i = 0; i < classObjects.length; i++ ) {
                String   s = classObjects[i].toString();
                boolean b1 =  s.equals( classObjects[i].getClass().getName() + "@" +
                                        Integer.toHexString(classObjects[i].hashCode()) );
addLog("----- " +b1  + "    " + s);

                results[ base + i ] = b1 ;
            }
//-2)
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

            addLog("*********  Test toString1010 begins ");
toString1010();
            addLog("*********  Test toString1010 results: ");

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
        System.exit(new toString1010().test());
    }
}


class XYZNAME1toString1010 {
    public int i = 1;
}
class XYZNAME2toString1010 {
    public int i = 1;
}


