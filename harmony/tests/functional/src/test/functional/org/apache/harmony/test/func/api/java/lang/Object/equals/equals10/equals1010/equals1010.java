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


package org.apache.harmony.test.func.api.java.lang.Object.equals.equals10.equals1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class equals1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void equals1010() {

            String Strings[] = { "String1", "String2" };
            Object Objects[] = { (Object) Strings[0], 
                                 (Object) Strings[0], 
                                 (Object) Strings[1],
                                 (Object) Strings[0] 
                               };
        label: {
            int base = 0;
//-1
// It is reflexive: for any non-null reference value x, 
// x.equals(x) should return true. 

            results[base +1] =   Objects[0].equals(Objects[1]);
            results[base +2] = ! Objects[0].equals(Objects[2]);
//-1)
            base += 2;
//-2
// It is symmetric: for any non-null reference values x and y, 
// x.equals(y) should return true if and only if y.equals(x) returns true. 

            results[base +1] =   Objects[0].equals(Objects[1]) & 
                                 Objects[1].equals(Objects[0])     ;
            results[base +2] = ! Objects[2].equals(Objects[0]) ||
                               ! Objects[0].equals(Objects[2])     ;
addLog("------2");
//-2)
            base += 2;
//-3
// It is transitive: for any non-null reference values x, y, and z, 
// if x.equals(y) returns true and y.equals(z) returns true, 
// then x.equals(z) should return true.

            results[base +1] =   Objects[0].equals(Objects[1]) & 
                                 Objects[1].equals(Objects[3]) &
                                 Objects[0].equals(Objects[3]) ;
//-3)
            base += 1;
//-4
// It is consistent: for any non-null reference values x and y, 
// multiple invocations of x.equals(y) consistently return true or consistently return false,
// provided no information used in equals comparisons on the objects is modified. 
            int i;
            for ( i = 0; i < 10; i++ ) {
                if ( i == 9 )
                    Objects[1] = Objects[2];
                if ( ! Objects[0].equals(Objects[1]) )
                    break;
            }
            if ( i != 9 )
                results[base + 1] = false;
//-4)
            base += 1;
//-5
// For any non-null reference value x, x.equals(null) should return false. 
            if ( Objects[1] == null ) {
                addLog("ERROR: Objects[1] == null ");
                results[results.length - 2] = false;
                break label; 
            }
            results[base + 1] = ! Objects[1].equals(null);
//-5)
        } // label
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

            addLog("*********  Test equals1010 begins ");
equals1010();
            addLog("*********  Test equals1010 results: ");

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
        System.exit(new equals1010().test());
    }
}



