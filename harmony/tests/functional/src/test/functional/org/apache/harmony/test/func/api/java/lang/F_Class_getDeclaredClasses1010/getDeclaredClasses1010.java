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


package org.apache.harmony.test.func.api.java.lang.F_Class_getDeclaredClasses1010;


import java.lang.reflect.Constructor;

import java.net.URL;
import java.io.*;
import java.util.*;

import org.apache.harmony.share.Test;
import org.apache.harmony.test.func.share.MyLog;


public class getDeclaredClasses1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void getDeclaredClasses1010() {

        String className1     = "org.apache.harmony.test.func.api.java.lang.F_Class_getDeclaredClasses1010" + ".MygetDeclaredClasses10101" ;
        String className2     = "org.apache.harmony.test.func.api.java.lang.F_Class_getDeclaredClasses1010" + ".MygetDeclaredClasses10102" ;
        String className3     = "org.apache.harmony.test.func.api.java.lang.F_Class_getDeclaredClasses1010" + ".MygetDeclaredClasses10103" ;

        Class c1, c2, c3;
        Class[] ca1, ca2, ca3;
        label: {
            try { 
                c1 = Class.forName(className1);
                c2 = Class.forName(className2);
                c3 = Class.forName(className3);
            } catch (ClassNotFoundException e ) {
                addLog("ERROR while getting classes");
                results[results.length-1] = false ;
                break label;
            }
//-1
            ca1 = c1.getDeclaredClasses();
            addLog(" check 1: length == " + ca1.length);
            results[1] = ca1.length == 0;
//-1)
//-2
            ca2 = c2.getDeclaredClasses();
            addLog(" check 2: length == " + ca2.length);
            results[2] = ca2.length == 8;
//-2)
//-3
            ca3 = c3.getDeclaredClasses();
            addLog(" check 3: length == " + ca3.length);
            results[3] = ca3.length == 4;
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

            addLog("*********  Test getDeclaredClasses1010 begins ");
getDeclaredClasses1010();
            addLog("*********  Test getDeclaredClasses1010 results: ");

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
        System.exit(new getDeclaredClasses1010().test());
    }
}


class MygetDeclaredClasses10101  {
    public String s = "THIS IS 1-st CLASS TO TEST";
}
class MygetDeclaredClasses10102  {
    public String s = "THIS IS 2-nd CLASS TO TEST";
    public interface MygetDeclaredClasses101021 {
        public String s = "THIS IS INTERFACE IN CLASS";
    }
    public class MygetDeclaredClasses101022 implements MygetDeclaredClasses101021 {
        public String s = "THIS IS CLASS IN CLASS";
    }
    private interface MygetDeclaredClasses101023 {
        String s = "THIS IS INTERFACE IN CLASS";
    }
    private class MygetDeclaredClasses101024 implements MygetDeclaredClasses101023 {
        String s = "THIS IS CLASS IN CLASS";
    }
    protected interface MygetDeclaredClasses101025 {
        String s = "THIS IS INTERFACE IN CLASS";
    }
    protected class MygetDeclaredClasses101026 implements MygetDeclaredClasses101025 {
        String s = "THIS IS CLASS IN CLASS";
    }
    interface MygetDeclaredClasses101027 {
        String s = "THIS IS INTERFACE IN CLASS";
    }
    class MygetDeclaredClasses101028 implements MygetDeclaredClasses101027 {
        String s = "THIS IS CLASS IN CLASS";
    }
}
class MygetDeclaredClasses10103 extends MygetDeclaredClasses10102 {
    public String s = "THIS IS SUBCLASS";
    public class MygetDeclaredClasses101031 {
        public String s = "THIS IS CLASS IN SUBCLASS";
    }
    private class MygetDeclaredClasses101032 {
        String s = "THIS IS CLASS IN SUBCLASS";
    }
    protected class MygetDeclaredClasses101033 {
        String s = "THIS IS CLASS IN SUBCLASS";
    }
    class MygetDeclaredClasses101034 {
        String s = "THIS IS CLASS IN SUBCLASS";
    }
}


