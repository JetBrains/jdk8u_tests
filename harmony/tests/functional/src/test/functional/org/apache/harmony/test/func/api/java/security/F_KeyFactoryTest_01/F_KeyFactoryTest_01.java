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
package org.apache.harmony.test.func.api.java.security.F_KeyFactoryTest_01;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 30.12.2004 
 */
public class F_KeyFactoryTest_01 extends ScenarioTest {

    public int test() {
        byte b[] = new byte[20];
        for (int i = 0; i < 20; i++) {
            b[i] = (byte)Math.round(Math.random()*127);
        }
        System.setSecurityManager(new SecurityManager());       
        //String algorithms[] = new String[]{"DSA", "RSA"};
        String algorithms[] = new String[]{"RSA"};
        Class keySpecClass;
        for (int i = 0; i < algorithms.length; i++) {
            try {
                log.info("Instantiating of KeyPairGenerator object...");
                KeyPairGenerator keyPairGen;
                try {
                    keyPairGen = KeyPairGenerator.getInstance(algorithms[i]);
                } catch (NoSuchAlgorithmException ex) {
                    log.info(ex.getMessage());
                    continue;
                }
                log.info("OK\nGenerating key pair...");
                KeyPair keyPair = keyPairGen.genKeyPair();       
                log.info("OK\nInstantiating of KeyFactory object...");
                KeyFactory kf = KeyFactory.getInstance(algorithms[i]);
                log.info("OK");
                
                if (kf == null) {
                    return fail("Couldn't generate a KeyFactory object. Test failed.");
                }
    
                PublicKey pubKey = null; 
                String algorithmName = null;
                try {
                    algorithmName = getAlgorithmName(algorithms[i]);
                } catch (NoSuchAlgorithmException ex) {
                    log.info(ex.getMessage());
                    continue;
                }
                Signature signature = Signature.getInstance(algorithmName);
                byte[] sign;
                try {
                    keySpecClass = Class.forName("java.security.spec." + algorithms[i] + "PublicKeySpec");
                    KeySpec keySpec = kf.getKeySpec(keyPair.getPublic(), keySpecClass);
                    pubKey = kf.generatePublic(keySpec);
                    if (pubKey == null) {
                        return fail("Public key is null. Test failed.");
                    }                
                    
                    signature.initSign(keyPair.getPrivate());
                    signature.update(b);
                    sign = signature.sign();
                    
                } catch (ClassNotFoundException e) {                
                    e.printStackTrace();
                    return error("Class not found: "  + e.getMessage());
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                    return error("Invalid key specification specified");
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                    return error("Invalid private key specified");
                } catch (SignatureException e) {
                    e.printStackTrace();
                    return error("A signature object is not initialized properly.");
                }
                
                try {
                    signature.initVerify(pubKey);
                    signature.update(b);
                    if (!signature.verify(sign)) {
                        return fail("Signature doesn't match. Test failed.");
                    }
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                    return fail("Invalid public key specified. Test failed.");
                } catch (SignatureException e) {
                    e.printStackTrace();
                    return error("A signature object is not initialized properly.");                
                }
                
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return error(e.getMessage());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return error(e.getMessage());
            }
            log.info("task has passed for " + algorithms[i] + "algorithm.");
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
            throw new NoSuchAlgorithmException("There are no signature algorithms with " + algorithm + ".");
        }
        return name;
    }

    public static void main(String[] args) {
        System.exit(new F_KeyFactoryTest_01().test(args));
    }
}