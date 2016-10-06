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
package org.apache.harmony.test.func.api.java.security.F_SignatureTest_03.auxiliary;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignatureSpi;

/**
 * Created on 25.08.2005
 */
public class F_SignatureSPImplementation extends SignatureSpi {

    private Signature m_Signature;    
    /**
     * 
     */
    public F_SignatureSPImplementation(String algorithm) throws NoSuchAlgorithmException {
        super();
        m_Signature = Signature.getInstance(algorithm);
        System.out.println("F_SignatureSPImplementation ctor has been called.");
    }
    

    /* (non-Javadoc)
     * @see java.security.SignatureSpi#engineInitVerify(java.security.PublicKey)
     */
    protected void engineInitVerify(PublicKey key) throws InvalidKeyException {
        m_Signature.initVerify(key);
        System.out.println("F_SignatureSPImplementation::engineInitVerify(PublicKey) has been called");
    }

    /* (non-Javadoc)
     * @see java.security.SignatureSpi#engineInitSign(java.security.PrivateKey)
     */
    protected void engineInitSign(PrivateKey key) throws InvalidKeyException {
        m_Signature.initSign(key);
        System.out.println("F_SignatureSPImplementation::engineInitSign(PrivateKey) has been called");
    }

    /* (non-Javadoc)
     * @see java.security.SignatureSpi#engineUpdate(byte)
     */
    protected void engineUpdate(byte b) throws SignatureException {
        m_Signature.update(b);
        System.out.println("F_SignatureSPImplementation::engineUpdate(byte) has been called");
    }

    /* (non-Javadoc)
     * @see java.security.SignatureSpi#engineUpdate(byte[], int, int)
     */
    protected void engineUpdate(byte[] b, int offset, int length) throws SignatureException {
        m_Signature.update(b, offset, length);
        System.out.println("F_SignatureSPImplementation::engineUpdate(byte[], int, int) has been called");
    }

    /* (non-Javadoc)
     * @see java.security.SignatureSpi#engineSign()
     */
    protected byte[] engineSign() throws SignatureException {
        System.out.println("F_SignatureSPImplementation::engineSign() has been called");
        return m_Signature.sign();        
    }

    /* (non-Javadoc)
     * @see java.security.SignatureSpi#engineVerify(byte[])
     */
    protected boolean engineVerify(byte[] b) throws SignatureException {
        System.out.println("F_SignatureSPImplementation::engineVerify(byte[]) has been called");
        return m_Signature.verify(b);
    }

    /* (non-Javadoc)
     * @see java.security.SignatureSpi#engineSetParameter(java.lang.String, java.lang.Object)
     */
    protected void engineSetParameter(String param, Object value) throws InvalidParameterException {      

    }

    /* (non-Javadoc)
     * @see java.security.SignatureSpi#engineGetParameter(java.lang.String)
     */
    protected Object engineGetParameter(String param) throws InvalidParameterException {
        return null;
    }
}
