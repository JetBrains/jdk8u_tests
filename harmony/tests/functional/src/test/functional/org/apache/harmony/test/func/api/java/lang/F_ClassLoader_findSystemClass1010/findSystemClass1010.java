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


package org.apache.harmony.test.func.api.java.lang.F_ClassLoader_findSystemClass1010;


import java.lang.reflect.Constructor;

import java.net.URL;
import java.io.*;
import java.util.*;

import org.apache.harmony.share.Test;
import org.apache.harmony.test.func.share.MyLog;


public class findSystemClass1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void findSystemClass1010() {


        MyfindSystemClass1010 myfindSystemClass1010 = new MyfindSystemClass1010();

        String foundableName    = "org.apache.harmony.test.func.api.java.lang.F_ClassLoader_findSystemClass1010" + ".ChildfindSystemClass1010" ;
        String nonfoundableName = "org.apache.harmony.test.func.api.java.lang.F_ClassLoader_findSystemClass1010" + ".Child" ;

        Class classObj = null;
//-1
        try {
            classObj = myfindSystemClass1010.myfindSystemClass(foundableName);
        } catch ( ClassNotFoundException e ) {
            results[1] = false ;
        }
//-1)
//-2
            results[2] = classObj != null ;
//-2)
//-3
        try {
            myfindSystemClass1010.myfindSystemClass(nonfoundableName);
            results[3] = false ;
        } catch ( ClassNotFoundException e ) {
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

            addLog("*********  Test findSystemClass1010 begins ");
findSystemClass1010();
            addLog("*********  Test findSystemClass1010 results: ");

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
        System.exit(new findSystemClass1010().test());
    }
}


class MyfindSystemClass1010 extends ClassLoader {
    MyfindSystemClass1010() {super();}

    public Class myfindSystemClass(String name) throws ClassNotFoundException {
        try {
            return super.findSystemClass(name);
        } catch ( ClassNotFoundException e ) {
            throw new ClassNotFoundException( (String) e.toString() );
        }
    }
}
class ChildfindSystemClass1010 extends ClassLoader {
    ChildfindSystemClass1010(ClassLoader parent) {super(parent);}
}



