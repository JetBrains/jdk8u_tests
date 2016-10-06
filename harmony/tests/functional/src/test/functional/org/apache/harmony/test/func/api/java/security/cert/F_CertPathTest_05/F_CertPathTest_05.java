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
package org.apache.harmony.test.func.api.java.security.cert.F_CertPathTest_05;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.apache.harmony.test.func.share.ScenarioTest;

//import java.security.*;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Created on Oct 5, 2005 
 */
public class F_CertPathTest_05 extends ScenarioTest {

    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    
    private String m_CRLFile;
    private String m_testCertFile;
    private String m_CA_RootCertFile;
    //private String m_ACME_CACertFile; 
    //private String m_CARootKeyFile;
    public int test() {
        
        //Provider p = new BouncyCastleProvider();
        //Security.addProvider(p);
        
        m_testCertFile = testArgs[0];
        m_CA_RootCertFile = testArgs[1];
        //m_ACME_CACertFile = testArgs[2];
        m_CRLFile = testArgs[2];
        //m_CARootKeyFile = testArgs[4];
        
        FileInputStream fis = null;
        try {
            log.info("Acquire an instance of CertificateFactory");
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            List certList = new ArrayList();
            
            fis = new FileInputStream(m_testCertFile);
            X509Certificate testCert = (X509Certificate)certFactory.generateCertificate(fis);
            log.info("Tested cert: " + testCert.toString());
            certList.add(testCert);
            fis.close();
            
//            fis = new FileInputStream(m_ACME_CACertFile);
//            X509Certificate ACME_CACert = (X509Certificate)certFactory.generateCertificate(fis);
//            certList.add(ACME_CACert);
//            fis.close();            
            
            fis = new FileInputStream(m_CA_RootCertFile);
            X509Certificate CARootCert = (X509Certificate)certFactory.generateCertificate(fis);
            log.info("CA root cert: " + CARootCert.toString());
            certList.add(CARootCert);
            fis.close();            
            
            fis = new FileInputStream(m_CRLFile);
            //X509CRL crl = (X509CRL)certFactory.generateCRL(fis);
            //log.info(crl.toString());
            List crlList = (ArrayList)certFactory.generateCRLs(fis);
            if (crlList == null) {
                crlList = new ArrayList();
                X509CRL crl = (X509CRL)certFactory.generateCRL(fis);
                crlList.add(crl);
            }
            
            CertPath certPath = certFactory.generateCertPath(certList);            
            
            List certStoreCollection = new ArrayList();
            certStoreCollection.addAll(crlList);
            certStoreCollection.addAll(certList);
            
            CollectionCertStoreParameters ccsParams = new CollectionCertStoreParameters(certStoreCollection);            
            
            CertStore certStore = CertStore.getInstance("Collection", ccsParams);
            
            X509CertSelector certSelector = new X509CertSelector();
            certSelector.setSubject(new X500Principal("EMAILADDRESS=dummy@acme.com, CN=John Doe, OU=Engineering Department, O=ACME Software, L=Novosibirsk, ST=Russia, C=RU").getEncoded()); 
            //certSelector.setIssuer(new X500Principal("CN=Peter Petroff, OU=Informational Resources Department, O=ACME Software, L=Novosibirsk, ST=Russia, C=RU").getEncoded());
            certSelector.setIssuer(new X500Principal("EMAILADDRESS=ca@acme.com, CN=Ivan Ivanoff, OU=Trust Agency, O=ACME Secure Labs, L=Novosibirsk, ST=Russia, C=RU").getEncoded());
            
//            X509CRLSelector crlSelector = new X509CRLSelector();
//            crlSelector.addIssuerName(new X500Principal("CN=Ivan Ivanoff, OU=Trust Agency, O=ACME Secure Labs, L=Novosibirsk, ST=Russia, C=RU").getEncoded());
//            crlSelector.setCertificateChecking(ACME_CACert);
            
            TrustAnchor caRootTA = new TrustAnchor(new X500Principal("EMAILADDRESS=ca@acme.com, CN=Ivan Ivanoff, OU=Trust Agency, O=ACME Secure Labs, L=Novosibirsk, ST=Russia, C=RU"), CARootCert.getPublicKey(), null);
            //TrustAnchor acmeTA = new TrustAnchor(ACME_CACert, null);
            
//            Set trustAnchors = new HashSet();
//            trustAnchors.add(caRootTA);
//            trustAnchors.add(acmeTA);
            
            PKIXParameters pkixParams = new PKIXParameters(Collections.singleton(caRootTA));
            pkixParams.addCertStore(certStore);
            pkixParams.setTargetCertConstraints(certSelector);
            pkixParams.setRevocationEnabled(true);
            
            CertPathValidator cpv = CertPathValidator.getInstance(CertPathValidator.getDefaultType());
            log.info("Certpath: " + certPath.toString());
            log.info("Params: " + pkixParams.toString());
            PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult)cpv.validate(certPath, pkixParams);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (CertificateException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (CRLException e) {
            e.printStackTrace();
            return fail("Test failed: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (CertPathValidatorException e) {
            log.info(e.getCertPath().toString());
            log.info("Index: " + e.getIndex());
            e.printStackTrace();
            return pass("Test passed");
        } /*catch (CertStoreException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } */finally {
            try {
                if (fis != null) {
                    fis.close();
                } 
            } catch (IOException e) {                    
            }
        }
        
        return fail("Certification path is valid, however one of the trusted certificates has been revoked. Test failed");
    }

    public static void main(String[] args) {
        System.exit(new F_CertPathTest_05().test(args));
    }
}
