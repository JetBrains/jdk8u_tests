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
/*
 * Created on 02.02.2005
 * Last modification G.Seryakova
 * Last modified on 02.02.2005
 *  
 * Test getting resources per ClassLoader's and Class's methods.
 */
package org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_03;

import java.io.*;
import java.net.URL;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Test getting resources per ClassLoader's and Class's methods.
 * 
 */
public class F_ClassLoaderTest_03 extends ScenarioTest {
    
    public static void main(String[] args) {
        System.exit(new F_ClassLoaderTest_03().test(args));
    }
    
    public int test() {
        Class cls = null;
        ClassLoader clsLoader = ClassLoader.getSystemClassLoader();
                
        URL res = ClassLoader.getSystemResource("input.txt");
        String strResult = "";
        try {
            BufferedReader inbr = new BufferedReader(new InputStreamReader(res.openStream())); 
                strResult = inbr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        log.info("Result of retrieving file per getSystemResource(): " + strResult);
        
        InputStream stream = ClassLoader.getSystemResourceAsStream("input.txt");
        strResult = "";
        try {
            BufferedReader inbr = new BufferedReader(new InputStreamReader(stream));                    
            strResult = inbr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        log.info("Result of retrieving file per getSystemResourceAsStream(): " + strResult);
        
        if (clsLoader == null) {
            log.info("SystemClassLoader is null.");
            return pass();
        }

        try {
            cls = clsLoader.loadClass("org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_03.auxiliary.A");
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return fail("Class not found.");
        }
        
        InputStream stream1 = cls.getResourceAsStream("input.txt");
        strResult = "";
        try {
            BufferedReader inbr = new BufferedReader(new InputStreamReader(stream1)); 
                strResult = inbr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        log.info("Result of retrieving file per Class.getResourceAsStream(): " + strResult);
         
        return pass();
    }
}

