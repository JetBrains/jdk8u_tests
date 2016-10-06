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
package org.apache.harmony.test.func.api.java.security.F_SignatureTest_01;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * Created on 15.12.2004
 * 
 * Usage java F_SignatureTest_01 <keysize> <keyalgorithm> <sigalgorithm> <data>
 *               <keysize> - the length of the cryptographic key in bytes  
 *               <keyalgorithm> - the name of the cryptographic keys generation algorithm
 *               <sigalgorithm> - the name of the digital signature generation algorithm
 *               <data> - data to be signed and verified
 */
public class F_SignatureTest_01 extends ScenarioTest {
    
    Signature signature = null;
    KeyPair keyPair = null;
    
    public int test() {
        String name[] = new String[2];
        Object k[] = Security.getAlgorithms("KeyPairGenerator").toArray();
        Object a[] = Security.getAlgorithms("Signature").toArray();
        for (int i = 0; i < k.length; i++) {
            name[0] = k[i].toString();
            for (int j = 0; j < a.length; j++) {
                name[1] = a[j].toString();
                if (name[1].equalsIgnoreCase(name[0]) || name[1].matches(".*[wW][iI][tT][hH]" + name[0] + ".*")) {
                    log.info("Uses algorithms -------" + name[0] + "-------" + name[1] + "-------");
                    try {
                        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(name[0]);            
                        keyPairGen.initialize(Integer.parseInt(testArgs[0]));            
                        signature = Signature.getInstance(name[1]);
                        keyPair = keyPairGen.generateKeyPair();
                        
                        if (HandleSignature(false /*bytewise update*/) != Result.PASS || 
                            HandleSignature(true) != Result.PASS) {
                                return fail("Test failed.");
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        return fail(e.getMessage());
                    }
                    log.info("task1 has passed.");
                    
                    try {
                        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(name[0]);
                        SecureRandom random = new SecureRandom();
                        
                        byte[] seeds = random.generateSeed(50);
                        random.setSeed(seeds);
                        keyPairGen.initialize(Integer.parseInt(testArgs[0]), random);            
                        signature = Signature.getInstance(name[1]);
                        keyPair = keyPairGen.generateKeyPair();
                        
                        if (HandleSignature(false /*bytewise update*/) != Result.PASS || 
                            HandleSignature(true) != Result.PASS) {
                                return fail("Test failed.");
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        return fail(e.getMessage());
                    } 
                    log.info("task2 has passed.");
                }
            }
        }
        
        return pass("Test passed");
    }
    
    private int HandleSignature(boolean bytewise) { 
        byte b[];
        if (signature.getAlgorithm().indexOf("DSA") >= 0) {
            b = new byte[20];
        } else {
            b = new byte[50];
        }
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte)Math.round(Math.random()*127);
        }
        byte[] sign = null;
        try {
//            try {
                sign = Sign(b, bytewise); 
                return (Verify(b, sign) ? Result.PASS : Result.FAIL);
//            } catch (SignatureException ex) {
//                if (signature.getAlgorithm().indexOf("DSA") >= 0) {
//                    byte b20[] = new byte[20];
//                    System.arraycopy(b, 0, b20, 0, 20);
//                    sign = Sign(b20, bytewise);
//                    return (Verify(b20, sign) ? Result.PASS : Result.FAIL);
//                } else {
//                    throw ex;
//                }
//            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return Result.FAIL;
        } catch (SignatureException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
    }
    
    private byte[] Sign(byte[] data, boolean bytewise) throws InvalidKeyException, SignatureException {
        signature.initSign(keyPair.getPrivate());
        if (bytewise) {
            for (int i = 0; i < data.length; i++) {
                signature.update(data[i]);
            }
        } else {
            signature.update(data);
        }
        return signature.sign();
    }   
    
    private boolean Verify(byte[] data, byte[] signature) throws InvalidKeyException, SignatureException {        
        this.signature.initVerify(keyPair.getPublic());        
        this.signature.update(data);
        return this.signature.verify(signature);
    }

    public static void main(String[] args) {
        System.exit(new F_SignatureTest_01().test(args));
    }
}