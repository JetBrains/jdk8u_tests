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
package org.apache.harmony.test.func.api.java.security.cert.F_CertStoreTest_02;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRLException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 12.10.2005
 */
public class F_CertStoreTest_02 extends ScenarioTest {

    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    private String m_testCertFile;
    private String m_CARootCertFile;
    private String m_crlFile;
    private final static String CLINET_AUTH = "1.3.6.1.5.5.7.3.2";
    private final static String ISSUER_ALT_NAME = "ca@acme.com";
    private final static String SUBJECT_ALT_NAME = "dummy@acme.com";
    private final static int RFC822NAME_TYPE = 1;
    
    public int test() {
        
        m_testCertFile = testArgs[0];
        m_CARootCertFile = testArgs[1];
        m_crlFile = testArgs[2];        
        
        FileInputStream fis = null;
        try {
            log.info("Acquire an instance of CertificateFactory object");
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            
            log.info("----------- Generate certificates -----------");
            fis = new FileInputStream(m_testCertFile);
            log.info("Generate end-entity certificate");
            X509Certificate testCert = (X509Certificate)certFactory.generateCertificate(fis);
            //log.info(testCert.toString());
            fis.close();
            
            fis = new FileInputStream(m_CARootCertFile);
            log.info("Generate CA certificate");
            X509Certificate CARootCert = (X509Certificate)certFactory.generateCertificate(fis);
            fis.close();
            //log.info(CARootCert.toString());
            log.info("----------- End generate certificates");
            
            fis = new FileInputStream(m_crlFile);
            log.info("Generate CRL");
            X509CRL crl = (X509CRL)certFactory.generateCRL(fis);
            fis.close();
            
            log.info("Create an instance ob CollectionCertStoreParameters");
            CollectionCertStoreParameters ccsParams = new CollectionCertStoreParameters(new ArrayList(Collections.singletonList(testCert)));
            
            log.info("Acquire an instance of CertStore object");
            CertStore certStore = CertStore.getInstance("Collection", ccsParams);
            
            log.info("Add a list of revoked certificates into certstore collection");
            ((CollectionCertStoreParameters)certStore.getCertStoreParameters()).getCollection().add(crl);
            
            String certStoreType = certStore.getType();
            log.info("This CertStore type is: " + certStoreType);
            if (!certStoreType.equals("Collection")) {
                return fail("FAILED: the type of this cert store must be 'Collection' instead of '" + certStoreType + "'");
            }
            
            log.info("Create an instance of X509CertSelector");
            X509CertSelector certSelector = new X509CertSelector();
            log.info("Add various certificate selection criteria");
            certSelector.setCertificateValid(new Date());
            if (!certSelector.match(testCert)) {
                return fail("FAILED: the certificate doesn't match in spite of the fact that current date falls within the certificate validity period");                
            }
            
            certSelector.setSerialNumber(testCert.getSerialNumber());
            if (!certSelector.match(testCert)) {
                return fail("FAILED: the certificate doesn't match in spite of the fact that the specified serial number matches the certificate serial number");
            }
            
            certSelector.setExtendedKeyUsage(Collections.singleton(CLINET_AUTH));
            if (!certSelector.match(testCert)) {
                return fail("FAILED: the certificate doesn't match in spite of the fact that it has specified key extension");
            }
            
            
            boolean[] keyUsage = {true /*digitalSignature*/, false/*nonRepudiation*/, true/*keyEncipherment*/};
            certSelector.setKeyUsage(keyUsage);
            if (!certSelector.match(testCert)) {
                return fail("FAILED: the certificate doesn't match, though it has specified key usage extensions");
            }
            
            List subjectAltNameList = new ArrayList();
            subjectAltNameList.add(new Integer(RFC822NAME_TYPE));
            subjectAltNameList.add(SUBJECT_ALT_NAME);
            
            certSelector.setSubjectAlternativeNames(Collections.singletonList(new ArrayList(subjectAltNameList)));
            certSelector.setMatchAllSubjectAltNames(true);
            if (!certSelector.match(testCert)) {
                return fail("FAILED: the certificate doesn't match, though it contains all of the specified subject alternative names");
            }
            
            certSelector.setSubjectPublicKey(testCert.getPublicKey());
            if (!certSelector.match(testCert)) {
                return fail("FAILED: the certificate doesn't match in spite of the fact that the certificate contain the specified subject public key");
            }
            
            log.info("Create an instance of X509CRLSelector");
            X509CRLSelector crlSelector = new X509CRLSelector();
            log.info("Add various CRL selection criteria");
            crlSelector.setCertificateChecking(testCert);
            if (!crlSelector.match(crl)) {
                return fail("FAILED: couln't find any valid CRL");
            }
            
            crlSelector.setDateAndTime(crl.getThisUpdate());
            if (!crlSelector.match(crl)) {
                return fail("FAILED: the CRL doesn't match in spite of the fact that current date falls within the CRL validity period");
            }
            
            //X500Principal crlIssuerPrincipal = new X500Principal("CN=Ivan Ivanoff, OU=Trust Agency, O=ACME Secure Labs, L=Novosibirsk, ST=Russia, C=RU");
            X500Principal crlIssuerPrincipal = CARootCert.getSubjectX500Principal();
            crlSelector.setIssuers(Collections.singletonList(crlIssuerPrincipal));
            if (!crlSelector.match(crl)) {
                return fail("FAILED: the CRL doesn't match, though the issuer DN matches specified DN");
            }
            
            log.info("Acquire a list of certificates out of certstore matching specified criteria");
            Collection certList = certStore.getCertificates(certSelector);
            if (certList.size() != 1) {
                return error("Wrong number of certificates in list: " + certList.size());
            }
           
            log.info("Get a certificate from the list of certificates");
            X509Certificate retrievedTestCert = (X509Certificate)certList.iterator().next();
            if (!retrievedTestCert.equals(testCert)) {
                return fail("FAILED: two same certificates aren't equal");
            }
            
            //log.info(retrievedTestCert.toString());
            X500Principal testCertIssuer = retrievedTestCert.getIssuerX500Principal();
            log.info("Certificate's issuer: " + testCertIssuer.getName());
            if (!testCertIssuer.equals(crlIssuerPrincipal)) {
                return fail("Issuer DN doesn't match. Test failed");
            }
            
            X500Principal testCertSubject = retrievedTestCert.getSubjectX500Principal();
            log.info(testCertSubject.toString());
            log.info("Certificate is issued to: " + testCertSubject.getName());
            if (!testCertSubject.equals(new X500Principal("EMAILADDRESS=dummy@acme.com, CN=John Doe, OU=Engineering Department, O=ACME Software, L=Novosibirsk, ST=Russia, C=RU"))) {
                return fail("Subject DN doesn't match. Test failed");
            }
            
            log.info("Acquire extended key usage of the end-entity certificate");
            List extKeyUsage = retrievedTestCert.getExtendedKeyUsage();
            if (extKeyUsage.size() != 1) {
                return fail("FAILED: number of entities in list of extended key usage must be 1 instead of " + extKeyUsage.size());
            }
            
            log.info("Verify extended key usage value");
            String extKeyUsageStr = extKeyUsage.iterator().next().toString(); 
            if (!extKeyUsageStr.equals(CLINET_AUTH)) {
                return fail("FAILED: wrong value of extended key usage encoutered: " + extKeyUsageStr);
            }            
            log.info("OK");
            
            log.info("Acquire alternative names of this certificate's issuer");
            Collection issuerAltNames = retrievedTestCert.getIssuerAlternativeNames();
            if (issuerAltNames.size() != 1) {
                return fail("FAILED: number of alternative names of the issuer must be 1 instead of " + issuerAltNames.size());
            }
            
            log.info("Verify type and value of the issuer's alternative name");
            List issuerAltNameList = (List)issuerAltNames.iterator().next();
            Iterator it = issuerAltNameList.iterator();
            int nameType = ((Integer)it.next()).intValue();
            String nameValue = (String)it.next();
            if (nameType != RFC822NAME_TYPE) {
                return fail("FAILED: the type of issuer's alternative name must be " + RFC822NAME_TYPE + "instead of " + nameType);                
            }
            
            if (!nameValue.equals(ISSUER_ALT_NAME)) {
                return fail("FAILED: expected issuer's alternative name is " + ISSUER_ALT_NAME + "instead of " + nameValue);
            }
            
            log.info("Acquire alternative name of the subject of this certificate");
            Collection subjectAltNames = retrievedTestCert.getSubjectAlternativeNames();
            if (subjectAltNames.size() != 1) {
                return fail("FAILED: number of alternative names of the subject must be 1 instead of " + subjectAltNames.size());
            }
            
            log.info("Verify type and value of the subject's alternative name");
            subjectAltNameList = (List)subjectAltNames.iterator().next();
            it = subjectAltNameList.iterator();
            nameType = ((Integer)it.next()).intValue();
            nameValue = (String)it.next();
            if (nameType != RFC822NAME_TYPE) {
                return fail("FAILED: the type of issuer's alternative name must be " + RFC822NAME_TYPE + "instead of " + nameType);                
            }
            
            if (!nameValue.equals(SUBJECT_ALT_NAME)) {
                return fail("FAILED: expected issuer's alternative name is " + SUBJECT_ALT_NAME + "instead of " + nameValue);
            }
            
            log.info("Acquire a list of CRLs matching specified criteria");
            Collection crlList = certStore.getCRLs(crlSelector);
            if (crlList.size() == 0) {
                return error("Zero number of certificate revocation lists encountered");
            }
            
            log.info("Get a CRL from the list of CRLs");
            X509CRL retrievedCRL = (X509CRL)crlList.iterator().next();
            if (!retrievedCRL.equals(crl)) {
                return fail("FAILED: two CRLs aren't equal, though they must be.");
            }            
            
            log.info("Compare hashcodes of tho equal objects");
            if (crl.hashCode() != retrievedCRL.hashCode()) {
                return fail("FAILED: hashcodes of tho equal objects aren't equal");
            }

            X500Principal crlIssuer = retrievedCRL.getIssuerX500Principal();
            log.info("Issuer of this CRL: " + crlIssuer.getName());
            if (!crlIssuer.equals(CARootCert.getSubjectX500Principal())) {
                return fail("FAILED: issuer's DN doesn't match");
            }

            log.info("Get revoked certificate from this CRL");
            X509CRLEntry crlEntry = retrievedCRL.getRevokedCertificate(testCert);
            if (crlEntry == null) {
                return fail("FAILED: no specified certificate exists in this CRL, whereas it must be");
            }           
            
            log.info("Compare the issuer of revoked certificate with the one which issued it"); 
            if (crlEntry.getCertificateIssuer() != null) {
                return fail("FAILED: the issuer of revoked certificate is also the CRL issuer, so null result is expected");
            }            
            
        } catch (CertificateException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CRLException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (CertStoreException e) {
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
        System.exit(new F_CertStoreTest_02().test(args));
    }
}
