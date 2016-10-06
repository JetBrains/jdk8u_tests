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
package org.apache.harmony.test.func.api.java.security.spec.F_EncodedKeySpecTest_01;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 01.09.2005 
 */
public class F_EncodedKeySpecTest_01 extends ScenarioTest {

    private static final String m_TestString = "This is a sample string to be used during encryption";
    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {
        try {
            log.info("Acquire an instance of KeyPairGenerator object");
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            log.info("Generate key pair");
            KeyPair keyPair = keyPairGen.generateKeyPair();
            log.info("Create an instance of MyEncodedKeySpec object");
            EncodedKeySpec keySpec = new MyEncodedKeySpec(keyPair.getPrivate().getEncoded());
            if (keySpec == null) {
                return fail("Couldn't create an instance of EncodedKeySpec");
            }
            
            log.info("\nEncrypt data");
            byte[] encryptedData = encrypt(keySpec);
            if (encryptedData == null) {
                return error("Couldn't encrypt data");
            }
            
            log.info("\nDecrypt encrypted data");
            byte[] decryptedData = decrypt(encryptedData, new MyEncodedKeySpec(keyPair.getPublic().getEncoded()));
            log.info("Compare strings");
            if (new String(decryptedData).equals(m_TestString)) {                
                return pass("Test passed");
            } else {
                return error("Strings are not equal after decryption.");
            }
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        }
        
        //return fail("Test failed");
    }
    
    private byte[] encrypt(KeySpec keySpec) {
        
        try {
            log.info("Acquire an instance of KeyFactory object");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            log.info("Generate private key based on encoded key material");
            Key key = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(((MyEncodedKeySpec)keySpec).getEncoded()));
            log.info("Acquire an instance of Cipher object");
            Cipher cipher = Cipher.getInstance("RSA");
            log.info("Init Cipher");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            log.info("Encrypt data");
            return cipher.doFinal(m_TestString.getBytes());            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    private byte[] decrypt(byte[] encryptedData, KeySpec keySpec) {
        try {
            log.info("Acquire an instance of KeyFactory object");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            log.info("Acquire an instance of Cipher object");
            Cipher cipher = Cipher.getInstance("RSA");
            log.info("Init Cipher");
            cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePublic(new X509EncodedKeySpec(((MyEncodedKeySpec)keySpec).getEncoded())));
            log.info("Decrypt data");
            return cipher.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    final class MyEncodedKeySpec extends EncodedKeySpec {
        private byte[] m_encodedKeySpec;
        
        public MyEncodedKeySpec(byte[] encodedKeySpec) {
            super(encodedKeySpec);
            this.m_encodedKeySpec = encodedKeySpec;
            log.info("MyEncodedKeySpec ctor has been called");
        }
        
        /* (non-Javadoc)
         * @see java.security.spec.EncodedKeySpec#getFormat()
         */
        public String getFormat() {
            return null;
        }
    }
    public static void main(String[] args) {
        System.exit(new F_EncodedKeySpecTest_01().test(args));
    }
}
