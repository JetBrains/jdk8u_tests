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
package org.apache.harmony.test.func.api.java.security.F_AlgorithmParametersTest_02;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.jpda.tests.framework.LogWriter;

/**
 * Created on 24.08.2005
 */
public class F_AlgorithmParametersTest_02 extends ScenarioTest {

    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    private static final String m_AlgorithmName = "DESede";
    private static final String m_StringToDecrypt = "A quick brown fox jumped over the lazy dog.";
    
    SecretKey secretKey = null;    
    byte[] encodedAlgorithmParams;
    byte[] encryptedData = null;
    
    public int test() {
        try {
            System.out.print("Acquire an instance of KeyGenerator object with '" + m_AlgorithmName + "' algorithm...");
            KeyGenerator keyGen = KeyGenerator.getInstance(m_AlgorithmName);
            System.out.print("OK\nGenerate DESede key...");
            Key key = keyGen.generateKey();
            System.out.print("OK\nObtain encoded key data...");
            byte[] encodedKey = key.getEncoded();
            System.out.print("OK\nAcquire an instance of SecretKeyFactory object with '" + m_AlgorithmName + "' algorithm...");            
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(m_AlgorithmName);                        
            System.out.print("OK\nCreate DESedeKeySpec object...");
            DESedeKeySpec keySpec = new DESedeKeySpec(encodedKey);
            System.out.print("OK\nGenerate a secret key...");
            secretKey = secretKeyFactory.generateSecret(keySpec);
            System.out.println("OK");

            if (encrypt())
            {
                if (decrypt()) {
                    return pass("Test passed");
                }
            }
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } 
        
        return fail("Test failed");
    }
    
    private boolean encrypt() {
        System.out.println("\nInitiate encryption");
        System.out.print("Create an initialization vector...");
        IvParameterSpec ivParamSpec = new IvParameterSpec(new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        System.out.println("OK");
        Cipher cipher;
        boolean bResult = false;
        try {            
            System.out.print("Acquire an instance of Cipher object...");
            cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            System.out.print("OK\nInit a cipher...");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParamSpec);
            System.out.print("OK\nEncrypt...");
            encryptedData = cipher.doFinal(m_StringToDecrypt.getBytes());
            System.out.println("OK");
            
            System.out.print("Acquire AlgorithmParameters being used during encryption...");
            AlgorithmParameters ap = cipher.getParameters();
            System.out.print("OK\nGet encoded algorithm parameters for further use...");
            encodedAlgorithmParams = ap.getEncoded();            
            System.out.println("OK");
            bResult = true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return bResult;

    }
    
    private boolean decrypt() {
        System.out.println("\nInitiate decryption");
        boolean bResult = false;
        try {
            System.out.print("Acquire an instance of AlgorithmParameters object...");
            AlgorithmParameters ap = AlgorithmParameters.getInstance(secretKey.getAlgorithm());
            System.out.print("OK\nInit algorithm parameters with the data obtained during encryption...");
            ap.init(encodedAlgorithmParams);
            System.out.print("OK\nGet IvParameterSpec out of algorithm parameters...");
            IvParameterSpec ivParamSpec = (IvParameterSpec)ap.getParameterSpec(Class.forName("javax.crypto.spec.IvParameterSpec"));
            System.out.print("OK\nAcquire an instance of Cipher object...");
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            System.out.print("OK\nInit Cipher...");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParamSpec);
            System.out.print("OK\nDecrypt...");
            byte[] decryptedData = cipher.doFinal(encryptedData);
            System.out.print("OK\nCheck whether both original string and decrypted string are equal...");
            String decryptedString = new String(decryptedData);
            bResult = decryptedString.equals(m_StringToDecrypt);
            System.out.println("OK");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        
        return bResult;
    }

    public static void main(String[] args) {
        System.exit(new F_AlgorithmParametersTest_02().test(args));
    }
}
