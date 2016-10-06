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
package org.apache.harmony.test.func.api.java.security.F_SignatureTest_02;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 25.08.2005
 */
public class F_SignatureTest_02 extends ScenarioTest {

    private final String m_TestString = "When one path of destiny is blocked, another will appear.";
    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {

        try {
            System.out.println("Create an instance of MyMessageDigest with algorithm 'MD5'");
            MessageDigest md = new MyMessageDigest("MD5");
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
            Signature signature = new MySignature("MD5WithRSA");            
            System.out.print("OK\nInit signature...");
            signature.initSign(keyPair.getPrivate());
            System.out.print("OK\nUpdate signature...");
            signature.update(digest);
            System.out.print("OK\nSign data...");
            byte[] sign = signature.sign();
            
            signature.initVerify(keyPair.getPublic());
            signature.update(digest);
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
        } catch (SignatureException e) {
            e.printStackTrace();
        }
            
        
        return fail("\nTest failed");
    }
    
    class MySignature extends Signature {
        private Signature m_Signature;
        
        public MySignature(String algorithm) throws NoSuchAlgorithmException {
            super(algorithm);
            m_Signature = Signature.getInstance(algorithm);
            System.out.println("MySignature ctor has been called");
        }
        
        /* (non-Javadoc)
         * @see java.security.SignatureSpi#engineSign()
         */
        protected byte[] engineSign() throws SignatureException {
            return m_Signature.sign();
        }
        
        /* (non-Javadoc)
         * @see java.security.SignatureSpi#engineGetParameter(java.lang.String)
         */
        protected Object engineGetParameter(String param) throws InvalidParameterException {
            return null;
        }
        
        /* (non-Javadoc)
         * @see java.security.SignatureSpi#engineUpdate(byte[], int, int)
         */
        protected void engineUpdate(byte[] b, int off, int len) throws SignatureException {
            m_Signature.update(b, off, len);
        }
        
        /* (non-Javadoc)
         * @see java.security.SignatureSpi#engineInitVerify(java.security.PublicKey)
         */
        protected void engineInitVerify(PublicKey key) throws InvalidKeyException {
            m_Signature.initVerify(key);
        }
        
        /* (non-Javadoc)
         * @see java.security.SignatureSpi#engineVerify(byte[])
         */
        protected boolean engineVerify(byte[] b) throws SignatureException {
            return m_Signature.verify(b);
        }
        
        /* (non-Javadoc)
         * @see java.security.SignatureSpi#engineUpdate(byte)
         */
        protected void engineUpdate(byte b) throws SignatureException {
            m_Signature.update(b);
        }
        
        /* (non-Javadoc)
         * @see java.security.SignatureSpi#engineSetParameter(java.lang.String, java.lang.Object)
         */
        protected void engineSetParameter(String param, Object value) throws InvalidParameterException {
            
        }
        
        /* (non-Javadoc)
         * @see java.security.SignatureSpi#engineInitSign(java.security.PrivateKey)
         */
        protected void engineInitSign(PrivateKey key) throws InvalidKeyException {
            m_Signature.initSign(key);
        }
    }
    
    class MyMessageDigest extends MessageDigest {
        
        private MessageDigest m_md;
        
        public MyMessageDigest(String algorithm) throws NoSuchAlgorithmException {
            super(algorithm);
            m_md = MessageDigest.getInstance(algorithm);
            System.out.println("MyMessageDigest ctor has been called.");
        }
        
        /* (non-Javadoc)
         * @see java.security.MessageDigestSpi#engineUpdate(byte[], int, int)
         */
        protected void engineUpdate(byte[] b, int offset, int length) {
            m_md.update(b, offset, length);
        }
        
        /* (non-Javadoc)
         * @see java.security.MessageDigestSpi#engineUpdate(byte)
         */
        protected void engineUpdate(byte b) {
            m_md.update(b);
        }
        
        /* (non-Javadoc)
         * @see java.security.MessageDigestSpi#engineReset()
         */
        protected void engineReset() {
            m_md.reset();
        }
        
        /* (non-Javadoc)
         * @see java.security.MessageDigestSpi#engineDigest()
         */
        protected byte[] engineDigest() {
            return m_md.digest();
        }
    }

    public static void main(String[] args) {
        System.exit(new F_SignatureTest_02().test(args));
    }
}
