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
package org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_03;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Collections;

import org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_03.auxiliary.F_CertPathBuilderSPImplementation;
import org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_03.auxiliary.F_CertStoreSPImplementation;
import org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_03.auxiliary.F_CertificateTestProvider;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on Oct 17, 2005 
 */
public class F_CertificateFactoryTest_03 extends ScenarioTest {

    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public static final String providerName = "org.apache.harmony.test.func.api.java.security.CertificateFactoryTestProvider";
    public static final double providerVersion = 1.0;
    public static final String providerInfo = "CertificateFactoryTestProvider v1.0 for testing purpose only";
    private String m_testCertFile;
    private String m_CARootCertFile;
    
    Provider p = null;
    //F_CertificateFactorySPImplementation certFactorySPI;
    F_CertPathBuilderSPImplementation certPathBuilderSPI;
    F_CertStoreSPImplementation certStoreSPI;
    CollectionCertStoreParameters ccsParams;
    
    public int test() {
        m_testCertFile = testArgs[0];
        m_CARootCertFile = testArgs[1];
        
        log.info("Create an instance of test provider\n");
        p = new F_CertificateTestProvider(providerName, providerVersion, providerInfo);
        if (p == null) {
            return fail("FAILED: Coudln't create a CertificateTestProvider object");
        }
        
        Security.addProvider(p);
        
        log.info("Acquire an instance of CertificateFactory object from newly created test provider");
        CertificateFactory certFactory = null;
        try {
            certFactory = CertificateFactory.getInstance("X.509", p);
        } catch (CertificateException ce) {
            ce.printStackTrace();
            return fail("FAILED: Couldn't get an instance of CertificateFactory object from the provider '" + p.getName() + "'");
        }        
         
        log.info("The name of acquired CertificateFactory object's provider:" + certFactory.getProvider().getName());        

        FileInputStream fis = null;
        X509Certificate caCert = null;
        X509Certificate testCert = null;
        try {
            fis = new FileInputStream(m_CARootCertFile);
            log.info("Generate CA certificate from the given file");
            caCert = (X509Certificate)certFactory.generateCertificate(fis);
            fis.close();
            
            fis = new FileInputStream(m_testCertFile);
            log.info("Generate an end-entity certificate from the given file");
            testCert = (X509Certificate)certFactory.generateCertificate(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return error("Error: " + fnfe.getMessage());
        } catch (CertificateException ce) {
            ce.printStackTrace();
            return error("Error: couldn't generate a certificate: " + ce.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ioe) {            
            }
        }

        ccsParams = new CollectionCertStoreParameters(Collections.singletonList(testCert));
        
        log.info("Create an instance of CertStoreSpi implementation object");
        try {
            certStoreSPI = new F_CertStoreSPImplementation(ccsParams);
        } catch (InvalidAlgorithmParameterException iape) {
            iape.printStackTrace();
            return error("Error: " + iape.getMessage());
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return error("Error: " + nsae.getMessage());
        }
        
        log.info("Create an instance of MyCertStore object");
        CertStore certStore = new MyCertStore();
        if (certStore == null) {
            return fail("FAILED: couldn't create an instance of MyCertStore object.");
        }

        TrustAnchor ta = new TrustAnchor(caCert, null);
        X509CertSelector certSelector = new X509CertSelector();
        certSelector.setSubjectPublicKey(testCert.getPublicKey());
        certSelector.setSubject(testCert.getSubjectX500Principal());
        
        PKIXBuilderParameters pkixParams = null;
        try {
            log.info("Create an instance of PKIXBuilderParameters object");
            pkixParams = new PKIXBuilderParameters(Collections.singleton(ta), certSelector);
            pkixParams.addCertStore(certStore);
            pkixParams.setRevocationEnabled(false);
            log.info("Create an instance of CertPathBuilderSpi implementation object");
            certPathBuilderSPI = new F_CertPathBuilderSPImplementation();
        } catch (InvalidAlgorithmParameterException iape) {
            iape.printStackTrace();
            return error("Error: couldn't instantiate a PKIXBuilderParameters object. Reason: " + iape.getMessage());
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return error("Error: couldn't instantiate a CertPathBuilderSpi object. Reason: " + nsae.getMessage());
        }
        
        log.info("Create an instance of MyCertPathBuilder object");
        CertPathBuilder certPathBuilder = new MyCertPathBuilder();
        if (certPathBuilder == null) {
            return fail("FAILED: Couldn't create an instance of MyCertPathBuilder object.");
        }
        
        try {
            log.info("Build a certification path");
            PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult)certPathBuilder.build(pkixParams);
            log.info("Certification path: " + result.getCertPath().toString());
        } catch (CertPathBuilderException cpbe) {
            cpbe.printStackTrace();
            return fail("FAILED: couldn't create a certification path. Reason: " + cpbe.getMessage());
        } catch (InvalidAlgorithmParameterException iape) {
            iape.printStackTrace();
            return error("Error: " + iape.getMessage());
        }
        
        return pass("Test passed");
    }
    
    class MyCertPathBuilder extends CertPathBuilder {
        
        public MyCertPathBuilder() {
            super(certPathBuilderSPI, p, "PKIX");
            log.info("MyCertPathBuilder ctor has been called");
        }        
    }
    
    class MyCertStore extends CertStore {
        public MyCertStore() {
            super(certStoreSPI, p, "Collection", ccsParams);
            log.info("MyCertStore ctor has been called");
        }        
    }

    public static void main(String[] args) {
        System.exit(new F_CertificateFactoryTest_03().test(args));
    }
}