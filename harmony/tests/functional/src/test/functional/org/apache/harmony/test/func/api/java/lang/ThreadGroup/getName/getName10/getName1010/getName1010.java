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


package org.apache.harmony.test.func.api.java.lang.ThreadGroup.getName.getName10.getName1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class getName1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void getName1010() {

            ThreadGroup tgObjects[] = { null,
                                       new ThreadGroup("tg1"),
                                       new ThreadGroup("tg2"),
                                       new ThreadGroup("tg3"),
                                       null, null, null
                                     };
                       tgObjects[4] =   new ThreadGroup(tgObjects[2], "tg2");
                       tgObjects[5] =   new ThreadGroup(tgObjects[3], "tg1");
                       tgObjects[6] =   new ThreadGroup(tgObjects[3], "tg3");

            boolean equals[][] = { {true, false, false, false, true, false},
                                   {false, true, false, true, false, false},
                                   {false, false, true, false, false, true},
                                   {false, true, false, true, false, false},
                                   {true, false, false, false, true, false},
                                   {false, false, true, false, false, true}
                                 };

//-1
            for ( int i = 1; i <= 6; i++ ) {
                String si = tgObjects[i].getName();
                for ( int j = 1; j <= 6; j++ ) {
                    String sj = tgObjects[j].getName();
                    results[(i-1)*6 + j-1] = si.equals(sj) == equals[i-1][j-1] ;
                }
            }
//-1)
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

            addLog("*********  Test getName1010 begins ");
getName1010();
            addLog("*********  Test getName1010 results: ");

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
        System.exit(new getName1010().test());
    }
}



