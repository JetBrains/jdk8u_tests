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
package org.apache.harmony.test.func.api.java.security.cert.F_CertPathTest_02;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
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
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on Sep 27, 2005 
 */
public class F_CertPathTest_02 extends ScenarioTest {

    private String m_certFile;
    private String m_KeyStorename;
    private char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {
        FileInputStream cert_in = null, ks_in = null;
        try {
            if (testArgs.length < 2) {
                return error("Not enough test parameters");
            }
            
            m_certFile = testArgs[0];
            m_KeyStorename = testArgs[1];
            
            cert_in = new FileInputStream(m_certFile);
            log.info("Acquire an instance of CertificateFactory object");
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            log.info("Generate certificate list from the given certificatefile");
            List l = (ArrayList)certFactory.generateCertificates(cert_in);            
            log.info("The list contains " + l.size() + " certificate(s)");            
            log.info("Acquire CertPath object by generating it using Certificatefactory.generateCertPath(List<Certificate>) method");
            CertPath certPath = certFactory.generateCertPath(l);
            String type = certPath.getType();
            if (!type.equals("X.509")) {
                return fail("The type of certificates in this certification path is: '" + type + "' instead of 'X.509'. Test failed");
            }
            log.info("The type of certificate in this certification path is: " + certPath.getType());
            log.info("Certificates contained in this CertPath are: " + certPath.toString());

            byte[] certPathEncoded = certPath.getEncoded();
            ByteArrayInputStream bais = new ByteArrayInputStream(certPathEncoded);
            CertPath certPath2 = certFactory.generateCertPath(bais);
            log.info("Certificates contained in this CertPath are: " + certPath2.toString());
            
            log.info("Compare two CertPath objects obtained in two different ways.");
            if (!certPath.equals(certPath2)) {
                return fail("Two objects aren't equal. Test failed");
            }
            log.info("Objects are equal");
            
            log.info("Compute a hash for the first CertPath object:");
            int hashCode1 = certPath.hashCode();
            log.info("" + hashCode1);
            log.info("Compute a hash for the second CertPath object:");
            int hashCode2 = certPath2.hashCode();
            log.info("" + hashCode2);
            log.info("Compare these two has codes");
            if (hashCode1 != hashCode2) {
                return fail("Two hash codes computed from two equal object are not the same. Test failed");
            }
//            log.info("Construct a key store from the given file");
//            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//            ks_in = new FileInputStream(m_KeyStorename); 
//            log.info("Load this KeyStore");
//            ks.load(ks_in, password);
//            Arrays.fill(password, ' ');
//            Certificate[] certs = ks.getCertificateChain("cert_test");
//            log.info("Chain length: " + certs.length);
            
//            X509CertSelector certSelector = new X509CertSelector();
//            Iterator it = l.iterator();
//            while (it.hasNext()) {
//                X509Certificate c = (X509Certificate)it.next();
//                certSelector.setCertificate(c);
//            }
//            
//            log.info("Acquire an instance of CertPathBuilder object");
//            CertPathBuilder certPathBuilder = CertPathBuilder.getInstance("PKIX");
//            log.info("Acquire an instance of PKIXBuilderParameters");
//            PKIXBuilderParameters pkixBuildParams = new PKIXBuilderParameters(ks, certSelector);
//            log.info("Build a Certpath object");
//            PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult)certPathBuilder.build(pkixBuildParams);
//            CertPath certpath2 = result.getCertPath();
//            log.info("Certificates contained in this CertPath are: " + certpath2.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (CertificateException e) {
            e.printStackTrace();
            return fail("Test failed");
        } /*catch (KeyStoreException e) {
            e.printStackTrace();
            return error("Error:" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage()); 
        } catch (CertPathBuilderException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } */finally {
            try {
                if (cert_in != null) {
                    cert_in.close();
                }
                
                if (ks_in != null) {
                    ks_in.close();
                }
            } catch (IOException e) {                    
            }
        }
        
        return pass("Test passed");
    }

    public static void main(String[] args) {
        System.exit(new F_CertPathTest_02().test(args));
    }
}
