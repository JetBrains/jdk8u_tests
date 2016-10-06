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
package org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_05;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 01.02.2005 
 */
public class F_SecureRandomTest_05 extends ScenarioTest {

    public int test() {
        
        System.setSecurityManager(new SecurityManager());
        byte[] seed = new byte[100];
        new Random().nextBytes(seed);
        SecureRandom random = new SecureRandom(seed);
        if (random == null) {
            return fail("Couldn't create an istance of SecureRandom object. Test failed");            
        }
        
        seed = random.generateSeed(100);
        random.setSeed(seed);
        byte[] numbers = new byte[10];
        random.nextBytes(numbers);
        
        return pass("Test passed");
    }

    public static void main(String[] args) {
        System.exit(new F_SecureRandomTest_05().test(args));
    }
}
