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


package org.apache.harmony.test.func.api.java.lang.Runtime.exec.exec10.exec1014;


import java.io.*;
import org.apache.harmony.share.Test;
import org.apache.harmony.test.func.share.MyLog;


public class exec1014 extends Test {

    boolean  results[] = new boolean[100];
    String  logArray[] = new String[100];
    int     logIndex   = 0; 


    void addLog(String s) {
        if ( logIndex < logArray.length )
            logArray[logIndex] = s;
        logIndex++;
    }

    void exec1014() {

        String tokens[] = { " "      ,
                            "java"  ,
                            "-help"
                          };
        String envp[]   = { "PATH=" + System.getProperty("java.home") +
                System.getProperty("path.separator") + "bin"  };
        Process p;
        InputStream bis;
        int exitValue;
        int exitValuesExpected[] = { 9, 0, 1 };

addLog("------- Process exec(String command, String[] envp) -------");

        Runtime rt = Runtime.getRuntime();

        label: {
//-1
            try {
addLog("-----1.1");

                p = rt.exec(tokens[1] + " " + tokens[2], envp );

addLog("-----1.2");

                bis = p.getInputStream();
//                bis.read();
                while (bis.read() != -1) {
                    bis.read();
                }

addLog("-----1.3");

            } catch ( IOException e ) {
                 addLog("IOException while 'rt.exec(...)'");
                 results[results.length -1] = false;
                 break label;
            }
            try {
                 p.waitFor();
            } catch ( InterruptedException e ) {
                 addLog("InterruptedException while 'waitFor()'");
                 results[results.length -1] = false;
                 break label;
            }
            exitValue = p.exitValue();

addLog("-----1.4  exitValue= " +exitValue );

            results[1] = exitValue == exitValuesExpected[1] ;

            try {
addLog("-----2.1");

                p = rt.exec( tokens[1], envp );

addLog("-----2.2");

                bis = p.getInputStream();
//                bis.read();
                while (bis.read() != -1) {
                    bis.read();
                }

addLog("-----2.3");

            } catch ( IOException e ) {
                 addLog("IOException while 'rt.exec(...)'");
                 results[results.length -1] = false;
                 break label;
            }
            try {
                 p.waitFor();
            } catch ( InterruptedException e ) {
                 addLog("InterruptedException while 'waitFor()'");
                 results[results.length -1] = false;
                 break label;
            }
            exitValue = p.exitValue();

addLog("-----2.4  exitValue= " +exitValue );

            results[1] &= exitValue == exitValuesExpected[2] ;
//-1)
        }

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

            addLog("*********  Test exec1014 begins ");
exec1014();
            addLog("*********  Test exec1014 results: ");

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
        System.exit(new exec1014().test());
    }
}



