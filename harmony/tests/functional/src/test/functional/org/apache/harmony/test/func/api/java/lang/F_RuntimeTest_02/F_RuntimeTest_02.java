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
 * Created on 27.01.2005
 * Last modification G.Seryakova
 * Last modified on 27.01.2005
 * 
 * This is the scenario test of some Runtime methods
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_RuntimeTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.io.*;

/**
 * This is the scenario test of some Runtime methods
 * 
 */
public class F_RuntimeTest_02 extends ScenarioTest {
    protected static Runtime curRuntime = Runtime.getRuntime();

    public static void main(String[] args) {
        curRuntime.exit(new F_RuntimeTest_02().test(args));
    }
    
    public int test() {
        Process p = null;
        try {
            p = curRuntime.exec(new String[] {"java", "-version"});            
        } catch (Exception e) {
            e.printStackTrace();
            return fail("");
        }

        String line;

        BufferedReader input = new BufferedReader(new InputStreamReader(p
            .getErrorStream()));
        try {
            while ((line = input.readLine()) != null) {
                log.info(line);
            }
            //            input.close();
            log.info("" + p.waitFor());
        } catch (IOException e) {
            e.printStackTrace();
            return fail("");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return fail("");
        }            
        
        

        return pass();
    }
}