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
package org.apache.harmony.test.func.api.java.security.cert.F_CertPathTest_04;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 04.10.2005
 */
public class F_CertPathTest_04 extends ScenarioTest {

    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    private String m_testCertFile;
    private String m_testKeyStore;
    private char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    
    public int test() {
        FileInputStream fis = null;
        ByteArrayInputStream bais = null;
        try {
            m_testCertFile = testArgs[0];
            m_testKeyStore = testArgs[1];
            
            log.info("Acquire an instance of CertificateFactory object");            
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            
            fis = new FileInputStream(m_testCertFile);
            log.info("Generate CertPath object");
            CertPath certPath = certFactory.generateCertPath((List)certFactory.generateCertificates(fis));
            log.info("Get encoded form of this certification path");
            byte[] certPathEncoded = certPath.getEncoded();
            bais = new ByteArrayInputStream(certPathEncoded);
            
            log.info("Obtain default encoding");
            Iterator it = certFactory.getCertPathEncodings();
            String encoding = (String)it.next();
            log.info("Reconstruct a CertPath object from it's encoded form");
            certPath = certFactory.generateCertPath(bais, encoding.equals("PkiPath") ? encoding : "PkiPath");
            log.info(certPath.toString());

            log.info("Create an instance of X509CertSelector object");
            X509CertSelector certSelector = new X509CertSelector();
            log.info("Set certificate selection constraints");
            certSelector.setSubject(new X500Principal("EMAILADDRESS=dummy@acme.com, CN=John Doe, OU=Engineering Department, O=ACME Software, L=Novosibirsk, ST=Russia, C=RU"));
            //certSelector.setIssuer(new X500Principal("CN=Peter Petroff, OU=Informational Resources Department, O=ACME Software, L=Novosibirsk, ST=Russia, C=RU"));
            certSelector.setIssuer(new X500Principal("EMAILADDRESS=ca@acme.com, CN=Ivan Ivanoff, OU=Trust Agency, O=ACME Secure Labs, L=Novosibirsk, ST=Russia, C=RU"));
            
            fis.close();
            fis = new FileInputStream(m_testKeyStore);
            log.info("Acquire an instance of KeyStore object");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            log.info("Load this keystore from the given file");
            keyStore.load(fis, password);
            Arrays.fill(password, ' ');
            log.info("Create an instance of PKIXParameters object");
            PKIXParameters pkixParams = new PKIXParameters(keyStore);
            
            log.info("Set validation constraints");
            pkixParams.setDate(new Date());
            pkixParams.setTargetCertConstraints(certSelector);
            pkixParams.setExplicitPolicyRequired(false);
            pkixParams.setRevocationEnabled(false);
            
            log.info("Acquire an instance of CertPathValidator object");
            CertPathValidator certPathValidator = CertPathValidator.getInstance(CertPathValidator.getDefaultType());
            log.info("Validate certification path");
            PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult)certPathValidator.validate(certPath, pkixParams);
//            log.info(result.toString());
            PublicKey pubKey = result.getPublicKey();
            log.info("Public key: " + pubKey.toString());
            
            TrustAnchor ta = result.getTrustAnchor();
            if (ta == null) {
                return fail("FAILED: the most-trusted Certificate Authority is null");
            }
            
            X509Certificate trustedCert = ta.getTrustedCert();
            if (trustedCert == null) {
                return fail("FAILED: the most-trusted CA certificate is null");
            }
            
            log.info("The most-trusted CA certificate: " + trustedCert.toString());        
            log.info("The most-trusted certificate's subject name: " + trustedCert.getSubjectX500Principal().getName());
            log.info("The most-trusted certificate's issuer name: " + trustedCert.getIssuerX500Principal().getName());
        } catch (CertificateException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());            
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());            
        } catch (CertPathValidatorException e) {
            e.printStackTrace();
            log.info(e.getCertPath().toString());
            log.info(""+ e.getIndex());
            return fail("FAILED: " + e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {                
            }
        }
        
        
        return pass("Test passed");
    }

    public static void main(String[] args) {
        System.exit(new F_CertPathTest_04().test(args));
    }
}
