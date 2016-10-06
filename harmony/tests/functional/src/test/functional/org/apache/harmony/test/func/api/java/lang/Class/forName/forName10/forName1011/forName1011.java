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


package org.apache.harmony.test.func.api.java.lang.Class.forName.forName10.forName1011;


import java.lang.reflect.Constructor;

import java.net.URL;
import java.io.*;
import java.util.*;

import org.apache.harmony.share.Test;
import org.apache.harmony.test.func.share.MyLog;


public class forName1011 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void forName1011() {

        Class c;

        String incompleteName = "org.apache.harmony.test.func.api.java.lang.Class.forName.forName10.forName1011";
        String className1     = incompleteName + ".MyforName10111" ;
        String className2     = incompleteName + ".MyforName10112" ;
        String className3     = incompleteName + ".MyforName10113" ;
        String className4     = incompleteName + ".MyforName10114" ;

        ClassLoader currentLoader = this.getClass().getClassLoader();

        label: {
//-1
            try { 
                c = Class.forName(incompleteName, true, null);
                addLog("check #1-true");
                results[1] = false ;
                break label;
            } catch (ClassNotFoundException e ) {
            }
            try { 
                c = Class.forName(incompleteName, false, null);
                addLog("check #1-false");
                results[1] = false ;
                break label;
            } catch (ClassNotFoundException e ) {
            }
//-1)
//-2
            try {
                c = Class.forName(className1, true, currentLoader);
            } catch (Exception e ) {
                addLog("check #2-true : " + e);
                results[2] = false ;
                break label;
            }
            try {
                c = Class.forName(className2, false, currentLoader);
            } catch (Exception e ) {
                addLog("check #2-false : " + e);
                results[2] = false ;
                break label;
            }
//-2)
//-3
            try {
                results[3] = Class.forName(className3, true, currentLoader) != null ;
            } catch (Exception e ) {
                addLog("check #3-true : " + e);
                results[results.length-1] = false ;
                break label;
            }
            try {
                results[4] = Class.forName(className4, false, currentLoader) != null ;
            } catch (Exception e ) {
                addLog("check #4-false : " + e);
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

            addLog("*********  Test forName1011 begins ");
forName1011();
            addLog("*********  Test forName1011 results: ");

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
        System.exit(new forName1011().test());
    }
}


class MyforName10111  {
    public String s = "THIS IS 1-st CLASS TO TEST";
}
class MyforName10112  {
    public String s = "THIS IS 2-nd CLASS TO TEST";
}
class MyforName10113  {
    public String s = "THIS IS 3-rd CLASS TO TEST";
}
class MyforName10114  {
    public String s = "THIS IS 4-th CLASS TO TEST";
}



