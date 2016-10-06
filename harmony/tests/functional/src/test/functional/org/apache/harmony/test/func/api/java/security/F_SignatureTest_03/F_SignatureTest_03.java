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
package org.apache.harmony.test.func.api.java.security.F_SignatureTest_03;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

import org.apache.harmony.test.func.api.java.security.F_SignatureTest_03.auxiliary.F_SignatureSPImplementation;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 25.08.2005
 */
public class F_SignatureTest_03 extends ScenarioTest {

    private final String m_TestString = "When one path of destiny is blocked, another will appear.";
    private final int OFFSET = 5;
    private final int LENGTH = 10;
    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {
        try {
            System.out.println("Acquire an instance of MyMessageDigest with algorithm 'MD5'");
            MessageDigest md = MessageDigest.getInstance("MD5");
            System.out.print("OK\nUpdate digest...");
            md.update(m_TestString.getBytes());
            System.out.print("OK\nComplete digest computation...");
            byte[] digest = md.digest();
            System.out.println("OK");
            
            System.out.print("Acquire an instance of KeyPairGenerator...");
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            System.out.print("OK\nGenerate a key pair...");
            KeyPair keyPair = keyPairGen.generateKeyPair();
            System.out.println("OK\nCreate an instance of MySignature object with algorithm 'MD5WithRSA'...");
            MySignature signature = new MySignature("MD5WithRSA");
            System.out.print("OK\nInit signature...");
            signature.initSign(keyPair.getPrivate());
            System.out.print("OK\nUpdate signature...");
            signature.update(digest, OFFSET, LENGTH);
            System.out.print("OK\nSign data...");
            byte[] sign = signature.sign();

            signature.initVerify(keyPair.getPublic());
            signature.update(digest, OFFSET, LENGTH);
            System.out.print("OK\nVerify signature...");
            if (signature.verify(sign)) {
                System.out.println("OK");
                return pass("Test passed");
            }
            
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (SignatureException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        }
        
        
        return 0;
    }
    
    class MySignature extends F_SignatureSPImplementation {
        public MySignature(String algorithm) throws NoSuchAlgorithmException {
            super(algorithm);
            System.out.println("MySignature ctor has been called");
        }
        
        public void initSign(PrivateKey key) throws InvalidKeyException {
            engineInitSign(key);
        }
        
        public void update(byte[] b, int offset, int length) throws SignatureException {
           engineUpdate(b, offset, length); 
        }
        
        public byte[] sign() throws SignatureException {
            return engineSign();
        }
        
        public void initVerify(PublicKey key) throws InvalidKeyException {
            engineInitVerify(key);
        }
        
        public boolean verify(byte[] signature) throws SignatureException {
            return engineVerify(signature);
        }
    }

    public static void main(String[] args) {
        System.exit(new F_SignatureTest_03().test(args));
    }
}
