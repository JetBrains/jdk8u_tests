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
package org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_02;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_02.auxiliary.SecureRandomProvider;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 23.12.2004 
 */
public class F_SecureRandomTest_02 extends ScenarioTest {

    public static final String ProviderName = "SRP";
    public static final double Version = 1.0;
    public static final String Description = "SRP secure random provider v1.0 for testing purpose only.";
    
    public int test() {
        log.info("vm.boot.class.path: " + System.getProperty("vm.boot.class.path"));
        log.info("vm.bootclasspath.appendclasspath: " + System.getProperty("vm.bootclasspath.appendclasspath"));
        log.info("vm.bootclasspath.initmethod: " + System.getProperty("vm.bootclasspath.initmethod"));
        log.info("vm.bootclasspath.prepend: " + System.getProperty("vm.bootclasspath.prepend"));
        
        if ("failed".equalsIgnoreCase(Security.getProperty("policy.allowSystemProperty"))) {
            return error("An extra policy feature is disabled by default.");
        }
        
        System.setSecurityManager(new SecurityManager());
        log.info("\t\t\tList of all providers currently installed in the system.");
        printProviders();
        Provider[] originalProviders = Security.getProviders();       
        
        
        Provider p = new SecureRandomProvider(ProviderName, Version, Description);
        if (p == null) {
            return fail("Couldn't create secure random provider. Test failed.");
        }
                
        log.info("\t\t\tAcquire general properties of the newly constructed provider.");
        // acquire general properties of the newly constructed provider
        String providerName         = p.getName();
        double providerVersion         = p.getVersion();
        String providerDescription     = p.getInfo(); 
        log.info("\nNewly created provider's name is: " + providerName);
        log.info("Provider " + providerName + "'s version is: " + providerVersion);
        log.info("Provider " + providerName + "'s info is: " + providerDescription);
        
        log.info("\nAdd the newly constructed provider into the list of providers.");
        //Security.addProvider(p);
        Security.insertProviderAt(p, 2);
        
        log.info("\t\t\tList of all providers currently installed in the system.");
        printProviders();
        
        Object ob[] = Security.getAlgorithms("SecureRandom").toArray();
        if (ob.length > 0) {
            String algorithmName = ob[0].toString();
            Provider pr[] = Security.getProviders("SecureRandom." + algorithmName);
            for (int i = 0; i < pr.length; i++) {
                String className = null;
                for (Enumeration en = pr[i].keys(); en.hasMoreElements();) {
                    Object value = en.nextElement();
                    if (((String)value).equalsIgnoreCase("SecureRandom." + algorithmName)) {
                        className = pr[i].get(value).toString();
                    }
                }
            // attempt to get an instance of pseudo-random numbers generation algorithm supplied by installed provider
            // from the newly constructed provider
                if (className != null) {
                    p.put("SecureRandom." + algorithmName, className);
                    printProviderProperties(p);        
                    try {
                        SecureRandom random = SecureRandom.getInstance(algorithmName);
                        byte[] seed = random.generateSeed(1000);
                        random.setSeed(seed);
                        byte[] randomNumbers = new byte[100];
                        random.nextBytes(randomNumbers);            
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        return fail(e.getMessage());
                    }
                    break;
                }
            }
        } else {
            log.info("There are no installed providers with SecureRandom algorithm implementation.");
        }

        // attempt to get an instance of pseudo-random numbers generaton algorithm supplied by our provider
        // from the newly constructed provider
        p.clear();
        HashMap map = new HashMap(1);        
        map.put("SecureRandom.SHA1PRNG", "org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_02.auxiliary.SecureRandomSPImplementation");        
        p.putAll(map);
        printProviderProperties(p);
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", ProviderName);
            byte[] seed = random.generateSeed(1000);
            random.setSeed(seed);
            byte[] randomNumbers = new byte[100];
            random.nextBytes(randomNumbers);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return fail(e.getMessage());
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return fail(e.getMessage());
        }
        
        return pass("Test passed");
    }

    private void printProviders() {
        log.info("----------- Providers -----------");
        Provider[] pl = Security.getProviders();
        for (int i = 0; i < pl.length; i++) {
            log.info("\nProvider " + (i + 1) + " : " + pl[i]);
            printProviderProperties(pl[i]);
        }
        log.info("----------- Providers -----------\n");
    }
    
    private void printProviderProperties(Provider p) {
        log.info("\n------------ Provider " + p.getName() + " Properties ------------\n");
        for (Enumeration en = p.keys(); en.hasMoreElements();) {
            Object value = en.nextElement();
            log.info("\t" + value + " = " + p.get(value));
        }
    }
    
    public static void main(String[] args) {
        System.exit(new F_SecureRandomTest_02().test(args));
    }
}
