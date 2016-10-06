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
package org.apache.harmony.test.func.api.java.security.cert.F_CertPathTest_06;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRLException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on Oct 7, 2005 
 */
public class F_CertPathTest_06 extends ScenarioTest {

    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    private String m_testCertFile;
    private String m_CARootCertFile;
    //private String m_ACMECertFile;
    private String m_crlFile;
    public int test() {
        
        m_testCertFile = testArgs[0];
        m_CARootCertFile = testArgs[1];
        //m_ACMECertFile = testArgs[2];
        m_crlFile = testArgs[2];
        
        FileInputStream fis = null;
        try {
            List certList = new ArrayList();
            
            log.info("Acquire an instance of CertificateFactory object");
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            
            fis = new FileInputStream(m_testCertFile);
            //List certList = (ArrayList)certFactory.generateCertificates(fis);
            log.info("Generate an end entity certificate");
            X509Certificate testCert = (X509Certificate)certFactory.generateCertificate(fis);
            certList.add(testCert);
            System.out.println("Test cert: " + testCert.toString());
            fis.close();
            
            fis = new FileInputStream(m_crlFile);
            log.info("Generate a list of revoked certificates");
            X509CRL crl = (X509CRL)certFactory.generateCRL(fis);
            
            ////////////////////////////////
//            fis.close();
//            fis = new FileInputStream(m_ACMECertFile);
//            X509Certificate ACMECACert = (X509Certificate)certFactory.generateCertificate(fis);
//            certList.add(ACMECACert);
//            System.out.println("ACME Cert: " + ACMECACert.toString());
            
            fis.close();
            fis = new FileInputStream(m_CARootCertFile);
            log.info("Generate a CA certificate");
            X509Certificate CARootCert = (X509Certificate)certFactory.generateCertificate(fis);
            certList.add(CARootCert);            
            System.out.println("CA Root Cert: " + CARootCert.toString());
            
            TrustAnchor CARootTrustAnchor = new TrustAnchor(CARootCert, null);
            //TrustAnchor ACMECaTrustAnchor = new TrustAnchor(ACMECACert, null);
            ////////////////////////////////
            
            log.info("Create an instance of TrustAnchor object based on CA certificate");
            //TrustAnchor CARootTrustAnchor = new TrustAnchor(new X500Principal("CN=Ivan Ivanoff, OU=Trust Agency, O=ACME Secure Labs, L=Novosibirsk, ST=Russia, C=RU"), CARootCert.getPublicKey(), null);
            //TrustAnchor ACMECaTrustAnchor = new TrustAnchor(new X500Principal("CN=Peter Petroff, OU=Informational Resources Department, O=ACME Software, L=Novosibirsk, ST=Russia, C=RU"), ACMECACert.getPublicKey(), null);            
            
            Set trustAnchors = new HashSet();
            //trustAnchors.add(ACMECaTrustAnchor);            
            trustAnchors.add(CARootTrustAnchor);
            
            log.info("Create an instance of X509CertSelector object");
            X509CertSelector certSelector = new X509CertSelector();
            log.info("Add certificates selection criteria");
            certSelector.setSubject(new X500Principal("EMAILADDRESS=dummy@acme.com, CN=John Doe, OU=Engineering Department, O=ACME Software, L=Novosibirsk, ST=Russia, C=RU"));
//            certSelector.setIssuer(new X500Principal("CN=Peter Petroff, OU=Informational Resources Department, O=ACME Software, L=Novosibirsk, ST=Russia, C=RU"));
//            certSelector.setSubject(new X500Principal("CN=Peter Petroff, OU=Informational Resources Department, O=ACME Software, L=Novosibirsk, ST=Russia, C=RU"));
            certSelector.setIssuer(new X500Principal("EMAILADDRESS=ca@acme.com, CN=Ivan Ivanoff, OU=Trust Agency, O=ACME Secure Labs, L=Novosibirsk, ST=Russia, C=RU"));

            log.info("Create an instance of CollectionCertStoreParameters object based on the list of certificates");
            CollectionCertStoreParameters ccsParams = new CollectionCertStoreParameters(certList);
            log.info("Add a CRL list into newly created collection");
            ccsParams.getCollection().add(crl);
            
            log.info("Acquire an instance of CertStore object based on the parameters constructed earlier");
            CertStore certStore = CertStore.getInstance("Collection", ccsParams);
            
            log.info("Create an instance of PKIXBuilderParameters object");
            PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustAnchors, certSelector);
            
            /////////////////////////////////////////////
//            fis.close();
//            fis = new FileInputStream(testArgs[4]);
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(fis, new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'});
//            PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(keyStore, certSelector);
            /////////////////////////////////////////////
            
            log.info("Add a CertStore object into params");
            pkixParams.addCertStore(certStore);
            log.info("Enable certificates revocation");
            pkixParams.setRevocationEnabled(true);
            
            log.info("Acquire an instance of CertPathBuilder object of default type");
            CertPathBuilder certPathBuilder = CertPathBuilder.getInstance(CertPathBuilder.getDefaultType());
            log.info("Attempt to build a certification path using revoked certificate");
            PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult)certPathBuilder.build(pkixParams);            
        } catch (CertificateException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } /*catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } */catch (IOException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (CertPathBuilderException e) {
            e.printStackTrace();
            return pass("Test passed");
        } catch (CRLException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } /*catch (KeyStoreException e) {
            e.printStackTrace();
        } */finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {                
            }
        }
        
        return fail("FAILED: in spite of the fact that an end entity certificate has been revoked, the certification path was successfully built");        
    }

    public static void main(String[] args) {
        System.exit(new F_CertPathTest_06().test(args));
    }
    
    
}