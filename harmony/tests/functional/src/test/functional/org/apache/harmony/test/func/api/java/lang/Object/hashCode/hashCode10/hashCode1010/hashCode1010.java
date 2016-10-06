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


package org.apache.harmony.test.func.api.java.lang.Object.hashCode.hashCode10.hashCode1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class hashCode1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void hashCode1010() {

            String Strings[] = { "String1", "String2" };
            Object Objects[] = { (Object) Strings[0], 
                                 (Object) Strings[1],
                                 (Object) Strings[1]
                               };
        label: {
            int base = 0;
//-1
// Whenever it is invoked on the same object more than once 
// during an execution of a Java application, 
// the hashCode method must consistently return the same integer, 
// provided no information used in equals comparisons on the object is modified.

            int hc = Objects[0].hashCode();
            int i;
            for ( i = 0; i < 10 ; i++ ) {
                 if ( i == 9 )
                     Objects[0] = Objects[1];
                 if ( Objects[0].hashCode() != hc )
                     break;
            }
            if ( i != 9 )
                results[base + 1] = false;
//-1)
            base += 1;
//-2
// If two objects are equal according to the equals(Object) method,
// then calling the hashCode method on each of the two objects
// must produce the same integer result. 

            if ( ! Objects[1].equals(Objects[2]) ) {
                addLog("ERROR: ! Objects[1].equals(Objects[2])" );
                results[results.length - 2] = false;
                break label; 
            }
            results[base + 1] = Objects[2].hashCode() == Objects[1].hashCode() ;
//-2)
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

            addLog("*********  Test hashCode1010 begins ");
hashCode1010();
            addLog("*********  Test hashCode1010 results: ");

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
        System.exit(new hashCode1010().test());
    }
}



