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
 * Created on 30.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.util.jar.share;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 *  
 */
public class Utils {
    public static String REFERENCE_RUNTIME = "";

    public static String TESTED_RUNTIME = "";

    public static String TESTED_RUNTIME_PARAMS = "";

    public static String REFERENCE_RUNTIME_PARAMS = "";

    public static String TESTED_CLASSPATH = "";

    public static String REFERENCE_CLASSPATH = "";

    public static String TEMP_STORAGE = "";
    
    public static String VM_OPTION = "";

    public static int THREADS = 1; //can be set from config param or based on

    public static final String[] MINIMAL_CHARSETS = new String[] { "US-ASCII",
            "ISO-8859-1", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16" };


    
    public static String[] constructCommandArguments(boolean isTestedRuntime,
            Class clz, String switchParamValue, File[] files) {
        ArrayList cmdLines = new ArrayList();
        cmdLines.add( isTestedRuntime ? TESTED_RUNTIME : REFERENCE_RUNTIME);
        addNonEmpty(cmdLines, isTestedRuntime ? TESTED_RUNTIME_PARAMS
                : REFERENCE_RUNTIME_PARAMS);
        if(isTestedRuntime) { // add VM optons to tested runtime
            StringTokenizer st = new StringTokenizer(VM_OPTION);
            while (st.hasMoreTokens()) {
                addNonEmpty(cmdLines, st.nextToken());
            }
        }

        cmdLines.add("-classpath");
        addNonEmpty(cmdLines, isTestedRuntime ? TESTED_CLASSPATH : REFERENCE_CLASSPATH);
        cmdLines.add(clz.getName());
        addNonEmpty(cmdLines, switchParamValue);
        for (int i = 0; i < files.length; i++) {
            addNonEmpty(cmdLines, files[i].getAbsolutePath());
        }
        return (String[]) cmdLines.toArray(new String[0]);
    }
    
    private static void addNonEmpty(Collection coll, String elem) {
            if (elem == null) {
                return;
            }
            elem = elem.trim();
            if (elem.length() == 0) {
                return;
            }
            coll.add(elem);
        }
    

    public static ProcessResult executeProcess(String[] cmdLines) {
        Process p;
        String line, output = "";

        try {
            //System.err.println("going to execute " + joinArray(cmdLines));
            p = Runtime.getRuntime().exec(cmdLines);
            InputStream is = p.getInputStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            while ((line = input.readLine()) != null) {
                output += line + "\r\n";
            }
            input.close();
        } catch (IOException e) {
            return ProcessResult.fail("ioexception in process execution");
        }

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            return ProcessResult
                    .fail("InterruptedException in process execution");
        }

        return new ProcessResult(p.exitValue(), output);
    }

    private static String joinArray(Object[] arr) {
        String s = "";

        for (int i = 0; i < arr.length; ++i) {
            if (i != 0) {
                s += " ";
            }
            s += arr[i];
        }
        return s;
    }
}