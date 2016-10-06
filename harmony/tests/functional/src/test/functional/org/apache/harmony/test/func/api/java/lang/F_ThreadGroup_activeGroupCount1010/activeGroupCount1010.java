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


package org.apache.harmony.test.func.api.java.lang.F_ThreadGroup_activeGroupCount1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class activeGroupCount1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 
        Object agcObjects[] = { null, new Object(), new Object() };

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void activeGroupCount1010() {


    ThreadGroup tgObjects[] = { null,null,null,null,null,null,null };

int actives1[][] = { {0, 9, 9, 9, 9, 9 }, //tg[1] is created
                     {0, 0, 9, 9, 9, 9 }, //tg[2] is created
                     {0, 0, 0, 9, 9, 9 }, //tg[3] is created
                     {0, 1, 0, 0, 9, 9 }, //tg[4] is created
                     {0, 1, 1, 0, 0, 9 }, //tg[5] is created
                     {0, 1, 2, 0, 0, 0 }  //tg[6] is created
                  };

        label: {
//-1
            for (int i = 1; i <= 6; i++) {
              switch (i) {
                case 1: tgObjects[1] = new ThreadGroup("tg11"); break;
                case 2: tgObjects[2] = new ThreadGroup("tg12"); break;
                case 3: tgObjects[3] = new ThreadGroup("tg13"); break;

                case 4: tgObjects[4] = new ThreadGroup(tgObjects[2], "tg21"); break;
                case 5: tgObjects[5] = new ThreadGroup(tgObjects[3], "tg31"); break;
                case 6: tgObjects[6] = new ThreadGroup(tgObjects[3], "tg32"); break;
               default: results[results.length-1] = false;
                  addLog("TEST ERROR: default case");
                  break label;
              }

                for (int j = 1; j <= i; j++ ) {

                    int n = tgObjects[j].activeGroupCount();
                    int k = actives1[i-1][j-1];
                    int m = (i-1)* 6 + j-1;
                    results[m] &= ( n >= k );
                }
            }

//-1)
//-2
int actives2[][] = { {0, 9, 9, 9, 9, 9 }, //tg[1] is destroyed
                     {0, 0, 9, 9, 9, 9 }, //tg[2] is destroyed
                     {0, 0, 0, 9, 9, 9 }, //tg[3] is destroyed
                     {0, 0, 0, 0, 9, 9 }, //tg[4] is destroyed
                     {0, 1, 0, 0, 0, 9 }, //tg[5] is destroyed
                     {0, 1, 1, 0, 0, 0 }  //tg[6] is destroyed
                  };

            for (int i = 6; i > 0; i--) {

                tgObjects[i].destroy();

                for (int j = 1; j <= i; j++ ) {

                    int n = tgObjects[j].activeGroupCount();
                    int k = actives2[i-1][j-1];
                    int m = (i-1)* 6 + j-1 + 50;
                    results[m] &= ( n >= k );
                }
            }
//-2)
        }
        return ;
    }

class ThreadactiveGroupCount1010 extends Thread {
    ThreadactiveGroupCount1010(ThreadGroup tg, String s) {super(tg, s);}
    public void run() {
        synchronized(agcObjects[1]) {
            agcObjects[1].notify();
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

            addLog("*********  Test activeGroupCount1010 begins ");
activeGroupCount1010();
            addLog("*********  Test activeGroupCount1010 results: ");

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
        System.exit(new activeGroupCount1010().test());
    }
}



