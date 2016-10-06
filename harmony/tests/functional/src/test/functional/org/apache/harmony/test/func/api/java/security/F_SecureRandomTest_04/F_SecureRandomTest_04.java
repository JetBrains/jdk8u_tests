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
package org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_04;

import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;
import java.util.Random;

import org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_04.auxiliary.SecureRandomProvider;
import org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_04.auxiliary.SecureRandomSPImplementation;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 31.01.2005 
 */
public class F_SecureRandomTest_04 extends ScenarioTest {

    public static final String ProviderName = "SRP";
    public static final double Version = 1.0;
    public static final String Description = "SRP secure random provider v1.0 for testing purpose only.";    
    
    public int test() {
        if ("failed".equalsIgnoreCase(Security.getProperty("policy.allowSystemProperty"))) {
            return error("An extra policy feature is disabled by default.");
        }
        
        System.setSecurityManager(new SecurityManager());       
        
        Provider p = new SecureRandomProvider(ProviderName, Version, Description);
        if (p == null) {
            return fail("Couldn't create secure random provider. Test failed.");           
        }
        
        SecureRandomSpi secureRandomSpi = new SecureRandomSPImplementation();
        if (secureRandomSpi == null) {
            return fail("Couldn't create an instance of SecureRandomSPImplementation. Test failed.");
        }
        
        Security.addProvider(p);
        SecureRandom random = new MySecureRandom(secureRandomSpi, p);        
        
        log.info("Generating seed...");
        byte[] seed = random.generateSeed(100);
        log.info("OK\nSeeding pseudo-random number generator...");
        random.setSeed(seed);
        log.info("OK\nGetting pseudo-random numbers...");
        byte[] numbers = new byte[10];        
        random.nextBytes(numbers);
        log.info("OK");
        
        return pass("Test passed");
    }

    public static void main(String[] args) {
        System.exit(new F_SecureRandomTest_04().test(args));
    }
    
    class MySecureRandom extends SecureRandom {
        public MySecureRandom(SecureRandomSpi secureRandomSpi, Provider provider) {
            super(secureRandomSpi, provider);
            log.info("MySecureRandom ctor has been called.");
        }
    }
}
