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
package org.apache.harmony.test.func.api.java.security.cert.F_CertPathTest_03;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on Sep 29, 2005 
 */
public class F_CertPathTest_03 extends ScenarioTest {

    private String m_testCertFile;
    private String m_caCertFile;
    private String m_caRootCertFile;
    private String m_keyStoreFile;
    private char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {

        m_testCertFile = testArgs[0];
        m_caCertFile = testArgs[1];
        m_caRootCertFile = testArgs[2];
        m_keyStoreFile = testArgs[3];
        
        FileInputStream fis = null;
        try {
            List certList = new ArrayList(3);
            
            log.info("Create an object of X509CertSelector");
            X509CertSelector certSelector = new X509CertSelector();
            
            log.info("-------- Generate certificates ---------");
            log.info("Acquire an instance of Certificatefactory");
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            
            log.info("Generate CA root certificate");
            fis = new FileInputStream(m_caRootCertFile);
            X509Certificate caRootCert = (X509Certificate)certFactory.generateCertificate(fis);
            certList.add(caRootCert);
            fis.close();            

            log.info("Generate CA certificate");
            fis = new FileInputStream(m_caCertFile);
            X509Certificate caCert = (X509Certificate)certFactory.generateCertificate(fis);
            certList.add(caCert);
            fis.close();

            log.info("Generate test certificate");
            fis = new FileInputStream(m_testCertFile);
            X509Certificate testCert = (X509Certificate)certFactory.generateCertificate(fis);
            certSelector.setCertificate(testCert);
            certList.add(testCert);
            fis.close();
  
            log.info("--------- End generate certificates --------");
            
            log.info("Create an object of CollectionCertStoreParameters");
            CollectionCertStoreParameters ccsParams = new CollectionCertStoreParameters(certList);
            log.info("Acquire an instance of CertStore object");
            CertStore certStore = CertStore.getInstance("Collection", ccsParams);
            
            log.info("Acquire an instance of KeyStore object");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            fis = new FileInputStream(m_keyStoreFile);
            log.info("Load this KeyStore from the given file");
            keyStore.load(fis, password);
            Arrays.fill(password, ' ');
            
            log.info("Create an object of PKIXBuilderParameters");
            PKIXBuilderParameters pkixBuilderParams = new PKIXBuilderParameters(keyStore, certSelector);
            pkixBuilderParams.addCertStore(certStore);
            pkixBuilderParams.setRevocationEnabled(false);
            pkixBuilderParams.setDate(new Date((2006 - 1900), 9, 1));
            
            log.info("Acquire an instance of CertPathBuilder object");
            CertPathBuilder certpathBuilder = CertPathBuilder.getInstance(CertPathBuilder.getDefaultType());
            log.info("Build a certification path");
            PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult)certpathBuilder.build(pkixBuilderParams);            
            log.info("Acquire a CertPath object from the build result");
            CertPath certPath = result.getCertPath();
            if (certPath.getCertificates().size() == 0) {
                return fail("Number of certificates in this certification path must not be zero. Test failed");
            }
            log.info("This CertPath object is: " + certPath.toString());
        } catch (CertificateException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (CertPathBuilderException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {                
            }
        }
        
        return pass("Test passed");
    }

    public static void main(String[] args) {
        System.exit(new F_CertPathTest_03().test(args));
    }
}