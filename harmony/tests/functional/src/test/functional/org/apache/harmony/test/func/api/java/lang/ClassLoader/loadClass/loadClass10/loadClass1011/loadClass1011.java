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


package org.apache.harmony.test.func.api.java.lang.ClassLoader.loadClass.loadClass10.loadClass1011;


import java.lang.reflect.Constructor;

import java.net.URL;
import java.io.*;
import java.util.*;

import org.apache.harmony.share.Test;
import org.apache.harmony.test.func.share.MyLog;


public class loadClass1011 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void loadClass1011() {


        MyloadClass1011 myloadClass1011 = new MyloadClass1011();

        String incompleteName = "org.apache.harmony.test.func.api.java.lang.ClassLoader.loadClass.loadClass10.loadClass1011" ;
        String className1      = incompleteName + ".Child1loadClass1011" ;
        String className2      = incompleteName + ".Child2loadClass1011" ;

        Class cls = null;
//-1
        try {
            cls = myloadClass1011.loadClass(incompleteName, false);
            results[1] = false ;
        } catch ( ClassNotFoundException e ) {
        }
//-1)
//-2
        try {
            cls = myloadClass1011.loadClass(className1, false);
        } catch ( ClassNotFoundException e ) {
            results[2] = false ;
        }
//-2)
//-3
        try {
            cls = myloadClass1011.loadClass(className2, true);
        } catch ( ClassNotFoundException e ) {
            results[3] = false ;
        }
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

            addLog("*********  Test loadClass1011 begins ");
loadClass1011();
            addLog("*********  Test loadClass1011 results: ");

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
        System.exit(new loadClass1011().test());
    }
}


class MyloadClass1011 extends ClassLoader {
    MyloadClass1011() {super();}

    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch ( ClassNotFoundException e ) {
            throw new ClassNotFoundException( (String) e.toString() );
        }
    }
}
class Child1loadClass1011 extends ClassLoader {
    Child1loadClass1011(ClassLoader parent) {super(parent);}
}
class Child2loadClass1011 extends ClassLoader {
    Child2loadClass1011(ClassLoader parent) {super(parent);}
}



