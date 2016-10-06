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


package org.apache.harmony.test.func.api.java.lang.ClassLoader.getResources.getResources10.getResources1010;


import java.lang.reflect.Constructor;

import java.net.URL;
import java.io.*;
import java.util.*;

import org.apache.harmony.share.Test;
import org.apache.harmony.test.func.share.MyLog;


public class getResources1010 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 

    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void getResources1010() {


        MygetResources1010 mygetResources1010 = new MygetResources1010();

        String incompleteName = 
        ("org.apache.harmony.test.func.api.java.lang.ClassLoader.getResources.getResources10.getResources1010").replace('.', File.separatorChar).replace('/', File.separatorChar)
                         + File.separator + "ChildgetResources1010" ;

        String resourceName = incompleteName + ".class" ;

        Enumeration enumeration = null;
//-1
        try {
            enumeration = mygetResources1010.mygetResources(incompleteName);
        } catch (IOException e ) {
            results[1] = false ;
        }
//-1)
//-2
        results[2] = ! enumeration.hasMoreElements() ;
//-2)
//-3
        try {
            enumeration = mygetResources1010.mygetResources(resourceName);
        } catch (IOException e ) {
            results[3] = false ;
        }
//-3)
//-4
        results[4] = enumeration.hasMoreElements() ;
//-4)
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

            addLog("*********  Test getResources1010 begins ");
getResources1010();
            addLog("*********  Test getResources1010 results: ");

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
        System.exit(new getResources1010().test());
    }
}


class MygetResources1010 extends ClassLoader {
    MygetResources1010() {super();}

    public Enumeration mygetResources(String name) throws IOException {
        try {
            return super.getResources(name);
        } catch ( IOException e ) {
            throw new IOException( (String) e.toString() );
        }
    }
}
class ChildgetResources1010 extends ClassLoader {
    ChildgetResources1010(ClassLoader parent) {super(parent);}
}



