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


package org.apache.harmony.test.func.api.java.lang.Class.forName.forName10.forName1010;


import java.lang.reflect.Constructor;

import java.net.URL;
import java.io.*;
import java.util.*;

import org.apache.harmony.share.Test;
import org.apache.harmony.test.func.share.MyLog;


public class forName1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void forName1010() {

        Class c;

        String incompleteName = "org.apache.harmony.test.func.api.java.lang.Class.forName.forName10.forName1010";
        String className1      = incompleteName + ".MyforName10101" ;
        String className2      = incompleteName + ".MyforName10102" ;
        label: {
//-1
            try { 
                c = Class.forName(incompleteName);
                results[1] = false ;
                break label;
            } catch (ClassNotFoundException e ) {
                addLog(" check #1: Exception caught: " +e);
            }
//-1)
//-2
            try {
                c = Class.forName(className1);
            } catch (Exception e ) {
                addLog(" check #2: Exception caught: " +e);
                results[2] = false ;
                break label;
            }
//-2)
//-3
            try {
                results[3] = Class.forName(className2) != null ;
            } catch (Exception e ) {
                results[results.length-1] = false ;
                break label;
            }
//-3)
        } // label:
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

            addLog("*********  Test forName1010 begins ");
forName1010();
            addLog("*********  Test forName1010 results: ");

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
        System.exit(new forName1010().test());
    }
}


class MyforName10101  {
    public String s = "THIS IS 1-st CLASS TO TEST";
}
class MyforName10102  {
    public String s = "THIS IS 2-nd CLASS TO TEST";
}



