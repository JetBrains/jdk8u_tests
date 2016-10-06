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
package org.apache.harmony.test.func.api.java.security.F_KeyFactoryTest_02;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyFactorySpi;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import org.apache.harmony.test.func.api.java.security.F_KeyFactoryTest_02.auxiliary.KeyFactoryProvider;
import org.apache.harmony.test.func.api.java.security.F_KeyFactoryTest_02.auxiliary.KeyFactorySPImplementation;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 31.12.2004 
 */
public class F_KeyFactoryTest_02 extends ScenarioTest {

    public static final String providerName = "org.apache.harmony.test.func.api.java.security.KeyFactoryTestProvider";
    public static final double providerVersion = 1.0;
    public static final String providerInfo = "KeyFactoryTestProvider v1.0 for testing purpose only";
    KeyFactorySpi kfspi = null;
    Provider p = null;
    private String testedKeyAlgorithm; 
    
    public int test() {   
        byte b[] = new byte[20];
        for (int i = 0; i < 20; i++) {
            b[i] = (byte)Math.round(Math.random()*127);
        }
        if ("failed".equalsIgnoreCase(Security.getProperty("policy.allowSystemProperty"))) {
            return error("An extra policy feature is disabled by default. Test can't be run.");
        }
        
        //System.setSecurityManager(new SecurityManager());        
        
        String[] algorithms = {"DSA", "RSA"};
        for (int i = 0; i < algorithms.length; i++) {
            testedKeyAlgorithm = algorithms[i];
            log.info (" ----- Processing algorithm '" + testedKeyAlgorithm + "' ------\n");
        
        try {
            kfspi = new KeyFactorySPImplementation(testedKeyAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return pass("Couldn't create a KeyFactorySpi object. There are no DSA algorithms.");
        }
            
        p = new KeyFactoryProvider(providerName, providerVersion, providerInfo);
        if (null == p) {
            return fail("Couldn't create a KeyFactoryProvider object.");
        }
        
        Security.addProvider(p);
        
        KeyFactory kf = new MyKeyFactory();
        if (null == kf) {
            return fail("Couldn't create an own KeyFactory implementation object.");
        }        
        
        try {
            log.info("Instantiating KeyPairGenerator...");
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(testedKeyAlgorithm);
            log.info("OK\nGenerating key pair...");
            KeyPair keyPair = keyPairGen.genKeyPair();
            log.info("OK");
            
            PublicKey pubKey;
            PrivateKey privKey;
            Class keySpecClass;
            
            String algorithmName = null;            
            try {
                algorithmName = getAlgorithmName(testedKeyAlgorithm);                
            } catch (NoSuchAlgorithmException ex) {
                return pass(ex.getMessage());
            }
            
            Signature signature = Signature.getInstance(algorithmName);
            byte[] sign;
            try {
                keySpecClass = Class.forName("java.security.spec." + testedKeyAlgorithm + "PublicKeySpec");
                log.info("Acquire KeySpec for " + testedKeyAlgorithm + "PublicKeySpec class");
                KeySpec keySpec = kf.getKeySpec(keyPair.getPublic(), keySpecClass);
                log.info("Generate a public key");
                pubKey = kf.generatePublic(keySpec);
                
                keySpecClass = Class.forName("java.security.spec." + testedKeyAlgorithm + "PrivateKeySpec");
                keySpec = kf.getKeySpec(keyPair.getPrivate(), keySpecClass);
                privKey = kf.generatePrivate(keySpec);
                
                signature.initSign(privKey);
                signature.update(b);
                sign = signature.sign();
                
                signature.initVerify(pubKey);
                signature.update(b);
                if (!signature.verify(sign)) {
                    return fail("Test failed.");
                }
                
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
                return error("Class not found: " + cnfe.getMessage());
            } catch (InvalidKeySpecException ikse) {
                ikse.printStackTrace();
                return error("Invalid key specification specified.");
            } catch (InvalidKeyException ike) {
                ike.printStackTrace();
                return error("Invalid key specified: " + ike.getMessage());
            } catch (SignatureException se) {
                se.printStackTrace();
                return error("A signature object hasn't been properly initialized.");
            } catch (Exception e ) {
                if (e.getMessage().indexOf("not implemented yet") == -1) {
                    e.printStackTrace();
                    return error("Error: " + e.getMessage());
                } 
                
                log.info(e.getMessage());
            }
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error(e.getMessage());
        } 
        }
        
        return pass("Test passed.");
    }
    
    private String getAlgorithmName(String algorithm) throws NoSuchAlgorithmException {
        String name = null;
        Object a[] = Security.getAlgorithms("Signature").toArray();
        for (int i = 0; i < a.length; i++) {
            name = a[i].toString();
            if (name.equalsIgnoreCase(algorithm) || name.matches(".*[wW][iI][tT][hH]" + algorithm + ".*")) {
                break;
            }
        }
        if (name == null) {
            throw new NoSuchAlgorithmException("There are no signature algorithms with " + algorithm);
        }
        return name;
    }

    public static void main(String[] args) {
        System.exit(new F_KeyFactoryTest_02().test(args));
    }
    
    class MyKeyFactory extends KeyFactory {
        public MyKeyFactory() {       
            super(kfspi, p, testedKeyAlgorithm);
            log.info("MyKeyFactory ctor has been called.");
        }        
    }
}