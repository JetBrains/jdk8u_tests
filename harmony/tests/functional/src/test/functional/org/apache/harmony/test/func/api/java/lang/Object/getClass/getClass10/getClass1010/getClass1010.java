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


package org.apache.harmony.test.func.api.java.lang.Object.getClass.getClass10.getClass1010;

import org.apache.harmony.share.Test;


import org.apache.harmony.test.func.share.MyLog;

public class getClass1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void getClass1010() {

            Object classObjects[] = { 
                       new XYZNAME2getClass1010(), new XYZNAME2getClass1010()
                                     };
            int ints[] = new int[] { 0, 0 };
            int base = 0;
//-1
// Class Class

            Class class1 = classObjects[0].getClass();

            results[base +1] = ! class1.isArray();
            results[base +2] = ! class1.isInterface();
            results[base +3] =   class1.isInstance(classObjects[0]);
//-1)
            base += 3;
//-2
// Class Interface

            Class class2         = classObjects[1].getClass();
            Class ifaceClasses[] = class1.getInterfaces();

            results[base +1] = ! ifaceClasses[0].isArray();
            results[base +2] =   ifaceClasses[0].isInterface();
//-2)
            base += 2;
//-3
// Class Arrays

            Class class3 = ints.getClass();

            results[base +1] =   class3.isArray();
            results[base +2] = ! class3.isInterface();
            results[base +3] =   class3.isInstance(ints);
//-3)
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

            addLog("*********  Test getClass1010 begins ");
getClass1010();
            addLog("*********  Test getClass1010 results: ");

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
        System.exit(new getClass1010().test());
    }
}


interface XYZNAME1getClass1010 {
    int i1 = 1;
}
class XYZNAME2getClass1010 implements XYZNAME1getClass1010 {
    private int i2 = 0;
    
    public void set() {
        i2 = i1;
    }
    public int get() {
        return i2;
    }
}


