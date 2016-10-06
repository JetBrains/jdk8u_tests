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


package org.apache.harmony.test.func.api.java.lang.ThreadGroup.getParent.getParent10.getParent1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class getParent1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void getParent1010() {

            ThreadGroup tgObjects[] = { null,
                                       new ThreadGroup("tg1"),
                                       new ThreadGroup("tg1"),
                                       new ThreadGroup("tg2"),
                                       null, null, null
                                     };
                       tgObjects[4] =   new ThreadGroup(tgObjects[2], "tg1");
                       tgObjects[5] =   new ThreadGroup(tgObjects[3], "tg1");
                       tgObjects[6] =   new ThreadGroup(tgObjects[3], "tg2");
//-1
            for ( int i = 1; i < tgObjects.length; i++ ) {
                ThreadGroup tg11 = tgObjects[i].getParent();

                for ( int j = 1; j < tgObjects.length; j++ ) {
                    ThreadGroup tg12 = tgObjects[j].getParent();

                    int flag1 = 0;
                    int flag2 = 0;
                    for ( ; ; ) {
                        if ( flag1 == 0 )
                            if ( tg11 == null )   flag1 = 1;
                            else                 tg11 = tg11.getParent();

                        if (flag2 == 0 )
                            if ( tg12 == null )   flag2 = 1;
                            else                 tg12 = tg12.getParent();

                        if ( flag1 != 0 && flag2 != 0 )
                        break;
                    }
                }
            }
            results[1] = true;
//-1)
//-2
            boolean equals[][] = { {true,   true,  true, false, false, false},
                                   {true,   true,  true, false, false, false},
                                   {true,   true,  true, false, false, false},
                                   {false, false, false,  true, false, false},
                                   {false, false, false, false,  true,  true},
                                   {false, false, false, false,  true,  true}
                                 };
            for ( int i = 1; i < tgObjects.length; i++ ) {

                ThreadGroup tg21 = tgObjects[i].getParent();

                for ( int j = 1; j < tgObjects.length; j++ ) {

                    ThreadGroup tg22 = tgObjects[j].getParent();
                    boolean b =  tg21.equals(tg22);

                    int n = 2 + (i-1)*tgObjects.length + j -1 ;
                    results[n] = b == equals[i-1][j-1];

                }
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

            addLog("*********  Test getParent1010 begins ");
getParent1010();
            addLog("*********  Test getParent1010 results: ");

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
        System.exit(new getParent1010().test());
    }
}



