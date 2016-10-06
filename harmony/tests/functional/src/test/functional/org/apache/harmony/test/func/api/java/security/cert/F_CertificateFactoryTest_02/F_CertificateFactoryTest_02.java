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
package org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_02;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_02.auxiliary.F_CertificateFactoryProvider;
import org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_02.auxiliary.F_CertificateFactorySPImplementation;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 25.08.2005
 */
public class F_CertificateFactoryTest_02 extends ScenarioTest {

    public static final String providerName = "org.apache.harmony.test.func.api.java.security.CertificateFactoryTestProvider";
    public static final double providerVersion = 1.0;
    public static final String providerInfo = "CertificateFactoryTestProvider v1.0 for testing purpose only";
    private String m_CertificateFile;

    F_CertificateFactorySPImplementation certFactorySPI;
    Provider p = null;
    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {

        m_CertificateFile = testArgs[0];
        
        System.setSecurityManager(new SecurityManager());
        try {
            certFactorySPI = new F_CertificateFactorySPImplementation("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
            return fail("Test failed: " + e.getMessage());
        }
        
        if (certFactorySPI == null) {
            return fail("Couldn't create an object of CertificateFactorySPI");
        }
        
        p = new F_CertificateFactoryProvider(providerName, providerVersion, providerInfo);
        if (p == null) {
            return fail("Couldn't create a CertificateFactoryProvider object");
        }
        
        Security.addProvider(p);
        CertificateFactory certFactory = new MyCertificateFactory();
        if (certFactory == null) {
            return fail("Couldn't create CertificateFactory object.");
        }
        
        try {
            FileInputStream fis = new FileInputStream(m_CertificateFile);
            Certificate cert = certFactory.generateCertificate(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return error("Error : " + e.getMessage());
        } catch (CertificateException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        }        
        
        return pass("Test passed");
    }
    
    class MyCertificateFactory extends CertificateFactory {
        public MyCertificateFactory() {
            super(certFactorySPI, p, "X.509");
            System.out.println("MyCertificateFactory ctor has been called");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_CertificateFactoryTest_02().test(args));
    }
}
