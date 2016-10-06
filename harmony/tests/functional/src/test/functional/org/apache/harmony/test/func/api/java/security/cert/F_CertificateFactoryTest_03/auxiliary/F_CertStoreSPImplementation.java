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
package org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_03.auxiliary;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.util.Collection;

/**
 * Created on Oct 20, 2005 
 */
public class F_CertStoreSPImplementation extends CertStoreSpi {

    private CertStore m_certStore;
    /**
     * @param arg0
     * @throws java.security.InvalidAlgorithmParameterException
     */
    public F_CertStoreSPImplementation(CertStoreParameters params) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        super(params);
        m_certStore = CertStore.getInstance("Collection", params);
        System.out.println("F_CertStoreSPImplementation ctor has been called");
    }

    /* (non-Javadoc)
     * @see java.security.cert.CertStoreSpi#engineGetCertificates(java.security.cert.CertSelector)
     */
    public Collection engineGetCertificates(CertSelector certSelector) throws CertStoreException {
        return m_certStore.getCertificates(certSelector);
    }

    /* (non-Javadoc)
     * @see java.security.cert.CertStoreSpi#engineGetCRLs(java.security.cert.CRLSelector)
     */
    public Collection engineGetCRLs(CRLSelector crlSelector) throws CertStoreException {
        return m_certStore.getCRLs(crlSelector);
    }

}
