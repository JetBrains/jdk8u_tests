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


package org.apache.harmony.test.func.api.java.lang.Thread.setPriority.setPriority10.setPriority1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class setPriority1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void setPriority1010() {

            ThreadGroup tg1 = new ThreadGroup("tg1");
            tg1.setMaxPriority( ( Thread.currentThread().MAX_PRIORITY +
                                  Thread.currentThread().MIN_PRIORITY ) / 2 );
            Thread t[] = { null, new Thread(tg1,"t1"),
                                 new Thread(tg1,"t2"),
                                 new Thread(tg1,"t3")
                         };
            label: {
//-1
                if (  Thread.currentThread().MAX_PRIORITY ==
                      Thread.currentThread().MIN_PRIORITY  ) {
addLog("ATTENTION: DEGENARATE CASE: MAX_PRIORITY == MIN_PRIORITY");
                    t[1].setPriority( tg1.getMaxPriority() );
                    results[1] = t[1].getPriority() == 
                                         Thread.currentThread().MAX_PRIORITY;
                    break label;
                }
//-1)
//-2
                if (  Thread.currentThread().MAX_PRIORITY == 
                      Thread.currentThread().MIN_PRIORITY + 1 ) {
                    t[1].setPriority( tg1.getMaxPriority() );
                    results[1] = t[1].getPriority() == 
                                         Thread.currentThread().MAX_PRIORITY;
                    t[2].setPriority( tg1.getMaxPriority() + 1 );
                    results[2] = t[2].getPriority() == 
                                         Thread.currentThread().MAX_PRIORITY;
                    break label;
                }
//-2)
//-3
                t[1].setPriority( tg1.getMaxPriority() -1 );
                results[1] = t[1].getPriority() == 
                                ( Thread.currentThread().MAX_PRIORITY +
                                  Thread.currentThread().MIN_PRIORITY ) /2 -1;
                t[2].setPriority( tg1.getMaxPriority() );
                results[2] = t[2].getPriority() == 
                                  ( Thread.currentThread().MAX_PRIORITY +
                                  Thread.currentThread().MIN_PRIORITY ) / 2 ;
                t[3].setPriority( tg1.getMaxPriority() + 1 );
                results[3] = t[3].getPriority() == 
                                  ( Thread.currentThread().MAX_PRIORITY +
                                  Thread.currentThread().MIN_PRIORITY ) / 2 ;
//-3)
            } //label
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

            addLog("*********  Test setPriority1010 begins ");
setPriority1010();
            addLog("*********  Test setPriority1010 results: ");

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
        System.exit(new setPriority1010().test());
    }
}



