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
package org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_03;

import java.security.SecureRandom;
import org.apache.harmony.test.func.share.ScenarioTest;
/**
 * Created on 28.12.2004 
 */
public class F_SecureRandomTest_03 extends ScenarioTest {
    SecureRandom random = null;
    
    public int test() {
        try {
            // create SecureRandom object that has not been seeded ...
            random = new SecureRandom();
            byte[] randomNumber = new byte[100];
            // ... and get random numbers
            random.nextBytes(randomNumber);
            for (int i = 0; i < randomNumber.length; i++) {
                log.info(randomNumber[i] + ";");
            }            
        } catch (Exception e) {
            e.printStackTrace();
            return fail("Test failed");
        }
        
        return pass("Test passed");
    }    

    public static void main(String[] args) {
        System.exit(new F_SecureRandomTest_03().test(args));
    }
}