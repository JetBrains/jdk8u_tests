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
package org.apache.harmony.test.func.api.java.security.cert.F_CertificateTest_01;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 01.09.2005 
 */
public class F_CertificateTest_01 extends ScenarioTest {

    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {
        String certFileName = testArgs[0];
        MyCertificate cert = null;
        try {
            cert = new MyCertificate(certFileName);            
        } catch (MyTestException e) {
            Exception ex = e.getException();
            if (ex instanceof FileNotFoundException) {
                ex.printStackTrace();
                return error("Couldn't find certificate file : " + ex.getMessage());
            } else {
                ex.printStackTrace();
                return fail(ex.getMessage());
            }
        }
        
        log.info("Certificate version: " + cert.getVersion());
        log.info("Certificate serial number: " + cert.getSerialNumber());
        log.info("Certificate signature algorithm: " + cert.getSigAlgName());
        log.info("Certificate issuer principal: " + cert.getIssuerPrincipal().toString());
        log.info("Certificate subject principal: " + cert.getSubjectPrincipal().toString());
        log.info("Certificate public key: " + cert.getPublicKey().toString());
        try {
            List listExtendedKeyUsage = cert.getExtendedKeyUsage();
            if (listExtendedKeyUsage != null) {
                Iterator it = listExtendedKeyUsage.iterator();
                while (it.hasNext()) {
                    log.info("Extended key usage: " + it.next());
                }
            }
            
            Collection altIssuerNames = cert.getIssuerAlternativeNames();
            if (altIssuerNames != null) {
                Iterator it = altIssuerNames.iterator();
                while (it.hasNext()) {
                    log.info("Alternative issuer name: " + it.next());
                }
            }
            
            Collection altSubjectNames = cert.getSubjectAlternativeNames();
            if (altSubjectNames != null) {
                Iterator it = altSubjectNames.iterator();
                while (it.hasNext()) {
                    log.info("Alternative subject name: " + it.next());
                }
            }
        } catch (CertificateParsingException e) {
            e.printStackTrace();
            return error("Error :" + e.getMessage());
        }
        
        return pass("Test passed");
    }
    
    final class MyCertificate extends Certificate {
        private String m_FileName;
        private MyX509Certificate m_x509Cert;
        public MyCertificate(String fileName) throws MyTestException {
            super("X.509");
            this.m_FileName = fileName;            
            m_x509Cert = new MyX509Certificate(); 
            log.info("MyCertificate ctor has been called");
        }       
        
        class MyX509Certificate extends X509Certificate {
            private X509Certificate m_cert;
            
            public MyX509Certificate() throws MyTestException {
                super();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(m_FileName);
                    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                    m_cert = (X509Certificate)certFactory.generateCertificate(fis);
                } catch (Exception e) {
                    throw new MyTestException(e);
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {                            
                        }
                    }
                }
                
                log.info("MyX509Certificate ctor has been called");
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getSigAlgName()
             */
            public String getSigAlgName() {
                return m_cert.getSigAlgName();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Extension#getExtensionValue(java.lang.String)
             */
            public byte[] getExtensionValue(String arg0) {
                return m_cert.getExtensionValue(arg0);
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getVersion()
             */
            public int getVersion() {
                return m_cert.getVersion();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getIssuerUniqueID()
             */
            public boolean[] getIssuerUniqueID() {
                return m_cert.getIssuerUniqueID();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getNotAfter()
             */
            public Date getNotAfter() {
                return m_cert.getNotAfter();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getNotBefore()
             */
            public Date getNotBefore() {
                return m_cert.getNotBefore();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#checkValidity()
             */
            public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
                m_cert.checkValidity();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#checkValidity(java.util.Date)
             */
            public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
                m_cert.checkValidity(date);
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getBasicConstraints()
             */
            public int getBasicConstraints() {
                return m_cert.getBasicConstraints();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getIssuerDN()
             */
            public Principal getIssuerDN() {
                return m_cert.getIssuerDN();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getKeyUsage()
             */
            public boolean[] getKeyUsage() {
                return m_cert.getKeyUsage();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getSerialNumber()
             */
            public BigInteger getSerialNumber() {
                return m_cert.getSerialNumber();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getSigAlgOID()
             */
            public String getSigAlgOID() {
                return m_cert.getSigAlgOID();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getSigAlgParams()
             */
            public byte[] getSigAlgParams() {
                return m_cert.getSigAlgParams();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getSignature()
             */
            public byte[] getSignature() {
                return m_cert.getSignature();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getSubjectDN()
             */
            public Principal getSubjectDN() {
                return m_cert.getSubjectDN();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getSubjectUniqueID()
             */
            public boolean[] getSubjectUniqueID() {
                return m_cert.getSubjectUniqueID();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getTBSCertificate()
             */
            public byte[] getTBSCertificate() throws CertificateEncodingException {
                return m_cert.getTBSCertificate();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.Certificate#verify(java.security.PublicKey)
             */
            public void verify(PublicKey key) throws CertificateException,
                NoSuchAlgorithmException, InvalidKeyException,
                NoSuchProviderException, SignatureException {
                m_cert.verify(key);
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.Certificate#verify(java.security.PublicKey, java.lang.String)
             */
            public void verify(PublicKey key, String sigProvider)
                throws CertificateException, NoSuchAlgorithmException,
                InvalidKeyException, NoSuchProviderException, SignatureException {
                m_cert.verify(key, sigProvider);
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Extension#getNonCriticalExtensionOIDs()
             */
            public Set getNonCriticalExtensionOIDs() {
                return m_cert.getNonCriticalExtensionOIDs();                
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Extension#getCriticalExtensionOIDs()
             */
            public Set getCriticalExtensionOIDs() {
                return m_cert.getCriticalExtensionOIDs();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Extension#hasUnsupportedCriticalExtension()
             */
            public boolean hasUnsupportedCriticalExtension() {
                return m_cert.hasUnsupportedCriticalExtension();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.Certificate#toString()
             */
            public String toString() {
                return m_cert.toString();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.Certificate#getPublicKey()
             */
            public PublicKey getPublicKey() {
                return m_cert.getPublicKey();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.Certificate#getEncoded()
             */
            public byte[] getEncoded() throws CertificateEncodingException {
                return m_cert.getEncoded();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getIssuerX500Principal()
             */
            public X500Principal getIssuerX500Principal() {
                return m_cert.getIssuerX500Principal();
            }
            
            /* (non-Javadoc)
             * @see java.security.cert.X509Certificate#getSubjectX500Principal()
             */
            public X500Principal getSubjectX500Principal() {
                return m_cert.getSubjectX500Principal();
            }
        }
        
        /* (non-Javadoc)
         * @see java.security.cert.Certificate#getEncoded()
         */
        public byte[] getEncoded() throws CertificateEncodingException {
            return m_x509Cert.getEncoded();
        }
        
        /* (non-Javadoc)
         * @see java.security.cert.Certificate#getPublicKey()
         */
        public PublicKey getPublicKey() {
            return m_x509Cert.getPublicKey();
        }
        
        /* (non-Javadoc)
         * @see java.security.cert.Certificate#verify(java.security.PublicKey)
         */
        public void verify(PublicKey key) throws CertificateException,
            NoSuchAlgorithmException, InvalidKeyException,
            NoSuchProviderException, SignatureException {
            m_x509Cert.verify(key);
        }
        
        /* (non-Javadoc)
         * @see java.security.cert.Certificate#verify(java.security.PublicKey, java.lang.String)
         */
        public void verify(PublicKey key, String sigProvider)
            throws CertificateException, NoSuchAlgorithmException,
            InvalidKeyException, NoSuchProviderException, SignatureException {
            m_x509Cert.verify(key, sigProvider);
        }
        
        /* (non-Javadoc)
         * @see java.security.cert.Certificate#toString()
         */
        public String toString() {
            return m_x509Cert.toString();
        }
        
        public int getVersion() {
            return m_x509Cert.getVersion();
        }
        
        public String getSigAlgName() {
            return m_x509Cert.getSigAlgName();
        }
        
        public byte[] getSignature() {
            return m_x509Cert.getSignature();
        }
        
        public BigInteger getSerialNumber() {
            return m_x509Cert.getSerialNumber();
        } 
        
        public X500Principal getIssuerPrincipal() {
            return m_x509Cert.getIssuerX500Principal();
        }
        
        public X500Principal getSubjectPrincipal() {
            return m_x509Cert.getSubjectX500Principal();
        }
        
        public List getExtendedKeyUsage() throws CertificateParsingException {
            return m_x509Cert.getExtendedKeyUsage();
        }
        
        public Collection getIssuerAlternativeNames() throws CertificateParsingException {
            return m_x509Cert.getIssuerAlternativeNames();
        }
        
        public Collection getSubjectAlternativeNames() throws CertificateParsingException {
            return m_x509Cert.getSubjectAlternativeNames();
        }
    }
    
    final class MyTestException extends RuntimeException {
        private Exception ex;
        public MyTestException(Throwable e) {
            super(e);
            ex = (Exception)e;
            log.info("MyTestException ctor has been called");
        }
        
        public Exception getException() {
            return ex;
        }
    }

    public static void main(String[] args) {
        System.exit(new F_CertificateTest_01().test(args));
    }
}
