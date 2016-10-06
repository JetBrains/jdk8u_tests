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
package org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_02.auxiliary;

import java.io.InputStream;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateFactorySpi;
import java.util.Collection;

/**
 * Created on 25.08.2005
 */
public class F_CertificateFactorySPImplementation extends CertificateFactorySpi {

    private CertificateFactory certFactory;
    
    /**
     * 
     */
    public F_CertificateFactorySPImplementation(String type) throws CertificateException {
        super();        
        certFactory = CertificateFactory.getInstance("X.509");
        System.out.println("F_CertificateFactorySPImplementation ctor has been called");
    }

    /* (non-Javadoc)
     * @see java.security.cert.CertificateFactorySpi#engineGenerateCertificate(java.io.InputStream)
     */
    public Certificate engineGenerateCertificate(InputStream is) throws CertificateException {
        //certFactory = CertificateFactory.getInstance("X.509");
        System.out.println("F_CertificateFactorySPImplementation::engineGenerateCertificate(InputStream) has been called");
        return certFactory.generateCertificate(is);
    }

    /* (non-Javadoc)
     * @see java.security.cert.CertificateFactorySpi#engineGenerateCertificates(java.io.InputStream)
     */
    public Collection engineGenerateCertificates(InputStream is) throws CertificateException {
        //certFactory = CertificateFactory.getInstance("X.509");
        System.out.println("F_CertificateFactorySPImplementation::engineGenerateCertificates(InputStream) has been called");
        return certFactory.generateCertificates(is);
    }

    /* (non-Javadoc)
     * @see java.security.cert.CertificateFactorySpi#engineGenerateCRL(java.io.InputStream)
     */
    public CRL engineGenerateCRL(InputStream is) throws CRLException {
        System.out.println("F_CertificateFactorySPImplementation::engineGenerateCRL(InputStream) has been called");
        return certFactory.generateCRL(is);
    }

    /* (non-Javadoc)
     * @see java.security.cert.CertificateFactorySpi#engineGenerateCRLs(java.io.InputStream)
     */
    public Collection engineGenerateCRLs(InputStream is) throws CRLException {
        System.out.println("F_CertificateFactorySPImplementation::engineGenerateCRLs(InputStream) has been called");
        return certFactory.generateCRLs(is);
    }

}
